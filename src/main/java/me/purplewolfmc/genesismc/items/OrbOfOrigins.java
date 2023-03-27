package me.purplewolfmc.genesismc.items;

import me.purplewolfmc.genesismc.files.GenesisDataFiles;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrbOfOrigins {

    public static ItemStack orb;

    public static void init() {
        createOrb();
    }

    public static void createOrb() {
        ItemStack item = new ItemStack(Material.MAGMA_CREAM);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.setCustomModelData(00002);
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Orb of Origins");
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        item.setItemMeta(meta);
        orb = item;

        //Shaped Recipe for ORB_OF_ORIGINS
        if(GenesisDataFiles.getPlugCon().getString("disable-orb_of_origins").equalsIgnoreCase("false")) {
            ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("orboforigins"), item);
            sr.shape("XOX",
                    "OSO",
                    "XOX");
            sr.setIngredient('X', Material.NETHERITE_INGOT);
            sr.setIngredient('O', Material.DIAMOND);
            sr.setIngredient('S', Material.NETHER_STAR);
            Bukkit.getServer().addRecipe(sr);

            ShapedRecipe sr1 = new ShapedRecipe(NamespacedKey.minecraft("orboforigins2"), item);
            sr1.shape("XOX",
                    "OSO",
                    "XOX");
            sr1.setIngredient('X', Material.DIAMOND);
            sr1.setIngredient('O', Material.NETHERITE_INGOT);
            sr1.setIngredient('S', Material.NETHER_STAR);
            Bukkit.getServer().addRecipe(sr1);
        }else{
            //orb of oriigns is disabled
        }
    }
}
