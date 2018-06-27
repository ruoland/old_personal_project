package ruo.minigame.minigame.paint;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityBackground extends EntityMob {

	public String texture;

	public EntityBackground(World worldIn) {
		super(worldIn);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setString("texture", texture);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		texture = compound.getString("texture");
	}
}
