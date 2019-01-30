package rmap.platformer;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityCoin extends EntityItem {
    public EntityCoin(World worldIn, double x, double y, double z) {
        super(worldIn);
        this.hoverStart = (float) (Math.random() * Math.PI * 2.0D);
        this.setSize(0.25F, 0.25F);
        this.setPosition(x, y, z);
        this.rotationYaw = (float) (Math.random() * 360.0D);
        this.motionX = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D));
        this.motionY = 0.20000000298023224D;
        this.motionZ = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D));
        this.setInfinitePickupDelay();
    }

    public EntityCoin(World worldIn, double x, double y, double z, ItemStack stack) {
        this(worldIn, x, y, z);
        this.setEntityItemStack(stack);
        this.lifespan = (stack.getItem() == null ? 6000 : stack.getItem().getEntityLifespan(stack, worldIn));
    }

    public EntityCoin(World world) {
        super(world);
        this.setInfinitePickupDelay();
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        super.onCollideWithPlayer(entityIn);
        PlatEffect.stageEnd();
        this.setDead();
    }
}
