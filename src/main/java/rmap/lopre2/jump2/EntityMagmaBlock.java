package rmap.lopre2.jump2;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityMagmaBlock extends EntityBigBlock {
    public EntityMagmaBlock(World world){
        super(world);
        this.setSize(1,1);
        this.setScale(0.5F,1,0.5F);
        this.setBlock(new ItemStack(Blocks.MAGMA));
    }
    private int upDelay = 40;
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        upDelay --;
        if(getSpawnY()+5 > posY && upDelay == 0){
            addVelocity(0,0.003,0);
        }
        else if(getSpawnY()+5 < posY)//목적지 도달시
        {
            upDelay = 250;
        }
    }

    @Override
    public EntityMagmaBlock spawn(double x, double y, double z) {
        EntityMagmaBlock lavaBlock = new EntityMagmaBlock(worldObj);
        lavaBlock.setSpawnXYZ(x, y, z);
        lavaBlock.setTeleport(false);
        lavaBlock.setPosition(lavaBlock.getSpawnX(), lavaBlock.getSpawnY(), lavaBlock.getSpawnZ());

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
