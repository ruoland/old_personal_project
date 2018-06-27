package ruo.cmplus.cm.beta.custommodelentity;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import ruo.cmplus.util.CommandPlusBase;
import ruo.minigame.api.WorldAPI;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandCustomEntity extends CommandPlusBase{

	@Override
	public String getCommandName() {
		
		return "ce";
	}
	private HashMap<String, EntityCustomMob> entity = new HashMap<String, EntityCustomMob>();

	private ArrayList<CustomModel> model = new ArrayList<>();
	private double x,y,z,x2,y2,z2;
	private double mx,my,mz,mx2,my2,mz2;

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		super.execute(server, sender, args);
		if(args[0].equals("pos1")){
			if(args.length > 2){
				double[] xyz = WorldAPI.valueOfS(args[1],args[2],args[3]);
				x = xyz[0];
				y = xyz[1];
				z = xyz[2];
			}else{
				x = sender.getPosition().getX();
				y = sender.getPosition().getY();
				z = sender.getPosition().getZ();
			}
		}
		if(args[0].equals("pos2")){
			if(args.length > 2){
				double[] xyz = WorldAPI.valueOfS(args[1],args[2],args[3]);
				x2 = xyz[0];
				y2 = xyz[1];
				z2 = xyz[2];
			}else{
				x2 = sender.getPosition().getX();
				y2= sender.getPosition().getY();
				z2 = sender.getPosition().getZ();
			}
		}
		
		if(args[0].equals("model")){
			CustomModel model = new CustomModel(args[1], mx,my,mz,mx2,my2,mz2);
			this.model.add(model);
		}
		if(args[0].equals("cr")){
			EntityCustomMob mob = new EntityCustomMob(WorldAPI.getWorld(), args[1],sender.getPosition().getX(), sender.getPosition().getY(), sender.getPosition().getZ(), x,y,z,x2,y2,z2);
			for(CustomModel model : this.model)
				mob.addModel(model);
			model.clear();
			sender.getEntityWorld().spawnEntityInWorld(mob);
			System.out.println(mob);

		}
	}
	/*
	 * 		x=0;
			y=0;
			z=0;
			x2=0;
			y2=0;
			z2=0;
	 */
}
