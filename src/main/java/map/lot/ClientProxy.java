package map.lot;


import oneline.api.RenderAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import map.lot.block.TileClock;
import map.lot.block.TileClockRenderer;
import map.lot.dungeon.arrow.EntityDunArrow;
import map.lot.dungeon.arrow.RenderDunArrow;
import map.lot.dungeon.arrow.TileArrowSpawn;
import map.lot.dungeon.arrow.TileArrowSpawnRenderer;
import map.lot.switch2.TileSwitch;
import map.lot.switch2.TileSwitchRenderer;
import map.lot.tool.EntityEnderShot;

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
