package gooditem;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class ItemMagnet extends Item {

    public ItemMagnet(){
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        if (isSelected) {
            AxisAlignedBB aabb = entityIn.getEntityBoundingBox().expand(10, 4, 10);
            for (Entity mob : worldIn.getEntitiesWithinAABB(EntityMob.class, aabb)) {
                mob.setVelocity((entityIn.posX - mob.posX) / 10, (entityIn.posY - mob.posY) / 30, (entityIn.posZ - mob.posZ) / 10);
            }
        }
    }
}
