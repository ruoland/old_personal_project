package ruo.asdfrpg.skill;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import ruo.asdfrpg.AsdfRPG;
import ruo.minigame.api.WorldAPI;

public class SkillVillageReturn extends Skill {

    @Override
    public void onEffect(SkillStack skillStack, int data) {
        NBTTagCompound tagCompound = skillStack.getTagCompound();
        EntityPlayer player = skillStack.getPlayer();
        if(data == 0){
            tagCompound.setDouble("posX", player.posX);
            tagCompound.setDouble("posY", player.posY);
            tagCompound.setDouble("posZ", player.posZ);
        }
        if(data == 1){
            if(!tagCompound.hasKey("posX")){
                System.out.println("귀환할 수 있는 마을이 없습니다.");
                return;
            }
            WorldAPI.teleport(tagCompound.getDouble("posX"), tagCompound.getDouble("posY"), tagCompound.getDouble("posZ"));
        }
    }

}
