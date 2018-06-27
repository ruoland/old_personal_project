package ruo.map.platformer.chapter1;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import ruo.minigame.map.EntityDefaultNPC;
import ruo.minigame.map.TypeModel;

import javax.annotation.Nullable;

public class EntityBabyCow extends EntityDefaultNPC {
    public EntityBabyCow(World world) {
        super(world);
        this.setModel(TypeModel.COW);
        this.setChild(true);
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
        this.startRiding(player);
        return super.processInteract(player, hand, stack);
    }

    @Override
    public double getYOffset() {
        return 0.7D;
    }
}
