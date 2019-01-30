package rmap.platformer.chapter2;

import minigameLib.map.EntityDefaultNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import rmap.platformer.PlatStage2;

import javax.annotation.Nullable;

public class EntityChickenShopKeeper extends EntityDefaultNPC {

    public EntityChickenShopKeeper(World worldIn){
        super(worldIn);
        this.setCustomNameTag("치킨샵키퍼");
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
        PlatStage2.flyGameStart();
        return super.processInteract(player, hand, stack);
    }

    public void completeMiniGame(double distance){

    }

}
