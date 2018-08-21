package ruo.map.hideandseek;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.MinecraftError;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ruo.halloween.EntityPlayerWeen;

public class ItemFakeHide extends Item {

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        EntityFakeHide fakeHide = new EntityFakeHide(worldIn);
        fakeHide.setPosition(pos);
        worldIn.spawnEntityInWorld(fakeHide);
        fakeHide.setCustomNameTag(playerIn.getCustomNameTag());
        fakeHide.setTexture(Minecraft.getMinecraft().thePlayer.getLocationSkin());

        return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}
