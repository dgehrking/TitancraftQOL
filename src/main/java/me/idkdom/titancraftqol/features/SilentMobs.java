package me.idkdom.titancraftqol.features;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SilentMobs implements Listener {

    private final JavaPlugin plugin;

    /**
     * Silent Mobs constructor
     * @param plugin instance of plugin
     */
    public SilentMobs(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Getter for silent mobs name config
     * @return String of silent mob's name
     */
    private String getSilentName() {
        return plugin.getConfig().getString("silent-mobs.name");
    }

    /**
     * Getter for silent mobs enabled config
     * @return Boolean of silent mob enabled value
     */
    private boolean featureEnabled() {
        return plugin.getConfig().getBoolean("silent-mobs.enabled", true);
    }

    /**
     * Checks if mob should be silent
     * @param entity mob to be checked
     * @return true of mob is named the silentName, false if not
     */
    private boolean isSilentName(LivingEntity entity) {
        Component nameComponent = entity.customName();
        if (nameComponent == null) return false;
        String name = PlainTextComponentSerializer.plainText().serialize(nameComponent);
        return name.equals(getSilentName()); //case-sensitive
    }

    /**
     * Updates mob's silent state
     * @param entity mob to be updated
     */
    private void updateEntity(LivingEntity entity) {
        if (isSilentName(entity)) {
            entity.setSilent(featureEnabled());
        }
    }

    /**
     * Called on startup or reload to update all mobs' silent state
     */
    public void updateAllEntities() {
        for (World world : Bukkit.getWorlds()) {
            for (LivingEntity entity : world.getLivingEntities()) {
                updateEntity(entity);
            }
        }
    }

    /**
     * Check for silence name on mob spawn
     * @param event mob spawn event to check
     */
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        //Check if entity is living and has silent name
        if (event.getEntity() instanceof LivingEntity entity) {
            updateEntity(entity);
        }
    }

    /**
     * Check for silence name on player interaction
     * @param event player interaction event to check
     */
    @EventHandler
    public void onNameChange(PlayerInteractEntityEvent event) {
        //Check if entity is living
        if (!(event.getRightClicked() instanceof LivingEntity entity)) return;

        // Schedule one-tick delay to check name after it's applied
        Bukkit.getScheduler().runTask(plugin, () -> updateEntity(entity));
    }

}
