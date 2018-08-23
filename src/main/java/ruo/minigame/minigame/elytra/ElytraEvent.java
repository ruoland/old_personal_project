package ruo.minigame.minigame.elytra;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
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
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.fakeplayer.FakePlayerHelper;
import ruo.minigame.minigame.elytra.miniween.EntityElytraBullet;

import java.util.Random;

public class ElytraEvent {
    private GameSettings gamesettings = Minecraft.getMinecraft().gameSettings;

    boolean elytraMode;
    int elytraCooltime = 0;
    public int killCount, spawnY;

    @SubscribeEvent
    public void login(LivingDeathEvent event) {
        if (MiniGame.elytra.isStart() && FakePlayerHelper.fakePlayer != null && event.getEntityLiving() instanceof EntityFakePlayer) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiElytraGameOver(killCount));
            Minecraft.getMinecraft().mouseHelper.ungrabMouseCursor();
        }
    }

    @SubscribeEvent
    public void login(RenderGameOverlayEvent.Post event) {
        if (MiniGame.elytra.isStart() && FakePlayerHelper.fakePlayer != null && event.getType() == ElementType.ALL) {
            Minecraft.getMinecraft().fontRendererObj.drawString("적 죽인 횟수:" + killCount, 0, 0, 0xFFFFFF);
            Minecraft.getMinecraft().fontRendererObj.drawString("폭탄 갯수:" + Elytra.bombCount, 0, 10, 0xFFFFFF);
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

    @SubscribeEvent
    public void login(PlayerInteractEvent event) {
        if (MiniGame.elytra.isStart() && (event instanceof PlayerInteractEvent.RightClickItem || event instanceof PlayerInteractEvent.RightClickEmpty) && elytraCooltime == 0 && event.getHand() == EnumHand.MAIN_HAND
                && event.getSide() == Side.SERVER && !event.getWorld().isRemote) {
            EntityFakePlayer player = FakePlayerHelper.fakePlayer;
            float yaw = player.getHorizontalFacing().getHorizontalAngle();
            System.out.println("화살 YAW "+yaw);
            spawnArrow();
            if (MiniGame.elytra.arrowUpgrade) {
                spawnArrow(yaw + 30);
                spawnArrow(yaw - 30);
            }
        }
    }

    public void spawnArrow() {
        EntityFakePlayer player = FakePlayerHelper.fakePlayer;
        spawnArrow(player.getHorizontalFacing().getHorizontalAngle());
    }

    public void spawnArrow(float yaw) {
        EntityFakePlayer player = FakePlayerHelper.fakePlayer;
        World world = player.worldObj;
        EntityElytraArrow arrow = new EntityElytraArrow(world, player);
        arrow.setAim(player, player.rotationPitch, yaw, 0, 1.6F, 1F);
        arrow.setPosition(player.posX+player.motionX, player.posY, player.posZ+player.motionZ);
        arrow.setDamage(10);
        arrow.setNoGravity(true);
        world.spawnEntityInWorld(arrow);
        elytraCooltime = 15;
        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT,
                SoundCategory.NEUTRAL, 1.0F, (float) (1.0F / (player.worldObj.rand.nextFloat() * 0.4F + 1.2F) + 0.1666));
    }

    @SubscribeEvent
    public void login(WorldEvent.Unload event) {
        if (MiniGame.elytra.isStart())
            MiniGame.elytra.end();
    }

    @SubscribeEvent
    public void login(PlayerTickEvent event) {
        if (!MiniGame.elytra.isStart() || FakePlayerHelper.fakePlayer == null)
            return;

        EntityPlayer player = event.player;
        BlockPos pos = new BlockPos(player.posX, player.posY - 0.5, player.posZ);
        if (!elytraMode) {
            if (gamesettings.keyBindForward.isKeyDown()) {
                player.setVelocity(player.getLookVec().xCoord * 0.45, 0.340, player.getLookVec().zCoord * 0.45);
            } else if (gamesettings.keyBindBack.isKeyDown() && player.worldObj.isAirBlock(pos)) {
                player.setVelocity(player.getLookVec().xCoord * 0.45, -0.240, player.getLookVec().zCoord * 0.45);
            } else {
                player.setVelocity(player.getLookVec().xCoord * 0.45, 0.04336579, player.getLookVec().zCoord * 0.45);
            }
        }
        if (elytraMode) {
            EntityFakePlayer fakePlayer = FakePlayerHelper.fakePlayer;
            fakePlayer.rotationPitch = player.rotationPitch;
            fakePlayer.rotationYaw = player.rotationYaw;
            fakePlayer.rotationYawHead = player.rotationYawHead;
            FakePlayerHelper.fakePlayer.rotationYaw = WorldAPI.getPlayer().getHorizontalFacing().getHorizontalAngle();

            double veloX = 0, veloZ = 0, speed = 0.2;
            if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
                if (Elytra.bombCount > 0) {
                    Elytra.bombCount--;
                    for (int i = 0; i < 360; i += 30) {
                        spawnArrow(player.rotationYaw + i);
                    }
                }
            }

            if (gamesettings.keyBindLeft.isKeyDown()) {
                veloX += EntityAPI.lookX(fakePlayer, -90, speed);
                veloZ += EntityAPI.lookZ(fakePlayer, -90, speed);
            } else if (gamesettings.keyBindRight.isKeyDown()) {
                veloX += EntityAPI.lookX(fakePlayer, 90, speed);
                veloZ += EntityAPI.lookZ(fakePlayer, 90, speed);
            }
            if (gamesettings.keyBindForward.isKeyDown()) {
                veloX += EntityAPI.lookX(fakePlayer, speed);
                veloZ += EntityAPI.lookZ(fakePlayer, speed);
            } else if (gamesettings.keyBindBack.isKeyDown()) {
                veloX += EntityAPI.lookX(fakePlayer, -speed);
                veloZ += EntityAPI.lookZ(fakePlayer, -speed);
            }
            if (!gamesettings.keyBindLeft.isKeyDown() && !gamesettings.keyBindRight.isKeyDown()
                    && !gamesettings.keyBindForward.isKeyDown()
                    && !gamesettings.keyBindBack.isKeyDown()) {
                veloZ = 0;
                veloX = 0;
            }
            FakePlayerHelper.fakePlayer.setVelocity(veloX, 0, veloZ);
            event.player.setVelocity(0, 0, 0);
        }
        if (event.side == Side.SERVER && elytraCooltime > 0) {
            elytraCooltime--;
        }
    }
}
