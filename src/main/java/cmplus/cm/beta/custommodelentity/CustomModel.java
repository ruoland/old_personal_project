package cmplus.cm.beta.custommodelentity;

import olib.api.BlockAPI;
import olib.api.WorldAPI;

public class CustomModel {
	private BlockAPI list;
	private double roX, roY,roZ;
	public CustomModel(String name, double x, double y, double z, double x2, double y2, double z2) {
		list = WorldAPI.getBlock(WorldAPI.getWorld(), x, x2, y, y2, z, z2);
	}

	public BlockAPI getList() {
		return list;
	}
	public double getRoX() {
		return roX;
	}
	public double getRoY() {
		return roY;
	}
	public double getRoZ() {
		return roZ;
	}
	public void setRoX(double roX) {
		this.roX = roX;
	}
	public void setRoY(double roY) {
		this.roY = roY;
	}
	public void setRoZ(double roZ) {
		this.roZ = roZ;
	}
	
}
