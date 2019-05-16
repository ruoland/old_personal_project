package minigameLib.minigame.elytra;

import minigameLib.MiniGame;
import olib.api.Direction;
import olib.api.EntityAPI;
import olib.api.PosHelper;
import olib.fakeplayer.EntityFakePlayer;
import olib.fakeplayer.FakePlayerHelper;
import minigameLib.minigame.elytra.playerarrow.EntityElytraArrow;
import minigameLib.minigame.elytra.playerarrow.EntityTNTArrow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;

public class ElytraEvent {

    protected static ItemStack bomberStack = new ItemStack(Blocks.TNT);

    @SubscribeEvent
    public void login(LivingDeathEvent event) {
        if (MiniGame.elytra.isStart() && event.getEntityLiving() instanceof EntityFakePlayer) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiElytraGameOver(Elytra.killCount));
            Minecraft.getMinecraft().mouseHelper.ungrabMouseCursor();
        }
    }

    @SubscribeEvent
    public void login(PlayerInteractEvent event) {
        if (MiniGame.elytra.isStart() && (event instanceof PlayerInteractEvent.RightClickItem || event instanceof PlayerInteractEvent.RightClickEmpty) && Elytra.elytraCooltime == 0 && event.getHand() == EnumHand.MAIN_HAND
                && event.getSide() == Side.SERVER && !event.getWorld().isRemote) {
            spawnArrow(Direction.FORWARD);
            if (Elytra.tripleArrow) {
                spawnArrow(Direction.FORWARD_RIGHT);
                spawnArrow(Direction.FORWARD_LEFT);
            }
        }
    }

    public EntityElytraArrow spawnArrow(Direction direction) {
        return spawnArrow(direction, FakePlayerHelper.fakePlayer.rotationYaw);
    }

    public EntityElytraArrow spawnArrow(float yaw) {
        return spawnArrow(null, yaw);
    }

    public EntityElytraArrow spawnArrow(Direction direction, float yaw) {
        EntityFakePlayer fakePlayer = FakePlayerHelper.fakePlayer;
        PosHelper posHelper = new PosHelper(fakePlayer);
        World world = fakePlayer.worldObj;
        EntityElytraArrow arrow = new EntityElytraArrow(world, fakePlayer);
        System.out.println(fakePlayer.rotationPitch+" - "+yaw);
        arrow.setAim(fakePlayer, fakePlayer.rotationPitch, yaw, 0, 0.7F, 1F);
        arrow.motionX -= fakePlayer.motionX;
        arrow.motionZ -= fakePlayer.motionZ;
        if (direction != null)
            arrow.setPosition(posHelper.getX(direction, 1, 1, true), fakePlayer.posY, posHelper.getZ(direction, 1, 1, true));
        else
            arrow.setPosition(fakePlayer.posX + fakePlayer.motionX, fakePlayer.posY, fakePlayer.posZ + fakePlayer.motionZ);
        arrow.setDamage(10);
        arrow.setNoGravity(true);
        world.spawnEntityInWorld(arrow);
        Elytra.elytraCooltime = Elytra.defaultCooltime;
        if (Elytra.tntArrow && Elytra.tntCooltime == 0) {
            spawnTNT();
        }
        return arrow;

    }

    public void spawnTNT() {
        EntityFakePlayer player = FakePlayerHelper.fakePlayer;
        PosHelper posHelper = new PosHelper(player);
        EntityTNTArrow arrow = new EntityTNTArrow(player.worldObj);
        arrow.setPosition(posHelper.getX(Direction.RIGHT, 1, 1, true), player.posY+2, posHelper.getZ(Direction.RIGHT, 1, 1, true));
        player.worldObj.spawnEntityInWorld(arrow);
        arrow.setTarget(arrow.getX(Direction.FORWARD, 10, 1, true), player.posY+2, arrow.getZ(Direction.FORWARD, 10, 1, true));

        arrow = new EntityTNTArrow(player.worldObj);
        arrow.setPosition(posHelper.getX(Direction.LEFT, 1, 1, true), player.posY+2, posHelper.getZ(Direction.LEFT, 1, 1, true));
        player.worldObj.spawnEntityInWorld(arrow);
        arrow.setTarget(arrow.getX(Direction.FORWARD, 10, 1, true), player.posY+2, arrow.getZ(Direction.FORWARD, 10, 1, true));

        Elytra.tntCooltime = 50;
    }

    @SubscribeEvent
    public void login(WorldEvent.Unload event) {
        if (MiniGame.elytra.isStart())
            MiniGame.elytra.end();
    }

    @SubscribeEvent
    public void login(LivingEvent.LivingUpdateEvent event) {
        if(!MiniGame.elytra.isStart())
            return;
        if(event.getEntityLiving() instanceof EntityLiving) {
            EntityLiving living = (EntityLiving) event.getEntityLiving();
            living.hurtTime = 0;
            living.hurtResistantTime = 0;
            living.maxHurtResistantTime = 0;
            living.maxHurtTime = 0;
        }
    }
    @SubscribeEvent
    public void login(PlayerTickEvent event) {
        if (!MiniGame.elytra.isStart())
            return;
        if (Keyboard.isKeyDown(Keyboard.KEY_B) && Elytra.bombCooltime == 0) {
            if (bomberStack.stackSize > 0) {
                bomberStack.stackSize--;
                Elytra.bombCooltime = 40;
                for (int i = 0; i < 360; i += 20) {
                    spawnArrow(event.player.rotationYaw + i).setDamage(50);
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
            if (Elytra.elytraCooltime > 0)
                Elytra.elytraCooltime--;
            if (Elytra.tntCooltime > 0)
                Elytra.tntCooltime--;
            if (Elytra.bombCooltime > 0)
                Elytra.bombCooltime--;
        }
    }
}
