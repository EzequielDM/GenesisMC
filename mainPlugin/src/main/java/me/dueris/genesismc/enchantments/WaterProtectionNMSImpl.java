package me.dueris.genesismc.enchantments;

import org.bukkit.craftbukkit.v1_20_R3.enchantments.CraftEnchantment;

import com.google.common.annotations.Beta;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

@Beta
public class WaterProtectionNMSImpl extends Enchantment {
    public WaterProtectionNMSImpl(Rarity weight, EnchantmentCategory target, EquipmentSlot[] slotTypes) {
        super(weight, target, slotTypes);
    }

    @Override
    public int getMinCost(int level) {
        return level * 2;
    }

    @Override
    public int getMaxCost(int level) {
        return (level * 2) + 3;
    }

    @Override
    public Component getFullname(int level) {
        MutableComponent mutableComponent = Component.literal("Water Protection");
            mutableComponent.withStyle(ChatFormatting.GRAY);
            if (level != 1 || this.getMaxLevel() != 1) {
                mutableComponent.append(CommonComponents.SPACE).append(Component.translatable("enchantment.level." + level));
            }
        return mutableComponent;
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public boolean isTradeable() {
        return super.isTradeable();
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        for(org.bukkit.enchantments.Enchantment enchant : Anvil.conflictenchantments){
            CraftEnchantment enchantt = (CraftEnchantment) enchant;
            if(other == enchantt.getHandle()){
                return true;
            }
        }
        return false;
    }
}
