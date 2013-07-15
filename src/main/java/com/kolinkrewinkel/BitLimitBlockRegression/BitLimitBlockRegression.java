package com.kolinkrewinkel.BitLimitBlockRegression;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BitLimitBlockRegression extends JavaPlugin {
    public RegressionCommandExecutor commandExecutor;
    public BlockGrowthManager blockGrowthManager;

    @Override
    public void onEnable() {
        this.commandExecutor = new RegressionCommandExecutor(this);
        this.getCommand("regression").setExecutor(this.commandExecutor);
        this.blockGrowthManager = new BlockGrowthManager(this);
    }

    @Override
    public void onDisable() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }

    @Override
    public void saveConfig() {
        super.saveConfig();

        this.reloadConfig();
    }
}
