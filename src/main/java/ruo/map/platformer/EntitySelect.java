package ruo.map.platformer;

import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import ruo.minigame.map.EntityDefaultNPC;
import ruo.minigame.map.TypeModel;

public class EntitySelect extends EntityDefaultNPC {
    public boolean isClick;
    public EntitySelect(World world) {
        super(world);
        typeModel = TypeModel.NONE;
        this.setSize(1, 1);
    }

    @Override
    public boolean getAlwaysRenderNameTag() {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        isClick = true;
        PlatEffect.killSelect();
        return super.attackEntityFrom(source, amount);

    }
}
