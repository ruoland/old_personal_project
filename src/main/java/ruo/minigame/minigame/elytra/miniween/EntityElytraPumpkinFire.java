package ruo.minigame.minigame.elytra.miniween;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import ruo.minigame.fakeplayer.EntityFakePlayer;

public class EntityElytraPumpkinFire extends EntityElytraPumpkin {
    public EntityElytraPumpkinFire(World worldIn) {
        super(worldIn);
        setBlockMode(Blocks.PUMPKIN);
        this.setSize(1F, 1.5F);
        this.setAttack(true);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.setFire(10);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source == DamageSource.onFire)
            return false;
        return super.attackEntityFrom(source, amount);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        super.collideWithEntity(entityIn);
        if(entityIn instanceof EntityFakePlayer) {
            if (isAttackMode())
                entityIn.setFire(2);
        }
    }

}
