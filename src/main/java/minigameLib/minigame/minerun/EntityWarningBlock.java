package minigameLib.minigame.minerun;

import minigameLib.map.EntityDefaultNPC;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityWarningBlock extends EntityDefaultNPC {
    public EntityWarningBlock(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.REDSTONE_BLOCK);
        this.setCollision(false);
     }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.teleportSpawnPos();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }
}
