package me.idkdom.titancraftqol.features;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiEndermanGrief implements Listener {

    private final JavaPlugin plugin;
    public AntiEndermanGrief(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Cancels entity changing a block if the entity is an Enderman
     * @param event
     */
    @EventHandler
    public void cancelPickupBlock(EntityChangeBlockEvent event) {
        if (event.getEntity().toString().contains("CraftEnderman")) {
            event.setCancelled(true);
        }
    }

}
