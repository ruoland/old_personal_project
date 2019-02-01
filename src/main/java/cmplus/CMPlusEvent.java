package cmplus;

import cmplus.cm.v18.function.FunctionIF;
import cmplus.cm.v18.function.VAR;
import cmplus.deb.DebAPI;
import cmplus.util.CommandPlusBase;
import minigameLib.api.WorldAPI;
import net.minecraft.command.CommandException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.GameRules;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class CMPlusEvent {
    private static final String[] uiList = "ALL,HELMET,PORTAL,CROSSHAIRS,BOSSHEALTH,ARMOR,HEALTH,FOOD,AIR,HOTBAR,EXPERIENCE,HEALTHMOUNT,JUMPBAR,CHAT,PLAYER_LIST,DEBUG"
            .split(",");


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
                DebAPI.msgVar("커멘드 이벤트-@가 들어간 인자를 발견함-발견한 변수이름-" + key);// 발견시 변수이름 표시함
                if (VAR.hasBoolean(key)) {
                    args[i] = args[i].replace(args[i], "" + VAR.getBoolean(key));
                } else if (VAR.hasInteger(key)) {
                    args[i] = args[i].replace(args[i], "" + VAR.getInt(key));
                } else if (VAR.hasDouble(key) || (VAR.ifMath(key) && VAR.findDoubleKey(key) != null)) {
                    DebAPI.msgVar(args[i] + " - " + key + " - " + "@" + VAR.findDoubleKey(key));
                    args[i] = args[i].replace("@" + VAR.findDoubleKey(key), "" + VAR.getDouble(VAR.findDoubleKey(key)));
                } else if (VAR.hasString(key)) {
                    args[i] = args[i].replace(args[i], "" + VAR.getStr(key));
                }
            }
            try {
                if (args[i].startsWith("@") || args[i].contains("@플레이어")) {
                    Entity entity = args[i].contains("@플레이어") ? WorldAPI.getPlayer()
                            : CommandPlusBase.getPlusEntity(e.getSender().getServer(), e.getSender(),
                            key.split("[.]")[0].replace("@", ""));
                    args[i] = args[i].replace("@플레이어", "@" + WorldAPI.getPlayer().getName());
                    if (entity != null) {
                        args[i] = args[i].replace("@" + entity.getName(), "@" + entity.getName());
                        DebAPI.msgVar("커멘드 이벤트-몬스터 인자를 발견함 - 발견한 몬스터" + entity.getName());
                        args[i] = replaceEntity((EntityLivingBase) entity, args[i]);
                        DebAPI.msgVar("커맨드 이벤트-" + args[i] + "로 교체함");
                        key = args[i].replace("@", "");
                        System.out.println("엔티티를 찾음" + entity);
                    }
                }
                if (VAR.ifMath(args[i])) {
                    DebAPI.msgVar("계산식을 발견함!" + args[i]);
                    args[i] = args[i].replace(args[i], "" + VAR.getDouble(args[i]));
                    key = key.replace(args[i], "" + VAR.getDouble(args[i]));
                    DebAPI.msgVar("계산식을 발견함!222" + args[i] + " - " + VAR.getDouble(key));
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
        if (event.player.worldObj.getGameRules().getBoolean("noHunger")) {
            event.player.getFoodStats().setFoodLevel(20);
        }
        if (!event.player.worldObj.getGameRules().getBoolean("weatherChange")) {
            event.player.worldObj.setRainStrength(0);
        }
    }

    @SubscribeEvent
    public void worldLoad(WorldEvent.Load e) {
        GameRules rules = e.getWorld().getGameRules();
        if (!rules.hasRule("noHunger"))
            rules.addGameRule("noHunger", "false", GameRules.ValueType.BOOLEAN_VALUE);
        if (!rules.hasRule("weatherChange"))
            rules.addGameRule("weatherChange", "true", GameRules.ValueType.BOOLEAN_VALUE);

    }

}
