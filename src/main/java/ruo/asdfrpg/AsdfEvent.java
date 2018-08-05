package ruo.asdfrpg;

import atomicstryker.dynamiclights.client.DynamicLights;
import atomicstryker.dynamiclights.client.IDynamicLightSource;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;
import ruo.asdfrpg.skill.EntityAsdfBlock;
import ruo.asdfrpg.skill.PlayerSkill;
import ruo.asdfrpg.skill.SkillHelper;
import ruo.asdfrpg.skill.Skills;
import ruo.cmplus.deb.DebAPI;
import ruo.minigame.api.LoginEvent;
import ruo.minigame.api.RenderAPI;
import ruo.minigame.api.ScriptAPI;
import ruo.minigame.api.WorldAPI;

public class AsdfEvent {
    public static int backX, backY, healthX, healthY, foodX, foodY;
    @SubscribeEvent
    public void guiopen(GuiOpenEvent e) {
        if(WorldAPI.equalsWorldName("TEST")) {
            GuiIngameForge.renderHealth = false;
            GuiIngameForge.renderExperiance = false;
            GuiIngameForge.renderFood = false;
            GuiIngameForge.renderHotbar = false;
            GuiIngameForge.renderJumpBar = false;
            if (e.getGui() instanceof GuiChat) {
                e.setGui(new GuiRPGChat());
            }
            if (e.getGui() instanceof GuiGameOver) {
                e.setGui(new GuiAsdfGameOver());
            }
            if (e.getGui() instanceof GuiBeacon)
                e.setCanceled(true);
        }else{
            GuiIngameForge.renderHealth = true;
            GuiIngameForge.renderExperiance = true;
            GuiIngameForge.renderFood = true;
            GuiIngameForge.renderHotbar = true;
        }
    }
    @SubscribeEvent
    public void gameoverlay(TickEvent.PlayerTickEvent e) {
        if(e.phase ==TickEvent.Phase.END && e.side == Side.SERVER)
        DebAPI.deb();
    }
    @SubscribeEvent
    public void gameoverlay(ServerChatEvent e) {
        if(e.getMessage().startsWith("set:")) {
            DebAPI.activeName = e.getMessage().replace("set:", "");
        }
        System.out.println("FOOD "+e.getPlayer().getFoodStats().getFoodLevel());
        String[] split = e.getMessage().split(",");
        if(split.length > 2){
            float x = Float.valueOf(split[0]);
            float y = Float.valueOf(split[1]);
            float z = Float.valueOf(split[2]);
            DebAPI.debAPI.get(DebAPI.activeName).x = x;
            DebAPI.debAPI.get(DebAPI.activeName).y = y;
            DebAPI.debAPI.get(DebAPI.activeName).z = z;
        }
    }
    @SubscribeEvent
    public void gameoverlay(RenderGameOverlayEvent.Post e) {
        if(WorldAPI.equalsWorldName("TEST")) {
            Minecraft mc = Minecraft.getMinecraft();
            int width = e.getResolution().getScaledWidth();
            int height = e.getResolution().getScaledHeight();
            DebAPI back = DebAPI.createDebAPI("backXYZ", 203.59, 21.55, 17);
            DebAPI health = DebAPI.createDebAPI("healthXYZ", 202.25, 19.3, 13.1);
            DebAPI backf = DebAPI.createDebAPI("backfXYZ", 124, 21, 17);
            DebAPI food = DebAPI.createDebAPI("foodXYZ", 123, 19.5, 30);
            DebAPI exp = DebAPI.createDebAPI("expXYZ", 180, 30, 8);
            DebAPI hotbar = DebAPI.createDebAPI("hotXYZ", 30, 21, 8);
            if (e.getType() == RenderGameOverlayEvent.ElementType.ALL) {
                EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                RenderAPI.drawTexture("asdfrpg:backgroundbar.png", width / 2 - back.x, height - back.y, player.getMaxHealth() * 3, back.z);
                RenderAPI.drawTexture("asdfrpg:healthbar.png", width / 2 - health.x, height - health.y, player.getHealth() * 3 - 2, health.z);
                RenderAPI.drawTexture("asdfrpg:backgroundbar.png", width / 2 - backf.x, height - back.y, 60, back.z);
                RenderAPI.drawTexture("asdfrpg:foodbar.png", width / 2 - food.x, height - food.y, (player.getFoodStats().getFoodLevel()) * 3 - 2, health.z);
                RenderAPI.drawTexture("asdfrpg:foodbar.png", width / 2 - exp.x, height - exp.y, (player.experience) * 10, exp.z);
                for (int i = 0; i < 9; i++) {
                    RenderAPI.drawTexture("asdfrpg:backgroundbar.png", (width / 2 - hotbar.x) + (i * 20), height - hotbar.y, 19, 19);
                    GlStateManager.pushMatrix();
                    GlStateManager.enableRescaleNormal();
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    RenderHelper.enableGUIStandardItemLighting();
                    GlStateManager.translate(0,0,1000);
                    mc.getRenderItem().renderItemAndEffectIntoGUI(player, player.inventory.mainInventory[i],  (int)((width / 2 - hotbar.x) + (i * 20)), 0);
                    mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, player.inventory.mainInventory[i],  (int)((width / 2 - hotbar.x) + (i * 20)), (int)(height - hotbar.y));
                    RenderHelper.disableStandardItemLighting();
                    GlStateManager.disableRescaleNormal();
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                }
            }
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

    @SubscribeEvent
    public void village(PlayerInteractEvent.EntityInteract e) {
        e.getEntityPlayer().startRiding(e.getTarget());
        SkillHelper.getPlayerSkill(e.getEntityPlayer()).useSkill(Skills.RIDING, 0);
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

    //        System.out.println(e.getEntityLiving()+" - "+e.getSource().getEntity()+" - "+e.getSource()+e.getSource().getSourceOfDamage());
    //EntityCreeper['Creeper'/11621, l='TEST', x=1170.59, y=4.00, z=196.16] - EntityPlayerMP['Player508'/111, l='TEST', x=1168.61, y=4.00, z=193.21] - net.minecraft.util.EntityDamageSource@33d163d6EntityPlayerMP['Player508'/111, l='TEST', x=1168.61, y=4.00, z=193.21]
    @SubscribeEvent
    public void playerTick(LivingAttackEvent e) {
        if (e.getSource().getEntity() instanceof EntityPlayer) {
            System.out.println("플레이어가 공격함");
            EntityPlayer player = (EntityPlayer) e.getSource().getEntity();

            PlayerSkill playerSkill = SkillHelper.getPlayerSkill(player.getUniqueID());
            if (playerSkill.isRegister(Skills.AUTO_ATTACK)) {
                EntityAsdfBlock asdfBlock = new EntityAsdfBlock(player.worldObj);
                asdfBlock.setPosition(player.getPosition().add(0, 3, 0));
                if (e.getEntityLiving().isServerWorld())
                    player.worldObj.spawnEntityInWorld(asdfBlock);
                asdfBlock.setTarget(e.getEntityLiving());
                asdfBlock.player = player;
            }
        }
    }

    @SubscribeEvent
    public void onEntityJoinedWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityLight) {
            EntityLight arrow = (EntityLight) event.getEntity();
            DynamicLights.addLightSource(new EntityLightAdapter(arrow, 15));
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

    private class EntityLightAdapter implements IDynamicLightSource {
        private EntityLight entity;
        private int level;

        public EntityLightAdapter(EntityLight light, int level) {
            entity = light;
            this.level = level;
            System.out.println(level);
        }

        @Override
        public Entity getAttachmentEntity() {
            return entity;
        }

        @Override
        public int getLightLevel() {
            return 15;
        }
    }

}
