package ruo.minigame.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import net.minecraftforge.fml.relauncher.Side;
import ruo.cmplus.CMManager;
import ruo.cmplus.camera.Camera;
import ruo.cmplus.util.Sky;
import ruo.minigame.api.RenderAPI;
import ruo.minigame.api.WorldAPI;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class CMEffect {
	static float yro = -20;
	static float zro = 90;
	static float ymo = 1.4F;

	public static CMEffect getEffect() {
		return new CMEffect();
	}
	public void setZoom(double argZoom, double tick) {
		TickRegister.register(new AbstractTick("zoom", Type.SERVER, 1, true) {
			@Override
			public void run(Type type) {
				Camera.getCamera().zoom = Camera.getCamera().plusminus(argZoom, Camera.getCamera().zoom, tick);
				absLoop = Camera.getCamera().moveLoop(argZoom, Camera.getCamera().zoom);
			}
		});
	}
	public void setZoom(double argZoom, double tick, AbstractTick abs) {
		TickRegister.register(new AbstractTick("zoom", Type.SERVER, 1, true) {
			@Override
			public void run(Type type) {
				Camera.getCamera().zoom = Camera.getCamera().plusminus(argZoom, Camera.getCamera().zoom, tick);
				absLoop = Camera.getCamera().moveLoop(argZoom, Camera.getCamera().zoom);
				if(!absLoop)
					abs.run(type);
			}
		});
	}
	
	/**
	 * 화면의 밝기를 천천히 낮춤
	 */
	public void gammaDown(AbstractTick run) {
		float gamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
		TickRegister.register(new AbstractTick(5, true) {
			
			@Override
			public void run(Type type) {
				WorldAPI.command("/gamma sub 0.01");
				if(gamma <= 0) {
					absLoop = false;
					run.run(type);
				}
			}
		});
	}
	
	
	/**
	 * 안개를 천천히 가깝게 만들음
	 */
	public void fog(double maxDistance, AbstractTick run) {
		Sky.fogDistance(-1);

		TickRegister.register(new AbstractTick(1, true) {
			@Override
			public void run(Type type) {
				Sky.fogDistance(Sky.getFogDistance()-0.5F);
				if(Sky.getFogDistance() <= maxDistance) {
					absLoop = false;
					run.run(type);
				}
			}
		});
	}
	/*
	 * TODO 반드시 클래스를 고쳐야함 이 클래스는 한번에 하나만 처리가능함 
	 * TODO 코드를 두번 실행하면 비정상적으로 작동할 거임.
	 */
	public void drawImage(final String texture, final int x, final int y, final int width, final int height,
			int tick) {
		final Minecraft mc = Minecraft.getMinecraft();
		TickRegister.register(new AbstractTick(null, 1, true) {
			float alpha = 1.0F;
			@Override
			public void run(Type type) {
				alpha--;
				if (alpha <= 0) {
					absLoop = false;
				}
				if (tickEvent.side == Side.CLIENT) {
					RenderAPI.drawTexture(texture, alpha, x, y,width,height);
				}
			}
		});
	}

	public void drawImage(final String texture, int tick, final AbstractTick abs) {
		TickRegister.register(new AbstractTick(null, 1, true) {
			float alpha = 1.0F;

			@Override
			public void run(Type type) {
				if (alpha <= 0) {
					absLoop = false;
					if (abs != null) {
						abs.run();
					}
				}
				RenderAPI.drawTexture(texture, alpha, 0, 0, Minecraft.getMinecraft().displayWidth,
						Minecraft.getMinecraft().displayHeight);
			}
		});
		
	}

	boolean darkCancel;
	
	public void darkCancel() {
		darkCancel = true;
	}
	/**
	 * 화면을 천천히 어둡게 만듦
	 * 화면을 완전히 어둡게 하면 바로 어두운 화면이 사라지니 조심
	 */
	public void darkScreen2(final AbstractTick absTic) {
		TickRegister.register(new AbstractTick("DARKSCREEN", Type.RENDER, 1, true) {
			float alpha = 0.0F;

			@Override
			public void run(Type type) {
				if (alpha >= 1 || darkCancel) {
					absLoop = false;
					if (absTic != null) {
						absTic.run();
					}
					darkCancel = false;
				}
				RenderAPI.drawTexture("commandplus:textures/gui/darkscreen.png", alpha, 0, 0,
						Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
			}
		});

		
	}

	/**
	 * 검은 텍스쳐의 알파값이 maxAlpha가 될 때까지 더함
	 */
	public void darkScreen2(int tick, final float maxAlpha, final AbstractTick absTick) {
		darkScreen(tick, absTick, false, maxAlpha);
	}
	
	/**
	 * 검은 텍스쳐의 알파값이 0이 될 때까지 뻄
	 * 
	 */
	public void darkScreen(int tick, final AbstractTick absTick) {
		darkScreen(tick, absTick, true, 1);
	}
	/**
	 * isPlus를 true로 설정하면 화면이 천천히 어두워지고(alpha가 maxAlpha가 될 때까지 더함)
	 * false로 하면 완전히 까맣게 변하고 천천히 화면이 밝아짐
	 */
	private void darkScreen(int argTick, final AbstractTick abs, boolean isPlus, float maxAlpha) {
		TickRegister.register(new AbstractTick("DARKSCREEN", null, 1, true) {
			float currentAlpha = isPlus ? 0F : 1F;
			float playerTick = 0;
			@Override
			public void run(Type type) {
				if(type == Type.SERVER) {
					if((((isPlus && currentAlpha >= maxAlpha) || (!isPlus && currentAlpha <= maxAlpha))) && playerTick != argTick)
						playerTick ++;
					if(isPlus && currentAlpha < maxAlpha)
						currentAlpha += 0.005F;
					else if(!isPlus && currentAlpha > maxAlpha && playerTick>=argTick)
						currentAlpha -= 0.005F;
				}
				if(type == Type.RENDER)
					RenderAPI.drawTexture("commandplus:textures/gui/darkscreen.png", currentAlpha, 0, 0,
							Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
				if ((isPlus && currentAlpha >= maxAlpha) || (!isPlus && currentAlpha <= 0) || darkCancel) {
					absLoop = !(isPlus ? playerTick>=argTick : currentAlpha <= 0);
					darkCancel = false;
					if (abs != null && !absLoop)
						abs.run();
				}
			}
		});
	}

	public void setFlowMode(boolean mode) {
		if (mode)
			Sky.enableBlockLayer();
		else
			Sky.disableBlockLayer();
	}

	static AbstractTick tick;

	public static void setCameraEarthquake(final int randomi) {
		final Random random = new Random();

		if (tick != null && randomi == 0) {
			tick.stopTick();
			TickRegister.remove(tick);
			Camera.getCamera().reset();
			return;
		}
		if (tick != null)
			TickRegister.remove(tick);
		tick = new AbstractTick(Type.CLIENT, 1, true) {
			@Override
			public void run(Type type) {
				if (absRunCount < 500) {
					float x;
					float y;
					float z;
					if (random.nextBoolean())
						x = Float.valueOf("0.0" + random.nextInt(randomi + 1));
					else
						x = Float.valueOf("-0.0" + random.nextInt(randomi + 1));
					if (random.nextBoolean())
						y = Float.valueOf("0.000" + random.nextInt(randomi + 1));
					else
						y = Float.valueOf("-0.000" + random.nextInt(randomi + 1));
					if (random.nextBoolean())
						z = Float.valueOf("0.00" + random.nextInt(randomi + 1));
					else
						z = Float.valueOf("-0.00" + random.nextInt(randomi + 1));
					Camera.getCamera().moveCamera(x, y, z);
					Camera.getCamera().moveCamera(x, y, z);
				} else {
					this.absLoop = false;
					Camera.getCamera().reset();
					return;
				}
			}
		};
		TickRegister.register(tick);
	}

	/**
	 * 점점 낮아짐
	 * 
	 * @param randomi
	 */
	public static void setCameraEarthquake2(final int randomi, int maxcount) {
		final Random random = new Random();

		if (tick != null && randomi == 0) {
			tick.stopTick();
			TickRegister.remove(tick);
			Camera.getCamera().reset();
			return;
		}
		if (tick != null)
			TickRegister.remove(tick);
		tick = new AbstractTick(Type.CLIENT, 1, true) {
			@Override
			public void run(Type type) {
				if (absRunCount < maxcount) {
					float x;
					float y;
					float z;
					if (random.nextBoolean())
						x = Float.valueOf("0.0" + random.nextInt(randomi + 1)) - Float.valueOf("0.00" + absRunCount);
					else
						x = Float.valueOf("-0.0" + random.nextInt(randomi + 1)) - Float.valueOf("0.000" + absRunCount);
					if (random.nextBoolean())
						y = Float.valueOf("0.000" + random.nextInt(randomi + 1)) - Float.valueOf("0.0000" + absRunCount);
					else
						y = Float.valueOf("-0.000" + random.nextInt(randomi + 1)) - Float.valueOf("0.0000" + absRunCount);
					if (random.nextBoolean())
						z = Float.valueOf("0.00" + random.nextInt(randomi + 1)) - Float.valueOf("0.000" + absRunCount);
					else
						z = Float.valueOf("-0.00" + random.nextInt(randomi + 1)) - Float.valueOf("0.000" + absRunCount);
					Camera.getCamera().moveCamera(x, y, z);
					Camera.getCamera().moveCamera(x, y, z);
				} else {
					this.absLoop = false;
					Camera.getCamera().reset();
					return;
				}
			}
		};
		TickRegister.register(tick);
	}

	public static void setCameraFall() {
		Camera.getCamera().lockCamera(true, 0, 0);
		Camera.getCamera().rotateCamera(0, -20, 90);
		Camera.getCamera().moveCamera(0, 1.4, 0);
		yro = -20;
		zro = 90;
		ymo = 1.4F;
	}

	public static void setCameraWakeUp() {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (ymo >= 0) {
					ymo -= 0.1;
				}
				if (zro > 0) {
					zro -= 3.5;
				}
				if (yro < 0) {
					yro += 2.5;
				}
				if (ymo <= 0 && zro <= 0 && yro >= 0) {
					cancel();
					Camera.getCamera().reset();
				}
				Camera.getCamera().moveCamera(0, ymo, 0);
				Camera.getCamera().rotateCamera(0, yro, zro);
			}
		};
		timer.schedule(task, 100, 30);
	}
	
	/*
	 * 루프리 리뉴얼 초기버전에서 쓰인 연출
	 */
	public void sleepPlayer() {
		Minecraft.getMinecraft().gameSettings.thirdPersonView = 2;
		CMManager.setSleep(true, EnumFacing.SOUTH);
		Camera.getCamera().lockCamera(true);
		Camera.getCamera().setYP(true);
		Camera.getCamera().playerCamera(true);
		Camera.getCamera().rotateCamera(50, 160, 0);
		Camera.getCamera().moveCamera(0, 0, 0);
		CMManager.setMoveLock(false);
	}
}
