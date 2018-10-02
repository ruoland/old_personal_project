package ruo.minigame.minigame.elytra;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;
import ruo.minigame.MiniGame;
import ruo.minigame.api.*;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.fakeplayer.FakePlayerHelper;

public class ElytraRenderEvent {

    @SubscribeEvent
    public void login(RenderGameOverlayEvent.Post event) {
        if (MiniGame.elytra.isStart() && FakePlayerHelper.fakePlayer != null && event.getType() == ElementType.ALL) {
            Minecraft.getMinecraft().fontRendererObj.drawString("점수:" + Elytra.killCount+Elytra.score, 0, 0, 0xFFFFFF);
            RenderAPI.renderItem(MiniGame.elytraEvent.bomberStack, 0, 20);
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
