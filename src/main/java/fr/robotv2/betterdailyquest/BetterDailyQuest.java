package fr.robotv2.betterdailyquest;

import fr.robotv2.anchor.bukkit.AnchorBukkit;
import fr.robotv2.betterdailyquest.addons.Addon;
import fr.robotv2.betterdailyquest.addons.AddonManager;
import fr.robotv2.betterdailyquest.command.BetterDailyQuestCommand;
import fr.robotv2.betterdailyquest.conditions.ConditionManager;
import fr.robotv2.betterdailyquest.group.QuestGroup;
import fr.robotv2.betterdailyquest.group.QuestGroupManager;
import fr.robotv2.betterdailyquest.listeners.QuestCosmeticListener;
import fr.robotv2.betterdailyquest.listeners.StatisticListeners;
import fr.robotv2.betterdailyquest.quest.Quest;
import fr.robotv2.betterdailyquest.quest.QuestAssignmentStarter;
import fr.robotv2.betterdailyquest.quest.QuestManager;
import fr.robotv2.betterdailyquest.questboard.QuestBoardListener;
import fr.robotv2.betterdailyquest.quest.type.QuestTypes;
import fr.robotv2.betterdailyquest.storage.DatabaseManager;
import fr.robotv2.betterdailyquest.storage.DatabaseManagerImpl;
import fr.robotv2.betterdailyquest.storage.model.ActiveQuest;
import fr.robotv2.betterdailyquest.storage.model.QuestPlayer;
import fr.robotv2.betterdailyquest.util.FileUtil;
import fr.robotv2.betterdailyquest.util.Futures;
import fr.robotv2.betterdailyquest.util.GroupUtil;
import fr.robotv2.betterdailyquest.util.McVersion;
import fr.robotv2.betterdailyquest.util.color.ColorProvider;
import fr.robotv2.betterdailyquest.util.color.LegacyColorProvider;
import fr.robotv2.betterdailyquest.util.color.ModernColorProvider;
import fr.robotv2.placeholderannotationlib.api.PlaceholderAnnotationProcessor;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.BukkitCommandHandler;
import revxrsal.zapper.ZapperJavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class BetterDailyQuest extends ZapperJavaPlugin {

    private QuestManager questManager;
    private QuestGroupManager questGroupManager;
    private DatabaseManager databaseManager;
    private BukkitCommandHandler commandHandler;

    private BetterDailyQuestConfiguration questConfiguration;
    private QuestResetHandler resetHandler;
    private ConditionManager conditionManager;
    private AddonManager addonManager;
    private QuestAssignmentStarter questAssignmentStarter;

    private ColorProvider colorProvider;
    private File libsFolder;

    public static BetterDailyQuest instance() {
        return JavaPlugin.getPlugin(BetterDailyQuest.class);
    }

    public static Logger logger() {
        return instance().getLogger();
    }

    public static void debug(String message) {
        instance().dbg(message);
    }

    @Override
    public void onLoad() {
        this.addonManager = new AddonManager(this, getRelativeFile("addons"));
        this.addonManager.load();
        this.addonManager.getAddons().forEach(Addon::onLoad);

        this.conditionManager = new ConditionManager(this);
        this.conditionManager.registerDefaultConditions();
    }

    @Override
    public void onEnable() {
        if(!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        this.conditionManager.closeRegistration();
        this.addonManager.getAddons().forEach(Addon::onPreEnable);

        saveDefaultConfig();
        this.questConfiguration = new BetterDailyQuestConfiguration();
        getQuestConfiguration().loadConfiguration(getConfig());

        Metrics metrics = new Metrics(this, 32844);
        metrics.addCustomChart(new SimplePie(
                "database_type",
                () -> getConfig().getString("database.type", "UNKNOWN").toUpperCase(Locale.ROOT)
        ));

        this.conditionManager = new ConditionManager(this);

        registerDatabaseManager();

        this.questGroupManager = QuestGroupManager.load(this, getRelativeFile("groups"));
        this.questManager = QuestManager.load(this, getRelativeFile("quests"), getQuestGroupManager());
        logContentErrors("startup", getQuestGroupManager().getErrors(), getQuestManager().getErrors());
        getDatabaseManager().init();

        this.resetHandler = new QuestResetHandler(this);
        getQuestGroupManager().startCronJobs();
        this.colorProvider = McVersion.current().isAtLeast(1, 17) ? new ModernColorProvider() : new LegacyColorProvider();
        getQuestConfiguration().getQuestBoardConfiguration().validateGroups(getQuestGroupManager().getGroups());
        getQuestConfiguration().getQuestBoardConfiguration().logErrors(getLogger());
        this.questAssignmentStarter = new QuestAssignmentStarter(
                getDatabaseManager(),
                () -> getQuestConfiguration().getMessageConfiguration().getCommandMessages(),
                getColorProvider(),
                event -> Bukkit.getPluginManager().callEvent(event)
        );

        registerListeners();
        registerCommands();

        GroupUtil.initialize(this);

        // load addons placeholders
        this.addonManager.getAddons().forEach(Addon::onPostEnable);

        // hide .libs folder
        try {
            this.libsFolder = new File(getDataFolder(), ".libs");
            FileUtil.hideFolder(libsFolder);
        } catch (IOException exception) {
            getLogger().log(Level.WARNING, "Failed to hide .libs folder", exception);
        }

        // load placeholderapi
        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            final PlaceholderAnnotationProcessor processor = PlaceholderAnnotationProcessor.defaultProcessor();
            final BetterDailyQuestClipPlaceholder expansion = new BetterDailyQuestClipPlaceholder(this, processor);
            expansion.register();
        }
    }

    @Override
    public void onDisable() {

        // stop group schedulers before closing plugin resources
        getQuestGroupManager().stopCronJobs();

        // disable addons
        getAddonManager().getAddons().forEach(Addon::onDisable);

        // save all players
        List<CompletableFuture<Void>> futures = Bukkit.getOnlinePlayers()
                .stream()
                .map(player -> getDatabaseManager().savePlayer(player, true))
                .collect(Collectors.toList()
                );

        // Wait for them to complete before closing the pool
        Futures.ofAll(futures).join();

        // Close the database
        if(getDatabaseManager() != null) {
            getDatabaseManager().close();
        }
    }

    public boolean onReload() {
        final BetterDailyQuestConfiguration candidateConfiguration;
        try {
            candidateConfiguration = loadCandidateConfiguration();
        } catch (IOException | InvalidConfigurationException | RuntimeException exception) {
            getLogger().log(Level.WARNING, "Reload rejected: config.yml is invalid. The previous configuration remains active.", exception);
            return false;
        }

        final QuestGroupManager candidateGroups = QuestGroupManager.load(this, getRelativeFile("groups"));
        final QuestManager candidateQuests = QuestManager.load(this, getRelativeFile("quests"), candidateGroups);
        candidateConfiguration.getQuestBoardConfiguration().validateGroups(candidateGroups.getGroups());

        final List<String> errors = new ArrayList<>();
        errors.addAll(candidateGroups.getErrors());
        errors.addAll(candidateQuests.getErrors());
        errors.addAll(candidateConfiguration.getQuestBoardConfiguration().getErrors());
        if(!errors.isEmpty()) {
            logContentErrors("reload", errors);
            getLogger().warning("Reload rejected. The previous configuration remains active.");
            return false;
        }

        getQuestGroupManager().stopCronJobs();
        try {
            candidateGroups.startCronJobs();
        } catch (RuntimeException exception) {
            try {
                getQuestGroupManager().startCronJobs();
            } catch (RuntimeException rollbackException) {
                exception.addSuppressed(rollbackException);
            }
            getLogger().log(Level.SEVERE, "Reload rejected because the new schedules could not start. The previous schedules were restored.", exception);
            return false;
        }

        this.questGroupManager = candidateGroups;
        this.questManager = candidateQuests;
        this.questConfiguration = candidateConfiguration;
        reloadConfig();

        for(Addon addon : getAddonManager().getAddons()) {
            try {
                addon.onReload();
            } catch (RuntimeException exception) {
                getLogger().log(Level.SEVERE, "Addon '" + addon.getName() + "' failed its reload hook after the core configuration was reloaded.", exception);
            }
        }
        return true;
    }

    private BetterDailyQuestConfiguration loadCandidateConfiguration() throws IOException, InvalidConfigurationException {
        final YamlConfiguration configuration = new YamlConfiguration();
        configuration.load(new File(getDataFolder(), "config.yml"));

        try(InputStream defaultsStream = getResource("config.yml")) {
            if(defaultsStream != null) {
                final YamlConfiguration defaults = YamlConfiguration.loadConfiguration(
                        new InputStreamReader(defaultsStream, StandardCharsets.UTF_8)
                );
                configuration.setDefaults(defaults);
            }
        }

        final BetterDailyQuestConfiguration candidate = new BetterDailyQuestConfiguration();
        candidate.loadConfiguration(configuration);
        return candidate;
    }

    @SafeVarargs
    private final void logContentErrors(String phase, List<String>... errorLists) {
        for(List<String> errors : errorLists) {
            for(String error : errors) {
                getLogger().warning("Invalid content during " + phase + ": " + error);
            }
        }
    }

    public void dbg(String message) {
        if(getQuestConfiguration().isDebug()) {
            getLogger().info("[DEBUG] " + message);
        }
    }

    public QuestManager getQuestManager() {
        return questManager;
    }

    public QuestGroupManager getQuestGroupManager() {
        return questGroupManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public BetterDailyQuestConfiguration getQuestConfiguration() {
        return questConfiguration;
    }

    public QuestResetHandler getResetHandler() {
        return resetHandler;
    }

    public AddonManager getAddonManager() {
        return addonManager;
    }

    public File getRelativeFile(String path) {
        return new File(getDataFolder(), path);
    }

    public File getQuestDataFolder() {
        return getRelativeFile("data");
    }

    public ColorProvider getColorProvider() {
        return colorProvider;
    }

    public ConditionManager getConditionManager() {
        return conditionManager;
    }

    public QuestAssignmentStarter getQuestAssignmentStarter() {
        return questAssignmentStarter;
    }

    public BukkitCommandHandler getCommandHandler() {
        return commandHandler;
    }

    private void registerDatabaseManager() {
        try {
            this.databaseManager = new DatabaseManagerImpl(this);
        } catch (Exception exception) {
            getLogger().log(Level.SEVERE, "Failed to initialize database manager", exception);
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private void registerListeners() {
        final PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new QuestCosmeticListener(this), this);
        pm.registerEvents(new StatisticListeners(), this);
        pm.registerEvents(new QuestBoardListener(this), this);

        QuestTypes.getLoadedTypes().forEach((type) -> {
            try {
                type.registerListener();
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException exception) {
                getLogger().log(Level.SEVERE, "Failed to register listener for " + type.getLiteral(), exception);
            }
        });
    }

    private void registerCommands() {
        commandHandler = BukkitCommandHandler.create(this);

        commandHandler.registerValueResolver(QuestGroup.class, (context) -> getQuestGroupManager().getGroup(context.pop()));
        commandHandler.getAutoCompleter().registerSuggestion("groups", SuggestionProvider.map(() -> getQuestGroupManager().getGroups(), QuestGroup::getGroupId));
        commandHandler.getAutoCompleter().registerParameterSuggestions(QuestGroup.class, "groups");

        commandHandler.registerValueResolver(Quest.class, (context) -> {
            final QuestGroup group = context.getResolvedArgument(QuestGroup.class);
            return getQuestManager().fromId(context.pop(), group.getGroupId());
        });

        commandHandler.getAutoCompleter().registerSuggestion("quests", (args, sender, command) -> {
            final String filter = args.size() > 1 ? args.get(args.size() - 2) : "";
            return getQuestManager().getQuests().stream()
                    .filter((quest) -> quest.getQuestGroup().getGroupId().equalsIgnoreCase(filter))
                    .map(Quest::getQuestId)
                    .collect(Collectors.toSet());
        });

        commandHandler.getAutoCompleter().registerSuggestion("target_quests", (args, sender, command) -> {
            final String filter = args.size() > 1 ? args.get(args.size() - 2) : "";
            Player player = Bukkit.getPlayer(filter);
            if(player == null) player = sender.as(BukkitCommandActor.class).requirePlayer();
            final QuestPlayer questPlayer = getDatabaseManager().getCachedQuestPlayer(player);
            if(questPlayer == null) return Collections.emptySet();
            return questPlayer.getActiveQuests().stream()
                    .map(ActiveQuest::getQuestId)
                    .collect(Collectors.toSet());
        });

        commandHandler.getAutoCompleter().registerSuggestion("waiting_target_quests", (args, sender, command) -> {
            final String filter = args.size() > 1 ? args.get(args.size() - 2) : "";
            Player player = Bukkit.getPlayer(filter);
            if(player == null) player = sender.as(BukkitCommandActor.class).requirePlayer();
            final QuestPlayer questPlayer = getDatabaseManager().getCachedQuestPlayer(player);
            if(questPlayer == null) return Collections.emptySet();
            return questPlayer.getActiveQuests().stream()
                    .filter(activeQuest -> !activeQuest.isStarted() && !activeQuest.isDone())
                    .map(ActiveQuest::getQuestId)
                    .collect(Collectors.toSet());
        });

        commandHandler.getAutoCompleter().registerParameterSuggestions(Quest.class, "quests");

        commandHandler.register(new BetterDailyQuestCommand(this));
    }
}
