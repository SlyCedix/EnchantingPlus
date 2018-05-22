package com.github.slycedix.eplus;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class CustomEnchant{
    private final String[] romanNumerals = {"I","II","III","IV","V","VI","VII","VIII","IX","X"};

    public abstract String getName();
    public abstract int getMaxLevel();
    public abstract Material[] getValidItems();
    public abstract String getDescription();

    boolean enchantItem(ItemStack item, byte level){
        if(item != null && item.getType() != Material.AIR) {
            ItemMeta meta = item.getItemMeta();
            ArrayList<String> lore = new ArrayList<>();
            if (level > 0) {
                if (meta.hasLore()) {
                    lore.addAll(meta.getLore());
                }
                if (getEnchantmentLevel(item) < level) {
                    int i = 0;
                    for (String str : lore) {
                        if (str.contains(this.getName())) {
                            lore.remove(i);
                        }
                    }
                    if (Arrays.asList(this.getValidItems()).contains(item.getType())) {
                        if (level <= 10) {
                            if (this.getMaxLevel() == 1) {
                                lore.add(this.getName());
                            } else {
                                lore.add(this.getName() + " " + romanNumerals[level - 1]);
                            }
                        } else {
                            lore.add(this.getName() + " " + level);
                        }
                        meta.setLore(lore);
                        item.setItemMeta(meta);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    byte getEnchantmentLevel(ItemStack item) {
        if (item != null && item.getType() != Material.AIR ) {
            ItemMeta meta = item.getItemMeta();
            if (!meta.hasLore()) {
                return 0;
            }
            ArrayList<String> lore = new ArrayList<>();
            lore.addAll(meta.getLore());

            if (this.getMaxLevel() > 1) {
                byte currLevel = 0;
                for (String str : lore) {
                    if (str.contains(this.getName())) {
                        String levelStr = str.substring(this.getName().length() + 1);
                        currLevel = (byte) (Arrays.asList(romanNumerals).indexOf(levelStr) + 1);
                        break;
                    }
                }
                return currLevel;
            } else {
                if (lore.contains(this.getName())) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
        return 0;
    }
}
