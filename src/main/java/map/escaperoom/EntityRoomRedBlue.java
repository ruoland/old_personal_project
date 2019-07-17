package map.escaperoom;

import map.lopre2.EntityPreBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import olib.api.WorldAPI;
import olib.map.TypeModel;

public class  EntityRoomRedBlue extends EntityRoomBlock {
    private static final DataParameter<Boolean> IS_SPONGE = EntityDataManager.createKey(EntityRoomRedBlue.class, DataSerializers.BOOLEAN);
    public EntityRoomRedBlue(World worldIn) {
        super(worldIn);
        setBlock(Blocks.REDSTONE_BLOCK);
        isFly = true;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(IS_SPONGE, false);
    }

    @Override
    public void applyEntityCollision(Entity entityIn) {
        //super.applyEntityCollision(entityIn);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        //super.collideWithEntity(entityIn);
    }

    public boolean isActive(){
        return canCollision();
    }
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(isActive() && dataManager.get(IS_SPONGE) && isServerWorld()) {
            BlockPos pos = getPosition();
            while (checkLava(pos)) {
                worldObj.setBlockState(pos, Blocks.AIR.getDefaultState());
                for (int i = -3; i < 3; i++) {
                    if (checkLava(pos.east(i))) {
                        worldObj.setBlockState(pos.east(i), Blocks.AIR.getDefaultState());
                    }

                    if (checkLava(pos.west(1))) {
                        worldObj.setBlockState(pos.west(i), Blocks.AIR.getDefaultState());
                    }
                }
                pos = pos.down(1);

                System.out.println("블럭 발견");
            }
        }
    }

    @Override
    public String getText() {
        return "빨강파랑 블럭입니다. /room rb 명령어로 빨강 모드나 파랑 모드로 바꿀 수 있는데 빨강 모드 상태에서는 파랑 블럭은 반투명 상태가 되며 밟을 수도 없습니다.";
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (hand == EnumHand.MAIN_HAND && isServerWorld()) {
            if(WorldAPI.equalsHeldItem(new ItemBlock(Blocks.SPONGE))){
                dataManager.set(IS_SPONGE, !dataManager.get(IS_SPONGE));
                System.out.println(dataManager.get(IS_SPONGE));
                return super.processInteract(player, hand, stack);
            }
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

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("isSponge", dataManager.get(IS_SPONGE));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        dataManager.set(IS_SPONGE , compound.getBoolean("isSponge"));
    }
}
