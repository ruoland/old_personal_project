package ruo.map.lopre2.jump2;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.Move;
import ruo.minigame.map.EntityDefaultNPC;
import ruo.minigame.map.TypeModel;

import java.util.ArrayList;
import java.util.List;

public class EntityGuardLoop extends EntityDefaultNPC {
    public static ArrayList<EntityGuardLoop> guardList = new ArrayList<EntityGuardLoop>();//사운드 이벤트에서 사용됨
    private static final DataParameter<Boolean> ISROTATE = EntityDataManager.<Boolean>createKey(EntityGuardLoop.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ISCHASE = EntityDataManager.<Boolean>createKey(EntityGuardLoop.class,
            DataSerializers.BOOLEAN);
    protected Vec3d[] pos;
    protected AxisAlignedBB aabb;
    protected float guardTick, prevYaw;// guardYaw는
    private BlockPos startPos, endPos;

    public EntityGuardLoop(World worldIn) {
        super(worldIn);
        this.setHealth(10000);
        this.setAlwaysRenderNameTag(true);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(ISCHASE, false);
        this.dataManager.register(ISROTATE, true);
    }


    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (isServerWorld() && WorldAPI.getPlayer() != null) {
            getAABB(false);
            if (canChase()) {
                if (!isChase() && !isSleep()) {
                    find();
                }
                if (isChase()) {
                    if (WorldAPI.getPlayerMP().interactionManager.getGameType() == GameType.SPECTATOR)
                        this.stopChase();
                    if (getEntitySenses().canSee(WorldAPI.getPlayer())) {//플레이어가 보임
                        findPlayerTick = 0;
                        prevX = WorldAPI.x();
                        prevY = WorldAPI.y();
                        prevZ = WorldAPI.z();
                    } else if (findPlayerTick == 0) {//플레이어가 보이지 않음
                        System.out.println("플레이어가 보이지 않아 마지막으로 보였던 장소로 이동중.");
                        EntityAPI.move(new Move(this, prevX, prevY, prevZ, false) {
                            public void complete() {
                                getAABB(true);
                                find();
                            }
                        }.setDistance(1));
                        findPlayerTick++;
                    }
                    if (EntityAPI.getDistance(this) > 360 || findPlayerTick == 60) {//플레이어를 3초이상 찾지 못하거나 거리가 360 이상인 경우 추적 중단
                        findPlayerTick = 0;
                        stopChase();
                    }
                    if (findPlayerTick >= 1)
                        findPlayerTick++;
                    return;
                }
            }
            if (!isChase() && !EntityAPI.isMove(this) && isRotate()) {
                rotation();
            }

        }
        if (!this.guardList.contains(this)) {
            guardList.add(this);
        }

    }

    protected Vec3d[] getAABB(boolean forceUpdate) {
        if ((!isChase() && prevYaw != rotationYawHead) || pos == null || aabb == null || forceUpdate) {
            prevYaw = rotationYawHead;
            EnumFacing facing = EnumFacing.fromAngle(rotationYaw-30);
            EnumFacing facing2 = EnumFacing.fromAngle(rotationYaw+30);

            pos = new Vec3d[]{new Vec3d(posX+(facing.getFrontOffsetX()*5), facing.getFrontOffsetY(), posZ+facing.getFrontOffsetZ()*5),
                    new Vec3d(posX+facing2.getFrontOffsetX()*5, facing2.getFrontOffsetY(), posZ+facing2.getFrontOffsetZ()*5)};
            aabb = new AxisAlignedBB(pos[0], pos[1]);
            System.out.println("pos0 "+pos[0].toString());
            System.out.println("pos1 "+pos[1].toString());

        }
        return pos;
    }

    public void rotation() {
        guardTick++;
        if (guardTick == 60) {
            prevYaw = rotationYawHead + 90;
            rotationYawHead = prevYaw;
            renderYawOffset = prevYaw;
            guardTick = 0;
        }
    }

    public void find() {
        if (aabb == null)
            return;
        List<EntityPlayer> pigList = EntityAPI.getEntity(worldObj, aabb, EntityPlayer.class);
        for (EntityPlayer pig : pigList) {
            if (getEntitySenses().canSee(pig)) {
                startChase(pig);
            }
        }
    }

    public void startChase(EntityPlayer pig) {
        if (defaultRotate || isRotate())
            this.setRotate(false);
        EntityAPI.move(new Move(this, pig, true, 1) {
            public void complete() {
                pig.attackEntityFrom(DamageSource.generic, 5);
                if (pig.isDead) {
                    stopChase();
                    moveLoop = false;
                }
            }

            ;
        }.setSpeed(1.5));
        setChase(true);

    }

    public void stopChase() {
        if (EntityAPI.isMove(this))
            EntityAPI.removeMove(this);
        EntityAPI.removeLook(this);
        if (defaultRotate)
            setRotate(true);
        setChase(false);
        findPlayerTick = 0;
        System.out.println("추적 중단함");
        if (startPos == null) {
            EntityAPI.move(new Move(this, getSpawnPos(), false) {
                public void complete() {
                    System.out.println("추적 중단후 원래 위치로 돌아옴");
                }

                ;
            });
        } else
            startExplorePos();

    }

    public void setExplorePos(BlockPos startPos, BlockPos endPos) {
        this.startPos = startPos;
        this.endPos = endPos;
    }

    public void startExplorePos() {
        EntityAPI.move(new Move(this, startPos, false) {
            @Override
            public void complete() {
                if (movecount % 2 == 0) {
                    setPosition(startPos);
                } else
                    setPosition(endPos);
            }
        });
    }

    public boolean canExplore() {
        return startPos != null;
    }

    private double prevX, prevY, prevZ;// 마지막으로 플레이어를 본 좌표
    protected int findPlayerTick = 0, playerRun = 0, sleepCount;
    protected boolean defaultRotate;

    public boolean isDefaultRotate() {
        return defaultRotate;
    }

    public void setDefaultRotate(boolean defaultRotate) {
        this.defaultRotate = defaultRotate;
    }

    public void setSleepCount(int sleepCount) {
        this.sleepCount = sleepCount;
        if (sleepCount == 0)
            playerRun = 0;
    }


    public boolean canChase() {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isSleep()) {
            playerRun += 1000;
            System.out.println("플레이어가 공격함" + playerRun);
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("isRotate", isRotate());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setRotate(compound.getBoolean("isRotate"));
    }

    public void setRotate(boolean b) {
        getDataManager().set(ISROTATE, b);
    }

    @Override
    public void setSleep(boolean is) {
        super.setSleep(is);
        if (is)
            setRotate(false);
    }

    public void setChase(boolean b) {
        getDataManager().set(ISCHASE, b);
    }

    public boolean isChase() {
        return getDataManager().get(ISCHASE);
    }

    public boolean isRotate() {
        return getDataManager().get(ISROTATE);
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (hand == EnumHand.MAIN_HAND && isServerWorld()) {
            this.setSleepRotate(getSleepRotate() + 90F);
            System.out.println(getSleepRotate());
            if (getModel() == TypeModel.ZOMBIE) {
                setModel(TypeModel.CREEPER);
            }
            if (getModel() == TypeModel.CREEPER) {
                setModel(TypeModel.ENDERMAN);
            }
            if (getModel() == TypeModel.ENDERMAN) {
                setModel(TypeModel.ZOMBIE);
            }
        }
        return super.processInteract(player, hand, stack);
    }

}
