package minigameLib.minigame.minerun.monster;

import minigameLib.minigame.minerun.EntityMR;
import olib.map.TypeModel;
import net.minecraft.world.World;

public class EntityMRZombie extends EntityMR {

    public EntityMRZombie(World worldIn) {
        super(worldIn);
        this.setModel(TypeModel.ZOMBIE);
    }
}
