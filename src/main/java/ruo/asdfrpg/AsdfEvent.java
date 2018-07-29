package ruo.asdfrpg;

import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import ruo.asdfrpg.skill.SkillHelper;
import ruo.asdfrpg.skill.Skills;
import ruo.minigame.api.LoginEvent;

public class AsdfEvent {
    @SubscribeEvent
    public void playerTick(LoginEvent e){
        SkillHelper.init(e.player);
        SkillHelper.readPlayerSkill();
    }
    @SubscribeEvent
    public void playerTick(PlayerEvent.PlayerLoggedOutEvent e){
        SkillHelper.savePlayerSkill();
    }
    @SubscribeEvent
    public void village(PlayerInteractEvent.RightClickBlock e){
        Block block = e.getWorld().getBlockState(e.getPos()).getBlock();
        if(Blocks.BEACON == block){
            System.out.println("스킬 사용됨");
            SkillHelper.getPlayerSkill(e.getEntityPlayer()).useSkill(Skills.VILLAGE_RETURN, 0);
        }
    }
    @SubscribeEvent
    public void keyEvent(InputEvent.KeyInputEvent e){

    }
    @SubscribeEvent
    public void playerTick(GuiOpenEvent e){
        if(e.getGui() instanceof GuiGameOver){
            e.setGui(new GuiAsdfGameOver());
        }
    }
    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent e){
        if(!e.player.isCreative()) {
            if (e.player.isPotionActive(AsdfRPG.flyPotion) && e.player.capabilities.isFlying) {
                e.player.capabilities.isFlying = true;
                e.player.sendPlayerAbilities();
            } else {
                e.player.capabilities.isFlying = false;
                e.player.sendPlayerAbilities();
            }
        }

    }
}
