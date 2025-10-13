package fr.robotv2.betterdailyquest.listeners;

import fr.robotv2.betterdailyquest.BetterDailyQuest;
import fr.robotv2.betterdailyquest.event.PlayerSwimEvent;
import fr.robotv2.betterdailyquest.event.PlayerWalkEvent;
import fr.robotv2.betterdailyquest.util.stats.StatisticWatcher;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;

public class StatisticListeners implements Listener {

    public static final int DELAY = 20;
    public static final Set<StatisticWatcher> WATCHERS = new HashSet<>();

    public StatisticListeners() {
        WATCHERS.add(new StatisticWatcher(Statistic.WALK_ONE_CM, PlayerWalkEvent::new));
        WATCHERS.add(new StatisticWatcher(Statistic.SWIM_ONE_CM, PlayerSwimEvent::new));
        // WATCHERS.add(new StatisticWatcher(Statistic.PLAY_ONE_MINUTE, (player, ignored1, ignored2) -> new PlayerStayOnlineEvent(player)));

        Bukkit.getScheduler().runTaskTimer(BetterDailyQuest.instance(), () -> {

            for(Player player : Bukkit.getOnlinePlayers()) {
                for(StatisticWatcher watcher : WATCHERS) {
                    watcher.check(player);
                }
            }

        }, DELAY, DELAY);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        for(StatisticWatcher watcher : WATCHERS) {
            watcher.init(event.getPlayer());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        for(StatisticWatcher watcher : WATCHERS) {
            watcher.clear(event.getPlayer());
        }
    }
}
