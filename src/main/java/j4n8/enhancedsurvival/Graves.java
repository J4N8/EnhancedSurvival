package j4n8.enhancedsurvival;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;
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

        ItemStack[] death_items = Stream.of(player.getInventory().getContents())
                .filter(Objects::nonNull) //Filter all non null values (removes empty slots)
                .toArray(ItemStack[]::new);

        Location location = player.getLocation().getBlock().getLocation();

        //Prevent items from dropping to ground and spawn grave
        event.getDrops().clear();
        location.getBlock().setType(Material.PLAYER_HEAD, false);
        grave_locations.put(location, death_items);
    }

    @EventHandler
    public void onGraveInteract(PlayerInteractEvent e){
        //Check if the action is right click on a grave
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK){
            Block block = e.getClickedBlock();
            if (block.getType() == Material.PLAYER_HEAD){
                //Get grave items based on location
                ItemStack[] items = grave_locations.get(block.getLocation());
                //Create the inventory according to amount of items
                Inventory inventory;
                if (items.length <= 9){ //TODO: set inventory size based on items in inventory
                    inventory = Bukkit.createInventory(null, 9, "Small inventory");
                }
                else if (items.length <= 27){
                    inventory = Bukkit.createInventory(null, 27, "Medium inventory");
                }
                else{
                    inventory = Bukkit.createInventory(null, 54, "Large inventory");
                }
                //Add items to inventory and open it
                inventory.setContents(items);
                e.getPlayer().openInventory(inventory);

                //TODO: Give player the items automatically on right click. Make them drop if inventory full
                //TODO: remove inventory from grave_locations
            }
        }
    }

    //TODO: Add onDisable() which saves grave_locations to file
}
