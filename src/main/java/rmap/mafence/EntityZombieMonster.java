package rmap.mafence;

import minigameLib.map.TypeModel;
import net.minecraft.world.World;

public class EntityZombieMonster extends EntityMonster {
    public EntityZombieMonster(World worldIn) {
        super(worldIn);
        this.setModel(TypeModel.ZOMBIE);
    }

}
