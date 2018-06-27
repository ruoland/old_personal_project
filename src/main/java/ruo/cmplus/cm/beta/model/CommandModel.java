package ruo.cmplus.cm.beta.model;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import ruo.cmplus.CMManager;
import ruo.cmplus.util.CommandPlusBase;
import ruo.minigame.api.WorldAPI;

public class CommandModel extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

		super.execute(server, sender, args);
		if (args[0].equalsIgnoreCase("sleep")) {
			setRenderOffsetForSleep(EnumFacing.byName(t.findString(args, 2, "NORTH")));
			setSize(0.2F, 0.2F);
			if(Boolean.valueOf(args[1])) {
				CMManager.setSit(false);
			}
			CMManager.setSleep(Boolean.valueOf(args[1]), EnumFacing.byName(t.findString(args, 2, "NORTH")));
		}
		if (args[0].equalsIgnoreCase("sit")) {
			if(Boolean.valueOf(args[1])) {
				CMManager.setSleep(false, EnumFacing.NORTH);
			}
			CMManager.setSit(Boolean.valueOf(args[1]));
		}
		if (args[0].equalsIgnoreCase("모두") || args[0].equalsIgnoreCase("all")) {
			if(args[2].equals("move")) {
				CMModelPlayer.traX = Float.valueOf(args[3]);
				CMModelPlayer.traY = Float.valueOf(args[4]);
				CMModelPlayer.traZ = Float.valueOf(args[5]);
			}
			if(args[2].equals("rotate")) {
				CMModelPlayer.roX = Float.valueOf(args[3]);
				CMModelPlayer.roY = Float.valueOf(args[4]);
				CMModelPlayer.roZ = Float.valueOf(args[5]);
			}
		}
		if (args[0].equalsIgnoreCase("왼팔") || args[0].equalsIgnoreCase("leftarm") || args[0].equalsIgnoreCase("la")) {
			if (args[1].equalsIgnoreCase("x"))
				CMModelPlayer.leftArmX = Float.valueOf(args[2]);
			if (args[1].equalsIgnoreCase("z"))
				CMModelPlayer.leftArmZ = Float.valueOf(args[2]);
		}
		if (args[0].equalsIgnoreCase("왼팔") || args[0].equalsIgnoreCase("leftarm") || args[0].equalsIgnoreCase("la")) {
			if (args[1].equalsIgnoreCase("x"))
				CMModelPlayer.leftArmX = Float.valueOf(args[2]);
			if (args[1].equalsIgnoreCase("z"))
				CMModelPlayer.leftArmZ = Float.valueOf(args[2]);
		}
		if (args[0].equalsIgnoreCase("오른팔") || args[0].equalsIgnoreCase("rightarm") || args[0].equalsIgnoreCase("ra")) {
			if (args[1].equalsIgnoreCase("x"))
				CMModelPlayer.rightArmX = Float.valueOf(args[2]);
			if (args[1].equalsIgnoreCase("z"))
				CMModelPlayer.rightArmZ = Float.valueOf(args[2]);
		}
		if (args[0].equalsIgnoreCase("왼다리") || args[0].equalsIgnoreCase("leftleg") || args[0].equalsIgnoreCase("ll")) {
			if (args[1].equalsIgnoreCase("x"))
				CMModelPlayer.leftLegX = Float.valueOf(args[2]);
			if (args[1].equalsIgnoreCase("z"))
				CMModelPlayer.leftLegZ = Float.valueOf(args[2]);
		}
		if (args[0].equalsIgnoreCase("오른다리") || args[0].equalsIgnoreCase("rightleg")
				|| args[0].equalsIgnoreCase("rl")) {
			if (args[1].equalsIgnoreCase("x"))
				CMModelPlayer.rightLegX = Float.valueOf(args[2]);
			if (args[1].equalsIgnoreCase("z"))
				CMModelPlayer.rightLegZ = Float.valueOf(args[2]);
		}
	}

	/**
     * Sets the width and height of the entity.
     */
    protected void setSize(float width, float height)
    {
        if (width != WorldAPI.getPlayer().width || height != WorldAPI.getPlayer().height)
        {
            float f = WorldAPI.getPlayer().width;
            WorldAPI.getPlayer().width = width;
            WorldAPI.getPlayer().height = height;
            AxisAlignedBB axisalignedbb = WorldAPI.getPlayer().getEntityBoundingBox();
            WorldAPI.getPlayer().setEntityBoundingBox(new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + (double)WorldAPI.getPlayer().width, axisalignedbb.minY + (double)WorldAPI.getPlayer().height, axisalignedbb.minZ + (double)WorldAPI.getPlayer().width));

            if (WorldAPI.getPlayer().width > f  && !WorldAPI.getPlayer().worldObj.isRemote)
            {
            	WorldAPI.getPlayer().moveEntity((double)(f - WorldAPI.getPlayer().width), 0.0D, (double)(f - WorldAPI.getPlayer().width));
            }
        }
    }
	private void setRenderOffsetForSleep(EnumFacing p_175139_1_) {
		WorldAPI.getPlayer().renderOffsetX = 0.0F;
		WorldAPI.getPlayer().renderOffsetZ = 0.0F;

		switch (p_175139_1_) {
		case SOUTH:
			WorldAPI.getPlayer().renderOffsetZ = -1.8F;
			break;
		case NORTH:
			WorldAPI.getPlayer().renderOffsetZ = 1.8F;
			break;
		case WEST:
			WorldAPI.getPlayer().renderOffsetX = 1.8F;
			break;
		case EAST:
			WorldAPI.getPlayer().renderOffsetX = -1.8F;
		default:
			break;
		}
	}

}
