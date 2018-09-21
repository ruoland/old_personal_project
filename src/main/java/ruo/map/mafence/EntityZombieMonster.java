package ruo.map.mafence;

import net.minecraft.world.World;
import ruo.minigame.map.TypeModel;

public class EntityZombieMonster extends EntityMonster {
    public EntityZombieMonster(World worldIn) {
        super(worldIn);
        this.setModel(TypeModel.ZOMBIE);
    }

}
