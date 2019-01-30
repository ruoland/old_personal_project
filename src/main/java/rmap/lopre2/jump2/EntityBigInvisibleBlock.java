package rmap.lopre2.jump2;

import minigameLib.api.WorldAPI;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import rmap.lopre2.EntityPreBlock;

public class EntityBigInvisibleBlock extends EntityPreBlock {
    public static boolean isInvisibleLock;

    public EntityBigInvisibleBlock(World worldIn) {
        super(worldIn);
        this.setCollision(true);
        setBlockMode(Blocks.STONE);
        this.noClip = !noClip;
        isFly = true;
        this.setScale(3, 1, 3);
        this.setSize(3, 1);
        defaultDelay = 40;
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        this.setTeleport(true);
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    public String getCustomNameTag() {
        return "BigInvisibleBlock 기본 딜레이:" + defaultDelay+" 현재 딜레이:"+currentDelay;
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (hand == EnumHand.MAIN_HAND && stack != null) {
            if (WorldAPI.equalsHeldItem(Items.STICK)) {
                if (!player.isSneaking())
                    defaultDelay++;
                else
                    defaultDelay--;
                return false;
            }
        }
        return super.processInteract(player, hand, stack);
    }

    @Override
    public EntityBigInvisibleBlock spawn(double x, double y, double z) {
        EntityBigInvisibleBlock lavaBlock = new EntityBigInvisibleBlock(worldObj);
        lavaBlock.setSpawnXYZ(x, y, z);
        lavaBlock.setTeleport(false);
        lavaBlock.setPosition(lavaBlock.getSpawnX(), lavaBlock.getSpawnY() + 0.3, lavaBlock.getSpawnZ());
        lavaBlock.setPositionAndRotationDirect(lavaBlock.getSpawnX(), lavaBlock.getSpawnY() + 0.3, lavaBlock.getSpawnZ(), 90, 90, 1, false);
        this.copyModel(lavaBlock);
        lavaBlock.prevBlock = prevBlock;
        lavaBlock.setBlockMode(getCurrentBlock());
        worldObj.spawnEntityInWorld(lavaBlock);
        return lavaBlock;

    }

    public int defaultDelay, currentDelay;

    @Override
    public void onLivingUpdate() {
        if (!isInvisibleLock) {
            if (isServerWorld()) {
                if (currentDelay >= 0 && !isTeleport()) {
                    if (isInvisible()) {
                        currentDelay -= 1;
                    } else
                        currentDelay--;
                }
            }
            if (currentDelay <= 0) {
                this.setInvisible(!isInvisible());
                currentDelay = defaultDelay;
            }
        } else {
            this.setInvisible(false);
        }
        super.onLivingUpdate();
    }

    @Override
    public boolean handleWaterMovement() {
        return false;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("defaultDelay", defaultDelay);
        compound.setInteger("currentDelay", currentDelay);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        currentDelay = compound.getInteger("currentDelay");
        defaultDelay = compound.getInteger("defaultDelay");
    }
}
