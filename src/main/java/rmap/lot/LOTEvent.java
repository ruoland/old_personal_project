package rmap.lot;

import minigameLib.api.WorldAPI;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import rmap.lot.dungeon.area.DungeonArea;
import rmap.lot.old.LOTEffect;

public class LOTEvent {
	public static DungeonArea dungeonArea;
	public static boolean isLOT = false;
	private int flyDelay = 0;
	@SubscribeEvent
	public void event(TickEvent.PlayerTickEvent e) {
		if(isLOT && !e.player.capabilities.isCreativeMode && WorldAPI.equalsWorldName("LOT")){
			if(dungeonArea != null){
				if(dungeonArea.checkDead()){
					dungeonArea.openDoor();
				}
			}
			if(e.player.posY <= 0){
				e.player.motionY = 0;
				WorldAPI.teleport(e.player.posX, 1, e.player.posZ);
			}
		}
	}

	@SubscribeEvent
	public void event(PlayerInteractEvent.LeftClickBlock e) {
		if (isLOT && WorldAPI.equalsHeldItem(Items.WOODEN_SHOVEL)) {
			e.setCanceled(true);
			DungeonArea.selectPos1 = e.getPos();
			System.out.println(e.getPos()+ " 가 선택됨(첫번쨰)");
		}
	}
	@SubscribeEvent
	public void event(RightClickBlock e) {
		if (isLOT && WorldAPI.equalsHeldItem(Items.WOODEN_SHOVEL)) {
			e.setCanceled(true);
			DungeonArea.selectPos2 = e.getPos();
			System.out.println(e.getPos()+ " 가 선택됨(두번째)");
		}
		if (isLOT && e.getEntityPlayer().isSneaking()) {
			if(WorldAPI.getBlock(e.getPos()) instanceof BlockChest){ 
				LOTEffect.openChest(e.getPos());
			}
		}
	}
	

	@SubscribeEvent
	public void event(LivingUpdateEvent e) {
		if (isLOT && LOTEffect.isPlayerWallMode && e.getEntityLiving() instanceof EntityPlayer) {
			e.getEntityLiving().motionY = 0;
			if (!LOTEffect.canForward()) {
				e.getEntityLiving().setVelocity(0, 0, 0);
			}
		}
	}

}
