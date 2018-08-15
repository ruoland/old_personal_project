package ruo.cmplus;

import java.util.Set;

public class WaypointManager {

    public static void add(String name, double x, double y, double z){
        double[] position = new double[]{x,y,z};
        CMPlus.cmPlusConfig.get("waypoint", name, position).set(position);
    }
    public static void add(String name, double[] position){
        CMPlus.cmPlusConfig.get("waypoint", name, position).set(position);
    }
    public static Set keySet(){
        return CMPlus.cmPlusConfig.getCategory("waypoint").keySet();
    }
    public static void remove(String name){
        CMPlus.cmPlusConfig.getCategory("waypoint").remove(name);
    }
    public static double[] get(String name){
        return CMPlus.cmPlusConfig.get("waypoint", name, (double[]) null).getDoubleList();
    }
    public static boolean has(String name){
        return CMPlus.cmPlusConfig.hasKey("waypoint", name);
    }
}
