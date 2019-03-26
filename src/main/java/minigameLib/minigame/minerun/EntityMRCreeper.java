package minigameLib.minigame.minerun;

import minigameLib.MiniGame;
import oneline.api.WorldAPI;
import oneline.map.TypeModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityMRCreeper extends EntityMR {
    protected boolean isHide;

    public EntityMRCreeper(World worldIn) {
        super(worldIn);
        this.setModel(TypeModel.CREEPER);
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
        if(hand == EnumHand.MAIN_HAND && isServerWorld())
        isHide = !isHide;
        System.out.println("하이드 상태? "+isHide);

        return super.processInteract(player, hand, stack);
    }

    @Override
    public void onLivingUpdate() {
        if(MiniGame.minerun.isStart()) {
            if (isHide) {
                if (WorldAPI.getPlayer().getDistanceToEntity(this) > 10) {
                    setPosition(getSpawnPosVec().addVector(0, 3, 0));
                    this.setInvisible(!isInvisible());
                    isFly = true;
                }
                else if(isFly){
                    isFly = false;
                    this.setInvisible(!isInvisible());
                }
            }
        }
        super.onLivingUpdate();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("isHide", isHide);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        isHide = compound.getBoolean("isHide");
    }

    @Override
    public void collideAttack(Entity entityIn) {
        worldObj.createExplosion(this, posX, posY, posZ, 1.5F, false);
    }
}
