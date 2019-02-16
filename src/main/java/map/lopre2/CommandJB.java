package map.lopre2;

import cmplus.CMPlus;
import cmplus.test.CMPacketCommand;
import cmplus.util.CommandPlusBase;
import map.lopre2.jump1.EntityBuildBlock;
import map.lopre2.jump2.EntityTeleportBlock;
import minigameLib.MiniGame;
import minigameLib.action.ActionEffect;
import minigameLib.api.WorldAPI;
import minigameLib.effect.AbstractTick;
import minigameLib.effect.TickRegister;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.multiplayer.WorldClient;
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
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldSummary;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;
import map.lopre2.jump1.EntityMoveBlock;
import map.lopre2.jump1.EntityWaterBlockCreator;
import map.lopre2.jump2.EntityBigInvisibleBlock;
import map.lopre2.jump2.EntityJumpSpider;

public class CommandJB extends CommandPlusBase {
    private int[] pos1, pos2;
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
            if (args[0].equalsIgnoreCase("spider") || args[0].equalsIgnoreCase("spi")) {
                EntityPlayer player = (EntityPlayer) sender;
                EntityJumpSpider spider = new EntityJumpSpider(WorldAPI.getWorld());
                spider.setPosition(player.posX, player.posY, player.posZ);
                WorldAPI.getWorld().spawnEntityInWorld(spider);
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
                CMPlus.INSTANCE.sendToAll(new CMPacketCommand("서버더블점프"+args[1]));

            }

            if (args[0].equalsIgnoreCase("deb"))
                isDebMode = true;

            if (args[0].equalsIgnoreCase("help")) {
                sender.addChatMessage(new TextComponentString("1.블럭이 보이지 않는 경우는 나갔다 들어오기"));
                sender.addChatMessage(new TextComponentString("2.체력과 배고픔 회복은 /heal"));
                sender.addChatMessage(new TextComponentString("3./fly true 를 입력하면 하늘을 날 수 있습니다"));
                sender.addChatMessage(new TextComponentString("4.너무 어려우면 /spawnpoint (사용시 도전과제 클리어 못함)"));
                sender.addChatMessage(new TextComponentString("5.달리기 키 " + Keyboard.getKeyName(Minecraft.getMinecraft().gameSettings.keyBindSprint.getKeyCode()) + "를 누르면 달리기가 쉬워집니다"));
            }
            if (args[0].equalsIgnoreCase("start")) {
                startTime = System.currentTimeMillis();
                sender.addChatMessage(new TextComponentString("팁1 R키를 누르면 스폰포인트로 바로 이동합니다"));
                sender.addChatMessage(new TextComponentString("팁2 죽었을 때 스페이스바를 누르면 바로 부활할 수 있습니다"));
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
            if (args[0].equalsIgnoreCase("hidepath1")) {
                WorldAPI.getPlayer().addStat(LoPre2.achievementHidePath1);
            }
            if (args[0].equalsIgnoreCase("hidepath2")) {
                WorldAPI.getPlayer().addStat(LoPre2.achievementHidePath2);
            }
            if (args[0].equalsIgnoreCase("end")) {
                endTime = System.currentTimeMillis();
                long se = endTime - startTime;
                long sec = se / (1000);
                long minute = sec / 60;
                long second = sec - sec / 60 * 60;
                System.out.println((endTime - startTime) / 1000 + "초.");

                if (FMLCommonHandler.instance().getSide() == Side.CLIENT && WorldAPI.equalsWorldName("JumpMap")) {
                    int appleCount = 0;
                    for (EntityPlayer player : server.getPlayerList().getPlayerList()) {
                        appleCount += WorldAPI.findInventoryItemCount(player, Items.APPLE);
                        player.addStat(LoPre2.achievementOneClear);
                        if (appleCount > 1) {
                            player.addStat(LoPre2.achievementApple);
                        }
                        if (LooPre2Event.deathCount == 0) {
                            player.addStat(LoPre2.achievementNoDie1);
                        }
                        if (LooPre2Event.gamemodeCount == 0) {
                            player.addStat(LoPre2.achievementNoGameMode1);
                        }
                    }

                    for(EntityPlayerMP playerMP : server.getPlayerList().getPlayerList()){
                        playerMP.addChatMessage(new TextComponentString("                           "));
                        playerMP.addChatMessage(new TextComponentString("                           "));
                        playerMP.addChatMessage(new TextComponentString("                           "));
                        playerMP.addChatMessage(new TextComponentString("걸린 시간:" + minute + "분 " + second + "초"));
                        playerMP.addChatMessage(new TextComponentString("모쿠르 1탄을 플레이 해주셔서 감사합니다!"));
                    }
                    WorldAPI.addMessage("점프맵 1탄을 클리어 하셨습니다. 2탄으로 바로 넘어갈까요?");
                    TextComponentString textComponent = new TextComponentString("[2탄으로 넘어가려면 이 메세지를 누르세요.]");
                    Style style = new Style();
                    style.setColor(TextFormatting.BOLD);
                    textComponent.setStyle(style.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/jb jump2")));
                    //sender.addChatMessage(textComponent);
                    endTime = 0;
                    startTime = 0;
                    WorldAPI.getPlayer().addChatComponentMessage(textComponent);
                } else if (WorldAPI.equalsWorldName("JumpMap Sea2")) {
                    WorldAPI.getPlayer().addStat(LoPre2.achievementTwoClear);
                    if (LooPre2Event.deathCount == 0) {
                        WorldAPI.getPlayer().addStat(LoPre2.achievementNoDie2);
                    }
                    if (LooPre2Event.gamemodeCount == 0) {
                        WorldAPI.getPlayer().addStat(LoPre2.achievementNoGameMode2);
                    }
                    for(EntityPlayerMP playerMP : server.getPlayerList().getPlayerList()){
                        playerMP.addChatMessage(new TextComponentString("                           "));
                        playerMP.addChatMessage(new TextComponentString("걸린 시간:" + minute + "분 " + second + "초"));
                        playerMP.addChatMessage(new TextComponentString("모쿠르 2탄도 클리어 하셨습니다. 플레이 해주셔서 감사합니다!"));
                    }
                }
            }
            if (args[0].equalsIgnoreCase("jump2")) {
                if (WorldAPI.equalsWorldName("JumpMap")) {
                    WorldAPI.addMessage("난이도를 선택해주세요.(설정에서 난이도를 언제든지 바꿀 수 있습니다.)");
                    TextComponentString textComponent = new TextComponentString("   ");
                    TextComponentString normal = new TextComponentString("[쉬운 난이도]");
                    normal.setStyle(new Style().setColor(TextFormatting.RED).setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/jb easy")));
                    TextComponentString hard = new TextComponentString("[보통 난이도]");
                    hard.setStyle(new Style().setColor(TextFormatting.GREEN).setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/jb normal")));
                    textComponent = (TextComponentString) normal.appendSibling(textComponent).appendSibling(hard);
                    sender.addChatMessage(textComponent);
                }
            }
            if (args[0].equalsIgnoreCase("easy") || args[0].equalsIgnoreCase("normal")) {
                worldLoad(args[0]);
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

    public void worldLoad(String diffu) {
        Minecraft mc = Minecraft.getMinecraft();
        TickRegister.register(new AbstractTick(Type.CLIENT, 1, false) {
            @Override
            public void run(Type type) {
                mc.theWorld.sendQuittingDisconnectingPacket();
                mc.loadWorld((WorldClient) null);
                ISaveFormat isaveformat = mc.getSaveLoader();
                String worldName = "JumpMap Sea2";
                if (diffu.equalsIgnoreCase("easy"))
                    mc.gameSettings.difficulty = EnumDifficulty.EASY;
                else if (diffu.equalsIgnoreCase("normal"))
                    mc.gameSettings.difficulty = EnumDifficulty.NORMAL;

                if (isaveformat.canLoadWorld(worldName)) {
                    try {
                        for (WorldSummary summary : isaveformat.getSaveList()) {
                            if (summary.getDisplayName().equalsIgnoreCase(worldName)) {
                                net.minecraftforge.fml.client.FMLClientHandler.instance().tryLoadExistingWorld(new GuiWorldSelection(new GuiMainMenu()), summary);
                                LooPre2Event.spawnCount = 0;
                                LooPre2Event.healCount = 0;
                                LooPre2Event.gamemodeCount = 0;
                                LooPre2Event.deathCount = 0;
                                break;
                            }
                        }
                    } catch (AnvilConverterException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
