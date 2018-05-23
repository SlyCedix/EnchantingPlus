package com.github.slycedix.eplus;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class Expedient extends CustomEnchant implements Listener {
    @Override
    public String getName(){
        return "Expedient";
    }

    @Override
    public String getNameColor() {
        return ChatColor.AQUA + "";
    }
    @Override
    public String getDescription() {
        return "Increases the swing speed of your tool";
    }

    @Override
    public String getDescriptionColor() {
        return ChatColor.ITALIC + "" + ChatColor.GRAY + "";
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.SUGAR;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public Material[] getValidItems(){
        return new Material[]{Material.WOOD_PICKAXE, Material.WOOD_AXE, Material.WOOD_SPADE, Material.WOOD_SWORD,
                Material.STONE_PICKAXE, Material.STONE_AXE, Material.STONE_SPADE, Material.STONE_SWORD,
                Material.IRON_PICKAXE, Material.IRON_AXE, Material.IRON_SPADE, Material.IRON_SWORD,
                Material.GOLD_PICKAXE, Material.GOLD_AXE, Material.GOLD_SPADE, Material.GOLD_SWORD,
                Material.DIAMOND_PICKAXE, Material.DIAMOND_AXE, Material.DIAMOND_SPADE, Material.DIAMOND_SWORD};
    }

    @EventHandler
    public void onSelectWeapon(PlayerItemHeldEvent event){
        Player p = event.getPlayer();
        ItemStack mainHand = p.getInventory().getItem(event.getNewSlot());
        int enchLevel = getEnchantmentLevel(mainHand);
        p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0f + (float) (enchLevel) / 2.0f);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Player p = (Player) event.getWhoClicked();
        ItemStack mainHand = p.getInventory().getItemInMainHand();
        int enchLevel = getEnchantmentLevel(mainHand);
        p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0f + (float) (enchLevel) / 2.0f);
    }
}
