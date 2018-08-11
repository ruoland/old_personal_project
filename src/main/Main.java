import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.charset.Charset;
import java.util.HashMap;

public class Main {
    private static StringBuffer renderText = new StringBuffer("");
    private static double prevX = 50, prevZ = 40;
    protected static HashMap<String, Double> yTeleport = new HashMap<>();

    public static boolean isRangeX(double x) {
        return prevX + 1 >= x && prevX - 1 <= x;
    }

    public static boolean isRangeZ(double z) {
        return prevZ + 1 >= z && prevZ - 1 <= z;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Gson gson = new Gson();
        JsonObject parent = new JsonObject();
        JsonObject texture = new JsonObject();
        JsonObject object = new JsonObject();
        BlockPos[] blockPos = new BlockPos[]{
                new BlockPos(1,2,3),new BlockPos(4,5,6)
        };
        parent.add("elements", gson.toJsonTree(blockPos));

        JsonParser parser = new JsonParser();
        JsonObject element = (JsonObject) parser.parse(new FileReader("./asdf.json"));
        JsonElement elements = element.get("elements");
        BlockPos[] blockPos1 = gson.fromJson(gson.toJson(elements), BlockPos[].class);
        blockPos[0].print();
        blockPos[1].print();

        System.out.println(""+blockPos1[0].name +blockPos1[0].getX()+blockPos1[0].getY()+blockPos1[0].getZ());
        System.out.println(""+blockPos1[1].name +blockPos1[1].getX()+blockPos1[1].getY()+blockPos1[1].getZ());
        try {
            BufferedWriter writerItem = Files.newWriter(new File("./asdfff.json"), Charset.forName("UTF-8"));
            writerItem.write(gson.toJson(parent));
            writerItem.newLine();
            writerItem.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void asdfaf(HashMap map){
        map.put("asdff", 145123D);
    }

    public static void a() {
        System.out.println("---" + (1F / 20F));
    }

    public static void a2() {
}

}
