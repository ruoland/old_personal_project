package ruo.helloween;

import cmplus.deb.DebAPI;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import ruo.helloween.miniween.*;

//@Mod(modid = "Halloween")
public class HelloWeen {
	@SidedProxy(serverSide = "ruo.helloween.HWCommonProxy", clientSide = "ruo.helloween.HWClientProxy")
	public static HWCommonProxy proxy;
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("Halloween");
	@EventHandler
	public void iniit(FMLInitializationEvent e) {
		DebAPI.registerEntity(this, "PlayerA", EntityPlayerWeen.class);

		DebAPI.registerEntity(this, "Ween", EntityWeen.class);
		DebAPI.registerEntity(this, "NO-EGG-MiniWeen", EntityMiniWeen.class);
		DebAPI.registerEntity(this, "NO-EGG-AMiniWeen", EntityAttackMiniWeen.class);
		DebAPI.registerEntity(this, "NO-EGG-DMiniWeen", EntityDefenceMiniWeen.class);
		DebAPI.registerEntity(this, "NO-EGG-NMiniWeen", EntityNightMiniWeen.class);
		DebAPI.registerEntity(this, "NO-EGG-FallingMiniWeen", EntityFallingMiniWeen.class);

		DebAPI.registerEntity(this, "NO-EGG-BigWeen", EntityBigWeen.class);
		DebAPI.registerEntity(this, "NO-EGG-EntityBlock", EntityBlock.class);
		DebAPI.registerEntity(this, "NO-EGG-EntityBlock2", EntityBlockFallAttack.class);
		DebAPI.registerEntity(this, "NO-EGG-EntityBlock3", EntityBlockMoveAttack.class);

		proxy.init();
		MinecraftForge.EVENT_BUS.register(new WeenEvent());
		//GameRegistry.register(itemFlying);
	}
	
	@EventHandler
	public void iniit(FMLPreInitializationEvent e) {
		
	}
}