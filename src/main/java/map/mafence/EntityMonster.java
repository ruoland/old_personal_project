package map.mafence;

import oneline.map.EntityDefaultNPC;
import oneline.map.TypeModel;
import net.minecraft.world.World;

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
