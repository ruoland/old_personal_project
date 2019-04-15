package map.lopre2.jump1;

import map.lopre2.EntityPreBlock;
import map.lopre2.LoPre2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import oneline.map.EntityDefaultNPC;

import java.util.List;

public class EntityTestB extends EntityDefaultNPC {
    public EntityTestB(World worldIn) {
        super(worldIn);
        setSize(1, 1);
        this.setBlockMode(Blocks.STONE);
        setCollision(true);
        isFly = true;
    }
    @Override
    public void applyEntityCollision(Entity entityIn) {
    }

    @Override
    public void knockBack(Entity entityIn, float strenght, double xRatio, double zRatio) {
        //super.knockBack(entityIn, strenght, xRatio, zRatio);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        //super.collideWithEntity(entityIn);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
            boolean isFly = true;

            List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(
                    this.posX - 0.5D, this.posY, this.posZ - 0.5D, this.posX + 0.5D, this.posY + 2, this.posZ + 0.5D));
            if (!list.isEmpty()) {
                for (Entity entity : list) {
                    if ((entity instanceof EntityPlayer) && !entity.noClip) {
                        isFly = false;
                    }
                }
            }else
                isFly = true;
            if (isFly) {
                if(LoPre2.compare(posY, getSpawnY()) == -1) {
                    System.out.println("올라가는 중"+(posY - getSpawnY()));
                    motionZ= 0;
                    motionY = 0.02;
                    motionX = 0;
                }
                else {
                    motionZ= 0;
                    motionY = 0;
                    motionX = 0;
                }
            }
            if (!isFly) {
                motionZ= 0;
                motionY = -0.02;
                System.out.println("내려가는 중");
                motionX = 0;
            }
    }

}
