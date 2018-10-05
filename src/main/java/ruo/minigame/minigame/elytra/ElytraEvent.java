package ruo.minigame.minigame.elytra;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;
import ruo.minigame.MiniGame;
import ruo.minigame.api.Direction;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.PosHelper;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.fakeplayer.FakePlayerHelper;
import ruo.minigame.minigame.elytra.playerarrow.EntityTNTArrow;
import ruo.minigame.minigame.elytra.playerarrow.EntityElytraArrow;

import static ruo.minigame.minigame.elytra.Elytra.*;

public class ElytraEvent {

    protected ItemStack bomberStack = new ItemStack(Blocks.TNT);

    @SubscribeEvent
    public void login(LivingDeathEvent event) {
        if (MiniGame.elytra.isStart() && event.getEntityLiving() instanceof EntityFakePlayer) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiElytraGameOver(Elytra.killCount));
            Minecraft.getMinecraft().mouseHelper.ungrabMouseCursor();
        }
    }

    @SubscribeEvent
    public void login(PlayerInteractEvent event) {
        if (MiniGame.elytra.isStart() && (event instanceof PlayerInteractEvent.RightClickItem || event instanceof PlayerInteractEvent.RightClickEmpty) && elytraCooltime == 0 && event.getHand() == EnumHand.MAIN_HAND
                && event.getSide() == Side.SERVER && !event.getWorld().isRemote) {
            spawnArrow(Direction.FORWARD);
            if (Elytra.tripleArrow) {
                spawnArrow(Direction.FORWARD_RIGHT);
                spawnArrow(Direction.FORWARD_LEFT);
            }
        }
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
        elytraCooltime = defaultCooltime;
        if (tntArrow && Elytra.tntCooltime == 0) {
            spawnTNT();
        }

    }

    public void spawnTNT() {
        EntityFakePlayer player = FakePlayerHelper.fakePlayer;
        PosHelper posHelper = new PosHelper(player);
        EntityTNTArrow arrow = new EntityTNTArrow(player.worldObj);
        arrow.setPosition(posHelper.getX(Direction.RIGHT, 1, 1, true), player.posY, posHelper.getZ(Direction.RIGHT, 1, 1, true));
        player.worldObj.spawnEntityInWorld(arrow);
        arrow = new EntityTNTArrow(player.worldObj);
        arrow.setPosition(posHelper.getX(Direction.LEFT, 1, 1, true), player.posY, posHelper.getZ(Direction.LEFT, 1, 1, true));
        player.worldObj.spawnEntityInWorld(arrow);
        Elytra.tntCooltime = 30;
    }

    @SubscribeEvent
    public void login(WorldEvent.Unload event) {
        if (MiniGame.elytra.isStart())
            MiniGame.elytra.end();
    }

    @SubscribeEvent
    public void login(PlayerTickEvent event) {
        if (!MiniGame.elytra.isStart())
            return;
        if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
            if (bomberStack.stackSize > 0) {
                bomberStack.stackSize--;
                for (int i = 0; i < 360; i += 30) {
                    spawnArrow(event.player.rotationYaw + i);
                }
            }
        }

        EntityFakePlayer fakePlayer = FakePlayerHelper.fakePlayer;
        GameSettings gamesettings = Minecraft.getMinecraft().gameSettings;
        double veloX = 0, veloZ = 0, speed = 0.2;

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
        if (event.side == Side.SERVER) {
            if (elytraCooltime > 0)
                elytraCooltime--;
            if (tntCooltime > 0)
                tntCooltime--;
        }
    }
}
