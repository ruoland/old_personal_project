package minigameLib.minigame.minerun;

import map.lopre2.EntityPreBlock;
import map.lopre2.jump1.EntityFallingBlock;
import minigameLib.map.EntityDefaultNPC;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityMRFallingBlockCreator extends EntityMR {
    private static final DataParameter<Integer> FALLING_DELAY = EntityDataManager.createKey(EntityMRFallingBlockCreator.class, DataSerializers.VARINT);

    public EntityMRFallingBlockCreator(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.TNT);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(FALLING_DELAY, 0);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        dataManager.set(FALLING_DELAY, dataManager.get(FALLING_DELAY) + 1);
        if (dataManager.get(FALLING_DELAY) == 40) {
            EntityWarningBlock warningBlocks = new EntityWarningBlock(worldObj);
            float railY = 0;
            for (float i = 0; i < 70; i += 0.5) {
                Block block = worldObj.getBlockState(new BlockPos(getSpawnX(), posY - i, getSpawnZ()))
                        .getBlock();
                if (block == Blocks.RAIL) {
                    railY = (float) (posY - i);

                    break;
                }
            }
            warningBlocks.setPosition(posX, railY, posZ);
            warningBlocks.setSpawnXYZ(posX, railY, posZ);
            if (isServerWorld())
                worldObj.spawnEntityInWorld(warningBlocks);
            warningBlocks.setDeathTimer(40);

        }

        if (dataManager.get(FALLING_DELAY) > 120) {
            dataManager.set(FALLING_DELAY, 0);
            EntityMRFallingBlock defaultNPC = new EntityMRFallingBlock(worldObj);
            defaultNPC.setBlockMode(Blocks.STONE);
            defaultNPC.setPosition(posX, posY, posZ);
            if (isServerWorld())
                worldObj.spawnEntityInWorld(defaultNPC);
            defaultNPC.setDeathTimer(60);
        }
    }
}
