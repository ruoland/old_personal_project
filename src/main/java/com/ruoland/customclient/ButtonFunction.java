package com.ruoland.customclient;

import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldSummary;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import ruo.minigame.api.RuoCode;

import java.io.*;

public class ButtonFunction {
    private CustomTool customTool;
    public ButtonFunction(CustomTool customTool){
        this.customTool = customTool;
    }
    public void init() {
        try {
            File functionFolder = new File("./function");
            functionFolder.mkdirs();
            for (GuiButton button : customTool.getButtonList()) {
                if (button instanceof GuiCusButton) {
                    if (((GuiCusButton) button).canEdit) {
                        File buttonFunction = new File("./function/" + button.displayString + ".txt");
                        if(!buttonFunction.isFile()) {
                            buttonFunction.createNewFile();
                            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(buttonFunction.getPath()), "UTF8"));
                            switch (button.displayString){
                                case "싱글 플레이":
                                    output.write("열기:맵 선택");
                                    break;
                                case "멀티 플레이":
                                    output.write("열기:멀티");
                                    break;
                                case "Language":
                                    output.write("열기:언어");
                                    break;
                                case "게임 종료":
                                    output.write("종료");
                                    break;
                                case "설정":
                                    output.write("열기:설정");
                                    break;
                                default:
                                    output.write("내용을 입력하세요.");
                                    break;
                            }
                            output.close();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(GuiButton button) {
        try {
            File buttonFunction = new File("./function/" + button.displayString + ".txt");
            BufferedReader reader = new BufferedReader(new FileReader(buttonFunction));
            String readline = reader.readLine().replace("\ufeff", "") ;
            System.out.println(readline.substring(0,1).trim()+""+readline.substring(0,1).trim().equalsIgnoreCase(""));
            while (readline != null) {
                if(readline.equalsIgnoreCase("내용을 입력하세요."))
                    break;
                runCommand(readline);
                readline = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runCommand(String command) {
        Minecraft mc = Minecraft.getMinecraft();
        GuiCustomBase screen = customTool.getScreen();
        System.out.println(command);
        if (command.startsWith("종료")) {
            CustomTool.closeBrowser();
            mc.shutdown();
        }
        if (command.startsWith("재생:")) {
            String soundName = command.replace("재생:", "");
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvent.REGISTRY.getObject(new ResourceLocation(soundName)), 1.0F));
        }
        if (command.startsWith("배경 변경:")) {
            String guiName = command.replace("배경 변경:", "");
            customTool.menuData.backgroundImage = guiName;
        }
        if (command.startsWith("열기:")) {
            String guiName = command.replace("열기:", "");
            switch (guiName) {
                case "멀티":
                    mc.displayGuiScreen(new GuiMultiplayer(screen));
                    break;
                case "옵션":
                    mc.displayGuiScreen(new GuiOptions(screen, mc.gameSettings));
                    break;
                case "설정":
                    mc.displayGuiScreen(new GuiOptions(screen, mc.gameSettings));
                    break;
                case "맵 선택":
                    mc.displayGuiScreen(new GuiWorldSelection(screen));
                    break;
                case "언어":
                    mc.displayGuiScreen(new GuiLanguage(screen, mc.gameSettings, mc.getLanguageManager()));
                    break;
            }
        }
        if (command.startsWith("접속:")) {
            String joinName = command.replace("접속:", "");
            System.out.println(joinName);
            if (joinName.startsWith("http")) {
                System.out.println("http");
            } else if (loadWorld(joinName)) {
                System.out.println("loadworld");
                return;
            } else {
                mc.displayGuiScreen(null);
                if(mc.theWorld != null) {
                    mc.theWorld.sendQuittingDisconnectingPacket();
                    mc.loadWorld((WorldClient) null);
                }
                mc.addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        FMLClientHandler.instance().connectToServer(screen,  new ServerData("instance", joinName, false));
                    }
                });
                System.out.println("서버" + joinName);
            }
        }
    }

    private boolean loadWorld(String worldName) {
        Minecraft mc = Minecraft.getMinecraft();
        ISaveFormat isaveformat = mc.getSaveLoader();
        mc.displayGuiScreen(null);
        if(mc.theWorld != null) {
            mc.theWorld.sendQuittingDisconnectingPacket();
            mc.loadWorld((WorldClient) null);
        }
        if (isaveformat.canLoadWorld(worldName)) {
            try {
                for (WorldSummary summary : isaveformat.getSaveList()) {
                    if (summary.getDisplayName().equalsIgnoreCase(worldName)) {
                        net.minecraftforge.fml.client.FMLClientHandler.instance().tryLoadExistingWorld(new GuiWorldSelection(new GuiMainMenu()), summary);
                        return true;
                    }
                }
            } catch (AnvilConverterException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
