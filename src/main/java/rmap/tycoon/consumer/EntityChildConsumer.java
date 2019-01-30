package rmap.tycoon.consumer;

import minigameLib.api.WorldAPI;
import minigameLib.map.TypeModel;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityChildConsumer extends EntityConsumer {
    private double x, z;
    public EntityChildConsumer(World worldIn) {
        super(worldIn);
        this.setModel(TypeModel.NPC);
        this.setChild(true);
    }

    @Override
    public void startShoping() {
        if(!isRiding())
            super.startShoping();
    }

    @Override
    public boolean startRiding(Entity entityIn) {
        x = WorldAPI.rand(1);

        return super.startRiding(entityIn);
    }

    public void updatePassenger(Entity passenger)
    {
        if (this.isPassenger(passenger))
        {
            passenger.setPosition(this.posX+x, this.posY, this.posZ +z);
        }
    }
}
