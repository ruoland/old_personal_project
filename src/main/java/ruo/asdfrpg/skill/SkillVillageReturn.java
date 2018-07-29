package ruo.asdfrpg.skill;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import ruo.minigame.api.WorldAPI;

public class SkillVillageReturn extends Skill {

    @Override
    public void onEffect(SkillStack skillStack, int data) {
        NBTTagCompound tagCompound = skillStack.getTagCompound();
        EntityPlayer player = skillStack.getPlayer();
        System.out.println("데이터"+data);

        if(data == 0){
            tagCompound.setDouble("posX", player.posX);
            tagCompound.setDouble("posY", player.posY);
            tagCompound.setDouble("posZ", player.posZ);
        }
        if(data == 1){
            WorldAPI.teleport(tagCompound.getDouble("posX"), tagCompound.getDouble("posY"), tagCompound.getDouble("posZ"));
        }
    }

}
