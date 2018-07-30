package ruo.asdfrpg.skill;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import ruo.map.lopre2.EntityPreBlock;
import ruo.minigame.map.EntityDefaultBlock;
import ruo.minigame.map.EntityDefaultNPC;
import scala.xml.dtd.EntityDef;

import java.util.List;

public class EntityAsdfBlock extends EntityDefaultBlock {
    public EntityAsdfBlock(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.STONE);
    }

    @Override
    public void targetArrive() {
        List<EntityLivingBase> livingBaseList = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox());
        for(EntityLivingBase livingBase : livingBaseList){
            livingBase.attackEntityFrom(DamageSource.fallingBlock, 2);
        }
        this.setDead();
    }
}
