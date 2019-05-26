package map.escaperoom.nouse;

import map.lopre2.EntityPreBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import olib.api.WorldAPI;

//점프맵 제작용 블럭
public class EntityRoomBlockJumpMap extends EntityPreBlock {
    public static int jump_count;
    public static boolean isTeleport;//휠 돌려서 ax 값을 증가시키기 위한 코드임
    public EntityRoomBlockJumpMap(World worldIn) {
        super(worldIn);
        this.setCollision(true);
        setBlockMode(Blocks.QUARTZ_BLOCK);
        setJumpName("퍼즐 블럭");
        setTeleportLock(true);
        this.setRotate(0, 0, 180);
        isFly = true;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }


    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getEntity() != null && source.getEntity().isSneaking()) {
            this.setDead();
            System.out.println("사라진 블럭 좌표 " + this.getSpawnX() + ", " + this.getSpawnY() + ", " + this.getSpawnZ());
        }
        return false;
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (hand == EnumHand.MAIN_HAND) {
            if (player.isSneaking()) {
                setTeleport(true);
                isTeleport = true;
            }
        }
        return super.processInteract(player, hand, stack);
    }

    @Override
    public EntityPreBlock spawn(double x, double y, double z) {
        if(jump_count >= 10){
            EntityRoomBlockJumpMap lavaBlock = new EntityRoomBlockJumpMap(worldObj);
            WorldAPI.addMessage("더이상 블럭을 만들 수 없습니다.");
            return lavaBlock;
        }

        EntityRoomBlockJumpMap lavaBlock = new EntityRoomBlockJumpMap(worldObj);
        dataCopy(lavaBlock, x, y, z);
        if (isServerWorld() || canForceSpawn()) {
            worldObj.spawnEntityInWorld(lavaBlock);
        }
        lavaBlock.setBlock(getCurrentBlock());
        jump_count= jump_count+1;
        return lavaBlock;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("JumpCount", jump_count);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        jump_count = (compound.getInteger("JumpCount"));
    }
}
