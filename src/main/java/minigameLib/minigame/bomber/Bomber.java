package minigameLib.minigame.bomber;

import cmplus.camera.Camera;
import oneline.api.WorldAPI;
import minigameLib.minigame.AbstractMiniGame;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

public class Bomber extends AbstractMiniGame {
	public static Item bombItem = new ItemBomber();
	
	public Bomber() {
	}
	
	@Override
	public boolean start(Object... obj) {
		String type = String.valueOf(obj[0]);
		ItemStack item = new ItemStack(bombItem);
		EntityPlayer player = WorldAPI.getPlayer();
		player.inventory.setInventorySlotContents(1, item);
		Camera.getCamera().reset();
		Camera.getCamera().playerCamera(true);
		Camera.getCamera().setYP(true);
		Camera.getCamera().moveCamera(0, -7, 0);

		if(type.equals("z+")){
			Camera.getCamera().rotateCamera(90, 180, 0);
			Camera.getCamera().lockCamera(true, 0, 0);
		}
		if(type.equals("z-")){
			Camera.getCamera().rotateCamera(90, 0, 0);
			Camera.getCamera().lockCamera(true, 179, 0);
		}
		if(type.equals("z++")){
			Camera.getCamera().rotateCamera(60, 180, 0);
			Camera.getCamera().lockCamera(true, 0, 0);
			Camera.getCamera().moveCamera(0, -6, 2.5);
		}
		Minecraft.getMinecraft().gameSettings.setSoundLevel(SoundCategory.BLOCKS, 0.02F);
		return super.start();
	}
	
	@Override
	public boolean end(Object... obj) {
		Camera.getCamera().reset();
		
		return super.end();
	}
	
}
