package rmap.hideandseek;

import minigameLib.map.EntityDefaultNPC;
import net.minecraft.world.World;

//허수아비.
public class EntityFakeHide extends EntityDefaultNPC {
    public EntityFakeHide(World worldIn) {
        super(worldIn);
        setAlwaysRenderNameTag(true);
    }

}
