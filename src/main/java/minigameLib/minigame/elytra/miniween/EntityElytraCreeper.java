package minigameLib.minigame.elytra.miniween;

import minigameLib.fakeplayer.EntityFakePlayer;
import minigameLib.map.TypeModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityElytraCreeper extends EntityElytraPumpkin {
    public EntityElytraCreeper(World world) {
        super(world);
        this.setModel(TypeModel.CREEPER);
        this.setElytra(true);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        worldObj.createExplosion(this, posX, posY, posZ, 3, true);
        setDead();
        return super.attackEntityFrom(source, amount);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        super.collideWithEntity(entityIn);
        if(entityIn instanceof EntityFakePlayer){
            worldObj.createExplosion(this, posX, posY, posZ, 3, true);
            setDead();
        }
    }
}
