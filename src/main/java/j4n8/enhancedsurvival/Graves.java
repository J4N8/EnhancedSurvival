package j4n8.enhancedsurvival;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class Graves implements Listener {

    //Map to store chest location and player's items
    private Map<Location, ItemStack[]> grave_locations = new HashMap<>();

    @EventHandler
    public void CreateGraveyardOnDeath(PlayerDeathEvent event) {
        //Get main information
        Player player = event.getEntity();
        if (player.getInventory().isEmpty()){//Prevents empty inventory error
            return;
        }
        if (event.getKeepInventory()){//Prevents spawn if keepinventory rule is on
            return;
        }

        ItemStack[] death_items = Stream.of(player.getInventory().getContents())
                .filter(Objects::nonNull) //Filter all non null values (removes empty slots)
                .toArray(ItemStack[]::new);

        Location location = player.getLocation().getBlock().getLocation();

        //Prevent items from dropping to ground and spawn grave
        event.getDrops().clear();
        location.getBlock().setType(Material.PLAYER_HEAD, false);
        grave_locations.put(location, death_items);

        //Send player the grave location
        player.sendMessage("Grave location: X: " + location.getBlockX() + " Y: " + location.getBlockY() + " Z: " + location.getBlockZ());
    }

    @EventHandler
    public void onGraveInteract(PlayerInteractEvent e){
        if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR && e.getPlayer().getInventory().getItemInOffHand().getType() == Material.AIR){ //Must click with empty hand
            //Check if the action is right click on a grave
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK){
                Block block = e.getClickedBlock();
                if (block.getType() == Material.PLAYER_HEAD){
                    //Get grave items based on location and drop them
                    ItemStack[] items = grave_locations.get(block.getLocation());
                    Player player = e.getPlayer();
                    for (ItemStack item : items){
                        player.getWorld().dropItem(player.getLocation(), item);
                    }
                    grave_locations.remove(block.getLocation());
                    block.setType(Material.AIR);
                }
            }
        }
    }
    //TODO: Add onDisable() which saves grave_locations to file
}
