package com.kolinkrewinkel.BitLimitBlockRegression;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: kolin
 * Date: 7/14/13
 * Time: 4:26 PM
 * To change this template use File | Settings | File Templates.
 */

public class BlockGrowthManager {
    private final BitLimitBlockRegression plugin;

    public BlockGrowthManager(BitLimitBlockRegression plugin) {
        this.plugin = plugin;

        this.startRandomizationEvents();
    }

    private void startRandomizationEvents() {

        class RepeatingGrowthTask implements Runnable {
            private final BitLimitBlockRegression plugin;

            public RepeatingGrowthTask(BitLimitBlockRegression plugin) {
                this.plugin = plugin;
            }

            @Override
            public void run() {
                ArrayList<HashMap> conditionsList = (ArrayList<HashMap>) plugin.getConfig().get("conditions");
                if (conditionsList != null) {

                } else {
                    Bukkit.broadcastMessage(ChatColor.RED + "No conditions to grow were found.");
                }
            }
        }

        Bukkit.getScheduler().runTaskTimer(this.plugin, new RepeatingGrowthTask(this.plugin), 20L, 0L);
    }

    boolean randomWithLikelihood(float likelihood) {
        Random rand = new Random();
        return (rand.nextInt((int)likelihood * 100) == 0);
    }
}
