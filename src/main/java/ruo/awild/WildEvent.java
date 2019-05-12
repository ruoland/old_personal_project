package ruo.awild;

import cmplus.cm.CommandChat;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.passive.HorseType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import oneline.api.BlockAPI;
import oneline.api.EntityAPI;
import oneline.api.WorldAPI;
import ruo.awild.ai.EntityAIAvoidEntityCreeper;
import ruo.awild.ai.EntityAIFindSound;

import java.util.List;
import java.util.Random;

public class WildEvent {
    @SubscribeEvent
    public void findSound(PlaySoundEvent event) {
        if (WorldAPI.getServer() != null  && event.getSound().getCategory() != SoundCategory.MUSIC
                && event.getSound().getCategory() != SoundCategory.WEATHER
                && event.getSound().getCategory() != SoundCategory.AMBIENT
                &&( event.getName().contains("player") || event.getName().contains("block"))) {
            ISound iSound = event.getResultSound();
            float expand = 5;

            List<EntityMob> livings = EntityAPI.getEntity(WorldAPI.getWorld(), new AxisAlignedBB(iSound.getXPosF() - expand, iSound.getYPosF() - expand, iSound.getZPosF() - expand,
                    iSound.getXPosF() + expand, iSound.getYPosF() + expand, iSound.getZPosF() + expand), EntityMob.class);
            for (EntityMob mob : livings) {
                if(mob.getAttackTarget() == null) {
                    for (EntityAITasks.EntityAITaskEntry task : mob.tasks.taskEntries) {
                        if (task.action instanceof EntityAIFindSound) {
                            EntityAIFindSound findSound = (EntityAIFindSound) task.action;
                            findSound.setSoundPos(new BlockPos(iSound.getXPosF(), iSound.getYPosF(), iSound.getZPosF()));
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void animalPanic(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof EntityAnimal && event.getSource().getEntity() instanceof EntityPlayer) {
            List<EntityAnimal> mobList = EntityAPI.getEntity(event.getEntityLiving().worldObj, event.getEntityLiving().getEntityBoundingBox().expandXyz(8), EntityAnimal.class);
            for (EntityAnimal animal : mobList) {
                if (animal.getEntitySenses().canSee(event.getEntityLiving())) {
                    animal.setRevengeTarget((EntityPlayer) event.getSource().getEntity());
                }
            }
        }
    }

    @SubscribeEvent
    public void event(LivingSetAttackTargetEvent event) {
        if (event.getEntityLiving() instanceof EntityEnderman) {
            if (event.getEntityLiving().worldObj.rand.nextInt(100) == 0) {
                World worldObj = ((EntityEnderman) event.getEntityLiving()).worldObj;
                EntityEnderman enderman = (EntityEnderman) event.getEntityLiving();
                EntityLivingBase target = event.getTarget();
                if (enderman.getHeldBlockState() != null && target != null) {
                    EntityEnderBlock enderBlock = new EntityEnderBlock(worldObj);
                    enderBlock.setPosition(enderman.posX, enderman.posY + target.getEyeHeight(), enderman.posZ);
                    if (enderman.getHeldBlockState() != null)
                        enderBlock.setBlockMode(enderman.getHeldBlockState().getBlock());
                    if (!worldObj.isRemote)
                        worldObj.spawnEntityInWorld(enderBlock);
                    enderBlock.setTarget(target.posX, target.posY + target.getEyeHeight(), target.posZ, 1);
                } else {
                    BlockAPI api = WorldAPI.getBlock(((EntityEnderman) event.getEntityLiving()).worldObj, enderman.posX - 3, enderman.posX + 3, enderman.posY - 1, enderman.posY + 2, enderman.posZ - 3, enderman.posZ + 3);
                    enderman.setHeldBlockState(api.getBlockState(0));
                    worldObj.setBlockToAir(api.getPos(0));
                }
            }
        }
    }

    @SubscribeEvent
    public void livingSpawn(EntityJoinWorldEvent event) {
        Random rand = event.getWorld().rand;
        World world = event.getWorld();
        if (event.getEntity() instanceof EntityCreature && event.getEntity() instanceof IMob) {
            EntityCreature mob = (EntityCreature) event.getEntity();
            mob.tasks.addTask(0, new EntityAIAvoidEntityCreeper(mob, 6.0F, 1.0D, 2.2D));
            mob.tasks.addTask(5, new EntityAIFindSound(mob));
        }
    }
    @SubscribeEvent
    public void livingSpawn(LivingSpawnEvent event) {
        Random rand = event.getWorld().rand;
        World world = event.getWorld();
        if (event.getEntityLiving() instanceof EntityCreature && event.getEntityLiving() instanceof IMob) {
            EntityCreature mob = (EntityCreature) event.getEntityLiving();
            if(!(mob instanceof EntityCreeper)) {
                mob.tasks.addTask(0, new EntityAIAvoidEntityCreeper(mob, 6.0F, 0.5D, 0.5D));
            }
            mob.tasks.addTask(5, new EntityAIFindSound(mob));
            mob.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128);
            if (event.getEntityLiving() instanceof EntityZombie && !(event.getEntityLiving() instanceof EntityWildZombie)) {//일반 좀비를 와일드 좀비로 변경
                NBTTagCompound tagCompound = new NBTTagCompound();
                event.getEntityLiving().writeEntityToNBT(tagCompound);
                EntityWildZombie wildZombie = new EntityWildZombie(event.getEntityLiving().worldObj);
                wildZombie.readEntityFromNBT(tagCompound);
                event.getEntityLiving().worldObj.spawnEntityInWorld(wildZombie);
                event.getEntityLiving().setDead();
            }
            if (event.getEntityLiving() instanceof EntityEnderman && !(event.getEntityLiving() instanceof EntityWildEnderman)) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                event.getEntityLiving().writeEntityToNBT(tagCompound);
                EntityWildEnderman wildZombie = new EntityWildEnderman(event.getEntityLiving().worldObj);
                wildZombie.readEntityFromNBT(tagCompound);
                event.getEntityLiving().worldObj.spawnEntityInWorld(wildZombie);
                event.getEntityLiving().setDead();
            }
            if (event.getEntityLiving() instanceof EntitySkeleton) {
                if (rand.nextInt(20) == 0) {
                    EntitySkeleton entityskeleton = new EntitySkeleton(world);
                    entityskeleton.setLocationAndAngles(event.getX(), event.getY(), event.getZ(), 0F, 0F);
                    entityskeleton.onInitialSpawn(world.getDifficultyForLocation(event.getEntityLiving().getPosition()), (IEntityLivingData) null);
                    world.spawnEntityInWorld(entityskeleton);
                    entityskeleton.startRiding(event.getEntityLiving());
                }
            } else if (!world.isDaytime() && rand.nextInt(30) == 0) {
                EntityWildHorse wildHorse = new EntityWildHorse(event.getWorld());
                wildHorse.setType(HorseType.SKELETON);
                wildHorse.setPosition(event.getX(), event.getY(), event.getZ());
                event.getWorld().spawnEntityInWorld(wildHorse);
                if (rand.nextBoolean())
                    wildHorse.startRiding(event.getEntityLiving());
                else
                    event.getEntityLiving().startRiding(wildHorse);
            }
        }

        if (event.getEntityLiving() instanceof EntityHorse) {
            EntityWildHorse wildHorse = new EntityWildHorse(event.getWorld());
            wildHorse.readEntityFromNBT(event.getEntityLiving().serializeNBT());
            event.getWorld().spawnEntityInWorld(wildHorse);
        }
    }

    @SubscribeEvent
    public void serverChat(ServerChatEvent event) {
        if (event.getMessage().contains("호출") || event.getMessage().contains("이리와")) {
            List<EntityAnimal> mobList = EntityAPI.getEntity(event.getPlayer().worldObj, event.getPlayer().getEntityBoundingBox().expandXyz(64), EntityAnimal.class);
            for (EntityAnimal horse : mobList) {
                if (horse.hasCustomName() && event.getMessage().contains(horse.getCustomNameTag())) {
                    EntityAPI.move(horse, event.getPlayer().posX, event.getPlayer().posY, event.getPlayer().posZ);
                    WorldAPI.addMessage(horse.getCustomNameTag() + "을 불렀습니다");
                    return;
                }
            }
            WorldAPI.addMessage("근처에 말이 없습니다");
        }
    }

    @SubscribeEvent
    public void rightClickEntity(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof EntityWildHorse || event.getTarget() instanceof EntityWolf) {
            if (WorldAPI.equalsItem(event.getItemStack(), Items.COMPASS)) {
                String name = CommandChat.getChatLine(0);
                if (name != null) {
                    event.getTarget().setCustomNameTag(name);
                }
            }
        }
    }

}
