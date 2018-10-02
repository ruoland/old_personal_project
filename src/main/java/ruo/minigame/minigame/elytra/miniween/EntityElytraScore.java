package ruo.minigame.minigame.elytra.miniween;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import ruo.minigame.MiniGame;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.map.EntityDefaultNPC;
import ruo.minigame.minigame.elytra.Elytra;
import scala.xml.dtd.EntityDef;

public class EntityElytraScore extends EntityDefaultNPC {
    private int score;

    public EntityElytraScore(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.GOLD_BLOCK);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        super.collideWithEntity(entityIn);
        if (entityIn instanceof EntityFakePlayer) {
            WorldAPI.addMessage("점수가 " + score + "만큼 상승하였습니다.");
        }
    }
}
