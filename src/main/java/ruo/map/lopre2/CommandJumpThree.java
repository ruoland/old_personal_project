package ruo.map.lopre2;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ruo.cmplus.util.CommandPlusBase;
import ruo.map.lopre2.jump1.EntityBuildBlock;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.Move;
import ruo.minigame.effect.TickRegister;
import ruo.minigame.map.EntityDefaultNPC;
import ruo.minigame.minigame.scroll.EntityJumpCreeper;
import ruo.minigame.minigame.scroll.EntityJumpFlyingCreeper;
import ruo.yout.command.CommandEntity;

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
        if (args[0].equalsIgnoreCase("flying")) {
            EntityJumpFlyingCreeper flyingCreeper = new EntityJumpFlyingCreeper(sender.getEntityWorld());
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
            EntityBuildBlock buildBlock = (EntityBuildBlock) EntityDefaultNPC.getNPC("rotate");
            for(Entity entity : sender.getEntityWorld().loadedEntityList){
                if(!(entity instanceof EntityPlayer)){
                    boolean isJumpBlock = entity instanceof EntityPreBlock && entity.getCustomNameTag().startsWith("rotate");
                    if(EntityList.getEntityString(entity).equalsIgnoreCase("rotate")
                            || entity.getCustomNameTag().equalsIgnoreCase("rotate") || isJumpBlock){
                        buildBlock = (EntityBuildBlock) entity;
                        EntityDefaultNPC entitydefServer = EntityDefaultNPC.getUUIDNPC(buildBlock.getUniqueID());
                        EntityDefaultNPC entitydefClient = (EntityDefaultNPC) buildBlock;

                        EnumFacing facing =null;
                        System.out.println("111111111111 찾음  "+buildBlock.getRotateX() + " - "+buildBlock.getRotateY() + " -"+buildBlock.getRotateZ());
                        if(entitydefServer.getRotateX() == 270){
                            facing = EnumFacing.NORTH;
                            entitydefServer.setRotate(0, 0,0);
                            entitydefServer.setTra(0,1,2);
                            entitydefClient.setRotate(0, 0,0);
                            entitydefClient.setTra(0,1,2);
                            WorldAPI.command(sender, "/mge rotate rotate set 0 0 0");
                            WorldAPI.command(sender, "/mge rotate tra set 0 1 2");
                        }
                        if(entitydefServer.getRotateX() == 180){
                            facing = EnumFacing.DOWN;
                            entitydefServer.setRotate(270, 0,0);
                            entitydefServer.setTra(0,4,0);
                            entitydefClient.setRotate(270, 0,0);
                            entitydefClient.setTra(0,4,0);
                            WorldAPI.command(sender, "/mge rotate rotate set 270 0 0");
                            WorldAPI.command(sender, "/mge rotate tra set 0 4 0");
                        }
                        if(entitydefServer.getRotateX() == 90){
                            facing = EnumFacing.SOUTH;
                            entitydefServer.setRotate(180, 0,0);
                            entitydefServer.setTra(0,6,3);
                            entitydefClient.setRotate(180, 0,0);
                            entitydefClient.setTra(0,6,3);
                            WorldAPI.command(sender, "/mge rotate rotate set 180 0 0");
                            WorldAPI.command(sender, "/mge rotate tra set 0 6 3");
                        }

                        if(entitydefServer.getRotateX() == 0){
                            facing = EnumFacing.UP;
                            entitydefServer.setRotate(90,0,0);
                            entitydefServer.setTra(0,3,5);
                            entitydefClient.setRotate(90,0,0);
                            entitydefClient.setTra(0,3,5);
                            WorldAPI.command(sender, "/mge rotate rotate set 90 0 0");
                            WorldAPI.command(sender, "/mge rotate tra set 0 3 5");
                        }


                        WorldAPI.command(sender, "/mge "+facing.rotateAround(EnumFacing.Axis.X).getName().toLowerCase()+" collision true");
                        WorldAPI.command(sender, "/mge "+facing.getName().toLowerCase()+" collision false");
                        System.out.println("2222222222 찾음22  "+facing.rotateAround(EnumFacing.Axis.X).getName()+"를 충돌하게 만들었고, "+facing.getName()+"를 충돌하지 않게 만들었음");
                    }
                }
            }
            EnumFacing facing = null;

        }
    }

    public  void rotate(){

    }
    public void lavacreeper(double x, double y, double z) {
        TickRegister.register(new AbstractTick(60, true) {
            @Override
            public void run(TickEvent.Type type) {
                for (int i = 0; i < 1; i++) {
                    EntityJumpFlyingCreeper creeper = new EntityJumpFlyingCreeper(WorldAPI.getWorld());
                    creeper.setPosition(x - i, y, z);
                    WorldAPI.getWorld().spawnEntityInWorld(creeper);
                    creeper.setTarget(x - i, y, z + 30, 1);
                }
                if(absRunCount == 15)
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
                if(absRunCount == 15)
                    absLoop = false;
            }
        });

    }

}
