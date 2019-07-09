package map.escaperoom;

import map.lopre2.CommandJB;
import map.lopre2.EntityPreBlock;
import map.lopre2.ItemCopy;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;
import olib.api.RenderAPI;
import olib.api.WorldAPI;
import olib.map.TypeModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

/**
 * 들 수 있는 블럭
 * 블럭을 버튼에 던지면 명령어 실행시키는 기능 있음
 * 에메랄드 블럭일 떄 엔티티를 맞추면 밀쳐내기 가능
 */
public class EntityRoomBlock extends EntityPreBlock {
    private static final DataParameter<Boolean> THROW_STATE = EntityDataManager.createKey(EntityRoomBlock.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> THROW_TIME = EntityDataManager.createKey(EntityRoomBlock.class, DataSerializers.VARINT);

    private static final DataParameter<Boolean> PLACE_STATE = EntityDataManager.createKey(EntityRoomBlock.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> PLACE_TIME = EntityDataManager.createKey(EntityRoomBlock.class, DataSerializers.VARINT);

    private static final DataParameter<Boolean> FORCE_FLY = EntityDataManager.createKey(EntityRoomBlock.class, DataSerializers.BOOLEAN);

    public EntityRoomBlock(World worldIn) {
        super(worldIn);
        this.setCollision(true);
        setBlockMode(Blocks.QUARTZ_BLOCK);
        setJumpName("퍼즐 블럭");
        setTeleportLock(false);
        this.setModel(TypeModel.SHAPE_BLOCK);
        isFly = false;
    }


    @Override
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        if (entityIn instanceof EntityPreBlock) {
            setVelocity(0, 0, 0);
        }
        if (entityIn instanceof EntityRoomBlockButton) {
            EntityRoomBlockButton puzzleBlock = (EntityRoomBlockButton) entityIn;
            puzzleBlock.runCommand (this);
            playSound(SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, 1F, 1);
        }
        return super.getCollisionBox(entityIn);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(THROW_STATE, false);
        dataManager.register(THROW_TIME, 0);
        dataManager.register(PLACE_STATE, false);
        dataManager.register(PLACE_TIME, 0);
        dataManager.register(FORCE_FLY, false);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        if (this.getCurrentBlock() == Blocks.EMERALD_BLOCK) {
            ((EntityLivingBase) entityIn).knockBack(this, 1.4F, this.posX - entityIn.posX, this.posZ - entityIn.posZ);
        }
        if (entityIn instanceof EntityPreBlock)
            setVelocity(0, 0, 0);
        super.collideWithEntity(entityIn);

    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (hand == EnumHand.MAIN_HAND && isServerWorld()) {
            if (this.getName().equalsIgnoreCase("entity.PuzzleMap.PuzzleBlock.name")) {
                if (WorldAPI.equalsHeldItem(player, Items.FEATHER)) {
                    setForceFly(!isFly);
                    System.out.println("플라이 " + isFly);
                    return super.processInteract(player, hand, stack);
                }
                if(!CommandJB.isDebMode && isForceFly())
                {
                    return false;
                }
                if (player.isSneaking() && !isTeleport()) {
                    setTeleport(true);
                    this.setTransparency(0.5F);
                } else if(isTeleport()){
                    setPosition(posX, posY, posZ);
                    setTeleport(false);
                    teleportEnd();
                    System.out.println("텔포 해제");
                }
            }
        }
        return super.processInteract(player, hand, stack);
    }

    @Override
    public void teleport() {
        super.teleport();
        isFly = true;
        setCollision(false);
    }

    @Override
    public void teleportEnd() {
        super.teleportEnd();
        setTransparency(1F);
        isFly = isForceFly();

        setCollision(true);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getEntity() instanceof EntityPlayer && isTeleport() && source.getEntity() != null && !source.getEntity().isSneaking()) {
            this.setTeleport(false);
            this.addVelocity(source.getEntity().getLookVec().xCoord * 3, source.getEntity().getLookVec().yCoord * 1.5, source.getEntity().getLookVec().zCoord * 3);
            dataManager.set(THROW_STATE, true);
            setTeleportLock(false);
            teleportEnd();
        }
        if (source == DamageSource.fallingBlock) {
            setHealth(getHealth() - amount);
            EntityRoomBlock roomBlock = new EntityRoomBlock(worldObj);
            roomBlock.setPosition(this.getSpawnPosVec());
            this.setHealth(roomBlock.getMaxHealth());
            NBTTagCompound tagCompound = new NBTTagCompound();
            this.writeEntityToNBT(tagCompound);
            roomBlock.readEntityFromNBT(tagCompound);
            this.worldObj.spawnEntityInWorld(roomBlock);
            return super.attackEntityFrom(source, amount);
        }
        return super.attackEntityFrom(source, amount);
    }

    public boolean isForceFly(){
        return dataManager.get(FORCE_FLY);
    }

    public void setForceFly(boolean fly){
        isFly = fly;
        dataManager.set(FORCE_FLY, isFly);
    }
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        //나무버튼이거나 텔포 중에는 떨어지지 않게 함

        if (dataManager.get(THROW_STATE)) {
            addThrowTime();
            if (getThrowTime() > 100) {
                teleportSpawnPos();
                setTeleportLock(true);
                dataManager.set(THROW_STATE, false);
                dataManager.set(THROW_TIME, 0);
                System.out.println(isServerWorld() + "원래자리로 돌아옴");
            }
        }

        if (dataManager.get(PLACE_STATE)) {
            addPlaceTime();
            if (getPlaceTime() > 300) {
                teleportSpawnPos();
                setTeleportLock(true);
                dataManager.set(PLACE_STATE, false);
                dataManager.set(PLACE_TIME, 0);
                System.out.println(isServerWorld() + "원래자리로 돌아옴");
            }
        }
        if(getCurrentBlock() == Blocks.SPONGE && isServerWorld()) {
            BlockPos pos = getPosition();
            while (checkLava(pos)) {
                worldObj.setBlockState(pos, Blocks.AIR.getDefaultState());
                for(int i = -3; i < 3;i++) {
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


        if (getCurrentBlock() == Blocks.EMERALD_BLOCK) {
            double knockbackSize = 1.5D;
            List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(
                    this.posX - knockbackSize, this.posY, this.posZ - knockbackSize, this.posX + knockbackSize, this.posY + 1.5, this.posZ + knockbackSize));
            if (!list.isEmpty()) {
                for (Entity entity : list) {
                    if ((entity instanceof EntityRoomBlock) && !entity.noClip) {
                        ((EntityLivingBase) entity).knockBack(this, 1.4F, this.posX - entity.posX, this.posZ - entity.posZ);
                    }
                }
            }
        }
    }

    public boolean checkLava(BlockPos pos){
        return worldObj.getBlockState(pos).getBlock() == Blocks.LAVA || worldObj.getBlockState(pos).getBlock() == Blocks.FLOWING_LAVA;

    }
    @Override
    public void setBlock(ItemStack stack) {
        super.setBlock(stack);
        setTexture(RenderAPI.getBlockTexture(((ItemBlock) stack.getItem()).block));
    }


    public void addThrowTime() {
        dataManager.set(THROW_TIME, dataManager.get(THROW_TIME) + 1);
    }

    public int getThrowTime() {
        return dataManager.get(THROW_TIME);
    }

    public void addPlaceTime() {
        dataManager.set(PLACE_TIME, dataManager.get(PLACE_TIME) + 1);
    }

    public int getPlaceTime() {
        return dataManager.get(PLACE_TIME);
    }

    @Override
    public boolean handleWaterMovement() {
        return false;
    }

    @Override
    public EntityPreBlock spawn(double x, double y, double z) {
        EntityRoomBlock lavaBlock = new EntityRoomBlock(worldObj);
        dataCopy(lavaBlock, x, y, z);
        if (isServerWorld() || canForceSpawn()) {
            worldObj.spawnEntityInWorld(lavaBlock);
        }
        lavaBlock.setBlock(getCurrentBlock());
        lavaBlock.setForceFly(isFly);
        System.out.println(lavaBlock.isFly);
        return lavaBlock;
    }


    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("delay", getThrowTime());
        compound.setBoolean("ThrowState", dataManager.get(THROW_STATE));
        compound.setBoolean("isForceFly", isForceFly());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        dataManager.set(THROW_TIME, compound.getInteger("delay"));
        dataManager.set(THROW_STATE, compound.getBoolean("ThrowState"));
        System.out.println("RR  블럭" + getCurrentBlock() + typeModel + getTexture());
        setForceFly(compound.getBoolean("isForceFly"));
    }

    @Override
    public void setPositionAndUpdate(double x, double y, double z) {
        super.setPositionAndUpdate(x, y, z);
    }
}
