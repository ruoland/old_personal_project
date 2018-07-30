package ruo.asdfrpg.skill;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import ruo.map.lopre2.EntityPreBlock;
import ruo.minigame.map.EntityDefaultBlock;
import ruo.minigame.map.EntityDefaultNPC;
import scala.xml.dtd.EntityDef;

import java.util.List;

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
