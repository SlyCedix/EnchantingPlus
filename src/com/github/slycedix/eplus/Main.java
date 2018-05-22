package com.github.slycedix.eplus;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        LoadEnchantments();
        this.getCommand("eplus").setExecutor(new CommandEplus());
        this.getServer().getPluginManager().registerEvents(new EnchantingGui(), this);
    }

    @Override
    public void onDisable() {
    }

    private void LoadEnchantments() {
        this.getServer().getPluginManager().registerEvents(new Multitool(), this);
        this.getServer().getPluginManager().registerEvents(new Expedient(), this);
    }

}