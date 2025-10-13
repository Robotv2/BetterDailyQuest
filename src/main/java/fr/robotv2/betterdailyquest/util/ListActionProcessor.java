package fr.robotv2.betterdailyquest.util;

import com.cryptomorin.xseries.XSound;
import com.google.common.base.Enums;
import fr.robotv2.betterdailyquest.BetterDailyQuest;
import fr.robotv2.betterdailyquest.util.placeholder.PlaceholderSupport;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;

import java.util.Collection;
import java.util.function.Function;

public class ListActionProcessor extends PlaceholderSupport<ListActionProcessor> {

    private Collection<String> actions;

    public ListActionProcessor(Collection<String> actions) {
        this.actions = actions;
    }

    @Override
    @Contract("_ -> this")
    public ListActionProcessor apply(Function<String, String> function) {
        this.actions = actions.stream().map(function).toList();
        return this;
    }

    public void process(OfflinePlayer player) {

        apply("%player%", player.getName());
        final Player online = player.getPlayer();
        boolean isOnline = online != null && online.isOnline();

        if(actions == null || actions.isEmpty()) {
            return;
        }

        for(String action : actions) {
            if(action == null || action.isEmpty()) {
                continue;
            }

            final String prefix = action.split(" ")[0];
            final String argument = action.length() == prefix.length() ? action.trim() : action.substring(prefix.length() + 1).trim();

            switch (prefix.toLowerCase()) {
                case "[player]":
                    if(!isOnline) break;
                    online.performCommand(argument);
                    break;
                case "[console]":
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), argument);
                    break;
                case "[message]":
                    if(!isOnline) break;
                    online.sendMessage(BetterDailyQuest.instance().getColorProvider().colorize(argument));
                    break;
                case "[close]":
                    if(!isOnline) break;
                    online.closeInventory();
                    break;
                case "[sound]":
                    if(!isOnline) break;
                    XSound.play(argument, (sound) -> sound.forPlayers(online));
                    break;
                default:
                    BetterDailyQuest.instance().getLogger().warning("Unknown action prefix: " + prefix + " in action: " + action);
                    break;
            }
        }
    }
}
