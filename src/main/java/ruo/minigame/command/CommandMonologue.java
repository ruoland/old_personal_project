package ruo.minigame.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import ruo.cmplus.util.CommandPlusBase;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.TextEffect;
import ruo.minigame.text.Monologue;

public class CommandMonologue extends CommandPlusBase{

	@Override
	public String getCommandName() {
		
		return "monologue2";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		
		return "";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		Monologue mono = TextEffect.monologue().setPause(false).setDark(true);
		String text = WorldAPI.str(args, true);
		String[] splitNext = new String[0];
		if(text.indexOf("<next<") != -1)
			splitNext = text.toString().split("<next<");
		StringBuffer[] textBuffer = text(mono, args);
		mono.addText(0, textBuffer);
		for(int i=1; i < splitNext.length; i++){
			StringBuffer[] splitNextBuffer = text(mono, splitNext[i].trim().split(" "));
			mono.addText(i, splitNextBuffer);
		}
		System.out.println("전체 내용 "+text);
		mono.start();
	}
	
	public StringBuffer[] text(Monologue mono, String[] args){
		int next = 0;
		StringBuffer[] textBuffer = new StringBuffer[]{
				new StringBuffer(), new StringBuffer(), new StringBuffer()
				,new StringBuffer() ,new StringBuffer()
		};
		for(String arg : args){
			if(arg.equals("<next<")){
				break;
			}				
			if(arg.equals("true") || arg.equals("false"))
			{
				mono.setPause(Boolean.valueOf(args[0]));
				mono.setDark(Boolean.valueOf(args[1]));
				continue;
			}
		
			if(arg.equals("<line<")){
				next++;
				textBuffer[next].append(arg+" ");
			} 
			else{
				textBuffer[next].append(arg+" ");
			}

		}
		return textBuffer;
	}

}
