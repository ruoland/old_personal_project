package minigameLib.minigame.minerun;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.init.Blocks;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityMRTNTCart extends EntityMR {
    public EntityMRTNTCart(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.TNT);
        this.setScale(0.8F,0.8F,0.8F);
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        EntityMinecartEmpty minecartEmpty = new EntityMinecartEmpty(worldObj);
        minecartEmpty.setPosition(posX,posY,posZ);
        worldObj.spawnEntityInWorld(minecartEmpty);
        startRiding(minecartEmpty);
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(getRidingEntity() != null)
        getRidingEntity().setVelocity(0,0,0);
        this.rotationYaw = 0;
        this.rotationPitch =0;
    }

    @Override
    public double getMountedYOffset() {
        return height * 0.6;
    }

    @Override
    public void collideAttack(Entity entityIn) {
        super.collideAttack(entityIn);
        worldObj.createExplosion(this, posX, posY, posZ, 1.5F, false);
    }
}
