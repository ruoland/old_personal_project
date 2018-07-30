package ruo.asdfrpg;

import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import ruo.asdfrpg.skill.EntityAsdfBlock;
import ruo.asdfrpg.skill.PlayerSkill;
import ruo.asdfrpg.skill.SkillHelper;
import ruo.asdfrpg.skill.Skills;
import ruo.minigame.api.LoginEvent;

public class AsdfEvent {
    @SubscribeEvent
    public void playerTick(LoginEvent e) {
        SkillHelper.init(e.player);
        SkillHelper.readPlayerSkill();
    }

    @SubscribeEvent
    public void playerTick(PlayerEvent.PlayerLoggedOutEvent e) {
        SkillHelper.savePlayerSkill();
    }

    @SubscribeEvent
    public void village(PlayerInteractEvent.RightClickBlock e) {
        Block block = e.getWorld().getBlockState(e.getPos()).getBlock();
        if (Blocks.BEACON == block) {
            System.out.println("스킬 사용됨" + e.getEntityPlayer().getUniqueID());
            SkillHelper.getPlayerSkill(e.getEntityPlayer()).useSkill(Skills.VILLAGE_RETURN, 0);
        }
    }

    @SubscribeEvent
    public void keyEvent(InputEvent.KeyInputEvent e) {

    }

    @SubscribeEvent
    public void guiOpen(GuiOpenEvent e) {
        if (e.getGui() instanceof GuiGameOver) {
            e.setGui(new GuiAsdfGameOver());
        }
        if (e.getGui() instanceof GuiBeacon)
            e.setCanceled(true);
    }

    //        System.out.println(e.getEntityLiving()+" - "+e.getSource().getEntity()+" - "+e.getSource()+e.getSource().getSourceOfDamage());
    //EntityCreeper['Creeper'/11621, l='TEST', x=1170.59, y=4.00, z=196.16] - EntityPlayerMP['Player508'/111, l='TEST', x=1168.61, y=4.00, z=193.21] - net.minecraft.util.EntityDamageSource@33d163d6EntityPlayerMP['Player508'/111, l='TEST', x=1168.61, y=4.00, z=193.21]
    @SubscribeEvent
    public void playerTick(LivingAttackEvent e) {
        System.out.println(e.getEntityLiving().isServerWorld());
        if (e.getSource().getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e.getSource().getEntity();

            PlayerSkill playerSkill = SkillHelper.getPlayerSkill(player.getUniqueID());
            if (playerSkill.isRegister(Skills.AUTO_ATTACK)) {
                EntityAsdfBlock asdfBlock = new EntityAsdfBlock(player.worldObj);
                asdfBlock.setPosition(player.getPosition().add(0, 3, 0));
                if (e.getEntityLiving().isServerWorld())
                    player.worldObj.spawnEntityInWorld(asdfBlock);
                asdfBlock.setTarget(e.getEntityLiving());
            }
        }
    }

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent e) {
        if (!e.player.isCreative()) {
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
