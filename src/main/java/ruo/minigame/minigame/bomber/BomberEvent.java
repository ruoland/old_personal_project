package ruo.minigame.minigame.bomber;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ruo.minigame.MiniGame;

public class BomberEvent {
	@SubscribeEvent
	public void login(WorldEvent.Unload event) {
		MiniGame.bomber.end();
	}
	@SubscribeEvent
	public void explode(ExplosionEvent event) {
		if (!MiniGame.bomber.isStart())
			return;
		if(!(event.getExplosion().getExplosivePlacedBy() instanceof EntityPlayer))
			return;
		for(int z = 0; z < 3;z++){ 
    		Vec3d pos2 = event.getExplosion().getPosition();
    		destory(event.getWorld(),new BlockPos(pos2.xCoord, pos2.yCoord, pos2.zCoord), false);
    		destory(event.getWorld(),new BlockPos(pos2.xCoord, pos2.yCoord, pos2.zCoord+z), false);
    		destory(event.getWorld(),new BlockPos(pos2.xCoord, pos2.yCoord, pos2.zCoord-z), false);
    		destory(event.getWorld(),new BlockPos(pos2.xCoord, pos2.yCoord+1, pos2.zCoord+z), false);
    		destory(event.getWorld(),new BlockPos(pos2.xCoord, pos2.yCoord+1, pos2.zCoord-z), false);
    		destory(event.getWorld(),new BlockPos(pos2.xCoord, pos2.yCoord+2, pos2.zCoord+z), false);
    		destory(event.getWorld(),new BlockPos(pos2.xCoord, pos2.yCoord+2, pos2.zCoord-z), false);
    	}
		for(int z = 0; z < 3;z++){ 
    		Vec3d pos2 = event.getExplosion().getPosition();
    		destory(event.getWorld(),new BlockPos(pos2.xCoord, pos2.yCoord, pos2.zCoord), false);
    		destory(event.getWorld(),new BlockPos(pos2.xCoord+z, pos2.yCoord, pos2.zCoord), false);
    		destory(event.getWorld(),new BlockPos(pos2.xCoord-z, pos2.yCoord, pos2.zCoord), false);
    		destory(event.getWorld(),new BlockPos(pos2.xCoord+z, pos2.yCoord+1, pos2.zCoord), false);
    		destory(event.getWorld(),new BlockPos(pos2.xCoord-z, pos2.yCoord+1, pos2.zCoord), false);
    		destory(event.getWorld(),new BlockPos(pos2.xCoord+z, pos2.yCoord+2, pos2.zCoord), false);
    		destory(event.getWorld(),new BlockPos(pos2.xCoord-z, pos2.yCoord+2, pos2.zCoord), false);
    	}
		for (BlockPos pos : event.getExplosion().getAffectedBlockPositions()) {
	    
			if (event.getWorld().getBlockState(pos).getBlock() == Blocks.COBBLESTONE)
			{
				destory(event.getWorld(),pos, false);
			}
			if (event.getWorld().getBlockState(pos.add(0,1,0)).getBlock() == Blocks.COBBLESTONE)
			{
				destory(event.getWorld(),pos.add(0,1,0), false);
			}
			if (event.getWorld().getBlockState(pos.add(0,2,0)).getBlock() == Blocks.COBBLESTONE)
			{
				destory(event.getWorld(),pos.add(0,2,0), false);
			}
		}
	}

	public void destory(World world, BlockPos pos, boolean asdf) {
		if(world.getBlockState(pos).getBlock() == Blocks.COBBLESTONE) {
			world.destroyBlock(pos, false);
		}
	
	}
	

}
