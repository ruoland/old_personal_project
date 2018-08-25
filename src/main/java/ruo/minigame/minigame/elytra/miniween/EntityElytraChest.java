package ruo.minigame.minigame.elytra.miniween;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import ruo.minigame.MiniGame;
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
            if(MiniGame.elytraEvent.defaultCooltime == 15) {
                MiniGame.elytraEvent.defaultCooltime = 7;
                return;
            }

            if(!Elytra.tripleArrow) {
                Elytra.tripleArrow = true;
                return;
            }
            Elytra.bombCount++;
        }
    }
}
