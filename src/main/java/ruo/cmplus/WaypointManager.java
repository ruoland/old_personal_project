package ruo.cmplus;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import ruo.minigame.api.NBTAPI;

import java.util.Set;

public class WaypointManager {
    private NBTAPI nbt;
    public WaypointManager(String worldname) {
        System.out.println("./saves/" + worldname + "/waypoint.dat");
        nbt = new NBTAPI("./saves/" + worldname + "/waypoint.dat");
    }

    public void add(String name, double x, double y, double z) {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setDouble("posX", x);
        tagCompound.setDouble("posY", y);
        tagCompound.setDouble("posZ", z);
        nbt.getNBT().setTag(name, tagCompound);
        nbt.saveNBT();
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
        position[0] = nbtPos.getDouble(name + "-X");
        position[1] = nbtPos.getDouble(name + "-Y");
        position[2] = nbtPos.getDouble(name + "-Z");
        return position;
    }

    public boolean has(String name) {
        return nbt.getNBT().hasKey(name);
    }

}
