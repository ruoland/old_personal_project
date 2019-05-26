package map.escaperoom;

import map.lopre2.EntityPreBlock;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityRoomRedBlue extends EntityRoomBlock {
    public EntityRoomRedBlue(World worldIn) {
        super(worldIn);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if ((EscapeRoom.isRedMode && getBlockMetadata() == 14) || (!EscapeRoom.isRedMode && getBlockMetadata() == 11)) {
            setTransparency(1F);
            setCollision(true);
        } else {
            setTransparency(0.5F);
            setCollision(false);
        }
    }

    @Override
    public EntityPreBlock spawn(double x, double y, double z) {
        EntityRoomRedBlue movingBlock = new EntityRoomRedBlue(worldObj);
        dataCopy(movingBlock, x,y,z);
        if (isServerWorld() || canForceSpawn()) {
            worldObj.spawnEntityInWorld(movingBlock);
        }
        return super.spawn(x, y, z);
    }
}
