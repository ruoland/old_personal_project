package ruo.yout.item;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import olib.api.WorldAPI;

public class ItemSpawn extends Item {

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {

        for (int i = 0; i < 3; i++) {
            double x =playerIn.posX+ WorldAPI.rand(30);
            double z = playerIn.posZ + WorldAPI.rand(30);
            EntityLiving creeper = null;
            switch (worldIn.rand.nextInt(5)) {
                case 0: {
                    creeper = new EntityCreeper(worldIn);
                    break;
                }
                case 1: {
                    creeper = new EntityZombie(worldIn);
                    break;
                }
                case 2: {
                    creeper = new EntitySkeleton(worldIn);
                    break;
                }
                case 3: {
                    creeper = new EntitySpider(worldIn);
                    break;
                }

                case 5: {
                    creeper = new EntityEnderman(worldIn);
                    break;
                }
            }
            if (creeper != null) {
                creeper.setPosition(x, playerIn.posY, z);
                worldIn.spawnEntityInWorld(creeper);
                creeper.onInitialSpawn(worldIn.getDifficultyForLocation(creeper.getPosition()), null);
                creeper.getEntityData().setBoolean("mojae", true);
            }

        }
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }
}
