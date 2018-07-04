package ruo.minigame.minigame.minerun;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import ruo.minigame.map.EntityDefaultNPC;
import ruo.minigame.map.TypeModel;

public class EntityMineRunCreeper extends EntityDefaultNPC {

    public EntityMineRunCreeper(World worldIn) {
        super(worldIn);
        this.setModel(TypeModel.CREEPER);
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        super.onCollideWithPlayer(entityIn);
        worldObj.createExplosion(this,posX,posY,posZ,2, false);
        this.setDead();
    }
}
