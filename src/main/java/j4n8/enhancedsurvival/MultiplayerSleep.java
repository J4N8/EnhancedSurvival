package j4n8.enhancedsurvival;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class MultiplayerSleep implements Listener {
    @EventHandler
    public void onPlayerSleep(PlayerBedEnterEvent e) {
        if (e.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            e.setUseBed(Event.Result.ALLOW);
            e.getPlayer().getWorld().setTime(0);
        }
    }
}
