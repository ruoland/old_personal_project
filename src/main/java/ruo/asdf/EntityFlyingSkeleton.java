package ruo.asdf;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.SkeletonType;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import ruo.minigame.map.EntityDefaultNPC;
import ruo.minigame.map.TypeModel;

import javax.annotation.Nullable;

public class EntityFlyingSkeleton extends EntityDefaultNPC {

    public EntityFlyingSkeleton(World worldIn) {
        super(worldIn);
        this.setElytra(true);
        this.setModel(TypeModel.SKELETON);
        experienceValue = 30;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50);
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
        motionX+=this.lookX(0.01);
        motionZ+=this.lookZ(0.01);
        motionY = 0;

    }
}
