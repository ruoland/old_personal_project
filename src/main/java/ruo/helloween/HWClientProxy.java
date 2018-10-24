package ruo.helloween;

import ruo.helloween.miniween.*;
import ruo.minigame.api.RenderAPI;

public class HWClientProxy extends HWCommonProxy {
    @Override
    public void init() {
        RenderAPI.registerRender(EntityPlayerWeen.class);
        RenderAPI.registerRender(EntityWeen.class);
        RenderAPI.registerRender(EntityMiniWeen.class);
        RenderAPI.registerRender(EntityAttackMiniWeen.class);
        RenderAPI.registerRender(EntityDefenceMiniWeen.class);
        RenderAPI.registerRender(EntityNightMiniWeen.class);
        RenderAPI.registerRender(EntityFallingMiniWeen.class);
        RenderAPI.registerRender(EntityBigWeen.class);
    }
}
