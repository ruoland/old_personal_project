package ruo.asdf;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import ruo.asdf.ai.EntityAIBlockPlace;
import ruo.minigame.map.EntityFlyNPC;
import ruo.minigame.map.TypeModel;

import javax.annotation.Nullable;

public class EntitySkelereeper extends EntitySkeleton {
    public EntitySkelereeper(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
        this.getHeldItemMainhand().setStackDisplayName("크리퍼 활");
        this.setDropChance(EntityEquipmentSlot.MAINHAND, 30);
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        EntityCreeper creeper = new EntityCreeper(worldObj);
        creeper.setPosition(posX,posY,posZ);
        worldObj.spawnEntityInWorld(creeper);
        creeper.startRiding(this);
        return super.onInitialSpawn(difficulty, livingdata);

    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(2, new EntityAIBlockPlace(this));
    }
}