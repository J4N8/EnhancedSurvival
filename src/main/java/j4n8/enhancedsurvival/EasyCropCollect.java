package j4n8.enhancedsurvival;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class EasyCropCollect implements Listener {
    private final List<Material> crop_types = Arrays.asList(Material.WHEAT, Material.POTATOES, Material.CARROTS, Material.BEETROOTS, Material.COCOA, Material.NETHER_WART); //TODO: Add more materials

    @EventHandler
    public void OnCropRightClick(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if (!crop_types.contains(block.getType())) {
            return; //Not a crop
        }

        Player player = e.getPlayer();
        /*if (player.getInventory().getItemInMainHand().getType() != Material.AIR){
            return; //Has item in hand
        }*/ //TODO: Add this as config option

        Ageable age = (Ageable) block.getBlockData();
        if (age.getAge() != age.getMaximumAge()) {
            return; //Crop is not fully grown
        }

        Collection<ItemStack> drops = block.getDrops();

        //Wheat
        if (block.getType() == Material.WHEAT) {
            drops.remove(new ItemStack(Material.WHEAT_SEEDS, 1));
        }
        //Potato
        else if (block.getType() == Material.POTATOES) {
            drops.remove(new ItemStack(Material.POTATO, 1));
        }
        //Carrot
        else if (block.getType() == Material.CARROTS) {
            drops.remove(new ItemStack(Material.CARROT, 1));
        }

        //Drop the items
        for (ItemStack drop : drops) {
            player.getWorld().dropItem(player.getLocation(), drop);
        }

        age.setAge(0);
        block.setBlockData(age);
    }
}
