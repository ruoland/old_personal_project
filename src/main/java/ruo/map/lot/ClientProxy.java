package ruo.map.lot;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.deb.DebAPI;
import ruo.map.lot.block.TileClock;
import ruo.map.lot.block.TileClockRenderer;
import ruo.map.lot.dungeon.arrow.EntityDunArrow;
import ruo.map.lot.dungeon.arrow.RenderDunArrow;
import ruo.map.lot.dungeon.arrow.TileArrowSpawn;
import ruo.map.lot.dungeon.arrow.TileArrowSpawnRenderer;
import ruo.map.lot.switch2.TileSwitch;
import ruo.map.lot.switch2.TileSwitchRenderer;
import ruo.map.lot.tool.EntityEnderShot;
import ruo.minigame.api.RenderAPI;

public class ClientProxy extends CommonProxy {
    public static KeyBinding keyBindFly = new KeyBinding("날기", Keyboard.KEY_F, "LOT");

    @Override
    public void init() {

        RenderAPI.registerRender(EntityDunArrow.class, new RenderDunArrow(Minecraft.getMinecraft().getRenderManager()));
        RenderAPI.registerRender(EntityEnderShot.class,  new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), LOT.enderShotIem, Minecraft.getMinecraft().getRenderItem()));
        RenderAPI.registerTileEntity(LOT.time, TileClock.class, new TileClockRenderer());
        RenderAPI.registerTileEntity(LOT.arrowspawn, TileArrowSpawn.class, new TileArrowSpawnRenderer());
        RenderAPI.registerTileEntity(LOT.switch2, TileSwitch.class, new TileSwitchRenderer());
    }
}
