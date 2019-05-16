package cmplus.util;

import olib.api.WorldAPI;
import olib.map.EntityDefaultNPC;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntityNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.List;

public abstract class CommandPlusBase extends CommandBase {

	public CommandTool t;


	public static ITextComponent text(String text) {
		return new TextComponentString(text);
	}

	public static ITextComponent text(String text, ClickEvent event) {
		return new TextComponentSelector(text).setStyle(new Style().setClickEvent(event));
	}

	public static ITextComponent text(String text, ClickEvent event, TextFormatting form) {
		return new TextComponentSelector(text).setStyle(new Style().setClickEvent(event).setColor(form));
	}

	public static ITextComponent text(String text, TextFormatting form) {
		return new TextComponentSelector(text).setStyle(new Style().setColor(form));
	}

	public CommandPlusBase() {
		t = new CommandTool(getCommandName());
	}

	@Override
	public String getCommandName() {
		String command = this.getClass().getName().replace("Command", "").toLowerCase();
		String[] split = command.split("\\.");
		return split[split.length - 1];
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "commandPlus." + getCommandName() + ".help";
	}

	public Entity addAI(MinecraftServer server, ICommandSender sender, String args, EntityAIBase ab)
			throws CommandException {
		Entity get = EntityDefaultNPC.getNPC(args);
		if (get == null) {
			for (Entity entity : WorldAPI.getWorld().loadedEntityList) {
				if (entity instanceof EntityMob && entity.getName().equalsIgnoreCase(args)
						|| entity.getCustomNameTag().equalsIgnoreCase(args)) {
					EntityMob mob = (EntityMob) entity;
					if (ab instanceof EntityAINearestAttackableTarget || ab instanceof EntityAIAvoidEntity) {
						mob.targetTasks.addTask(0, ab);
						mob.targetTasks.addTask(1, new EntityAIHurtByTarget((EntityCreature) mob, true, new Class[0]));
						mob.tasks.addTask(2, new EntityAIAttackMelee((EntityCreature) mob, 0.24, false));
					} else
						mob.tasks.addTask(0, ab);
				}
			}
		}
		if (get == null) {
			get = getEntity(server, sender, args);
		}

		return get;
	}

	public static Entity getPlusEntity(MinecraftServer server, ICommandSender sender, String args) throws CommandException {
		Entity get = EntityDefaultNPC.getNPC(args);
		if (get == null) {
			for (Entity entity : WorldAPI.getWorld().loadedEntityList) {
				if (entity.getName().equalsIgnoreCase(args) || entity.getCustomNameTag().equalsIgnoreCase(args)) {
					return entity;
				}
			}
		}
		if (get == null) {
			try {
				get = getEntity(server != null ? server : FMLCommonHandler.instance().getMinecraftServerInstance(), sender, args);
			}catch (EntityNotFoundException e) {
				System.out.println("엔티티를 찾을 수 없음!"+args);
				return null;
			}
		}
		return get;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	/**
	 * 인자가 있는지 없는지 확인하는 메서드
	 */
	public String checkArgs(String[] args, int s) {
		if (args.length <= s) {
			System.out.println(getCommandName() + "인자 없음.");
			return "";
		} else
			return args[s];
	}

	public boolean getTrueFalse(String args) {
		if (args.equalsIgnoreCase("진실") || args.equalsIgnoreCase("true")) {
			return true;
		}
		if (args.equalsIgnoreCase("거짓") || args.equalsIgnoreCase("false")) {
			return false;
		}
		return false;
	}

	public double[] getPos(ICommandSender sender, int start, String[] args) {
		if (args.length > start + 2) {
			double[] pos = new double[3];
			pos[0] = args[start].equalsIgnoreCase("~") ? sender.getPosition().getX() : Double.valueOf(args[start]);
			pos[1] = args[start + 1].equalsIgnoreCase("~") ? sender.getPosition().getY()
					: Double.valueOf(args[start + 1]);
			pos[2] = args[start + 2].equalsIgnoreCase("~") ? sender.getPosition().getZ()
					: Double.valueOf(args[start + 2]);
			return pos;
		} else {
			return WorldAPI.changePosArray((EntityPlayer) sender);
		}

	}

	public BlockPos getBlockPos(ICommandSender sender, int start, String[] args) {
		if (args.length > 2) {
			double[] pos = new double[3];
			pos[0] = args[start].equalsIgnoreCase("~") ? sender.getPosition().getX() : Double.valueOf(args[start]);
			pos[1] = args[start + 1].equalsIgnoreCase("~") ? sender.getPosition().getY()
					: Double.valueOf(args[start + 1]);
			pos[2] = args[start + 2].equalsIgnoreCase("~") ? sender.getPosition().getZ()
					: Double.valueOf(args[start + 2]);
			return new BlockPos(pos[0], pos[1], pos[2]);
		}
		return null;
	}

	public BlockPos getBlockPos(ICommandSender sender, String[] args) {
		if (args.length > 2) {
			double[] pos = new double[3];
			pos[0] = args[0].equalsIgnoreCase("~") ? sender.getPosition().getX() : Double.valueOf(args[0]);
			pos[1] = args[1].equalsIgnoreCase("~") ? sender.getPosition().getY() : Double.valueOf(args[1]);
			pos[2] = args[2].equalsIgnoreCase("~") ? sender.getPosition().getZ() : Double.valueOf(args[2]);
			return new BlockPos(pos[0], pos[1], pos[2]);
		}
		return null;
	}

	public CommandTool t() {
		return new CommandTool(getCommandName());
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos pos) {
		t.addLoMessage("help");
		return super.getTabCompletionOptions(server, sender, args, pos);
	}
}
