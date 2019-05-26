package olib.map;

import olib.api.WorldAPI;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Blocks;
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

import java.io.File;
import java.util.HashMap;
import java.util.Random;


class EntityModelNPC extends EntityMob {
    private static final HashMap<EnumModel, Rotations> MODEL_ROTATIONS_MAP = new HashMap<>();

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
    private static final DataParameter<Integer> HEAD_BLOCK_ID = EntityDataManager.createKey(EntityModelNPC.class,
            DataSerializers.VARINT);
    private static final DataParameter<Integer> HEAD_BLOCK_METADATA = EntityDataManager.createKey(EntityModelNPC.class,
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

    private static final DataParameter<Rotations> ANGLE_ARM_LEFT = EntityDataManager.createKey(EntityModelNPC.class,
            DataSerializers.ROTATIONS);
    private static final DataParameter<Rotations> ANGLE_ARM_RIGHT = EntityDataManager.createKey(EntityModelNPC.class,
            DataSerializers.ROTATIONS);
    private static final DataParameter<Rotations> ANGLE_LEG_LEFT = EntityDataManager.createKey(EntityModelNPC.class,
            DataSerializers.ROTATIONS);
    private static final DataParameter<Rotations> ANGLE_LEG_RIGHT = EntityDataManager.createKey(EntityModelNPC.class,
            DataSerializers.ROTATIONS);
    private static final DataParameter<String> TEXTURE = EntityDataManager.createKey(EntityModelNPC.class, DataSerializers.STRING);
    protected TypeModel typeModel = TypeModel.NPC;
    private ModelBase customModel;
    private ResourceLocation texture;

    public EntityModelNPC(World world) {
        super(world);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(ANGLE_ARM_LEFT, putRotation(EnumModel.LEFT_ARM));
        this.dataManager.register(ANGLE_ARM_RIGHT, putRotation(EnumModel.RIGHT_ARM));
        this.dataManager.register(ANGLE_LEG_LEFT, putRotation(EnumModel.LEFT_LEG));
        this.dataManager.register(ANGLE_LEG_RIGHT, putRotation(EnumModel.RIGHT_LEG));
        this.dataManager.register(ROTATION_XYZ, putRotation(EnumModel.ROTATION));
        this.dataManager.register(TRANSLATION_XYZ, putRotation(EnumModel.TRANSLATION));
        this.dataManager.register(SCALE_XYZ, putRotation(EnumModel.SCALE, 1, 1, 1));
        this.dataManager.register(RGB_COLOR, putRotation(EnumModel.RGB, 255, 255, 255));
        this.dataManager.register(TRANSPARENCY, 1F);
        this.dataManager.register(IS_ELYTRA, false);
        this.dataManager.register(IS_SIT, false);
        this.dataManager.register(IS_CHILD, false);
        this.dataManager.register(IS_SLEEP, false);
        this.dataManager.register(SLEEP_ROTATE, 0F);
        this.dataManager.register(BLOCK_ID, 1);
        this.dataManager.register(BLOCK_METADATA, 0);
        this.dataManager.register(HEAD_BLOCK_ID, 1);
        this.dataManager.register(HEAD_BLOCK_METADATA, 0);
        this.dataManager.register(TEXTURE, "");
    }

    public Rotations putRotation(EnumModel model) {
        return putRotation(model, new Rotations(0, 0, 0));
    }

    public Rotations putRotation(EnumModel model, float x, float y, float z) {
        return putRotation(model, new Rotations(x, y, z));
    }

    public Rotations putRotation(EnumModel model, Rotations rotations) {
        MODEL_ROTATIONS_MAP.put(model, rotations);
        return rotations;
    }

    public Rotations getRGBColor() {
        return getDataManager().get(RGB_COLOR);
    }

    public Rotations getRotationXYZ() {
        return getDataManager().get(ROTATION_XYZ);
    }

    //Translate
    public Rotations getTraXYZ() {
        return getDataManager().get(TRANSLATION_XYZ);
    }

    public Rotations getScaleXYZ() {
        return dataManager.get(SCALE_XYZ);
    }

    public Rotations getAngleArmLeft() {
        return dataManager.get(ANGLE_ARM_LEFT);
    }

    public Rotations getAngleArmRight() {
        return dataManager.get(ANGLE_ARM_RIGHT);
    }

    public Rotations getAngleLegLeft() {
        return dataManager.get(ANGLE_LEG_LEFT);
    }

    public Rotations getAngleLegRight() {
        return dataManager.get(ANGLE_LEG_RIGHT);
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

    public Rotations getRotations(EnumModel model) {
        return MODEL_ROTATIONS_MAP.get(model);
    }

    public float getX(EnumModel model) {
        return MODEL_ROTATIONS_MAP.get(model).getX();
    }

    public float getY(EnumModel model) {
        return MODEL_ROTATIONS_MAP.get(model).getY();
    }

    public float getZ(EnumModel model) {
        return MODEL_ROTATIONS_MAP.get(model).getZ();
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
        setXYZ(EnumModel.ROTATION, x, y, z);
    }

    public void addRotate(float x, float y, float z) {
        setRotate(getRotateX() + x, getRotateY() + y, getRotateZ() + z);
    }

    public Rotations getXYZ(EnumModel model) {
        return MODEL_ROTATIONS_MAP.get(model);
    }

    public void printXYZ(EnumModel model) {
        System.out.println(model + " - " + getX(model) + " - " + getY(model) + " - " + getZ(model));
    }

    public void setXYZ(EnumModel model, float x, float y, float z) {
        putRotation(model);

        switch (model) {

            case SCALE:
                getDataManager().set(SCALE_XYZ, putRotation(EnumModel.SCALE, new Rotations(x, y, z)));
                return;
            case RGB:
                getDataManager().set(RGB_COLOR, putRotation(EnumModel.RGB, new Rotations(x, y, z)));
                return;
            case ROTATION:
                getDataManager().set(ROTATION_XYZ, putRotation(EnumModel.ROTATION, new Rotations(x, y, z)));
                return;
            case TRANSLATION:
                getDataManager().set(TRANSLATION_XYZ, putRotation(EnumModel.TRANSLATION, new Rotations(x, y, z)));
                return;
            default:
                System.out.println(model + "은 설정되지 않았습니다");
        }
    }

    public void addXYZ(EnumModel model, float x, float y, float z) {
        setXYZ(model, getX(model) + x, getY(model) + y, getZ(model) + z);
    }

    public void addXYZ(EnumModel model, float value) {
        setXYZ(model, getX(model) + value, getY(model) + value, getZ(model) + value);
    }

    public void addScale(float value) {
        setScale(getScaleX() + value, getScaleY() + value, getScaleZ() + value);
    }

    public void addScale(float x, float y, float z) {
        setScale(getScaleX() + x, getScaleY() + y, getScaleZ() + z);
    }

    public void setScale(float x, float y, float z) {
        setXYZ(EnumModel.SCALE, x, y, z);
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
        setXYZ(EnumModel.TRANSLATION, x, y, z);
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
        setXYZ(EnumModel.RGB, red, green, blue);

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
        setBlock(stack);
    }

    public void setBlock(Block block) {
        if(block != getCurrentBlock())
        setBlock(new ItemStack(block));
    }

    public void setBlock(ItemStack stack) {
        this.getDataManager().set(BLOCK_ID, Block.getIdFromBlock(Block.getBlockFromItem(stack.getItem())));
        setBlockMetadata(stack.getMetadata());
    }

    public void setBlockMetadata(int metadata) {
        this.getDataManager().set(BLOCK_METADATA, metadata);
    }

    public int getBlockMetadata() {
        return this.getDataManager().get(BLOCK_METADATA);
    }

    public void setHeadBlock(ItemStack stack) {
        dataManager.set(HEAD_BLOCK_METADATA, stack.getMetadata());
        dataManager.set(HEAD_BLOCK_ID, Block.getIdFromBlock(Block.getBlockFromItem(stack.getItem())));
    }

    public int getHeadBlockMetadata() {
        return this.getDataManager().get(HEAD_BLOCK_METADATA);
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
        setTexture(new ResourceLocation(tex));
    }

    public void setTexture(ResourceLocation tex) {
        dataManager.set(TEXTURE, tex.toString());
        texture = tex;
    }

    public ResourceLocation getTexture() {
        if (texture == null || !texture.toString().equalsIgnoreCase(dataManager.get(TEXTURE)))
            texture = new ResourceLocation(dataManager.get(TEXTURE));
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
        for (EnumModel model : EnumModel.values()) {
            if (MODEL_ROTATIONS_MAP.containsKey(model))
                compound.setTag(model.name(), MODEL_ROTATIONS_MAP.get(model).writeToNBT());
        }

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
        compound.setString("texture", getTexture().toString());
        compound.setInteger("BlockID", Block.getIdFromBlock(getCurrentBlock()));
        compound.setInteger("BlockMetadata", dataManager.get(BLOCK_METADATA));
        compound.setInteger("HEAD_BLOCK_ID", (dataManager.get(HEAD_BLOCK_ID)));
        compound.setInteger("HEAD_BLOCK_METADATA", dataManager.get(HEAD_BLOCK_METADATA));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        getDataManager().set(ANGLE_ARM_LEFT, getRotations(compound, "angleArmLeft"));
        getDataManager().set(ANGLE_ARM_RIGHT, getRotations(compound, "angleArmRight"));
        getDataManager().set(ANGLE_LEG_LEFT, getRotations(compound, "angleLegLeft"));
        getDataManager().set(ANGLE_LEG_RIGHT, getRotations(compound, "angleLegRight"));

        getDataManager().set(SCALE_XYZ, getRotations(compound, "SCALEXYZ"));
        getDataManager().set(ROTATION_XYZ, getRotations(compound, "ROTATIONXYZ"));
        getDataManager().set(TRANSLATION_XYZ, getRotations(compound, "TRANSLATIONXYZ"));
        getDataManager().set(RGB_COLOR, getRotations(compound, "RGBCOLOR"));
        setChild(compound.getBoolean("ISCHILD"));
        setSleep(compound.getBoolean("ISSLEEP"), (int) compound.getFloat("SLEEPROTATE"));
        setElytra(compound.getBoolean("ISELYTRA"));
        setSit(compound.getBoolean("ISSIT"));
        if (compound.hasKey("MODELTYPE"))
            this.typeModel = TypeModel.valueOf(compound.getString("MODELTYPE"));
        setTexture(compound.getString("texture"));
        if ((Block.getBlockById(compound.getInteger("BlockID")) != Blocks.AIR)) {
            setBlockMode(Block.getBlockById(compound.getInteger("BlockID")));

        }
        setBlockMetadata(compound.getInteger("BlockMetadata"));
        dataManager.set(HEAD_BLOCK_ID, compound.getInteger("HEAD_BLOCK_ID"));
        dataManager.set(HEAD_BLOCK_METADATA, compound.getInteger("HEAD_BLOCK_METADATA"));
    }

    public Block getHeadBlock() {
        return Block.getBlockById(dataManager.get(HEAD_BLOCK_ID));
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
        npc.setRGB(getRed(), getGreen(), getBlue());
        npc.setTransparency(getTransparency());
        npc.setAngles(rotationYaw, rotationPitch);
        npc.setTexture(getTexture());
        if (getCurrentBlock() != null)
            npc.setBlock(getCurrentBlock());
        npc.typeModel = typeModel;
    }

    public void printModel() {
        System.out.println("-------------------------------------------------------------------");
        System.out.println("TypeModel" + getModel());
        if (getModel() == TypeModel.BLOCK) {
            System.out.println("Block" + getCurrentBlock());
            System.out.println("Stack" + getCurrentStack());
            System.out.println("BlockID" + dataManager.get(BLOCK_ID));
        }
        System.out.println("Texture" + getTexture());
        printXYZ(EnumModel.TRANSLATION);
        printXYZ(EnumModel.SCALE);
        printXYZ(EnumModel.ROTATION);
        System.out.println("getTransparency" + getTransparency());
        printXYZ(EnumModel.RGB);
        System.out.println("-------------------------------------------------------------------");
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
                return;
            case NORTH:
                setSleepRotate(180);
                return;
            case WEST:
                setSleepRotate(270);
                return;
            case EAST:
                setSleepRotate(90);
                return;
            default:
                setSleepRotate(90);
                return;
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
        if (!ely) {
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
        addRotate((float) x, (float) y, (float) z);
    }
}
