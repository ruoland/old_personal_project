package ruo.asdfwild.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;

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
