package ruo.asdf;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import ruo.minigame.api.SpawnDirection;
import ruo.minigame.map.EntityDefaultNPC;
import ruo.minigame.map.TypeModel;

import javax.annotation.Nullable;

public class EntityFlyingCreeper extends EntityDefaultNPC {

    public EntityFlyingCreeper(World worldIn) {
        super(worldIn);
        this.setModel(TypeModel.CREEPER);
        this.setElytra(true);
        experienceValue = 30;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30);
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        this.setPosition(getPosition().add(0,10,0));
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        motionX+=this.getX(SpawnDirection.FORWARD, 0.01, false);
        motionZ+=this.getZ(SpawnDirection.FORWARD, 0.01, false);
        motionY = 0;
        
    }
}
