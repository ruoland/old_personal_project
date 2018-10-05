package ruo.minigame.minigame.elytra.playerarrow;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import ruo.minigame.map.EntityDefaultNPC;

public class EntityHomingTNT extends EntityDefaultNPC {
    public EntityHomingTNT(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.TNT);
    }
}
