package fr.robotv2.betterdailyquest.quest;

import fr.robotv2.betterdailyquest.configurations.messages.MessageConfiguration;
import fr.robotv2.betterdailyquest.event.QuestStartEvent;
import fr.robotv2.betterdailyquest.storage.DatabaseManager;
import fr.robotv2.betterdailyquest.storage.model.ActiveQuest;
import fr.robotv2.betterdailyquest.storage.model.QuestPlayer;
import fr.robotv2.betterdailyquest.util.color.ColorProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class QuestAssignmentStarter {

    private final DatabaseManager databaseManager;
    private final Supplier<MessageConfiguration.CommandMessages> messages;
    private final ColorProvider colorProvider;
    private final Consumer<Event> eventDispatcher;

    public QuestAssignmentStarter(DatabaseManager databaseManager,
                                  MessageConfiguration.CommandMessages messages,
                                  ColorProvider colorProvider,
                                  Consumer<Event> eventDispatcher) {
        this.databaseManager = databaseManager;
        this.messages = () -> messages;
        this.colorProvider = colorProvider;
        this.eventDispatcher = eventDispatcher;
    }

    public QuestAssignmentStarter(DatabaseManager databaseManager,
                                  Supplier<MessageConfiguration.CommandMessages> messages,
                                  ColorProvider colorProvider,
                                  Consumer<Event> eventDispatcher) {
        this.databaseManager = databaseManager;
        this.messages = messages;
        this.colorProvider = colorProvider;
        this.eventDispatcher = eventDispatcher;
    }

    public boolean start(CommandSender sender, Player target, String questId, boolean isOthers) {
        MessageConfiguration.CommandMessages messages = this.messages.get();
        final QuestPlayer questPlayer = databaseManager.getCachedQuestPlayer(target);
        if(questPlayer == null) {
            sender.sendMessage(colorProvider.colorize(messages.getPlayerNotLoaded()));
            return false;
        }

        final ActiveQuest activeQuest = questPlayer.getActiveQuest(questId);
        if(activeQuest == null) {
            sender.sendMessage(colorProvider.colorize(messages.getQuestNotFound()));
            return false;
        }

        if(activeQuest.isDone()) {
            sender.sendMessage(colorProvider.colorize(messages.getQuestAlreadyCompleted()));
            return false;
        }

        if(activeQuest.isStarted()) {
            sender.sendMessage(colorProvider.colorize(messages.getQuestAlreadyStarted()));
            return false;
        }

        final Quest quest = activeQuest.getQuest();
        if(quest == null) {
            sender.sendMessage(colorProvider.colorize(messages.getQuestUnavailable()
                    .replace("%quest_id%", activeQuest.getQuestId())));
            return false;
        }

        activeQuest.setStarted(true);
        eventDispatcher.accept(new QuestStartEvent(quest, activeQuest, target));

        sender.sendMessage(colorProvider.colorize((isOthers ? messages.getStartSuccessOthers() : messages.getStartSuccessSelf())
                .replace("%quest_id%", activeQuest.getQuestId())
                .replace("%player%", target.getName())));
        return true;
    }
}
