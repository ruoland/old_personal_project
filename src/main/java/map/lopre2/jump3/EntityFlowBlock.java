package map.lopre2.jump3;

import map.lopre2.EntityPreBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class EntityFlowBlock extends EntityPreBlock {
    public EntityFlowBlock(World worldObj) {
        super(worldObj);
    }

    @Override
    public EntityPreBlock spawn(double x, double y, double z) {
        EntityFlowBlock lavaInvisible = new EntityFlowBlock(worldObj);
        dataCopy(lavaInvisible, x, y, z);
        if (isServerWorld() || canForceSpawn()) {
            worldObj.spawnEntityInWorld(lavaInvisible);
        }
        return lavaInvisible;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(
                this.posX - 0.5D, this.posY, this.posZ - 0.5D, this.posX + 0.5D, this.posY + 2, this.posZ + 0.5D));
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if ((entity instanceof EntityPlayer) && !entity.noClip) {
                    entity.moveEntity(0,0,0.01);
                    moveEntity(0,0,0.01);
                }
            }
        }
    }

    @Override
    public String getText() {
        return "용암위에 있으면 아무 방향으로 이동하는 블럭입니다";
    }
}
