package minigameLib.minigame.minerun.monster;

import minigameLib.minigame.minerun.EntityMR;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class EntityMRTNT extends EntityMR {
    public EntityMRTNT(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.TNT);
    }

    @Override
    public void collideAttack(Entity entityIn) {
        super.collideAttack(entityIn);
        worldObj.createExplosion(this, posX, posY, posZ, 1.5F, false);
    }
}
