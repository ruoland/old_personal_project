package map.escaperoom.nouse;

import map.escaperoom.nouse.base.EntityRoomDoorBase;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class EntityRoomDoor extends EntityRoomDoorBase {

    public EntityRoomDoor(World worldIn) {
        super(worldIn);

        this.setBlockMode(Blocks.STONE);
        this.setSize(3, 5);
        this.setScale(3, 5, 1);
        setCollision(true);
    }

}
