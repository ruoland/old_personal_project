package ruo.map.platformer.chapter1;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import ruo.map.lopre2.EntityPreBlock;
import ruo.minigame.api.WorldAPI;

public class EntityPlatBlock extends EntityPreBlock {
    //넉백된 상태
    private static final DataParameter<Boolean> ISKNOCKBACK = EntityDataManager.createKey(EntityPlatBlock.class, DataSerializers.BOOLEAN);

    //넉백 가능한지 여부
    private static final DataParameter<Boolean> CANKNOCKBACK = EntityDataManager.createKey(EntityPlatBlock.class, DataSerializers.BOOLEAN);

    public EntityPlatBlock(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.COBBLESTONE);
        this.setSize(1, 1);
        isFly = true;
        this.setCollision(true);
        this.noClip = !noClip;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(CANKNOCKBACK, true);
        dataManager.register(ISKNOCKBACK, false);
    }

    public void setCanKnockBack(boolean a) {
        dataManager.set(CANKNOCKBACK, a);
    }

    public boolean canKnockBack() {
        return dataManager.get(CANKNOCKBACK);
    }


    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (hand == EnumHand.MAIN_HAND) {
            setTeleport(!isTeleport());
        }
        return true;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (WorldAPI.getPlayer() != null && dataManager.get(ISKNOCKBACK).booleanValue() && WorldAPI.getPlayer().getDistanceToEntity(this) > 25) {
            this.setDead();
            System.out.println("플랫블럭이 멀리 날라가 사라짐");
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isTeleport() && source.getEntity() instanceof EntityPlayer) {
            setTeleport(false);
            if(canKnockBack()) {
                this.addVelocity(source.getEntity().getLookVec().xCoord * 4, source.getEntity().getLookVec().yCoord * 7, source.getEntity().getLookVec().zCoord * 4);
                dataManager.set(ISKNOCKBACK, true);
            }
        }
        if (source.getEntity() != null && source.getEntity().isSneaking()) {
            this.setDead();
            System.out.println("사라진 플랫블럭 좌표 " + this.getSpawnX() + ", " + this.getSpawnY() + ", " + this.getSpawnZ());
        }
        return true;
    }

    @Override
    public boolean canTeleportLock() {
        return false;
    }
}
