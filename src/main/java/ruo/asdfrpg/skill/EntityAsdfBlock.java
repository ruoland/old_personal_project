package ruo.asdfrpg.skill;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import ruo.minigame.map.EntityDefaultBlock;

public class EntityAsdfBlock extends EntityDefaultBlock {
    public EntityPlayer player = null;
    public EntityAsdfBlock(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.STONE);
    }


    @Override
    public void targetArrive() {
        worldObj.createExplosion(player,posX,posY,posZ,1.5F, false);
        this.setDead();
    }
}
