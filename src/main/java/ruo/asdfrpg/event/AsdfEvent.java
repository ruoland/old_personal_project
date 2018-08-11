package ruo.asdfrpg.event;

import atomicstryker.dynamiclights.client.DynamicLights;
import atomicstryker.dynamiclights.client.IDynamicLightSource;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentKnockback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;
import ruo.asdfrpg.EntityLight;
import ruo.asdfrpg.GuiAsdfGameOver;
import ruo.asdfrpg.GuiRPGChat;
import ruo.asdfrpg.skill.*;
import ruo.cmplus.deb.DebAPI;
import ruo.minigame.api.LoginEvent;
import ruo.minigame.api.RenderAPI;
import ruo.minigame.api.ScriptAPI;
import ruo.minigame.api.WorldAPI;

public class AsdfEvent {
    public static int backX, backY, healthX, healthY, foodX, foodY;

    @SubscribeEvent
    public void lootingLevel(EntityJoinWorldEvent e) {
        if (e.getEntity() instanceof EntityXPOrb) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void lootingLevel(LivingExperienceDropEvent e) {
        SkillHelper.getPlayerSkill().addPlayerExp(e.getDroppedExperience());
        System.out.println(e.getDroppedExperience());
    }

    @SubscribeEvent
    public void guiopen(GuiOpenEvent e) {
        if (WorldAPI.equalsWorldName("TEST")) {
            GuiIngameForge.renderHealth = false;
            GuiIngameForge.renderExperiance = false;
            GuiIngameForge.renderFood = false;
            GuiIngameForge.renderHotbar = false;
            GuiIngameForge.renderJumpBar = false;
            if (e.getGui() instanceof GuiChat) {
                //e.setGui(new GuiRPGChat());
            }
            if (e.getGui() instanceof GuiGameOver) {
                e.setGui(new GuiAsdfGameOver());
            }
            if (e.getGui() instanceof GuiBeacon)
                e.setCanceled(true);
        } else {
            GuiIngameForge.renderHealth = true;
            GuiIngameForge.renderExperiance = true;
            GuiIngameForge.renderFood = true;
            GuiIngameForge.renderHotbar = true;
            GuiIngameForge.renderJumpBar = true;
        }
    }


    @SubscribeEvent
    public void gameoverlay(RenderGameOverlayEvent.Post e) {
        if (WorldAPI.equalsWorldName("TEST")) {
            Minecraft mc = Minecraft.getMinecraft();
        }
    }

    @SubscribeEvent
    public void playerTick(LoginEvent e) {
        SkillHelper.init(e.player);
        SkillHelper.readPlayerSkill();
    }

    @SubscribeEvent
    public void playerTick(PlayerEvent.PlayerLoggedOutEvent e) {
        SkillHelper.savePlayerSkill();
    }


}
