package olib.action;

import cmplus.CMPlus;
import cmplus.deb.DebAPI;
import cmplus.test.CMPacketCommand;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import olib.ClientProxy;
import olib.api.BlockAPI;
import olib.api.LoginEvent;
import olib.api.WorldAPI;
import org.lwjgl.input.Keyboard;

public class ActionEvent {

    /**
     * 맵 데이터를 저장할 때는 맵의 이름이 필요함
     * 맵 이름이 noworld로 올 때도 있고 mpserver로 올 때도 있으므로 미리 로그인할 때 저장함
     */
    @SubscribeEvent
    public void login(LoginEvent e) {
        if (!WorldAPI.getCurrentWorldName().equalsIgnoreCase("noworld")) {
            ActionEffect.load(WorldAPI.getCurrentWorldName());
        }
    }

    @SubscribeEvent
    public void rotate(LivingFallEvent e) {
        if (DoubleJump.canMapDoubleJump() && (e.getEntityLiving() instanceof EntityPlayer)) {
            e.setDistance(e.getDistance() - 3);
        }
    }

    /**
     * 점프를 하면 플레이어가 점프를 한 상태라고 저장
     */
    @SubscribeEvent
    public void rotate(LivingEvent.LivingJumpEvent e) {
        if (DoubleJump.canMapDoubleJump() && (e.getEntityLiving() instanceof EntityPlayer)) {
            DoubleJump.setIsPlayerJump(true);
            DebAPI.msgText("MiniGame", "점프함" + e.getEntityLiving());
        }
    }

    @SubscribeEvent
    public void playerTick(PlayerTickEvent event) {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            if (DoubleJump.canMapDoubleJump()) {
                //더블 점프를 사용한 상태에서 플레이어가 땅에 닿은 경우 초기화
                if (DoubleJump.onDoubleJump() && event.player.onGround) {
                    DoubleJump.resetDoubleJump();
                }
                if (DoubleJump.canUseDoubleJump(event.player)) {
                    DoubleJump.setOnDoubleJump(true);
                    DoubleJump.setIsPlayerJump(false);
                    System.out.println("더블점프함");
                        event.player.motionY = 0.57F;
                        event.player.fallDistance = 0;
                    DebAPI.msgText("MiniGame", "더블점프함");
                  if (event.player.isSprinting()) {
                        float f = event.player.rotationYaw * 0.017453292F;
                        event.player.motionX -= (double) (MathHelper.sin(f) * 0.2F);
                        event.player.motionZ += (double) (MathHelper.cos(f) * 0.2F);
                    }
                    CMPlus.INSTANCE.sendToServer(new CMPacketCommand("더블점프:" + event.player.getName()));
                }
            }
        }
        if (ActionEffect.getYTP() != 0) {
            double tpY = ActionEffect.getYTP();
            BlockPos tpPos = event.player.getBedLocation();
            if (event.player instanceof EntityPlayerMP && tpPos != null && event.player.posY < tpY && tpPos.getY() > event.player.posY) {
                event.player.fallDistance = 0;
                event.player.setHealth(event.player.getMaxHealth());
                WorldAPI.teleport((EntityPlayerMP) event.player, event.player.getBedLocation().add(0, 0.3, 0), ActionEffect.getYaw(), ActionEffect.getPitch());
                Block block = event.player.worldObj.getBlockState(event.player.getBedLocation().add(0, -1, 0)).getBlock();
                if (block instanceof BlockLiquid || event.player.worldObj.isAirBlock(event.player.getBedLocation().add(0, -1, 0))) {//만약 텔레포트할 위치에 밟을 블럭이 없는 경우
                    BlockAPI blockAPI = WorldAPI.getBlock(event.player.worldObj, event.player.getBedLocation(), 4D);
                    for (int i = 0; i < blockAPI.size(); i++) {
                        if (blockAPI.getBlock(i) instanceof BlockBasePressurePlate) {
                            event.player.setSpawnPoint(blockAPI.getPos(i), true);
                            WorldAPI.teleport((EntityPlayerMP) event.player, event.player.getBedLocation().add(0, 1, 0), ActionEffect.getYaw(), ActionEffect.getPitch());
                            break;
                        }
                    }
                }
            }
        }
        if (ActionEffect.canCrawl()) {
            GameSettings gs = Minecraft.getMinecraft().gameSettings;
            if (ClientProxy.grab.isKeyDown()) {
                System.out.println("키 다운 상태");
                GrabHelper.wallGrabCheck(event.player, GrabHelper.wallGrab);
                if (GrabHelper.wallGrab) {
                    KeyBinding bind = Minecraft.getMinecraft().gameSettings.keyBindForward;
                    if (bind.isPressed() || bind.isKeyDown()) {
                        if (!GrabHelper.blockColide) {
                            if (GrabHelper.getGrabSpeed() < 1.5)
                                GrabHelper.setGrabSpeed((float) (GrabHelper.getGrabSpeed() + 0.001D));
                            event.player.motionY = GrabHelper.getGrabSpeed();
                        }
                    } else
                        event.player.motionY = 0;
                }
            } else if (GrabHelper.wallGrab) {
                GrabHelper.ungrab(event.player);
            }
            if ((ClientProxy.grab.isKeyDown() || ClientProxy.grab.isPressed())
                    && (gs.keyBindSneak.isKeyDown() || gs.keyBindSneak.isPressed())) {
                WorldAPI.getPlayer().setAIMoveSpeed(WorldAPI.getPlayer().getAIMoveSpeed() / 2);
                if (event.player.height != 1.8F || event.player.getEyeHeight() != event.player.getDefaultEyeHeight()) {
                    event.player.height = 0.75F;
                    event.player.eyeHeight = (float) 0.75F - 0.42F;
                    event.player
                            .setEntityBoundingBox(new AxisAlignedBB(event.player.getEntityBoundingBox().minX,
                                    event.player.getEntityBoundingBox().minY,
                                    event.player.getEntityBoundingBox().minZ,
                                    event.player.getEntityBoundingBox().minX + event.player.width,
                                    event.player.getEntityBoundingBox().minY + event.player.height,
                                    event.player.getEntityBoundingBox().minZ + event.player.width));
                }
            } else if (event.player.height != 1.8F && event.player.eyeHeight != event.player.getDefaultEyeHeight() && event.player.getEntityWorld().isAirBlock(event.player.getPosition().add(0, 1, 0))) {
                event.player.width = 0.6F;
                event.player.height = 1.8F;
                event.player.eyeHeight = event.player.getDefaultEyeHeight();
                return;
            }

        }
    }

    @SubscribeEvent
    public void client(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().currentScreen == null && Keyboard.isKeyDown(Keyboard.KEY_R) && WorldAPI.getPlayer() != null && WorldAPI.getPlayer().getBedLocation() != null) {
            BlockPos bedLocation = WorldAPI.getPlayerMP().getBedLocation();
            WorldAPI.teleport(bedLocation.getX() + 0.5, bedLocation.getY(), bedLocation.getZ() + 0.5);
            WorldAPI.getPlayerMP().heal(20);
            WorldAPI.getPlayer().fallDistance = 0;
            WorldAPI.getPlayer().getFoodStats().setFoodLevel(20);
            if (WorldAPI.getBlock(bedLocation.add(0, -1, 0)) == Blocks.AIR) {
                BlockAPI blockAPI = WorldAPI.getBlock(WorldAPI.getWorld(), bedLocation, 5);
                for (BlockPos pos : blockAPI.getPosList()) {
                    if (WorldAPI.getBlock(pos) instanceof BlockBasePressurePlate) {
                        WorldAPI.teleport(pos.add(0, 1, 0));
                        break;
                    }
                }
            }
        }
    }
}
