package ruo.asdf.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

public class BonusExpEnchant extends Enchantment {
    public BonusExpEnchant() {
        super(Rarity.RARE, EnumEnchantmentType.ARMOR_HEAD, null);
        this.setName("BonusExp");
        this.setRegistryName("SUAKGE", "BonusExp");
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinLevel() {
        return 1;
    }
}
