package ruo.yout.mojaelab;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import olib.api.EntityAPI;
import olib.api.WorldAPI;
import ruo.yout.EntityFlyingCreeperLab;
import ruo.yout.EntityMoJaeCreeper;
import ruo.yout.EntityMojaeArrow;
import ruo.yout.Mojae;

public class LabEvent {

    @SubscribeEvent
    public void event(LivingSpawnEvent event) {
            if (!(event.getEntityLiving() instanceof EntityAnimal)) {
            if (event.getEntityLiving() instanceof EntityMob && Mojae.morespawn && !event.getEntityLiving().getEntityData().hasKey("mojae")) {
                if(event.getEntityLiving() instanceof EntityCreeper){
                    event.getEntityLiving().setDead();
                }
                if(event.getEntityLiving() instanceof EntityEnderman){
                    event.getEntityLiving().setHealth(20);
                    event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
                }
                for (int i = 0; i < 7; i++) {
                    double x = event.getX() + WorldAPI.rand(30);
                    double z = event.getZ() + WorldAPI.rand(30);
                    EntityLiving creeper = null;
                    switch (event.getWorld().rand.nextInt(5)) {
                        case 0: {
                            //creeper = new EntityCreeper(event.getWorld());
                            break;
                        }
                        case 1: {
                            creeper = new EntityZombie(event.getWorld());
                            break;
                        }
                        case 2: {
                            creeper = new EntitySkeleton(event.getWorld());
                            break;
                        }
                        case 3: {
                            creeper = new EntitySpider(event.getWorld());
                            break;
                        }
                        case 4: {
                            creeper = new EntityEnderman(event.getWorld());
                            break;
                        }
                    }
                    if (creeper != null) {
                        creeper.setPosition(x, event.getY(), z);
                        event.getWorld().spawnEntityInWorld(creeper);
                        creeper.onInitialSpawn(event.getWorld().getDifficultyForLocation(creeper.getPosition()), null);
                        creeper.getEntityData().setBoolean("mojae", true);
                    }

                }
            }
        }
    }

    @SubscribeEvent
    public void livingAttackEvent(LivingHurtEvent event) {
        if (Mojae.skelreeper && event.getEntityLiving() instanceof EntitySkeleton) {
            if (event.getSource().getEntity() instanceof EntityMoJaeCreeper) {
                event.setCanceled(true);
                event.setAmount(0);
                event.getEntityLiving().setVelocity(0, 0, 0);
            }
        }
        if (event.getEntityLiving() instanceof EntityFlyingCreeperLab && event.getSource().getEntity() instanceof EntityFlyingCreeperLab) {
            event.setCanceled(true);
            event.setAmount(0);
            event.getEntityLiving().setVelocity(0, 0, 0);
        }
    }

    @SubscribeEvent
    public void livingAttackEvent(LivingAttackEvent event) {
        if (event.getSource() instanceof EntityDamageSourceIndirect) {
            EntityDamageSourceIndirect sourceIndirect = (EntityDamageSourceIndirect) event.getSource();
            if (Mojae.arrowRiding) {
                if (sourceIndirect.getSourceOfDamage() instanceof EntityArrow) {
                    event.setCanceled(true);
                }
                System.out.println("타입 " + sourceIndirect.damageType);
                System.out.println("소스오브 " + sourceIndirect.getSourceOfDamage());
                System.out.println("엔티티 " + sourceIndirect.getEntity());
            }
            if (Mojae.skelreeper) {
                if (event.getEntityLiving().getEntityData().getBoolean("isArrowreper") && sourceIndirect.getSourceOfDamage() instanceof EntityArrow) {
                    event.setCanceled(true);
                }
                System.out.println("타입 " + sourceIndirect.damageType);
                System.out.println("소스오브 " + sourceIndirect.getSourceOfDamage());
                System.out.println("엔티티 " + sourceIndirect.getEntity());
                System.out.println("엔티티2 " + event.getEntityLiving());
            }
            if (Mojae.wither && event.getEntityLiving() instanceof EntityWither) {
                EntityWither wither = (EntityWither) event.getEntityLiving();
                System.out.println("  타입 " + sourceIndirect.damageType);
                System.out.println("  소스오브 " + sourceIndirect.getSourceOfDamage());
                System.out.println("  엔티티 " + sourceIndirect.getEntity());
                System.out.println("  엔티티2 " + event.getEntityLiving());
                System.out.println("  데미지" + event.getAmount());
            }
            //System.out.println("  2222타입 " + sourceIndirect.damageType);//2222타입 arrow
            //System.out.println("  2222소스오브 " + sourceIndirect.getSourceOfDamage());//2222소스오브 EntityTippedArrow['화살'/54558, l='test', x=415.04, y=5.64, z=-33.54]
            //System.out.println("  2222엔티티 " + sourceIndirect.getEntity());//  2222엔티티 EntitySkeleton['스켈레톤'/49328, l='test', x=414.96, y=4.00, z=-33.65]
            //System.out.println("  2222엔티티2 " + event.getEntityLiving());// 2222엔티티2 EntitySkeleton['스켈레톤'/20671, l='test', x=413.61, y=4.00, z=-35.18]
            //System.out.println("  2222데미지"+event.getAmount());// 2222데미지4.0
            if ((!(event.getEntityLiving() instanceof EntityPlayer) && event.getSource().getEntity() != null && Mojae.canTeamKill && EntityList.getEntityString(event.getEntityLiving()).equalsIgnoreCase(EntityList.getEntityString(event.getSource().getEntity())))) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void joinWorld(EntityJoinWorldEvent event) {

        if (event.getEntity() instanceof EntityArrow) {
            if (checkArrow((EntityArrow) event.getEntity()))
                return;
            EntityArrow arrow = (EntityArrow) event.getEntity();
            if (arrow.pickupStatus == EntityArrow.PickupStatus.DISALLOWED)
                arrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
            if (Mojae.wither && !(arrow instanceof EntityMojaeArrow) && !event.getWorld().isRemote) {
                EntityMojaeArrow arrow1 = new EntityMojaeArrow(event.getWorld());
                arrow1.readFromNBT(EntityAPI.getNBT(arrow));
                arrow.setDead();
                event.getWorld().spawnEntityInWorld(arrow1);
                arrow1.setPosition(arrow1.posX, arrow1.posY + 1, arrow1.posZ);
            }
        }

        if (Mojae.arrow_count > 1 && event.getEntity() instanceof EntityArrow) {//한번에 나가는 화살을 조절함
            EntityArrow arrow = (EntityArrow) event.getEntity();
            if (arrow.shootingEntity instanceof EntitySkeleton) {
                EntitySkeleton skeleton = (EntitySkeleton) arrow.shootingEntity;
                NBTTagCompound tagCompound = skeleton.getEntityData();
                int attackDelay = tagCompound.getInteger("AttackDelay");//딜레이가 없으면 이벤트로 추가 소환된 화살이 이벤트에 반응하여 무한 반복됨
                tagCompound.setInteger("AttackDelay", 5);//그래서 5틱의 딜레이를 엔티티에게 부여함
                if (attackDelay == 0) {
                    for (int i = 0; i < Mojae.arrow_count; i++) {//설정된 값만큼 반복함
                        EntityLivingBase attackTarget = skeleton.getAttackTarget();
                        EntityZombie zombie = new EntityZombie(event.getWorld());//화살 쏘는 메서드가 엔티티 객체를 필요로 해서 만듬
                        zombie.setPosition(attackTarget.posX + WorldAPI.rand(3), attackTarget.posY, attackTarget.posZ + WorldAPI.rand(3));
                        skeleton.attackEntityWithRangedAttack(zombie, ItemBow.getArrowVelocity(skeleton.getItemInUseMaxCount()));
                    }
                }
            }
        }
    }


    /**
     * //화살을 쏘면 그 위에  크리퍼를 태우거나 화살 대신 크리퍼 나오게 하는 코드. 스켈레톤 플레이어 둘다 포함
     */
    public boolean checkArrow(EntityArrow arrow) {
        if (Mojae.skelreeper && arrow.shootingEntity instanceof EntitySkeleton) {
            arrowReeper(arrow);
            return true;
        }
        if (Mojae.arrowReeper && arrow.shootingEntity instanceof EntityPlayer) {
            arrowReeper(arrow);
            return true;
        }
        if (Mojae.arrowRiding && arrow.shootingEntity instanceof EntitySkeleton) {
            arrowRiding(arrow);
            return true;

        }
        if (Mojae.arrowRiding && arrow.shootingEntity instanceof EntityPlayer) {
            arrowRiding(arrow);
            return true;
        } else
            return false;
    }

    public void arrowReeper(EntityArrow arrow) {
        EntityMoJaeCreeper creeper = new EntityMoJaeCreeper(arrow.worldObj);
        creeper.setPosition(arrow.posX, arrow.posY, arrow.posZ);
        creeper.setVelocity(arrow.motionX, arrow.motionY, arrow.motionZ);
        arrow.setDead();
        arrow.worldObj.spawnEntityInWorld(creeper);
        creeper.ignite();
        creeper.getEntityData().setBoolean("isArrowreper", true);
    }

    public void arrowRiding(EntityArrow arrow) {
        arrow.shootingEntity.startRiding(arrow);
    }

    @SubscribeEvent
    public void event(LivingEvent.LivingUpdateEvent event) {
        if (Mojae.skelDelay != -1) {
            if (event.getEntityLiving() instanceof EntitySkeleton) {
                EntitySkeleton skeleton = (EntitySkeleton) event.getEntityLiving();
                NBTTagCompound tagCompound = skeleton.getEntityData();
                if (tagCompound.hasKey("AttackDelay") && tagCompound.getInteger("AttackDelay") > 0) {
                    tagCompound.setInteger("AttackDelay", tagCompound.getInteger("AttackDelay") - 1);
                }
                for (EntityAITasks.EntityAITaskEntry aiBase : skeleton.tasks.taskEntries) {
                    if (aiBase.action instanceof EntityAIAttackRangedBow) {
                        if (!(aiBase.action instanceof EntityAIMojaeAttackRangedBow)) {
                            skeleton.tasks.removeTask(aiBase.action);
                            skeleton.tasks.addTask(0, new EntityAIMojaeAttackRangedBow(skeleton, 1, Mojae.skelDelay, 15));
                        }
                        break;
                    }
                }
            }
        }
        if (event.getEntityLiving() instanceof EntityCreature) {
            EntityCreature living = (EntityCreature) event.getEntityLiving();
            NBTTagCompound tagCompound = living.getEntityData();
            if (!tagCompound.hasKey("isDelay0")) {
                if (living.getAttackTarget() != null) {
                    for (EntityAITasks.EntityAITaskEntry aiBase : living.tasks.taskEntries) {
                        if (aiBase.action instanceof EntityAIAttackMelee && !(aiBase.action instanceof EntityAIMojaeAttackMelee)) {//어택 밀리 클래스를 새로 생성해서 공격 딜레이를 0으로 만드려고 하는 중이었음
                            living.tasks.removeTask(aiBase.action);
                            living.tasks.addTask(0, new EntityAIMojaeAttackMelee(living, 1, false));
                            tagCompound.setBoolean("isDelay0", true);
                        }
                        break;
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public void event(EntityJoinWorldEvent event) {

        if (event.getEntity() instanceof EntityLiving) {
            EntityLiving living = (EntityLiving) event.getEntity();
            String monsterName = EntityList.getEntityString(living);
            if (Mojae.dog_pan) {
                if (living instanceof EntityMob) {
                    EntityMob mob = (EntityMob) living;
                    mob.targetTasks.addTask(2, new EntityAINearestAttackableTarget(mob, EntityLiving.class, false));
                }
            }
            if (Mojae.monterAttack.containsKey(monsterName)) {
                String attackKey = Mojae.monterAttack.get(monsterName);
                Class entityClass = EntityList.NAME_TO_CLASS.get(attackKey);

                if (living instanceof EntityMob) {
                    EntityMob mob = (EntityMob) living;
                    mob.targetTasks.addTask(2, new EntityAINearestAttackableTarget(mob, entityClass, false));
                }
            }
        }
    }
}
