package minigameLib.minigame.minerun;

import cmplus.camera.Camera;
import cmplus.deb.DebAPI;
import minigameLib.MiniGame;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import oneline.action.ActionEffect;
import oneline.api.Direction;
import oneline.api.EntityAPI;
import oneline.api.PosHelper;
import oneline.api.WorldAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;

import static minigameLib.minigame.minerun.MineRun.*;

public class MineRunEvent {
    public static short respawnTime;
    public int lineLR = 0;
    public int lineUD = 0;

    //line은 왼쪽라인 오른쪽라인으로 갈 수 있는 값을 담고 있음, FB는 Forward Back 앞뒤 값 말함 - 7월 14일
    protected double lineX, lineZ;

    private ItemStack stack = new ItemStack(Items.NETHER_STAR);
    private int pickupCount;
    public static boolean halfMode = false;

    @SubscribeEvent
    public void playerTick(PlayerTickEvent e) {
        if (!MiniGame.minerun.isStart() || e.player.isDead)
            return;
        if (respawnTime == 0 && MineRun.elytraMode() == EnumElytra.RUNNING) {
            if (!runner.isInLava() && !runner.isInWater() && respawnTime <= 0 && MineRun.runner.isNotColliding()) {
                if (!MineRun.runner.isOnLadder()) {
                    e.player.motionX = MineRun.xCoord();//앞으로 나아가게 함 - 7월 14일
                    e.player.motionZ = MineRun.zCoord();
                } else {
                    double posX = e.player.posX + curX + EntityAPI.lookX(e.player, 2.8);
                    double posZ = e.player.posZ + curZ + EntityAPI.lookZ(e.player, 2.8);
                    if(posX != 0)
                    e.player.motionX = MineRun.runner.posX - posX;//앞으로 나아가게 함 - 7월 14일
                    if(posZ != 0)
                    e.player.motionZ = MineRun.runner.posZ - posZ;
                }

                MineRun.setFakePositionUpdate();
                if (e.player.getRidingEntity() != null) {
                    EntityMinecartEmpty minecartEmpty = (EntityMinecartEmpty) e.player.getRidingEntity();
                    minecartEmpty.motionX = MineRun.xCoord();
                    minecartEmpty.motionZ = MineRun.zCoord();
                    if (minecartEmpty.onGround) {
                        minecartEmpty.setCanUseRail(true);
                    }
                }
            } else {
                e.player.motionX = 0;
                e.player.motionZ = 0;

            }
            double runnery = MineRun.runner.posY;
            double playery = e.player.posY;
            int distance = 2;

            double value = runnery + distance - playery;
            if (value != 0)
                e.player.motionY = value / 20;
            else
                e.player.motionY = 0;
        }

        EntityMineRunner runner = MineRun.runner;

        if (MineRun.elytraMode() != EnumElytra.RUNNING && runner != null) {
            if (MineRun.elytraMode() == EnumElytra.ELYTRA) {//이건 런너가 움직이는 모션을 주기 위해서 있음!
                MineRun.setFakePositionUpdate();
                runner.motionX = MineRun.xCoord();//걷는 모션을 주기 위해 있음 - 7월 14일
                runner.motionY = 0;
                runner.motionZ = MineRun.zCoord();//?걷는 모션? 다리를 움직이는 모션 아닌가? 2019년 3월 31일 --- 4월 16일 걷는 모션이 다리를 움직이는 모션이지.. 이걸 왜 생각 못했지?

            }
        }
    }

    @SubscribeEvent
    public void keyInput(KeyInputEvent e) {
        if (!MiniGame.minerun.isStart())
            return;
        PosHelper posHelper = MineRun.playerPosHelper;
        if (MineRun.elytraMode() == EnumElytra.ELYTRA && MineRun.runner != null) {
            if (lineUD < 1 && DebAPI.isKeyDown(Keyboard.KEY_W) && Keyboard.getEventKeyState()) {
                MineRun.setPosition(curX, 1, curZ);
                lineUD++;
                System.out.println(lineUD);
            }
            if (lineUD > -1 && DebAPI.isKeyDown(Keyboard.KEY_S) && Keyboard.getEventKeyState()) {
                MineRun.setPosition(curX, -1, curZ);
                lineUD--;
                System.out.println(lineUD);
            }
            if (lineUD == 0) {
                if (DebAPI.isKeyDown(Keyboard.KEY_W))
                    MineRun.setPosition(curX, 1, curZ);
                if (DebAPI.isKeyDown(Keyboard.KEY_S))
                    MineRun.setPosition(curX, -1, curZ);
            }
            if (lineLR < 1 && DebAPI.isKeyDown(Keyboard.KEY_A) && Keyboard.getEventKeyState()) {
                lineLR++;
                MineRun.setPosition(posHelper.getXZ(Direction.LEFT, absLR(), false));
            }
            if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()) {
                lineLR--;
                MineRun.setPosition(posHelper.getXZ(Direction.RIGHT, absLR(), false));
            }
        }
        if (MineRun.elytraMode() == EnumElytra.RUNNING) {
            if (lineLR < 1 && DebAPI.isKeyDown(Keyboard.KEY_A)) {
                lineLR++;
                if (lineLR == 0) {
                    MineRun.setPosition(posHelper.getXZ(Direction.LEFT, 0, false));
                    System.out.println("posHelper Left" + posHelper.getXZ(Direction.LEFT, 0, false));
                } else if (halfMode)
                    MineRun.setPosition(posHelper.getXZ(Direction.LEFT, absLR() * 1, false));
                else {
                    MineRun.setPosition(posHelper.getXZ(Direction.LEFT, absLR() * 2, false));
                    System.out.println("posHelper Left" + posHelper.getXZ(Direction.LEFT, absLR() * 2, false));
                }
                MineRun.setFakePositionUpdate();

            }

            if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D)) {
                lineLR--;
                if (lineLR == 0) {
                    MineRun.setPosition(posHelper.getXZ(Direction.RIGHT, 0, false));
                    System.out.println("posHelper Left" + posHelper.getXZ(Direction.RIGHT, 0, false));
                } else if (halfMode) {
                    MineRun.setPosition(posHelper.getXZ(Direction.RIGHT, absLR() * 1, false));
                } else {
                    MineRun.setPosition(posHelper.getXZ(Direction.RIGHT, absLR() * 2, false));
                    System.out.println("posHelper Right" + posHelper.getXZ(Direction.RIGHT, absLR() * 2, false));

                }
                MineRun.setFakePositionUpdate();
            }
            System.out.println("lineLR " + lineLR + " - " + absLR());

            if (DebAPI.isKeyDown(Keyboard.KEY_SPACE)) {
                if (MineRun.runner.getRidingEntity() != null) {
                    EntityMinecartEmpty minecartEmpty = (EntityMinecartEmpty) MineRun.runner.getRidingEntity();
                    minecartEmpty.setCanUseRail(false);
                    minecartEmpty.setPosition(minecartEmpty.posX, minecartEmpty.posY + 0.3, minecartEmpty.posZ);
                    minecartEmpty.setPositionAndRotationDirect(minecartEmpty.posX, minecartEmpty.posY + 0.3, minecartEmpty.posZ, minecartEmpty.rotationYaw, minecartEmpty.rotationPitch, 1, false);
                    minecartEmpty.addVelocity(0, 0.42, 0);
                    System.out.println("점프함");
                }
            }
        }
    }

    public double getX(Direction direction) {
        return EntityAPI.getX(WorldAPI.getPlayer(), direction, absLR() * 2, false);
    }

    public double getZ(Direction direction) {
        return EntityAPI.getZ(WorldAPI.getPlayer(), direction, absLR() * 2, false);
    }

    public int absLR() {
        return Math.abs(lineLR);
    }

    public int getLR() {
        return (lineLR);
    }

    @SubscribeEvent
    public void mountCancelEvent(EntityMountEvent e) {
        if (e.isDismounting() && MiniGame.minerun.isStart()) {
            if (e.getEntityBeingMounted() instanceof EntityMinecartEmpty) {
                if (!e.getEntityBeingMounted().isDead)
                    e.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void jumpRunner(KeyInputEvent e) {
        if (!MiniGame.minerun.isStart())
            return;
        GameSettings settings = Minecraft.getMinecraft().gameSettings;
        if (settings.keyBindJump.isKeyDown() && !MineRun.runner.isJumping())
            MineRun.runner.jump();
    }

    @SubscribeEvent
    public void changeElytra(KeyInputEvent e) {
        if (!MiniGame.minerun.isStart())
            return;
        if (DebAPI.isKeyDown(Keyboard.KEY_V)) {//엘리트라 모드로 변경함
            if (MineRun.elytraMode() == EnumElytra.ELYTRA) {
                MineRun.setElytra(EnumElytra.RUNNING);
                System.out.println(MineRun.elytraMode());
                return;
            }
            if (MineRun.elytraMode() == EnumElytra.RUNNING) {
                MineRun.setElytra(EnumElytra.ELYTRA);
                System.out.println(MineRun.elytraMode());
                return;
            }
        }
    }

    @SubscribeEvent
    public void logout(PlayerEvent.PlayerLoggedOutEvent e) {
        if (MiniGame.minerun.isStart())
            MiniGame.minerun.end();
    }


    //@SubscribeEvent
    public void cameraMove(EntityViewRenderEvent.CameraSetup event) {
        if (MiniGame.minerun.isStart() && MineRun.elytraMode() == EnumElytra.RUNNING && (WorldAPI.getPlayer().getRidingEntity() == null)) {
            Camera.getCamera().moveCamera(EntityAPI.lookX(WorldAPI.getPlayer(), 3.5)
                            + getX(lineLR < 0 ? Direction.RIGHT : Direction.LEFT),
                    -1.5, EntityAPI.lookZ(WorldAPI.getPlayer(), 3.5)
                            + getZ(lineLR < 0 ? Direction.RIGHT : Direction.LEFT));
        }
    }


    @SubscribeEvent
    public void playerDeadEvent(LivingHurtEvent event) {
        if (MiniGame.minerun.isStart() && event.getEntityLiving() instanceof EntityPlayer && event.getEntityLiving().getHealth() - event.getAmount() <= 0) {
            if (respawnTime > 0) {
                event.setCanceled(true);
                return;
            }
            Minecraft.getMinecraft().displayGuiScreen(new GuiMRGameOver(pickupCount));
            Minecraft.getMinecraft().mouseHelper.ungrabMouseCursor();
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void itemPickup(EntityItemPickupEvent e) {
        Item pickupItem = e.getItem().getEntityItem().getItem();
        if (pickupItem == Items.NETHER_STAR) {
            pickupCount++;
            e.setCanceled(true);
            EntityItem item = e.getItem();
            item.setPosition(item.posX, item.posY + 2, item.posZ);
        }
        if (pickupItem == Items.POTIONITEM) {
            e.setCanceled(true);
            EntityItem item = e.getItem();
            item.setPosition(item.posX, item.posY + 2, item.posZ);
            e.getEntityPlayer().heal(3);
        }
    }


    //블럭 속에 들어갔을 때 블럭 이미지가 화면에 렌더링 되는 걸 막기 위해 있음
    @SubscribeEvent
    public void renderBlockOverlay(RenderBlockOverlayEvent e) {
        e.setCanceled(MiniGame.minerun.isStart());
    }

    @SubscribeEvent
    public void waterDamage(PlayerTickEvent e) {
        if (!MiniGame.minerun.isStart() || e.player.isDead)
            return;

        if (e.player.isInWater()) {
            e.player.attackEntityFrom(DamageSource.drown, 4);
        }
    }

    @SubscribeEvent
    public void playerRespawn(PlayerTickEvent e) {
        if (!MiniGame.minerun.isStart() || e.player.isDead)
            return;

        if (e.side == Side.SERVER && e.phase == TickEvent.Phase.START && respawnTime > 0) {
            respawnTime--;
            if (MineRun.spawnPoint == null) {
                WorldAPI.addMessage("부활 장소가 없어 게임이 중단됐습니다.");
                MiniGame.minerun.end();
                return;
            }
            if (respawnTime == 60) {
                WorldAPI.teleport(MineRun.spawnPoint.addVector(-EntityAPI.lookX(runner, 3), 2, -EntityAPI.lookZ(runner, 3)));
                lineLR = 0;
                MineRun.setPosition(0, 0, 0);
                MineRun.setFakePositionUpdate();
                WorldAPI.addMessage("3초 뒤에 시작됩니다.");
            }
            if (respawnTime == 40) {
                WorldAPI.addMessage("2초 뒤에 시작됩니다.");
            }
            if (respawnTime == 20) {
                WorldAPI.addMessage("1초 뒤에 시작됩니다.");
            }
            if (respawnTime == 0) {
                e.player.setHealth(e.player.getMaxHealth());
                runner.setHealth(runner.getMaxHealth());
                WorldAPI.command("minerun lava");
            }
            return;
        }
    }
}
