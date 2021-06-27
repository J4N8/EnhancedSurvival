package j4n8.enhancedsurvival;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class EnhancedSurvival extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        FileConfiguration config = getConfig();

        //Plugin manager
        PluginManager pm = getServer().getPluginManager();

        //Events config
        if (config.getBoolean("graves")) {
            pm.registerEvents(new Graves(), this);
        }
        if (config.getBoolean("multiplayer_sleep")) {
            pm.registerEvents(new MultiplayerSleep(), this);
        }
        if (config.getBoolean("easy-crop-collect")) {
            pm.registerEvents(new EasyCropCollect(), this);
        }

        //Commands config
        if (config.getBoolean("bed")) {
            this.getCommand("bed").setExecutor(new CommandBed());
        }
        if (config.getBoolean("tpa")) {
            this.getCommand("tpa").setExecutor(new CommandTpa());
            this.getCommand("tpaccept").setExecutor(new CommandTpa());
        }

        getLogger().info("EnhancedSurvival plugin enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
