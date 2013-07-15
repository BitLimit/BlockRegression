package com.kolinkrewinkel.BitLimitBlockRegression;

import org.bukkit.*;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

                ArrayList<HashMap> conditionsList= (ArrayList<HashMap>) plugin.getConfig().getList("conditions");

                if (conditionsList != null) {
                    Iterator conditionsListIterator = conditionsList.iterator();
                    while (conditionsListIterator.hasNext()) {
                        HashMap<String, Object> growthCondition = (HashMap<String, Object>) conditionsListIterator.next();
                        Material seedBlockMaterial = Material.getMaterial((Integer) growthCondition.get("seed"));
                        World world = Bukkit.getWorld((String) growthCondition.get("world"));

                        Chunk[] chunks= world.getLoadedChunks();
                        for (Chunk chunk : chunks) {
                            Random random = new Random();
                            int x = random.nextInt(16);
                            int y = random.nextInt(256);
                            int z = random.nextInt(16);

                            Block block = chunk.getBlock(x, y, z);

//                            Bukkit.broadcastMessage(seedBlockMaterial.toString());
                            if (block.getType().getId() == seedBlockMaterial.getId()) {
                                Location location = block.getLocation();
                                Bukkit.broadcastMessage(ChatColor.GREEN + "Found match at (" + Integer.toString(location.getBlockX()) + ", " + Integer.toString(location.getBlockY()) + ", " + Integer.toString(location.getBlockZ()) + ")!");

                                this.growCopyingFromLocationWithAttemptCount(block.getLocation(), 0);
                            } else {
//                                Bukkit.broadcastMessage(ChatColor.RED + "No match found.");
                            }
                        }
                    }
                }
            }

            private void growCopyingFromLocationWithAttemptCount(Location location, Integer attemptCount) {
                if (attemptCount > 4) {
                    return;
                }

                Block block = location.getBlock();
                Material material = block.getType();
                Random rand = new Random();
                Integer indexOfOffset = rand.nextInt(2);
                Integer offset = -1 + rand.nextInt(2);
                Location expansionLocation = location.clone();

                if (indexOfOffset == 0) {
                    expansionLocation.setX(expansionLocation.getBlockX() + offset);
                } else if (indexOfOffset == 1) {
                    expansionLocation.setY(expansionLocation.getBlockY() + offset);
                } else {
                    expansionLocation.setZ(expansionLocation.getBlockZ() + offset);
                }

                if (expansionLocation.getBlock().getTypeId() == Material.AIR.getId()) {
                    expansionLocation.getBlock().setType(material);

                    Bukkit.broadcastMessage("Set block at location: " + expansionLocation.toString());
                } else {
                    this.growCopyingFromLocationWithAttemptCount(location, attemptCount + 1);
                }
            }
        }

        Bukkit.getScheduler().runTaskTimer(this.plugin, new RepeatingGrowthTask(this.plugin), 0L, 20L);
    }

    boolean randomWithLikelihood(float likelihood) {
        Random rand = new Random();
        return (rand.nextInt((int)likelihood * 100) == 0);
    }


}

