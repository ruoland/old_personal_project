package ruo.map.lopre2.jump2;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntitySmallBlock extends EntityBigBlock {
    public EntitySmallBlock(World world){
        super(world);
        this.setSize(1,1);
        this.setScale(1,1,1);
        this.setCanFalling(true);
        this.setBlock(new ItemStack(Blocks.WOOL,1,11));
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
    }

    @Override
    public EntitySmallBlock spawn(double x, double y, double z) {
        EntitySmallBlock lavaBlock = new EntitySmallBlock(worldObj);
        lavaBlock.setCanFalling(canFalling());
        lavaBlock.setSpawnXYZ(x, y, z);
        lavaBlock.setTeleport(false);
        lavaBlock.setPosition(lavaBlock.getSpawnX(), lavaBlock.getSpawnY(), lavaBlock.getSpawnZ());
        lavaBlock.setPositionAndRotationDirect(lavaBlock.getSpawnX(), lavaBlock.getSpawnY(), lavaBlock.getSpawnZ(), 90, 90, 1, false);
        lavaBlock.setBlockMode(getCurrentStack());
        worldObj.spawnEntityInWorld(lavaBlock);
        this.copyModel(lavaBlock);
        return lavaBlock;
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        this.setTeleport(true);
        return super.onInitialSpawn(difficulty, livingdata);
    }
}
