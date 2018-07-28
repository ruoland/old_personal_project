package ruo.halloween;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
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
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.camera.Camera;
import ruo.cmplus.util.Sky;
import ruo.halloween.miniween.*;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.RenderAPI;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.CMEffect;
import ruo.minigame.effect.TickRegister;
import ruo.minigame.map.EntityDefaultNPC;
import ruo.minigame.map.TypeModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class EntityWeen extends EntityDefaultNPC {

    public static long startTime, endTime;
    private static final DataParameter<Boolean> ISJUMP = EntityDataManager.<Boolean>createKey(EntityWeen.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ISROTATE = EntityDataManager.<Boolean>createKey(EntityWeen.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ISCOMPLETEY = EntityDataManager.<Boolean>createKey(EntityWeen.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> LOOK_PLAYER = EntityDataManager.<Boolean>createKey(EntityWeen.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> PATTERN = EntityDataManager.createKey(EntityWeen.class, DataSerializers.VARINT);
    //1 = 점프 후 추락, 2 = 미니윈, 3 = 빅윈, 4 = 밤모드 텔레포트 상태, 5 = 밤모드 다음 빅윈 소환후 계단, 6 = 마지막 점프

    private final BossInfoServer bossHealth = (BossInfoServer) (new BossInfoServer(new TextComponentString("윈의 체력"),
            BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);

    public EntityWeen(World worldIn) {
        super(worldIn);
        this.setSize(13, 13);
        this.setModel(TypeModel.BLOCK);
        this.setTra(0, -5F, 0);
        this.addScale(12);
        setCollision(true);
        setRotate(0, 0, 180);

        this.setBlock(Blocks.LIT_PUMPKIN);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(0);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(250.0D);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getEntity() instanceof EntityAttackMiniWeen && pattern() <= 5) {//패턴6 상태에서 미니윈 폭발에 윈이 죽는 경우가 있음
            return super.attackEntityFrom(source, amount);
        }
        if (source.getEntity() instanceof EntityBigWeen) {
            EntityBigWeen ween = (EntityBigWeen) source.getEntity();
            if (ween.isFivePattern) {
                TickRegister.register(new AbstractTick(100, false) {
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
        if (!isSturn())
            return false;
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        // sevenPattern();
        jumpStartWeen();
        return super.onInitialSpawn(difficulty, livingdata);
    }


    public void onLivingUpdate() {
        if (isServerWorld()) {
            if (!isSturn() && WorldAPI.getPlayer() != null)
                faceEntity(WorldAPI.getPlayer(), 360, 360);
            if (!isSturn() && pattern() == 1) {
                if (!isJumpWeen())
                    jumpStartWeen();
                if (isJumpWeen() && isServerWorld() && isRotateComplete() && onGround && isCompleteY())
                    jumpWeenFall();//하늘에 올라갔다가 땅에 떨어진 경우
            }
            if (pattern() == 6)
                jumpWeenUpgrade();
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_V)) {
            forceSkip = true;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
            patternHold = !patternHold;
            System.out.println(patternHold);
        }
        super.onLivingUpdate();
    }

    /**
     * 첫번째 패턴
     */
    public void jumpStartWeen() {
        EntityWeen ween = this;
        System.out.println("[첫패턴]점프를 시작해 스턴을 해제함");
        setSturn(false);
        getDataManager().set(ISJUMP, true);
        WeenEffect.entityRotateX(this, -90, new AbstractTick() {
            @Override
            public void run(Type type) {
                dataManager.set(ISROTATE, true);
                WeenEffect.entityJump(ween, 60, new AbstractTick() {
                    @Override
                    public void run(Type type) {
                        setRotate(90, 0, 0);
                        dataManager.set(ISCOMPLETEY, true);
                    }
                });
            }
        });
    }

    private static ArrayList<EntityAttackMiniWeen> blockList = new ArrayList<>();
    private int firstEndHP = 220, secondEndHP = 160, fourEndHP = 110;
    private boolean forceSkip, patternHold;

    public void jumpWeenFall() {
        TickRegister.register(new AbstractTick(Type.SERVER, 3, true) {
            @Override
            public void run(Type type) {
                if(absRunCount == 10)
                    absLoop = false;
                for(int i = 0; i < 2;i++) {
                    EntityAttackMiniWeen blockWeen = new EntityAttackMiniWeen(worldObj);
                    blockWeen.setPosition(posX + WorldAPI.minRand(10, 20), posY, posZ + WorldAPI.minRand(10, 20));
                    worldObj.spawnEntityInWorld(blockWeen);
                    blockWeen.setFlyXYZ(0, 0, 0);
                    blockWeen.addRotate(rand.nextInt(90), rand.nextInt(90), rand.nextInt(90));
                    blockWeen.addVelocity(WorldAPI.rand2(8), rand.nextInt(3), WorldAPI.rand2(8));
                    blockList.add(blockWeen);
                    System.out.println("소환됨" + blockWeen);
                }
            }
        });
        for (EntityLivingBase base : worldObj.getEntitiesWithinAABB(EntityLivingBase.class,
                getEntityBoundingBox())) {
            if (base == this || base instanceof EntityMiniWeen) {
                continue;
            }
            base.attackEntityFrom(DamageSource.inWall, 10000);
        }
        WeenEffect.entityKnockBackDamage(this, 30, 5, 30, 3.5F, true, DamageSource.fall, 5);
        CMEffect.setCameraEarthquake2(15, 60);
        WeenEffect.fallParticle(this, Blocks.GRASS, 50);

        this.attackEntityFrom(DamageSource.fall, 5);

        if (!patternHold && (forceSkip || getHealth() < firstEndHP)) {
            System.out.println("[첫패턴]첫번째 패턴 완료함");
            setPattern(2);
            forceSkip = false;
            twoPatternAttackMiniWeen();
        } else {
            System.out.println("" + !patternHold + forceSkip + (getHealth() < firstEndHP) + "[첫패턴]체력이 " + firstEndHP + " 넘은채 추락해 스턴걸림");

        }
        setSturn(200);

        dataManager.set(ISJUMP, false);
        dataManager.set(ISCOMPLETEY, false);
        dataManager.set(ISROTATE, false);

    }

    @Override
    public void onSturn() {
        setBlock(isSturn() ? Blocks.PUMPKIN : Blocks.LIT_PUMPKIN);
        if (isSturn()) {
            System.out.println("스턴 걸림");
            this.bossHealth.setName(new TextComponentString("윈 (기절한 상태)"));
        } else {
            this.bossHealth.setName(new TextComponentString("윈"));
        }
    }

    public void twoPatternAttackMiniWeen() {
        setRotate(0, 0, 180);
        TickRegister.register(new AbstractTick(130, false) {
            @Override
            public boolean stopCondition() {
                return isDead;
            }

            @Override
            public void run(Type type) {
                System.out.println(absDefTick + "틱" + absRunCount + " - " + blockList.size());
                    TickRegister.register(new AbstractTick(30, true) {
                        @Override
                        public boolean stopCondition() {
                            return blockList.size() <= absRunCount;
                        }
                        @Override
                        public void run(Type type) {
                            EntityAttackMiniWeen attackMiniWeen = blockList.get(absRunCount);
                            attackMiniWeen.setFlyXYZ(attackMiniWeen.posX, posY + 10,
                                    attackMiniWeen.posZ);
                            attackMiniWeen.setTargetExplosion(false);
                            TickRegister.register(new AbstractTick(40, false) {
                                @Override
                                public void run(Type type) {
                                    attackMiniWeen.setFlyXYZ(WorldAPI.x() + WorldAPI.rand(3), WorldAPI.y() + WorldAPI.getPlayer().eyeHeight,
                                            WorldAPI.z() + WorldAPI.rand(3));
                                    attackMiniWeen.setTargetExplosion(true);
                                }
                            });
                            if( absRunCount > blockList.size() - 1) {
                                blockList.clear();
                                blockList = null;
                                twoPattern2();
                            }
                        }
                    });
            }
        });
    }
    public void twoPattern2() {

        if (!patternHold && (forceSkip || (getHealth() < secondEndHP))) {
            threePattern();
            return;
        }
    }

    public void threePattern() {
        System.out.println("[두번째 패턴]체력이 " + secondEndHP + " 넘어 다음 패턴으로 넘어감");
        setPattern(3);
        summonBigWeen();

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
        TickRegister.register(new AbstractTick(40, true) {
            @Override
            public boolean stopCondition() {
                return isDead || bigween.isDead;
            }

            @Override
            public void run(Type type) {
                if (absRunCount == 20) {
                    absLoop = false;
                    return;
                }
                EntityPlayer player = WorldAPI.getPlayer();
                for (int i = 0; i < 8; i++) {
                    Vec3d v = player.getLookVec();
                    EntityDefenceMiniWeen miniween = new EntityDefenceMiniWeen(worldObj, bigween.ween);
                    miniween.setPosition(posX + WorldAPI.rand(20), posY + 10 + rand.nextInt(4),
                            posZ + WorldAPI.rand(20));
                    worldObj.spawnEntityInWorld(miniween);
                    miniween.setFlyXYZ(WorldAPI.x() + v.xCoord * 1.5, WorldAPI.y() + player.eyeHeight + v.yCoord * 1.5, WorldAPI.z() + v.zCoord * 1.5);
                    TickRegister.register(new AbstractTick(300, false) {
                        @Override
                        public void run(Type type) {
                            miniween.setFlyXYZ(bigween.posX, bigween.posY, bigween.posZ);
                            miniween.goWeen = true;
                        }
                    });
                }

            }
        });
    }

    public void theNight() {
        EntityWeen ween = this;
        setPattern(4);
        WeenEffect.fog(7, new AbstractTick() {
            @Override
            public void run(Type type) {
                TickRegister.register(new AbstractTick(Type.SERVER, 40, true) {
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
                            EntityMiniWeen miniween = new EntityAttackMiniWeen(worldObj, ween);
                            miniween.setPosition(posX, posY + 3, posZ);
                            worldObj.spawnEntityInWorld(miniween);
                            miniween.setFlyXYZ(WorldAPI.x(), WorldAPI.y() + WorldAPI.getPlayer().eyeHeight,
                                    WorldAPI.z());
                            System.out.println("미니윈 소환");
                        }
                        if (!patternHold && getHealth() < fourEndHP && absRunCount > 7) {
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
                TickRegister.register(new AbstractTick(Type.RENDER, 1, false) {
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
        TickRegister.register(new AbstractTick(Type.RENDER, 1, true) {
            @Override
            public boolean stopCondition() {
                return isDead;
            }

            @Override
            public void run(Type type) {
                Sky.fogDistance(absRunCount);
                if (absRunCount == f1) {
                    absLoop = false;
                    TickRegister.register(new AbstractTick(Type.SERVER, 20, true) {
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
        TickRegister.register(new AbstractTick(40, true) {
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
                    summonPlayerWeen(worldObj, WorldAPI.rand(10), WorldAPI.rand(10), WorldAPI.rand(10),
                            WorldAPI.rand(10));
                    summonPlayerWeen(worldObj, WorldAPI.rand(10), WorldAPI.rand(10), WorldAPI.rand(10),
                            WorldAPI.rand(10));
                    summonPlayerWeen(worldObj, WorldAPI.rand(10), WorldAPI.rand(10), WorldAPI.rand(10),
                            WorldAPI.rand(10));
                    summonPlayerWeen(worldObj, WorldAPI.rand(20), WorldAPI.rand(20), WorldAPI.rand(20), WorldAPI.rand(20));
                    summonPlayerWeen(worldObj, WorldAPI.rand(20), WorldAPI.rand(20), WorldAPI.rand(20), WorldAPI.rand(20));
                    summonPlayerWeen(worldObj, WorldAPI.rand(20), WorldAPI.rand(20), WorldAPI.rand(20), WorldAPI.rand(20));
                }
            }
        });
    }

    public void summonPlayerWeen(World worldObj, double x, double z, double x2, double z2) {
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
        WeenEffect.entityRotateX(this, -90, new AbstractTick() {
            @Override
            public void run(Type type) {
                dataManager.set(ISROTATE, true);
                WeenEffect.entityJump(ween, 60, new AbstractTick() {
                    @Override
                    public void run(Type type) {
                        System.out.println("도달함");
                        setRotate(90, 0, 0);
                        setVelocity(0, 0, 0);
                        for (int i = 0; i < 30; i++) {
                            double x = WorldAPI.minRand(10, 15);
                            double z = WorldAPI.minRand(10, 15);
                            EntityMiniWeen miniween = new EntityNightMiniWeen(WorldAPI.getWorld(), ween)
                                    .setFlyXYZ(posX + x, posY - 60, posZ + z);
                            miniween.setPosition(posX + x, posY, posZ + z);
                            WorldAPI.getWorld().spawnEntityInWorld(miniween);
                            miniween.setVelocity(motionX, motionY, motionZ);
                        }
                        getDataManager().set(ISJUMP, true);
                        dataManager.set(ISCOMPLETEY, true);
                        dataManager.set(ISROTATE, true);
                    }
                });
            }
        });

    }

    public void jumpWeenUpgrade() {
        if (isJumpWeen() && isServerWorld() && isRotateComplete() && onGround && isRotateComplete() && posY > 3
                && posY < 5) {
            WeenEffect.entityKnockBackDamage(this, 30, 30, 30, 5.5F, true, DamageSource.fall, 7);
            getDataManager().set(ISJUMP, false);
            dataManager.set(ISCOMPLETEY, false);
            dataManager.set(ISROTATE, false);
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

        TickRegister.register(new AbstractTick(4, true) {
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
        ItemStack stack = EntityAPI.createBook(EntityPeaceWitch.getName(0), "호박", "호박 윈을 잡는데 걸린 시간 : \n " + (minute) + "분 "
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
        TickRegister.register(new AbstractTick(20, true) {
            @Override
            public void run(Type type) {
                if (item.motionY == 0) {
                    if (WorldAPI.getWorld() == null)
                        return;
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

    public boolean isJumpWeen() {
        return getDataManager().get(ISJUMP);
    }

    public boolean isRotateComplete() {
        return getDataManager().get(ISROTATE);
    }

    public boolean isCompleteY() {
        return getDataManager().get(ISCOMPLETEY);
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
        this.dataManager.register(ISJUMP, false);
        this.dataManager.register(ISROTATE, false);
        this.dataManager.register(ISCOMPLETEY, false);
        this.dataManager.register(PATTERN, 1);
        this.dataManager.register(LOOK_PLAYER, true);
    }

    public int pattern() {
        return dataManager.get(PATTERN);
    }

    public void setPattern(int pattern) {
        dataManager.set(PATTERN, pattern);
    }

    /******************/

    public Vec3d getLook(float partialTicks, float pitch) {
        if (partialTicks == 1.0F) {
            return this.getVectorForRotation(pitch, this.rotationYawHead);
        } else {
            float f = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * partialTicks;
            float f1 = this.prevRotationYawHead + (this.rotationYawHead - this.prevRotationYawHead) * partialTicks;
            return this.getVectorForRotation(f, f1);
        }
    }

    public void shakePlayer(EntityPlayer player) {
        player.addVelocity(0, 2, 0);
        TickRegister.register(new AbstractTick(Type.CLIENT, 1, true) {

            @Override
            public boolean stopCondition() {
                return isDead;
            }

            @Override
            public void run(Type type) {
                if (absRunCount < 10) {
                    player.addVelocity(0.03, 0.01, 0);
                }
                if (absRunCount > 10 && absRunCount < 21) {
                    player.addVelocity(-0.03, 0.01, 0);
                }
                if ((absRunCount > 30 && absRunCount < 40)) {
                    player.addVelocity(-0.31, 0, 0);
                    if (player.onGround) {
                        player.attackEntityFrom(DamageSource.inWall, 3);
                        player.setHealth(player.getHealth() - 3);
                        absLoop = false;
                    }
                }
                if (absRunCount > 50) {
                    if (player.onGround) {
                        player.attackEntityFrom(DamageSource.inWall, 5);
                        player.setHealth(player.getHealth() - 5);
                        absLoop = false;
                    }
                }
            }
        });
    }

    @Override
    public void knockBack(Entity entityIn, float strenght, double xRatio, double zRatio) {
        // super.knockBack(entityIn, strenght, xRatio, zRatio);
    }

    public void upupup(EntityPlayer player) {
        player.addVelocity(0, 3.5, 0);
        Camera.getCamera().reset();
        TickRegister.register(new AbstractTick(Type.CLIENT, 1, true) {
            @Override
            public void run(Type type) {
                Camera.getCamera().rotateZ += 0.555;
                Camera.getCamera().moveCamera(0, 1.5, 0);
                if (Camera.getCamera().rotateZ >= 180) {
                    absLoop = false;
                }
            }
        });
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
