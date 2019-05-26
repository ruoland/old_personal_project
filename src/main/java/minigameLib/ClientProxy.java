package minigameLib;

import minigameLib.command.CommandMg;
import minigameLib.minigame.minerun.monster.*;
import olib.api.RenderAPI;
import olib.fakeplayer.EntityFakePlayer;
import olib.map.EntityDefaultBlock;
import olib.map.EntityDefaultNPC;
import minigameLib.minigame.bomber.EntityBomb;
import minigameLib.minigame.elytra.EntityFlyingWeen;
import minigameLib.minigame.elytra.miniween.*;
import minigameLib.minigame.elytra.playerarrow.EntityHomingTNT;
import minigameLib.minigame.elytra.playerarrow.EntityTNTArrow;
import minigameLib.minigame.minerun.*;
import minigameLib.minigame.scroll.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;

public class ClientProxy extends CommonProxy {
	public static KeyBinding grab = new KeyBinding("액션-", Keyboard.KEY_R, "카카카테고리");

	@Override
	public void pre(FMLPreInitializationEvent event) {

		//DebAPI.registerEvent(new TextEvent());
		ClientRegistry.registerKeyBinding(grab);
		ClientCommandHandler.instance.registerCommand(new CommandMg());
	}

	public void init(FMLInitializationEvent e){
		try {
			Field modelManagerField = Minecraft.class.getDeclaredField("modelManager");
			modelManagerField.setAccessible(true);
			ModelManager modelManager = (ModelManager) modelManagerField.get(Minecraft.getMinecraft());
			Field blockRenderField = Minecraft.class.getDeclaredField("blockRenderDispatcher");
			blockRenderField.setAccessible(true);
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@" + modelManager + blockRenderField.get(Minecraft.getMinecraft()));
			BlockRendererDispatcher blockRenderDispatcher = new BlockRendererDispatcherMineRun(modelManager.getBlockModelShapes(), Minecraft.getMinecraft().getBlockColors());
			blockRenderField.set(Minecraft.getMinecraft(), blockRenderDispatcher);
			((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(blockRenderDispatcher);
			blockRenderDispatcher.onResourceManagerReload(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		RenderAPI.registerRender(EntityWarningBlock.class);

		RenderAPI.registerRender(EntityMRCreeper.class);
        RenderAPI.registerRender(EntityMRZombie.class);
        RenderAPI.registerRender(EntityMREnderman.class);
        RenderAPI.registerRender(EntityMRMissileCreeper.class);
        RenderAPI.registerRender(EntityMRRocketCreeper.class);
        RenderAPI.registerRender(EntityMRWalkingZombie.class);
        RenderAPI.registerRender(EntityFlyingWeen.class);
        RenderAPI.registerRender(EntityElytraBullet.class);
        RenderAPI.registerRender(EntityElytraPumpkin.class);
        RenderAPI.registerRender(EntityElytraPumpkinAttack.class);
        RenderAPI.registerRender(EntityElytraPumpkinFire.class);
        RenderAPI.registerRender(EntityElytraChest.class);
        RenderAPI.registerRender(EntityElytraScore.class);
        RenderAPI.registerRender(EntityTNTArrow.class);
        RenderAPI.registerRender(EntityHomingTNT.class);
        RenderAPI.registerRender(EntityElytraCreeper.class);
        RenderAPI.registerRender(EntityElytraBossWeen.class);
        RenderAPI.registerRender(EntityElytraBossMini.class);
        RenderAPI.registerRender(EntityBomb.class);
        RenderAPI.registerRender(EntityFakePlayer.class);
        RenderAPI.registerRender(EntityDefaultNPC.class);
        RenderAPI.registerRender(EntityDefaultBlock.class);
		RenderAPI.registerRender(EntityJumpCreeper.class);
		RenderAPI.registerRender(EntityJumpSpider.class, new RenderSpider<EntityJumpSpider>(RenderAPI.getRenderMananger()));
		RenderAPI.registerRender(EntityScrollBoss.class);RenderAPI.registerRender(EntityScrollBossFallingBlock.class);
		RenderAPI.registerRender(EntityScrollBossWarning.class);
		RenderAPI.registerRender(EntityScrollBossShootingBlock.class);
		RenderAPI.registerRender(EntityJumpFlyingBlock.class);
		RenderAPI.registerRender(EntityJumpTNT.class);
		RenderAPI.registerRender(EntityJumpDoubleReset.class);
		RenderAPI.registerRender(EntityJumpFlyingCreeper.class);
	}
	@Override
	public void post(FMLPostInitializationEvent event){
	
	}	
}
