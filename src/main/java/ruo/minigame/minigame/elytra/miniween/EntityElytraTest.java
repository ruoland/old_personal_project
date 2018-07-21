package ruo.minigame.minigame.elytra.miniween;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.map.EntityDefaultNPC;

import javax.annotation.Nullable;

public class EntityElytraTest extends EntityDefaultNPC {

    public EntityElytraTest(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.PUMPKIN);
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
        if (WorldAPI.equalsItem(stack, Items.APPLE)) {
            setPositionAndRotation(posX,posY,posZ,rotationYaw+1,rotationPitch);
            System.out.println(rotationYaw);
        }
        if (WorldAPI.equalsItem(stack, Items.GOLDEN_APPLE)) {
        }
        if (WorldAPI.equalsItem(stack, Items.WHEAT)) {
            worldObj.setBlockState(getPosition().add(lookX(3), 0, lookZ(3)), Blocks.STONE.getDefaultState());
        }
        if (WorldAPI.equalsItem(stack, Items.ARROW)) {
            if (player.isSneaking())
                addRotate(-1, 0, 0);
            else
                addRotate(1, 0, 0);
            System.out.println(getRotateX());
        }
        if (WorldAPI.equalsItem(stack, Items.BREAD)) {
            if (player.isSneaking())
                addRotate(0, 1, 0);
            else
                addRotate(0, -1, 0);
            System.out.println(getRotateY());
        }
        return super.processInteract(player, hand, stack);

    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(WorldAPI.getPlayer() != null)
        faceEntity(WorldAPI.getPlayer(), 360,360);

    }
}
