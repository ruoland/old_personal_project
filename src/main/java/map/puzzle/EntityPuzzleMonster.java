package map.puzzle;

import net.minecraft.init.Items;
import net.minecraft.util.DamageSource;
import oneline.api.EntityAPI;
import oneline.map.EntityDefaultNPC;
import oneline.map.TypeModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityPuzzleMonster extends EntityDefaultNPC {

    public EntityPuzzleMonster(World worldIn) {
        super(worldIn);
        typeModel = TypeModel.ZOMBIE;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }


    @Override
    protected void collideWithEntity(Entity entityIn) {
        entityIn.applyEntityCollision(this);
        ((EntityLivingBase) entityIn).knockBack(this, 1.4F, this.posX - entityIn.posX, this.posZ - entityIn.posZ);
        super.collideWithEntity(entityIn);
    }


    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(source.getEntity() instanceof EntityPlayer && ((EntityPlayer) source.getEntity()).getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD){
            setDead();
        }
        return super.attackEntityFrom(source, amount);

    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!noTarget()) {
            Vec3d target = getTargetPosition();
            getLookHelper().setLookPosition(target.xCoord, target.yCoord + getEyeHeight(), target.zCoord, 360, 360);
            this.renderYawOffset = rotationYaw;
        }
    }
}
