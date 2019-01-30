package rmap.lopre2;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.lwjgl.input.Mouse;

public class ItemBlockMove extends Item {
    private int prevWheel;
    private double ax;

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        if (player.isServerWorld() && entity  instanceof EntityPreBlock) {
            EntityPreBlock block = (EntityPreBlock) entity;
            EnumFacing facing = EnumFacing.fromAngle(player.rotationPitch);
            block.setPosition(block.posX + facing.getFrontOffsetX() * ax, block.posY, block.posZ + facing.getFrontOffsetZ() * ax);
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (playerIn.isServerWorld() && hand == EnumHand.MAIN_HAND && target instanceof EntityPreBlock) {
            EntityPreBlock block = (EntityPreBlock) target;
            if (stack.getDisplayName().indexOf("DOWN") != -1)
                block.setPosition(block.posX, block.posY - ax, block.posZ);
            else
                block.setPosition(block.posX, block.posY + ax, block.posZ);

        }
        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (!worldIn.isRemote && hand == EnumHand.MAIN_HAND) {
            if (itemStackIn.getDisplayName().indexOf("DOWN") != -1)
                itemStackIn.setStackDisplayName("UP");
            else
                itemStackIn.setStackDisplayName("DOWN");
        }
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        prevWheel = Mouse.getEventDWheel();
        if(prevWheel == 120) {
            ax-=0.01;
        }
        if(prevWheel == -120) {
            ax+=0.01;
        }
    }
}
