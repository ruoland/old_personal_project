package ruo.yout.command;

import cmplus.cm.v17.CommandClip;
import cmplus.util.CommandPlusBase;
import net.minecraft.block.Block;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandParticle;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import olib.api.WorldAPI;
import olib.effect.TickRegister;
import olib.effect.TickTask;
import ruo.yout.mojaelab.YEvent;

import java.util.List;

//아래로 내려가면 무슨 일이 일어나는지 실험하는 용 명령어
public class CommandMojaeY extends CommandPlusBase {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayer player = (EntityPlayer) sender;
        World world = sender.getEntityWorld();

        if(args[0].equalsIgnoreCase("time")){
            TickRegister.register(new TickTask(200000, true) {
                int time = 1;
                @Override
                public void run(TickEvent.Type type) {
                    WorldAPI.worldtime(time * 2);
                    System.out.println("시간 증가");
                }
            });
        }
        if (args[0].equalsIgnoreCase("b")) {
            sender.getEntityWorld().setBlockState(sender.getPosition(), Block.getBlockFromName(args[1]).getDefaultState());
            sender.addChatMessage(new TextComponentString(sender.getPosition()+ " 에 " + Block.getBlockFromName(args[1]).getLocalizedName()+"를 설치했습니다."));
        }
        if (args[0].equalsIgnoreCase("e")) {
            EntityLiving living = (EntityLiving) EntityList.createEntityByName(args[1], sender.getEntityWorld());
            living.setPosition(sender.getPositionVector().xCoord, sender.getPositionVector().yCoord, sender.getPositionVector().zCoord);
            sender.getEntityWorld().spawnEntityInWorld(living);
            PotionEffect potioneffect = new PotionEffect(MobEffects.GLOWING, 20000, 0);
            living.addPotionEffect(potioneffect);
            sender.addChatMessage(new TextComponentString(sender.getPosition()+ " 에" + living.getName()+"를(을) 소환했습니다."));

        }
        if (args[0].equalsIgnoreCase("r")) {
            EntityLiving living = (EntityLiving) EntityList.createEntityByName(args[1], sender.getEntityWorld());
            living.setPosition(sender.getPositionVector().xCoord, sender.getPositionVector().yCoord, sender.getPositionVector().zCoord);
            sender.getEntityWorld().spawnEntityInWorld(living);
            sender.getCommandSenderEntity().startRiding(living);
            PotionEffect potioneffect = new PotionEffect(MobEffects.GLOWING, 20000, 0);
            living.addPotionEffect(potioneffect);
            sender.addChatMessage(new TextComponentString(living.getName()+"를 소환하고 탑승했습니다."));
        }
        if(args[0].equalsIgnoreCase("tp")){
            WorldAPI.teleport(sender.getPosition().getX(), Double.valueOf(args[1]), sender. getPosition().getZ());
            sender.addChatMessage(new TextComponentString("Y를  "+(args[1])+"으로 내려갔습니다."));
        }

        if (args[0].equalsIgnoreCase("mo")) {
            ItemStack itemStack = new ItemStack(Items.NAME_TAG);
            itemStack.setStackDisplayName(args.length < 1 ? "모리" : "모리 라이딩");
            player.inventory.addItemStackToInventory(itemStack);
        }

        if(args[0].equalsIgnoreCase("speed")){
            YEvent.speed = Double.valueOf(args[1]);
        }

        if (args[0].equalsIgnoreCase("item")) {
            player.inventory.addItemStackToInventory(new ItemStack(Items.BOW));
            player.inventory.addItemStackToInventory(new ItemStack(Items.ARROW, 64));
            player.inventory.addItemStackToInventory(new ItemStack(Items.FISHING_ROD));
            player.inventory.addItemStackToInventory(new ItemStack(Items.ENDER_PEARL, 16));
        }
        if(args[0].equalsIgnoreCase("particle")){
            if(args.length > 1) {
                WorldAPI.command(sender, "/particle " + args[1] + " ~ ~ ~ 1 1 1 0 10");
                return;
            }
                WorldAPI.command(sender, "/particle largeexplode ~ ~ ~ 1 1 1 0 10");
        }
        if (args[0].equalsIgnoreCase("summon")) {
            String name = args[1];
            double d0 = ((EntityPlayer) sender).posX;
            double d1 = ((EntityPlayer) sender).posY;
            double d2 = ((EntityPlayer) sender).posZ;
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            if ("LightningBolt".equalsIgnoreCase(name)) {
                world.addWeatherEffect(new EntityLightningBolt(world, d0, d1, d2, false));
                notifyCommandListener(sender, this, "commands.summon.success", new Object[0]);
                return;
            }
            nbttagcompound.setString("id", name);
            Entity entity = AnvilChunkLoader.readWorldEntityPos(nbttagcompound, world, d0, d1, d2, true);
            entity.setLocationAndAngles(d0, d1, d2, entity.rotationYaw, entity.rotationPitch);
            if (args.length > 2 && args[2].equalsIgnoreCase("riding")) {
                player.startRiding(entity);
            }
        }
    }


    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if (args[0].equalsIgnoreCase("b"))
            return getListOfStringsMatchingLastWord(args, Block.REGISTRY.getKeys());
        else if (args[0].equalsIgnoreCase("e") || args[0].equalsIgnoreCase("r")) {
            return getListOfStringsMatchingLastWord(args, EntityList.getEntityNameList());
        }
        else if (args[0].equalsIgnoreCase("particle") ) {
            return getListOfStringsMatchingLastWord(args, EnumParticleTypes.getParticleNames());
        }
        return super.getTabCompletionOptions(server, sender, args, pos);
    }
}
