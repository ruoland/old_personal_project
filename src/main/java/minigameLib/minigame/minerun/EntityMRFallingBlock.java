package minigameLib.minigame.minerun;

import minigameLib.map.EntityDefaultNPC;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityMRFallingBlock extends EntityMR {
    public EntityMRFallingBlock(World worldIn) {
        super(worldIn);
        setBlockMode(Blocks.ANVIL);
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
