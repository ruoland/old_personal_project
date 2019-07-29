package ruo.creeperworld;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityBigCreeper extends EntityCreeper {
    public EntityBigCreeper(World worldIn) {
        super(worldIn);

    }

    @Override
    public boolean isChild() {
        return true;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setByte("explosionRadius", (byte)15);
    }

    @Override
    public void onStruckByLightning(EntityLightningBolt lightningBolt) {
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        compound.setByte("explosionRadius", (byte)15);
        super.readEntityFromNBT(compound);

    }
}
