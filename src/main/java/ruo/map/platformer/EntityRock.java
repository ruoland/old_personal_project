package ruo.map.platformer;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.map.EntityDefaultNPC;

public class EntityRock extends EntityDefaultNPC {

    //굴러가는 돌
    public EntityRock(World world){
        super(world);
        this.setBlockMode(Blocks.STONE);
        this.setScale(3,3,3);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.addRotate((float) EntityAPI.lookX(this, 2),0,(float)EntityAPI.lookZ(this, 2));
        this.motionX = EntityAPI.lookX(this, 0.03);
        this.motionZ = EntityAPI.lookZ(this, 0.03);
    }
}
