package j4n8.enhancedsurvival;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommandTpa implements CommandExecutor {

    //           Player1 Player2
    static HashMap<UUID, UUID> tpa = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tpa")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 1) {
                    Player player2 = Bukkit.getPlayer(args[0]);
                    if (player2 != null && player2.isOnline()) {
                        if (!player.getName().equals(player2.getName())) {
                            if (!tpa.containsKey(player.getUniqueId()) && !tpa.containsValue(player2.getUniqueId())) {
                                tpa.put(player.getUniqueId(), player2.getUniqueId());
                                player.sendMessage(ChatColor.DARK_PURPLE + "[Teleport]" + ChatColor.RESET + " Request sent to " + ChatColor.RED + player2.getName() + "!");
                                player2.sendMessage(ChatColor.DARK_PURPLE + "[Teleport] " + ChatColor.RED + player.getName() + ChatColor.RESET + " has requested to teleport to you.");
                                player2.playSound(player2.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
                            } else {
                                player.sendMessage(ChatColor.DARK_PURPLE + "[Teleport] " + ChatColor.RESET + "You have already requested teleport to " + ChatColor.RED + player2.getName());
                            }
                        } else {
                            player.sendMessage(ChatColor.DARK_PURPLE + "[Teleport] " + "You can't teleport to yourself!");
                        }
                    } else {
                        player.sendMessage(ChatColor.DARK_PURPLE + "[Teleport] " + ChatColor.RED + args[0] + ChatColor.RESET + " is not online.");
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
                Player player1 = null;
                if (tpa.containsValue(player.getUniqueId())) {
                    for (Map.Entry<UUID, UUID> entry : tpa.entrySet()) {
                        if (entry.getValue().equals(player.getUniqueId())) {
                            player1 = Bukkit.getPlayer(entry.getKey());
                            break;
                        }
                    }
                    if (player1 != null && player1.isOnline()) {
                        player1.teleport(player.getLocation());
                        player1.sendMessage(ChatColor.DARK_PURPLE + "[Teleport] " + ChatColor.RESET + "You've been teleported to " + ChatColor.RED + player.getName());
                        player.sendMessage("You have accepted teleport from " + player1.getName());
                        tpa.remove(player1.getUniqueId(), player.getUniqueId());
                    } else {
                        player.sendMessage(ChatColor.DARK_PURPLE + "[Teleport] " + ChatColor.RESET + "This player is no longer online!");
                        tpa.remove(player1.getUniqueId(), player.getUniqueId());
                    }
                } else {
                    player.sendMessage(ChatColor.DARK_PURPLE + "[Teleport] " + ChatColor.RESET + "You don't have any teleport requests.");
                }
            }
            return true;
        }
        return false;
    }
}
