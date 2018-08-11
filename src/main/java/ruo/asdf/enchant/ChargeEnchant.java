package ruo.asdf.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;

public class ChargeEnchant extends Enchantment {
    public ChargeEnchant() {
        super(Rarity.RARE, EnumEnchantmentType.BOW, null);
        this.setName("Charge");
        this.setRegistryName("SUAKGE", "Charge");
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getMinLevel() {
        return 1;
    }
}
