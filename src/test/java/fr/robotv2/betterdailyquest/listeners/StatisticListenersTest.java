package fr.robotv2.betterdailyquest.listeners;

import fr.robotv2.betterdailyquest.event.PlayerBoatEvent;
import fr.robotv2.betterdailyquest.event.PlayerJumpEvent;
import fr.robotv2.betterdailyquest.event.PlayerMinecartEvent;
import fr.robotv2.betterdailyquest.util.stats.StatisticWatcher;
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
import static org.mockito.Mockito.*;

class StatisticListenersTest {

    @Test
    void emitsOneDedicatedEventPerStatistic() {
        Player player = mock(Player.class);
        PluginManager pluginManager = mock(PluginManager.class);
        when(player.getUniqueId()).thenReturn(UUID.randomUUID());
        when(player.getStatistic(Statistic.JUMP)).thenReturn(10, 13);
        when(player.getStatistic(Statistic.BOAT_ONE_CM)).thenReturn(100, 350);
        when(player.getStatistic(Statistic.MINECART_ONE_CM)).thenReturn(1000, 1425);

        try(MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
            bukkit.when(Bukkit::getPluginManager).thenReturn(pluginManager);
            List<StatisticWatcher> watchers = StatisticListeners.createWatchers().stream()
                    .filter(watcher -> watcher.getStatistic() == Statistic.JUMP
                            || watcher.getStatistic() == Statistic.BOAT_ONE_CM
                            || watcher.getStatistic() == Statistic.MINECART_ONE_CM)
                    .toList();
            watchers.forEach(watcher -> {
                watcher.init(player);
                watcher.check(player);
            });
        }

        ArgumentCaptor<Event> events = ArgumentCaptor.forClass(Event.class);
        verify(pluginManager, times(3)).callEvent(events.capture());
        assertEquals(3, ((PlayerJumpEvent) events.getAllValues().get(0)).getDiff());
        assertEquals(250, ((PlayerBoatEvent) events.getAllValues().get(1)).getDiff());
        assertEquals(425, ((PlayerMinecartEvent) events.getAllValues().get(2)).getDiff());
    }
}
