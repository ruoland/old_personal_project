package map.hideandseek;

import minigameLib.map.EntityDefaultNPC;
import minigameLib.map.TypeModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class EntityGuardLoop extends EntityDefaultNPC {
    public static ArrayList<EntityGuardLoop> guardList = new ArrayList<EntityGuardLoop>();//사운드 이벤트에서 사용됨
    private BlockPos startPos, endPos;
    private EntityLivingBase traceEntity;
    public EntityGuardLoop(World worldIn) {
        super(worldIn);
        this.setHealth(10000);
        this.setAlwaysRenderNameTag(true);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(1, new EntityAIFindPlayer(this));
        this.tasks.addTask(1, new EntityAITracePlayer(this, 1.0F));
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!this.guardList.contains(this)) {
            guardList.add(this);
        }

    }

    public EntityLivingBase getTraceEntity() {
        return traceEntity;
    }

    public void setTraceEntity(EntityLivingBase traceEntity) {
        this.traceEntity = traceEntity;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (hand == EnumHand.MAIN_HAND && isServerWorld()) {
            this.setSleepRotate(getSleepRotate() + 90F);
            System.out.println(getSleepRotate());
            if (getModel() == TypeModel.ZOMBIE) {
                setModel(TypeModel.CREEPER);
            }
            if (getModel() == TypeModel.CREEPER) {
                setModel(TypeModel.ENDERMAN);
            }
            if (getModel() == TypeModel.ENDERMAN) {
                setModel(TypeModel.ZOMBIE);
            }
        }
        return super.processInteract(player, hand, stack);
    }

}
