package ruo.awild;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import olib.map.EntityFlyNPC;

public class EntityEnderBlock extends EntityFlyNPC {

    public EntityEnderBlock(World world) {
        super(world);
        this.setBlockMode(Blocks.DIRT);
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        super.onCollideWithPlayer(entityIn);
        entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 3);
    }

    @Override
    public void targetArrive() {
        super.targetArrive();
        this.setDead();
        worldObj.playEvent(1021, this.getPosition().add(0,-1,0), 0);
    }
}
