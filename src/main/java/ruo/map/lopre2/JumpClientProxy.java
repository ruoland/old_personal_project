package ruo.map.lopre2;


import ruo.map.lopre2.jump1.*;
import ruo.map.lopre2.jump2.*;
import ruo.map.lopre2.jump3.EntityLavaSpawnBlock;
import ruo.minigame.api.RenderAPI;

public class JumpClientProxy extends JumpCommonProxy {
    @Override
    public void init() {
        RenderAPI.registerRender(EntityLavaSpawnBlock.class);
        RenderAPI.registerRender(EntityTeleportBlock.class);
        RenderAPI.registerRender(EntityMagmaBlock.class);
        RenderAPI.registerRender(EntityBigInvisibleBlock.class);
        RenderAPI.registerRender(EntityBigBlockMove.class);
        RenderAPI.registerRender(EntitySmallBlock.class);
        RenderAPI.registerRender(EntityBigBlock.class);
        RenderAPI.registerRender(EntityKnockbackBlock.class);
        RenderAPI.registerRender(EntityLavaBlock.class);
        RenderAPI.registerRender(EntityFallingBlock.class);
        RenderAPI.registerRender(EntityMoveBlock.class);
        RenderAPI.registerRender(EntityWaterFlowBlock.class);
        RenderAPI.registerRender(EntityWaterBlockCreator.class);
        RenderAPI.registerRender(EntityPreBlock.class);
        RenderAPI.registerRender(EntityBuildBlock.class);
        RenderAPI.registerRender(EntityInvisibleBlock.class);;
    }
}
