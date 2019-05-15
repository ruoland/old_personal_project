package minigameLib.minigame.minerun;

import cmplus.deb.DebAPI;
import map.lopre2.EntityPreBlock;
import map.lopre2.jump1.EntityLavaBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import oneline.map.EntityDefaultNPC;
import oneline.map.TypeModel;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class EntityMineRunner extends EntityDefaultNPC {
    private static final DataParameter<Boolean> isCollision = EntityDataManager.createKey(EntityMineRunner.class, DataSerializers.BOOLEAN);
    public EntityMineRunner(World worldIn) {
        super(worldIn);
        this.setModel(TypeModel.NPC);
        this.setCollision(true);
    }

    @Override
    public boolean canRenderOnFire() {
        return false;
    }

    @Override
    public void jump() {
        super.jump();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(isCollision, false);
    }

    @Override
    protected void doBlockCollisions() {
        super.doBlockCollisions();
        dataManager.set(isCollision, true);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        extinguish();
    }

    @Override
    public void knockBack(Entity entityIn, float strenght, double xRatio, double zRatio) {
        //super.knockBack(entityIn, strenght, xRatio, zRatio);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public boolean isOnLadder() {
        return super.isOnLadder();
    }

    public void updatePos(){
        dataManager.set(isCollision, false);
    }
}
