package map.puzzle.dungeon;

import minigameLib.map.EntityDefaultNPC;
import minigameLib.map.EnumModel;
import minigameLib.map.TypeModel;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

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
        if(getHealth() - amount <= 0){
            setCorpse();
            setHealth(10);
            setSize(3,1);
        }
        return super.attackEntityFrom(source, amount);

    }

    public void setCorpse(){
        setXYZ(EnumModel.ROTATION, 90, 0,90);
        setXYZ(EnumModel.TRANSLATION,0,1.3F,0);
        setSturn(200);
    }
}
