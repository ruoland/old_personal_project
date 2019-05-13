package map.escaperoom.dungeon;

import oneline.effect.AbstractTick;
import oneline.effect.TickRegister;
import oneline.map.EntityDefaultNPC;
import oneline.map.EnumModel;
import oneline.map.TypeModel;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EntityRespawnZombie extends EntityDefaultNPC {
    public EntityRespawnZombie(World worldIn) {
        super(worldIn);
        typeModel = TypeModel.ZOMBIE;
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (getHealth() - amount <= 0) {
            setCorpse();
            setHealth(10);
            setSize(3, 1);
        }
        return super.attackEntityFrom(source, amount);

    }

    public void setCorpse() {
        setXYZ(EnumModel.ROTATION, 90, 0, 90);
        setXYZ(EnumModel.TRANSLATION, 0, 1.3F, 0);
        setSturn(200);
    }

    @Override
    public void onSturn() {
        super.onSturn();
        if (!isSturn() && isServerWorld()) {
            TickRegister.register(new AbstractTick(TickEvent.Type.SERVER, 1, true) {
                @Override
                public boolean stopCondition() {
                    return isDead;
                }

                float a = 0;
                @Override
                public void run(TickEvent.Type type) {
                    a += 1F;
                    setXYZ(EnumModel.ROTATION, 90F-a, 0, 90F-a);
                    if (a >= 90F) {
                        stopTick();
                    }
                }
            });
            TickRegister.register(new AbstractTick(TickEvent.Type.SERVER, 1, true) {
                float a = 0;

                @Override
                public boolean stopCondition() {
                    return isDead;
                }

                @Override
                public void run(TickEvent.Type type) {
                    a += 0.01F / 1.3F;
                    setXYZ(EnumModel.TRANSLATION, 0, 1.3F-a, 0);
                    if (a >= 1.3F) {
                        stopTick();
                    }
                }
            });
        }

    }
}
