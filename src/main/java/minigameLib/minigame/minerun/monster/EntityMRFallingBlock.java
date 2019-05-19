package minigameLib.minigame.minerun.monster;

import minigameLib.minigame.minerun.EntityMR;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityMRFallingBlock extends EntityMR {
    public EntityMRFallingBlock(World worldIn) {
        super(worldIn);
        setBlockMode(Blocks.STONE);
        this.setCollision(true);
        isFly =false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void collideAttack(Entity entityIn) {
        super.collideAttack(entityIn);
        entityIn.attackEntityFrom(DamageSource.fallingBlock, 5);
    }
}
