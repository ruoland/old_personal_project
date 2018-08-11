package ruo.cmplus.deb;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.CMPlus;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.map.ModelDefaultNPC;
import ruo.minigame.map.RenderDefaultNPC;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

public class DebAPI {
    public static HashMap<String, DebAPI> debAPI = new HashMap<>();
    public int mode;
    public float x = 0, y = 0F, z = 0;
    public float dex, dey, dez;
    public String name;
    public static String activeName;
    public double speed = 0.05;

    private DebAPI(String name) {
        this.name = name;
        debAPI.put(name, this);
    }

    private DebAPI(String name, double x, double y, double z) {
        this(name);
        this.x = (float) x;
        this.y = (float) y;
        this.z = (float) z;
        this.dex = (float) x;
        this.dey = (float) y;
        this.dez = (float) z;
    }

    public static RenderManager getRenderManager() {
        return Minecraft.getMinecraft().getRenderManager();
    }

    public static void deb(){
        for(DebAPI debAPI: debAPI.values()){
            debAPI.a2();
        }
    }

    public static DebAPI get(){
        return debAPI.get(activeName);
    }

    public static void println(){
        DebAPI deb = debAPI.get(activeName);
        System.out.println(activeName+" - "+deb.x+" - "+deb.y+" - "+deb.z);
    }
    public void a2() {
        if(activeName != null && activeName.equalsIgnoreCase(name)) {
            if (DebAPI.isKeyDown(Keyboard.KEY_F)) {
                reset();
            }
            if (DebAPI.isKeyDown(Keyboard.KEY_G)) {
                System.out.println(x+" - "+y+" - "+z);
            }
            if (DebAPI.isKeyDown(Keyboard.KEY_C)) {
                speed+=0.05;
                System.out.println(speed);
            }
            if (DebAPI.isKeyDown(Keyboard.KEY_V)) {
                speed-=0.05;
                System.out.println(speed);
            }
            if (DebAPI.isKeyDown(Keyboard.KEY_J)) {
                x += speed;
                System.out.println("X" + x);
            }
            if (DebAPI.isKeyDown(Keyboard.KEY_I)) {
                y += speed;
                System.out.println("Y" + y);
            }
            if (DebAPI.isKeyDown(Keyboard.KEY_Z)) {
                z += speed;
                System.out.println("Z" + z);
            }
            if (DebAPI.isKeyDown(Keyboard.KEY_L)) {
                x -= speed;
                System.out.println("X" + x);
            }
            if (DebAPI.isKeyDown(Keyboard.KEY_K)) {
                y -= speed;
                System.out.println("Y" + y);
            }
            if (DebAPI.isKeyDown(Keyboard.KEY_X)) {
                z -= speed;
                System.out.println("Z" + z);
            }
        }
    }

    public static boolean isKeyDown(int keycode) {
        return Keyboard.isKeyDown(keycode) && Minecraft.getMinecraft().currentScreen == null;
    }

    public static DebAPI createDebAPI(String name, double x, double y, double z) {
        if (debAPI.containsKey(name))
            return debAPI.get(name);
        else
            return new DebAPI(name, x, y, z);
    }

    public void reset() {
        x = dex;
        y = dey;
        z = dez;
    }

    private static HashMap<String, Configuration> configHash = new HashMap<>();
    public static Configuration getWorldConfig() {
        return getWorldConfig(WorldAPI.getCurrentWorldName());

    }

    public static Configuration getWorldConfig(String worldName) {
        if(!configHash.containsKey(worldName)) {
            try {
                createFile("./saves/" + worldName + "/commandplus/", "key.txt");
                Configuration configuration = new Configuration(new File("./saves/" + worldName + "/commandplus/", "key.txt"));
                configuration.load();
                configuration.save();
                configHash.put(worldName, configuration);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return configHash.get(worldName);
    }

    public static void reloadConfig(){
        for(Configuration config : configHash.values()){
            config.load();
            config.save();
        }
    }

    public static void saveWorldConfig() {
        for (String key : configHash.keySet()) {
            configHash.get(key).save();
        }
    }

    private static HashMap<String, Properties> hash = new HashMap<>();
    public static int debMode = 0;

    public static Properties getWorldProperties() {
        return getWorldProperties(WorldAPI.getCurrentWorldName());
    }

    public static Properties getWorldProperties(String worldName) {
        Properties properties = hash.containsKey(worldName)
                ? hash.get(worldName) : new Properties();
        try {
            File file = new File("./saves/"+worldName+"/commandplus/key.txt");
            if(!file.isFile()) {
                createFile("./saves/" + worldName + "/commandplus/", "key.txt");
            }
            properties.load(new FileInputStream("./saves/" + worldName + "/commandplus/key.txt"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        hash.put(worldName, properties);

        return hash.get(worldName);
    }

    public static void saveWorldProperties() {
        try {
            for (String key : hash.keySet()) {
                System.out.println(hash.get(key).getProperty("tpy"));
                hash.get(key)
                        .store(new FileOutputStream("./saves/" + key + "/commandplus/key.txt"), "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File createFile(String dir, String fileName) {
        File file = new File(dir + fileName);
        if (!file.exists()) {
            try {
                new File(dir).mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static void saveObject(String name, Object array) {
        try {
            FileOutputStream fos = new FileOutputStream("./" + name);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(array);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object readObject(String name, Object defaultObject) {
        Object list = defaultObject;
        try {
            FileInputStream fos = new FileInputStream("./" + name);
            ObjectInputStream oos = new ObjectInputStream(fos);
            list = oos.readObject();
            oos.close();
            fos.close();
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void removeRecipe(ItemStack stack) {
        Iterator recipe = CraftingManager.getInstance().getRecipeList().iterator();
        while (recipe.hasNext()) {
            if (ItemStack.areItemStacksEqual(((IRecipe) (recipe.next())).getRecipeOutput(), stack))
                recipe.remove();
        }
    }

    public static void registerEvent(Object obj) {
        MinecraftForge.EVENT_BUS.register(obj);
    }

    private static int id = 140;

    public static void createJson(Item itemBlock, Item model){
        ResourceLocation resourceLocation = itemBlock instanceof ItemBlock ?((ItemBlock) itemBlock).getBlock().getRegistryName() : itemBlock.getRegistryName();
        ResourceLocation resourceLocationModel = model instanceof ItemBlock ?((ItemBlock) model).getBlock().getRegistryName() : model.getRegistryName();

        String modid = resourceLocation.getResourceDomain();
        String modelBlock = model == null ? "stone" : resourceLocationModel.getResourcePath();
        String itemName = resourceLocation.getResourcePath();
        Gson gson = new Gson();
        JsonObject parent = new JsonObject();
        JsonObject texture = new JsonObject();
        parent.addProperty("parent", itemBlock instanceof ItemBlock ?"block/"+modelBlock : "item/"+modelBlock);
        if(itemBlock instanceof ItemBlock) {
            parent.add("textures", texture);
        }
        File jsonFolder = new File("D:/OneDrive/src/main/resources/assets/"+modid+"");
        File blockStateFolder = new File(jsonFolder+"/blockstates/");
        File blockFolder = new File(jsonFolder+"/models/block/");
        File itemFolder = new File(jsonFolder+"/models/item/");
        File jsonBlockState = new File(blockStateFolder+"/"+itemName+".json");
        File jsonBlock = new File(blockFolder+"/"+itemName+".json");
        File jsonItem = new File(itemFolder+"/"+itemName+".json");
        if(jsonItem.isFile() || jsonBlock.isFile() || jsonBlockState.isFile())
            return;
        if(!jsonFolder.isDirectory())
            return;
        try {
            jsonFolder.mkdirs();
            blockStateFolder.mkdirs();
            blockFolder.mkdirs();
            itemFolder.mkdirs();
            jsonItem.createNewFile();
            BufferedWriter writerItem = Files.newWriter(jsonItem, Charset.forName("UTF-8"));
            writerItem.write(gson.toJson(parent));
            writerItem.newLine();
            writerItem.close();;
            if(itemBlock instanceof ItemBlock) {
                jsonBlockState.createNewFile();
                jsonBlock.createNewFile();
                BufferedWriter writerBlock = Files.newWriter(jsonBlock, Charset.forName("UTF-8"));
                writerBlock.write(gson.toJson(parent));
                writerBlock.newLine();
                writerBlock.close();
                BufferedWriter writerBlockState = Files.newWriter(jsonBlockState, Charset.forName("UTF-8"));
                writerBlockState.write("{ \"variants\": { \"normal\": {\"model\": \""+modelBlock+"\" } } }");
                writerBlockState.newLine();
                writerBlockState.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        ModelLoader.setCustomModelResourceLocation(itemBlock, 0, new ModelResourceLocation(modid+":"+itemName, "inventory"));
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
                .register(itemBlock, 0, new ModelResourceLocation(modid+":"+itemName, "inventory"));
    }
    public static void createJson(Block block, Block model) {
        createJson(new ItemBlock(block), new ItemBlock(model));
    }

    public static void registerBlock(Block block) {
        GameRegistry.register(block);
        GameRegistry.register(new ItemBlock(block), block.getRegistryName());
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0,
                new ModelResourceLocation(block.getRegistryName(), "inventory"));
    }

    public static void registerTileEntity(Block block, Class tileEntity, TileEntitySpecialRenderer renderer) {
        GameRegistry.register(block);
        GameRegistry.register(new ItemBlock(block), block.getRegistryName());
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0,
                new ModelResourceLocation(block.getRegistryName(), "inventory"));
        GameRegistry.registerTileEntity(tileEntity, block.getRegistryName().toString());
        ClientRegistry.bindTileEntitySpecialRenderer(tileEntity, renderer);

    }

    public static void registerEntity(Object mod, String name, Class entity) {
        registerEntity(mod, name, "textures/entity/steve.png", entity, new ModelDefaultNPC());
    }

    public static void registerEntity(Object mod, String name, String texture, Class entity) {
        registerEntity(mod, name, texture, entity, new ModelDefaultNPC());
    }

    public static void registerEntity(Object mod, String name, ResourceLocation texture, Class entity) {
        registerEntity(mod, name, texture, entity, new ModelDefaultNPC());
    }

    public static void registerEntity(Object mod, String name, String texture, Class entity, ModelBase model) {
        registerEntity(mod, name, new ResourceLocation(texture), entity, model);
    }

    public static void registerEntity(Object mod, String name, Class entity, Render r) {
        boolean sendsVelocity = name.indexOf("VELOCITY-") != -1;
        boolean noEgg = name.indexOf("NO-EGG-") != -1;
        name = name.replace("NO-EGG-", "").replace("VELOCITY-", "");

        if (noEgg) {
            EntityRegistry.registerModEntity(entity, name, id, mod, 80, 3, sendsVelocity);
        } else
            EntityRegistry.registerModEntity(entity, name, id, mod, 80, 3, sendsVelocity, getEggColor(name), getEggColor(name) * 5);

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
            RenderingRegistry.registerEntityRenderingHandler(entity, r);
        id++;
    }

    public static void registerEntity(Object mod, String name, ResourceLocation texture, Class entity, ModelBase model) {
        boolean sendsVelocity = name.indexOf("VELOCITY-") != -1;
        boolean noEgg = name.indexOf("NO-EGG-") != -1;
        name = name.replace("NO-EGG-", "").replace("VELOCITY-", "");

        if (noEgg) {
            EntityRegistry.registerModEntity(entity, name.replace("NO-EGG-", ""), id, mod, 80, 3, sendsVelocity);
        } else
            EntityRegistry.registerModEntity(entity, name, id, mod, 80, 3, false, getEggColor(name), getEggColor(name) * 5);
        RenderingRegistry.registerEntityRenderingHandler(entity, new RenderDefaultNPC(model, texture));
        id++;
    }

    public static int getEggColor(String name) {
        int color11 = 0;
        for (byte b : name.getBytes()) {
            color11 += b * 30;
        }
        return color11 * 400;

    }

    public static String readFirstLine(String f) {
        try {
            return Files.readFirstLine(new File(f), Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    public static void save(Object save, String name) {
        try {
            for (Field field : save.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                ConfigCategory category = CMPlus.debConfig.getCategory(name);
                String key = field.getName().toLowerCase();
                Property property = CMPlus.debConfig.getCategory(name).get(key);
                if (property == null)
                    continue;
                Object obj = field.get(null);
                field.setAccessible(true);
                if (field.getType() == double.class)
                    property.set((double) obj);
                if (field.getType() == float.class)
                    property.set((float) obj);
                if (field.getType() == int.class)
                    property.set((int) obj);
                if (field.getType() == String.class)
                    property.set((String) obj);
                if (field.getType() == boolean.class)
                    property.set((boolean) obj);
                if (field.getType() == double[].class)
                    property.set((double[]) field.get(null));
                if (field.getType() == int[].class)
                    property.set((int[]) obj);
                if (field.getType() == String[].class)
                    property.set((String[]) obj);
                if (field.getType() == boolean[].class)
                    property.set((boolean[]) obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ㅣ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
     * 이 메서드는 게임에서 나갈 때 실행됨
     */
    public static void load(Object save, String name) {
        try {
            for (Field field : save.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                ConfigCategory category = CMPlus.debConfig.getCategory(name);
                String key = field.getName().toLowerCase();
                String simName = name;
                Property property = CMPlus.debConfig.getCategory(simName).get(key);

                if (!CMPlus.debConfig.hasKey(save.getClass().getSimpleName(), key))
                    continue;

                if (field.getType() == double.class)
                    field.set(null, property.getDouble());
                if (field.getType() == float.class)
                    field.set(null, (float) property.getDouble());
                if (field.getType() == int.class)
                    field.set(null, property.getInt());
                if (field.getType() == String.class)
                    field.set(null, property.getString());
                if (field.getType() == boolean.class)
                    field.set(null, property.getBoolean());

                if (field.getType() == double[].class)
                    field.set(null, property.getDoubleList());
                if (field.getType() == int[].class)
                    field.set(null, property.getIntList());
                if (field.getType() == String[].class)
                    field.set(null, property.getStringList());
                if (field.getType() == boolean[].class)
                    field.set(null, property.getBooleanList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

