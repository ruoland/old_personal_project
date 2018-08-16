package ruo.minigame.map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Rotations;
import net.minecraft.world.World;
import ruo.minigame.api.RenderAPI;
import ruo.minigame.api.WorldAPI;

import java.io.File;
import java.util.Random;


class EntityModelNPC extends EntityMob {
    private static final DataParameter<Boolean> IS_ELYTRA = EntityDataManager.createKey(EntityModelNPC.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_SIT = EntityDataManager.createKey(EntityModelNPC.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_SLEEP = EntityDataManager.createKey(EntityModelNPC.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Float> SLEEP_ROTATE = EntityDataManager.createKey(EntityModelNPC.class,
            DataSerializers.FLOAT);
    private static final DataParameter<Boolean> IS_CHILD = EntityDataManager.createKey(EntityModelNPC.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> BLOCK_ID = EntityDataManager.createKey(EntityModelNPC.class,
            DataSerializers.VARINT);
    private static final DataParameter<Integer> BLOCK_METADATA = EntityDataManager.createKey(EntityModelNPC.class,
            DataSerializers.VARINT);
    private static final DataParameter<Rotations> ROTATION_XYZ = EntityDataManager.createKey(EntityModelNPC.class,
            DataSerializers.ROTATIONS);
    private static final DataParameter<Rotations> SCALE_XYZ = EntityDataManager.createKey(EntityModelNPC.class,
            DataSerializers.ROTATIONS);
    private static final DataParameter<Rotations> TRANSLATION_XYZ = EntityDataManager.createKey(EntityModelNPC.class,
            DataSerializers.ROTATIONS);

    private static final DataParameter<Float> TRANSPARENCY = EntityDataManager.createKey(EntityModelNPC.class,
            DataSerializers.FLOAT);
    private static final DataParameter<Rotations> RGB_COLOR = EntityDataManager.createKey(EntityModelNPC.class,
            DataSerializers.ROTATIONS);

    protected TypeModel typeModel = TypeModel.NPC;
    private ModelBase customModel;
    private ResourceLocation texture;
    private RenderDefaultNPC asdfasdgasdgas;//렌더 디폴트 엔피씨 클래스 이동용

    public EntityModelNPC(World world) {
        super(world);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(ROTATION_XYZ, new Rotations(0, 0, 0));
        this.dataManager.register(TRANSLATION_XYZ, new Rotations(0, 0, 0));
        this.dataManager.register(SCALE_XYZ, new Rotations(1, 1, 1));
        this.dataManager.register(RGB_COLOR, new Rotations(1, 1, 1));
        this.dataManager.register(TRANSPARENCY, 1F);
        this.dataManager.register(IS_ELYTRA, false);
        this.dataManager.register(IS_SIT, false);
        this.dataManager.register(IS_CHILD, false);
        this.dataManager.register(IS_SLEEP, false);
        this.dataManager.register(SLEEP_ROTATE, 0F);
        this.dataManager.register(BLOCK_ID, 1);
        this.dataManager.register(BLOCK_METADATA, 0);
    }

    public Rotations getRGBColor() {
        return getDataManager().get(RGB_COLOR);
    }

    public float getRed() {
        return getRGBColor().getX();
    }

    public float getGreen() {
        return getRGBColor().getY();
    }

    public float getBlue() {
        return getRGBColor().getZ();
    }

    public void addRGB(float red, float green, float blue) {
        setRGB(getRed() + red, getGreen() + green, getBlue() + blue);
    }

    public void setRGB(float red, float green, float blue) {
        getDataManager().set(RGB_COLOR, new Rotations(red, green, blue));
    }

    public void addTransparency(float a) {
        getDataManager().set(TRANSPARENCY, getTransparency() + a);
    }

    public void setTransparency(float value) {
        getDataManager().set(TRANSPARENCY, value);
    }

    public float getTransparency() {
        return getDataManager().get(TRANSPARENCY);
    }

    //Rotate
    public Rotations getRotationXYZ() {
        return getDataManager().get(ROTATION_XYZ);
    }

    public float getRotateX() {
        return getRotationXYZ().getX();
    }

    public float getRotateY() {
        return getRotationXYZ().getY();
    }

    public float getRotateZ() {
        return getRotationXYZ().getZ();
    }


    public void setRotate(float x, float y, float z) {
        getDataManager().set(ROTATION_XYZ, new Rotations(x, y, z));
    }

    public void addRotate(float x, float y, float z) {
        setRotate(getRotateX() + x, getRotateY() + y, getRotateZ() + z);
    }

    //Scale
    public Rotations getScaleXYZ() {
        return getDataManager().get(SCALE_XYZ);
    }

    public void addScale(float value) {
        setScale(getScaleX() + value, getScaleY() + value, getScaleZ() + value);
    }

    public void addScale(float x, float y, float z) {
        setScale(getScaleX() + x, getScaleY() + y, getScaleZ() + z);
    }

    public void setScale(float x, float y, float z) {
        getDataManager().set(SCALE_XYZ, new Rotations(x, y, z));
    }

    public float getScaleX() {
        return getScaleXYZ().getX();
    }

    public float getScaleY() {
        return getScaleXYZ().getY();
    }

    public float getScaleZ() {
        return getScaleXYZ().getZ();
    }

    //Translate
    public Rotations getTraXYZ() {
        return getDataManager().get(TRANSLATION_XYZ);
    }

    public float getTraX() {
        return getTraXYZ().getX();
    }

    public float getTraY() {
        return getTraXYZ().getY();
    }

    public float getTraZ() {
        return getTraXYZ().getZ();
    }

    public void addTraXYZ(float x, float y, float z) {
        setTra(getTraX() + x, getTraY() + y, getTraZ() + z);
    }

    public void setTra(float x, float y, float z) {
        getDataManager().set(TRANSLATION_XYZ, new Rotations(x, y, z));
    }

    //엔피씨 모델
    public TypeModel getModel() {
        return typeModel;
    }

    public ModelBase getCustomModel() {
        return customModel;
    }

    public void setCustomModel(ModelBase customModel) {
        this.customModel = customModel;
    }

    private ItemStack currentStack = new ItemStack(Blocks.STONE);

    private int currentBlockID() {
        return getDataManager().get(BLOCK_ID);
    }

    public ItemStack getCurrentStack() {
        Block currentBlockStack = Block.getBlockFromItem(currentStack.getItem());
        Block currentBlock = Block.getBlockById(currentBlockID());
        if (currentBlock != currentBlockStack)
            currentStack = new ItemStack(getCurrentBlock(), 1, dataManager.get(BLOCK_METADATA));
        if (currentStack == null)
            currentStack = new ItemStack(Blocks.STONE);
        return currentStack;
    }

    public Block getCurrentBlock() {
        return Block.getBlockById(currentBlockID());
    }

    public void setBlockMode(Block block) {
        setBlockMode(new ItemStack(block, 1, 0));
    }

    public void setBlockMode(ItemStack stack) {
        this.setSize(1F, 1F);
        setModel(TypeModel.BLOCK);
        setBlockMetadata(stack.getMetadata());
        setTexture(RenderAPI.getBlockTexture(((ItemBlock) stack.getItem()).getBlock()));
        this.getDataManager().set(BLOCK_ID, Block.getIdFromBlock(Block.getBlockFromItem(stack.getItem())));
    }

    public void setBlock(Block block) {
        setBlock(new ItemStack(block));
    }

    public void setBlock(ItemStack stack) {
        this.getDataManager().set(BLOCK_ID, Block.getIdFromBlock(Block.getBlockFromItem(stack.getItem())));
        setBlockMetadata(stack.getMetadata());
        setTexture(RenderAPI.getBlockTexture(((ItemBlock) stack.getItem()).getBlock()));
    }

    public void setBlockMetadata(int metadata) {
        this.getDataManager().set(BLOCK_METADATA, metadata);
    }

    public int getBlockMetadata() {
        return this.getDataManager().get(BLOCK_METADATA);
    }

    /**
     * 모델을 바꾸면 모델 회전이나 이동은 모두 초기화 됨
     *
     * @param model
     */
    public void setModel(TypeModel model) {
        this.typeModel = model;
    }

    public void setTexture(String tex) {
        texture = new ResourceLocation(tex);
    }

    public void setTexture(ResourceLocation tex) {
        texture = tex;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public ResourceLocation getPlayerSkin(boolean isRandom) {
        ResourceLocation texture;
        texture = Minecraft.getMinecraft().thePlayer.getLocationSkin(WorldAPI.getPlayer().getName());
        if (isRandom) {
            File f = new File("./assets/skins/");
            if (f.listFiles() != null) {
                File skinfolder = f.listFiles()[new Random().nextInt(f.listFiles().length - 1)];
                File skin = skinfolder.listFiles()[0];
                texture = new ResourceLocation("skins/" + skinfolder.getName() + "/" + skin.getName());
            }
        }
        return texture;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setTag("SCALEXYZ", getScaleXYZ().writeToNBT());
        compound.setTag("ROTATIONXYZ", getRotationXYZ().writeToNBT());
        compound.setTag("TRANSLATIONXYZ", getTraXYZ().writeToNBT());
        compound.setTag("RGBCOLOR", getRGBColor().writeToNBT());
        compound.setBoolean("ISCHILD", isChild());
        compound.setBoolean("ISSLEEP", isSleep());
        compound.setFloat("SLEEPROTATE", getSleepRotate());
        compound.setBoolean("ISELYTRA", isElytra());
        compound.setBoolean("ISSIT", isSit());
        compound.setString("MODELTYPE", typeModel.name());
        compound.setString("texture", getTexture() != null ? getTexture().toString() : "");
        compound.setInteger("BlockID", Block.getIdFromBlock(getCurrentBlock()));
        compound.setInteger("BlockMetadata", dataManager.get(BLOCK_METADATA));
        if(getCurrentBlock() != null)
            compound.setString("blockTexture", RenderAPI.getBlockTexture(getCurrentBlock()).toString());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        getDataManager().set(SCALE_XYZ, getRotations(compound, "SCALEXYZ"));
        getDataManager().set(ROTATION_XYZ, getRotations(compound, "ROTATIONXYZ"));
        getDataManager().set(TRANSLATION_XYZ, getRotations(compound, "TRANSLATIONXYZ"));
        getDataManager().set(RGB_COLOR, getRotations(compound, "RGBCOLOR"));
        setChild(compound.getBoolean("ISCHILD"));
        setSleep(compound.getBoolean("ISSLEEP"), (int) compound.getFloat("SLEEPROTATE"));
        setElytra(compound.getBoolean("ISELYTRA"));
        setSit(compound.getBoolean("ISSIT"));
        if(compound.hasKey("MODELTYPE"))
        this.typeModel = TypeModel.valueOf(compound.getString("MODELTYPE"));
        if (!compound.getString("texture").equals(""))
            setTexture(compound.getString("texture"));
        if ((Block.getBlockById(compound.getInteger("BlockID")) != Blocks.AIR)) {
            setBlockMode(Block.getBlockById(compound.getInteger("BlockID")));
            setTexture(compound.getString("blockTexture"));
        }
        setBlockMetadata(compound.getInteger("BlockMetadata"));
    }

    public Rotations getRotations(NBTTagCompound tag, String name) {
        if (tag.hasKey(name)) {
            return new Rotations((NBTTagList) tag.getTag(name));
        } else
            return new Rotations(0, 0, 0);
    }

    /**
     * 이 엔피씨 모델 정보를 인자에 있는 엔피씨로 옮김
     *
     * @param npc
     */
    public void copyModel(EntityDefaultNPC npc) {
        npc.setTra(getTraX(), getTraY(), getTraZ());
        npc.setRotate(getRotateX(), getRotateY(), getRotateZ());
        npc.setScale(getScaleX(), getScaleY(), getScaleZ());
        npc.setTransparency(getTransparency());
        npc.setRGB(getRed(), getGreen(), getBlue());
    }

    public void setSleep(boolean isSleep, int rotate) {
        setSleep(isSleep);
        setSleepRotate(rotate);
    }

    public void setSleep(boolean isSleep, EnumFacing enumfacing) {
        setSleep(isSleep);
        switch (enumfacing) {
            case SOUTH:
                setSleepRotate(0);
            case NORTH:
                setSleepRotate(180);
            case WEST:
                setSleepRotate(270);
            case EAST:
                setSleepRotate(90);
            default:
                setSleepRotate(90);
        }
    }


    public boolean isSit() {
        return dataManager.get(IS_SIT);
    }

    public void setSit(boolean ely) {
        this.dataManager.set(IS_SIT, ely);
    }

    public boolean isElytra() {
        return dataManager.get(IS_ELYTRA);
    }

    public void setElytra(boolean ely) {
        this.setFlag(7, ely);
        this.dataManager.set(IS_ELYTRA, ely);
        if(!ely) {
            this.setFlag(7, true);
            this.setFlag(7, false);
        }
    }

    public void setChild(boolean is) {
        dataManager.set(IS_CHILD, is);
        this.setSize(0.3F, 0.8F);
    }

    public void setSleep(boolean isSleep) {
        dataManager.set(IS_SLEEP, isSleep);
    }

    public void setSleepRotate(float sl) {
        this.getDataManager().set(SLEEP_ROTATE, sl);
    }

    public float getSleepRotate() {
        return getDataManager().get(SLEEP_ROTATE);
    }

    public boolean isSleep() {
        return dataManager.get(IS_SLEEP).booleanValue();
    }

    public boolean isChild() {
        return dataManager.get(IS_CHILD).booleanValue();
    }

    protected void addRotate(double x, double y, double z) {
        addRotate((float)x,(float)y,(float)z);
    }
}
