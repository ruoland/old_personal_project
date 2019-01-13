package ruo.minigame.minigame.minerun;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import ruo.minigame.map.EntityDefaultNPC;
import ruo.minigame.map.TypeModel;

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
