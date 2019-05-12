package gooditem;

import it.unimi.dsi.fastutil.Hash;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemWarning extends Item {
    private int lastSlot;
    private ArrayList<EntityMob> mobArrayList = new ArrayList<>();
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        if(!worldIn.isRemote) {
            if (isSelected) {
                lastSlot = itemSlot;
                AxisAlignedBB aabb = entityIn.getEntityBoundingBox().expand(10, 4, 10);
                for (EntityMob mob : worldIn.getEntitiesWithinAABB(EntityMob.class, aabb)) {
                    if (!mobArrayList.contains(mob)) {
                        mobArrayList.add(mob);
                        entityIn.addChatMessage(new TextComponentString(mob.getName() + "가(이) " + String.format("%.3f", entityIn.getDistanceToEntity(mob)) + " 거리 안에 존재합니다."));
                    }
                }
            } else if (lastSlot == itemSlot && mobArrayList.size() > 0) {
                mobArrayList.clear();
                lastSlot = -1;
            }
        }
    }
}
