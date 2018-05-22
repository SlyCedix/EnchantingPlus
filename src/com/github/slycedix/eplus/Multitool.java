package com.github.slycedix.eplus;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Multitool extends CustomEnchant implements Listener {
    public enum tool{PICKAXE, AXE, SHOVEL, ANY}
    public enum mat{WOOD, STONE, GOLD, IRON, DIAMOND}

    @Override
    public String getName(){
        return ChatColor.AQUA + "Multitool";
    }

    @Override
    public String getDescription() {
        return ChatColor.ITALIC + "" + ChatColor.GRAY + "Transforms tool into the tool needed for the task at hand";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public Material[] getValidItems(){
        Material[] validItems = {Material.WOOD_PICKAXE, Material.WOOD_AXE, Material.WOOD_SPADE, Material.WOOD_SWORD,
                Material.STONE_PICKAXE, Material.STONE_AXE, Material.STONE_SPADE, Material.STONE_SWORD,
                Material.IRON_PICKAXE, Material.IRON_AXE, Material.IRON_SPADE, Material.IRON_SWORD,
                Material.GOLD_PICKAXE, Material.GOLD_AXE, Material.GOLD_SPADE, Material.GOLD_SWORD,
                Material.DIAMOND_PICKAXE, Material.DIAMOND_AXE, Material.DIAMOND_SPADE, Material.DIAMOND_SWORD};
        return validItems;
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event){
        Player p = event.getPlayer();
        ItemStack mainHand = p.getInventory().getItemInMainHand();
        if(getEnchantmentLevel(mainHand) > 0){
            tool neededTool = ToolBrokenBy(event);
            mat toolMaterial = getItemMaterial(mainHand);
            switch(neededTool){
                case PICKAXE:
                    switch(toolMaterial) {
                        case WOOD:
                            mainHand.setType(Material.WOOD_PICKAXE);
                            break;
                        case STONE:
                            mainHand.setType(Material.STONE_PICKAXE);
                            break;
                        case IRON:
                            mainHand.setType(Material.IRON_PICKAXE);
                            break;
                        case GOLD:
                            mainHand.setType(Material.GOLD_PICKAXE);
                            break;
                        case DIAMOND:
                            mainHand.setType(Material.DIAMOND_PICKAXE);
                            break;
                    }
                    break;
                case AXE:
                    switch(toolMaterial) {
                        case WOOD:
                            mainHand.setType(Material.WOOD_AXE);
                            break;
                        case STONE:
                            mainHand.setType(Material.STONE_AXE);
                            break;
                        case IRON:
                            mainHand.setType(Material.IRON_AXE);
                            break;
                        case GOLD:
                            mainHand.setType(Material.GOLD_AXE);
                            break;
                        case DIAMOND:
                            mainHand.setType(Material.DIAMOND_AXE);
                            break;
                    }
                    break;
                case SHOVEL:
                    switch(toolMaterial) {
                        case WOOD:
                            mainHand.setType(Material.WOOD_SPADE);
                            break;
                        case STONE:
                            mainHand.setType(Material.STONE_SPADE);
                            break;
                        case IRON:
                            mainHand.setType(Material.IRON_SPADE);
                            break;
                        case GOLD:
                            mainHand.setType(Material.GOLD_SPADE);
                            break;
                        case DIAMOND:
                            mainHand.setType(Material.DIAMOND_SPADE);
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player){
            Player p = (Player) event.getDamager();
            ItemStack mainHand = p.getInventory().getItemInMainHand();
            if(getEnchantmentLevel(mainHand) > 0){
                mat toolMaterial = getItemMaterial(mainHand);
                switch(toolMaterial) {
                    case WOOD:
                        mainHand.setType(Material.WOOD_SWORD);
                        break;
                    case STONE:
                        mainHand.setType(Material.STONE_SWORD);
                        break;
                    case IRON:
                        mainHand.setType(Material.IRON_SWORD);
                        break;
                    case GOLD:
                        mainHand.setType(Material.GOLD_SWORD);
                        break;
                    case DIAMOND:
                        mainHand.setType(Material.DIAMOND_SWORD);
                        break;
                }
            }
        }
    }

    private static tool ToolBrokenBy(BlockDamageEvent event){
        net.minecraft.server.v1_12_R1.Block nmsBlock = net.minecraft.server.v1_12_R1.Block.getById(event.getBlock().getTypeId());
        if(net.minecraft.server.v1_12_R1.Item.getById(278).getDestroySpeed(CraftItemStack.asNMSCopy(event.getItemInHand()),nmsBlock.getBlockData()) > 1.0f)
            return tool.PICKAXE;
        if(net.minecraft.server.v1_12_R1.Item.getById(279).getDestroySpeed(CraftItemStack.asNMSCopy(event.getItemInHand()),nmsBlock.getBlockData()) > 1.0f)
            return tool.AXE;
        if(net.minecraft.server.v1_12_R1.Item.getById(277).getDestroySpeed(CraftItemStack.asNMSCopy(event.getItemInHand()),nmsBlock.getBlockData()) > 1.0f)
            return tool.SHOVEL;
        return tool.ANY;
    }

    private static mat getItemMaterial(ItemStack item) {
        if (item.getType() == Material.WOOD_PICKAXE || item.getType() == Material.WOOD_AXE || item.getType() == Material.WOOD_SPADE || item.getType() == Material.WOOD_SWORD)
            return mat.WOOD;
        if (item.getType() == Material.STONE_PICKAXE || item.getType() == Material.STONE_AXE || item.getType() == Material.STONE_SPADE || item.getType() == Material.STONE_SWORD)
            return mat.STONE;
        if (item.getType() == Material.IRON_PICKAXE || item.getType() == Material.IRON_AXE || item.getType() == Material.IRON_SPADE || item.getType() == Material.IRON_SWORD)
            return mat.IRON;
        if (item.getType() == Material.GOLD_PICKAXE || item.getType() == Material.GOLD_AXE || item.getType() == Material.GOLD_SPADE || item.getType() == Material.GOLD_SWORD)
            return mat.GOLD;
        if (item.getType() == Material.DIAMOND_PICKAXE || item.getType() == Material.DIAMOND_AXE || item.getType() == Material.DIAMOND_SPADE || item.getType() == Material.DIAMOND_SWORD)
            return mat.DIAMOND;
        return mat.DIAMOND;
    }
}
