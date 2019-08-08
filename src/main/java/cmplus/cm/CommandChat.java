package cmplus.cm;

import cmplus.util.CommandTool;
import olib.api.WorldAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.List;

public class CommandChat extends CommandBase {

	@Override
	public String getCommandName() {
		return "chat";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commandPlus.chat.help";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {

		//채팅 메세지를 지우거나 변경하거나 단어를 추가하는 명령어
		CommandTool t = new CommandTool(getCommandName());
		EntityPlayerMP p = WorldAPI.getPlayerMP();
		if (t.length(args)) {
			t.addLoMessage("help.delete");
			t.addLoMessage("help.set");
			t.addLoMessage("help.addh");
			t.addLoMessage("help.gui");
			return;
		}
		int line = 0;
		if (!args[1].equals("all")) {
			line = Integer.valueOf(args[1]);
		}
		if (t.argCheck(args[0], "delete", "삭제")) {
			if (args[1].equals("all")) {
				Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages();
				return;
			}
			deleteChatLine(line);
			return;
		}
		if (t.argCheck(args[0], "set", "설정")) {
			String setchat = t.getCommand(args, 2, args.length);
			changeChatLine(line, setchat);
			return;
		}

		if (t.argCheck(args[0], "add", "추가")) {
			String addchat = t.getCommand(args, 2, args.length);
			addChatLine(line, addchat);
			return;
		}
		if(args[0].equals("gui")){
			t.getSetting().chatVisibility = EnumChatVisibility.valueOf(args[1].toUpperCase());
			return;
		}
	}
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
    	//"삭제", "설정", "추가", , "모두"
    	if(args.length == 1)
    		 return getListOfStringsMatchingLastWord(args, "delete", "set", "add", "gui");
    	if(args.length == 2)
   		 return getListOfStringsMatchingLastWord(args, "FULL", "SYSTEM", "HIDDEN");
    	else {
    		return super.getTabCompletionOptions(server, sender, args, pos);
    	}
    }
	
	public static void deleteChatLine(int p_146242_1_) {
		try {
			GuiNewChat c = Minecraft.getMinecraft().ingameGUI.getChatGUI();
			java.lang.reflect.Field f = c.getClass().getDeclaredField("field_146253_i");
			java.lang.reflect.Field f2 = c.getClass().getDeclaredField("field_146252_h");// field_146252_h chatLines
			f.setAccessible(true);
			f2.setAccessible(true);
			List list = ((List) f.get(c));
			List chatline = ((List) f2.get(c));
			list.remove(p_146242_1_);
			chatline.remove(p_146242_1_);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void addChatLine(int p_146242_1_, String text) {
		try {
			Minecraft mc = Minecraft.getMinecraft();
			GuiNewChat c = mc.ingameGUI.getChatGUI();
			java.lang.reflect.Field f = c.getClass().getDeclaredField("chatLines");
			f.setAccessible(true);
			java.lang.reflect.Field f2 = c.getClass().getDeclaredField("drawnChatLines");
			f2.setAccessible(true);
			List<ChatLine> list = ((List) f.get(c));
			List<ChatLine> chatline = ((List) f2.get(c));
			Style style = new Style();
			if(text.indexOf("/bold/") != -1){
				text = text.replace("/bold/", "");
				style.setBold(true);
			}
			list.set(p_146242_1_, new ChatLine(mc.ingameGUI.getUpdateCounter(),
					new TextComponentString(list.get(p_146242_1_).getChatComponent().getFormattedText()).appendSibling(new TextComponentString(text).setStyle(style)), 0));
			chatline.set(p_146242_1_, new ChatLine(mc.ingameGUI.getUpdateCounter(),
					new TextComponentString(chatline.get(p_146242_1_).getChatComponent().getFormattedText()).appendSibling(new TextComponentString(text).setStyle(style)),
					0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void changeChatLine(int p_146242_1_, String text) {
		try {
			Minecraft mc = Minecraft.getMinecraft();
			GuiNewChat c = mc.ingameGUI.getChatGUI();
			java.lang.reflect.Field f = c.getClass().getDeclaredField("chatLines");
			f.setAccessible(true);
			java.lang.reflect.Field f2 = c.getClass().getDeclaredField("drawnChatLines");
			f2.setAccessible(true);
			List lise = ((List) f.get(c));
			List chatline = ((List) f2.get(c));
			Style style = new Style();
			if(text.indexOf("/bold/") != -1){
				text = text.replace("/bold/", "");
				style.setBold(true);
			}
			lise.set(p_146242_1_, new ChatLine(mc.ingameGUI.getUpdateCounter(), new TextComponentString(text).setStyle(style), 0));
			chatline.set(p_146242_1_, new ChatLine(mc.ingameGUI.getUpdateCounter(), new TextComponentString(text).setStyle(style), 0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getChatLine(int line){
		Minecraft mc = Minecraft.getMinecraft();
		return mc.ingameGUI.getChatGUI().getSentMessages().size() > 0 ? mc.ingameGUI.getChatGUI().getSentMessages().get(line) : "없음";
	}
	public static String getLastChat(){
		Minecraft mc = Minecraft.getMinecraft();
		List<String> l = mc.ingameGUI.getChatGUI().getSentMessages();
		return l.size() > 0 ? l.get(l.size()-1) : "없음";
	}
}
