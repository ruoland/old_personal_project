package cmplus.cm.v18.customgui;

import cmplus.cm.v18.function.VAR;
import cmplus.util.CommandPlusBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;

public class CommandGUI extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		
		if (args.length == 1)
			Minecraft.getMinecraft().displayGuiScreen(new GuiCustom(args[0]));
		else if (Minecraft.getMinecraft().currentScreen instanceof GuiCustom) {
			GuiCustom custom = (GuiCustom) Minecraft.getMinecraft().currentScreen;

			if(t.argCheck(args[0], "close", "닫기")){
				Minecraft.getMinecraft().displayGuiScreen(null);
			}
			if(t.argCheck(args[0], "remove", "삭제")){
				for(GuiButton button : custom.getButton()){
					if(button.displayString.equals(args[1])){
						custom.getButton().remove(button);
						return;
					}
				}
				for(GuiTexture button : custom.getTexture()){
					if(button.id == Integer.valueOf(args[1])){
						custom.getTexture().remove(button);
						return;
					}
				}
				for(GuiString button : custom.getString()){
					if(button.id == Integer.valueOf(args[1])){
						custom.getString().remove(button);
						return;
					}
				}
			}
			if (t.argCheck(args[0], "string", "문자")) {
				for(GuiString button : custom.getString()){
					if(button.id == Integer.valueOf(args[1])){
						if(args[2].equals("X")){
							button.x = ""+ t.math(args[3], VAR.getDouble(button.x), VAR.getDouble(args[3]));
						}
						if(args[2].equals("Y")){
							button.y =""+t.math(args[3], VAR.getDouble(button.y), VAR.getDouble(args[3]));
						}
						if(args[2].equals("V")){
							button.visible = VAR.getBoolean(args[3]);
						}
						return;
					}
				}
				GuiString texture = new GuiString(custom.getString().size(), args[2], args[3] ,args[4]);
				custom.getString().add(texture);
			}
			if (t.argCheck(args[0], "image", "이미지")) {
				for(GuiTexture button : custom.getTexture()){
					if(button.id == Integer.valueOf(args[1])){
						if(args[2].equals("X")){
							button.x = ""+ t.math(args[3], VAR.getDouble(button.x), VAR.getDouble(args[3]));
						}
						if(args[2].equals("Y")){
							button.y =""+t.math(args[3], VAR.getDouble(button.y), VAR.getDouble(args[3]));
						}
						if(args[2].equals("W")){
							button.width = ""+ t.math(args[3], VAR.getDouble(button.width), VAR.getDouble(args[3]));
						}
						if(args[2].equals("H")){
							button.height = ""+ t.math(args[3], VAR.getDouble(button.height), VAR.getDouble(args[3]));
						}
						if(args[2].equals("V")){
							button.visible = VAR.getBoolean(args[3]);
						}
						if(args[2].equals("T")){
							button.resourceLocation = new ResourceLocation(args[3]);
						}
						return;
					}
				}
				GuiTexture texture = new GuiTexture(custom.getButton().size(), args[2], args[3] ,args[4], args[5], args[6]);
				custom.getTexture().add(texture);
			}
			if (t.argCheck(args[0], "button", "버튼")) {
				for(GuiButton button : custom.getButton()){
					if(button.id == Integer.valueOf(args[1])){
						if(args[2].equals("X")){
							button.xPosition = (int) t.math(args[3], VAR.getDouble(""+button.xPosition), VAR.getDouble(args[3]));
						}
						if(args[2].equals("Y")){
							button.yPosition = (int) t.math(args[3], VAR.getDouble(""+button.yPosition), VAR.getDouble(args[3]));
						}
						if(args[2].equals("W")){
							button.width = (int) t.math(args[3], VAR.getDouble(""+button.width), VAR.getDouble(args[3]));
						}
						if(args[2].equals("H")){
							button.height = (int) t.math(args[3], VAR.getDouble(""+button.height), VAR.getDouble(args[3]));
						}
						if(args[2].equals("V")){
							button.visible = VAR.getBoolean(args[3]);
						}
						if(args[2].equals("N")){
							button.displayString = VAR.getStr(args[3]);

						}
						return;
					}
				}

				if(Double.valueOf(args[2]) != null){
					
				}
				GuiCustom.GuiCusButton b = new GuiCustom.GuiCusButton(Integer.valueOf(args[1]), (int) VAR.getDouble(args[2]),  (int) VAR.getDouble(args[3]), args[1]);
				custom.getButton().add(b);
			}
			
		}
	}

}
