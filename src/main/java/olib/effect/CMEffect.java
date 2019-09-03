package olib.effect;

import cmplus.CMManager;
import cmplus.camera.Camera;
import cmplus.util.Sky;
import olib.api.DrawTexture;
import olib.api.RenderAPI;
import olib.api.WorldAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import net.minecraftforge.fml.relauncher.Side;

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

    public void setZoom(double zoom, double tick) {
        TickRegister.register(new AbstractTick("zoom", Type.SERVER, 1, true) {
            @Override
            public void run(Type type) {
                Camera.zoom = Camera.plusminus(zoom, Camera.zoom, tick);
                absLoop = Camera.equalsCut(zoom, Camera.zoom);
            }
        });
    }

    public void setZoom(double argZoom, double tick, AbstractTick abs) {
        TickRegister.register(new AbstractTick("zoom", Type.SERVER, 1, true) {
            @Override
            public void run(Type type) {
                Camera.zoom = Camera.plusminus(argZoom, Camera.zoom, tick);
                absLoop = Camera.equalsCut(argZoom, Camera.zoom);
                if (!absLoop)
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
                if (gamma <= 0) {
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
                Sky.fogDistance(Sky.getFogDistance() - 0.5F);
                if (Sky.getFogDistance() <= maxDistance) {
                    absLoop = false;
                    run.run(type);
                }
            }
        });
    }

    public void drawImage(final String texture, final int x, final int y, final int width, final int height, int tick) {
        TickRegister.register(new AbstractTick(null, 1, true) {
            float alpha = 1.0F;

            @Override
            public void run(Type type) {
                if(absRunCount > tick) {
                    alpha--;
                    if (alpha <= 0) {
                        absLoop = false;
                    }
                }
                if (tickEvent.side == Side.CLIENT) {
                    RenderAPI.drawTexture(new DrawTexture.Builder().setTexture(texture).setAlpha(alpha).setXY(x,y).setSize(width,height).build());
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

                RenderAPI.drawTexture(new DrawTexture.Builder().setTexture(texture).setAlpha(alpha).setXY(0,0).setSize(Minecraft.getMinecraft().displayWidth,
                        Minecraft.getMinecraft().displayHeight).build());
            }
        });

    }


    public void darkCancel() {
        TickRegister.remove("DARKSCREEN");
    }

    /**
     * 화면을 천천히 어둡게 만듦
     * 화면을 완전히 어둡게 하면 바로 어두운 화면이 사라짐
     */
    public void darkScreen2(final AbstractTick absTic) {
        TickRegister.register(new AbstractTick("DARKSCREEN", Type.RENDER, 1, true) {
            float alpha = 0.0F;

            @Override
            public void run(Type type) {
                if (alpha >= 1) {
                    absLoop = false;
                    if (absTic != null) {
                        absTic.run();
                    }
                }
                RenderAPI.drawTexture(new DrawTexture.Builder().setTexture("commandplus:textures/gui/darkscreen.png").setAlpha(alpha).setXY(0,0).setSize(Minecraft.getMinecraft().displayWidth,
                        Minecraft.getMinecraft().displayHeight).build());
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
                if (type == Type.SERVER) {
                    if ((((isPlus && currentAlpha >= maxAlpha) || (!isPlus && currentAlpha <= maxAlpha))) && playerTick != argTick)
                        playerTick++;
                    if (isPlus && currentAlpha < maxAlpha)
                        currentAlpha += 0.005F;
                    else if (!isPlus && currentAlpha > maxAlpha && playerTick >= argTick)
                        currentAlpha -= 0.005F;
                }
                if (type == Type.RENDER)
                    RenderAPI.drawTexture(new DrawTexture.Builder().setTexture("commandplus:textures/gui/darkscreen.png").setAlpha(currentAlpha).setXY(0,0).setSize(Minecraft.getMinecraft().displayWidth,
                            Minecraft.getMinecraft().displayHeight).build());

                if ((isPlus && currentAlpha >= maxAlpha) || (!isPlus && currentAlpha <= 0)) {
                    absLoop = !(isPlus ? playerTick >= argTick : currentAlpha <= 0);
                    if (abs != null && !absLoop)
                        abs.run();
                }
            }
        });
    }


    public static void setCameraEarthquake(final int randomi) {
        final Random random = new Random();
        boolean isTickRun = TickRegister.isAbsTickRun("Earth");
        if (isTickRun && randomi == 0) {
            TickRegister.remove("Earth");
            Camera.reset();
            return;
        }
        TickRegister.register(new AbstractTick("Earth",Type.CLIENT, 1, true) {
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
                    Camera.moveCamera(x, y, z);
                    Camera.moveCamera(x, y, z);
                } else {
                    this.absLoop = false;
                    Camera.reset();
                }
            }
        });
    }

    /**
     * 점점 낮아짐
     */
    public static void setCameraEarthquake2(final int randomi, int maxcount) {
        if (TickRegister.isAbsTickRun("EarthQuake") && randomi == 0) {
            TickRegister.remove(TickRegister.getAbsTick("EarthQuake"));
            Camera.reset();
            return;
        }
        TickRegister.register(new AbstractTick("EarthQuake",Type.CLIENT, 1, true) {
            @Override
            public void run(Type type) {
                if (absRunCount < maxcount) {
                    float x = (WorldAPI.rand(randomi + 1) / 100) - (absRunCount / 1000);
                    float y = (WorldAPI.rand(randomi + 1) / 1000) - (absRunCount / 10000);
                    float z = (WorldAPI.rand(randomi + 1) / 100) - (absRunCount / 1000);
                    Camera.moveCamera(x, y, z);
                    System.out.println("카메라 흔드는 중");
                } else {
                    this.absLoop = false;
                    Camera.reset();
                }
            }
        });
    }

    public static void setCameraFall() {
        Camera.lockCamera(true, 0, 0);
        Camera.rotateCamera(0, -20, 90);
        Camera.moveCamera(0, 1.4, 0);
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
                    Camera.reset();
                }
                Camera.moveCamera(0, ymo, 0);
                Camera.rotateCamera(0, yro, zro);
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
        Camera.lockCamera(true);
        Camera.setYP(true);
        Camera.playerCamera(true);
        Camera.rotateCamera(50, 160, 0);
        Camera.moveCamera(0, 0, 0);
        CMManager.setMoveLock(false);
    }
}
