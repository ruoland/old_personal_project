package minigameLib.minigame.minerun;

import oneline.map.TypeModel;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityMRChicken extends EntityMR {
    public EntityMRChicken(World worldIn) {
        super(worldIn);
        this.setModel(TypeModel.CHICKEN);
        this.setSize(1,1);
    }

    @Override
    public void collideAttack(Entity entityIn) {
        super.collideAttack(entityIn);
         
    }
}
