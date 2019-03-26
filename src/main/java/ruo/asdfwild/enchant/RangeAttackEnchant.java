package ruo.asdfwild.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;

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
