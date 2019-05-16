package olib.android;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandCall extends CommandBase{

	@Override
	public String getCommandName() {
		
		return "call";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		
		return null;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		Android android = new Android();
		Call call = new Call() {
			
			@Override
			public void end() {
				System.out.println("종료됨");
			}
			
			@Override
			public void dial() {
				
			}
			
			@Override
			public void call() {
			}
		};
		if(android.isLogin){
			android.sendCall(call, args[0]);
		}
	}

}
