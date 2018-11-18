package ruo.minigame.action;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import ruo.cmplus.CMPlus;
import ruo.cmplus.deb.DebAPI;
import ruo.cmplus.test.CMPacketCommand;
import ruo.minigame.ClientProxy;
import ruo.minigame.MiniGame;
import ruo.minigame.api.BlockAPI;
import ruo.minigame.api.LoginEvent;
import ruo.minigame.api.WorldAPI;

import static ruo.minigame.action.ActionEffect.*;

public class ActionEvent {

    @SubscribeEvent
    public void login(LoginEvent e){
        if(!WorldAPI.getCurrentWorldName().equalsIgnoreCase("noworld")) {
            ActionEffect.mapName = WorldAPI.getCurrentWorldName();
            ActionEffect.load();
        }
    }
    @SubscribeEvent
    public void rotate(LivingFallEvent e) {
        if (ActionEffect.canDoubleJump() && e.getEntityLiving() instanceof EntityPlayer) {
            if(!canDoubleJump)
                e.setDistance(0);
        }
    }
    @SubscribeEvent
    public void rotate(LivingEvent.LivingJumpEvent e) {
        if (ActionEffect.canDoubleJump() && e.getEntityLiving() instanceof EntityPlayer) {
            isPlayerJump = true;
            DebAPI.msgText("MiniGame","점프함"+e.getEntityLiving());

        }

    }
    @SubscribeEvent
    public void playerTick(PlayerTickEvent event) {
        if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            GameSettings gs = Minecraft.getMinecraft().gameSettings;

            if (ActionEffect.canDoubleJump()) {
                if (!canDoubleJump && event.player.onGround) {
                    DebAPI.msgText("MiniGame", "" + isPlayerJump + !canDoubleJump + event.player.onGround + event.player.motionY + event.player + "땅에 닿음");
                    canDoubleJump = true;
                }
                boolean isCanJump = (canDoubleJump && gs.keyBindJump.isPressed() && !event.player.onGround && isPlayerJump);
                if (isCanJump) {
                    canDoubleJump = false;
                    isPlayerJump = false;
                    System.out.println("더블점프함");
                    event.player.motionY = 0.5F;
                    event.player.fallDistance = 0;
                    DebAPI.msgText("MiniGame", "더블점프함");
                    if (event.player.isSprinting()) {
                        float f = event.player.rotationYaw * 0.017453292F;
                        event.player.motionX -= (double) (MathHelper.sin(f) * 0.2F);
                        event.player.motionZ += (double) (MathHelper.cos(f) * 0.2F);
                    }
                    CMPlus.INSTANCE.sendToServer(new CMPacketCommand("더블점프:"+event.player.getName()));

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
                if(block instanceof BlockLiquid || event.player.worldObj.isAirBlock(event.player.getBedLocation().add(0, -1, 0))){//만약 텔레포트할 위치에 밟을 블럭이 없는 경우
                    BlockAPI blockAPI = WorldAPI.getBlock(event.player.worldObj, event.player.getBedLocation(), 4D);
                    for(int i=0;i<blockAPI.size();i++){
                        if(blockAPI.getBlock(i) instanceof BlockBasePressurePlate){
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
                            if(GrabHelper.getGrabSpeed() < 1.5)
                            GrabHelper.setGrabSpeed((float) (GrabHelper.getGrabSpeed() + 0.001D));
                            event.player.motionY = GrabHelper.getGrabSpeed();
                        }
                    }else
                        event.player.motionY = 0;
                }
            }else if(GrabHelper.wallGrab){
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
            } else if(event.player.height != 1.8F && event.player.eyeHeight != event.player.getDefaultEyeHeight() && event.player.getEntityWorld().isAirBlock(event.player.getPosition().add(0,1,0))){
                event.player.width = 0.6F;
                event.player.height = 1.8F;
                event.player.eyeHeight = event.player.getDefaultEyeHeight();
                return;
            }

        }
    }
}
