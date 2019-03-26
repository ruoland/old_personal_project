package minigameLib.minigame.minerun;

import oneline.map.TypeModel;
import net.minecraft.world.World;

public class EntityMRZombie extends EntityMR {

    public EntityMRZombie(World worldIn) {
        super(worldIn);
        this.setModel(TypeModel.ZOMBIE);
    }
}
