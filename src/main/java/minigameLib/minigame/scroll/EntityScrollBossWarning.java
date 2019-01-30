package minigameLib.minigame.scroll;

import minigameLib.map.EntityDefaultNPC;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class EntityScrollBossWarning extends EntityDefaultNPC {
    public EntityScrollBossWarning(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.WOOL);
        setBlockMetadata(14);
        setDeathTimer(60);
    }

    @Override
    public void onTimerDeath() {
        super.onTimerDeath();
        System.out.println("죽음");
        EntityScrollBossFallingBlock fallingBlock = new EntityScrollBossFallingBlock(worldObj);
        fallingBlock.setPosition(posX, posY+10, posZ);
        worldObj.spawnEntityInWorld(fallingBlock);
    }
}