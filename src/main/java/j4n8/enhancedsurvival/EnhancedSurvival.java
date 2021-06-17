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
        pm.registerEvents(new MultiplayerSleep(), this);

        //Register commands
        this.getCommand("bed").setExecutor(new CommandBed());
        this.getCommand("tpa").setExecutor(new CommandTpa());
        this.getCommand("tpaccept").setExecutor(new CommandTpa());


        getLogger().info("EnhancedSurvival plugin enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
