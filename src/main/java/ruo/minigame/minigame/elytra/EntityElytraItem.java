package ruo.minigame.minigame.elytra;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ruo.minigame.MiniGame;
import ruo.minigame.api.SpawnDirection;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.fakeplayer.FakePlayerHelper;
import ruo.minigame.map.EntityDefaultNPC;

public class EntityElytraItem extends EntityDefaultNPC {
    private Vec3d vec3d;

    public EntityElytraItem(World worldIn, double x, double y, double z) {
        super(worldIn);
        this.setBlockMode(Blocks.CHEST);
        this.addRotate(180, 0, 0);
        this.setPosition(x, y, z);
        this.setSize(1, 5);
        vec3d = FakePlayerHelper.fakePlayer.getXZ(SpawnDirection.BACK, 20, false).normalize();
        this.setDeathTimer(500);
    }

    public EntityElytraItem(World world) {
        super(world);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(FakePlayerHelper.fakePlayer != null) {
            if (vec3d != null) {
                System.out.println("플레이어 뒤로 이동하는 중" + vec3d.xCoord + " - " + vec3d.zCoord);
                this.setVelocity(vec3d.xCoord, 0, vec3d.zCoord);
            }
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(source.getEntity() instanceof EntityFakePlayer || source.getEntity() instanceof EntityElytraArrow)
        {
            if(FakePlayerHelper.fakePlayer.getDistance(posX, posY, posZ) < 8) {
                if (rand.nextBoolean()) {
                    if (!MiniGame.elytra.arrowUpgrade) {
                        WorldAPI.addMessage("화살이 이제 세개씩 나갑니다.");
                        MiniGame.elytra.arrowUpgrade = true;
                    }
                } else {
                    MiniGame.elytra.bombCount++;
                    WorldAPI.addMessage("폭탄 아이템이 추가됐습니다.");
                }
                this.setDead();
            }
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        super.collideWithEntity(entityIn);
        System.out.println(entityIn);
        if (entityIn instanceof EntityFakePlayer) {
            if (!MiniGame.elytra.arrowUpgrade)
                MiniGame.elytra.arrowUpgrade = true;
            this.setDead();
        }
    }
}
