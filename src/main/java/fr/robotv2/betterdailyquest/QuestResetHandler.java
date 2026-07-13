package fr.robotv2.betterdailyquest;

import fr.robotv2.betterdailyquest.group.QuestGroup;
import fr.robotv2.betterdailyquest.quest.Quest;
import fr.robotv2.betterdailyquest.quest.options.Optionnable;
import fr.robotv2.betterdailyquest.storage.model.ActiveQuest;
import fr.robotv2.betterdailyquest.storage.model.QuestPlayer;
import fr.robotv2.betterdailyquest.util.GroupUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class QuestResetHandler {

    private final BetterDailyQuest plugin;

    public QuestResetHandler(BetterDailyQuest plugin) {
        this.plugin = plugin;
    }

    /**
     * Refreshes the group's quest pool by removing stored assignments and filling new ones when enabled.
     */
    public void reset(QuestGroup group) {

        for (QuestPlayer questPlayer : plugin.getDatabaseManager().getCachedPlayers()) {
            questPlayer.removeActiveQuest(group);
        }

        plugin.getDatabaseManager().removeQuests(group).thenRun(() -> {
            plugin.getLogger().info("All stored quests in group " + group.getGroupId() + " have been removed for pool refresh.");
        }).exceptionally((throwable) -> {
            plugin.getLogger().log(Level.SEVERE, "An error occurred while deleting stored quests from storage.", throwable);
            return null;
        });

        if(group.getOption().getOptionValue(Optionnable.Option.AUTOMATICALLY_GIVEN)) {
            plugin.getDatabaseManager().getCachedPlayers().forEach((questPlayer) -> fillPlayer(questPlayer, group, true));
        }
    }

    private int getRoleLimit(String role, QuestGroup group, int defaultValue) {
        return group.getRoleAssignation(role).orElse(defaultValue);
    }

    public int getPlayerLimit(QuestPlayer questPlayer, QuestGroup group) {
        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(questPlayer.getId());
        return getRoleLimit(GroupUtil.getPlayerPrimaryGroup(offlinePlayer), group, group.getGlobalAssignation());
    }

    public int fillPlayer(QuestPlayer questPlayer, boolean force) {
        int amount = 0;

        for(QuestGroup group : plugin.getQuestGroupManager().getGroups()) {
            amount += fillPlayer(questPlayer, group, force);
        }

        return amount;
    }

    /**
     * Fills missing assignments after a pool refresh or manual fill.
     * @return the number of quest added
     */
    public int fillPlayer(QuestPlayer questPlayer, QuestGroup group, boolean force) {
        if(!group.getOption().getOptionValue(Optionnable.Option.AUTOMATICALLY_GIVEN) && !force) {
            return 0; //If the group is not automatically given, we do not fill the player.
        }

        final int required = getPlayerLimit(questPlayer, group);
        final int current = questPlayer.getActiveQuests(group).size();

        BetterDailyQuest.debug("Player " + questPlayer.getPlayer().getName() + " has " + current + " quest(s) in group " + group.getGroupId() + " and need " + required + " quest(s).");

        if(required != 0 && required > current) {
            final int diff = required - current;
            return fillPlayer(questPlayer, group, diff);
        }

        return 0;
    }

    /**
     * Fill the player with quests.
     * @param questPlayer to fill quest with
     * @param group to select quests from
     * @param amount of quest to give
     * @return the number of quests given
     */
    public int fillPlayer(QuestPlayer questPlayer, QuestGroup group, int amount) {
        final List<Quest> quests = new ArrayList<>();
        final int max = plugin.getQuestManager().getQuests(group).size();

        while(quests.size() < amount) {

            if(quests.size() >= max) {
                break;
            }

            final Quest random = plugin.getQuestManager().getRandomQuest(questPlayer, group);

            if(random == null) {
                break; //There is no quest available for this delay.
            }

            if(!quests.contains(random)) {
                quests.add(random);
            }
        }

        for(Quest quest : quests) {
            final ActiveQuest activeQuest = new ActiveQuest(questPlayer.getPlayer(), quest);
            questPlayer.addActiveQuest(activeQuest);
        }

        return quests.size();
    }
}
