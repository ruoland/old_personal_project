package map.puzzle;

import map.lopre2.EntityPreBlock;
import map.lopre2.jump2.EntityTeleportBlock;
import minigameLib.api.EntityAPI;
import minigameLib.map.EntityDefaultNPC;
import minigameLib.map.TypeModel;
import minigameLib.minigame.minerun.EntityMRWalkingZombie;
import minigameLib.minigame.minerun.EntityMRZombie;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
import javax.xml.crypto.Data;

public class EntityPuzzleMonster extends EntityDefaultNPC {
    private static final DataParameter<String> PATH = EntityDataManager.createKey(EntityPuzzleMonster.class, DataSerializers.STRING);

    public EntityPuzzleMonster(World worldIn) {
        super(worldIn);
        typeModel = TypeModel.ZOMBIE;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(PATH, "");
    }


    @Override
    protected void collideWithEntity(Entity entityIn) {
        entityIn.applyEntityCollision(this);
        ((EntityLivingBase) entityIn).knockBack(this, 1.4F, this.posX - entityIn.posX, this.posZ - entityIn.posZ);
        super.collideWithEntity(entityIn);

    }


    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
        if (hand == EnumHand.MAIN_HAND && isServerWorld()) {
            dataManager.set(PATH, player.getHorizontalFacing().getName());
            updateSpawnPosition();
            startMove();
        }

        return super.processInteract(player, hand, stack);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!noTarget()) {
            Vec3d target = getTargetPosition();
            getLookHelper().setLookPosition(target.xCoord, target.yCoord + getEyeHeight(), target.zCoord, 360, 360);
        }
    }

    public void startMove() {
        EnumFacing pathFacing = EnumFacing.byName(dataManager.get(PATH));
        if (pathFacing != null)
            setTarget(posX + (EntityAPI.getFacingX(pathFacing) * 2.5), posY, posZ + (EntityAPI.getFacingZ(pathFacing) * 2.5));
    }

    @Override
    public void targetArrive() {
        super.targetArrive();
        EnumFacing pathFacing = EnumFacing.byName(dataManager.get(PATH));
        if (pathFacing != null) {
            dataManager.set(PATH, EnumFacing.fromAngle(pathFacing.getHorizontalAngle() + 180).getName());
            startMove();
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setString("PATH ", dataManager.get(PATH));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        dataManager.set(PATH, compound.getString("PATH"));
        if (noTarget() && posX != getSpawnX() && posZ != getSpawnZ()) {
            setPosition(getSpawnX(), getSpawnY(), getSpawnZ());
        }
        if (noTarget())
            startMove();
    }
}
