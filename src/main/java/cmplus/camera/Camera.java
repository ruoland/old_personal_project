package cmplus.camera;

import olib.api.WorldAPI;
import olib.effect.TickTask;
import olib.effect.TickRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;

public class Camera {
	public static double rotateX, rotateY, rotateZ, zoom = 70;
	public static double traX, traY, traZ;
	public static float lockPlayerYaw, lockPlayerPitch;//TODO 왜 있는지 모르겠음
	public static float lockCameraYaw, lockCameraPitch;//
	public static boolean lockCamera, lockPerson, yp;
	public static boolean isDebug = false, freeCam = false;
	private static Minecraft mc = Minecraft.getMinecraft();
	private static EntityRenderer r = mc.entityRenderer;

	public static void reset() {
		setZoom(70);
		rotateCamera(0, 0, 0);
		moveCamera(0, 0, 0);
		lockCameraPitch = 0;
		lockCameraYaw = 0;
		lockPlayerPitch = 0;
		lockPlayerYaw = 0;
		playerCamera(false);
		lockCamera(false, lockPlayerPitch, lockPlayerYaw);
		setYP(false);
	}

	public static void setYP(boolean yp) {
		EntityPlayer player = WorldAPI.getPlayer();
		yp = yp;
		lockPlayerPitch = 0;
		lockPlayerYaw = 0;
	}

	public static void setZoom(double argZoom) {
		zoom = argZoom;
	}
	

	public static float getZoom() {
		return (float) zoom;
	}

	public static void resetZoom() {
		setZoom(70);
	}

	public static void lockPerson(boolean b) {
		lockPerson = b;
	}

	public static void bedCamera() {
		moveCamera(0, 1, 0);
		rotateCamera(0, 0, 90);
	}

	public static void lockCamera(boolean b) {
		EntityPlayer player = WorldAPI.getPlayer();
		lockCamera = b;
		lockPlayerPitch = player.prevRotationPitch;
		lockPlayerYaw = player.prevRotationYaw;
	}

	public static void lockCamera(boolean lock, float yaw, float pitch) {
		lockCamera = lock;
		lockPlayerPitch = pitch;
		lockPlayerYaw = yaw;
	}

	public static boolean isLock() {
		return lockCamera;
	}

	public static void playerCamera(boolean b) {
		if (b) {
			mc.gameSettings.thirdPersonView = -1;
			lockPerson(true);
		} else
			mc.gameSettings.thirdPersonView = 0;

	}


	public static void rotateCamera(double x, double y, double z) {
		rotateX = x;
		rotateY = y;
		rotateZ = z;
	}

	public static void moveCamera(double x, double y, double z) {
		move(x, y, z);
	}

	public static void move(double x, double y, double z) {
		traX = x;
		traY = y; 
		traZ = z;
	}
	public static void moveAdd(double x, double y, double z) {
		traX += x;
		traY += y;
		traZ += z;
	}

	/**
	 * Tick 이벤트용.
	 *20/@param x를 딱 한번 수행함
	 */
	public static boolean cameraRotate(double x, double y, double z, double tick) {
		rotateX = plusminus(x, rotateX, tick);
		rotateY = plusminus(y, rotateY, tick);
		rotateZ = plusminus(z, rotateZ, tick);
		return cmRotateEquals(x, y, z);
	}
	public static boolean cameraMove(double x, double y, double z, double tick) {
		traX = plusminus(x, traX, tick);
		traY = plusminus(y, traY, tick);
		traZ = plusminus(z, traZ, tick);
		return cmTraEquals(x, y, z);
	}
	
	public static void cameraRotateTimer(double x, double y, double z) {
		TickRegister.register(new TickTask("camera-rotate", Type.SERVER, 1, true) {
			@Override
			public void run(Type type) {
				rotateX = plusminus(x, rotateX);
				rotateY = plusminus(y, rotateY);
				rotateZ = plusminus(z, rotateZ);
				absLoop = equalsCut(x, rotateX) || equalsCut(y, rotateY) || equalsCut(z, rotateZ);
			}
		});
	}
	
	public static void cameraMoveTimer(double x, double y, double z) {
		TickRegister.register(new TickTask("camera-move", Type.SERVER, 1, true) {
			@Override
			public void run(Type type) {
				traX = plusminus(x, traX);
				traY = plusminus(y, traY);
				traZ = plusminus(z, traZ);
				absLoop = equalsCut(x, traX) || equalsCut(y, traY) || equalsCut(z, traZ);
			}
		});
	}
	
	public static boolean cmTraEquals(double x, double y, double z) {
		if (Double.compare(Math.round(x), Math.round(traX)) == 0)
			if (Double.compare(Math.round(y), Math.round(traY)) == 0)
				if (Double.compare(Math.round(z), Math.round(traZ)) == 0) return true;
		return false;
	}
	public static boolean cmRotateEquals(double x, double y, double z) {
        return Double.compare(Math.round(x), Math.round(rotateX)) == 0 &&
                Double.compare(Math.round(y), Math.round(rotateY)) == 0 &&
                Double.compare(Math.round(z), Math.round(rotateZ)) == 0;
	}
	/**
	 * 특정 값이 될 때까지 1/20의 값으로 더하는 메서드
	 */
	public static double plusminus(double fir, double sec) {
		return plusminus(fir,sec,20);
	}

	/**
	 * 특정 값이 될 때까지 1/n의 값으로 더하는 메서드입
	 */
	public static double plusminus(double fir, double sec, double tick) {
		if (Math.round(fir) == Math.round(sec))
			return fir;
		if (WorldAPI.cut(fir, 1) > WorldAPI.cut(sec, 1)) {
			sec += WorldAPI.cut(1F / tick, 1);
		}
		if (WorldAPI.cut(fir, 1) < WorldAPI.cut(sec, 1)) {
			sec -= WorldAPI.cut(1F / tick, 1);
		}
		return sec;
	}

	/**
	 * 둘의 값을 비교
	 */
	public static boolean equalsCut(double fir, double sec) {
		return WorldAPI.cut(fir) != WorldAPI.cut(sec);
	}

}
