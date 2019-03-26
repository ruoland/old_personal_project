package cmplus;

import oneline.api.NBTAPI;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Set;

public class WaypointManager {
    private static HashMap<String, WaypointManager> wayMap = new HashMap<>();
    private NBTAPI nbt;

    private WaypointManager(String worldname) {
        System.out.println("./saves/" + worldname + "/waypoint.dat");
        nbt = new NBTAPI("./saves/" + worldname + "/waypoint.dat");;
        wayMap.put(worldname, this);

    }

    public static WaypointManager getWaypoint(String worldName){
        return wayMap.containsKey(worldName) ? wayMap.get(worldName) :new WaypointManager(worldName);
    }

    public void add(String name, double x, double y, double z) {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setDouble("posX", x);
        tagCompound.setDouble("posY", y);
        tagCompound.setDouble("posZ", z);
        nbt.getNBT().setTag(name, tagCompound);
        nbt.saveNBT();
        System.out.println(name+x+" - "+y+" - "+z);

    }

    public void add(String name, double[] position) {
        add(name, position[0], position[1], position[2]);

    }

    public Set keySet() {
        return nbt.getNBT().getKeySet();
    }

    public void remove(String name) {
        nbt.getNBT().removeTag(name);
        nbt.saveNBT();
    }

    public double[] get(String name) {
        double[] position = new double[3];
        NBTTagCompound nbtPos = nbt.getNBT().getCompoundTag(name);
        position[0] = nbtPos.getDouble("posX");
        position[1] = nbtPos.getDouble("posY");
        position[2] = nbtPos.getDouble("posZ");
        return position;
    }

    public boolean has(String name) {
        return nbt.getNBT().hasKey(name);
    }

}
