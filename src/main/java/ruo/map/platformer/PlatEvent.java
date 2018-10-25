package ruo.map.platformer;

import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.map.TypeModel;

public class PlatEvent {
    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(Plat.platCoin, Plat.chickenRiding, Plat.platCoinSpawn);
    }

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent e){
        if(WorldAPI.getCurrentWorldName().equalsIgnoreCase("Plat") && e.phase == TickEvent.Phase.END) {

            if (e.player.getRidingEntity() instanceof EntityChicken ) {
                if (!e.player.onGround) {
                    e.player.motionY += 0.4;
                    Vec3d vec = e.player.getLookVec();
                    e.player.addVelocity(vec.xCoord*1.2, 0, vec.zCoord*1.2);
                }
                if(PlatStage2.isFlyStart()){
                    if(e.player.onGround){
                        PlatStage2.flyGameEnd();
                    }
                }
            }
            if (false && PlatEffect.getCurrentStage() instanceof PlatStage1 && e.player.worldObj.rand.nextInt(100) == 0) {
                EntityLargeFireball fireball = new EntityLargeFireball(e.player.worldObj, e.player.posX + WorldAPI.rand(32), e.player.posY + 30, e.player.posZ + WorldAPI.rand(32), 0, -1, 0);
                fireball.explosionPower = 1;
                e.player.worldObj.spawnEntityInWorld(fireball);
            }
        }
    }

    @SubscribeEvent
    public void pickupItem(EntityItemPickupEvent e)
    {
        if(WorldAPI.getCurrentWorldName().equalsIgnoreCase("Plat")) {
            if (e.getItem() instanceof EntityCoin) {
                if (e.getItem().getEntityItem().getItem() == Items.NETHER_STAR) {
                    PlatEffect.missionComplete();
                }
                if (e.getItem().getEntityItem().getItem() == Plat.platCoin) {
                    PlatEffect.addCoin(1);
                }
            }
        }
    }
    @SubscribeEvent
    public void riding(PlayerInteractEvent.EntityInteract e)
    {
        if(WorldAPI.getCurrentWorldName().equalsIgnoreCase("Plat")) {
            if (!e.getWorld().isRemote) {
                if (e.getHand() == EnumHand.MAIN_HAND) {
                    if(PlatEffect.getCurrentStage() instanceof PlatStage4){
                        if (e.getTarget() instanceof EntityNPC) {
                            EntityNPC txtnpc = (EntityNPC) e.getTarget();
                            if(txtnpc.getModel() == TypeModel.COW){
                                txtnpc.startRiding(e.getEntityPlayer());
                            }
                        }
                    }
                    if (e.getTarget() instanceof EntityChicken) {
                        if (WorldAPI.findInventoryItemCount(e.getEntityPlayer(), Plat.chickenRiding) > 0)
                            e.getTarget().startRiding(e.getEntityPlayer());
                    }
                }
            }
        }
    }
}
