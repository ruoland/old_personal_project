package ruo.minigame.minigame.elytra.miniween;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import ruo.minigame.MiniGame;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.map.EntityDefaultNPC;
import ruo.minigame.minigame.elytra.Elytra;

public class EntityElytraChest extends EntityDefaultNPC {
    public EntityElytraChest(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.CHEST);
        this.setRotate(0,0,90);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        super.collideWithEntity(entityIn);
        if(entityIn instanceof EntityFakePlayer){
            if(Elytra.defaultCooltime == 15) {
                Elytra.defaultCooltime = 7;
                WorldAPI.addMessage("공격 속도가 상승하였습니다!");
                return;
            }

            if(!Elytra.tripleArrow) {
                Elytra.tripleArrow = true;
                WorldAPI.addMessage("화살이 3발씩 나갑니다!");
                return;
            }


            MiniGame.elytra.addBombCount();
        }
    }
}
