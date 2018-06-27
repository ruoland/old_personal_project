package ruo.minigame.api;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EasyScript {
    private String name, defaultValue;
    public EasyScript(String scriptName, String defaultValue){
        name = scriptName;
        this.defaultValue = defaultValue;

        File file = new File("./"+scriptName);
        try {
            file.createNewFile();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String read(int line){
        try {
            return Files.readAllLines(Paths.get("./" + name), Charset.forName("UTF-8")).get(line);
        }catch (Exception e){
            e.printStackTrace();
            return defaultValue;
        }
    }
}
