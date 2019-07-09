package olib.action;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;

public class DoubleJump {
    //더블 점프 활성화와 플레이어가 점프 상태인가
    public static boolean onDoubleJump, isPlayerJump;

    private static String mapName;


    /**
     * 더블 점프를 한 상태인가
     */
    public static boolean onDoubleJump() {
        return onDoubleJump;
    }

    /**
     * 플레이어가 일반 점프를 한 상태인가
     */
    public static boolean isIsPlayerJump() {
        return isPlayerJump;
    }


    public static void setIsPlayerJump(boolean isPlayerJump) {
        DoubleJump.isPlayerJump = isPlayerJump;
    }

    public static void setOnDoubleJump(boolean onDoubleJump) {
        DoubleJump.onDoubleJump = onDoubleJump;
    }

    public static void doubleJump(boolean on) {
        ActionEffect.getActionData(mapName).mapDoubleJump(on);
    }

    /**
     * 맵에서 더브점프 사용 가능한가?
     */
    public static boolean canMapDoubleJump() {
        return ActionEffect.getActionData(mapName).canMapDoubleJump();
    }

    /**
     * 플레이어가 더블 점프를 사용할 수 있는가?
     */
    public static boolean canUseDoubleJump(EntityPlayer player){
        GameSettings gs = Minecraft.getMinecraft().gameSettings;
        return (!DoubleJump.onDoubleJump && gs.keyBindJump.isPressed() && !player.onGround && DoubleJump.isPlayerJump);
    }

    public static void resetDoubleJump(){
        DoubleJump.onDoubleJump = false;
        DoubleJump.isPlayerJump = false;
    }

    public static void setMapName(String mapName) {
        DoubleJump.mapName = mapName;
    }

}
