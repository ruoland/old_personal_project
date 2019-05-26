package map.escaperoom;

import map.lopre2.EntityPreBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import olib.api.EntityAPI;

//플레이어가 보는 방향으로 움직이는 블럭
public class EntityRoomMovingBlock extends EntityRoomBlock{
    public EntityRoomMovingBlock(World worldIn) {
        super(worldIn);
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        player.startRiding(this);
        return super.processInteract(player, hand, stack);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(!getPassengers().isEmpty()){
            EntityPlayer player = (EntityPlayer) getPassengers().get(0);
            this.moveEntity(EntityAPI.lookX(player, 0.2), 0 , EntityAPI.lookZ(player, 0.2));
        }
    }

    @Override
    public EntityPreBlock spawn(double x, double y, double z) {
        EntityRoomMovingBlock movingBlock = new EntityRoomMovingBlock(worldObj);
        dataCopy(movingBlock, x,y,z);
        if (isServerWorld() || canForceSpawn()) {
            worldObj.spawnEntityInWorld(movingBlock);
        }
        return super.spawn(x, y, z);
    }
}
