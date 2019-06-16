package ruo.helloween.miniween;

import net.minecraft.world.World;
import olib.map.EntityDefaultNPC;
import olib.map.TypeModel;

public class EntityCreeperWeen extends EntityDefaultNPC {
    public EntityCreeperWeen(World worldIn) {
        super(worldIn);
        this.setModel(TypeModel.CREEPER);
    }
}
