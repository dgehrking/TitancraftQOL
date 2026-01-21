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

    private String getBabyName() {
        return plugin.getConfig().getString("baby-mobs.name");
    }

    private boolean featureEnabled() {
        return plugin.getConfig().getBoolean("baby-mobs.enabled", true);
    }

    private boolean isBabyName(LivingEntity entity) {
        Component nameComponent = entity.customName();
        if (nameComponent == null) return false;
        String name = PlainTextComponentSerializer.plainText().serialize(nameComponent);
        return name.equals(getBabyName()); //case-sensitive
    }

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

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        LivingEntity entity = event.getEntity();

        if (!isBabyName(entity)) return;

        updateLivingEntity(entity);
    }

    @EventHandler
    public void onNameChange(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof LivingEntity entity)) return;
        Bukkit.getScheduler().runTask(plugin, () -> {
           updateLivingEntity(entity);
        });
    }

}
