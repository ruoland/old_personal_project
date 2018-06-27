package ruo.minigame.minigame.scroll;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import ruo.minigame.MiniGame;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.map.EntityDefaultNPC;

import javax.annotation.Nullable;

public class EntityMushroom extends EntityDefaultNPC{
    private Vec3d target;

    public EntityMushroom(World world){
        super(world);
        this.setBlockMode(Blocks.BROWN_MUSHROOM_BLOCK);
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        target = new Vec3d(posX - EntityAPI.lookPlayerX(5), 0,posZ - EntityAPI.lookPlayerZ(5)).normalize();
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(target != null)
            this.setVelocity(target.xCoord, target.yCoord,target.zCoord);
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        super.onCollideWithPlayer(entityIn);
    }
}
