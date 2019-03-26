package ruo.asdfwild.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;

public class RangeBreakEnchant extends Enchantment {
    public RangeBreakEnchant() {
        super(Rarity.RARE, EnumEnchantmentType.DIGGER, null);
        this.setName("RangeBreak");
        this.setRegistryName("SUAKGE", "RangeBreak");
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
