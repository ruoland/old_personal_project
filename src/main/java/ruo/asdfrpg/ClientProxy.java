package ruo.asdfrpg;

import ruo.asdfrpg.camp.TileCampFire;
import ruo.asdfrpg.camp.TileCampFireRenderer;
import ruo.asdfrpg.skill.entity.EntityLight;
import ruo.asdfrpg.skill.entity.RenderLight;
import ruo.minigame.api.RenderAPI;

public class ClientProxy extends CommonProxy {

    public void init(){
        RenderAPI.registerRender(EntityLight.class, new RenderLight(1.0F));
        RenderAPI.registerTileEntity(AsdfRPG.campFire, TileCampFire.class, new TileCampFireRenderer());

    }
}
