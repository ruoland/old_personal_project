package olib.fakeplayer;

import olib.map.EntityDefaultNPC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityFakePlayer extends EntityDefaultNPC {
    private int respawnTime;//무적 타임
    private static double boundingX, boundingY, boundingZ;
    public EntityFakePlayer(World worldIn) {
        super(worldIn);
        initEntityAI();
        this.setHealth(getMaxHealth());
        respawnTime = 40;
    }

    @Override
    public void setElytra(boolean ely) {
        super.setElytra(ely);
        setElytraSize(getHorizontalFacing());
    }

    public void setElytraSize(EnumFacing facing){
        double min = 1.5;
        float tra = 0.28F;
        if(!isElytra())
        {
            boundingY = 0;
            boundingX = 0;
            boundingZ = 0;
            return;
        }
        if(facing == EnumFacing.EAST) {
            boundingX = min;
            boundingZ = 0;
        }
        if(facing == EnumFacing.WEST) {
            boundingX = -min;
            boundingZ = 0;
        }
        if(facing == EnumFacing.SOUTH){
            boundingX = 0;
            boundingZ = min;
        }
        if(facing == EnumFacing.NORTH){
            boundingX = 0;
            boundingZ = -min;
        }
        setTra(0,tra, 0);

    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(8.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80);
    }

    protected void initEntityAI() {
        //this.tasks.addTask(0, new EntityAISwimming(this));
        // this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        // this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        // this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class,
        // 8.0F));
        // this.tasks.addTask(8, new EntityAILookIdle(this));
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        FakePlayerHelper.setFakeDead();
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(respawnTime > 0)
            respawnTime--;
        setEntityBoundingBox(new AxisAlignedBB(getEntityBoundingBox().minX,
                getEntityBoundingBox().minY,
                getEntityBoundingBox().minZ,
                getEntityBoundingBox().minX + width+boundingX,
                getEntityBoundingBox().minY + height+boundingY,
                getEntityBoundingBox().minZ + width+boundingZ));
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(respawnTime > 0 || source.getEntity() instanceof EntityFakePlayer)
            return false;
        return super.attackEntityFrom(source, amount);
    }

    NetworkPlayerInfo playerInfo;

    @Override
    public void jump() {
        super.jump();
    }

    @Nullable
    public ResourceLocation getLocationElytra() {
        NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
        return networkplayerinfo == null ? null : networkplayerinfo.getLocationElytra();
    }

    @Nullable
    public NetworkPlayerInfo getPlayerInfo() {
        if (this.playerInfo == null) {
            this.playerInfo = Minecraft.getMinecraft().getConnection()
                    .getPlayerInfo(Minecraft.getMinecraft().thePlayer.getUniqueID().toString());
        }
        return this.playerInfo;
    }

}
