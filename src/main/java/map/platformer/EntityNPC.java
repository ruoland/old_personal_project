package map.platformer;

import minigameLib.api.WorldAPI;
import minigameLib.map.EntityDefaultNPC;
import minigameLib.map.TypeModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;


public class EntityNPC extends EntityDefaultNPC {
    private int textNumber;

    public EntityNPC(World world) {
        super(world);
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
        String text = "platformer." + getCustomNameTag() + ".text" + textNumber;
        if (PlatEffect.hasKey("lang", text)) {
            WorldAPI.getPlayer().addChatComponentMessage(new TextComponentString(PlatEffect.format("lang",text)));
            textNumber++;
        } else
            textNumber = 0;
        return super.processInteract(player, hand, stack);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        String model = "plat." + getCustomNameTag().toLowerCase() + ".model";
        if (PlatEffect.hasKey("model", model)) {
            String key = PlatEffect.format("model",model).toUpperCase();
            this.setModel(TypeModel.valueOf(key));
        }

    }

    @Override
    public void setDead() {
        super.setDead();
    }
}
