package ruo.minigame.minigame.starmine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
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
import ruo.minigame.api.Direction;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.fakeplayer.FakePlayerHelper;

public class StarMineEvent {
    private GameSettings gamesettings = Minecraft.getMinecraft().gameSettings;

    public int elytraCooltime = 0;
    public int killCount, spawnY;

    @SubscribeEvent
    public void login(RenderGameOverlayEvent.Post event) {
        if (MiniGame.starMine.isStart() && FakePlayerHelper.fakePlayer != null && event.getType() == ElementType.ALL) {
            Minecraft.getMinecraft().fontRendererObj.drawString("적 죽인 횟수:" + killCount, 0, 0, 0xFFFFFF);
            Minecraft.getMinecraft().fontRendererObj.drawString("폭탄 갯수:" + StarMine.bombCount, 0, 10, 0xFFFFFF);
        }
    }

    @SubscribeEvent
    public void event(MouseEvent event) {
        if (!MiniGame.starMine.isStart())
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
        if (MiniGame.starMine.isStart() && (event instanceof PlayerInteractEvent.RightClickItem || event instanceof PlayerInteractEvent.RightClickEmpty) && elytraCooltime == 0 && event.getHand() == EnumHand.MAIN_HAND
                && event.getSide() == Side.SERVER && !event.getWorld().isRemote) {
            spawnArrow();
            if (StarMine.tripleArrow) {
                spawnArrow(Direction.FORWARD_RIGHT);
                spawnArrow(Direction.FORWARD_LEFT);
            }
        }
    }

    public void spawnArrow() {
        spawnArrow(Direction.FORWARD);
    }

    public void spawnArrow(Direction direction) {
        spawnArrow(direction, FakePlayerHelper.fakePlayer.rotationYaw);
    }

    public void spawnArrow(float yaw) {
        spawnArrow(null, yaw);
    }

    public void spawnArrow(Direction direction, float yaw) {
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
        elytraCooltime = 5;
        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT,
                SoundCategory.NEUTRAL, 1.0F, (float) (1.0F / (player.worldObj.rand.nextFloat() * 0.4F + 1.2F) + 0.1666));
    }

    @SubscribeEvent
    public void login(WorldEvent.Unload event) {
        if (MiniGame.starMine.isStart())
            MiniGame.starMine.end();
    }

    @SubscribeEvent
    public void login(PlayerTickEvent event) {
        if (!MiniGame.starMine.isStart() || FakePlayerHelper.fakePlayer == null)
            return;

        EntityFakePlayer fakePlayer = FakePlayerHelper.fakePlayer;
        fakePlayer.setVelocity(0,0,0);
        double veloX = 0, veloY = 0, veloZ = 0, speed = 0.2;

        if (gamesettings.keyBindLeft.isKeyDown()) {
            veloX += EntityAPI.lookX(fakePlayer, -90, speed);
            veloZ += EntityAPI.lookZ(fakePlayer, -90, speed);
        } else if (gamesettings.keyBindRight.isKeyDown()) {
            veloX += EntityAPI.lookX(fakePlayer, 90, speed);
            veloZ += EntityAPI.lookZ(fakePlayer, 90, speed);
        }
        if (gamesettings.keyBindForward.isKeyDown()) {
            veloY += 0.3;
        } else if (gamesettings.keyBindBack.isKeyDown()) {
            veloY -= 0.3;
        }
        if (!gamesettings.keyBindLeft.isKeyDown() && !gamesettings.keyBindRight.isKeyDown()
                && !gamesettings.keyBindForward.isKeyDown()
                && !gamesettings.keyBindBack.isKeyDown()) {
            veloZ = 0;
            veloY = 0;
            veloX = 0;
        }
        FakePlayerHelper.fakePlayer.setVelocity(veloX, veloY, veloZ);
        event.player.setVelocity(0, 0, 0);

        if (event.side == Side.SERVER && elytraCooltime > 0) {
            elytraCooltime--;
        }
    }
}
