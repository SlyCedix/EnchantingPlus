package com.github.slycedix.eplus;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public abstract class CustomEnchant{
    private final String[] romanNumerals = {"I","II","III","IV","V"};

    public abstract String getName();
    public abstract int getMaxLevel();
    public abstract Material[] getValidItems();
    public abstract String getDescription();
    public abstract String getNameColor();
    public abstract String getDescriptionColor();
    public abstract Material getDisplayMaterial();

    String getColoredName(){
        return getNameColor() + getName();
    }

    String getColoredDescription(){
        return getDescriptionColor() + getDescription();
    }

    boolean enchantItem(ItemStack item, byte level){
        if(item != null && item.getType() != Material.AIR) {
            ItemMeta meta = item.getItemMeta();
            ArrayList<String> lore = new ArrayList<String>();
            if (level > 0) {
                if (meta.hasLore()) {
                    lore.addAll(meta.getLore());
                }
                if (getEnchantmentLevel(item) < level) {
                    Iterator<String> iterator = lore.iterator();
                    while(iterator.hasNext()) {
                        String line = iterator.next();
                        if (line.contains(this.getColoredName())) {
                            iterator.remove();
                        }
                    }
                    if (Arrays.asList(this.getValidItems()).contains(item.getType())) {
                        if (level <= 10) {
                            if (this.getMaxLevel() == 1) {
                                lore.add(this.getColoredName());
                            } else {
                                lore.add(this.getColoredName() + " " + romanNumerals[level - 1]);
                            }
                        } else {
                            lore.add(this.getColoredName() + " " + level);
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
            ArrayList<String> lore = new ArrayList<String>();
            lore.addAll(meta.getLore());

            if (this.getMaxLevel() > 1) {
                byte currLevel = 0;
                Iterator<String> iterator = lore.iterator();
                while(iterator.hasNext()) {
                    String line = iterator.next();
                    if (line.contains(this.getName())) {
                        String levelStr = line.substring(this.getColoredName().length() + 1);
                        currLevel = (byte) (Arrays.asList(romanNumerals).indexOf(levelStr) + 1);
                        break;
                    }
                }
                return currLevel;
            } else {
                if (lore.contains(this.getColoredName())) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
        return 0;
    }
}
