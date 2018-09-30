package ruo.minigame.minigame.minerun;

import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import ruo.minigame.map.EntityDefaultNPC;

public class EntityDummyPlayer extends EntityDefaultNPC {
    public EntityDummyPlayer(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }
}
