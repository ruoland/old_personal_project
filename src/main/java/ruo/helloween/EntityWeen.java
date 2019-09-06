package ruo.helloween;

import cmplus.util.Sky;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import olib.api.*;
import olib.effect.TickTask;
import olib.effect.CMEffect;
import olib.effect.TickRegister;
import olib.map.EntityDefaultNPC;
import olib.map.TypeModel;
import org.lwjgl.input.Keyboard;
import ruo.helloween.miniween.EntityAttackMiniWeen;
import ruo.helloween.miniween.EntityDefenceMiniWeen;
import ruo.helloween.miniween.EntityMiniWeen;
import ruo.helloween.miniween.EntityNightMiniWeen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class EntityWeen extends EntityDefaultNPC {
    public static ItemStack shield_block = new ItemStack(Blocks.GLASS);
    public static long startTime, endTime;
    private static final DataParameter<Boolean> IS_JUMP = EntityDataManager.createKey(EntityWeen.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_ROTATE = EntityDataManager.createKey(EntityWeen.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> COMPLETE_Y = EntityDataManager.createKey(EntityWeen.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> LOOK_PLAYER = EntityDataManager.createKey(EntityWeen.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Float> PATTERN = EntityDataManager.createKey(EntityWeen.class, DataSerializers.FLOAT);
    //1 = 점프 후 추락, 2 = 미니윈, 2.5 = 회전하면서 미니윈 던짐, 3 = 빅윈, 4 = 밤모드 텔레포트 상태, 5 = 밤모드 다음 빅윈 소환후 계단, 6 = 마지막 점프

    private final BossInfoServer bossHealth = (BossInfoServer) (new BossInfoServer(new TextComponentString("윈의 체력"),
            BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);

    public EntityWeen(World worldIn) {
        super(worldIn);
        this.setSize(13, 13);
        this.setModel(TypeModel.BLOCK);
        this.addScale(12);
        this.setTra(0, -5F, 0);
        setCollision(true);
        setRotate(0, 0, 180);
        this.setBlock(Blocks.LIT_PUMPKIN);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(0);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(270.0D);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getEntity() instanceof EntityBigWeen) {
            System.out.println("빅윈에게 공격받음");
            EntityBigWeen ween = (EntityBigWeen) source.getEntity();
            if (ween.isFivePattern) {
                TickRegister.register(new TickTask(100, false) {
                    public void run(Type type) {
                        jumpStartSixWeen();
                    }

                    @Override
                    public boolean stopCondition() {
                        return isDead;
                    }
                });
            }
            setSturn(100);
            System.out.println("빅윈이 폭파해 스턴됨");
            return super.attackEntityFrom(source, 15);
        }
        if (!isSturn()) {
            System.out.println("스턴이 아님");
            return false;
        }
        System.out.println(source+"에게 공격받음");
        return super.attackEntityFrom(source, 5);
    }


    public void onLivingUpdate() {
        if (isServerWorld()) {
            if (!isSturn() && pattern() == 1) {
                //if (!isJumpWeen())
                    //jumpStartWeen();
               // if (isJumpWeen() && isServerWorld() && isRotateComplete() && onGround && isCompleteY())
                    //fallWeen();//하늘에 올라갔다가 땅에 떨어진 경우
            }
            if (pattern() == 6)
                jumpWeenUpgrade();
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_V)) {
            forceSkip = true;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
            patternHold = !patternHold;
            System.out.println("패턴 홀드 " + patternHold);
        }
        super.onLivingUpdate();
    }

    public EntityAttackMiniWeen summonAttackWeen(Vec3d pos) {
        return summonAttackWeen(pos.xCoord, pos.yCoord, pos.zCoord);
    }

    public EntityAttackMiniWeen summonAttackWeen(double posX, double posY, double posZ) {
        EntityAttackMiniWeen blockWeen = new EntityAttackMiniWeen(worldObj);
        blockWeen.setPosition(posX, posY, posZ);
        worldObj.spawnEntityInWorld(blockWeen);
        return blockWeen;
    }

    /**
     * 첫번째 패턴
     */
    public void jumpStartWeen() {
        EntityWeen ween = this;
        System.out.println("[첫패턴]점프를 위해 스턴을 해제함");
        setSturn(false);
        getDataManager().set(IS_JUMP, true);
        WeenEffect.entityRotateX(this, -90, new TickTask() {
            @Override
            public void run(Type type) {
                System.out.println("[첫패턴]회전을 끝냄");

                dataManager.set(IS_ROTATE, true);
                WeenEffect.entityJump(ween, 60, new TickTask() {
                    @Override
                    public void run(Type type) {
                        System.out.println("[첫패턴]목적 Y에 도달함. 이제 추락함");
                        setRotate(90, 0, 0);
                        dataManager.set(COMPLETE_Y, true);
                    }
                });
            }
        });
    }


    private ArrayList<EntityAttackMiniWeen> blockList = new ArrayList<>();
    private final int firstEndHP = 240, secondEndHP = 160, fourEndHP = 110;
    private boolean forceSkip, patternHold;

    public void fallWeen() {
        for (EntityLivingBase base : worldObj.getEntitiesWithinAABB(EntityLivingBase.class, //호박에 깔린 몬스터는 10000의 데미지를 줌
                getEntityBoundingBox())) {
            if (base == this || base instanceof EntityMiniWeen) {
                continue;
            }
            base.attackEntityFrom(DamageSource.inWall, 10000);
        }

        TickRegister.register(new TickTask(Type.SERVER, 3, true) {//미니윈을 땅에서 소환하는 메서드
            @Override
            public boolean stopCondition() {
                return isDead;
            }
            @Override
            public void run(Type type) {
                if (blockList.size() < 50) {
                    if (absRunCount == 10)
                        absLoop = false;
                    for (int i = 0; i < 2; i++) {
                        EntityAttackMiniWeen blockWeen = new EntityAttackMiniWeen(worldObj);
                        blockWeen.isFly = false;
                        blockWeen.setPosition(posX + (rand.nextInt(32) - 16), posY+10, posZ + (rand.nextInt(32) - 16));
                        worldObj.spawnEntityInWorld(blockWeen);
                        blockWeen.addRotate(rand.nextInt(90), rand.nextInt(90), rand.nextInt(90));
                        blockWeen.addVelocity((rand.nextInt(3) - 3), (rand.nextInt(3) - 3), (rand.nextInt(3) - 3));
                        blockWeen.setPattern(1);
                        blockList.add(blockWeen);
                        System.out.println("소환됨" + blockWeen);
                    }
                }
            }
        });

        WeenEffect.entityKnockBackDamage(this, 30, 5, 30, 3.5F, DamageSource.fall, 5);
        CMEffect.setCameraEarthquake2(15, 60);

        this.attackEntityFrom(DamageSource.fall, 5);

        if (getHealth() < firstEndHP) {
            System.out.println("[첫패턴]첫번째 패턴 완료함+현재 스턴 걸림");
            setPattern(2);
            twoPattern();
        }
        setSturn(130);
        dataManager.set(IS_JUMP, false);
        dataManager.set(COMPLETE_Y, false);
        dataManager.set(IS_ROTATE, false);
    }



    public void twoPattern() {
        TickRegister.register(new TickTask(130, false) {
            @Override
            public boolean stopCondition() {
                return isDead;
            }

            @Override
            public void run(Type type) {
                setRotate(0, 0, 180);
                System.out.println(absDefTick + "틱" + absRunCount + " - " + blockList.size());
                TickRegister.register(new TickTask(15, true) {
                    @Override
                    public void run(Type type) {
                        if (absRunCount >= blockList.size() - 1) {//블럭을 모두 던지면 다음 패턴으로
                            blockList.clear();
                            blockList = null;
                            System.out.println("2.5 패턴으로 넘어감");
                            twoPattern2();
                            stopTick();
                            return;
                        }
                        EntityAttackMiniWeen attackMiniWeen = blockList.get(absRunCount);
                        if (attackMiniWeen.isDead)//만약 미니윈이 시간이 지나 죽거나 폭발해서 사라진 경우 다음으로 넘김
                            return;
                        attackMiniWeen.startTwoPattern();
                    }
                });
            }
        });
    }

    public void twoPattern2() {
        setPattern(2.5F);
        System.out.println("2.5패턴 돌입");
        PosHelper posHelper = new PosHelper(this);
        TickRegister.register(new TickTask(250, false) {

            @Override
            public boolean stopCondition() {
                return isDead;
            }

            @Override
            public void run(Type type) {
                TickRegister.register(new TickTask(30, true) {

                    @Override
                    public boolean stopCondition() {
                        return isDead;
                    }

                    @Override
                    public void run(Type type) {

                        EntityAttackMiniWeen attackMiniWeen = summonAttackWeen(posHelper.getXZ(Direction.FORWARD, 8, true).add(posHelper.getXZ(Direction.RIGHT, WorldAPI.rand(3), false)).addVector(0, rand.nextInt(10), 0));
                        attackMiniWeen.setTarget(WorldAPI.getPlayer().posX + WorldAPI.rand(1), WorldAPI.getPlayer().posY+ WorldAPI.getPlayer().getEyeHeight(), WorldAPI.getPlayer().posZ + WorldAPI.rand(1));
                        attackMiniWeen.setExplosionStrength(1F);
                        attackMiniWeen.setTargetExplosion(true);
                        attackMiniWeen.setPattern(2.5F);

                        if (absRunCount >= 5 && !patternHold && (forceSkip || (getHealth() < secondEndHP))) {
                            absLoop = false;
                            threePattern();
                        }
                    }
                });
            }
        });


    }

    public void threePattern() {
        System.out.println("[두번째 패턴]체력이 " + secondEndHP + " 넘어 다음 패턴으로 넘어감");
        TickRegister.register(new TickTask(200, false) {

            @Override
            public boolean stopCondition() {
                return isDead;
            }

            @Override
            public void run(Type type) {
                setPattern(3);
                summonBigWeen();
            }
        });
    }

    /**
     * 세번째 패턴
     */
    public void summonBigWeen() {
        EntityBigWeen bigween = new EntityBigWeen(worldObj);
        bigween.ween = this;
        bigween.setPosition(posX, posY + 20, posZ);
        bigween.isFivePattern = false;
        worldObj.spawnEntityInWorld(bigween);
        TickRegister.register(new TickTask(40, true) {
            @Override
            public boolean stopCondition() {
                return isDead || bigween.isDead;
            }

            @Override
            public void run(Type type) {
                EntityPlayer player = WorldAPI.getPlayer();
                if(bigween.getHealth() <= 30) {
                    for (int i = 0; i < 8; i++) {
                        Vec3d v = player.getLookVec();
                        EntityDefenceMiniWeen miniween = new EntityDefenceMiniWeen(worldObj, bigween.ween);
                        miniween.setPosition(posX + WorldAPI.rand(20), posY + 10 + rand.nextInt(4),
                                posZ + WorldAPI.rand(20));
                        worldObj.spawnEntityInWorld(miniween);
                        miniween.setPattern(3);
                        miniween.setTarget(WorldAPI.x() + v.xCoord * 1.5, WorldAPI.y() + player.eyeHeight + v.yCoord * 1.5, WorldAPI.z() + v.zCoord * 1.5);
                        TickRegister.register(new TickTask(300, false) {
                            @Override
                            public void run(Type type) {
                                miniween.setTarget(bigween.posX, bigween.posY, bigween.posZ);
                                miniween.goWeen = true;
                            }
                        });
                    }
                }

            }
        });
    }

    public void theNight() {
        EntityWeen ween = this;
        setPattern(4);
        worldObj.setWorldTime(13600);
        WeenEffect.fog(7, new TickTask() {
            @Override
            public void run(Type type) {
                TickRegister.register(new TickTask(Type.SERVER, 40, true) {
                    @Override
                    public boolean stopCondition() {
                        return isDead;
                    }

                    @Override
                    public void run(Type type) {
                        if (absRunCount % 4 == 0) {
                            setPosition(WorldAPI.x() + WorldAPI.minRand(10, 20), posY,
                                    WorldAPI.z() + WorldAPI.minRand(10, 20));
                            worldObj.playSound((EntityPlayer) null, posX, posY, posZ,
                                    SoundEvents.ENTITY_ENDERMEN_TELEPORT, getSoundCategory(), 2.0F, 1.0F);
                            playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 2.0F, 1.0F);
                        }
                        if (absRunCount % 4 != 0) {
                            EntityMiniWeen miniween = new EntityAttackMiniWeen(worldObj);
                            miniween.setPosition(posX, posY + 3, posZ);
                            worldObj.spawnEntityInWorld(miniween);
                            miniween.setTarget(WorldAPI.x(), WorldAPI.y() + WorldAPI.getPlayer().eyeHeight,
                                    WorldAPI.z());
                            System.out.println("미니윈 소환");
                            miniween.setPattern(4);
                        }
                        if (!patternHold && (getHealth() < fourEndHP && absRunCount > 7 || forceSkip)) {
                            absLoop = false;
                            setPattern(5);
                            for (long i = worldObj.getWorldTime(); i <= 20000; i += 8) {
                                worldObj.setWorldTime(i);
                                if (i < 20000) {
                                    System.out.println("[네번째 패턴]체력이 " + fourEndHP + " 넘어 빅윈 패턴으로 넘어감");
                                    fivePattern();
                                    break;
                                }
                            }
                        }
                    }
                });
                TickRegister.register(new TickTask(Type.RENDER, 1, false) {
                    @Override
                    public void run(Type type) {
                        // Sky.starGenerate(10000);
                        Sky.setMoonTexture(RenderAPI.getBlockTexture(Blocks.PUMPKIN).toString());
                    }
                });
            }
        });
    }

    public void fivePattern() {
        float f1 = 5.0F + ((Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16) - 5.0F)
                * (1.0F - (float) 0 / 20.0F);
        TickRegister.register(new TickTask(Type.RENDER, 1, true) {
            @Override
            public boolean stopCondition() {
                return isDead;
            }

            @Override
            public void run(Type type) {
                Sky.fogDistance(absRunCount);
                if (absRunCount == f1) {
                    absLoop = false;
                    TickRegister.register(new TickTask(Type.SERVER, 20, true) {
                        @Override
                        public boolean stopCondition() {
                            return isDead;
                        }

                        @Override
                        public void run(Type type) {
                            System.out.println("[다섯번째 패턴]미니윈과 빅윈 소환함");
                            if (absRunCount == 0)
                                summonPlayerWeenAndBigWeen();
                            if (absRunCount == 10) {
                                absLoop = false;
                            }
                        }
                    });
                }
            }
        });
    }


    public void summonPlayerWeenAndBigWeen() {
        EntityBigWeen bigween = new EntityBigWeen(worldObj);
        bigween.ween = this;
        bigween.setPosition(posX, posY + 20, posZ);
        bigween.isFivePattern = true;
        worldObj.spawnEntityInWorld(bigween);
        TickRegister.register(new TickTask(40, true) {
            @Override
            public boolean stopCondition() {
                return isDead || bigween.isDead;
            }

            @Override
            public void run(Type type) {
                if (absRunCount == 5) {
                    EntityPlayerWeen.change();
                }
                if (absRunCount == 7) {
                    absLoop = false;
                    return;
                }
                if (absRunCount % 4 == 0) {
                    summonPlayerWeen(worldObj, WorldAPI.rand(10), WorldAPI.rand(10));
                    summonPlayerWeen(worldObj, WorldAPI.rand(10), WorldAPI.rand(10));
                    summonPlayerWeen(worldObj, WorldAPI.rand(10), WorldAPI.rand(10));
                    summonPlayerWeen(worldObj, WorldAPI.rand(20), WorldAPI.rand(20));
                    summonPlayerWeen(worldObj, WorldAPI.rand(20), WorldAPI.rand(20));
                    summonPlayerWeen(worldObj, WorldAPI.rand(20), WorldAPI.rand(20));
                }
            }
        });
    }

    public void summonPlayerWeen(World worldObj, double x, double z) {
        EntityPlayerWeen ween = new EntityPlayerWeen(worldObj);
        ween.setPosition(WorldAPI.x() + x, WorldAPI.y() + 5, WorldAPI.z() + z);
        worldObj.spawnEntityInWorld(ween);
        ween.getPlayerSkin(true);
        ween.setElytra(true);
        Vec3d v = WorldAPI.getPlayer().getLookVec();
        ween.setTarget(WorldAPI.x() + v.xCoord * 3.5, WorldAPI.y() + WorldAPI.getPlayer().eyeHeight + 5,
                WorldAPI.z() + v.zCoord * 3.5);
    }

    /**
     * 여섯번째 패턴
     */
    public void jumpStartSixWeen() {
        setPattern(6);
        if (!this.isServerWorld()) {
            System.out.println("서버 월드가 아니므로 캔슬됨");
            return;
        }
        EntityWeen ween = this;
        WeenEffect.entityRotateX(this, -90, new TickTask() {
            @Override
            public void run(Type type) {
                dataManager.set(IS_ROTATE, true);
                WeenEffect.entityJump(ween, 60, new TickTask() {
                    @Override
                    public void run(Type type) {
                        System.out.println("도달함");
                        setRotate(90, 0, 0);
                        setVelocity(0, 0, 0);
                        for (int i = 0; i < 30; i++) {
                            double x = WorldAPI.minRand(10, 15);
                            double z = WorldAPI.minRand(10, 15);
                            EntityMiniWeen miniween = new EntityNightMiniWeen(WorldAPI.getWorld(), ween);
                            miniween.setTarget(posX + x, posY - 60, posZ + z);
                            miniween.setPosition(posX + x, posY, posZ + z);
                            Objects.requireNonNull(WorldAPI.getWorld()).spawnEntityInWorld(miniween);
                            miniween.setVelocity(motionX, motionY, motionZ);
                            miniween.setPattern(6);
                        }
                        getDataManager().set(IS_JUMP, true);
                        dataManager.set(COMPLETE_Y, true);
                        dataManager.set(IS_ROTATE, true);
                    }
                });
            }
        });

    }

    public void jumpWeenUpgrade() {
        if (isJumpWeen() && isServerWorld() && isRotateComplete() && onGround && isRotateComplete() && posY > 3
                && posY < 5) {
            WeenEffect.entityKnockBackDamage(this, 30, 30, 30, 5.5F, DamageSource.fall, 7);
            getDataManager().set(IS_JUMP, false);
            dataManager.set(COMPLETE_Y, false);
            dataManager.set(IS_ROTATE, false);
            CMEffect.setCameraEarthquake2(15, 60);
            this.attackEntityFrom(DamageSource.fall, 5);
            this.setSturn(true);
            if (getHealth() > 40)
                this.setHealth(40);
            // sevenPattern();
        }
    }

    public void sevenPattern() {
        EntityWeen ween = this;

        TickRegister.register(new TickTask(4, true) {
            @Override
            public void run(Type type) {
                ween.setScale(ween.getScaleX() - 1, 0, 0);
                ween.setScale(0, ween.getScaleY() - 1, 0);
                ween.setScale(0, 0, ween.getScaleZ() - 1);
                ween.setSize(width - 1, height - 1);
                if (ween.getScaleX() == 3) {
                    absLoop = false;
                }
            }
        });

    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        EntityWeen ween = this;
        for (int i = 0; i < 25; i++)
            seedDrop();
        endTime = System.currentTimeMillis();
        long se = endTime - startTime;
        long sec = se / (1000);
        long minute = sec / 60;
        long second = sec - sec / 60 * 60;
        System.out.println((endTime - EntityWeen.startTime) / 1000 + "초.");
        ItemStack stack = EntityAPI.createBook(WorldAPI.getPlayer().getName()+"밈에게", "호박", "호박 윈을 잡는데 걸린 시간 : \n " + (minute) + "분 "
                + second + "초" + "\n\n 플레이 해주셔서 감사합니다. \n\n 제가 만든 모드 점프맵도 한번 해보세요. \n (뒤에도 페이지 있음)", "\n 간단한 미니게임이 있음! \n 하려면 채팅창에 슈팅게임 이라고 입력하세요", "");

        if (!worldObj.isRemote)
            ((EntityPlayer) cause.getEntity()).inventory.addItemStackToInventory(stack);
        File file = new File("./config/Halloween.cfg");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void seedDrop() {
        EntityItem item = new EntityItem(worldObj, posX + WorldAPI.rand(10), posY + 30, posZ + WorldAPI.rand(10),
                new ItemStack(Items.PUMPKIN_SEEDS));
        worldObj.spawnEntityInWorld(item);
        TickRegister.register(new TickTask(20, true) {
            @Override
            public void run(Type type) {
                if (item.motionY == 0) {
                    IBlockState state = worldObj.getBlockState(item.getPosition().down());
                    if (state != null && state.getBlock() == Blocks.GRASS || state.getBlock() == Blocks.DIRT) {
                        WorldAPI.getWorld().setBlockState(item.getPosition().down(), Blocks.FARMLAND.getDefaultState());
                        WorldAPI.getWorld().setBlockState(item.getPosition(), Blocks.PUMPKIN_STEM.getDefaultState());
                    }
                    item.setDead();
                    absLoop = false;
                }
            }
        });
    }
    @Override
    public void onSturn() {
        setBlock(isSturn() ? Blocks.PUMPKIN : Blocks.LIT_PUMPKIN);
        if (isSturn()) {
            System.out.println("스턴 걸림");
            this.bossHealth.setName(new TextComponentString("윈 (기절)" + getHealth()));
        } else {
            System.out.println("스턴 풀림");
            this.bossHealth.setName(new TextComponentString("윈" + getHealth()));
        }
    }
    public boolean isJumpWeen() {
        return getDataManager().get(IS_JUMP);
    }

    public boolean isRotateComplete() {
        return getDataManager().get(IS_ROTATE);
    }

    public boolean isCompleteY() {
        return getDataManager().get(COMPLETE_Y);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        if (entityIn instanceof EntityBlock || entityIn instanceof EntityBigWeen || entityIn instanceof EntityMiniWeen)
            return;
        super.collideWithEntity(entityIn);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(IS_JUMP, false);
        this.dataManager.register(IS_ROTATE, false);
        this.dataManager.register(COMPLETE_Y, false);
        this.dataManager.register(PATTERN, 1F);
        this.dataManager.register(LOOK_PLAYER, true);
    }

    public float pattern() {
        return dataManager.get(PATTERN);
    }

    public void setPattern(float pattern) {
        dataManager.set(PATTERN, pattern);
    }

    /******************/

    @Override
    public void knockBack(Entity entityIn, float strenght, double xRatio, double zRatio) {
        // super.knockBack(entityIn, strenght, xRatio, zRatio);
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        this.bossHealth.setPercent(this.getHealth() / this.getMaxHealth());
    }

    public void addTrackingPlayer(EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bossHealth.addPlayer(player);
    }

    public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossHealth.removePlayer(player);
    }


}
