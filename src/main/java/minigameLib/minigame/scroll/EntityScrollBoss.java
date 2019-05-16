package minigameLib.minigame.scroll;

import minigameLib.MiniGame;
import olib.fakeplayer.FakePlayerHelper;
import olib.map.EntityDefaultNPC;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityScrollBoss extends EntityDefaultNPC {
    public EntityScrollBoss(World worldIn) {
        super(worldIn);
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        FakePlayerHelper.spawnFakePlayer(false);
        return super.onInitialSpawn(difficulty, livingdata);
    }

    private int delay = 0;
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        delay--;
        if(delay <= 0 && MiniGame.scroll.isStart()){
            delay = 40;
            //EntityScrollBossWarning scrollBossWarning = new EntityScrollBossWarning(worldObj);
            //scrollBossWarning.setPosition(posX+  WorldAPI.rand((int) (MiniGame.scroll.getRightX() * 5)), posY, posZ+ WorldAPI.rand((int) (MiniGame.scroll.getRightZ() * 5)));
            //if(isServerWorld())
             //   worldObj.spawnEntityInWorld(scrollBossWarning);

            EntityScrollBossShootingBlock scrollBossShootingBlock = new EntityScrollBossShootingBlock(worldObj);
            scrollBossShootingBlock.setPosition(posX,posY,posZ);
            if(isServerWorld())
                worldObj.spawnEntityInWorld(scrollBossShootingBlock);

            scrollBossShootingBlock.shotingTarget();


        }
    }
}
