package map.escaperoom;

import map.lopre2.EntityPreBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class EntityRoomJumper extends EntityRoomBlock {
    public EntityRoomJumper(World worldIn) {
        super(worldIn);
        setBlockMode(Blocks.SLIME_BLOCK);
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        super.onCollideWithPlayer(entityIn);
        if(this.getDistanceToEntity(entityIn) < 2 && entityIn.posY > (this.posY+0.5) && entityIn.onGround){
            entityIn.motionY += 1;
        }
    }

    @Override
    public EntityPreBlock spawn(double x, double y, double z) {
        EntityRoomJumper movingBlock = new EntityRoomJumper(worldObj);
        dataCopy(movingBlock, x,y,z);
        if (isServerWorld() || canForceSpawn()) {
            worldObj.spawnEntityInWorld(movingBlock);
        }
        return movingBlock;
    }
}
