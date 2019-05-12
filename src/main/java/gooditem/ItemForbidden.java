package gooditem;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class ItemForbidden extends Item {

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        if(isSelected){
            AxisAlignedBB aabb = entityIn.getEntityBoundingBox().expand(10, 4, 10);
            for(Entity mob : worldIn.getEntitiesWithinAABB(EntityMob.class, aabb)){
                mob.setVelocity((mob.posX - entityIn.posX) / 10, (mob.posY - entityIn.posY) / 10, (mob.posZ - entityIn.posZ) / 10);
            }
        }
    }
}
