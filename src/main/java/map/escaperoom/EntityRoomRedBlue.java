package map.escaperoom;

import map.lopre2.EntityPreBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import olib.api.WorldAPI;

public class EntityRoomRedBlue extends EntityRoomBlock {
    public EntityRoomRedBlue(World worldIn) {
        super(worldIn);
        setBlock(Blocks.REDSTONE_BLOCK);
        isFly = true;
    }

    @Override
    public void applyEntityCollision(Entity entityIn) {
        //super.applyEntityCollision(entityIn);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        //super.collideWithEntity(entityIn);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (hand == EnumHand.MAIN_HAND && isServerWorld()) {
            if (WorldAPI.equalsHeldItem(player, Items.FEATHER))
            {
                setForceFly(!isFly);
                System.out.println("플라이 "+isFly);

                return super.processInteract(player, hand, stack);
            }
            if (WorldAPI.equalsHeldItem(player, Items.BRICK)) {
                setCollision(!canCollision());
                System.out.println("" + canCollision());
                return super.processInteract(player, hand, stack);
            }
        }
        if (getCurrentBlock() == Blocks.LAPIS_BLOCK) {
            setBlock(Blocks.LAPIS_BLOCK);
        } else if (getCurrentBlock() == Blocks.REDSTONE_BLOCK)
            setBlock(Blocks.REDSTONE_BLOCK);

        return super.processInteract(player, hand, stack);
    }

    @Override
    public EntityPreBlock spawn(double x, double y, double z) {
        EntityRoomRedBlue movingBlock = new EntityRoomRedBlue(worldObj);
        dataCopy(movingBlock, x, y, z);
        movingBlock.setForceFly(isFly);
        if (isServerWorld() || canForceSpawn()) {
            worldObj.spawnEntityInWorld(movingBlock);
        }
        return movingBlock;
    }
}
