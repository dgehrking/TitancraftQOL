package me.idkdom.titancraftqol;

import me.idkdom.titancraftqol.features.SilentMobs;
import org.bukkit.plugin.java.JavaPlugin;

public final class TitancraftQOL extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        if (getConfig().getBoolean("silent-mobs.enabled")) {
            String silentName = getConfig().getString("silent-mobs.name");

            getServer().getPluginManager().registerEvents(new SilentMobs(silentName, this), this);
        }

        getLogger().info("TitancraftQOL enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
