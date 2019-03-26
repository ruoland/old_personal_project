package minigameLib.minigame.scroll;

import minigameLib.MiniGame;
import oneline.map.EntityDefaultNPC;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class EntityScrollBossShootingBlock extends EntityDefaultNPC {
    public EntityScrollBossShootingBlock(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.STONE);
    }

    public void shotingTarget(){
        MiniGame.scroll.pitch += 90;
        setTarget(posX+(MiniGame.scroll.getBackX() * 10), posY, posZ+(MiniGame.scroll.getBackZ() * 10));
    }
}