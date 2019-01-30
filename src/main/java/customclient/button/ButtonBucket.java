package customclient.button;

import net.minecraft.client.gui.GuiButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class ButtonBucket {
    private File buttonFile;
    private ArrayList<String> scripts = new ArrayList<>();

    public ButtonBucket(GuiButton button) {
        this.buttonFile = new File("./function/" + button.displayString + ".txt");
        System.out.println("[버튼 버킷]버튼 버킷 객체가 생성됨 리로드함");
        reload();
        System.out.println("[버튼 버킷]버튼 버킷 객체가 생성됨 리로드 끝남");
    }

    public boolean isScriptEmpty() {
        return scripts.isEmpty();
    }

    public void reload() {
        scripts.clear();
        try {
            if (buttonFile.length() > 0) {
                BufferedReader reader = new BufferedReader(new FileReader(buttonFile));
                String readline = reader.readLine().replace("\ufeff", "");
                while (readline != null) {
                    scripts.add(readline);
                    readline = reader.readLine();
                }
                reader.close();
                System.out.println(scripts);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getScripts() {
        return scripts;
    }

    public String getAllScript() {
        StringBuffer buffer = new StringBuffer();
        for (String str : scripts) {
            buffer.append(str).append("/n/");
        }
        return buffer.toString();
    }

    public void read(String scripts) {
        this.scripts.clear();
        this.scripts.addAll(Arrays.asList(scripts.split("/n/")));
        System.out.println(scripts);
    }

}
