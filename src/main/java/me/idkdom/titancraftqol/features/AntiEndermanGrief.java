package me.idkdom.titancraftqol.features;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiEndermanGrief implements Listener {

    private final JavaPlugin plugin;

    /**
     * Anti Enderman Grief constructor
     * @param plugin instance of plugin
     */
    public AntiEndermanGrief(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Cancels entity changing a block if the entity is an Enderman
     * @param event entity change block event to check
     */
    @EventHandler
    public void cancelPickupBlock(EntityChangeBlockEvent event) {
        if (!plugin.getConfig().getBoolean("anti-enderman-grief.enabled")) return;
        if (event.getEntity().toString().contains("CraftEnderman")) {
            event.setCancelled(true);
        }
    }

}
