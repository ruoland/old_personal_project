package ruo.minigame.minigame.elytra_scroll;

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
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.deb.DebAPI;
import ruo.minigame.MiniGame;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.PosHelper;
import ruo.minigame.api.SpawnDirection;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.fakeplayer.FakePlayerHelper;

public class ElytraScrollEvent {
    private GameSettings gamesettings = Minecraft.getMinecraft().gameSettings;

    public int elytraCooltime = 0, defaultCooltime = 15;
    public int killCount, spawnY;

    @SubscribeEvent
    public void login(RenderGameOverlayEvent.Post event) {
        if (MiniGame.elytra.isStart() && FakePlayerHelper.fakePlayer != null && event.getType() == ElementType.ALL) {
            Minecraft.getMinecraft().fontRendererObj.drawString("적 죽인 횟수:" + killCount, 0, 0, 0xFFFFFF);
            Minecraft.getMinecraft().fontRendererObj.drawString("폭탄 갯수:" + ElytraScroll.bombCount, 0, 10, 0xFFFFFF);
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
            spawnArrow();
            if (ElytraScroll.tripleArrow) {
                spawnArrow(SpawnDirection.FORWARD_RIGHT);
                spawnArrow(SpawnDirection.FORWARD_LEFT);
            }
        }
    }

    public void spawnArrow() {
        spawnArrow(SpawnDirection.FORWARD);
    }

    public void spawnArrow(SpawnDirection direction) {
        spawnArrow(direction, FakePlayerHelper.fakePlayer.rotationYaw);
    }

    public void spawnArrow(float yaw) {
        spawnArrow(null, yaw);
    }

    public void spawnArrow(SpawnDirection direction, float yaw) {
        EntityFakePlayer player = FakePlayerHelper.fakePlayer;
        PosHelper posHelper = new PosHelper(player);
        World world = player.worldObj;
        EntityElytraArrow arrow = new EntityElytraArrow(world, player);
        arrow.setAim(player, player.rotationPitch, yaw, 0, 1.6F, 1F);
        if (direction != null)
            arrow.setPosition(posHelper.getX(direction, 1, 1, true), player.posY, posHelper.getZ(direction, 1, 1, true));
        else
            arrow.setPosition(player.posX + player.motionX, player.posY, player.posZ + player.motionZ);
        arrow.setDamage(10);
        arrow.setNoGravity(true);
        world.spawnEntityInWorld(arrow);
        elytraCooltime = defaultCooltime;
        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT,
                SoundCategory.NEUTRAL, 1.0F, (float) (1.0F / (player.worldObj.rand.nextFloat() * 0.4F + 1.2F) + 0.1666));
    }

    @SubscribeEvent
    public void login(WorldEvent.Unload event) {
        if (MiniGame.elytraScroll.isStart())
            MiniGame.elytraScroll.end();
    }

    @SubscribeEvent
    public void login(PlayerTickEvent event) {
        if (!MiniGame.elytraScroll.isStart() || FakePlayerHelper.fakePlayer == null)
            return;

        EntityFakePlayer player = FakePlayerHelper.fakePlayer;
        player.setVelocity(0,0,0);
        if (DebAPI.isKeyDown(Keyboard.KEY_W)) {
            player.setVelocity(player.getLookVec().xCoord * 0.45, 0.340, player.getLookVec().zCoord * 0.45);
        }
        if (DebAPI.isKeyDown(Keyboard.KEY_S)) {
            player.setVelocity(player.getLookVec().xCoord * 0.45, -0.240, player.getLookVec().zCoord * 0.45);
        }
        if (DebAPI.isKeyDown(Keyboard.KEY_D)) {
            player.setVelocity(player.getLookVec().xCoord * 0.75, 0, player.getLookVec().zCoord * 0.45);
        }
        if (DebAPI.isKeyDown(Keyboard.KEY_A)) {
            player.setVelocity(player.getLookVec().xCoord * 0.45, -0.240, player.getLookVec().zCoord * 0.45);
        }

        if (event.side == Side.SERVER && elytraCooltime > 0) {
            elytraCooltime--;
        }
    }
}
