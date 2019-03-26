package map.platformer;

import oneline.api.EntityAPI;
import oneline.map.EntityDefaultNPC;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

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
