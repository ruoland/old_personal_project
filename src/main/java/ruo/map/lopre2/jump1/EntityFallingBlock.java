package ruo.map.lopre2.jump1;

import net.minecraft.block.Block;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import ruo.map.lopre2.CommandJB;
import ruo.map.lopre2.EntityPreBlock;

//반복해서 떨어지는 블럭 - 6월 21일
//1탄에서 용암에 떨어지는 블럭으로 쓰임
//용암에 들어서는 경우 천천히 용암속으로 사라짐

public class EntityFallingBlock extends EntityPreBlock {
    public int lavaY;

    public EntityFallingBlock(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.STONE);
        this.setCollision(true);
    }

    @Override
    public EntityFallingBlock spawn(double x, double y, double z) {
        EntityFallingBlock lavaBlock = new EntityFallingBlock(worldObj);
        lavaBlock.setSpawnXYZ(x, y, z);
        lavaBlock.setTeleport(false);
        lavaBlock.setPosition(lavaBlock.getSpawnX(), lavaBlock.getSpawnY() + 0.3, lavaBlock.getSpawnZ());
        lavaBlock.setPositionAndRotationDirect(lavaBlock.getSpawnX(), lavaBlock.getSpawnY() + 0.3,
                lavaBlock.getSpawnZ(), 90, 90, 1, false);
        this.copyModel(lavaBlock);
        lavaBlock.prevBlock = prevBlock;
        lavaBlock.setBlockMode(getCurrentBlock());
        worldObj.spawnEntityInWorld(lavaBlock);
        return lavaBlock;
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        if (lavaY == 0)
            for (int i = 0; i < 70; i++) {
                Block block = worldObj.getBlockState(new BlockPos(getSpawnX(), getSpawnY() - i, getSpawnZ()))
                        .getBlock();
                if (block == Blocks.LAVA) {
                    lavaY = (int) getSpawnY() - i;
                    break;
                }
            }
        return super.onInitialSpawn(difficulty, livingdata);
    }


    @Override
    public String getCustomNameTag() {
        return "폴링 블럭" + lavaY;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (isInLava()) {
            if (lavaY == 0)
                lavaY = MathHelper.floor_double(posY);
            motionX = 0;
            motionY = -0.004;
            motionZ = 0;
        }
        if (lavaY - 0.2 > posY) {
            this.setPositionAndUpdate(posX, getSpawnY(), posZ);
        }
    }

    @Override
    public void teleportEnd() {
        super.teleportEnd();
        for (int i = 0; i < 70; i++) {
            Block block = worldObj.getBlockState(new BlockPos(getSpawnX(), getSpawnY() - i, getSpawnZ()))
                    .getBlock();
            if (block == Blocks.LAVA) {
                lavaY = (int) getSpawnY() - i;
                break;
            }
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("lavaY", lavaY);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        lavaY = compound.getInteger("lavaY");
    }

}
