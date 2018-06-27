package ruo.awild;

import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import ruo.awild.ai.EntityAIAvoidEntityWildHorse;
import ruo.cmplus.cm.CommandChat;
import net.minecraft.world.World;
import ruo.minigame.api.WorldAPI;

import javax.annotation.Nullable;


public class EntityWildHorse extends EntityHorse {
    public EntityWildHorse(World worldIn) {
        super(worldIn);
    }

    private EntityAITempt aiTempt;

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.aiTempt = new EntityAITempt(this, 0.6D, Items.WHEAT, true);
        this.tasks.addTask(2, this.aiTempt);

        //this.tasks.addTask(3, new EntityAIAvoidEntityWildHorse(this, EntityPlayer.class, 10.0F, 2D, 2.4D));
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(aiTempt != null && aiTempt.isRunning()){
            this.setEatingHaystack(false);
        }
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
        if (this.isTame()) {
            return super.processInteract(player, hand, stack);
        } else if ((this.aiTempt == null || this.aiTempt.isRunning()) && stack != null && stack.getItem() == Items.WHEAT) {
            --stack.stackSize;
            if (!this.worldObj.isRemote) {
                if (this.rand.nextInt(3) == 0) {
                    setHorseTamed(true);
                    setTamedBy(player);
                    setHorseSaddled(true);
                }
            }
        }
        return super.processInteract(player, hand, stack);
    }


}
