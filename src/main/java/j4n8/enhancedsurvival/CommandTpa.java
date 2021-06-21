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

    /**
     * Purple {@code [Teleport]} command prefix text
     */
    private static final String TP_PREFIX = "§5[Teleport] §r";

    /**
     * A {@link HashMap} containing /tpa requests where the key is
     * the UUID of the player who ran the command and the value is
     * the UUID of the destination player
     */
    static HashMap<UUID, UUID> tpaRequests = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;    // todo: print "this command can only be used by players...
        if (!cmd.getName().matches("(?i)tpa|tpaccept"))
            return true;    // todo: invalid syntax - print usage

        Player player = ((Player) sender).getPlayer();
        assert player != null;
        if (cmd.getName().equalsIgnoreCase("tpa"))
            processTpa(player, args[0]);
        else if (cmd.getName().equalsIgnoreCase("tpaccept"))
            processTpAccept(player);
        return true;
    }

    /**
     * Processes the /tpa command
     *
     * @param player          the command executor
     * @param specifiedPlayer the specified destination player
     */
    private void processTpa(Player player, String specifiedPlayer) {
        if (specifiedPlayer.isEmpty())
            return; // maybe print usage or something

        Player destPlayer = Bukkit.getPlayer(specifiedPlayer);
        if (destPlayer == null || !destPlayer.isOnline()) {
            player.sendMessage(TP_PREFIX + ChatColor.RED + specifiedPlayer + ChatColor.RESET + " is not online.");
            return;
        }

        if (player.getName().equalsIgnoreCase(destPlayer.getName())) {
            player.sendMessage(TP_PREFIX + "You can't teleport to yourself!");
            return;
        }

        if (tpaRequests.containsKey(player.getUniqueId()) || tpaRequests.containsValue(destPlayer.getUniqueId())) {
            player.sendMessage(TP_PREFIX + "You have already requested teleport to "
                    + ChatColor.RED + destPlayer.getName());
            return;
        }

        tpaRequests.put(player.getUniqueId(), destPlayer.getUniqueId());
        player.sendMessage(TP_PREFIX + " Request sent to " + ChatColor.RED + destPlayer.getName() + "!");
        destPlayer.sendMessage(TP_PREFIX + ChatColor.RED + player.getName()
                + ChatColor.RESET + " has requested to teleport to you.");
        destPlayer.playSound(destPlayer.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
    }

    /**
     * Processes the /tpaccept command
     *
     * @param player the command executor
     */
    private void processTpAccept(Player player) {
        if (!tpaRequests.containsValue(player.getUniqueId())) {
            player.sendMessage(TP_PREFIX + "You don't have any teleport requests.");
            return;
        }

        Player player1 = null;
        for (Map.Entry<UUID, UUID> entry : tpaRequests.entrySet())
            if (entry.getValue().equals(player.getUniqueId())) {
                player1 = Bukkit.getPlayer(entry.getKey());
                tpaRequests.remove(entry.getKey(), entry.getValue());
                break;
            }

        if (player1 != null && player1.isOnline()) {
            player1.teleport(player.getLocation());
            player1.sendMessage(TP_PREFIX + "You've been teleported to " + ChatColor.RED + player.getName());
            player.sendMessage("You have accepted teleport from " + player1.getName());
        } else player.sendMessage(TP_PREFIX + "This player is no longer online!");
    }

}
