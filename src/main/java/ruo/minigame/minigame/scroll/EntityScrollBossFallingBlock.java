package ruo.minigame.minigame.scroll;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import ruo.minigame.map.EntityDefaultNPC;

import java.util.List;

public class EntityScrollBossFallingBlock extends EntityDefaultNPC {
    public EntityScrollBossFallingBlock(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.STONE);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        super.fall(distance, damageMultiplier);
        List<Entity> list = Lists.newArrayList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()));
        DamageSource damagesource = DamageSource.fallingBlock;

        for (Entity entity : list)
        {
            entity.attackEntityFrom(damagesource, 7);
        }
        this.setDead();
    }
}