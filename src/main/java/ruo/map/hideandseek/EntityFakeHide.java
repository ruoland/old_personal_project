package ruo.map.hideandseek;

import net.minecraft.world.World;
import ruo.minigame.map.EntityDefaultNPC;

//허수아비.
public class EntityFakeHide extends EntityDefaultNPC {
    public EntityFakeHide(World worldIn) {
        super(worldIn);
        setAlwaysRenderNameTag(true);
    }

}
