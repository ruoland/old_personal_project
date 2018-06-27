package ruo.map.lopre2.dummy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import ruo.map.lopre2.EntityPreBlock;
import ruo.minigame.MiniGame;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;

import java.util.List;

public class EntityDisBlock extends EntityPreBlock {
    private double[] endPos = new double[3];

    public EntityDisBlock(World world) {
        super(world);
        this.setBlockMode(Blocks.STONE);
        this.setCollision(true);
        isFly = true;
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        setSpawnXYZ(posX, posY, posZ);
        this.setTeleport(true);
        return super.onInitialSpawn(difficulty, livingdata);
    }
    public void setPos(EntityPlayer player, double y) {
        if (y != posY) {
            endPos[1] = y;
            return;
        }
        this.endPos = new double[]{posX + EntityAPI.lookX(player, 2), y, posZ + EntityAPI.lookZ(player, 2)};
    }
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (WorldAPI.getPlayer() != null && getDistanceToEntity(WorldAPI.getPlayer()) < 10 && endPos[0] != 0) {
            double[] entityPos = WorldAPI.changePosArray(this);
            double x = 0, y = 0, z = 0, speed = 0.03D;
            double spx = getSpawnX(), spy = getSpawnY(), spz = getSpawnZ();
            if (spy > entityPos[1]) {
                y = speed;
            }
            if (spy < entityPos[1]) {
                y = -speed;
            }
            if (spx > entityPos[0]) {
                x = speed;
            }
            if (spx < entityPos[0]) {
                x = -speed;
            }
            if (spz > entityPos[2]) {
                z = speed;
            }
            if (spz < entityPos[2]) {
                z = -speed;
            }
            setVelocity(x, y, z);
        }
        else if(endPos[0] != 0 && endPos[1] != 0 && endPos[2] != 0)
            setPosition(endPos[0], endPos[1], endPos[2]);

        List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(
                this.posX - 0.5D, this.posY, this.posZ - 0.5D, this.posX + 0.5D, this.posY + 2, this.posZ + 0.5D));
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if ((entity instanceof EntityPlayer) && !entity.noClip) {
                    setVelocity(0, 0, 0);
                }
            }
        }
    }
}
