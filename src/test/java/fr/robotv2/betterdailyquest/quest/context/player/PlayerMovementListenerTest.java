package fr.robotv2.betterdailyquest.quest.context.player;

import fr.robotv2.betterdailyquest.event.*;
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
import static org.mockito.Mockito.*;

class PlayerMovementListenerTest {

    @Test
    void walkCentimetersBecomeWalkBlocks() {
        PlayerWalkEvent event = mock(PlayerWalkEvent.class);
        when(event.getPlayer()).thenReturn(mock(Player.class));
        when(event.getDiff()).thenReturn(250);

        try(MockedStatic<Bukkit> bukkit = minecraftVersion()) {
            PlayerWalkListener listener = spy(new PlayerWalkListener(null));
            doNothing().when(listener).updateQuestProgress(any());
            listener.onWalk(event);

            RunningQuestContext<?, ?> context = capture(listener);
            assertSame(QuestTypes.WALK_TYPE, context.getType());
            assertEquals(2.5D, context.getAmount().doubleValue());
        }
    }

    @Test
    void swimCentimetersBecomeSwimBlocks() {
        PlayerSwimEvent event = mock(PlayerSwimEvent.class);
        when(event.getPlayer()).thenReturn(mock(Player.class));
        when(event.getDiff()).thenReturn(125);

        try(MockedStatic<Bukkit> bukkit = minecraftVersion()) {
            PlayerSwimListener listener = spy(new PlayerSwimListener(null));
            doNothing().when(listener).updateQuestProgress(any());
            listener.onSwim(event);

            RunningQuestContext<?, ?> context = capture(listener);
            assertSame(QuestTypes.SWIM_TYPE, context.getType());
            assertEquals(1.25D, context.getAmount().doubleValue());
        }
    }

    @Test
    void jumpsRemainWholeUnits() {
        PlayerJumpEvent event = mock(PlayerJumpEvent.class);
        when(event.getPlayer()).thenReturn(mock(Player.class));
        when(event.getDiff()).thenReturn(3);

        try(MockedStatic<Bukkit> bukkit = minecraftVersion()) {
            PlayerJumpListener listener = spy(new PlayerJumpListener(null));
            doNothing().when(listener).updateQuestProgress(any());
            listener.onJump(event);

            RunningQuestContext<?, ?> context = capture(listener);
            assertSame(QuestTypes.JUMP_TYPE, context.getType());
            assertEquals(3D, context.getAmount().doubleValue());
        }
    }

    @Test
    void boatCentimetersBecomeBlocks() {
        PlayerBoatEvent event = mock(PlayerBoatEvent.class);
        when(event.getPlayer()).thenReturn(mock(Player.class));
        when(event.getDiff()).thenReturn(250);

        try(MockedStatic<Bukkit> bukkit = minecraftVersion()) {
            PlayerBoatListener listener = spy(new PlayerBoatListener(null));
            doNothing().when(listener).updateQuestProgress(any());
            listener.onBoat(event);

            RunningQuestContext<?, ?> context = capture(listener);
            assertSame(QuestTypes.BOAT_TYPE, context.getType());
            assertEquals(2.5D, context.getAmount().doubleValue());
        }
    }

    @Test
    void minecartCentimetersBecomeBlocks() {
        PlayerMinecartEvent event = mock(PlayerMinecartEvent.class);
        when(event.getPlayer()).thenReturn(mock(Player.class));
        when(event.getDiff()).thenReturn(425);

        try(MockedStatic<Bukkit> bukkit = minecraftVersion()) {
            PlayerMinecartListener listener = spy(new PlayerMinecartListener(null));
            doNothing().when(listener).updateQuestProgress(any());
            listener.onMinecart(event);

            RunningQuestContext<?, ?> context = capture(listener);
            assertSame(QuestTypes.MINECART_TYPE, context.getType());
            assertEquals(4.25D, context.getAmount().doubleValue());
        }
    }

    private static MockedStatic<Bukkit> minecraftVersion() {
        MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class);
        bukkit.when(Bukkit::getBukkitVersion).thenReturn("1.21.8-R0.1-SNAPSHOT");
        return bukkit;
    }

    private static RunningQuestContext<?, ?> capture(fr.robotv2.betterdailyquest.quest.context.QuestProgressionEnhancer listener) {
        ArgumentCaptor<RunningQuestContext<?, ?>> captor = ArgumentCaptor.forClass(RunningQuestContext.class);
        verify(listener).updateQuestProgress(captor.capture());
        return captor.getValue();
    }
}
