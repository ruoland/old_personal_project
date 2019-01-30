package ruo.asdfrpg.skill.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import ruo.minigame.map.EntityDefaultBlock;

public class EntitySkillBlock extends EntityDefaultBlock {
    public EntityPlayer player = null;
    private EntityLivingBase target;

    public EntitySkillBlock(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.STONE);
    }

    public void setTarget(EntityLivingBase target) {
        this.target = target;
    }

    @Override
    public void targetArrive() {
        worldObj.createExplosion(player,posX,posY,posZ,1.5F, false);
        this.setDead();
    }
}
