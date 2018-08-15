package ruo.cmplus.camera;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.TickRegister;

public class Camera {
	public double rotateX, rotateY, rotateZ, zoom = 70;
	public double traX, traY, traZ;
	public float yaw, pitch;
	public boolean lockCamera, lockPerson, yp;
	public static boolean isDebug = false, freeCam = false;
	private Minecraft mc = Minecraft.getMinecraft();
	private EntityRenderer r = mc.entityRenderer;
	private static Camera camera = new Camera();

	public void reset() {
		setZoom(70);
		rotateCamera(0, 0, 0);
		moveCamera(0, 0, 0);
		playerCamera(false);
		lockCamera(false, pitch, yaw);
		setYP(false);
	}

	public void setYP(boolean yp) {
		EntityPlayer player = WorldAPI.getPlayer();
		this.yp = yp;
		pitch = 0;
		yaw = 0;
	}

	public static Camera getCamera() {
		return camera;
	}

	public void setZoom(double argZoom) {
		zoom = argZoom;
	}
	

	public float getZoom() {
		return (float) zoom;
	}

	public void resetZoom() {
		setZoom(70);
	}

	public void lockPerson(boolean b) {
		lockPerson = b;
	}

	public void bedCamera() {
		EntityPlayer entity = WorldAPI.getPlayer();
		moveCamera(0, 1, 0);
		rotateCamera(0, 0, 90);
	}

	public void lockCamera(boolean b) {
		EntityPlayer player = WorldAPI.getPlayer();
		lockCamera = b;
		pitch = player.prevRotationPitch;
		yaw = player.prevRotationYaw;
	}

	public void lockCamera(boolean lock, float yaw, float pitch) {
		lockCamera = lock;
		this.pitch = pitch;
		this.yaw = yaw;
	}

	public boolean isLock() {
		return lockCamera;
	}

	public void playerCamera(boolean b) {
		if (b) {
			mc.gameSettings.thirdPersonView = -1;
			lockPerson(true);
		} else
			mc.gameSettings.thirdPersonView = 0;

	}


	public void rotateCamera(double x, double y, double z) {
		rotateX = x;
		rotateY = y;
		rotateZ = z;
	}

	public void moveCamera(double x, double y, double z) {
		move(x, y, z);
	}

	public void move(double x, double y, double z) {
		traX = x;
		traY = y; 
		traZ = z;
	}
	public void moveAdd(double x, double y, double z) {
		traX += x;
		traY += y;
		traZ += z;
	}

	/**
	 * Tick 이벤트용.
	 *20/@param x를 딱 한번 수행함
	 */
	public boolean cameraRotate(double x, double y, double z, double tick) {
		rotateX = plusminus(x, rotateX, tick);
		rotateY = plusminus(y, rotateY, tick);
		rotateZ = plusminus(z, rotateZ, tick);
		return cmRotateEquals(x, y, z);
	}
	public boolean cameraMove(double x, double y, double z, double tick) {
		traX = plusminus(x, traX, tick);
		traY = plusminus(y, traY, tick);
		traZ = plusminus(z, traZ, tick);
		return cmTraEquals(x, y, z);
	}
	
	public void cameraRotateTimer(double x, double y, double z) {
		TickRegister.register(new AbstractTick("camera-rotate", Type.SERVER, 1, true) {
			@Override
			public void run(Type type) {
				rotateX = plusminus(x, rotateX);
				rotateY = plusminus(y, rotateY);
				rotateZ = plusminus(z, rotateZ);
				absLoop = equalsCut(x, rotateX) || equalsCut(y, rotateY) || equalsCut(z, rotateZ);
			}
		});
	}
	
	public void cameraMoveTimer(double x, double y, double z) {
		TickRegister.register(new AbstractTick("camera-move", Type.SERVER, 1, true) {
			@Override
			public void run(Type type) {
				traX = plusminus(x, traX);
				traY = plusminus(y, traY);
				traZ = plusminus(z, traZ);
				absLoop = equalsCut(x, traX) || equalsCut(y, traY) || equalsCut(z, traZ);
			}
		});
	}
	
	public boolean cmTraEquals(double x, double y, double z) {
        return Double.compare(Math.round(x), Math.round(traX)) == 0 &&
                Double.compare(Math.round(y), Math.round(traY)) == 0 &&
                Double.compare(Math.round(z), Math.round(traZ)) == 0;
	}
	public boolean cmRotateEquals(double x, double y, double z) {
        return Double.compare(Math.round(x), Math.round(rotateX)) == 0 &&
                Double.compare(Math.round(y), Math.round(rotateY)) == 0 &&
                Double.compare(Math.round(z), Math.round(rotateZ)) == 0;
	}
	/**
	 * 특정 값이 될 때까지 1/20의 값으로 더하는 메서드
	 */
	public double plusminus(double fir, double sec) {
		return plusminus(fir,sec,20);
	}

	/**
	 * 특정 값이 될 때까지 1/n의 값으로 더하는 메서드입
	 */
	public double plusminus(double fir, double sec, double tick) {
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
	public boolean equalsCut(double fir, double sec) {
		return WorldAPI.cut(fir) != WorldAPI.cut(sec);
	}

}
