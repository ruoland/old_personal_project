package map.puzzle;

import map.puzzle.base.EntityPuzzleDoorBase;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class EntityPuzzleDoor extends EntityPuzzleDoorBase {

    public EntityPuzzleDoor(World worldIn) {
        super(worldIn);

        this.setBlockMode(Blocks.STONE);
        this.setSize(3, 5);
        this.setScale(3, 5, 1);
        setCollision(true);
    }

}
