package minigameLib.action;

import minigameLib.api.EntityAPI;
import minigameLib.api.WorldAPI;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Loader;
import map.lopre2.EntityPreBlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GrabHelper {
	private static float width, height, grabSpeed;
	public static boolean isReset = true, wallGrab, blockColide;
	public static String wallFacing;
	public static HashMap<Integer, BlockPos> endPos = new HashMap<>();
	public static HashMap<Integer, BlockPos> startPos = new HashMap<>();

	public static float getGrabSpeed() {
		return grabSpeed;
	}

	public static void ungrab(EntityPlayer player) {
		wallGrab = false;
		player.onGround = false;
		wallFacing = "";
		prevX = 0;
		prevZ = 0;
		GrabHelper.setGrabSpeed(0);
		blockColide = false;
	}
	private static double prevX, prevZ;

	public static boolean isRangeX(double x){
		return prevX+1 >= x && prevX-1 <= x;
	}
	public static boolean isRangeZ(double z){
		return prevZ+1 >= z && prevZ-1 <= z;
	}
	/**
	 * 플레이어가 벽을 잡을 수 있나? isGrab은 벽을 잡은 상태에서 체크하는 건지 전달함
	 */
	public static void wallGrabCheck(EntityPlayer player, boolean isGrab) {
		String index = player.getHorizontalFacing().getName();

		double posX = EntityAPI.lookX(player, 1), posY = player.posY, posZ = EntityAPI.lookZ(player, 1);
		if (wallGrab && !isRangeX(player.posX) && !isRangeZ(player.posZ)) {
			ungrab(player);
			System.out.println("언그랩됨");
		}
		Block lookblock = WorldAPI.getBlock(posX, posY + 1, posZ).getBlock();//플레이어 눈 앞에 블럭 - 이게 AIR가 아니어야 올라갈 수 있음
		Block lookblockup = WorldAPI.getBlock(posX, posY + 2, posZ).getBlock();//플레이어 눈 앞 블럭 위에 블럭 - 이게 AIR여야만 올라갈 수 있음
		if (wallGrab) {
			Block jumpCheckBlock = WorldAPI.getBlock(posX, posY - 1, posZ).getBlock();//점프한 상태에서도 체크하기 위해서 있음
			Block jumpCheckBlock2 = WorldAPI.getBlock(posX, posY, posZ).getBlock();//
			if ((WorldAPI.equalsBlock(lookblock, Blocks.AIR) && !WorldAPI.equalsBlock(lookblockup, Blocks.AIR))
					|| !WorldAPI.equalsBlock(lookblockup, Blocks.AIR)) {
				System.out.println((WorldAPI.equalsBlock(lookblock, Blocks.AIR) && !WorldAPI.equalsBlock(lookblockup, Blocks.AIR))+" - "+  !WorldAPI.equalsBlock(lookblockup, Blocks.AIR)+" - "+ (WorldAPI.equalsBlock(jumpCheckBlock, Blocks.AIR) && WorldAPI.equalsBlock(jumpCheckBlock2, Blocks.AIR)));
				blockColide = true;
			} else {
				blockColide = false;
			}
		}
		System.out.println("블럭1"+blockColide);

		AxisAlignedBB aabbblock = lopre(player);

		if (aabbblock != null || (!WorldAPI.equalsBlock(lookblock, Blocks.AIR) && WorldAPI.equalsBlock(lookblockup, Blocks.AIR))) {
			if (!isGrab) {
				player.motionY = 0;
			}
			wallGrab = true;
			wallFacing = index;
			prevX = player.posX;
			prevZ = player.posZ;
			System.out.println("잡을 수 있게 설정됨");
		}

	}
	public static AxisAlignedBB lopre(EntityPlayer player){
		if(Loader.isModLoaded("LoopPre2")) {
			AxisAlignedBB aabbp = new AxisAlignedBB(EntityAPI.lookX(player, 1), player.posY + 1, EntityAPI.lookZ(player, 1), player.posX, player.posY, player.posZ);
			List<EntityPreBlock> list = player.worldObj.getEntitiesWithinAABB(EntityPreBlock.class, aabbp);
			for (EntityPreBlock entity : list) {
				ArrayList<EntityPreBlock> preBlockArrayList = (ArrayList<EntityPreBlock>) EntityAPI.getEntity(entity.worldObj, entity.getEntityBoundingBox().addCoord(0,0.2,0), EntityPreBlock.class);
				boolean skip = false;
				for(EntityPreBlock preBlock : preBlockArrayList){
					if(preBlock.isInvisible())
						skip = true;
				}
				AxisAlignedBB aabb = entity.getCollisionBoundingBox();

				if (aabb != null && aabbp != null && aabb.intersectsWith(aabbp) && (preBlockArrayList.size() == 1 || skip)) {
					blockColide = false;
					return aabb;
				}
			}
		}
		return null;
	}
	public static void reset() {
		isReset = true;
	}

	public static float getWidth() {
		return width;
	}

	public static void setWidth(float width) {
		GrabHelper.width = width;
	}

	public static float getHeight() {
		return height;
	}

	public static void setHeight(float height) {
		GrabHelper.height = height;
	}

	public static void setGrabSpeed(float grabSpeed) {
		GrabHelper.grabSpeed = grabSpeed;
	}
}
