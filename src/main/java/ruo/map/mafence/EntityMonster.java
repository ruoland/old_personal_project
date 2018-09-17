package ruo.map.mafence;

import net.minecraft.world.World;
import ruo.minigame.map.EntityDefaultNPC;
import ruo.minigame.map.TypeModel;

public class EntityMonster extends EntityDefaultNPC {

    public EntityMonster(World worldIn) {
        super(worldIn);
        this.setModel(TypeModel.ZOMBIE);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(!Mafence.MAFENCE_START)
            setDead();
    }
}
