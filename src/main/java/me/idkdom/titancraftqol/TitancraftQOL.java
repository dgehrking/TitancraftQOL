package me.idkdom.titancraftqol;

import me.idkdom.titancraftqol.features.SilentMobs;
import me.idkdom.titancraftqol.features.AntiEndermanGrief;
import org.bukkit.plugin.java.JavaPlugin;

public final class TitancraftQOL extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig(); //create config if missing
        //Plugin config defaults
        getConfig().addDefault("silent-mobs.enabled", true);
        getConfig().addDefault("silent-mobs.name", "silence");
        getConfig().addDefault("anti-enderman-grief.enabled", true);
        getConfig().options().copyDefaults(true);
        saveConfig();
        //Register features
        if (getConfig().getBoolean("silent-mobs.enabled")) {
            String silentName = getConfig().getString("silent-mobs.name");
            getServer().getPluginManager().registerEvents(new SilentMobs(silentName, this), this);
        }
        if (getConfig().getBoolean("anti-enderman-grief.enabled")) {
            getServer().getPluginManager().registerEvents(new AntiEndermanGrief(this), this);
        }

        getLogger().info("TitancraftQOL enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
