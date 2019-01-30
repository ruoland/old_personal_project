package minigameLib.minigame.elytra.miniween;

import minigameLib.MiniGame;
import minigameLib.effect.AbstractTick;
import minigameLib.effect.TickRegister;
import minigameLib.fakeplayer.EntityFakePlayer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EntityElytraBullet extends EntityElytraPumpkinAttack {

    private static final DataParameter<Float> DAMAGE = EntityDataManager.createKey(EntityElytraBullet.class, DataSerializers.FLOAT);

    public EntityElytraBullet(World world) {
        super(world);
        this.setBlockMode(Blocks.PUMPKIN);
        this.setScale(0.5F, 0.5F, 0.5F);
        this.setSize(0.5F, 0.5F);
        this.setDeathTimer(100);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(DAMAGE, 3F);
    }

    @Override
    public void targetArrive() {
        if (isTNTMode()) {
            worldObj.createExplosion(this, posX, posY, posZ, 1.5F, true);
            setDead();
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getEntity() instanceof EntityArrow || source.isProjectile() || source.getDamageType().equalsIgnoreCase("arrow")) {
            this.setDead();
        }

        return false;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        motionY = 0;
        if (isWaterMode()) {

            BlockPos waterPosition = getPosition().add(0,-1,0);
            this.worldObj.setBlockState(waterPosition, Blocks.WATER.getDefaultState());
            worldObj.getBlockState(waterPosition.offset(EnumFacing.SOUTH));
            TickRegister.register(new AbstractTick(5, true) {

                @Override
                public void run(TickEvent.Type type) {
                    for(EnumFacing facing : EnumFacing.HORIZONTALS) {
                        BlockPos waterPos = waterPosition.offset(facing);
                        IBlockState waterState = worldObj.getBlockState(waterPos);
                        Block block = waterState.getBlock();
                        worldObj.setBlockToAir(waterPosition.add(0,-1,0));

                        if(block instanceof BlockLiquid && waterState.getValue(BlockLiquid.LEVEL) > 0) {
                            worldObj.setBlockToAir(waterPos);
                        }
                        if(absRunCount > 5){
                            worldObj.setBlockToAir(waterPosition);
                            absLoop = false;
                        }
                    }
                }
            });
        }
        if (!MiniGame.elytra.isStart())
            setDead();
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        if (!(entityIn instanceof EntityElytraPumpkin)) {
            super.collideWithEntity(entityIn);
        } else {
            return;
        }

        if (entityIn instanceof EntityFakePlayer) {
            if (isBurning()) {
                entityIn.setFire(2);
            }
            if (isTNTMode()) {
                worldObj.createExplosion(this, posX, posY, posZ, 1.5F, true);
                entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), getDamage() * 1.5F);

            } else
                entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), getDamage());
            this.setDead();
        }
    }

    public void setDamage(float damage) {
        dataManager.set(DAMAGE, damage);
    }

    public float getDamage() {
        return dataManager.get(DAMAGE);
    }

}
