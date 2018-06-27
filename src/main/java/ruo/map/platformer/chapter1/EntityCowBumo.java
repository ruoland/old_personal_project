package ruo.map.platformer.chapter1;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import ruo.map.platformer.EntityNPC;
import ruo.map.platformer.PlatEffect;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.map.TypeModel;

import javax.annotation.Nullable;

public class EntityCowBumo extends EntityNPC {

    public EntityCowBumo(World world) {
        super(world);
        this.setSize(0.9F, 1.4F);
        this.setModel(TypeModel.COW);
        this.setRotate(0,0,0);
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
        if (hand == EnumHand.MAIN_HAND && isServerWorld()) {
            for (Entity passenger : player.getPassengers()) {
                if (passenger instanceof EntityBabyCow) {
                    EntityBabyCow babyCow = (EntityBabyCow) player.getPassengers().get(0);
                    babyCow.dismountRidingEntity();
                    addChat(0, PlatEffect.format("lang", "plat.c1.stage.end1"));
                    babyCow.addChat(2, PlatEffect.format("lang","plat.c1.stage.end2"));
                    addChat(4, PlatEffect.format("lang","plat.c1.stage.end3"));
                    babyCow.setPosition(posX, posY, posZ);
                }
            }
        }
        return super.processInteract(player, hand, stack);
    }

    public boolean check(String key) {
        System.out.println(PlatEffect.hasKey("script",key));
        if (PlatEffect.hasKey("script",key)) {
            String keyValue = PlatEffect.format("lang",key);
            String[] condition = keyValue.split(".");
            EntityLivingBase living = null;
            if (condition[0].equalsIgnoreCase("Player"))
                living = WorldAPI.getPlayer();
            else
                living = getNPC(condition[0]);

            System.out.println(condition[0] + condition[1] + condition[2]);

            if (condition[1].equalsIgnoreCase("riding")) {
                for (Entity passenger : living.getPassengers()) {
                    if (passenger.getName().indexOf(condition[2]) != -1 || passenger.getClass().getName().indexOf(condition[2]) != -1) {
                        System.out.println("true");
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
