package ruo.minigame.map;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import ruo.asdfrpg.skill.SkillHelper;
import ruo.asdfrpg.skill.Skills;
import ruo.cmplus.deb.DebAPI;
import ruo.halloween.miniween.EntityAttackMiniWeen;
import ruo.map.lopre2.CommandJB;
import ruo.map.lopre2.EntityBuildBlock;
import ruo.map.lopre2.EntityLavaBlock;
import ruo.map.lopre2.EntityPreBlock;
import ruo.map.lopre2.dummy.EntityBuildBlockMove;
import ruo.minigame.api.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class EntityDefaultBlock extends EntityDefaultNPC {
    //넉백된 상태
    private static final DataParameter<Boolean> IS_KNOCK_BACK = EntityDataManager.createKey(EntityDefaultBlock.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_TELEPORT = EntityDataManager.createKey(EntityDefaultBlock.class,
            DataSerializers.BOOLEAN);
    //넉백 가능한지 여부
    private static final DataParameter<Boolean> CAN_KNOCK_BACK = EntityDataManager.createKey(EntityDefaultBlock.class, DataSerializers.BOOLEAN);

    private static final DataParameter<String> ENTITY_SKIN = EntityDataManager.createKey(EntityDefaultBlock.class, DataSerializers.STRING);

    private ArrayList<BlockData> blockList = new ArrayList<>();
    private ResourceLocation blockTexture = new ResourceLocation("minecraft:textures/blocks/dirt.png");
    private Vec3d targetVec = null, targetPosition;
    public static double ax = 3;
    private double distance = 1;
    public EntityDefaultBlock(World worldIn) {
        super(worldIn);
        this.setSize(1, 1);
        this.setCollision(true);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(CAN_KNOCK_BACK, true);
        dataManager.register(IS_KNOCK_BACK, false);
        dataManager.register(IS_TELEPORT, false);
        dataManager.register(ENTITY_SKIN, "minecraft:textures/blocks/dirt.png");
    }

    public void teleport() {
        Vec3d vec = WorldAPI.getPlayer().getLookVec();
        double x = WorldAPI.x() + vec.xCoord * 3;
        double y = WorldAPI.y() + WorldAPI.getPlayer().getEyeHeight() + vec.yCoord * 3;
        double z = WorldAPI.z() + vec.zCoord * 3;

        this.setPositionAndRotationDirect(x, y, z, 90, 90, 0, true);
        this.setPosition(x, y, z);
        if (isServerWorld() && DebAPI.isKeyDown(Keyboard.KEY_K)) {
            EntityDefaultBlock defaultBlock = spawn(x, y, z);
            defaultBlock.setPosition(defaultBlock.getSpawnX(), defaultBlock.getSpawnY(), defaultBlock.getSpawnZ());
            this.setDead();
        }

    }


    @Override
    public void setBlock(Block block) {
        super.setBlock(block);
        blockTexture = RenderAPI.getBlockTexture(block);
        dataManager.set(ENTITY_SKIN, blockTexture.toString());
    }

    @Override
    public void setBlock(ItemStack stack) {
        super.setBlock(stack);
        blockTexture = RenderAPI.getBlockTexture(((ItemBlock) stack.getItem()).getBlock());
        System.out.println(blockTexture);
        dataManager.set(ENTITY_SKIN, blockTexture.toString());
    }

    public void addBlock(Block block, Vec3d vec3d) {
        blockList.add(new BlockData(Block.getIdFromBlock(block), vec3d.xCoord, vec3d.yCoord, vec3d.zCoord));
        blockTexture = RenderAPI.getBlockTexture(block);

    }

    public void addBlock(int block, Vec3d vec3d) {
        addBlock(Block.getBlockById(block), vec3d);
    }

    public void setTarget(Entity base) {
        setTarget(base.posX, base.posY, base.posZ);
    }

    public void setTarget(double x, double y, double z) {
        if (x == 0 && y == 0 && z == 0) {
            targetPosition = null;
            targetVec = null;
        } else {
            this.targetVec = new Vec3d(x - posX, y - posY, z - posZ).normalize().scale(0.5);
            this.targetPosition = new Vec3d(x, y, z);
        }
    }

    public EntityDefaultBlock spawn(double x, double y, double z) {
        EntityDefaultBlock defaultBlock = new EntityDefaultBlock(worldObj);
        defaultBlock.setSpawnXYZ(x, y, z);
        defaultBlock.setTeleport(false);
        defaultBlock.setPosition(defaultBlock.getSpawnX(), defaultBlock.getSpawnY(), defaultBlock.getSpawnZ());
        defaultBlock.setPositionAndRotationDirect(defaultBlock.getSpawnX(), defaultBlock.getSpawnY(), defaultBlock.getSpawnZ(), 90, 90, 0, false);
        defaultBlock.setBlockMode(getCurrentBlock());
        this.copyModel(defaultBlock);
        defaultBlock.setRotate(getRotateX(), getRotateY(), getRotateZ());
        defaultBlock.setBlockMetadata(getBlockMetadata());
        if (isServerWorld()) {
            worldObj.spawnEntityInWorld(defaultBlock);
        }
        defaultBlock.setPosition(defaultBlock.getSpawnX(), defaultBlock.getSpawnY(), defaultBlock.getSpawnZ());

        return defaultBlock;

    }

    public ResourceLocation getTexture() {
        if (!dataManager.get(ENTITY_SKIN).equalsIgnoreCase(blockTexture.toString()))
            blockTexture = new ResourceLocation(dataManager.get(ENTITY_SKIN));

        return blockTexture;
    }

    @Override
    public void onLivingUpdate() {
        if (WorldAPI.getPlayer() != null) {
            if (isTeleport()) {
                teleport();
            }
            if (targetVec != null) {
                setVelocity(targetVec);
                System.out.println(isTeleport() + " - " + targetVec + " - " + getDistance(targetPosition));
                if (getDistance(targetPosition) < distance) {
                    setTarget(0, 0, 0);
                    targetArrive();
                }
            }
            super.onLivingUpdate();
            this.rotationYaw = 0;
            this.renderYawOffset = 0;
            if (isKnockBack() && WorldAPI.getPlayer().getDistanceToEntity(this) > 25) {
                this.setDead();
                System.out.println("블럭이 멀리 날라가 사라짐");
            }
        }
    }

    public void targetArrive() {

    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
        if (hand == EnumHand.MAIN_HAND && isServerWorld()) {
            if (SkillHelper.getPlayerSkill(player).isRegister(Skills.BLOCK_GRAB))
                setTeleport(true);

        }
        if (player.isSneaking() && stack != null && stack.getItem() instanceof ItemBlock) {
            this.setBlock(stack);
        }
        return super.processInteract(player, hand, stack);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isTeleport() && source.getEntity() instanceof EntityPlayer) {
            setTeleport(false);
            if (canKnockBack()) {
                this.addVelocity(source.getEntity().getLookVec().xCoord * 4, source.getEntity().getLookVec().yCoord * 7, source.getEntity().getLookVec().zCoord * 4);
                dataManager.set(IS_KNOCK_BACK, true);
            }
        }
        if (source.getEntity() != null && source.getEntity().isSneaking()) {
            this.setDead();
            System.out.println("사라진 플랫블럭 좌표 " + this.getSpawnX() + ", " + this.getSpawnY() + ", " + this.getSpawnZ());
        }
        return true;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (targetPosition != null) {
            compound.setDouble("targetX", targetPosition.xCoord);
            compound.setDouble("targetY", targetPosition.yCoord);
            compound.setDouble("targetZ", targetPosition.zCoord);
        }
        compound.setDouble("distance", distance);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setTarget(compound.getDouble("targetX"), compound.getDouble("targetY"), compound.getDouble("targetZ"));
        setDistance(compound.getDouble("distance"));
    }

    @Override
    public void applyEntityCollision(Entity entityIn) {
    }

    @Override
    public void knockBack(Entity entityIn, float strenght, double xRatio, double zRatio) {
        //super.knockBack(entityIn, strenght, xRatio, zRatio);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        //super.collideWithEntity(entityIn);
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setTeleport(boolean a) {
        this.dataManager.set(IS_TELEPORT, a);
    }

    public boolean isTeleport() {
        return dataManager.get(IS_TELEPORT);
    }

    public void setCanKnockBack(boolean a) {
        dataManager.set(CAN_KNOCK_BACK, a);
    }

    public boolean canKnockBack() {
        return dataManager.get(CAN_KNOCK_BACK);
    }

    public void setKnockBack() {
        dataManager.set(IS_KNOCK_BACK, true);
    }

    public boolean isKnockBack() {
        return dataManager.get(IS_KNOCK_BACK);
    }

    public ArrayList<BlockData> getBlockList() {
        return blockList;
    }

    protected class BlockData {
        private int id;
        private double x, y, z;
        private ResourceLocation texture;

        public BlockData(int id, double x, double y, double z) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.z = z;
            texture = RenderAPI.getBlockTexture(getBlock());

        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getZ() {
            return z;
        }

        public Block getBlock(){
            return Block.getBlockById(id);
        }

        public ResourceLocation getTexture(){
            return texture;
        }
    }

}
