package map.hideandseek;

import oneline.map.EntityDefaultNPC;
import net.minecraft.world.World;

//허수아비.
public class EntityFakeHide extends EntityDefaultNPC {
    public EntityFakeHide(World worldIn) {
        super(worldIn);
        setAlwaysRenderNameTag(true);
    }

}
