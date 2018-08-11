package ruo.minigame.minigame.elytra.miniween;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityElytraPumpkinFire extends EntityElytraPumpkin {
    public EntityElytraPumpkinFire(World worldIn) {
        super(worldIn);
        setBlockMode(Blocks.PUMPKIN);
        this.setSize(1F,1.5F);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.setFire(10);
        if(getSpawnDirection() != null)
        this.setVelocity(getXZ(getSpawnDirection().reverse(), 0.03, false));
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(source == DamageSource.onFire)
            return false;
        return super.attackEntityFrom(source, amount);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        super.collideWithEntity(entityIn);
        System.out.println(entityIn);
        entityIn.setFire(2);
    }

}
