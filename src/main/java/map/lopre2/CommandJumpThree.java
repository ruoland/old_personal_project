package map.lopre2;

import cmplus.util.CommandPlusBase;
import map.Loop;
import map.lopre2.jump1.EntityBuildBlock;
import map.lopre2.jump3.JumpTool;
import minigameLib.minigame.scroll.EntityJumpCreeper;
import minigameLib.minigame.scroll.EntityJumpFlyingCreeper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import olib.api.WorldAPI;
import olib.effect.AbstractTick;
import olib.effect.Move;
import olib.effect.TickRegister;
import olib.map.EntityDefaultNPC;

public class CommandJumpThree extends CommandPlusBase {

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args[0].equalsIgnoreCase("cave")) {
            Loop.read(sender.getEntityWorld(), "ball", 156.4, 66.960, -423.06).setRotate(WorldAPI.rand(180), WorldAPI.rand(180), WorldAPI.rand(180));
            Loop.read(sender.getEntityWorld(), "ball", 153, 62, -422).setRotate(WorldAPI.rand(180), WorldAPI.rand(180), WorldAPI.rand(180));
            Loop.read(sender.getEntityWorld(), "ball", 149.2, 62.5, -422.7).setRotate(WorldAPI.rand(180), WorldAPI.rand(180), WorldAPI.rand(180));
            Loop.read(sender.getEntityWorld(), "ball", 155.5, 67.6, -422.3).setRotate(WorldAPI.rand(180), WorldAPI.rand(180), WorldAPI.rand(180));
            Loop.read(sender.getEntityWorld(), "ball", 157.4, 63.1, -423.3).setRotate(WorldAPI.rand(180), WorldAPI.rand(180), WorldAPI.rand(180));
            WorldAPI.setBlock(sender.getEntityWorld(), 145, 59, -422, 156, 68, -422, Blocks.BARRIER);
        }

        if (args[0].equalsIgnoreCase("rotatespawn")) {
            JumpTool.rotateBlockSpawn((EntityPlayer) sender, args[1]);
        }
        if (args[0].equalsIgnoreCase("creeper1")) {
            creeper(157.2, 63, -379.5);
        }
        if (args[0].equalsIgnoreCase("creeper2")) {
            creeper(157.2, 62.0, -367.6);
        }
        if (args[0].equalsIgnoreCase("creeper3")) {
            creeper(157.1, 63.0, -356.9);
        }
        if (args[0].equalsIgnoreCase("lavacreeper")) {
            lavacreeper(156, 63.7, -343.0);
        }
        if (args[0].equalsIgnoreCase("rotate")) {
            rotate(sender, args);
        }
    }


    public void lavacreeper(double x, double y, double z) {
        TickRegister.register(new AbstractTick(60, true) {
            @Override
            public void run(TickEvent.Type type) {
                EntityJumpFlyingCreeper creeper = new EntityJumpFlyingCreeper(WorldAPI.getWorld());
                creeper.setPosition(x, y, z);
                WorldAPI.getWorld().spawnEntityInWorld(creeper);
                creeper.setTarget(x - 20, y, z, 1);
                if (absRunCount == 15)
                    absLoop = false;
            }
        });


    }

    public void creeper(double x, double y, double z) {
        TickRegister.register(new AbstractTick(60, true) {
            @Override
            public void run(TickEvent.Type type) {
                EntityJumpCreeper creeper = new EntityJumpCreeper(WorldAPI.getWorld());
                creeper.setPosition(x, y, z);
                WorldAPI.getWorld().spawnEntityInWorld(creeper);

                creeper.move(new Move(creeper, x - 16, y, z, false) {
                                 @Override
                                 public void complete() {
                                     creeper.setDead();
                                 }
                             }
                );
                if (absRunCount == 15)
                    absLoop = false;
            }
        });

    }

    public void rotate(ICommandSender sender, String[] args) {
        EntityBuildBlock buildBlock = (EntityBuildBlock) EntityDefaultNPC.getNPC("rotate");
        for (Entity entity : sender.getEntityWorld().loadedEntityList) {
            if (!(entity instanceof EntityPlayer)) {
                boolean isJumpBlock = entity instanceof EntityPreBlock && ((EntityPreBlock) entity).getJumpName().equalsIgnoreCase(args[1]);

                if (entity.getCustomNameTag().equalsIgnoreCase(args[1]) || isJumpBlock) {
                    buildBlock = (EntityBuildBlock) entity;
                    String buildName = buildBlock.getJumpName();
                    EntityDefaultNPC entitydefServer = EntityDefaultNPC.getUUIDNPC(buildBlock.getUniqueID());
                    EnumFacing facing = null;
                    System.out.println("111111111111 찾음  " + buildName + buildBlock.getRotateX() + " - " + buildBlock.getRotateY() + " -" + buildBlock.getRotateZ());
                    if (entitydefServer.getRotateX() == 270) {
                        facing = EnumFacing.NORTH;
                        WorldAPI.command(sender, "/mge " + buildName + " rotate set 0 0 0");
                        WorldAPI.command(sender, "/mge " + buildName + " tra set 0 0 0");
                    } else if (entitydefServer.getRotateX() == 180) {
                        facing = EnumFacing.DOWN;
                        WorldAPI.command(sender, "/mge " + buildName + " rotate set 270 0 0");
                        WorldAPI.command(sender, "/mge " + buildName + " tra set 0 -8 1");
                    } else if (entitydefServer.getRotateX() == 90) {
                        facing = EnumFacing.SOUTH;
                        WorldAPI.command(sender, "/mge " + buildName + " rotate set 180 0 0");
                        WorldAPI.command(sender, "/mge " + buildName + " tra set 0 -9 -7");
                    } else if (entitydefServer.getRotateX() == 0) {
                        facing = EnumFacing.UP;
                        WorldAPI.command(sender, "/mge " + buildName + " rotate set 90 0 0");
                        WorldAPI.command(sender, "/mge " + buildName + " tra set 0 -1 -8");
                    }

                    WorldAPI.command(sender, "/mge " + buildName + "-" + facing.rotateAround(EnumFacing.Axis.X).getName().toLowerCase() + " collision true");
                    WorldAPI.command(sender, "/mge " + buildName + "-" + facing.getName().toLowerCase() + " collision false");
                    System.out.println("2222222222 찾음22  " + buildName + "-" + facing.rotateAround(EnumFacing.Axis.X).getName() + "를 충돌하게 만들었고, " + buildName + "-" + facing.getName() + "를 충돌하지 않게 만들었음");

                }
            }
        }
    }
}
