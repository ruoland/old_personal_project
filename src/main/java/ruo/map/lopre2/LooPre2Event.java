package ruo.map.lopre2;

import net.minecraft.block.Block;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.DamageSource;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.deb.DebAPI;
import ruo.map.lopre2.jump1.EntityLavaBlock;
import ruo.minigame.api.LoginEvent;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.Move;
import ruo.minigame.minigame.scroll.EntityJumpCreeper;


public class LooPre2Event {
    private int currentStage;
    public static int deathCount, gamemodeCount, spawnCount, healCount;
    private double startY;
    private boolean isDown;
    public static String asdf;
    @SubscribeEvent
    public void waterJump(ServerChatEvent e) {
        asdf =e.getMessage();
    }
    @SubscribeEvent
    public void waterJump(LivingUpdateEvent e) {
        if (LoPre2.checkWorld() && e.getEntityLiving().isServerWorld() && e.getEntityLiving() instanceof EntityPlayer && e.getEntityLiving().isInWater()) {
            if (e.getEntityLiving().onGround) {
                startY = e.getEntityLiving().posY;
            }
            if ((startY + 1 <= e.getEntityLiving().posY || isDown) && Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown()) {
                e.getEntityLiving().motionY -= 0.04;
                isDown = true;
                if (e.getEntityLiving().onGround) {
                    startY = e.getEntityLiving().posY;
                    isDown = false;
                }
            }
        }
    }
    @SubscribeEvent
    public void a(ServerChatEvent event) {
        String dis = event.getMessage();
        if (dis != null) {
            if(WorldAPI.equalsHeldItem(LoPre2.itemCopy) && event.getPlayer().isServerWorld()) {
                if (dis.startsWith("yst")) {
                    ItemStack itemStackIn = event.getPlayer().getHeldItemMainhand();
                    NBTTagCompound compound = itemStackIn.hasTagCompound() ? itemStackIn.getTagCompound() : itemStackIn.serializeNBT();
                    compound.setBoolean("YONOFF", !compound.getBoolean("YONOFF"));
                    itemStackIn.setTagCompound(compound);
                }
                if (dis.startsWith("prev")) {
                    ItemStack itemStackIn = event.getPlayer().getHeldItemMainhand();
                    NBTTagCompound compound = itemStackIn.hasTagCompound() ? itemStackIn.getTagCompound() : itemStackIn.serializeNBT();
                    compound.setBoolean("PREV", !compound.getBoolean("PREV"));
                    itemStackIn.setTagCompound(compound);
                }
            }
            if (dis.startsWith("bu")) {
                CommandJB.isDebMode = true;
                for(EntityPreBlock preBlock : ItemCopy.getPreBlockList()){
                    preBlock.setDead();
                }
            }
            if (dis.startsWith("x:")) {
                CommandJB.isDebMode = true;
                ItemSpanner.debX = Double.valueOf(dis.replace("x:", ""));
            }
            if (dis.startsWith("z:")) {
                ItemSpanner.debZ = Double.valueOf(dis.replace("z:", ""));
            }
            if (dis.startsWith("dis:")) {
                CommandJB.isDebMode = true;
                ItemCopy.setDistance(Integer.valueOf(dis.replace("dis:", "")));
            }
            if (dis.startsWith("yinte:")) {
                CommandJB.isDebMode = true;
                ItemCopy.setYinterval(Double.valueOf(dis.replace("yinte:", "")));
            }
            if (dis.startsWith("inte:")) {
                CommandJB.isDebMode = true;
                ItemCopy.setInterval(Double.valueOf(dis.replace("inte:", "")));
            }
        }
    }

    @SubscribeEvent
    public void deathCount(LivingDeathEvent event) {
        if (LoPre2.checkWorld()) {
            if (event.getEntityLiving() instanceof EntityPlayer) {
                deathCount++;
            }
        }
    }

    @SubscribeEvent
    public void gamemode(CommandEvent event) {
        if (LoPre2.checkWorld())
        {
            ICommand command = event.getCommand();
            String name = command.getCommandName();
            if(event.getSender() instanceof EntityPlayer) {
                if (name.equalsIgnoreCase("gamemode") && event.getParameters()[0].equalsIgnoreCase("1")) {
                    gamemodeCount++;
                }
                if (name.equalsIgnoreCase("spawnpoint")) {
                    spawnCount++;
                }
                if (name.equalsIgnoreCase("heal")) {
                    healCount++;
                }
            }
        }
    }

    @SubscribeEvent
    public void a(CommandEvent event) {
        if (LoPre2.checkWorld()) {
            if (event.getCommand().getCommandName().equalsIgnoreCase("waypoint")) {
                if (event.getParameters()[0].indexOf("stage") != -1) {
                    try {
                        currentStage = Integer.valueOf(event.getParameters()[0].replace("stage", ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("현재 스테이지 " + currentStage);
            }
        }
    }
    public static boolean nightVision = true, posYDead = true, fireAttack;
    @SubscribeEvent
    public void a(PlayerTickEvent event) {
        if (LoPre2.checkWorld()) {
            if(event.player.getDistance(1195.1, 4.4, -541.8) < 2){
                event.player.addStat(LoPre2.achievementPoorBlock);
            }
            event.player.setAir(0);
            event.player.worldObj.setRainStrength(0);
            if(fireAttack) {
                event.player.extinguish();
            }
            /*
            if (nightVision && !event.player.isInLava() && (currentStage != 9 && currentStage != 10 && currentStage != 11)) {
                event.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 2000));
            } else
                event.player.removePotionEffect(MobEffects.NIGHT_VISION);
            */
            if (posYDead && event.player.posY < 0) {
                event.player.setHealth(0);
            }
        }
    }

    @SubscribeEvent
    public void renderUI(RenderGameOverlayEvent.Post event) {
        if (LoPre2.checkWorld()) {
            if (event.getType() == ElementType.ALL) {
                Minecraft.getMinecraft().fontRendererObj.drawString("도움이 필요하면 /jb help", 0, 0,
                        0xFFFFFF);
                if (ItemSpanner.renderText != null) {
                    String[] split = ItemSpanner.renderText.split("/line/");
                    for (int i = 0; i < split.length; i++) {
                        Minecraft.getMinecraft().fontRendererObj.drawString(split[i].replace("/line/", ""), 0, 100 + (i * 10),
                                0xFFFFFF);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void event(LivingAttackEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            if (event.getSource() == DamageSource.onFire || event.getSource() == DamageSource.inFire) {
                event.setCanceled(true);
                event.getEntityLiving().extinguish();
                fireAttack = true;
            }else
                fireAttack = false;
        }
    }

    @SubscribeEvent
    public void event(LoginEvent event) {
        if (WorldAPI.equalsWorldName("JumpMap") || WorldAPI.equalsWorldName("JumpMap Sea2")) {
            if (Minecraft.getMinecraft().gameSettings.limitFramerate > 60) {
                Minecraft.getMinecraft().gameSettings.limitFramerate = 60;
            }
        }
    }

    @SubscribeEvent
    public void event(MouseEvent event) {
        if (event.getDwheel() == 120) {
            if (DebAPI.isKeyDown(Keyboard.KEY_LCONTROL)) {
                EntityLavaBlock.ax -= 0.3;
            } else
                EntityLavaBlock.ax -= 0.05;
        }
        if (event.getDwheel() == -120) {
            if (DebAPI.isKeyDown(Keyboard.KEY_LCONTROL)) {
                EntityLavaBlock.ax += 0.3;
            } else
                EntityLavaBlock.ax += 0.05;
        }
    }
}
