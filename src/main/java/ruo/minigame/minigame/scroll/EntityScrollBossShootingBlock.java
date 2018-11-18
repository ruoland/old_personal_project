package ruo.minigame.minigame.scroll;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import ruo.minigame.MiniGame;
import ruo.minigame.fakeplayer.FakePlayerHelper;
import ruo.minigame.map.EntityDefaultNPC;

import java.util.List;

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