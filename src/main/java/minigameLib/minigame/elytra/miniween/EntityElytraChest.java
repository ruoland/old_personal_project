package minigameLib.minigame.elytra.miniween;

import minigameLib.MiniGame;
import oneline.api.WorldAPI;
import oneline.fakeplayer.EntityFakePlayer;
import oneline.map.EntityDefaultNPC;
import minigameLib.minigame.elytra.Elytra;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityElytraChest extends EntityDefaultNPC {
    public EntityElytraChest(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.CHEST);
        this.setRotate(0, 0, 90);
        isFly = true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getEntity() instanceof EntityFakePlayer) {
            if (isServerWorld()) {
                upgrade();
                this.setDead();
            }
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        teleportSpawnPos();
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
                if (!Elytra.tntArrow) {
                    Elytra.tntArrow = true;
                    WorldAPI.addMessage("이제 작은 미사일이 같이 발사됩니다!");
                    break;
                } else upgrade();
            case 4:
                if (Elytra.defaultCooltime == 10) {
                    Elytra.defaultCooltime = 5;
                    WorldAPI.addMessage("화살이 더 많이 발사됩니다.");
                    break;
                } else upgrade();
                break;

        }
    }
}
