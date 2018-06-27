package ruo.asdf.boss;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.map.EntityDefaultNPC;

import java.util.List;

//블럭을 위로 올려보내고 떨굼
public class EntityUpBlock2 extends EntityDefaultNPC {

    public EntityUpBlock2(World worldIn) {
        super(worldIn);
    }

    @Override
    public void onLivingUpdate() {
        if (WorldAPI.getPlayer() != null && WorldAPI.getPlayer().getDistance(posX, posY, posZ) < 40) {
            if (posY < getSpawnY() + 6) {
                this.setVelocity(0, 0.03, 0);
                this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5D, this.posY, this.posZ - 0.5D,
                        this.posX + 0.5D, this.posY + 1, this.posZ + 0.5D));
            }
            List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox());
            if (!list.isEmpty()) {
                for (Entity entity : list) {
                    if ((entity instanceof EntityPlayer) && !entity.noClip) {
                        this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5D, this.posY, this.posZ - 0.5D,
                                this.posX + 0.5D, this.posY + 1 + 2.5D, this.posZ + 0.5D));
                        entity.onGround = true;
                        entity.moveEntity(0, 0.161, 0);
                    }
                }
            }
        } else
            motionY = 0;

        super.onLivingUpdate();
    }


}
