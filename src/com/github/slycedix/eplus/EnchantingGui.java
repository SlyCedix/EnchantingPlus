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

public class EnchantingGui implements Listener {
    private static Multitool multitool = new Multitool();
    private static Expedient expedient = new Expedient();

    public static boolean openGUI(Player p){
        Inventory enchantingGUI = Bukkit.createInventory(null, 36, ChatColor.DARK_GREEN + "Enchanting");
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

        if(inventory.getName().equals(ChatColor.DARK_GREEN + "Enchanting") && event.getSlotType() != InventoryType.SlotType.OUTSIDE){
            ItemStack clicked = event.getCurrentItem();
            String clickedName = clicked.getItemMeta().getDisplayName();
            ItemStack mainHand = p.getInventory().getItemInMainHand();
            String nameColor = ChatColor.DARK_AQUA + "" + ChatColor.BOLD;
            String[] enchNames = {nameColor + "Multitool", nameColor + "Expedient"};
            Boolean success = false;
            Boolean attemptedEnchant = false;

            ItemStack[] enchantedBooks = {new ItemStack(Material.ENCHANTED_BOOK), new ItemStack(Material.ENCHANTED_BOOK),
                    new ItemStack(Material.ENCHANTED_BOOK), new ItemStack(Material.ENCHANTED_BOOK), new ItemStack(Material.ENCHANTED_BOOK)};

            ItemMeta[] enchantedBooksMeta = {enchantedBooks[0].getItemMeta(), enchantedBooks[0].getItemMeta(),
                    enchantedBooks[0].getItemMeta(),enchantedBooks[0].getItemMeta(), enchantedBooks[0].getItemMeta()};

            ItemStack goBack = new ItemStack(Material.ENCHANTMENT_TABLE);
            ItemMeta goBackMeta = goBack.getItemMeta();
            goBackMeta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Go Back");
            goBack.setItemMeta(goBackMeta);

            if(clickedName.equals(enchNames[0])){
                success = multitool.enchantItem(mainHand, (byte)1);
                attemptedEnchant = true;
            }else if(clickedName.equals(enchNames[1])){
                enchantedBooksMeta[0].setDisplayName(enchNames[1] + " I");
                enchantedBooksMeta[1].setDisplayName(enchNames[1] + " II");
                enchantedBooksMeta[2].setDisplayName(enchNames[1] + " III");

                enchantedBooks[0].setItemMeta(enchantedBooksMeta[0]);
                enchantedBooks[1].setItemMeta(enchantedBooksMeta[1]);
                enchantedBooks[2].setItemMeta(enchantedBooksMeta[2]);

                inventory.setItem(12, enchantedBooks[0]);
                inventory.setItem(13, enchantedBooks[1]);
                inventory.setItem(14, enchantedBooks[2]);
                inventory.setItem(31, goBack);
            }else if(clickedName.contains(enchNames[1] + " ")){
                if(clickedName.equals(enchNames[1] + " I")){
                    success = expedient.enchantItem(mainHand, (byte) 1);
                } else if(clickedName.equals(enchNames[1] + " II")){
                    success = expedient.enchantItem(mainHand, (byte) 2);
                } else if(clickedName.equals(enchNames[1] + " III")){
                    success = expedient.enchantItem(mainHand, (byte) 3);
                }
                attemptedEnchant = true;
            } else if(clickedName.equals(goBack.getItemMeta().getDisplayName())){
                p.closeInventory();
                openGUI(p);
            }
            if(attemptedEnchant) {
                if (success) {
                    p.sendMessage(ChatColor.GREEN + "Enchantment Successful");
                }else{
                    p.sendMessage(ChatColor.RED + "Enchantment Unsuccessful");
                }
            }
            event.setCancelled(true);
        }
    }
}
