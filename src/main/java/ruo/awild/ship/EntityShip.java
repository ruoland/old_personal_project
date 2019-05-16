package ruo.awild.ship;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import olib.map.EntityDefaultBlock;

public class EntityShip extends EntityDefaultBlock {
    public EntityShip(World worldIn) {
        super(worldIn);
    }


    @Override
    public void updatePassenger(Entity passenger) {
        if (this.isPassenger(passenger))
        {
            passenger.setPosition(this.posX, passenger.posY, this.posZ);
        }
    }
}
