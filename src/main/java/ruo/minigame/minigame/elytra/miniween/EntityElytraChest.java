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
        this.setRotate(0, 0, 90);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        super.collideWithEntity(entityIn);
        if (entityIn instanceof EntityFakePlayer) {
            if (Elytra.defaultCooltime == 15) {
                Elytra.defaultCooltime = 7;
                WorldAPI.addMessage("공격 속도가 상승하였습니다!");
                return;
            }

            if (!Elytra.tripleArrow) {
                Elytra.tripleArrow = true;
                WorldAPI.addMessage("화살이 3발씩 나갑니다!");
                return;
            }

            MiniGame.elytra.addBombCount();
        }
    }

    public void upgrade() {
        switch (rand.nextInt(5)) {
            case 0:
                if (Elytra.defaultCooltime == 15) {
                    Elytra.defaultCooltime = 7;
                    WorldAPI.addMessage("공격 속도가 상승하였습니다!");
                    return;
                } else upgrade();
                break;

            case 1:
                if (!Elytra.tripleArrow) {
                    Elytra.tripleArrow = true;
                    WorldAPI.addMessage("화살이 3발씩 나갑니다!");
                } else upgrade();
                break;

            case 2:
                MiniGame.elytra.addBombCount();
                WorldAPI.addMessage("화살 폭탄이 1개 추가됐습니다!(B키를 누르면 사용할 수 있음)");
                break;
            case 3:
                Elytra.tntArrow = true;
                WorldAPI.addMessage("이제 작은 미사일이 같이 발사됩니다!");
                break;
        }
    }
}
