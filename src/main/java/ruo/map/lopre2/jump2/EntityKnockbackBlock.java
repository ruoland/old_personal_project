package ruo.map.lopre2.jump2;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import ruo.map.lopre2.jump1.EntityMoveBlock;

import java.util.List;

public class EntityKnockbackBlock extends EntityMoveBlock {
    public double knockbackSize = 0.9;
    public EntityKnockbackBlock(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.EMERALD_BLOCK);
        this.setPlayerMove(false);
        this.setCollision(true);
        this.isFly = true;
        speed = 0.04;
    }

    @Override
    public String getCustomNameTag() {
        return "KnockbackBlock "+" 넉백 "+knockbackSize;
    }

    @Override
    public EntityKnockbackBlock spawn(double x, double y, double z) {
        EntityKnockbackBlock lavaBlock = new EntityKnockbackBlock(worldObj);
        lavaBlock.setSpawnXYZ(x, y, z);
        lavaBlock.setTeleport(false);
        lavaBlock.setPosition(lavaBlock.getSpawnX(), lavaBlock.getSpawnY(), lavaBlock.getSpawnZ());

        lavaBlock.setBlockMode(getCurrentStack());
        lavaBlock.knockbackSize = knockbackSize;
        worldObj.spawnEntityInWorld(lavaBlock);
        this.copyModel(lavaBlock);
        return lavaBlock;
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        super.onCollideWithPlayer(entityIn);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(
                this.posX - knockbackSize, this.posY, this.posZ - knockbackSize, this.posX + knockbackSize, this.posY + 1.5, this.posZ + knockbackSize));
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if ((entity instanceof EntityPlayer) && !entity.noClip) {
                    ((EntityLivingBase) entity).knockBack(this, 0.4F, this.posX - entity.posX, this.posZ - entity.posZ);

                }
            }
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setDouble("knockback", knockbackSize);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        knockbackSize = compound.getDouble("knockback");
    }
}
