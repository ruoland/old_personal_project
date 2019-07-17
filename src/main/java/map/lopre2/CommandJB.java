package map.lopre2;

import cmplus.CMPlus;
import cmplus.test.CMPacketCommand;
import cmplus.util.CommandPlusBase;
import minigameLib.MiniGame;
import olib.action.ActionEffect;
import olib.action.DoubleJump;
import olib.api.WorldAPI;
import olib.effect.AbstractTick;
import olib.effect.TickRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;
import map.lopre2.jump1.EntityMoveBlock;
import map.lopre2.jump1.EntityWaterBlockCreator;
import map.lopre2.jump2.EntityBigInvisibleBlock;

public class CommandJB extends CommandPlusBase {
    private int[] pos1, pos2;
    public static int x,y=80,width,height;
    public static long startTime, endTime;
    public static boolean isDebMode = false, isLavaInvisible;//용암 블럭 투명화를 반대로 설정함
    //E 1127 247 -70
    //E 회전 -180 0 -180
    //N 1127 247 -61
    //N 회전 -180 0 -180
    //D 1127 247 -56
    //D 회전 -180 0 -180

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("tutorial")) {
                EntityPlayer player = getPlayer(server,sender, args[1]);
                player.addChatComponentMessage(new TextComponentString("1.블럭이 안보이면 맵을 나갔다가 들어와 주세요."));
                player.addChatComponentMessage(new TextComponentString("2.블럭에 끼었을 때 /jb up 명령어를 입력해주세요"));
                player.addChatComponentMessage(new TextComponentString("3.heal 명령어를 입력하면 체력과 배고픔이 회복됩니다"));
                player.addChatComponentMessage(new TextComponentString("4.R키를 누르면 스폰포인트로 이동합니다"));
                player.addChatComponentMessage(new TextComponentString("5./jb help 명령어를 입력하면 위에 내용을 다시 볼 수 있습니다.."));
            }
            if (args[0].equalsIgnoreCase("crawl")) {
                boolean var = t.findBoolean(args, 1, false);
                if (!ActionEffect.canCrawl() && Boolean.valueOf(args[1])) {
                    WorldAPI.addMessage("R키를 누르고 벽으로 다가가면 벽에 매달리거나 올라갈 수 있습니다.");
                }
                ActionEffect.crawl(var);
            }
            if (args[0].equalsIgnoreCase("bu")) {
                for (EntityPreBlock preBlock : ItemCopy.getUndoBlockList()) {
                    preBlock.setDead();
                }
            }
            if (args[0].equalsIgnoreCase("dj")) {
                if (!DoubleJump.canMapDoubleJump() && Boolean.valueOf(args[1])) {
                    WorldAPI.addMessage("이제 점프를 공중에서 한번더 할 수 있습니다.(달리면서 점프하면 좀 더 멀리 뛸 수 있음)");
                }
                DoubleJump.doubleJump(Boolean.valueOf(args[1]));
                CMPlus.INSTANCE.sendToAll(new CMPacketCommand("서버더블점프"+args[1]));

            }

            if (args[0].equalsIgnoreCase("deb"))
                isDebMode = true;

            if (args[0].equalsIgnoreCase("help")) {
                if(args.length > 1){
                    if(args[1].equalsIgnoreCase("2")){
                        sender.addChatMessage(new TextComponentString(""));
                    }
                }else {
                    sender.addChatMessage(new TextComponentString("블럭을 들고 K키를 누르면 블럭을 설치합니다."));
                    sender.addChatMessage(new TextComponentString("블럭을 들고 J키를 누르면 블럭을 복사후 붙여넣기 합니다."));
                    sender.addChatMessage(new TextComponentString("블럭을 들고 마우스 휠을 돌리면 거리를 조절할 수 있습니다."));
                    sender.addChatMessage(new TextComponentString("블럭을 들고 화살표 키를 누르면 블럭을 회전시킬 수 있습니다."));
                    sender.addChatMessage(new TextComponentString("블럭을 쉬프트 키를 누르고 마우스 왼클릭을 하면 블럭을 없앨 수 있습니다.."));
                    sender.addChatMessage(new TextComponentString("블럭을 쉬프트 키를 누르고 마우스 우클릭을 하면 블럭을 들 수 있습니다.."));
                }
            }
            if (args[0].equalsIgnoreCase("start")) {
                startTime = System.currentTimeMillis();
                sender.addChatMessage(new TextComponentString("팁1 R키를 누르면 스폰포인트로 바로 이동합니다"));
                sender.addChatMessage(new TextComponentString("팁2 죽었을 때 스페이스바를 누르면 바로 부활할 수 있습니다"));
            }
            if (args[0].equalsIgnoreCase("tpy")) {
                ActionEffect.setYTP(Double.valueOf(args[1]), ActionEffect.getPitch(), ActionEffect.getYaw());
                sender.addChatMessage(new TextComponentString("스폰포인트로 TPY 자동 갱신되게 했음 /spawnpoint 로 내려갈 것"));

            }
            if (args[0].equalsIgnoreCase("yp")) {
                ActionEffect.setYP(Float.valueOf(args[1]), Float.valueOf(args[2]));
            }
            if (args[0].equalsIgnoreCase("lavainv")) {
                isLavaInvisible = !isLavaInvisible;
                System.out.println(isLavaInvisible);
            }

            if (args[0].equalsIgnoreCase("pos1")) {
                pos1 = WorldAPI.changePosArrayInt((EntityLivingBase) sender);
            }
            if (args[0].equalsIgnoreCase("pos2")) {
                pos2 = WorldAPI.changePosArrayInt((EntityLivingBase) sender);
            }
            if (args[0].equalsIgnoreCase("inv"))
                WorldAPI.setBlock(sender.getEntityWorld(), pos1[0], pos1[1], pos1[2], pos2[0], pos2[1], pos2[2], MiniGame.blockInvisible);
            if (args[0].equalsIgnoreCase("block")) {
                System.out.println(Loop.blockSet(sender.getEntityWorld(), pos1[0], pos1[1], pos1[2], pos2[0], pos2[1], pos2[2]));

            }
            if (args[0].equalsIgnoreCase("save"))
                Loop.save(sender.getEntityWorld(), args[1], pos1[0], pos1[1], pos1[2], pos2[0], pos2[1], pos2[2]);
            if (args[0].equalsIgnoreCase("blockspawn"))
                Loop.read(sender.getEntityWorld(), args[1], parseDouble(args[2]), parseDouble(args[3]), parseDouble(args[4]));

            if (args[0].equalsIgnoreCase("downlock")) {
                EntityWaterBlockCreator.downLock = !EntityWaterBlockCreator.downLock;
                System.out.println("DOWNLOCK " + EntityWaterBlockCreator.downLock);
            }
            if (args[0].equalsIgnoreCase("downReset")) {
                EntityWaterBlockCreator.downReset = true;
                TickRegister.register(new AbstractTick(20, false) {

                    @Override
                    public void run(Type type) {
                        EntityWaterBlockCreator.downReset = false;
                    }
                });
                System.out.println("DOWNRESET " + EntityWaterBlockCreator.downReset);
            }
            if (args[0].equalsIgnoreCase("moveStop")) {
                EntityMoveBlock.allBlockMoveStop = Boolean.valueOf(args[1]);
            }

            if (args[0].equalsIgnoreCase("invlock")) {
                EntityBigInvisibleBlock.isInvisibleLock = !EntityBigInvisibleBlock.isInvisibleLock;
            }
            if (args[0].equalsIgnoreCase("downlong")) {
                EntityWaterBlockCreator.downLock = !EntityWaterBlockCreator.downLock;
            }
            if (args[0].equalsIgnoreCase("up")) {
                Vec3d vec = sender.getPositionVector();
                WorldAPI.teleport(vec.xCoord, vec.yCoord + 1, vec.zCoord);
            }
        }
    }
}
