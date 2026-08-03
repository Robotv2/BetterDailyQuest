package fr.robotv2.betterdailyquest.configurations.messages;

import org.bukkit.configuration.ConfigurationSection;

public class MessageConfiguration {

    private final CommandMessages commandMessages;

    public MessageConfiguration(ConfigurationSection section) {
        ConfigurationSection commandSection = section.getConfigurationSection("commands");
        this.commandMessages = new CommandMessages(commandSection);
    }

    public CommandMessages getCommandMessages() {
        return commandMessages;
    }

    public static class CommandMessages {

        private final String reloadSuccess;
        private final String reloadFailure;
        private final String playerNotLoaded;
        private final String questNotFound;
        private final String questAlreadyHas;
        private final String questUnavailable;
        private final String questAlreadyCompleted;
        private final String questAlreadyStarted;
        private final String maxRerollsReached;
        private final String noQuestAvailable;
        private final String specifyPlayerForReroll;
        private final String giveSuccess;
        private final String clearSuccess;
        private final String resetSuccess;
        private final String startSuccessOthers;
        private final String startSuccessSelf;
        private final String rerollSuccessOthers;
        private final String rerollSuccessSelf;
        private final String completeSuccess;
        private final String questBoardUnavailable;
        private final String questBoardStartDenied;

        public CommandMessages(ConfigurationSection section) {
            this.reloadSuccess = getStringOrDefault(section, "reload_success", "&aThe plugin has been reloaded successfully.");
            this.reloadFailure = getStringOrDefault(section, "reload_failure", "&cReload failed. The previous configuration is still active. Check the console.");
            this.playerNotLoaded = getStringOrDefault(section, "player_not_loaded", "&cThe player is not connected or is not loaded.");
            this.questNotFound = getStringOrDefault(section, "quest_not_found", "&cThe target does not have this quest.");
            this.questAlreadyHas = getStringOrDefault(section, "quest_already_has", "&cThe target already has this quest.");
            this.questUnavailable = getStringOrDefault(section, "quest_unavailable", "&cThe quest is not available. (%quest_id%)");
            this.questAlreadyCompleted = getStringOrDefault(section, "quest_already_completed", "&cThe quest is already completed.");
            this.questAlreadyStarted = getStringOrDefault(section, "quest_already_started", "&cThe quest assignment is already started.");
            this.maxRerollsReached = getStringOrDefault(section, "max_rerolls_reached", "&cYou have reached the maximum rerolls for this quest group.");
            this.noQuestAvailable = getStringOrDefault(section, "no_quest_available", "&cNo replacement quest is available for reroll.");
            this.specifyPlayerForReroll = getStringOrDefault(section, "specify_player_for_reroll", "&cYou must specify a player for reroll-others.");

            this.giveSuccess = getStringOrDefault(section, "give_success", "&aThe quest '%quest_id%' has successfully been given to the player '%player%'.");
            this.clearSuccess = getStringOrDefault(section, "clear_success", "&aThe quest '%quest_id%' has successfully been cleared for the player '%player%'.");
            this.resetSuccess = getStringOrDefault(section, "reset_success", "&aThe quest assignment '%quest_id%' has successfully been restarted for the player '%player%'.");
            this.startSuccessOthers = getStringOrDefault(section, "start_success_others", "&aThe quest assignment '%quest_id%' has been started for the player '%player%'.");
            this.startSuccessSelf = getStringOrDefault(section, "start_success_self", "&aYour quest assignment '%quest_id%' has been started.");
            this.rerollSuccessOthers = getStringOrDefault(section, "reroll_success_others", "&aThe quest '%quest_id%' has successfully been rerolled for the player '%player%'.");
            this.rerollSuccessSelf = getStringOrDefault(section, "reroll_success_self", "&aYour quest '%quest_id%' has been successfully rerolled.");
            this.completeSuccess = getStringOrDefault(section, "complete_success", "&aThe quest '%quest_id%' has successfully been completed for the player '%player%'.");
            this.questBoardUnavailable = getStringOrDefault(section, "quest_board_unavailable", "&cThe Quest Board is unavailable. Please contact a server administrator.");
            this.questBoardStartDenied = getStringOrDefault(section, "quest_board_start_denied", "&cYou do not have permission to start this quest assignment.");
        }

        private String getStringOrDefault(ConfigurationSection section, String key, String defaultValue) {
            return section != null ? section.getString(key, defaultValue) : defaultValue;
        }

        public String getReloadSuccess() {
            return reloadSuccess;
        }

        public String getReloadFailure() {
            return reloadFailure;
        }

        public String getPlayerNotLoaded() {
            return playerNotLoaded;
        }

        public String getQuestNotFound() {
            return questNotFound;
        }

        public String getQuestAlreadyHas() {
            return questAlreadyHas;
        }

        public String getQuestUnavailable() {
            return questUnavailable;
        }

        public String getQuestAlreadyCompleted() {
            return questAlreadyCompleted;
        }

        public String getQuestAlreadyStarted() {
            return questAlreadyStarted;
        }

        public String getMaxRerollsReached() {
            return maxRerollsReached;
        }

        public String getNoQuestAvailable() {
            return noQuestAvailable;
        }

        public String getSpecifyPlayerForReroll() {
            return specifyPlayerForReroll;
        }

        public String getGiveSuccess() {
            return giveSuccess;
        }

        public String getClearSuccess() {
            return clearSuccess;
        }

        public String getResetSuccess() {
            return resetSuccess;
        }

        public String getStartSuccessOthers() {
            return startSuccessOthers;
        }

        public String getStartSuccessSelf() {
            return startSuccessSelf;
        }

        public String getRerollSuccessOthers() {
            return rerollSuccessOthers;
        }

        public String getRerollSuccessSelf() {
            return rerollSuccessSelf;
        }

        public String getCompleteSuccess() {
            return completeSuccess;
        }

        public String getQuestBoardUnavailable() {
            return questBoardUnavailable;
        }

        public String getQuestBoardStartDenied() {
            return questBoardStartDenied;
        }
    }
}
