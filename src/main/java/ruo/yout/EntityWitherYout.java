package ruo.yout;

import net.minecraft.entity.boss.EntityWither;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import ruo.minigame.api.WorldAPI;

public class EntityWitherYout extends EntityWither {
    public EntityWitherYout(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return super.attackEntityFrom(DamageSource.causePlayerDamage(WorldAPI.getPlayer()), amount);
    }
}
