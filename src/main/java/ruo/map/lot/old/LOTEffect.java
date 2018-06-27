package ruo.map.lot.old;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import ruo.cmplus.camera.Camera;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.IngameEffect;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.AbstractTick.BlockXYZ;
import ruo.minigame.effect.TickRegister;

public class LOTEffect {
	public static BlockPos pos;
	public static boolean isPlayerWallMode, isChestItem;
	public static ItemStack chestItem;

	public static void water(EntityPlayer player) {
		if(TickRegister.isAbsTickRun("WATERWAND")) {
			AbstractTick absTick = TickRegister.getAbsTick("WATERWAND");
			absTick.run(null);
			TickRegister.remove(absTick);
		}
		BlockPos firstPos = new BlockPos(player.posX+(EntityAPI.getFacingX(player.rotationYaw+90)* 5),
				player.posY, 
				player.posZ+(EntityAPI.getFacingZ(player.rotationYaw+90)* 5));
		BlockPos secondPos = new BlockPos(
				player.posX+(EntityAPI.getFacingX(player.rotationYaw-90)* 5)+EntityAPI.getFacingX(player) * 5,
				player.posY=10,
				player.posZ+(EntityAPI.getFacingZ(player.rotationYaw-90)* 5)+EntityAPI.getFacingZ(player) * 5);
		WorldAPI.blockTick(player.worldObj, firstPos.getX(), secondPos.getX(), firstPos.getY(), secondPos.getY(), 
				firstPos.getZ(), secondPos.getZ(), new BlockXYZ() {
					
					@Override
					public void run(TickEvent.Type type) {
						if(WorldAPI.getBlock(getPos()) == Blocks.AIR) {
							WorldAPI.getWorld().setBlockState(getPos(), Blocks.WATER.getDefaultState());
						}
					}
				});
		TickRegister.register(new AbstractTick("WATERWAND", Type.SERVER,200, false) {
			@Override
			public void run(Type type) {
				WorldAPI.blockTick(player.worldObj, firstPos.getX(), secondPos.getX(), firstPos.getY(), secondPos.getY(), 
						firstPos.getZ(), secondPos.getZ(), new BlockXYZ() {
							@Override
							public void run(Type type) {
								if(WorldAPI.getBlock(getPos()) == Blocks.WATER) {
									WorldAPI.getWorld().setBlockState(getPos(), Blocks.AIR.getDefaultState());
								}
							}
						});
			}
		});

	}
	public static void wallmode(BlockPos pos) {
		isPlayerWallMode = true;
		EntityPlayer player = WorldAPI.getPlayer();
		LOTEffect.pos = pos.subtract(player.getPosition());
		if (pos.getX() > 0) {
			WorldAPI.command("/scroll x");
		}
		if (pos.getX() < 0) {
			WorldAPI.command("/scroll x r");
		}
		if (pos.getZ() > 0) {
			WorldAPI.command("/scroll z");
		}
		if (pos.getZ() < 0) {
			WorldAPI.command("/scroll z r");
		}
	}
	
	public static boolean canForward() {
		if(pos == null) {
			isPlayerWallMode = false;
			return false;
		}
		BlockPos pos = LOTEffect.pos;
		BlockPos block = null, block2 = null;
		if(pos.getX() > 0) {
			block = (WorldAPI.getPlayer().getPosition().add(0, 0, 1));
			block2 = (WorldAPI.getPlayer().getPosition().add(0, 1, 1));
		}
		if(pos.getX() < 0) {
			block = (WorldAPI.getPlayer().getPosition().add(0, 0, -1));
			block2 = (WorldAPI.getPlayer().getPosition().add(0, 1, -1));
		}
		if(pos.getZ() > 0) {
			block = (WorldAPI.getPlayer().getPosition().add(1, 0, 0));
			block2 = (WorldAPI.getPlayer().getPosition().add(1, 1, 0));
		}
		if(pos.getZ() < 0) {
			block =	(WorldAPI.getPlayer().getPosition().add(-1, 0, 0));
			block2 = (WorldAPI.getPlayer().getPosition().add(-1, 1, 0));
		}
		System.out.println((WorldAPI.getBlock(block) != Blocks.AIR) +""+ (WorldAPI.getBlock(block2) != Blocks.AIR));
		return WorldAPI.getBlock(block) != Blocks.AIR && WorldAPI.getBlock(block2) != Blocks.AIR;
	}
	
	
	public static void openChest(BlockPos pos) {
		LOTEffect.chestItem = WorldAPI.getChestInventory(pos).getStackInSlot(0);
		if(chestItem == null)
			chestItem = new ItemStack(Items.GOLDEN_APPLE);
		WorldAPI.teleport(pos.getX()+0.5, pos.getY(), pos.getZ()-2);
		Camera.getCamera().playerCamera(true);
		TickRegister.register(new AbstractTick(1, true) {
			EntityItem item = new EntityItem(WorldAPI.getWorld(), WorldAPI.x(), WorldAPI.y()+3.5, WorldAPI.z()-1.7, LOTEffect.chestItem);
			int fourCamTick, camMode;
			Camera cm = Camera.getCamera();
			@Override
			public void run(Type type) {
				if(camMode == 0 && (!cm.cmTraEquals(-1.4, 1, -3.2) || !cm.cmRotateEquals(0, -60, 0))) {
					cm.cameraRotate(0, -60, 0, 1);
					cm.cameraMove(-1.4, 1, -3.2, 20);
					System.out.println("첫번째 모드"+absRunCount);
					//대상 상자가 Z2 앞에 있음. 상자 Z 609 플레이어 Z 607
				}
				else
					camMode = 1;
				
				if(camMode == 1 && (!cm.cmTraEquals(-1, -2, 1) || !cm.cmRotateEquals(0, 230, 0))) {
					cm.cameraRotate(0, 230, 0, 2);
					cm.cameraMove(-1, -2, 1, 20);
					System.out.println("두번째 모드"+absRunCount);

				}
				else if(camMode == 1){
					camMode = 2;
					IngameEffect.openChestEffect(pos, true);
				}
				
				if(camMode == 2 && (!cm.cmTraEquals(0, -1, 2) || !cm.cmRotateEquals(0, 180, 0))) {
					cm.cameraRotate(0, 180, 0, 5);
					cm.cameraMove(0, -1, 2, 20);
					System.out.println("세번째 모드"+absRunCount);

				}else if(camMode == 2)
					camMode = 3;
				
				if(camMode == 3) {
					System.out.println(fourCamTick+"네번째 모드"+absRunCount);
					camMode = 4;
					WorldAPI.command("/camera yaw set -180");
					item.setInfinitePickupDelay();
					WorldAPI.getWorld().spawnEntityInWorld(item);
					fourCamTick = absRunCount;
				}
				if(camMode == 4) {
					item.setVelocity(0, 0, 0);
				}
				if(camMode == 4 && fourCamTick + 100 > absRunCount) {
					absLoop = false;
					item.setDead();
				}
			}
		});
	}
}
