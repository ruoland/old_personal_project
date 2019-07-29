package ruo.creeperworld;

import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.world.World;

public class
EntityMiniCreeper extends EntityCreeper {
    public EntityMiniCreeper(World worldIn) {
        super(worldIn);
        setSize(0.5F,0.7F);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
    }
    protected PathNavigate getNewNavigator(World worldIn)
    {
        return new PathNavigateClimber(this, worldIn);
    }

    @Override
    public boolean isOnLadder() {
        return isCollidedHorizontally;
    }

    @Override
    public boolean isChild() {
        return true;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setByte("ExplosionRadius", (byte)1.5);
        super.writeEntityToNBT(compound);
    }

    @Override
    public void onStruckByLightning(EntityLightningBolt lightningBolt) {
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        compound.setByte("ExplosionRadius", (byte)1.5);
        super.readEntityFromNBT(compound);

    }
}
