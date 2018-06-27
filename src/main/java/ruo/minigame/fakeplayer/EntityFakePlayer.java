package ruo.minigame.fakeplayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import ruo.minigame.map.EntityDefaultNPC;

import javax.annotation.Nullable;

public class EntityFakePlayer extends EntityDefaultNPC {
    private int respawnTime;//무적 타임
    public EntityFakePlayer(World worldIn) {
        super(worldIn);
        initEntityAI();
        this.setHealth(getMaxHealth());
        respawnTime = 40;
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
