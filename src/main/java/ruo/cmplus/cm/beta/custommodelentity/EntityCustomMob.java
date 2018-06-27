package ruo.cmplus.cm.beta.custommodelentity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import ruo.minigame.api.BlockAPI;
import ruo.minigame.api.WorldAPI;

import java.util.ArrayList;
import java.util.HashMap;

public class EntityCustomMob extends EntityMob {
	private double x, y, z, x2, y2, z2;
	private static HashMap<String, BlockAPI> listMap = new HashMap<>();
	private BlockAPI blockList;
	private ArrayList<CustomModel> model = new ArrayList<CustomModel>();

	public EntityCustomMob(World worldIn) {
		super(worldIn);
		this.setSize(1, 1);
	}

	public EntityCustomMob(World worldIn, String name, double px, double py, double pz, double x, double y, double z,
			double x2, double y2, double z2) {
		super(worldIn);
		this.setSize(1,1);
		this.x = x;
		this.y = y;
		this.z = z;
		this.x2 = x2;
		this.y2 = y2;
		this.z2 = z2;
		this.setCustomNameTag(name);
		this.setPosition(px, py, pz);
		blockList = WorldAPI.getBlock(worldObj, x, x2, y, y2, z, z2);
		listMap.put(getName(), blockList);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox() {
		return getEntityBoundingBox();
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity entityIn) {
		return getEntityBoundingBox();
	}

	public static BlockAPI getBlockList(String name) {
		return listMap.get(name);
	}

	public void addModel(CustomModel cmodel) {
		this.model.add(cmodel);
	}

	public ArrayList<CustomModel> getModel() {
		return model;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setDouble("x", x);
		compound.setDouble("y", y);
		compound.setDouble("z", z);
		compound.setDouble("x2", x2);
		compound.setDouble("y2", y2);
		compound.setDouble("z2", z2);
		//RuoAPI.saveObject("blockList.dat", blockList);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.x = compound.getDouble("x");
		this.y = compound.getDouble("y");
		this.z = compound.getDouble(" z");
		this.x2 = compound.getDouble("x2");
		this.y2 = compound.getDouble("y2");
		this.z2 = compound.getDouble("z2");
		//blockList = RuoAPI.loadList("blockList.dat");
	}
}
