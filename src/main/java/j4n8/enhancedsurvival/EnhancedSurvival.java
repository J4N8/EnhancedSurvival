package j4n8.enhancedsurvival;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class EnhancedSurvival extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        //Register events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new Graves(), this);

        getLogger().info("EnhancedSurvival plugin enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
