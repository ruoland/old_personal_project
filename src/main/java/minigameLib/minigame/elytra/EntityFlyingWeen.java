package minigameLib.minigame.elytra;

import minigameLib.MiniGame;
import minigameLib.api.Direction;
import minigameLib.api.EntityAPI;
import minigameLib.api.WorldAPI;
import minigameLib.effect.AbstractTick;
import minigameLib.effect.TickRegister;
import minigameLib.fakeplayer.EntityFakePlayer;
import minigameLib.fakeplayer.FakePlayerHelper;
import minigameLib.map.EntityDefaultNPC;
import minigameLib.map.TypeModel;
import minigameLib.minigame.elytra.miniween.old.EntityElytraWeenCore;
import minigameLib.minigame.elytra.miniween.old.EntityElytraWeenTNT;
import minigameLib.minigame.elytra.miniween.old.EntityElytraWeenUP;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import org.lwjgl.input.Keyboard;

public class EntityFlyingWeen extends EntityDefaultNPC {
    private final BossInfoServer bossHealth = (BossInfoServer) (new BossInfoServer(new TextComponentString("호박"),
            BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);
    private final BossInfoServer playerHealth = (BossInfoServer) (new BossInfoServer(new TextComponentString("플레이어 체력"),
            BossInfo.Color.RED, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);
    public boolean forceStageSkip, deadFalling;
    private EntityFakePlayer fakePlayer;

    public EntityFlyingWeen(World worldIn) {
        super(worldIn);
        this.setSize(7, 7);
        this.setBlockMode(Blocks.LIT_PUMPKIN);
        setModel(TypeModel.BLOCK);
        setTra(0, -4, 0);
        addScale(7);
        fakePlayer = FakePlayerHelper.fakePlayer;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(10000.0D);

    }

    private int deathTick;


    @Override
    public void onDeath(DamageSource cause) {
        //super.onDeath(cause);
    }

    @Override
    protected void onDeathUpdate() {
        //super.onDeathUpdate();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        System.out.println("공격받음" + source.damageType);
        if (getHealth() - 2 <= 0 || deadFalling) {
            deadFalling = true;
            this.setHealth(0.01F);
            return false;
        }
        return super.attackEntityFrom(source, 3);
    }

    private static boolean firstPatternEnd, secondPatternEnd, thirdPatternEnd, fourPatternEnd, fivePatternEnd;

    @Override
    public void setDead() {
        super.setDead();
        firstPatternEnd = false;
        secondPatternEnd = false;
        thirdPatternEnd = false;
        fourPatternEnd = false;
        fivePatternEnd = false;
    }

    @Override
    public void onLivingUpdate() {
        this.hurtTime = 0;
        this.hurtResistantTime = 0;
        this.maxHurtResistantTime = 0;
        this.maxHurtTime = 0;
        super.onLivingUpdate();
        this.hurtTime = 0;
        this.hurtResistantTime = 0;
        this.maxHurtResistantTime = 0;
        this.maxHurtTime = 0;
        if (WorldAPI.getPlayer() == null || FakePlayerHelper.fakePlayer == null || !MiniGame.elytra.isStart()) {//플레이어가 없는 경우
            this.setDead();
            return;
        }
        EntityPlayer player = WorldAPI.getPlayer();

        if (deadFalling) {
            deathTick++;
            if (!onGround) {
                addRotate(deathTick, deathTick, deathTick);
            } else if (deathTick >= 100) {
                if (!isDead && MiniGame.elytra.isStart()) {
                    MiniGame.elytra.end();
                }
                this.setDead();
                System.out.println("플라잉 윈 죽음");
            }
            worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX + WorldAPI.rand(7), posY + 10, posZ + WorldAPI.rand(7), 0, 0, 0);
            return;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_V))//강제로 다음 패턴으로
        {
            forceStageSkip = true;
        }
        if (!firstPatternEnd) {//윈을 날리는 패턴
            this.setPosition(EntityAPI.lookX(player, 15), fakePlayer.posY, EntityAPI.lookZ(player,15));
        }
        if (firstPatternEnd && !secondPatternEnd) {
            this.setPosition(EntityAPI.lookX(player,6), fakePlayer.posY - 15, EntityAPI.lookZ(player,6));
        }
        if (secondPatternEnd && !thirdPatternEnd)
            this.setPosition(EntityAPI.lookX(player,15), fakePlayer.posY, EntityAPI.lookZ(player,15));
        if (thirdPatternEnd)
            this.setPosition(EntityAPI.lookX(player,15), fakePlayer.posY, EntityAPI.lookZ(player,15));
        if (fourPatternEnd && !fivePatternEnd) {
            this.setPosition(EntityAPI.lookX(player,6), fakePlayer.posY - 15, EntityAPI.lookZ(player,6));
        }
    }

    public void secondPattern() {
        firstPatternEnd = true;
        System.out.println("[두번째 패턴] 시작됨");
        TickRegister.register(new AbstractTick(40, true) {
            @Override
            public boolean stopCondition() {
                return !isEntityAlive() || !MiniGame.elytra.isStart() || deadFalling;
            }

            @Override
            public void run(Type type) {
                if (absRunCount == 0)
                    absDefTick = 40;
                EntityElytraWeenUP ween = new EntityElytraWeenUP(worldObj, fakePlayer.posX, fakePlayer.posY, fakePlayer.posZ);
                ween.setPosition(posX + WorldAPI.rand(3), posY + 3, posZ + WorldAPI.rand(3));
                worldObj.spawnEntityInWorld(ween);
                if (forceStageSkip) {
                    forceStageSkip = false;
                    absLoop = false;
                    thirdPattern();
                    return;
                }
                if (absRunCount > 5 || forceStageSkip) {
                    absDefTick = 10;
                    if (absRunCount > 35 || forceStageSkip) {
                        absLoop = false;
                        forceStageSkip = false;
                        thirdPattern();
                    }
                }
            }
        });
    }

    public void thirdPattern() {
        TickRegister.register(new AbstractTick(60, true) {
            @Override
            public boolean stopCondition() {
                return !isEntityAlive() || !MiniGame.elytra.isStart() || deadFalling;
            }

            @Override
            public void run(Type type) {
                if (absRunCount == 0) {
                    secondPatternEnd = true;
                    setRotate(0, 0, 0);
                    System.out.println("[세번째 패턴] 시작됨");
                    absDefTick = 40;
                }
                System.out.println("[세번째 패턴] "+absDefTick+"틱이 지나감");
                int distance = 10;
                String index = WorldAPI.getPlayer().getHorizontalFacing().getName();
                double leftX = fakePlayer.getX(Direction.LEFT, distance, true);
                double leftZ =  fakePlayer.getZ(Direction.LEFT, distance, true);
                double rightX = fakePlayer.getX(Direction.RIGHT, distance, true);
                double rightZ = fakePlayer.getZ(Direction.RIGHT, distance, true);

                if (index.equalsIgnoreCase("NORTH") || index.equalsIgnoreCase("SOUTH")) {
                    for (int i = 0; i < absRunCount; i++) {//미니윈을 좌우에도 소환함
                        double randXZ[] = forwardRandomXZ(5);
                        EntityElytraWeenCore ween = spawnWeen(leftX + randXZ[0], leftZ + randXZ[1],
                                rightX + randXZ[0], rightZ + randXZ[1]);
                    }
                    for (int i = 0; i < absRunCount; i++) {//미니윈을 좌우에도 소환함
                        double randXZ[] = forwardRandomXZ(5);
                        EntityElytraWeenCore ween = spawnWeen(rightX + randXZ[0], rightZ + randXZ[1],
                                leftX + randXZ[0], leftZ + randXZ[1]);
                    }
                    System.out.println("[세번째 패턴] 윈이 소환됨");
                }
                if (index.equalsIgnoreCase("WEST") || index.equalsIgnoreCase("EAST")) {
                    for (int i = 0; i < absRunCount; i++) {//미니윈을 좌우에도 소환함
                        double randXZ[] = forwardRandomXZ(5);
                        EntityElytraWeenCore ween = spawnWeen(rightX + randXZ[0], rightZ + randXZ[1],
                                leftX + randXZ[0], leftZ + randXZ[1]);
                    }
                    for (int i = 0; i < absRunCount; i++) {//미니윈을 좌우에도 소환함
                        double randXZ[] = forwardRandomXZ(5);
                        EntityElytraWeenCore ween = spawnWeen(leftX + randXZ[0], leftZ + randXZ[1],
                                rightX + randXZ[0], rightZ + randXZ[1]);
                    }
                    System.out.println("[세번째 패턴] 윈이 소환됨");
                }
                if (forceStageSkip || absRunCount > 7) {
                    forceStageSkip = false;
                    absLoop = false;
                    fourPattern();
                }
            }
        });
    }

    private double[] forwardRandomXZ(int bound) {
        return new double[]{FakePlayerHelper.fakePlayer.getX(Direction.FORWARD, worldObj.rand.nextInt(bound), false), FakePlayerHelper.fakePlayer.getZ(Direction.FORWARD, worldObj.rand.nextInt(5), false)};
    }

    public void fourPattern() {
        thirdPatternEnd = true;
        TickRegister.register(new AbstractTick(80, true) {
            @Override
            public boolean stopCondition() {
                return !isEntityAlive() || !MiniGame.elytra.isStart() || deadFalling;
            }

            @Override
            public void run(Type type) {
                if(absRunCount == 0){
                    absDefTick = 40;
                    return;
                }
                System.out.println("[네번째 패턴] 40틱이 지나감");
                String index = WorldAPI.getPlayer().getHorizontalFacing().getName();
                if (index.equalsIgnoreCase("NORTH") || index.equalsIgnoreCase("SOUTH")) {
                    for (int i = 0; i < 5; i++) {
                        spawnWeenTNT(posX + WorldAPI.rand(5),fakePlayer.getZ(Direction.BACK,20, true));
                        spawnWeenTNT(posX - WorldAPI.rand(5), fakePlayer.getZ(Direction.BACK, 20, true));
                    }
                }
                if (index.equalsIgnoreCase("WEST") || index.equalsIgnoreCase("EAST")) {
                    for (int i = 0; i < 5; i++) {
                        spawnWeenTNT(fakePlayer.getX(Direction.BACK,20, true), posZ + WorldAPI.rand(5));
                        spawnWeenTNT(fakePlayer.getX(Direction.BACK,20, true), posZ - WorldAPI.rand(5));
                    }
                }
                if (absRunCount > 2 || forceStageSkip) {
                    absLoop = false;
                    forceStageSkip = false;
                    fivePattern();
                }
            }
        });
    }

    public void fivePattern() {
        fourPatternEnd = true;
        System.out.println("[다섯번째 패턴] 시작됨");

        TickRegister.register(new AbstractTick(5, true) {
            @Override
            public boolean stopCondition() {
                return !isEntityAlive() || !MiniGame.elytra.isStart() || deadFalling;
            }

            @Override
            public void run(Type type) {

                {
                    EntityElytraWeenUP ween = new EntityElytraWeenUP(worldObj, fakePlayer.posX, fakePlayer.posY, fakePlayer.posZ);
                    ween.setPosition(posX, posY + 3, posZ);
                    worldObj.spawnEntityInWorld(ween);
                    double[] vec3d = WorldAPI.getVecXZ(rotationPitch, rotationYaw + (absRunCount * 35), 15);
                    ween.setTarget(posX + vec3d[0], fakePlayer.posY, posZ + vec3d[1]);
                    ween.setFivePattern(true);
                }
                {
                    EntityElytraWeenUP ween = new EntityElytraWeenUP(worldObj, fakePlayer.posX, fakePlayer.posY, fakePlayer.posZ);
                    ween.setPosition(posX, posY + 3, posZ);
                    worldObj.spawnEntityInWorld(ween);
                    double[] vec3d = WorldAPI.getVecXZ(rotationPitch, rotationYaw + (absRunCount * 35), 15);
                    ween.setTarget(posX - vec3d[0], fakePlayer.posY, posZ - vec3d[1]);
                    ween.setFivePattern(true);
                }
                if (absRunCount > 30 || forceStageSkip) {
                    absLoop = false;
                    forceStageSkip = false;
                    sixPattern();
                }
            }
        });
    }

    public void sixPattern() {
        fivePatternEnd = true;
        System.out.println("[여섯번째 패턴] 시작됨");

        TickRegister.register(new AbstractTick(80, true) {
            @Override
            public boolean stopCondition() {
                return !isEntityAlive() || !MiniGame.elytra.isStart() || deadFalling;
            }
            @Override
            public void run(Type type) {
                if(absRunCount == 0) {
                    absDefTick = 20;
                    return;
                }
                //왼쪽 가운데 오른쪽에 호박을 보낼 건데 어느쪽이 잘 작동하는지 확인하기 위해 테스트 중
                if (absRunCount % 2 == 0) {
                    for (int i = 0; i < 3; i++) {
                        double[] vec3d = WorldAPI.getVecXZ(rotationPitch, (rotationYaw - 30) + (i * 30), 15);
                        spawnWeen(posX + vec3d[0], posZ + vec3d[1]);
                        System.out.println("[여섯번째 패턴] 앞 왼쪽 오른쪽"+i+(posX + vec3d[0])+ " - "+ (posZ+vec3d[1]));
                    }
                } else {
                    spawnWeen(posX, posZ, fakePlayer.getX(Direction.RIGHT, 15, true), fakePlayer.getZ(Direction.RIGHT,15, true));
                    spawnWeen(fakePlayer.getX(Direction.BACK, 15, true), fakePlayer.getZ(Direction.BACK, 15, true));
                    spawnWeen(posX, posZ, fakePlayer.getX(Direction.LEFT, 15, true), fakePlayer.getZ(Direction.LEFT, 15, true));
                    System.out.println("[여섯번째 패턴] 앞 왼쪽 오른쪽이 아님");

                }
                if (absRunCount > 5 || forceStageSkip) {
                    absLoop = false;
                    forceStageSkip = false;
                }
            }
        });
    }

    public EntityElytraWeenCore spawnWeen(double targetX, double targetZ) {
        return spawnWeen(posX, posZ, targetX, targetZ);
    }

    public EntityElytraWeenCore spawnWeen(double spawnX, double spawnZ, double targetX, double targetZ) {
        String index = WorldAPI.getPlayer().getHorizontalFacing().getName();
        EntityElytraWeenCore ween = new EntityElytraWeenCore(worldObj);
        if (index.equalsIgnoreCase("NORTH") || index.equalsIgnoreCase("SOUTH")) {
            ween.setPosition(spawnX, fakePlayer.posY, spawnZ);
        }
        if (index.equalsIgnoreCase("WEST") || index.equalsIgnoreCase("EAST")) {
            ween.setPosition(spawnX, fakePlayer.posY, spawnZ);
        }
        ween.setTarget(targetX, fakePlayer.posY, targetZ);
        worldObj.spawnEntityInWorld(ween);
        return ween;
    }

    public EntityElytraWeenTNT spawnWeenTNT(double targetX, double targetZ) {
        String index = WorldAPI.getPlayer().getHorizontalFacing().getName();
        EntityElytraWeenTNT ween = new EntityElytraWeenTNT(worldObj, targetX, fakePlayer.posY, targetZ);
        if (index.equalsIgnoreCase("NORTH") || index.equalsIgnoreCase("SOUTH")) {
            ween.setPosition(targetX + WorldAPI.rand(5), posY, posZ);
        }
        if (index.equalsIgnoreCase("WEST") || index.equalsIgnoreCase("EAST")) {
            ween.setPosition(posX, posY, targetZ + WorldAPI.rand(5));
        }

        worldObj.spawnEntityInWorld(ween);
        return ween;
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        this.bossHealth.setPercent(this.getHealth() / this.getMaxHealth());
        if (fakePlayer != null)
            this.playerHealth.setPercent(fakePlayer.getHealth() / fakePlayer.getMaxHealth());

    }

    public void addTrackingPlayer(EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bossHealth.addPlayer(player);
        this.playerHealth.addPlayer(player);
    }

    public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossHealth.removePlayer(player);
        this.playerHealth.removePlayer(player);
    }

	/*
	});
			TickRegister.register(new AbstractTick(5, true) {
				@Override
				public void run(Type type) {
			        ISaveFormat isaveformat = Minecraft.getMinecraft().getSaveLoader();
			        try {
						for(WorldSummary summary :isaveformat.getSaveList()) {
							if(summary.getDisplayName().equals("루프리")) {
						        net.minecraftforge.fml.client.FMLClientHandler.instance().tryLoadExistingWorld(null, summary);
								NPCEffect.getHelper().addChat(000, "루프리 세이브 파일이 감지됐어. 바로 루프리 맵을 불러올까?");
								absLoop = false;
							}
							
						}
					} catch (AnvilConverterException e) {
						e.printStackTrace();
					}
				}
			});
	 */

}
