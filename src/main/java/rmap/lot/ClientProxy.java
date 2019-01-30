package rmap.lot;


import minigameLib.api.RenderAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import rmap.lot.block.TileClock;
import rmap.lot.block.TileClockRenderer;
import rmap.lot.dungeon.arrow.EntityDunArrow;
import rmap.lot.dungeon.arrow.RenderDunArrow;
import rmap.lot.dungeon.arrow.TileArrowSpawn;
import rmap.lot.dungeon.arrow.TileArrowSpawnRenderer;
import rmap.lot.switch2.TileSwitch;
import rmap.lot.switch2.TileSwitchRenderer;
import rmap.lot.tool.EntityEnderShot;

public class ClientProxy extends CommonProxy {

    @Override
    public void init() {

        RenderAPI.registerRender(EntityDunArrow.class, new RenderDunArrow(Minecraft.getMinecraft().getRenderManager()));
        RenderAPI.registerRender(EntityEnderShot.class,  new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), LOT.enderShotIem, Minecraft.getMinecraft().getRenderItem()));
        RenderAPI.registerTileEntity(LOT.time, TileClock.class, new TileClockRenderer());
        RenderAPI.registerTileEntity(LOT.arrowspawn, TileArrowSpawn.class, new TileArrowSpawnRenderer());
        RenderAPI.registerTileEntity(LOT.switch2, TileSwitch.class, new TileSwitchRenderer());
    }
}
