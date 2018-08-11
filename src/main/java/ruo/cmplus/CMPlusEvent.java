package ruo.cmplus;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.command.CommandException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.DimensionType;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.camera.Camera;
import ruo.cmplus.cm.v17.Deb;
import ruo.cmplus.cm.v18.function.FunctionIF;
import ruo.cmplus.cm.v18.function.VAR;
import ruo.cmplus.deb.DebAPI;
import ruo.cmplus.util.*;
import ruo.minigame.api.LoginEvent;
import ruo.minigame.api.WorldAPI;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;

public class CMPlusEvent {
    private static final String[] uiList = "ALL,HELMET,PORTAL,CROSSHAIRS,BOSSHEALTH,ARMOR,HEALTH,FOOD,AIR,HOTBAR,EXPERIENCE,HEALTHMOUNT,JUMPBAR,CHAT,PLAYER_LIST,DEBUG"
            .split(",");
    private Camera cm = Camera.getCamera();
    private Minecraft mc = Minecraft.getMinecraft();
    private double prevDistance;

    @SubscribeEvent
    public void event(RenderFogEvent e) {
        if (Sky.isFogOn()) {
            if (Sky.getFogDistance() == -1 || prevDistance != mc.gameSettings.renderDistanceChunks) {
                prevDistance = mc.gameSettings.renderDistanceChunks;
                Sky.fogDistance(5.0F + ((mc.gameSettings.renderDistanceChunks * 16) - 5.0F) * (1.0F - (float) 0 / 20.0F));
                Sky.fogOnOff(false);
            }
            GlStateManager.enableFog();
            GlStateManager.setFogStart(Sky.getFogDistance());
            GlStateManager.setFogEnd(Sky.getFogDistance() + 1);
        }
    }

    @SubscribeEvent
    public void event(DrawBlockHighlightEvent e) {
        if (!Sky.isLayer()) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void event(CommandEvent e) {
        String[] args = e.getParameters();
        if ((e.getCommand().getCommandName().equals("while") && e.getParameters()[0].equals("off"))
                || (e.getCommand().getCommandName().equals("if") && e.getParameters()[0].equals("off"))) {
            System.out.println("while이나 if 명령어를 종료하는 명령어이니 조건을 체크하지 않고 명령어 실행하게 함");
            return;
        }

        for (int i = 0; i < e.getParameters().length; i++) {// 전체 인자를 반복하는 폴문
            String key = args[i].replace("@", "");// 인자에서 @를 뺌(이 변수가 있는 이유는 매번 @빼기엔 코드가 더러워져서)
            if (args[i].startsWith("@") && (VAR.hasKey(key) || VAR.ifMath(key))) {
                // @로 시작하는 인자 발견시 그 이름을 가진 변수가 있는지 아니면 그 인자에 계산식이 있는지
                Deb.msgVar("커멘드 이벤트-@가 들어간 인자를 발견함-발견한 변수이름-" + key);// 발견시 변수이름 표시함
                if (VAR.hasBoolean(key)) {
                    args[i] = args[i].replace(args[i], "" + VAR.getBoolean(key));
                } else if (VAR.hasInteger(key)) {
                    args[i] = args[i].replace(args[i], "" + VAR.getInt(key));
                } else if (VAR.hasDouble(key) || (VAR.ifMath(key) && VAR.findDoubleKey(key) != null)) {
                    Deb.msgVar(args[i] + " - " + key + " - " + "@" + VAR.findDoubleKey(key));
                    args[i] = args[i].replace("@" + VAR.findDoubleKey(key), "" + VAR.getDouble(VAR.findDoubleKey(key)));
                } else if (VAR.hasString(key)) {
                    args[i] = args[i].replace(args[i], "" + VAR.getStr(key));
                }
            }
            try {
                if (args[i].startsWith("@") || args[i].indexOf("@플레이어") != -1) {
                    System.out.println("1" + args[i]);
                    Entity entity = args[i].indexOf("@플레이어") != -1 ? WorldAPI.getPlayer()
                            : CommandPlusBase.getPlusEntity(e.getSender().getServer(), e.getSender(),
                            key.split("[.]")[0].replace("@", ""));
                    args[i] = args[i].replace("@플레이어", "@" + WorldAPI.getPlayer().getName());
                    System.out.println("2" + args[i]);
                    if (entity != null) {
                        args[i] = args[i].replace("@" + entity.getName(), "@" + entity.getName());
                        Deb.msgVar("커멘드 이벤트-몬스터 인자를 발견함 - 발견한 몬스터" + entity.getName());
                        args[i] = replaceEntity((EntityLivingBase) entity, args[i]);
                        Deb.msgVar("커맨드 이벤트-" + args[i] + "로 교체함");
                        key = args[i].replace("@", "");
                        System.out.println("엔티티를 찾음" + entity);
                    }
                }
                if (VAR.ifMath(args[i])) {
                    Deb.msgVar("계산식을 발견함!" + args[i]);
                    args[i] = args[i].replace(args[i], "" + VAR.getDouble(args[i]));
                    key = key.replace(args[i], "" + VAR.getDouble(args[i]));
                    Deb.msgVar("계산식을 발견함!222" + args[i] + " - " + VAR.getDouble(key));
                }
            } catch (CommandException e1) {
                e1.printStackTrace();
            }
        }

        e.setParameters(args);
        if (FunctionIF.isIF()
                && !(e.getCommand().getCommandName().equals("if") && e.getParameters()[0].equals("off"))) {
            if (FunctionIF.currentIF().check()) {
                e.setCanceled(false);
                return;
            }

            e.setCanceled(true);
        }
    }

    public String replaceEntity(EntityLivingBase entity, String arg) {
        String entityname = "@" + entity.getName() + ".";
        arg = arg.replace(entityname + "X", "" + entity.posX);
        arg = arg.replace(entityname + "Y", "" + entity.posY);
        arg = arg.replace(entityname + "Z", "" + entity.posZ);
        arg = arg.replace(entityname + "체력", "" + entity.getHealth());
        arg = arg.replace(entityname + "이름", "" + entity.getCustomNameTag());
        arg = arg.replace(entityname + "피치", "" + entity.rotationPitch);
        arg = arg.replace(entityname + "요", "" + entity.rotationYaw);
        arg = arg.replace(entityname + "최대체력", "" + entity.getMaxHealth());
        arg = arg.replace(entityname + "방어력", "" + entity.getTotalArmorValue());
        arg = arg.replace(entityname + "아이템",
                "" + entity.getHeldItemMainhand() == null ? "없음" : entity.getHeldItemMainhand().getDisplayName());
        arg = arg.replace(entityname + "LX", "" + entity.getLookVec().xCoord);
        arg = arg.replace(entityname + "LY", "" + entity.getLookVec().yCoord);
        arg = arg.replace(entityname + "LZ", "" + entity.getLookVec().zCoord);
        arg = arg.replace(entityname + "죽음여부", "" + entity.isDead);
        arg = arg.replace(entityname + "바닥여부", "" + entity.onGround);
        arg = arg.replace(entityname + "불여부", "" + entity.isBurning());
        arg = arg.replace(entityname + "아이여부", "" + entity.isChild());
        arg = arg.replace(entityname + "아이여부", "" + entity.isEntityAlive());
        arg = arg.replace(entityname + "용암여부", "" + entity.isInLava());
        arg = arg.replace(entityname + "투명화여부", "" + entity.isInvisible());
        arg = arg.replace(entityname + "물여부", "" + entity.isInWater());
        arg = arg.replace(entityname + "라이딩여부", "" + entity.isRiding());
        arg = arg.replace(entityname + "웅크림여부", "" + entity.isSneaking());
        arg = arg.replace(entityname + "달리기여부", "" + entity.isSprinting());
        arg = arg.replace(entityname + "물여부", "" + entity.isInWater());
        arg = arg.replace(entityname + "보는곳", "" + entity.getHorizontalFacing().getName());
        return arg;
    }

    @SubscribeEvent
    public void login(PlayerTickEvent event) {
        if (event.player.worldObj.getGameRules().getBoolean("food")) {
            event.player.getFoodStats().setFoodLevel(20);
        }
        if (!event.player.worldObj.getGameRules().getBoolean("weatherChange")) {
            event.player.worldObj.setRainStrength(0);
        }
    }

    @SubscribeEvent
    public void login(RenderHandEvent event)
    {
        event.setCanceled(!CMManager.isUI("HAND"));
    }


    @SubscribeEvent
    public void login(LoginEvent e) {
        if (!WorldAPI.getCurrentWorldName().equalsIgnoreCase("MpServer") && !WorldAPI.getCurrentWorldName().equalsIgnoreCase("noworld"))
            if(new File("/saves/" + WorldAPI.getCurrentWorldName() + "/Waypoint").isFile()) {
                CMManager.waypoint = (HashMap<String, double[]>) DebAPI
                        .readObject("/saves/" + WorldAPI.getCurrentWorldName() + "/Waypoint", new HashMap<String, double[]>());
            }
    }

    @SubscribeEvent
    public void worldLoad(WorldEvent.Load e) {
        GameRules rules = e.getWorld().getGameRules();
        if (!rules.hasRule("food"))
            rules.addGameRule("food", "false", GameRules.ValueType.BOOLEAN_VALUE);
        if (!rules.hasRule("weatherChange"))
            rules.addGameRule("weatherChange", "true", GameRules.ValueType.BOOLEAN_VALUE);

    }

    @SubscribeEvent
    public void worldUnload(WorldEvent.Unload e) {
        if (!WorldAPI.getCurrentWorldName().equalsIgnoreCase("MpServer") && !WorldAPI.getCurrentWorldName().equalsIgnoreCase("noworld"))
            DebAPI.saveObject("/saves/" + WorldAPI.getCurrentWorldName() + "/Waypoint", CMManager.waypoint);
    }

    @SubscribeEvent
    public void mouse(MouseEvent event) {
        if (Minecraft.getMinecraft().currentScreen == null) {
            event.setCanceled(CMManager.isMouse());
        }
    }

    @SubscribeEvent
    public void event(WorldEvent.Load e) {
        if (e.getWorld().provider.getDimensionType() == DimensionType.OVERWORLD) {
            try {
                WorldProvider10 pro = new WorldProvider10();
                pro.registerWorld(e.getWorld());
                for (Field f : e.getWorld().provider.getClass().getDeclaredFields()) {
                    Field prof;
                    try {
                        prof = pro.getClass().getDeclaredField(f.getName());
                    } catch (Exception e2) {
                        continue;
                    }
                    prof.setAccessible(true);
                    f.setAccessible(true);
                    //prof.set(pro, f.get(e.getWorld().provider));
                }
                pro.setSpawnPoint(e.getWorld().provider.getSpawnPoint());

                for (Field f : World.class.getDeclaredFields()) {
                    if (f.getName().equals("provider")) {
                        f.setAccessible(true);
                        f.set(e.getWorld(), pro);
                        e.getWorld().provider.setCloudRenderer(new CloudRenderer());
                        e.getWorld().provider.setSkyRenderer(new SkyRenderer());
                        Sky.pro = (WorldProvider10) e.getWorld().provider;

                        break;
                    }
                }

            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public void bgmStop(PlaySoundEvent e) {
        if (e.getName().indexOf("music.") != -1) {
            e.getManager().stopSound(e.getSound());
            e.getManager().stopSound(e.getResultSound());
            Minecraft.getMinecraft().getSoundHandler().stopSound(e.getSound());
            Minecraft.getMinecraft().getSoundHandler().stopSound(e.getResultSound());
        }
    }


    @SubscribeEvent
    public void playerTick(TickEvent.WorldTickEvent e) {
        if (CMManager.norain)
            e.world.setRainStrength(0);
    }

    @SubscribeEvent
    public void playerTick(PlayerTickEvent e) {
        if (Keyboard.isKeyDown(Keyboard.KEY_INSERT) && WorldAPI.getLookBlock() != null) {
            if (WorldAPI.getLookBlock() == null && WorldAPI.getBlock(WorldAPI.getLookBlock()) == null) {
                return;
            }
            System.out.println(WorldAPI.getLookBlock().getX() + "," + WorldAPI.getLookBlock().getY() + ","
                    + WorldAPI.getLookBlock().getZ());
            System.out.println(WorldAPI.getBlock(WorldAPI.getLookBlock()).getLocalizedName());
        }
    }

    @SubscribeEvent
    public void renderUI(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == ElementType.ALL)
            for (String s : uiList) {
                if (event.getType() == ElementType.valueOf(s)) {
                    if (!CMManager.isUI(s)) {
                        event.setCanceled(true);
                    }
                }
            }
    }

    @SubscribeEvent
    public void event(PlayerRespawnEvent event) {
        if (CMManager.getGameOver() != null) {
            Minecraft.getMinecraft().currentScreen = null;
        }
    }

    @SubscribeEvent
    public void event(GuiOpenEvent event) {
        if (CMManager.getGameOver() != null && event.getGui() instanceof GuiGameOver) {
            event.setGui(CMManager.getGameOver());
            CMManager.setGameOverNull();
        }
    }

}
