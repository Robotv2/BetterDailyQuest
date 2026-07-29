package fr.robotv2.betterdailyquest.util.stats;

import fr.robotv2.betterdailyquest.event.PlayerWalkEvent;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StatisticWatcherTest {

    @Test
    void emitsOnlyPositiveDeltasAndRebasesAfterAReset() {
        Player player = mock(Player.class);
        PluginManager pluginManager = mock(PluginManager.class);
        UUID playerId = UUID.randomUUID();
        when(player.getUniqueId()).thenReturn(playerId);
        when(player.getStatistic(Statistic.WALK_ONE_CM)).thenReturn(100, 250, 50, 80);

        StatisticWatcher watcher = new StatisticWatcher(Statistic.WALK_ONE_CM, PlayerWalkEvent::new);

        try(MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
            bukkit.when(Bukkit::getPluginManager).thenReturn(pluginManager);

            watcher.check(player);
            watcher.check(player);
            watcher.check(player);
            watcher.check(player);
        }

        ArgumentCaptor<Event> events = ArgumentCaptor.forClass(Event.class);
        verify(pluginManager, times(2)).callEvent(events.capture());
        List<Integer> deltas = events.getAllValues().stream()
                .map(PlayerWalkEvent.class::cast)
                .map(PlayerWalkEvent::getDiff)
                .toList();
        assertEquals(List.of(150, 30), deltas);
    }

    @Test
    void clearingAPlayerDropsThePreviousBaseline() {
        Player player = mock(Player.class);
        PluginManager pluginManager = mock(PluginManager.class);
        when(player.getUniqueId()).thenReturn(UUID.randomUUID());
        when(player.getStatistic(Statistic.WALK_ONE_CM)).thenReturn(100, 200);

        StatisticWatcher watcher = new StatisticWatcher(Statistic.WALK_ONE_CM, PlayerWalkEvent::new);
        watcher.init(player);
        watcher.clear(player);

        try(MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
            bukkit.when(Bukkit::getPluginManager).thenReturn(pluginManager);
            watcher.check(player);
        }

        verify(pluginManager, times(0)).callEvent(org.mockito.ArgumentMatchers.any());
    }
}
