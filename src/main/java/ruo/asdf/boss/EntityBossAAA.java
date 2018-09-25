package ruo.asdf.boss;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ruo.minigame.api.BlockAPI;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.Direction;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.map.EntityDefaultNPC;

import java.util.ArrayList;
import java.util.List;

public class EntityBossAAA extends EntityDefaultNPC {
    private ArrayList<EntityMob> blackEntityList = new ArrayList();
    private ArrayList<EntityMob> blackEntitySearch = new ArrayList();
    private EntityLivingBase bhAttack;
    private Vec3d bhAttackVec3d, bhAttackTarget;
    private Vec3d earthHideVec3d, earthHideTarget;
    private EntityDefaultNPC hideModeBlock;
    private static final DataParameter<Boolean> IS_HIDE = EntityDataManager.createKey(EntityBossAAA.class, DataSerializers.BOOLEAN);

    public EntityBossAAA(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(IS_HIDE, false);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        bhUpdate();
    }

    public void spawnUpBlock(){
        BlockAPI blockAPI = WorldAPI.getBlock(worldObj, posX,posY,posZ,16);

    }
    public void setEarthHide(boolean hide) {
        this.noClip = hide;
        this.isFly = hide;
        dataManager.set(IS_HIDE, hide);
        this.setPosition(posX, posY - 3, posZ);

        if (hide) {
            if(!worldObj.isAirBlock(getPosition().add(0, 2, 0))) {
                hideModeBlock = EntityAPI.spawn(posX, posY + 1, posZ);
                hideModeBlock.setBlockMode(worldObj.getBlockState(getPosition().add(0, 2, 0)).getBlock());
            }
            earthHideTarget = getAttackTarget().getPositionVector();
            earthHideVec3d = this.getPositionVector().subtract(getAttackTarget().getPositionVector());
        } else {
            earthHideVec3d = null;
            earthHideTarget = null;
            hideModeBlock = null;
        }
    }

    public boolean isHide() {
        return dataManager.get(IS_HIDE);
    }

    public void earthUpdate() {
        if (isHide()) {
            setVelocity(earthHideVec3d);
            if (getDistance(earthHideTarget) < 1) {
                setEarthHide(false);
                if(!worldObj.isAirBlock(getPosition().add(0, 2, 0))) {
                    EntityDefaultNPC defaultNPC = EntityAPI.spawn(posX, posY + 1, posZ);
                    defaultNPC.setBlockMode(worldObj.getBlockState(getPosition().add(0, 2, 0)).getBlock());
                    defaultNPC.addVelocity(0, 0.5, 0);
                }
                this.addVelocity(0,0.5,0);
                worldObj.setBlockToAir(getPosition());
                worldObj.setBlockToAir(getPosition().add(0,1,0));
                worldObj.setBlockToAir(getPosition().add(0,2,0));
            }else
            {
                hideModeBlock.setPosition(posX,posY+2,posZ);
                hideModeBlock.setBlock(worldObj.getBlockState(getPosition().add(0, 2, 0)).getBlock());
            }
        }
    }

    public void bhSearch() {
        List<EntityMob> livingBaseList = EntityAPI.getEntity(worldObj, getEntityBoundingBox().expand(32, 32, 32), EntityMob.class);
        for (EntityMob livingBase : livingBaseList) {
            blackEntitySearch.add(livingBase);
        }
    }

    public void bhUpdate() {
        double targetY = posY + 5;
        if (blackEntitySearch.size() == 0 && blackEntityList.size() == 0) {
            bhSearch();
        }
        for (EntityMob livingBase : blackEntitySearch) {
            if (!blackEntityList.contains(livingBase)) {
                livingBase.setVelocity(posX - livingBase.posX, targetY - livingBase.posY, posZ - livingBase.posZ);
                if (livingBase.getDistance(posX, targetY, posZ) < 1) {
                    blackEntityList.add(livingBase);
                }
            }
        }
        blackEntitySearch.removeAll(blackEntityList);
        if (blackEntitySearch.size() == 0 && blackEntityList.size() > 0) {
            bhAttack();
        }
    }

    public void bhAttack() {
        if (bhAttack == null) {
            bhAttack = blackEntityList.get(worldObj.rand.nextInt(blackEntityList.size() - 1));
            bhAttackVec3d = getAttackTarget().getPositionVector().subtract(this.getPositionVector()).normalize();
            bhAttackTarget = getAttackTarget().getPositionVector();
        }
        if (getDistance(bhAttackTarget.xCoord, bhAttackTarget.yCoord, bhAttackTarget.zCoord) < 1) {
            bhAttack.attackEntityFrom(DamageSource.fall, 10);
            worldObj.createExplosion(bhAttack, bhAttack.posX, bhAttack.posY, bhAttack.posZ, 3F, false);
            blackEntityList.remove(bhAttack);
            bhAttack = null;
        } else
            bhAttack.setVelocity(bhAttackVec3d.xCoord, bhAttackVec3d.yCoord, bhAttackVec3d.zCoord);
    }

    public void spawnWall() {
        EntityKnockbackWall wall = new EntityKnockbackWall(worldObj);
        wall.setPosition(this.getPosition());
        worldObj.spawnEntityInWorld(wall);
        wall.setForward(this.getHorizontalFacing());

        for (int i = 1; i <= 2; i++) {
            EntityKnockbackWall wall1 = new EntityKnockbackWall(worldObj);
            wall1.setPosition(this.getPosition().add(getX(Direction.RIGHT, i, false), 0, 0));
            worldObj.spawnEntityInWorld(wall1);
            wall1.setForward(this.getHorizontalFacing());
        }
        for (int i = 1; i <= 2; i++) {
            EntityKnockbackWall wall1 = new EntityKnockbackWall(worldObj);
            wall1.setPosition(this.getPosition().add(0, 0, getZ(Direction.RIGHT,i, false)));
            worldObj.spawnEntityInWorld(wall1);
            wall1.setForward(this.getHorizontalFacing());
        }
    }
}
