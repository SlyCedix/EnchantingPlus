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

    static boolean openGUI(Player p, boolean admin){
        Inventory enchantingGUI;
        if(admin) {
            enchantingGUI = Bukkit.createInventory(null, 36, ChatColor.DARK_GREEN + "Enchanting Admin");
        } else {
            enchantingGUI = Bukkit.createInventory(null, 36, ChatColor.DARK_GREEN + "Enchanting");
        }
        ItemStack prev = new ItemStack(Material.PAPER);
        ItemStack next = new ItemStack(Material.PAPER);
        ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);

        ItemMeta prevMeta = prev.getItemMeta();
        ItemMeta nextMeta = next.getItemMeta();
        ItemMeta fillerMeta = filler.getItemMeta();

        prevMeta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Previous Page");
        nextMeta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Next Page");
        fillerMeta.setDisplayName(" ");

        prev.setItemMeta(prevMeta);
        next.setItemMeta(nextMeta);
        filler.setItemMeta(fillerMeta);

        ItemStack multiPick = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemStack expedSugar = new ItemStack(Material.SUGAR);

        ItemMeta multiPickMeta = multiPick.getItemMeta();
        ItemMeta expedSugarMeta = expedSugar.getItemMeta();

        multiPickMeta.setDisplayName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Multitool");
        expedSugarMeta.setDisplayName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Expedient");

        ArrayList<String> multiPickLore = new ArrayList<>();
        ArrayList<String> expedSugarLore = new ArrayList<>();

        multiPickLore.add(multitool.getDescription());
        expedSugarLore.add(expedient.getDescription());

        multiPickMeta.setLore(multiPickLore);
        expedSugarMeta.setLore(expedSugarLore);

        multiPick.setItemMeta(multiPickMeta);
        expedSugar.setItemMeta(expedSugarMeta);
        for(int i  = 0; i < 36; i++){
            enchantingGUI.setItem(i, filler);
        }
        enchantingGUI.setItem(12, expedSugar);
        enchantingGUI.setItem(13, multiPick);

        p.openInventory(enchantingGUI);
        return true;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Player p = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();

        if((inventory.getName().equals(ChatColor.DARK_GREEN + "Enchanting") ||
                inventory.getName().equals(ChatColor.DARK_GREEN + "Enchanting Admin")) &&
                event.getSlotType() != InventoryType.SlotType.OUTSIDE && !event.getCurrentItem().getType().equals(Material.AIR)){
            ItemStack clicked = event.getCurrentItem();
            String clickedName = clicked.getItemMeta().getDisplayName();
            ItemStack mainHand = p.getInventory().getItemInMainHand();
            String nameColor = ChatColor.DARK_AQUA + "" + ChatColor.BOLD;
            String[] enchNames = {nameColor + "Multitool", nameColor + "Expedient"};
            Boolean success = false;
            Boolean attemptedEnchant = false;
            int deduction = 0;

            ItemStack[] enchantedBooks = {new ItemStack(Material.ENCHANTED_BOOK), new ItemStack(Material.ENCHANTED_BOOK),
                    new ItemStack(Material.ENCHANTED_BOOK), new ItemStack(Material.ENCHANTED_BOOK), new ItemStack(Material.ENCHANTED_BOOK)};

            ItemMeta[] enchantedBooksMeta = {enchantedBooks[0].getItemMeta(), enchantedBooks[0].getItemMeta(),
                    enchantedBooks[0].getItemMeta(),enchantedBooks[0].getItemMeta(), enchantedBooks[0].getItemMeta()};

            ItemStack goBack = new ItemStack(Material.ENCHANTMENT_TABLE);
            ItemMeta goBackMeta = goBack.getItemMeta();
            goBackMeta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Go Back");
            goBackMeta.setLore(Arrays.asList(ChatColor.GREEN + "" + ChatColor.ITALIC + "Current Experience: " + getPlayerExp(p)));
            goBack.setItemMeta(goBackMeta);

            if(clickedName.equals(enchNames[0])){
                success = multitool.enchantItem(mainHand, (byte)1);
                attemptedEnchant = true;
            }else if(clickedName.equals(enchNames[1])){
                enchantedBooksMeta[0].setDisplayName(enchNames[1] + " I");
                enchantedBooksMeta[1].setDisplayName(enchNames[1] + " II");
                enchantedBooksMeta[2].setDisplayName(enchNames[1] + " III");

                enchantedBooksMeta[0].setLore(Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "XP Cost: " + getExpAtLevel(15)));
                enchantedBooksMeta[1].setLore(Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "XP Cost: " + getExpAtLevel(30)));
                enchantedBooksMeta[2].setLore(Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "XP Cost: " + getExpAtLevel(45)));

                enchantedBooks[0].setItemMeta(enchantedBooksMeta[0]);
                enchantedBooks[1].setItemMeta(enchantedBooksMeta[1]);
                enchantedBooks[2].setItemMeta(enchantedBooksMeta[2]);

                inventory.setItem(12, enchantedBooks[0]);
                inventory.setItem(13, enchantedBooks[1]);
                inventory.setItem(14, enchantedBooks[2]);
                inventory.setItem(31, goBack);
            }else if(clickedName.contains(enchNames[1] + " ")){
                System.out.println(clickedName);
                if(clicked.getItemMeta().getDisplayName().equals(enchNames[1] + " I") && (10000> getExpAtLevel(15) || inventory.getName().contains("Admin"))){
                    System.out.println(clickedName);
                    success = expedient.enchantItem(mainHand, (byte) 1);
                    deduction = 15;
                } else if(clicked.getItemMeta().getDisplayName().equals(enchNames[1] + " II") && (10000 > getExpAtLevel(30) || inventory.getName().contains("Admin"))){
                    System.out.println(clickedName);
                    success = expedient.enchantItem(mainHand, (byte) 2);
                    deduction = 30;
                } else if(clicked.getItemMeta().getDisplayName().equals(enchNames[1] + " III") && (10000> getExpAtLevel(45) || inventory.getName().contains("Admin"))){
                    System.out.println(clickedName);
                    success = expedient.enchantItem(mainHand, (byte) 3);
                    deduction = 45;
                }
                attemptedEnchant = true;
            } else if(clickedName.equals(goBack.getItemMeta().getDisplayName())){
                p.closeInventory();
                openGUI(p, inventory.getName().equals(ChatColor.DARK_GREEN + "Enchanting Admin"));
            }

            if(attemptedEnchant) {
                if (success) {
                    p.sendMessage(ChatColor.GREEN + "Enchantment Successful");
                    if(!inventory.getName().contains("Admin")) {
                        changePlayerExp(p, -1 * getExpAtLevel(deduction));
                    }
                }else{
                    p.sendMessage(ChatColor.RED + "Enchantment Unsuccessful");
                }
            }

            event.setCancelled(true);
        }
    }
    // Calculate amount of EXP needed to level up
    private static int getExpToLevelUp(int level){
        if(level <= 15){
            return 2*level+7;
        } else if(level <= 30){
            return 5*level-38;
        } else {
            return 9*level-158;
        }
    }

    // Calculate total experience up to a level
    private static int getExpAtLevel(int level){
        if(level <= 16){
            return (int) (Math.pow(level,2) + 6*level);
        } else if(level <= 31){
            return (int) (2.5*Math.pow(level,2) - 40.5*level + 360.0);
        } else {
            return (int) (4.5*Math.pow(level,2) - 162.5*level + 2220.0);
        }
    }

    // Calculate player's current EXP amount
    private static int getPlayerExp(Player player){
        int exp = 0;
        int level = player.getLevel();

        // Get the amount of XP in past levels
        exp += getExpAtLevel(level);

        // Get amount of XP towards next level
        exp += Math.round(getExpToLevelUp(level) * player.getExp());

        return exp;
    }

    // Give or take EXP
    private static int changePlayerExp(Player player, int exp){
        // Get player's current exp
        int currentExp = getPlayerExp(player);

        // Reset player's current exp to 0
        player.setExp(0);
        player.setLevel(0);

        // Give the player their exp back, with the difference
        int newExp = currentExp + exp;
        player.giveExp(newExp);

        // Return the player's new exp amount
        return newExp;
    }
}
