package minigameLib.minigame.scrollmaker;

import olib.api.EntityAPI;
import olib.api.WorldAPI;
import olib.map.EntityDefaultNPC;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.init.Blocks;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.lwjgl.input.Mouse;

import javax.annotation.Nullable;

public class EntityScrollMouse extends EntityDefaultNPC {
	private int mouseDelay = 20;
	private Minecraft mc = Minecraft.getMinecraft();
	public EntityScrollMouse(World worldIn) {
		super(worldIn);
	}
	@Override
	public void onLivingUpdate() {
		if(isServerWorld()) {
			if(mouseDelay > 0) {
				mouseDelay --;
			}
		}
		super.onLivingUpdate();
		if(mc.currentScreen != null && mc.currentScreen instanceof GuiScrollMaker) {
			GuiScrollMaker rts = (GuiScrollMaker) mc.currentScreen;
			double i = Mouse.getEventX() * mc.currentScreen.width / this.mc.displayWidth;
			double j = mc.currentScreen.height - Mouse.getEventY() * mc.currentScreen.height / this.mc.displayHeight - 1;
			this.setPosition(WorldAPI.x()+ EntityAPI.lookX(mc.thePlayer, 6)+i, WorldAPI.y()+j, WorldAPI.z()+ EntityAPI.lookZ(mc.thePlayer, 6));
			if(Mouse.isButtonDown(0) && mouseDelay == 0) {
				mouseDelay = 20;
				EntityDefaultNPC npc = new EntityDefaultNPC(worldObj);
				npc.setPosition(posX, posY, posZ);
				worldObj.spawnEntityInWorld(npc);
				npc.setBlockMode(Blocks.STONE);
				npc.setCollision(true);
			}
		}
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		WorldAPI.command("scroll x");
		mc.displayGuiScreen(new GuiScrollMaker());
		return super.onInitialSpawn(difficulty, livingdata);
	}
}
