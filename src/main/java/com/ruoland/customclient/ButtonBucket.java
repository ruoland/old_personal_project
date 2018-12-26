package com.ruoland.customclient;

import net.minecraft.client.gui.GuiButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class ButtonBucket {
    private File buttonFile;
    private ArrayList<String> scripts = new ArrayList<>();
    private long lastEdited;

    public ButtonBucket(GuiButton button){
        this.buttonFile = new File("./function/" + button.displayString + ".txt");
        reload();
    }

    public void reload(){
        if(lastEdited != buttonFile.lastModified()) {
            scripts.clear();
            try {
                if(buttonFile.length() > 0) {
                    BufferedReader reader = new BufferedReader(new FileReader(buttonFile));
                    String readline = reader.readLine().replace("\ufeff", "");
                    while (readline != null) {
                        scripts.add(readline);
                        readline = reader.readLine();
                    }
                }
                lastEdited = buttonFile.lastModified();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<String> getScripts() {
        return scripts;
    }
}
