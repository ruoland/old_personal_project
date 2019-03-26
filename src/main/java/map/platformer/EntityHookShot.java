package map.platformer;

import oneline.api.WorldAPI;
import oneline.map.EntityDefaultNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityHookShot extends EntityDefaultNPC {
    public EntityPlayer player;

    public EntityHookShot(World worldIn) {
        super(worldIn);
        isFly = true;
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
        if (hand == EnumHand.MAIN_HAND) {
            {
                if (WorldAPI.equalsItem(stack, Items.APPLE)) {
                    this.player = player;
                    System.out.println("서있음-플레이어 넣음" + player);
                } else {
                    player = null;
                    System.out.println("서있음-플레이어 없앰" + player);
                }
                System.out.println("서있음 " + player);

            }

        }
        return super.processInteract(player, hand, stack);
    }

    /**
     * if (WorldAPI.equalsHeldItem(Items.ARROW)) {
     * player.addVelocity(x / 140, y /100, z / 140);
     * }
     * else if (WorldAPI.equalsHeldItem(Items.WOODEN_SWORD)) {
     * player.addVelocity(x / 140, y /100, z / 140);
     * }
     * else if (WorldAPI.equalsHeldItem(Items.GOLDEN_APPLE)) {
     * player.addVelocity(x / 100, y / 120, z / 100);
     * }
     * else if (WorldAPI.equalsHeldItem(Items.BREAD)) {
     * player.addVelocity(x / 100, y / 120, z / 100);
     * }
     */
    //특정 시간이 될 때까지 보는 방향으로 보내고 y도 천천히 증가시키고
    //반대로
    private double speedY = 0.005D, speedXZ = 0D;
    private int delay = 60;
    private boolean backMode;//뒤로 가는지

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (player != null) {
            if(WorldAPI.equalsHeldItem(Items.APPLE)) {
                if ((posX - player.posX) > 0) {
                    player.motionX = (posX - player.posX) / 100;
                }
                if ((posY - player.posY) > 10) {
                    player.motionY = (posY - player.posY) / 30;
                }
                if ((posZ - player.posZ) > 0) {
                    player.motionZ = (posZ - player.posZ) / 100;
                }
            }
            if(WorldAPI.equalsHeldItem(Items.BREAD)) {
                if ((posX - player.posX) > 0) {
                    player.motionX = (posX - player.posX) / 150;
                }
                if ((posY - player.posY) > 10) {
                    player.motionY = (posY - player.posY) / 30;
                }
                if ((posZ - player.posZ) > 0) {
                    player.motionZ = (posZ - player.posZ) / 150;
                }
            }
            if(WorldAPI.equalsHeldItem(Items.CHICKEN)) {
                if ((posX - player.posX) > 0) {
                    player.motionX = (posX - player.posX) / 80;
                }
                if ((posY - player.posY) > 10) {
                    player.motionY = (posY - player.posY) / 30;
                }
                if ((posZ - player.posZ) > 0) {
                    player.motionZ = (posZ - player.posZ) / 80;
                }
            }
        }
    }
}
