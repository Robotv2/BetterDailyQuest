package fr.robotv2.betterdailyquest.conditions;

import fr.robotv2.betterdailyquest.quest.context.RunningQuestContext;

public interface Condition {

    boolean isMet(RunningQuestContext<?, ?> context);

    String callback();
}
