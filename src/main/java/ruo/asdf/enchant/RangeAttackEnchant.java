package ruo.asdf.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import ruo.minigame.api.EntityAPI;

import java.util.List;

public class RangeAttackEnchant extends Enchantment {
    public RangeAttackEnchant() {
        super(Rarity.COMMON, EnumEnchantmentType.WEAPON, null);
        this.setName("Range");
        this.setRegistryName("SUAKGE", "range");
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinLevel() {
        return 1;
    }
}
