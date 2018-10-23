package ruo.asdfrpg;

import ruo.asdfrpg.CommonProxy;
import ruo.asdfrpg.EntityLight;
import ruo.asdfrpg.camp.TileCampFire;
import ruo.asdfrpg.camp.TileCampFireRenderer;
import ruo.asdfrpg.skill.RenderLight;
import ruo.cmplus.deb.DebAPI;
import ruo.minigame.api.RenderAPI;

public class ClientProxy extends CommonProxy {

    public void init(){
        RenderAPI.registerRender(EntityLight.class, new RenderLight(1.0F));
        RenderAPI.registerTileEntity(AsdfRPG.campFire, TileCampFire.class, new TileCampFireRenderer());

    }
}
