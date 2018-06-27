package ruo.halloween.miniween;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.map.EntityDefaultNPC;

public class EntityFallingBigWeen extends EntityDefaultNPC{

	private boolean fall;
	private int delay = 20;
    public EntityFallingBigWeen(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.LIT_PUMPKIN);
        this.setRotate(0, -90, 0);
        this.setScale(6, 6, 6);
    }

    @Override
    public void onLivingUpdate() {
    	super.onLivingUpdate();
    	if(delay > 0) {
    		delay --;
    		if(delay == 0)
    			fall = true;
    	}
    	if(!fall) {
    		setPosition(WorldAPI.x(), WorldAPI.y()+5, WorldAPI.z());
    	}
    }
    
    @Override
    public void fall(float distance, float damageMultiplier) {
        super.fall(distance, damageMultiplier);
        this.worldObj.createExplosion(this, posX, posY, posZ, 1.5F, false);
    }
}

