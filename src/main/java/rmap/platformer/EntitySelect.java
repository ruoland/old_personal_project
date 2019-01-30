package rmap.platformer;

import minigameLib.map.EntityDefaultNPC;
import minigameLib.map.TypeModel;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

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
