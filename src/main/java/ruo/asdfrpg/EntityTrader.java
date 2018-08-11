package ruo.asdfrpg;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ruo.minigame.api.PosHelper;
import ruo.minigame.api.SpawnDirection;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.map.EntityDefaultNPC;

import javax.annotation.Nullable;

public class EntityTrader extends EntityDefaultNPC {
    private BlockPos chestPos;
    public EntityTrader(World worldIn) {
        super(worldIn);
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
        sellToTrader();
        return super.processInteract(player, hand, stack);
    }

    public void findChest(){
        WorldAPI.blockTick(worldObj, posX - 3, posX + 3, posY - 3, posY + 3, posZ - 3, posZ + 3, new AbstractTick.BlockXYZ() {
            @Override
            public void run(TickEvent.Type type) {
                if(getBlock() == Blocks.CHEST){
                    chestPos = getPos();
                }
            }
        });
        if(chestPos == null)
        {
            addChat(0, "왜 상자가 없어졌지?");
            PosHelper posHelper = new PosHelper(this);
            chestPos = new BlockPos(posHelper.getXZ(SpawnDirection.RIGHT, 1, true));
            this.worldObj.setBlockState(chestPos, Blocks.CHEST.getDefaultState());
        }
    }

    /**
     * 상인에게 아이템을 판매함
     */
    public void sellToTrader(){
        if(chestPos == null || !(worldObj.getTileEntity(chestPos) instanceof TileEntityChest))
            findChest();
        TileEntityChest entityChest = (TileEntityChest) worldObj.getTileEntity(chestPos);
        int itemSize = 0;
        for(int i = 0; i < entityChest.getSizeInventory();i++){
            if(entityChest.getStackInSlot(i) != null)
                itemSize++;
        }
        System.out.println(itemSize+"의 아이템이 존재함");
        entityChest.clear();
    }
}
