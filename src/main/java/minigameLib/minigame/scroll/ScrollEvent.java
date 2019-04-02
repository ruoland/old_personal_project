package minigameLib.minigame.scroll;

import cmplus.camera.Camera;
import minigameLib.MiniGame;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import oneline.api.WorldAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import map.lopre2.LoPre2;

public class ScrollEvent {
    public ScrollEvent() {
    }

    private Minecraft mc = Minecraft.getMinecraft();
    private GameSettings s = mc.gameSettings;

    @SubscribeEvent
    public void login(WorldEvent.Unload event) {
        if (MiniGame.scroll.isStart()) {
            MiniGame.scroll.end();
            //모드 점프맵용
            WorldAPI.addMessage("카메라 설정이 해제 됐습니다. 다시 횡스크롤 설정으로 돌아가려면 /scroll z 명령어를 입력해주세요.");
        }
    }

    @SubscribeEvent
    public void login(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityArrow) {
            EntityArrow arrow = (EntityArrow) event.getEntity();
            if (arrow.shootingEntity instanceof EntityPlayer) {
                arrow.setDamage(arrow.getDamage() * 1.5);
            }
        }
        //ItemBow bow = (ItemBow) event.getBow().getItem();
        //bow.onPlayerStoppedUsing(event.getBow(), event.getWorld(), event.getEntityPlayer(), 100);
        //event.setAction(ActionResult.newResult(EnumActionResult.SUCCESS, event.getBow()));
    }

    @SubscribeEvent
    public void login(TickEvent.PlayerTickEvent event) {
        Scroll scroll = MiniGame.scroll;
        if (scroll.x && event.player.posZ != scroll.startZ) {
            event.player.setPosition(event.player.posZ, event.player.posY, scroll.startZ);
            System.out.println("위치 조정됨 x");
        }
        if (scroll.z && event.player.posX != scroll.startX) {
            event.player.setPosition(scroll.startX, event.player.posY, event.player.posZ);
            System.out.println("위치 조정됨 z");
        }
    }

    @SubscribeEvent
    public void login(ClientTickEvent event) {
        if (LoPre2.checkWorld() && mc.currentScreen instanceof GuiGameOver) {
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                this.mc.thePlayer.respawnPlayer();
                this.mc.displayGuiScreen((GuiScreen) null);
            }
        }
        if (mc.currentScreen != null || !MiniGame.scroll.isStart())
            return;

        float y = (Mouse.getEventY() - 180F) * 0.1F;
        if (y <= 90 && y >= -90)
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

    public int get() {
        int y = Mouse.getEventY();
        if (y > 90)
            return 270;
        else if (y < 90)
            return 90;
        return 0;
    }
}
