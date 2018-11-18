package ruo.minigame.minigame.scroll;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import ruo.cmplus.camera.Camera;
import ruo.cmplus.cm.CommandCamera;
import ruo.map.tycoon.GuiDayEnd;
import ruo.minigame.MiniGame;
import ruo.minigame.api.ScriptAPI;

public class ScrollEvent {
    public ScrollEvent() {
    }

    private Minecraft mc = Minecraft.getMinecraft();
    private GameSettings s = mc.gameSettings;
    private int prevX, pitch;

    @SubscribeEvent
    public void login(WorldEvent.Unload event) {
        if (MiniGame.scroll.isStart())
            MiniGame.scroll.end();
    }
    @SubscribeEvent
    public void login(EntityJoinWorldEvent event) {
        if(event.getEntity() instanceof EntityArrow){
            EntityArrow arrow = (EntityArrow) event.getEntity();
            if(arrow.shootingEntity instanceof EntityPlayer){
                arrow.setDamage(arrow.getDamage() * 1.5);
            }
        }
        //ItemBow bow = (ItemBow) event.getBow().getItem();
        //bow.onPlayerStoppedUsing(event.getBow(), event.getWorld(), event.getEntityPlayer(), 100);
        //event.setAction(ActionResult.newResult(EnumActionResult.SUCCESS, event.getBow()));
    }
    @SubscribeEvent
    public void login(ClientTickEvent event) {
        if (mc.currentScreen != null || !MiniGame.scroll.isStart())
            return;

        float y = (Mouse.getEventY()-180F) * 0.1F;
        prevX = Mouse.getEventY();
        //System.out.println(y  + " - " + Mouse.getEventY());
        if(y <= 90 && y >= -90)
        Camera.getCamera().pitch = y;
        else
            Mouse.setCursorPosition(Mouse.getEventX(), get());
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            MiniGame.scroll.pos(Keyboard.KEY_A);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            MiniGame.scroll.pos(Keyboard.KEY_D);
        }
    }
    public int get(){
        int y = Mouse.getEventY();
        if(y > 90)
            return 270;
        else if(y < 90)
            return 90;
    return 0;
    }
}
