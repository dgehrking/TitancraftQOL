package me.idkdom.titancraftqol.features;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Breedable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class BabyMobs implements Listener {
    public final JavaPlugin plugin;

    /**
     * Baby Mobs constructor
     * @param plugin instance of plugin
     */
    public BabyMobs(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Check for baby name in config
     * @return baby name
     */
    private String getBabyName() {
        return plugin.getConfig().getString("baby-mobs.name");
    }

    /**
     * Check if feature is enabled
     * @return true/false for if the feature is enabled
     */
    private boolean featureEnabled() {
        return plugin.getConfig().getBoolean("baby-mobs.enabled", true);
    }

    /**
     * Check if the entity has a baby name
     * @param entity entity to be checked
     * @return true/false for if the entity is named the baby name
     */
    private boolean isBabyName(LivingEntity entity) {
        Component nameComponent = entity.customName();
        if (nameComponent == null) return false;
        String name = PlainTextComponentSerializer.plainText().serialize(nameComponent);
        return name.equals(getBabyName()); //case-sensitive
    }

    /**
     * Updates entities based on the config and if they should be babies or not
     * @param entity entity to be updated
     */
    private void updateLivingEntity(LivingEntity entity) {
        if (!(entity instanceof Breedable breedable)) return;
        if (isBabyName(entity)) {
            if (featureEnabled()) {
                breedable.setBaby();
                breedable.setAgeLock(true);
            } else {
                breedable.setAgeLock(false);
            }
        } else {
            breedable.setAgeLock(false);
        }
    }

    /**
     * Called on startup or reload to update all mobs' silent state
     */
    public void updateAllEntities() {
        for (World world : Bukkit.getWorlds()) {
            for (LivingEntity entity : world.getLivingEntities()) {
                updateLivingEntity(entity);
            }
        }
    }

    /**
     * Update creature's state on spawn
     * @param event creature spawn event
     */
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        LivingEntity entity = event.getEntity();
        updateLivingEntity(entity);
    }

    /**
     * Update creature when right-clicked by a player
     * @param event player interaction event
     */
    @EventHandler
    public void onNameChange(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof LivingEntity entity)) return;
        Bukkit.getScheduler().runTask(plugin, () -> updateLivingEntity(entity));
    }

}
