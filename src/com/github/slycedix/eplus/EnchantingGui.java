package com.github.slycedix.eplus;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class EnchantingGui implements Listener {
    private static Multitool multitool = new Multitool();
    private static Expedient expedient = new Expedient();

    private static CustomEnchant[] enchants = {multitool, expedient};
    private final static String[] romanNumerals = {"I","II","III","IV","V"};
    private static String nameColor = ChatColor.DARK_AQUA + "" + ChatColor.BOLD;

    static boolean openGUI(Player p, boolean admin){
        p.closeInventory();
        Inventory enchantingGUI;
        if(admin) {
            enchantingGUI = Bukkit.createInventory(null, 36, ChatColor.DARK_GREEN + "Enchanting Admin");
        } else {
            enchantingGUI = Bukkit.createInventory(null, 36, ChatColor.DARK_GREEN + "Enchanting");
        }

        ItemStack vanillaEnchants = new ItemStack(Material.ENCHANTMENT_TABLE);
        setItemName(vanillaEnchants, ChatColor.DARK_RED + "" + ChatColor.BOLD + "Vanilla Enchants");
        addLoreToItem(vanillaEnchants,ChatColor.GREEN + "" + ChatColor.ITALIC + "Current Experience: " + getPlayerExp(p));

        populateCustomEnchants(enchantingGUI);

        enchantingGUI.setItem(31, vanillaEnchants);

        p.openInventory(enchantingGUI);
        return true;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Player p = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();

        if((inventory.getName().contains(ChatColor.DARK_GREEN + "Enchanting"))
                && event.getSlotType() != InventoryType.SlotType.OUTSIDE
                && !event.getCurrentItem().getType().equals(Material.AIR)){
            ItemStack clicked = event.getCurrentItem();
            clickAction(p, inventory, clicked);
            event.setCancelled(true);
        }
    }

    private void clickAction(Player p, Inventory inventory, ItemStack clicked) {
        String clickedName;
        String nameColor = ChatColor.DARK_AQUA + "" + ChatColor.BOLD;

        if(clicked.hasItemMeta()){
            if(clicked.getItemMeta().hasDisplayName()) {
                clickedName = clicked.getItemMeta().getDisplayName();
            }else {
                clickedName = "";
            }
        } else {
            clickedName = "";
        }
        boolean levelsPop = false;
        for(CustomEnchant ench : enchants){
            if(clickedName.equals(nameColor + ench.getName())){
                populateLevels(ench, inventory, p);
                levelsPop = true;
            }
        }
        if(!levelsPop) {
            if (clickedName.contains("Go Back")) {
                p.closeInventory();
                openGUI(p, inventory.getName().equals(ChatColor.DARK_GREEN + "Enchanting Admin"));
            } else {
                getEnchantFromName(clickedName, p, inventory);
            }
        }
    }

    private static int getExpToLevelUp(int level){
        if(level <= 15){
            return 2*level+7;
        } else if(level <= 30){
            return 5*level-38;
        } else {
            return 9*level-158;
        }
    }

    private static int getExpAtLevel(int level){
        if(level <= 16){
            return (int) (Math.pow(level,2) + 6*level);
        } else if(level <= 31){
            return (int) (2.5*Math.pow(level,2) - 40.5*level + 360.0);
        } else {
            return (int) (4.5*Math.pow(level,2) - 162.5*level + 2220.0);
        }
    }

    private static int getPlayerExp(Player player){
        int exp = 0;
        int level = player.getLevel();

        exp += getExpAtLevel(level);

        exp += Math.round(getExpToLevelUp(level) * player.getExp());

        return exp;
    }

    private static int changePlayerExp(Player player, int exp){
        int currentExp = getPlayerExp(player);

        player.setExp(0);
        player.setLevel(0);

        int newExp = currentExp + exp;
        player.giveExp(newExp);

        return newExp;
    }

    private static void addLoreToItem(ItemStack item, String str){
        ItemMeta itemMeta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<String>();
        if(itemMeta.hasLore()){
            lore.addAll(itemMeta.getLore());
        }
        lore.add(str);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
    }

    private static void setLoreOfItem(ItemStack item, String str){
        ItemMeta itemMeta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(str);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
    }

    private static void setItemName(ItemStack item, String str){
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(str);
        item.setItemMeta(itemMeta);
    }

    private static void populateLevels(CustomEnchant enchant, Inventory inventory, Player p){
        ItemStack enchantedBook = new ItemStack(Material.ENCHANTED_BOOK);

        ItemStack goBack = new ItemStack(Material.ENCHANTMENT_TABLE);
        setItemName(goBack, ChatColor.DARK_RED + "" + ChatColor.BOLD + "Go Back");
        addLoreToItem(goBack, ChatColor.GREEN + "" + ChatColor.ITALIC + "Current Experience: " + getPlayerExp(p));
        fillInventory(inventory);
        inventory.setItem(31, goBack);

        for(int i = 0; i < enchant.getMaxLevel(); i++){
            setItemName(enchantedBook, nameColor + enchant.getName() + " " + romanNumerals[i]);
            setLoreOfItem(enchantedBook, ChatColor.GRAY + "" + ChatColor.ITALIC + "XP Cost: " + getExpAtLevel(60/enchant.getMaxLevel() * (i+1)));
            inventory.setItem(13 - (enchant.getMaxLevel() / 2)  + i, enchantedBook);
        }


    }

    private static void getEnchantFromName(String name, Player p, Inventory inventory){
        for (CustomEnchant ench: enchants) {
            if(name.contains(ench.getName())){
                if(ench.getMaxLevel() > 1){
                    String roman = name.substring(5 + ench.getName().length());
                    byte level = (byte) (Arrays.asList(romanNumerals).indexOf(roman) + 1);
                    if(getPlayerExp(p) > getExpAtLevel((60/ench.getMaxLevel()) * level) || inventory.getName().contains("Admin")) {
                        if(ench.enchantItem(p.getInventory().getItemInMainHand(), level)){
                            changePlayerExp(p, -1 * getExpAtLevel((60/ench.getMaxLevel()) * level));
                        } else {
                            p.sendMessage(ChatColor.RED + "Enchantment Failed");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "You do not have enough XP for that enchantment");
                    }
                } else {
                    if(getPlayerExp(p) > getExpAtLevel(60) || inventory.getName().contains("Admin")) {
                        if(ench.enchantItem(p.getInventory().getItemInMainHand(), (byte) 1)){
                            changePlayerExp(p, -1 * getExpAtLevel(60));
                        } else {
                            p.sendMessage(ChatColor.RED + "Enchantment Failed");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "You do not have enough XP for that enchantment");
                    }
                }
            }
            ItemStack goBack = new ItemStack(Material.ENCHANTMENT_TABLE);
            setItemName(goBack, ChatColor.DARK_RED + "" + ChatColor.BOLD + "Go Back");
            addLoreToItem(goBack, ChatColor.GREEN + "" + ChatColor.ITALIC + "Current Experience: " + getPlayerExp(p));
            inventory.setItem(31, goBack);
        }
    }

    private static void fillInventory(Inventory inventory){
        ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
        setItemName(filler, " ");
        for(int i  = 0; i < 36; i++){
            inventory.setItem(i, filler);
        }
    }

    private static void populateCustomEnchants(Inventory inventory){
        fillInventory(inventory);
        int i = 0;
        for(CustomEnchant ench : enchants){
            ItemStack item = new ItemStack(ench.getDisplayMaterial());
            setItemName(item, nameColor + ench.getName());
            addLoreToItem(item, ench.getColoredDescription());
            inventory.setItem(13 - (enchants.length / 2) + i,item);
            i++;
        }
    }
}
