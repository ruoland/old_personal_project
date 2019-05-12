package gooditem;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemCall extends Item {

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        boolean isFind = false;
        for(Entity entity : worldIn.loadedEntityList){
            if(entity instanceof EntityTameable){
                EntityTameable tameable = (EntityTameable) entity;
                if(tameable.isTamed() && tameable.getOwnerId().equals(playerIn.getUniqueID())){
                    tameable.setPosition(playerIn.posX, playerIn.posY, playerIn.posZ);
                    isFind = true;
                }
            }
        }
        if(!isFind)
            playerIn.addChatComponentMessage(new TextComponentString("너무 멀리 있거나 길들여진 동물이 없는 것 같습니다."));

        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }
}
