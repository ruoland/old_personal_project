package ruo.asdfwild.boss;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import oneline.api.EntityAPI;
import oneline.map.EntityDefaultNPC;

import java.util.List;

//앞으로 나아가면서 플레이어를 밀어내는 블럭
public class EntityKnockbackWall extends EntityDefaultNPC {
    private EnumFacing forward;
    private double forwardX = EntityAPI.lookX(forward, 1);
    private double forwardZ = EntityAPI.lookZ(forward, 1);
    public EntityKnockbackWall(World worldIn) {
        super(worldIn);
        this.setDeathTimer(102);
    }

    public void setForward(EnumFacing forwardFacing) {
        this.forward = forwardFacing;
        forwardX = EntityAPI.lookX(forward, 1);;
        forwardZ = EntityAPI.lookZ(forward, 1);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(forward != null) {
            motionX = forwardX;
            motionZ = forwardZ;
            List<EntityLivingBase> livingBaseList = EntityAPI.getEntity(worldObj, getEntityBoundingBox().addCoord(forwardX, 0, forwardZ),EntityLivingBase.class);
            for(EntityLivingBase livingBase : livingBaseList){
                livingBase.knockBack(this, 0.4F, this.posX - livingBase.posX, this.posZ - livingBase.posZ);
            }
        }
    }
}
