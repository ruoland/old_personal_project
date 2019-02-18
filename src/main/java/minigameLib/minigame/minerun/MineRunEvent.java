package minigameLib.minigame.minerun;

import cmplus.camera.Camera;
import cmplus.deb.DebAPI;
import minigameLib.MiniGame;
import minigameLib.action.ActionEffect;
import minigameLib.api.Direction;
import minigameLib.api.EntityAPI;
import minigameLib.api.PosHelper;
import minigameLib.api.WorldAPI;
import minigameLib.fakeplayer.EntityFakePlayer;
import minigameLib.fakeplayer.FakePlayerHelper;
import minigameLib.minigame.scroll.ScrollEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;

public class MineRunEvent {
    public static short respawnTime;
    //엘리트라 모드1용 - 7월 14일
    public int lineLR = 0, lineFB = 0;

    //엘리트라 모드2용 - 7월 14일
    public int lineUD = 0;

    //line은 왼쪽라인 오른쪽라인으로 갈 수 있는 값을 담고 있음, FB는 Forward Back 앞뒤 값 말함 - 7월 14일
    protected double lineX, lineZ, lineFBX, lineFBZ;

    private ItemStack stack = new ItemStack(Items.NETHER_STAR);
    private int pickupCount;

    @SubscribeEvent
    public void login(EntityViewRenderEvent.CameraSetup event) {
        if (MiniGame.minerun.isStart() && MineRun.elytraMode() == 0 && (WorldAPI.getPlayer().getRidingEntity() == null)) {
            Camera.getCamera().moveCamera(EntityAPI.lookX(WorldAPI.getPlayer(), 3.5)
                            + getX(lineLR < 0 ? Direction.RIGHT : Direction.LEFT),
                    -1.5, EntityAPI.lookZ(WorldAPI.getPlayer(), 3.5)
                            + getZ(lineLR < 0 ? Direction.RIGHT : Direction.LEFT));
        }
    }

    public double getX(Direction direction) {
        return EntityAPI.getX(WorldAPI.getPlayer(), direction, absLR() * 2, false);
    }

    public double getZ(Direction direction) {
        return EntityAPI.getZ(WorldAPI.getPlayer(), direction, absLR() * 2, false);
    }
    @SubscribeEvent
    public void login(LivingHurtEvent event) {
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

    @SubscribeEvent
    public void renderBlockOverlay(RenderBlockOverlayEvent e) {
        e.setCanceled(MiniGame.minerun.isStart());
    }

    @SubscribeEvent
    public void playerTick(PlayerTickEvent e) {
        if (!MiniGame.minerun.isStart() || e.player.isDead)
            return;
        if (e.player.isInWater()) {
            e.player.attackEntityFrom(DamageSource.drown, 4);
        }

        if (e.side == Side.SERVER && e.phase == TickEvent.Phase.START && respawnTime > 0) {
            respawnTime--;

            if (respawnTime == 60) {
                WorldAPI.teleport(new Vec3d(e.player.getBedLocation()).addVector(0.5,0,0));
                WorldAPI.addMessage("3초 뒤에 시작됩니다.");
            }
            if (respawnTime == 40) {
                WorldAPI.addMessage("2초 뒤에 시작됩니다.");
            }
            if (respawnTime == 20) {
                WorldAPI.addMessage("1초 뒤에 시작됩니다.");
            }
            if (respawnTime == 0) {
                PosHelper posHelper = new PosHelper(WorldAPI.getPlayer());
                e.player.setHealth(e.player.getMaxHealth());
                if (lineLR == 1) {
                    MineRun.setPosition(posHelper.getXZ(Direction.LEFT, 1, false));
                }
                if (lineLR == -1)
                    MineRun.setPosition(posHelper.getXZ(Direction.RIGHT, 1, false));
                lineLR = 0;
                WorldAPI.command("minerun lava");
            }
            return;
        }
        EntityFakePlayer fakePlayer = FakePlayerHelper.fakePlayer;
        if (MineRun.elytraMode() == 0) {
            if (!e.player.isInLava() && !e.player.isInWater() && respawnTime <= 0) {
                e.player.motionX = MineRun.xCoord();//앞으로 나아가게 함 - 7월 14일
                e.player.motionZ = MineRun.zCoord();
                if(e.player.getRidingEntity() != null){
                    EntityMinecartEmpty minecartEmpty = (EntityMinecartEmpty) e.player.getRidingEntity();
                    minecartEmpty.motionX= MineRun.xCoord();
                    minecartEmpty.motionZ= MineRun.zCoord();
                    if(minecartEmpty.onGround){
                        minecartEmpty.setCanUseRail(true);
                    }
                }
            }
        }
        if (MineRun.elytraMode() > 0 && FakePlayerHelper.fakePlayer != null) {
            if (MineRun.elytraMode() == 2) {
                fakePlayer.motionY = 0;
                MineRun.setFakePositionUpdate();
                fakePlayer.motionX = MineRun.xCoord();//걷는 모션을 주기 위해 있음 - 7월 14일
                fakePlayer.motionZ = MineRun.zCoord();
            }
            if (MineRun.elytraMode() == 1) {
                //엘리트라 모드1은 아래에서 위를 봄
                //플레이어를 페이크 아래에 배치함
                e.player.motionY = 0.1;
                MineRun.setFakePositionUpdate();
                fakePlayer.fallDistance = 0;
            }
            if (fakePlayer.fallDistance > 1242345 && e.player.getBedLocation(0) != null) {//추락시 스폰지점으로 - 7월 14일
                fakePlayer.fallDistance = 0;
                ActionEffect.teleportSpawnPoint(WorldAPI.getPlayer());
                MineRun.setFakePositionUpdate();
            }
        }
    }
    @SubscribeEvent
    public void keyInput(EntityMountEvent e) {
        if(e.isDismounting() && MiniGame.minerun.isStart()){
            if(e.getEntityBeingMounted() instanceof EntityMinecartEmpty){
                if(!e.getEntityBeingMounted().isDead)
                e.setCanceled(true);
            }
        }
    }
    @SubscribeEvent
    public void keyInput(KeyInputEvent e) {
        if (!MiniGame.minerun.isStart())
            return;
        PosHelper posHelper = MineRun.playerPosHelper;
        if (DebAPI.isKeyDown(Keyboard.KEY_V)) {//엘리트라 모드로 변경함
            if (MineRun.elytraMode() == 2) {
                MineRun.setElytra(1);
                System.out.println(MineRun.elytraMode());
                return;
            }
            if (MineRun.elytraMode() == 1) {
                MineRun.setElytra(0);
                System.out.println(MineRun.elytraMode());
                return;
            }
            if (MineRun.elytraMode() == 0) {
                MineRun.setElytra(2);
                System.out.println(MineRun.elytraMode());
                return;
            }
        }
        if (MineRun.elytraMode() > 0 && FakePlayerHelper.fakePlayer != null) {
            if (MineRun.elytraMode() == 2) {
                if (lineUD < 1 && DebAPI.isKeyDown(Keyboard.KEY_W) && Keyboard.getEventKeyState()) {
                    MineRun.setPosition(MineRun.curX, 1, MineRun.curZ);
                    lineUD++;
                    System.out.println(lineUD);
                }
                if (lineUD > -1 && DebAPI.isKeyDown(Keyboard.KEY_S) && Keyboard.getEventKeyState()) {
                    MineRun.setPosition(MineRun.curX, -1, MineRun.curZ);
                    lineUD--;
                    System.out.println(lineUD);
                }
                if (lineUD == 0) {
                    if (DebAPI.isKeyDown(Keyboard.KEY_W))
                        MineRun.setPosition(MineRun.curX, 1, MineRun.curZ);
                    if (DebAPI.isKeyDown(Keyboard.KEY_S))
                        MineRun.setPosition(MineRun.curX, -1, MineRun.curZ);
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
            if (MineRun.elytraMode() == 1) {
                if (lineFB < 1 && DebAPI.isKeyDown(Keyboard.KEY_W) && Keyboard.getEventKeyState()) {
                    lineFB++;
                    //abs는 앞으로만 이동하게 하기 위해서 함
                    MineRun.setPosition(EntityAPI.forwardX(WorldAPI.getPlayer(), absFB(), false), 0, EntityAPI.forwardZ(WorldAPI.getPlayer(), absFB(), false));
                    if (lineLR == 1) {
                        MineRun.setPosition(posHelper.getXZ(Direction.FORWARD_LEFT, absLR(), false));
                    }
                    if (lineLR == -1) {
                        MineRun.setPosition(posHelper.getXZ(Direction.FORWARD_RIGHT, absLR(), false));
                    }
                }
                if (lineFB > -1 && DebAPI.isKeyDown(Keyboard.KEY_S) && Keyboard.getEventKeyState()) {
                    lineFB--;
                    MineRun.setPosition(EntityAPI.backX(WorldAPI.getPlayer(), absFB(), false), 0, EntityAPI.backZ(WorldAPI.getPlayer(), absFB(), false));
                    if (lineLR == 1) {
                        MineRun.setPosition(posHelper.getXZ(Direction.BACK_LEFT, absLR(), false));
                    }
                    if (lineLR == -1) {
                        MineRun.setPosition(posHelper.getXZ(Direction.BACK_RIGHT, absLR(), false));
                    }
                }
                if (lineLR < 1 && DebAPI.isKeyDown(Keyboard.KEY_A) && Keyboard.getEventKeyState()) {
                    lineLR++;
                    MineRun.setPosition(posHelper.getXZ(Direction.LEFT, absLR(), false));
                    if (lineFB == 1) {
                        MineRun.setPosition(posHelper.getXZ(Direction.FORWARD_LEFT, absLR(), false));
                    }
                    if (lineFB == -1) {
                        MineRun.setPosition(posHelper.getXZ(Direction.BACK_LEFT, absLR(), false));
                    }
                }
                if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()) {
                    lineLR--;
                    MineRun.setPosition(posHelper.getXZ(Direction.RIGHT, absLR(), false));

                    if (lineFB == 1) {
                        MineRun.setPosition(posHelper.getXZ(Direction.FORWARD_LEFT, absLR(), false));
                    }
                    if (lineFB == -1) {
                        MineRun.setPosition(posHelper.getXZ(Direction.FORWARD_RIGHT, absLR(), false));
                    }
                }
                System.out.println(lineLR + " - " + lineFB);

            }
        }
        if (MineRun.elytraMode() == 0) {
            if (lineLR < 1 && DebAPI.isKeyDown(Keyboard.KEY_A)) {
                lineLR++;
                boolean isLR = false;
                if (lineLR == 0) {
                    lineLR++;
                    isLR = true;
                }
                MineRun.setPosition(posHelper.getXZ(Direction.LEFT, absLR() * 2, false));
                if (isLR)
                    lineLR--;
            }

            if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D)) {
                lineLR--;
                boolean isLR = false;
                if (lineLR == 0) {//가운데로 보내기 위해서 1 깎음
                    lineLR--;
                    isLR = true;
                }
                MineRun.setPosition(posHelper.getXZ(Direction.RIGHT, absLR() * 2, false));
                if (isLR)
                    lineLR++;
            }
            if (DebAPI.isKeyDown(Keyboard.KEY_SPACE)) {
                if(WorldAPI.getPlayer().getRidingEntity() != null) {
                    EntityMinecartEmpty minecartEmpty = (EntityMinecartEmpty) WorldAPI.getPlayer().getRidingEntity();
                    minecartEmpty.setCanUseRail(false);
                    minecartEmpty.setPosition(minecartEmpty.posX,minecartEmpty.posY+0.3,minecartEmpty.posZ);
                    minecartEmpty.setPositionAndRotationDirect(minecartEmpty.posX,minecartEmpty.posY+0.3,minecartEmpty.posZ, minecartEmpty.rotationYaw, minecartEmpty.rotationPitch, 1, false);
                    minecartEmpty.addVelocity(0, 0.3, 0);
                    System.out.println("점프함");


                }
            }
        }

    }

    public int absFB() {
        return Math.abs(lineFB);
    }

    public int absLR() {
        return Math.abs(lineLR);
    }

    public int getLR() {
        return (lineLR);
    }

    @SubscribeEvent
    public void logout(PlayerEvent.PlayerLoggedOutEvent e) {
        if (MiniGame.minerun.isStart())
            MiniGame.minerun.end();
    }
}
