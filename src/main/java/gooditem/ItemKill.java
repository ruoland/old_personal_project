package gooditem;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class ItemKill extends Item {
    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        if(player.isSneaking())
        {
            if(entity instanceof EntityLivingBase)
                ((EntityLivingBase) entity).setHealth(0);
        }else
            entity.setDead();
        return super.onLeftClickEntity(stack, player, entity);

    }
}
