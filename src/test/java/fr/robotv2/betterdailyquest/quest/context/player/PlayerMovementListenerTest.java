package fr.robotv2.betterdailyquest.quest.context.player;

import fr.robotv2.betterdailyquest.event.PlayerSwimEvent;
import fr.robotv2.betterdailyquest.event.PlayerWalkEvent;
import fr.robotv2.betterdailyquest.quest.context.RunningQuestContext;
import fr.robotv2.betterdailyquest.quest.type.QuestTypes;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PlayerMovementListenerTest {

    @Test
    void walkCentimetersBecomeWalkBlocks() {
        PlayerWalkEvent event = mock(PlayerWalkEvent.class);
        when(event.getPlayer()).thenReturn(mock(Player.class));
        when(event.getDiff()).thenReturn(250);

        try(MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
            bukkit.when(Bukkit::getBukkitVersion).thenReturn("1.21.8-R0.1-SNAPSHOT");
            PlayerWalkListener listener = spy(new PlayerWalkListener(null));
            doNothing().when(listener).updateQuestProgress(any());

            listener.onWalk(event);

            ArgumentCaptor<RunningQuestContext<?, ?>> contextCaptor = ArgumentCaptor.forClass(RunningQuestContext.class);
            verify(listener).updateQuestProgress(contextCaptor.capture());
            RunningQuestContext<?, ?> context = contextCaptor.getValue();
            assertSame(QuestTypes.WALK_TYPE, context.getType());
            assertEquals(2.5D, context.getAmount().doubleValue());
        }
    }

    @Test
    void swimCentimetersBecomeSwimBlocks() {
        PlayerSwimEvent event = mock(PlayerSwimEvent.class);
        when(event.getPlayer()).thenReturn(mock(Player.class));
        when(event.getDiff()).thenReturn(125);

        try(MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
            bukkit.when(Bukkit::getBukkitVersion).thenReturn("1.21.8-R0.1-SNAPSHOT");
            PlayerSwimListener listener = spy(new PlayerSwimListener(null));
            doNothing().when(listener).updateQuestProgress(any());

            listener.onSwim(event);

            ArgumentCaptor<RunningQuestContext<?, ?>> contextCaptor = ArgumentCaptor.forClass(RunningQuestContext.class);
            verify(listener).updateQuestProgress(contextCaptor.capture());
            RunningQuestContext<?, ?> context = contextCaptor.getValue();
            assertSame(QuestTypes.SWIM_TYPE, context.getType());
            assertEquals(1.25D, context.getAmount().doubleValue());
        }
    }
}
