package minigameLib.minigame.minerun;

import olib.api.WorldAPI;
import olib.map.EntityDefaultNPC;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityMR extends EntityDefaultNPC {
    protected boolean isLookPlayer = true;
    protected boolean isHide;
    public EntityMR(World worldIn) {
        super(worldIn);
        isFly = true;
    }

    @Override
    public void knockBack(Entity entityIn, float strenght, double xRatio, double zRatio) {
        //super.knockBack(entityIn, strenght, xRatio, zRatio);
    }

    @Override
    public void applyEntityCollision(Entity entityIn) {
        //ssuper.applyEntityCollision(entityIn);
    }

    @Override
    protected void collideWithNearbyEntities() {
        //super.collideWithNearbyEntities();
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        //super.collideWithEntity(entityIn);
        if (entityIn instanceof EntityPlayer && !WorldAPI.getPlayer().isCreative() && getDistanceToEntity(entityIn) < 0.7 && !isSturn()) {
            this.swingArm();
            this.setLastAttacker(entityIn);
            collideAttack(WorldAPI.getPlayer());
            this.setSturn(100);
        }
    }

    public void collideAttack(Entity entityIn) {
        entityIn.attackEntityFrom(DamageSource.cactus, 3);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (source.getEntity() instanceof EntityPlayer) {
            if (source.getEntity().isSneaking()) {
                this.setDead();
            }
        }
        return false;
    }

    @Override
    public void onSturn() {
        setInvisible(!isSturn());
        System.out.println("스턴"+isSturn()+isInvisible());
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (isLookPlayer && WorldAPI.getPlayer() != null)
            this.faceEntity(WorldAPI.getPlayer(), 360, 360);
        if(isFly)
        this.setVelocity(0,0,0);
    }
}
