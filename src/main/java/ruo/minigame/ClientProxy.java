package ruo.minigame;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.input.Keyboard;
import ruo.minigame.minigame.minerun.BlockRendererDispatcherMineRun;

import java.lang.reflect.Field;

public class ClientProxy extends CommonProxy {
	public static KeyBinding grab = new KeyBinding("액션-", Keyboard.KEY_R, "카카카테고리");

	@Override
	public void pre(FMLPreInitializationEvent event) {
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
		//DebAPI.registerEvent(new TextEvent());

		ClientRegistry.registerKeyBinding(grab);

		ClientCommandHandler.instance.registerCommand(new CommandMg());
	}
	
	@Override
	public void post(FMLPostInitializationEvent event){
	
	}	
}
