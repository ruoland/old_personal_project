package ruo.map.lopre2;

import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.cm.v18.CommandFly;
import ruo.cmplus.util.CommandPlusBase;
import ruo.map.lopre2.jump2.EntityBigInvisibleBlock;
import ruo.map.lopre2.jump2.EntityJumpSpider;
import ruo.minigame.action.ActionEffect;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.TickRegister;
import ruo.minigame.map.EntityDefaultNPC;

public class CommandJB extends CommandPlusBase {
    private int[] pos1, pos2;
    public static long startTime, endTime;
    public static boolean isDebMode = false, isLavaInvisible;//용암 블럭 투명화를 반대로 설정함
    public static boolean upMode, downMode;
    //E 1127 247 -70
    //E 회전 -180 0 -180
    //N 1127 247 -61
    //N 회전 -180 0 -180
    //D 1127 247 -56
    //D 회전 -180 0 -180

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("spider") || args[0].equalsIgnoreCase("spi")) {
                EntityPlayer player = (EntityPlayer) sender;
                EntityJumpSpider spider = new EntityJumpSpider(WorldAPI.getWorld());
                spider.setPosition(player.posX, player.posY, player.posZ);
                WorldAPI.getWorld().spawnEntityInWorld(spider);
            }
            if (args[0].equalsIgnoreCase("loopmove")) {
                Entity entity = Minecraft.getMinecraft().objectMouseOver.entityHit;
                if (entity != null) {
                    EntityDefaultNPC npc = EntityDefaultNPC.getUUIDNPC(entity.getUniqueID());
                    if (npc instanceof EntityMoveBlock) {
                        EntityMoveBlock loopMoveBlock = (EntityMoveBlock) npc;
                        if (args[1].equalsIgnoreCase("add")) {
                            loopMoveBlock.endPos[0] = loopMoveBlock.posX + EntityAPI.lookX(npc, Integer.valueOf(args[2]));
                            loopMoveBlock.endPos[1] = loopMoveBlock.posY;
                            loopMoveBlock.endPos[2] = loopMoveBlock.posZ + EntityAPI.lookZ(npc, Integer.valueOf(args[2]));
                        }
                    }

                }
            }
            if (args[0].equalsIgnoreCase("crawl")) {
                boolean var = t.findBoolean(args, 1, false);
                if (!ActionEffect.canCrawl() && Boolean.valueOf(args[1])) {
                    WorldAPI.addMessage("R키를 누르고 벽으로 다가가면 벽에 매달리거나 올라갈 수 있습니다.");
                }
                ActionEffect.crawl(var);

            }
            if (args[0].equalsIgnoreCase("dj")) {
                if (!ActionEffect.canDoubleJump() && Boolean.valueOf(args[1])) {
                    WorldAPI.addMessage("이제 점프를 공중에서 한번더 할 수 있습니다.(달리면서 점프하면 좀 더 멀리 뛸 수 있음)");
                }
                ActionEffect.doubleJump(Boolean.valueOf(args[1]));
            }

            if (args[0].equalsIgnoreCase("deb"))
                isDebMode = true;


            if (args[0].equalsIgnoreCase("help")) {
                sender.addChatMessage(new TextComponentString("1.블럭이 보이지 않는 경우는 나갔다 들어오기"));
                sender.addChatMessage(new TextComponentString("2.체력과 배고픔 회복은 /heal"));
                sender.addChatMessage(new TextComponentString("3.스폰 포인트가 잘못 설정되어 계속 공중에서 떨어지는 경우 /fly true 를 입력하면 하늘을 날 수 있습니다"));
                sender.addChatMessage(new TextComponentString("4.중간저장을 하려면 /spawnpoint"));
                sender.addChatMessage(new TextComponentString("5.달리기 키 " + Keyboard.getKeyName(Minecraft.getMinecraft().gameSettings.keyBindSprint.getKeyCode()) + "를 누르면 달리기가 쉬워집니다"));

            }
            if (args[0].equalsIgnoreCase("up")) {
                upMode = !upMode;
                if (upMode)
                    sender.addChatMessage(new TextComponentString("스패너를 들고 우클릭하면 빅블럭과 투명 블럭의 위치를 위로 올릴 수 있습니다."));
                else
                    sender.addChatMessage(new TextComponentString("해제됐습니다."));
            }
            if (args[0].equalsIgnoreCase("down")) {
                downMode = !downMode;
                if (downMode)
                    sender.addChatMessage(new TextComponentString("스패너를 들고 우클릭하면 빅블럭과 투명 블럭의 위치를 아래로 내릴 수 있습니다."));
                else
                    sender.addChatMessage(new TextComponentString("해제됐습니다."));
            }
            if (args[0].equalsIgnoreCase("start")) {
                startTime = System.currentTimeMillis();
            }
            if (args[0].equalsIgnoreCase("tpy")) {
                ActionEffect.setYTP(Double.valueOf(args[1]), ActionEffect.getPitch(), ActionEffect.getYaw());
            }
            if (args[0].equalsIgnoreCase("yp")) {
                ActionEffect.setYP(Float.valueOf(args[1]), Float.valueOf(args[2]));
            }
            if (args[0].equalsIgnoreCase("lavainv")) {
                isLavaInvisible = !isLavaInvisible;
                System.out.println(isLavaInvisible);
            }

            if (args[0].equalsIgnoreCase("end")) {
                endTime = System.currentTimeMillis();
                long se = endTime - startTime;
                long sec = se / (1000);
                long minute = sec / 60;
                long second = sec - sec / 60 * 60;
                System.out.println((endTime - startTime) / 1000 + "초.");
                WorldAPI.addMessage(("걸린 시간:" + minute + "분 " + second + "초"));
                WorldAPI.addMessage("플레이 해주셔서 감사합니다!");
                WorldAPI.teleport(1114.6, 240.0, -61.6);
            }

            if (args[0].equalsIgnoreCase("pos1")) {
                pos1 = WorldAPI.changePosArrayInt((EntityLivingBase) sender);
            }
            if (args[0].equalsIgnoreCase("pos2")) {
                pos2 = WorldAPI.changePosArrayInt((EntityLivingBase) sender);
            }
            if (args[0].equalsIgnoreCase("set"))
                Loop2Event.curve(sender.getEntityWorld(), pos1[0], pos1[1], pos1[2], pos2[0], pos2[1], pos2[2]);

            if (args[0].equalsIgnoreCase("block"))
                Loop2Event.blockSet(sender.getEntityWorld(), pos1[0], pos1[1], pos1[2], pos2[0], pos2[1], pos2[2]);
            if (args[0].equalsIgnoreCase("blockmove"))
                Loop2Event.blockSetMove(sender.getEntityWorld(), pos1[0], pos1[1], pos1[2], pos2[0], pos2[1], pos2[2]);

            if (args[0].equalsIgnoreCase("water")) {
                ActionEffect.inWater(Boolean.valueOf(args[1]));
            }
            if (args[0].equalsIgnoreCase("removewater")) {
                ActionEffect.inWaterRemove();
            }
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
                EntityMoveBlock.moveStop = Boolean.valueOf(args[1]);
            }
            if (args[0].equalsIgnoreCase("nightvision")) {
                LooPre2Event.nightVision = t.parseBoolean(args[1]);
            }
            if (args[0].equalsIgnoreCase("posy")) {
                LooPre2Event.posYDead = t.parseBoolean(args[1]);
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
