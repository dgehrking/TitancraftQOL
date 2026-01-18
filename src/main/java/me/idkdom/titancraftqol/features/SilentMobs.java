package me.idkdom.titancraftqol.features;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class SilentMobs implements Listener {

    private final String silentName;
    private final JavaPlugin plugin;

    public SilentMobs(String silentName, JavaPlugin plugin) {
        this.silentName = silentName;
        this.plugin = plugin;
    }

    /**
     * Checks if mob should be silent
     * @param entity
     * @return true of mob is named the silentName, false if not
     */
    private boolean isSilentName(LivingEntity entity) {
        Component nameComponent = entity.customName();
        if (nameComponent == null) return false;

        String name = PlainTextComponentSerializer.plainText().serialize(nameComponent);
        return name.equals(silentName);
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        //Check if entity is living and has silent name
        if (event.getEntity() instanceof LivingEntity entity && isSilentName(entity)) {
            entity.setSilent(true);
        }
    }

    @EventHandler
    public void onNameChange(PlayerInteractEntityEvent event) {
        //Check if entity is living
        if (!(event.getRightClicked() instanceof LivingEntity entity)) return;

        // Schedule one-tick delay to check name after it's applied
        Bukkit.getScheduler().runTask(plugin, () -> {
            if (isSilentName(entity)) {
                entity.setSilent(true);
            }
        });
    }

}
