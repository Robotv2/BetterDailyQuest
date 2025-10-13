package fr.robotv2.betterdailyquest.event;

import fr.robotv2.betterdailyquest.quest.Quest;
import org.bukkit.event.Event;

public abstract class QuestEvent extends Event {

    private final Quest quest;

    protected QuestEvent(Quest quest) {
        this.quest = quest;
    }

    public Quest getQuest() {
        return quest;
    }
}
