package minigameLib.minigame.elytra;

import minigameLib.MiniGame;
import minigameLib.api.RenderAPI;
import minigameLib.api.WorldAPI;
import minigameLib.fakeplayer.FakePlayerHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ElytraRenderEvent {

    @SubscribeEvent
    public void login(RenderGameOverlayEvent.Post event) {
        if (MiniGame.elytra.isStart() && FakePlayerHelper.fakePlayer != null && event.getType() == ElementType.ALL) {
            Minecraft.getMinecraft().fontRendererObj.drawString("점수:" + (Elytra.killCount+Elytra.score), 0, 0, 0xFFFFFF);
            for(int i = 0; i < MiniGame.elytraEvent.bomberStack.stackSize;i++)
            RenderAPI.renderItem(MiniGame.elytraEvent.bomberStack, i*20, 20, false);
        }
    }

    @SubscribeEvent
    public void event(MouseEvent event) {
        if (!MiniGame.elytra.isStart())
            return;
        if (event.getDwheel() == 120) {
            WorldAPI.teleport(WorldAPI.x(), WorldAPI.y() - 0.5, WorldAPI.z());
        }
        if (event.getDwheel() == -120) {
            WorldAPI.teleport(WorldAPI.x(), WorldAPI.y() + 0.5, WorldAPI.z());
        }
    }
}
