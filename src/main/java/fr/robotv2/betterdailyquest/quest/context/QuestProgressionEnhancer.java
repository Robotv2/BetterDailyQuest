package fr.robotv2.betterdailyquest.quest.context;

import fr.robotv2.betterdailyquest.BetterDailyQuest;
import fr.robotv2.betterdailyquest.conditions.Condition;
import fr.robotv2.betterdailyquest.event.QuestDoneEvent;
import fr.robotv2.betterdailyquest.event.TaskDoneEvent;
import fr.robotv2.betterdailyquest.event.TaskIncrementEvent;
import fr.robotv2.betterdailyquest.quest.Quest;
import fr.robotv2.betterdailyquest.quest.enums.QuestStatus;
import fr.robotv2.betterdailyquest.quest.options.Optionnable;
import fr.robotv2.betterdailyquest.quest.task.Task;
import fr.robotv2.betterdailyquest.storage.model.ActiveQuest;
import fr.robotv2.betterdailyquest.storage.model.ActiveTask;
import fr.robotv2.betterdailyquest.storage.model.QuestPlayer;
import fr.robotv2.betterdailyquest.util.placeholder.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.Collection;
import java.util.Objects;

public abstract class QuestProgressionEnhancer implements Listener {

    private final BetterDailyQuest plugin;

    protected QuestProgressionEnhancer(BetterDailyQuest plugin) {
        this.plugin = plugin;
    }

    public void updateQuestProgress(RunningQuestContext<?, ?> context) {
        final QuestPlayer questPlayer = plugin.getDatabaseManager().getCachedQuestPlayer(context.getInitiator());

        if (questPlayer == null) {
            return;
        }

        for (ActiveQuest activeQuest : questPlayer.getActiveQuests()) {
            updateQuestProgressFor(questPlayer, activeQuest, context);
        }
    }

    public void updateQuestProgressFor(ActiveQuest activeQuest, RunningQuestContext<?, ?> context) {
        final QuestPlayer questPlayer = plugin.getDatabaseManager().getCachedQuestPlayer(activeQuest.getOwner());
        updateQuestProgressFor(questPlayer, activeQuest, context);
    }

    private void updateQuestProgressFor(QuestPlayer questPlayer, ActiveQuest activeQuest, RunningQuestContext<?, ?> context) {
        if(activeQuest.getStatus() != QuestStatus.STARTED) {
            return; // quest is not started or is already done, no need to update progress
        }

        final Quest quest = plugin.getQuestManager().fromId(activeQuest.getQuestId(), activeQuest.getGroupId());
        if(quest == null) {
            return; // quest is no longer in the config.
        }

        final Condition nonMetCondition = firstNonMetCondition(quest.getConditions(), context);
        if(nonMetCondition != null) {
            final String callback = nonMetCondition.callback();
            if(callback != null) {
                String temp = callback;
                temp = Placeholders.ACTIVE_QUEST_PLACEHOLDER.apply(temp, activeQuest);
                temp = plugin.getColorProvider().colorize(temp);
                context.getInitiator().sendMessage(temp);
            }
            return; // a quest condition is not met, stop here.
        }

        boolean canProceed = true;

        for (Task task : quest.getTasks()) {
            final ActiveTask activeTask = activeQuest.getActiveTask(task.getTaskId());

            if (activeTask == null) {
                continue;
            }

            if (quest.getOptionValue(Optionnable.Option.DEPENDANT_TASK) && !canProceed) {
                break; // break the loop if the previous task was not completed
            }

            canProceed = activeTask.isDone();

            if(!Objects.equals(task.getType(), context.getType())) {
                continue;
            }

            if(activeTask.isDone()) {
                continue;
            }

            final Condition taskCondition = firstNonMetCondition(task.getConditions(), context);
            if(taskCondition != null) {
                final String callback = taskCondition.callback();
                if(callback != null) {
                    String temp = callback;
                    temp = Placeholders.ACTIVE_QUEST_PLACEHOLDER.apply(temp, activeQuest);
                    temp = Placeholders.ACTIVE_TASK_PLACEHOLDER.apply(temp, activeTask);
                    temp = plugin.getColorProvider().colorize(temp);
                    context.getInitiator().sendMessage(temp);
                }
                continue; // a task condition is not met, skip this task.
            }

            if(context.getTarget() != null
                    && !task.isTarget(context.getTarget())) {
                continue; // only skip if the target is not null
            }

            activeTask.addProgress(context.getAmount());
            final TaskIncrementEvent event = new TaskIncrementEvent(quest, task, activeQuest, activeTask, context.getInitiator());
            Bukkit.getPluginManager().callEvent(event);

            if(event.isCancelled()) {
                activeTask.removeProgress(context.getAmount());
                return;
            }

            if(activeTask.getProgress().compareTo(activeTask.getRequired()) >= 0) {

                activeTask.setDone(true);
                Bukkit.getPluginManager().callEvent(new TaskDoneEvent(context.getInitiator(), quest, task, activeTask));

                if(activeQuest.isDone()) {
                    if(questPlayer != null) {
                        questPlayer.recordQuestCompletion(activeQuest.getQuestId());
                    }
                    Bukkit.getPluginManager().callEvent(new QuestDoneEvent(quest, activeQuest, context.getInitiator()));
                }
            }
        }
    }

    private Condition firstNonMetCondition(Collection<Condition> conditions, RunningQuestContext<?, ?> context) {
        for (Condition condition : conditions) {
            if (!condition.isMet(context)) {
                return condition;
            }
        }

        return null;
    }
}
