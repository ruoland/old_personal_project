package map.tycoon;

import oneline.api.WorldAPI;
import net.minecraft.util.math.BlockPos;

import java.util.Comparator;

public class BreadDataComparator implements Comparator<BlockPos> {

	@Override
	public int compare(BlockPos o1, BlockPos o2) {
		double distance = o1.distanceSq(WorldAPI.getPlayer().getPosition());
		double distance2 = o2.distanceSq(WorldAPI.getPlayer().getPosition());
		return o1.compareTo(o2);
	}

}
