package fr.robotv2.betterdailyquest.command;

import fr.robotv2.betterdailyquest.BetterDailyQuest;
import fr.robotv2.betterdailyquest.configurations.messages.MessageConfiguration;
import fr.robotv2.betterdailyquest.event.QuestDoneEvent;
import fr.robotv2.betterdailyquest.event.QuestStartEvent;
import fr.robotv2.betterdailyquest.event.TaskDoneEvent;
import fr.robotv2.betterdailyquest.group.QuestGroup;
import fr.robotv2.betterdailyquest.quest.Quest;
import fr.robotv2.betterdailyquest.storage.DatabaseManager;
import fr.robotv2.betterdailyquest.storage.model.ActiveQuest;
import fr.robotv2.betterdailyquest.storage.model.QuestPlayer;
import fr.robotv2.betterdailyquest.util.color.ColorProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.function.Consumer;

@Command({"betterdailyquest", "bdq"})
public class BetterDailyQuestCommand {

    private final BetterDailyQuest plugin;

    public BetterDailyQuestCommand(BetterDailyQuest plugin) {
        this.plugin = plugin;
    }

    @DefaultFor({"betterdailyquest", "bdq"})
    public void onDefault(BukkitCommandActor actor) {
        actor.getSender().sendMessage(ChatColor.GREEN + "This server is using BetterDailyQuest with version " + plugin.getDescription().getVersion() + ".");
    }

    @Subcommand("reload")
    @CommandPermission("betterdailyquest.command.reload")
    public void onReload(BukkitCommandActor actor) {
        plugin.onReload();
        MessageConfiguration.CommandMessages messages = plugin.getQuestConfiguration().getMessageConfiguration().getCommandMessages();
        actor.getSender().sendMessage(plugin.getColorProvider().colorize(messages.getReloadSuccess()));
    }

    @Subcommand("give")
    @CommandPermission("betterdailyquest.command.give")
    public void onGive(BukkitCommandActor actor, @Named("group") QuestGroup group, @Named("quest") Quest quest, @Named("target") @Default("me") Player target) {
        final QuestPlayer questPlayer = plugin.getDatabaseManager().getCachedQuestPlayer(target);
        MessageConfiguration.CommandMessages messages = plugin.getQuestConfiguration().getMessageConfiguration().getCommandMessages();

        if(questPlayer == null) {
            actor.getSender().sendMessage(plugin.getColorProvider().colorize(messages.getPlayerNotLoaded()));
            return;
        }

        if(questPlayer.hasQuest(quest)) {
            actor.getSender().sendMessage(plugin.getColorProvider().colorize(messages.getQuestAlreadyHas()));
            return;
        }

        if(!questPlayer.canReceiveQuest(quest)) {
            actor.getSender().sendMessage(plugin.getColorProvider().colorize(messages.getQuestAlreadyCompleted()));
            return;
        }

        questPlayer.addActiveQuest(new ActiveQuest(target, quest));
        String successMessage = messages.getGiveSuccess()
                .replace("%quest_id%", quest.getQuestId())
                .replace("%player%", target.getName());
        actor.getSender().sendMessage(plugin.getColorProvider().colorize(successMessage));
    }

    @Subcommand("clear")
    @CommandPermission("betterdailyquest.command.clear")
    @AutoComplete("@players @target_quests")
    public void onClear(BukkitCommandActor actor, @Named("target") Player player, @Named("questID") String questID) {
        final QuestPlayer questPlayer = plugin.getDatabaseManager().getCachedQuestPlayer(player);
        MessageConfiguration.CommandMessages messages = plugin.getQuestConfiguration().getMessageConfiguration().getCommandMessages();

        if(questPlayer == null) {
            actor.getSender().sendMessage(plugin.getColorProvider().colorize(messages.getPlayerNotLoaded()));
            return;
        }

        final ActiveQuest activeQuest = questPlayer.getActiveQuest(questID);
        if(activeQuest == null) {
            actor.getSender().sendMessage(plugin.getColorProvider().colorize(messages.getQuestNotFound()));
            return;
        }

        plugin.getDatabaseManager().removeQuestsAndTasks(activeQuest);
        questPlayer.removeActiveQuest(activeQuest);

        String successMessage = messages.getClearSuccess()
                .replace("%quest_id%", activeQuest.getQuestId())
                .replace("%player%", player.getName());
        actor.getSender().sendMessage(plugin.getColorProvider().colorize(successMessage));
    }

    @Subcommand("reset")
    @CommandPermission("betterdailyquest.command.reset")
    @AutoComplete("@players @target_quests")
    public void onReset(BukkitCommandActor actor, @Named("target") Player target, String questID) {
        // Backward-compatible command name: restarts the same quest assignment, not a reroll.
        final QuestPlayer questPlayer = plugin.getDatabaseManager().getCachedQuestPlayer(target);
        MessageConfiguration.CommandMessages messages = plugin.getQuestConfiguration().getMessageConfiguration().getCommandMessages();

        if(questPlayer == null) {
            actor.getSender().sendMessage(plugin.getColorProvider().colorize(messages.getPlayerNotLoaded()));
            return;
        }

        final ActiveQuest activeQuest = questPlayer.getActiveQuest(questID);
        if(activeQuest == null) {
            actor.getSender().sendMessage(plugin.getColorProvider().colorize(messages.getQuestNotFound()));
            return;
        }

        if(activeQuest.getQuest() == null) {
            String unavailableMessage = messages.getQuestUnavailable()
                    .replace("%quest_id%", activeQuest.getQuestId());
            actor.getSender().sendMessage(plugin.getColorProvider().colorize(unavailableMessage));
            return;
        }

        plugin.getDatabaseManager().removeQuestsAndTasks(activeQuest);
        questPlayer.removeActiveQuest(activeQuest);

        final Quest quest = activeQuest.getQuest();
        if(quest != null) {
            questPlayer.addActiveQuest(new ActiveQuest(target, quest));
        } else {
            String unavailableMessage = messages.getQuestUnavailable()
                    .replace("%quest_id%", activeQuest.getQuestId());
            actor.getSender().sendMessage(plugin.getColorProvider().colorize(unavailableMessage));
            return;
        }

        String successMessage = messages.getResetSuccess()
                .replace("%quest_id%", quest.getQuestId())
                .replace("%player%", target.getName());
        actor.getSender().sendMessage(plugin.getColorProvider().colorize(successMessage));
    }

    @Subcommand("start")
    @CommandPermission("betterdailyquest.command.start")
    @AutoComplete("@waiting_target_quests")
    public void onStart(BukkitCommandActor actor, @Named("questID") String questID) {
        handleStart(actor, actor.requirePlayer(), questID, false);
    }

    @Subcommand("start-others")
    @CommandPermission("betterdailyquest.command.start.others")
    @AutoComplete("@players @waiting_target_quests")
    public void onStartOthers(BukkitCommandActor actor, @Named("target") Player target, @Named("questID") String questID) {
        handleStart(actor, target, questID, true);
    }

    private void handleStart(BukkitCommandActor actor, Player target, String questID, boolean isOthers) {
        MessageConfiguration.CommandMessages messages = plugin.getQuestConfiguration().getMessageConfiguration().getCommandMessages();
        handleStart(
                actor.getSender(),
                target,
                questID,
                isOthers,
                plugin.getDatabaseManager(),
                messages,
                plugin.getColorProvider(),
                event -> Bukkit.getPluginManager().callEvent(event)
        );
    }

    static void handleStart(CommandSender sender, Player target, String questID, boolean isOthers,
                            DatabaseManager databaseManager, MessageConfiguration.CommandMessages messages,
                            ColorProvider colorProvider, Consumer<Event> eventDispatcher) {
        final QuestPlayer questPlayer = databaseManager.getCachedQuestPlayer(target);
        if(questPlayer == null) {
            sender.sendMessage(colorProvider.colorize(messages.getPlayerNotLoaded()));
            return;
        }

        final ActiveQuest activeQuest = questPlayer.getActiveQuest(questID);
        if(activeQuest == null) {
            sender.sendMessage(colorProvider.colorize(messages.getQuestNotFound()));
            return;
        }

        if(activeQuest.isDone()) {
            sender.sendMessage(colorProvider.colorize(messages.getQuestAlreadyCompleted()));
            return;
        }

        if(activeQuest.isStarted()) {
            sender.sendMessage(colorProvider.colorize(messages.getQuestAlreadyStarted()));
            return;
        }

        final Quest quest = activeQuest.getQuest();
        if(quest == null) {
            String unavailableMessage = messages.getQuestUnavailable()
                    .replace("%quest_id%", activeQuest.getQuestId());
            sender.sendMessage(colorProvider.colorize(unavailableMessage));
            return;
        }

        activeQuest.setStarted(true);
        eventDispatcher.accept(new QuestStartEvent(quest, activeQuest, target));

        String successMessage = (isOthers ? messages.getStartSuccessOthers() : messages.getStartSuccessSelf())
                .replace("%quest_id%", activeQuest.getQuestId())
                .replace("%player%", target.getName());
        sender.sendMessage(colorProvider.colorize(successMessage));
    }

    @Subcommand("reroll")
    @CommandPermission("betterdailyquest.command.reroll")
    @AutoComplete("@target_quests")
    public void onRerollSelf(BukkitCommandActor actor, String questID) {
        final Player target = actor.requirePlayer();
        handleReroll(actor, questID, target, false);
    }

    @Subcommand("reroll-others")
    @CommandPermission("betterdailyquest.command.reroll.others")
    @AutoComplete("@players @target_quests")
    public void onRerollOthers(BukkitCommandActor actor, Player player, String questID) {
        MessageConfiguration.CommandMessages messages = plugin.getQuestConfiguration().getMessageConfiguration().getCommandMessages();
        if (player == null) {
            actor.getSender().sendMessage(plugin.getColorProvider().colorize(messages.getSpecifyPlayerForReroll()));
            return;
        }

        handleReroll(actor, questID, player, true);
    }

    private void handleReroll(BukkitCommandActor actor, String questID, Player target, boolean isOthers) {
        // Reroll replaces the active assignment with a different quest from the same group.
        final QuestPlayer questPlayer = plugin.getDatabaseManager().getCachedQuestPlayer(target);
        MessageConfiguration.CommandMessages messages = plugin.getQuestConfiguration().getMessageConfiguration().getCommandMessages();

        if (questPlayer == null) {
            actor.getSender().sendMessage(plugin.getColorProvider().colorize(messages.getPlayerNotLoaded()));
            return;
        }

        final ActiveQuest activeQuest = questPlayer.getActiveQuest(questID);
        if (activeQuest == null) {
            actor.getSender().sendMessage(plugin.getColorProvider().colorize(messages.getQuestNotFound()));
            return;
        }

        int currentRerollCount = activeQuest.getRerollCount();

        if (activeQuest.getQuest() == null) {
            String unavailableMessage = messages.getQuestUnavailable()
                    .replace("%quest_id%", activeQuest.getQuestId());
            actor.getSender().sendMessage(plugin.getColorProvider().colorize(unavailableMessage));
            return;
        }

        final QuestGroup group = activeQuest.getQuest().getQuestGroup();
        if(group.getMaxRerolls() > 0 && currentRerollCount >= group.getMaxRerolls()) {
            actor.getSender().sendMessage(plugin.getColorProvider().colorize(messages.getMaxRerollsReached()));
            return;
        }

        final Quest newQuest = plugin.getQuestManager().getRandomQuest(questPlayer, activeQuest.getQuest().getQuestGroup());
        if (newQuest == null) {
            actor.getSender().sendMessage(plugin.getColorProvider().colorize(messages.getNoQuestAvailable()));
            return;
        }

        questPlayer.removeActiveQuest(activeQuest);

        final ActiveQuest newActiveQuest = new ActiveQuest(target, newQuest);
        newActiveQuest.setRerollCount(currentRerollCount + 1);
        questPlayer.addActiveQuest(newActiveQuest);

        if (isOthers) {
            String successMessage = messages.getRerollSuccessOthers()
                    .replace("%quest_id%", activeQuest.getQuestId())
                    .replace("%player%", target.getName());
            actor.getSender().sendMessage(plugin.getColorProvider().colorize(successMessage));
        } else {
            String successMessage = messages.getRerollSuccessSelf()
                    .replace("%quest_id%", activeQuest.getQuestId());
            actor.getSender().sendMessage(plugin.getColorProvider().colorize(successMessage));
        }
    }

    @Subcommand("complete")
    @CommandPermission("betterdailyquest.command.complete")
    @AutoComplete("@players @target_quests")
    public void onComplete(BukkitCommandActor actor, @Named("target") Player target, String questID) {
        final QuestPlayer questPlayer = plugin.getDatabaseManager().getCachedQuestPlayer(target);
        MessageConfiguration.CommandMessages messages = plugin.getQuestConfiguration().getMessageConfiguration().getCommandMessages();

        if(questPlayer == null) {
            actor.getSender().sendMessage(plugin.getColorProvider().colorize(messages.getPlayerNotLoaded()));
            return;
        }

        final ActiveQuest activeQuest = questPlayer.getActiveQuest(questID);
        if(activeQuest == null) {
            actor.getSender().sendMessage(plugin.getColorProvider().colorize(messages.getQuestNotFound()));
            return;
        }

        if(activeQuest.isDone()) {
            actor.getSender().sendMessage(plugin.getColorProvider().colorize(messages.getQuestAlreadyCompleted()));
            return;
        }

        final Quest quest = activeQuest.getQuest();
        if(quest == null) {
            String unavailableMessage = messages.getQuestUnavailable()
                    .replace("%quest_id%", activeQuest.getQuestId());
            actor.getSender().sendMessage(plugin.getColorProvider().colorize(unavailableMessage));
            return;
        }

        activeQuest.getTasks().forEach((task) -> {
            if(task.isDone()) {
                return;
            }

            task.setDone(true);
            Bukkit.getPluginManager().callEvent(new TaskDoneEvent(target, quest, quest.getTask(task.getTaskId()), task));

            if(activeQuest.isDone()) {
                questPlayer.recordQuestCompletion(activeQuest.getQuestId());
                Bukkit.getPluginManager().callEvent(new QuestDoneEvent(quest, activeQuest, target));
            }
        });

        String successMessage = messages.getCompleteSuccess()
                .replace("%quest_id%", activeQuest.getQuestId())
                .replace("%player%", target.getName());
        actor.getSender().sendMessage(plugin.getColorProvider().colorize(successMessage));
    }
}
