package com.lttf.mcMMOTrade;

import com.lttf.mcMMOTrade.commands.ExpTrade;
import com.lttf.mcMMOTrade.commands.LevelTrade;
import com.lttf.mcMMOTrade.commands.mcMMOTrade;
import com.lttf.mcMMOTrade.events.PlayerDeath;
import com.lttf.mcMMOTrade.events.PlayerInteract;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * Created by Nick (lttf) on 27/09/2015.
 */
public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable(){
        PluginDescriptionFile pdfFile = getDescription();
        Logger logger = getLogger();
        this.getServer().getPluginManager().registerEvents(this, this);
        logger.info(pdfFile.getName() + " has been enabled (V." + pdfFile.getVersion() + ")");

        // Config File Registration
        getConfig().options().copyDefaults(true);
        saveConfig();
        getConfigData();

        // Register Commands & Events
        registerCommands();
        registerEvents();
    }

    public static String levels;
    public static String experience;

    private void getConfigData() {
        experience = getConfig().getString("enabled.exptrade").toUpperCase();
        levels = getConfig().getString("enabled.leveltrade").toUpperCase();
    }

    @Override
    public void onDisable() {
        PluginDescriptionFile pdfFile = getDescription();
        Logger logger = getLogger();
        logger.info(pdfFile.getName() + " has been disabled (V." + pdfFile.getVersion() + ")");
    }

    // Command Registration
    private void registerCommands() {
        getCommand("leveltrade").setExecutor(new LevelTrade());
        getCommand("exptrade").setExecutor(new ExpTrade());
        getCommand("mcmmotrade").setExecutor(new mcMMOTrade());
    }

    // Event Registration
    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerInteract(), this);
        pm.registerEvents(new PlayerDeath(), this);
    }

}
