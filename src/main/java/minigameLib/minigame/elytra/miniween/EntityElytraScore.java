package minigameLib.minigame.elytra.miniween;

import minigameLib.api.WorldAPI;
import minigameLib.fakeplayer.EntityFakePlayer;
import minigameLib.map.EntityDefaultNPC;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

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
