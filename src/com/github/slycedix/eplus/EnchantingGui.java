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
    private static Inventory enchantingGUI = Bukkit.createInventory(null, 36, ChatColor.DARK_GREEN + "Enchanting");
    private static Multitool multitool = new Multitool();
    private static Expedient expedient = new Expedient();

    private static ItemStack[] enchantedBooks = {new ItemStack(Material.ENCHANTED_BOOK), new ItemStack(Material.ENCHANTED_BOOK),
            new ItemStack(Material.ENCHANTED_BOOK), new ItemStack(Material.ENCHANTED_BOOK), new ItemStack(Material.ENCHANTED_BOOK)};

    private static ItemMeta[] enchantedBooksMeta = {enchantedBooks[0].getItemMeta(), enchantedBooks[0].getItemMeta(),
            enchantedBooks[0].getItemMeta(),enchantedBooks[0].getItemMeta(), enchantedBooks[0].getItemMeta()};

    static boolean openGUI(Player p){
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

        enchantingGUI.setItem(0, filler);
        enchantingGUI.setItem(1, filler);
        enchantingGUI.setItem(2, filler);
        enchantingGUI.setItem(3, filler);
        enchantingGUI.setItem(4, filler);
        enchantingGUI.setItem(5, filler);
        enchantingGUI.setItem(6, filler);
        enchantingGUI.setItem(7, filler);
        enchantingGUI.setItem(8, filler);

        enchantingGUI.setItem(9, filler);
        enchantingGUI.setItem(10, filler);
        enchantingGUI.setItem(11, filler);
        enchantingGUI.setItem(12, expedSugar);
        enchantingGUI.setItem(13, multiPick);
        enchantingGUI.setItem(14, filler);
        enchantingGUI.setItem(15, filler);
        enchantingGUI.setItem(16, filler);
        enchantingGUI.setItem(17, filler);

        enchantingGUI.setItem(18, filler);
        enchantingGUI.setItem(19, filler);
        enchantingGUI.setItem(20, filler);
        enchantingGUI.setItem(21, filler);
        enchantingGUI.setItem(22, filler);
        enchantingGUI.setItem(23, filler);
        enchantingGUI.setItem(24, filler);
        enchantingGUI.setItem(25, filler);
        enchantingGUI.setItem(26, filler);

        enchantingGUI.setItem(27, prev);
        enchantingGUI.setItem(28, filler);
        enchantingGUI.setItem(29, filler);
        enchantingGUI.setItem(30, filler);
        enchantingGUI.setItem(31, filler);
        enchantingGUI.setItem(32, filler);
        enchantingGUI.setItem(33, filler);
        enchantingGUI.setItem(34, filler);
        enchantingGUI.setItem(35, next);

        p.openInventory(enchantingGUI);

        return true;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Player p = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();

        if(inventory.equals(enchantingGUI) && event.getSlotType() != InventoryType.SlotType.OUTSIDE){
            ItemStack clicked = event.getCurrentItem();
            String clickedName = clicked.getItemMeta().getDisplayName();
            ItemStack mainHand = p.getInventory().getItemInMainHand();

            if(clickedName.equals(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Multitool")){
                if(multitool.enchantItem(mainHand, (byte)1)){
                    p.sendMessage(ChatColor.GREEN + "Enchantment Successful");
                } else {
                    p.sendMessage(ChatColor.RED + "Enchantment Unsuccessful");
                }
            } else if(clickedName.equals(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Expedient")){
                enchantedBooksMeta[0].setDisplayName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Expedient I");
                enchantedBooksMeta[1].setDisplayName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Expedient II");
                enchantedBooksMeta[2].setDisplayName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Expedient III");

                enchantedBooks[0].setItemMeta(enchantedBooksMeta[0]);
                enchantedBooks[1].setItemMeta(enchantedBooksMeta[1]);
                enchantedBooks[2].setItemMeta(enchantedBooksMeta[2]);

                enchantingGUI.setItem(12, enchantedBooks[0]);
                enchantingGUI.setItem(13, enchantedBooks[1]);
                enchantingGUI.setItem(14, enchantedBooks[2]);
            }
            event.setCancelled(true);
        }
    }
}
