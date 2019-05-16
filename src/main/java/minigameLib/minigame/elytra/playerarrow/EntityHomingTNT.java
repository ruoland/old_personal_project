package minigameLib.minigame.elytra.playerarrow;

import olib.map.EntityDefaultNPC;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class EntityHomingTNT extends EntityDefaultNPC {
    public EntityHomingTNT(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.TNT);
    }
}
