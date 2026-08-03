package fr.robotv2.betterdailyquest.listeners;

import fr.robotv2.betterdailyquest.BetterDailyQuest;
import fr.robotv2.betterdailyquest.event.PlayerBoatEvent;
import fr.robotv2.betterdailyquest.event.PlayerJumpEvent;
import fr.robotv2.betterdailyquest.event.PlayerMinecartEvent;
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

import java.util.List;

public class StatisticListeners implements Listener {

    public static final int DELAY = 20;
    private final List<StatisticWatcher> watchers = createWatchers();

    public StatisticListeners() {
        Bukkit.getOnlinePlayers().forEach(this::initialize);

        Bukkit.getScheduler().runTaskTimer(BetterDailyQuest.instance(), () -> {
            for(Player player : Bukkit.getOnlinePlayers()) {
                for(StatisticWatcher watcher : watchers) {
                    watcher.check(player);
                }
            }
        }, DELAY, DELAY);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        initialize(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        for(StatisticWatcher watcher : watchers) {
            watcher.clear(event.getPlayer());
        }
    }

    private void initialize(Player player) {
        for(StatisticWatcher watcher : watchers) {
            watcher.init(player);
        }
    }

    static List<StatisticWatcher> createWatchers() {
        return List.of(
                new StatisticWatcher(Statistic.WALK_ONE_CM, PlayerWalkEvent::new),
                new StatisticWatcher(Statistic.SPRINT_ONE_CM, PlayerWalkEvent::new),
                new StatisticWatcher(Statistic.CROUCH_ONE_CM, PlayerWalkEvent::new),
                new StatisticWatcher(Statistic.SWIM_ONE_CM, PlayerSwimEvent::new),
                new StatisticWatcher(Statistic.JUMP, PlayerJumpEvent::new),
                new StatisticWatcher(Statistic.BOAT_ONE_CM, PlayerBoatEvent::new),
                new StatisticWatcher(Statistic.MINECART_ONE_CM, PlayerMinecartEvent::new)
        );
    }
}
