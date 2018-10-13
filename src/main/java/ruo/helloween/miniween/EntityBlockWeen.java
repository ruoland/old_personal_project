package ruo.helloween.miniween;

import net.minecraft.world.World;
import ruo.minigame.map.EntityDefaultNPC;

public class EntityBlockWeen extends EntityDefaultNPC {
    public EntityBlockWeen(World worldIn) {
        super(worldIn);
        this.setDeathTimer(200);
    }
}
