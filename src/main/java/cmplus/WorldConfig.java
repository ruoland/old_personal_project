package cmplus;

import minigameLib.api.WorldAPI;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Property;

import java.util.Collection;
import java.util.Set;

//맵마다 다른 설정을 갖게
public class WorldConfig {
    private ConfigCategory category;

    private WorldConfig(String category) {
        this.category = CMPlus.cmPlusConfig.getCategory(category);
    }

    public ConfigCategory getCategory() {
        return category;
    }

    public Property getProperty(String name) {
        return category.get(name);
    }

    public Set keySet() {
        return category.keySet();
    }

    public Collection<Property> values() {
        return category.values();
    }

    public void setProperty(String key, String value) {
        category.get(key).setValue(value);
    }

    public void setValues(String key, String[] values) {
        category.get(key).setValues(values);
    }
    public void setProperty(String key, int value) {
        category.get(key).setValue(value);
    }
    public void setProperty(String key, boolean value) {
        category.get(key).setValue(value);
    }
    public void setProperty(String key, double value) {
        category.get(key).setValue(value);
    }

    public void setValues(String key, boolean[] values) {
        category.get(key).setValues(values);
    }

    public void setValues(String key, int[] values) {
        category.get(key).setValues(values);

    }

    public void setValues(String key, double[] values) {
        category.get(key).setValues(values);
    }


    public boolean containsKey(String name) {
        return category.containsKey(name);
    }

    public boolean containsValue(String name) {
        return category.containsValue(name);
    }

    public static WorldConfig getWorldConfig() {
        return new WorldConfig(WorldAPI.getCurrentWorldName());
    }

    public static WorldConfig getWorldConfig(String worldName) {
        return new WorldConfig(worldName);
    }
}
