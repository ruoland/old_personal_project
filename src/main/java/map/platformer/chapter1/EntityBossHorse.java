package map.platformer.chapter1;

import oneline.api.WorldAPI;
import oneline.effect.AbstractTick;
import oneline.map.EntityDefaultNPC;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.HorseType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

public class EntityBossHorse extends EntityHorse {
    public EntityBossHorse(World world){
        super(world);
    }

    protected void initEntityAI()
    {
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
        if(hand == EnumHand.MAIN_HAND){
            delay = 60;
            if(getPassengers().size() > 0) {
                getPassengers().get(0).startRiding(player);
            }
        }
        return true;
    }

    public void updatePassenger(Entity passenger)
    {
        if (this.isPassenger(passenger))
        {
            passenger.setPosition(this.posX, this.posY +this.getMountedYOffset() + passenger.getYOffset(), this.posZ);
        }
    }

    public void horseRear(){
        try {
            Method method = this.getClass().getSuperclass().getDeclaredMethod("makeHorseRear");
            method.setAccessible(true);
            method.invoke(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private int delay;
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.setType(HorseType.HORSE);
        this.setGrowingAge(10000);

        if(isServerWorld() && delay >= 60){
            delay --;
            if(delay == 0){
                //firstPattern();
            }
        }
    }

    public void firstPattern(){
        horseRear();
        WorldAPI.blockTick(worldObj, 0,0,0,0,0,0, new AbstractTick.BlockXYZ() {
            @Override
            public void run(TickEvent.Type type) {
                EntityDefaultNPC lavaBlock = new EntityDefaultNPC(worldObj);
                lavaBlock.setPosition(x, y, z);
                lavaBlock.setBlock(Blocks.STONE);
                worldObj.spawnEntityInWorld(lavaBlock);
                worldObj.setBlockState(getPos(), Blocks.STONE.getDefaultState());
            }
        });
        this.setAttackTarget(WorldAPI.getPlayer());
    }

    public void spawnMagma(){

    }

}
