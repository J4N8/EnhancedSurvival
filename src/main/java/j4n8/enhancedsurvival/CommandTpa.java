package j4n8.enhancedsurvival;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CommandTpa implements CommandExecutor {

    //              Player1 Player2
    private HashMap<Player, Player> tpa = new HashMap<>();

    private HashMap<Player, Player> getTpa() {
        return tpa;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tpa")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 1) {
                    Player player2 = Bukkit.getPlayer(args[0]);
                    if (player2 != null && player2.isOnline()) {
                        if (!player.getName().equals(player2.getName())) {
                            if (!getTpa().containsKey(player)) {
                                getTpa().put(player, player2);
                                player2.sendMessage("[Teleport] " + player.getName() + " has requested to teleport to you.");
                                player2.playSound(player2.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
                            } else {
                                player.sendMessage("You have already requested teleport to " + player2.getName());
                            }
                        } else {
                            player.sendMessage("You can't teleport to yourself!");
                        }
                    } else {
                        player.sendMessage(args[0] + " is not online.");
                    }
                } else {
                    return false;
                }
            }
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("tpaccept")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (getTpa().containsKey(player)) {
                    Player player1 = getTpa().get(player);
                    if (player1 != null && player1.isOnline()) {
                        player1.teleport(player.getLocation());
                        player1.sendMessage("You've been teleported to " + player.getName());
                        player.sendMessage("You have accepted teleport from " + player1.getName());
                        getTpa().remove(player);
                    } else {
                        player.sendMessage("This player is no longer online!");
                        getTpa().remove(player);
                    }
                } else {
                    player.sendMessage("You don't have any teleport requests.");
                }
            }
            return true;
        }
        return false;
    }
}
