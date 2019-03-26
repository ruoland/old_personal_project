package oneline.effect;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class LoopMoveHelper extends EntityMoveHelper{

	public LoopMoveHelper(EntityLiving entitylivingIn) {
		super(entitylivingIn);
		
	}

	int count;//같은 좌표인 경우 카운트가 추가됨
	double prevPosX, prevPosZ, prevPosY;
	@Override
	public void onUpdateMoveHelper() {
        double eX = Double.valueOf(String.format("%.1f",entity.posX));
        double eY = Double.valueOf(String.format("%.1f",entity.posY));
        double eZ = Double.valueOf(String.format("%.1f",entity.posZ));
        if (this.action == EntityMoveHelper.Action.STRAFE)
        {
            float f = (float)this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
            float f1 = (float)this.speed * f;
            float f2 = this.moveForward;
            float f3 = this.moveStrafe;
            float f4 = MathHelper.sqrt_float(f2 * f2 + f3 * f3);

            if (f4 < 1.0F)
            {
                f4 = 1.0F;
            }

            f4 = f1 / f4;
            f2 = f2 * f4;
            f3 = f3 * f4;
            float f5 = MathHelper.sin(this.entity.rotationYaw * 0.017453292F);
            float f6 = MathHelper.cos(this.entity.rotationYaw * 0.017453292F);
            float f7 = f2 * f6 - f3 * f5;
            float f8 = f3 * f6 + f2 * f5;
            PathNavigate pathnavigate = this.entity.getNavigator();

            if (pathnavigate != null)
            {
                NodeProcessor nodeprocessor = pathnavigate.getNodeProcessor();

                if (nodeprocessor != null && nodeprocessor.getPathNodeType(this.entity.worldObj, MathHelper.floor_double(this.entity.posX + (double)f7), MathHelper.floor_double(this.entity.posY), MathHelper.floor_double(this.entity.posZ + (double)f8)) != PathNodeType.WALKABLE)
                {
                    this.moveForward = 1.0F;
                    this.moveStrafe = 0.0F;
                    f1 = f;
                }
            }

            this.entity.setAIMoveSpeed(f1);
            this.entity.setMoveForward(this.moveForward);
            this.entity.setMoveStrafing(this.moveStrafe);
            this.action = EntityMoveHelper.Action.WAIT;
        }
        else if (this.action == EntityMoveHelper.Action.MOVE_TO)
        {
            this.action = EntityMoveHelper.Action.WAIT;
            double d0 = this.posX - this.entity.posX;
            double d1 = this.posZ - this.entity.posZ;
            double d2 = this.posY - this.entity.posY;
            double d3 = d0 * d0 + d2 * d2 + d1 * d1;

            if (d3 < 2.500000277905201E-7D)
            {
                this.entity.setMoveForward(0.0F);
                return;
            }

            float f9 = (float)(MathHelper.atan2(d1, d0) * (180D / Math.PI)) - 90.0F;
            this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, f9, 90.0F);
            this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));

            int posX = MathHelper.floor_double(eX + entity.getLookVec().xCoord);
            int posY = MathHelper.floor_double(eY+1);
            int posZ = MathHelper.floor_double(eZ + entity.getLookVec().zCoord);

            if((prevPosX == eX && prevPosY == eY && prevPosZ == eZ)){
            	BlockPos pos = new BlockPos(posX, posY, posZ);
            	String block = entity.worldObj.getBlockState(pos).getBlock().getUnlocalizedName();
            	if(isAirBlock(posX, posY, posZ)){
            		this.entity.getJumpHelper().setJumping();
            		this.entity.addVelocity(entity.getLookVec().xCoord * 0.1, 0, entity.getLookVec().zCoord * 0.1);
            	}
            }else{
            	count = 0;
            }
            prevPosX = Double.valueOf(String.format("%.1f",eX));
            prevPosY = Double.valueOf(String.format("%.1f",eY));
            prevPosZ = Double.valueOf(String.format("%.1f",eZ));

        }
        else
        {
            this.entity.setMoveForward(0.0F);
        }
    }
	
	public boolean isAirBlock(double x, double y, double z){
    	BlockPos pos = new BlockPos(posX+x, posY+y, posZ+z);
    	String block = entity.worldObj.getBlockState(pos).getBlock().getUnlocalizedName();
    	return block.indexOf("torch") != -1 || block.indexOf("snow") != -1 || entity.worldObj.getBlockState(pos) == Blocks.AIR.getDefaultState() || block.indexOf("door") != -1 || block.equals("tile.ladder");
	}
	
}
