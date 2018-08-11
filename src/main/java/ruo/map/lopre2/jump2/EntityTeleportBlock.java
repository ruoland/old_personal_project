package ruo.map.lopre2.jump2;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import ruo.map.lopre2.EntityPreBlock;
import ruo.minigame.api.PosHelper;
import ruo.minigame.api.SpawnDirection;
import ruo.minigame.api.WorldAPI;

public class EntityTeleportBlock extends EntityPreBlock {
    private int moveXZ = 0, timeDelay, moveDelay;
    private double moveX, moveZ;
    private boolean reverse;

    public EntityTeleportBlock(World worldObj) {
        super(worldObj);
        this.setBlockMode(Blocks.STONE);
    }

    @Override
    public String getCustomNameTag() {
        return "텔레포트 블럭 이동시간  " + moveDelay;
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (WorldAPI.equalsHeldItem(Items.APPLE)) {
            PosHelper posHelper = new PosHelper(player);
            moveX = posHelper.getX(SpawnDirection.FORWARD, 2, false);
            moveZ = posHelper.getZ(SpawnDirection.FORWARD, 2, false);
        }
        if (WorldAPI.equalsHeldItem(Items.GOLDEN_APPLE)) {
            if (player.isSneaking())
                moveDelay--;
            else
                moveDelay++;
        }

        return super.processInteract(player, hand, stack);

    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        timeDelay++;
        if (timeDelay >= 20) {
            timeDelay = 0;

            if (!reverse)
                moveXZ++;
            else if(reverse)
                moveXZ--;

            if (moveXZ == 3 || moveXZ == -1) {
                reverse = !reverse;

                if(moveXZ == 3) moveXZ--;
                if(moveXZ == -1) moveXZ++;
            }
            System.out.println(moveXZ+" - "+reverse);
        }

        if (moveXZ == 2)
            setPosition(getSpawnX() + moveX, getSpawnY(), getSpawnZ() + moveZ);
        if (moveXZ == 1)
            setPosition(getSpawnX(), getSpawnY(), getSpawnZ());
        if (moveXZ == 0)
            setPosition(getSpawnX() - moveX, getSpawnY(), getSpawnZ() - moveZ);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setDouble("moveX", moveX);
        compound.setDouble("moveZ", moveZ);
        compound.setDouble("moveXZ", moveXZ);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        moveX = compound.getDouble("moveX");
        moveZ = compound.getDouble("moveZ");
        moveXZ = compound.getInteger("moveXZ");
    }
}
