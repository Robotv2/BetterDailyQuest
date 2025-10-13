package fr.robotv2.betterdailyquest.storage;

import fr.robotv2.anchor.api.database.Database;
import fr.robotv2.anchor.api.database.SupportType;
import fr.robotv2.anchor.api.repository.MigrationExecutor;
import fr.robotv2.anchor.api.repository.Operator;
import fr.robotv2.anchor.api.repository.Repository;
import fr.robotv2.anchor.api.repository.async.AsyncQueryableRepository;
import fr.robotv2.anchor.api.repository.async.AsyncRepository;
import fr.robotv2.anchor.bukkit.AnchorBukkit;
import fr.robotv2.anchor.sql.repository.SQLRepository;
import fr.robotv2.betterdailyquest.BetterDailyQuest;
import fr.robotv2.betterdailyquest.group.QuestGroup;
import fr.robotv2.betterdailyquest.storage.dto.ActiveQuestDto;
import fr.robotv2.betterdailyquest.storage.dto.ActiveTaskDto;
import fr.robotv2.betterdailyquest.storage.dto.QuestPlayerDto;
import fr.robotv2.betterdailyquest.storage.loader.impl.MonoPlayerLoader;
import fr.robotv2.betterdailyquest.storage.model.ActiveQuest;
import fr.robotv2.betterdailyquest.storage.model.ActiveTask;
import fr.robotv2.betterdailyquest.storage.model.QuestPlayer;
import fr.robotv2.betterdailyquest.util.Futures;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class DatabaseManagerImpl implements DatabaseManager {

    private final BetterDailyQuest plugin;
    private final Database database;

    private final Map<UUID, QuestPlayer> cache;
    private final Map<UUID, ReentrantLock> locks;

    public DatabaseManagerImpl(BetterDailyQuest plugin) {
        this.plugin = plugin;
        this.database = AnchorBukkit.resolveDatabase(plugin, plugin.getConfig().getConfigurationSection("database"));
        if(!database.supports(SupportType.QUERY)) {
            throw new IllegalStateException("The provided database does not support queries. Please use a database that supports queries.");
        }

        this.cache = new ConcurrentHashMap<>();
        this.locks = new ConcurrentHashMap<>();

        Bukkit.getPluginManager().registerEvents(new MonoPlayerLoader(this), plugin);
    }

    @NotNull
    public BetterDailyQuest getPlugin() {
        return plugin;
    }

    @UnmodifiableView
    public Map<UUID, QuestPlayer> getCache() {
        return Collections.unmodifiableMap(cache);
    }

    @Override
    public void init() {
        database.connect();
        initRepository(database.getRepository(QuestPlayerDto.class));
        initRepository(database.getRepository(ActiveQuestDto.class));
        initRepository(database.getRepository(ActiveTaskDto.class));
    }

    private void initRepository(Repository<?, ?> repository) {
        if (repository instanceof SQLRepository<?, ?> sqlRepository) {
            sqlRepository.createTableIfNotExists();
        }

        if(repository instanceof MigrationExecutor migrationExecutor) {
            try {
                migrationExecutor.migrate();
            } catch (Exception exception) {
                BetterDailyQuest.logger().log(Level.WARNING, "Migration failed", exception);
            }
        }
    }

    @Override
    public void close() {
        database.disconnect();
    }

    @Override
    public @NotNull AsyncQueryableRepository<UUID, QuestPlayerDto> getQuestPlayerRepository() {
        return (AsyncQueryableRepository<UUID, QuestPlayerDto>) database.getAsyncRepository(QuestPlayerDto.class);
    }

    @Override
    public @NotNull AsyncQueryableRepository<UUID, ActiveQuestDto> getActiveQuestRepository() {
        return (AsyncQueryableRepository<UUID, ActiveQuestDto>) database.getAsyncRepository(ActiveQuestDto.class);
    }

    @Override
    public @NotNull AsyncQueryableRepository<UUID, ActiveTaskDto> getActiveTaskRepository() {
        return (AsyncQueryableRepository<UUID, ActiveTaskDto>) database.getAsyncRepository(ActiveTaskDto.class);
    }

    private ReentrantLock getLock(UUID playerId) {
        return locks.computeIfAbsent(playerId, (id) -> new ReentrantLock());
    }

    @Override
    public CompletableFuture<Void> savePlayer(Player player, boolean removeFromCache) {
        final UUID playerId = player.getUniqueId();
        final Lock lock = getLock(playerId);

        return CompletableFuture.runAsync(() -> {
            lock.lock();
            try {
                final QuestPlayer questPlayer = cache.get(playerId);
                if (questPlayer == null) return;

                List<CompletableFuture<Void>> futures = new ArrayList<>();
                if(questPlayer.isDirty()) {
                    futures.add(getQuestPlayerRepository().save(new QuestPlayerDto(questPlayer)));
                }

                for (ActiveQuest quest : questPlayer.getActiveQuests()) {
                    if(quest.isDirty()) {
                        futures.add(getActiveQuestRepository().save(new ActiveQuestDto(quest)));
                    }

                    for (ActiveTask task : quest.getTasks()) {
                        if(task.isDirty()) {
                            futures.add(getActiveTaskRepository().save(new ActiveTaskDto(task)));
                        }
                    }
                }

                Futures.ofAll(futures).join();

            } finally {
                lock.unlock(); // Release lock
            }
        }).thenRun(() -> {
            if (removeFromCache) cache.remove(playerId);
            locks.remove(playerId);
            plugin.getLogger().info("Data of player '" + player.getName() + "' saved successfully.");
        });
    }

    @Override
    public CompletableFuture<Void> savePlayers(Player player, boolean removeFromCache) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        if (player != null) {
            futures.add(savePlayer(player, removeFromCache));
        } else {
            for (QuestPlayer questPlayer : getCachedPlayers()) {
                Player p = Bukkit.getPlayer(questPlayer.getId());
                if (p != null) {
                    futures.add(savePlayer(p, removeFromCache));
                }
            }
        }

        return Futures.ofAll(futures);
    }

    @Override
    public CompletableFuture<QuestPlayer> composePlayer(Player player, boolean shouldCache) {
        final UUID playerId = player.getUniqueId();
        final Lock lock = getLock(playerId);
        return CompletableFuture.supplyAsync(() -> {
            lock.lock(); // Acquire lock
            try {
                return populateQuestPlayer(player);
            } finally {
                lock.unlock();
            }
        }).thenCompose((future) -> future).thenApply((questPlayer) -> {
            if (shouldCache) {
                cache.put(player.getUniqueId(), questPlayer);
            }
            return questPlayer;
        });
    }

    private CompletableFuture<QuestPlayer> populateQuestPlayer(Player player) {
        return fetchQuestPlayer(player).thenCompose(this::fetchAndPopulateActiveQuests);
    }

    private CompletableFuture<QuestPlayer> fetchQuestPlayer(Player player) {
        return getQuestPlayerRepository()
                .findById(player.getUniqueId())
                .thenApply(optional -> optional.map(QuestPlayer::new).orElse(new QuestPlayer(player)));
    }

    private CompletableFuture<QuestPlayer> fetchAndPopulateActiveQuests(QuestPlayer questPlayer) {
        if (questPlayer == null) return CompletableFuture.completedFuture(null);
        final CompletableFuture<List<ActiveQuestDto>> dtos = getActiveQuestRepository().query()
                .where("owner_id", Operator.EQUAL, questPlayer.getId())
                .all();
        return dtos.thenCompose((quests) -> populateActiveQuests(questPlayer, quests));
    }

    private CompletableFuture<QuestPlayer> populateActiveQuests(QuestPlayer questPlayer, Collection<ActiveQuestDto> activeQuestDtos) {
        if (activeQuestDtos.isEmpty()) {
            return CompletableFuture.completedFuture(questPlayer);
        }

        final Set<UUID> activeQuestIds = activeQuestDtos.stream().map(ActiveQuestDto::getId).collect(Collectors.toSet());
        final CompletableFuture<List<ActiveTaskDto>> dtos = getActiveTaskRepository().query()
                .where("active_quest_id", Operator.IN, activeQuestIds)
                .all();
        final CompletableFuture<Map<UUID, List<ActiveTaskDto>>> taskMap = dtos.thenApply((tasks) ->
            tasks.stream().collect(Collectors.groupingBy(ActiveTaskDto::getParentActiveQuestId))
        );

        return taskMap.thenApply((map) -> {
            for (ActiveQuestDto dto : activeQuestDtos) {
                final List<ActiveTask> tasks = map.getOrDefault(dto.getId(), Collections.emptyList()).stream().map(ActiveTask::new).toList();
                final ActiveQuest activeQuest = new ActiveQuest(dto, tasks);
                questPlayer.addActiveQuest(activeQuest);
            }
            return questPlayer;
        });
    }

    @Nullable
    public QuestPlayer getCachedQuestPlayer(Player player) {
        return getCachedQuestPlayer(player.getUniqueId());
    }

    @Nullable
    public QuestPlayer getCachedQuestPlayer(UUID playerId) {
        return cache.get(playerId);
    }

    @UnmodifiableView
    public Collection<QuestPlayer> getCachedPlayers() {
        return Collections.unmodifiableCollection(cache.values());
    }

    @Override
    public CompletableFuture<Void> removeQuests(QuestGroup group) {
        return CompletableFuture.allOf(
            getActiveQuestRepository().query().where("group_id", Operator.EQUAL, group.getGroupId()).delete(),
            getActiveTaskRepository().query().where("parent_quest_group_id", Operator.EQUAL, group.getGroupId()).delete()
        );
    }

    @Override
    public CompletableFuture<Void> removeQuests(QuestPlayer player) {
        final List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (ActiveQuest active : player.getActiveQuests()) {
            futures.add(removeQuestsAndTasks(active));
            player.removeActiveQuestById(active.getQuestId());
        }
        return Futures.ofAll(futures);
    }

    @Override
    public CompletableFuture<Void> removeQuests(QuestPlayer player, QuestGroup group) {
        final List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (ActiveQuest active : player.getActiveQuests(group)) {
            futures.add(removeQuestsAndTasks(active));
            player.removeActiveQuestById(active.getQuestId());
        }
        return Futures.ofAll(futures);
    }

    @Override
    public CompletableFuture<Void> removeQuestsIfEnded(QuestPlayer player) {
        final List<CompletableFuture<Void>> futures = new ArrayList<>();
        final List<String> ended = new ArrayList<>();

        for (ActiveQuest active : player.getActiveQuests()) {
            if (active.hasEnded()) {
                futures.add(removeQuestsAndTasks(active));
                ended.add(active.getQuestId());
            }
        }

        for(String questId : ended) {
            player.removeActiveQuestById(questId);
        }

        return Futures.ofAll(futures);
    }

    @Override
    public CompletableFuture<Void> removeQuestsAndTasks(ActiveQuest quest) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        futures.add(getActiveQuestRepository().deleteById(quest.getUID()));
        futures.addAll(quest.getTasks().stream().map(task -> getActiveTaskRepository().deleteById(task.getId())).toList());
        return Futures.ofAll(futures);
    }
}
