package ruo.yout.command;

import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandGive;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ruo.cmplus.cm.v18.function.CommandFor;
import ruo.cmplus.util.CommandPlusBase;
import ruo.minigame.api.WorldAPI;
import ruo.yout.Mojae;

import java.util.Iterator;
import java.util.List;

public class CommandMoJae extends CommandPlusBase {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args[0].equalsIgnoreCase("dog")){
            Mojae.dog_pan = Boolean.valueOf(args[1]);
        }
        if(args[0].equalsIgnoreCase("block")){
            Block block = Block.getBlockFromName(args[1]);
            String key = args[2];
            float value = (float) parseDouble(args[3]);
            if(key.equalsIgnoreCase("light")){
                block.setLightLevel(value);
            }
            if(key.equalsIgnoreCase("explosion")){
                block.setResistance(value);
            }
            if(key.equalsIgnoreCase("hard")){
                block.setHardness(value);
            }
        }
        if(args[0].equalsIgnoreCase("attack")){
            Mojae.monterAttack.put(args[2], args[1]);
            Mojae.monterAttack.put(args[1], args[2]);
            updateAI(sender.getEntityWorld());
        }
        if(args[0].equalsIgnoreCase("remove")){
            Mojae.monterAttackRemove.put(Mojae.monterAttack.get(args[2]),Mojae.monterAttack.get(args[1]));
            Mojae.monterAttackRemove.put(Mojae.monterAttack.get(args[1]),Mojae.monterAttack.get(args[2]));
            updateAI(sender.getEntityWorld());
        }
        if(args[0].equalsIgnoreCase("unlock")){
            for(Entity entity : sender.getEntityWorld().loadedEntityList){
                if(entity instanceof EntityLivingBase){
                    EntityLivingBase livingBase = (EntityLivingBase) entity;
                    System.out.println(livingBase+" - "+livingBase.isPotionActive(Mojae.lockPotion));
                    if(livingBase.isPotionActive(Mojae.lockPotion) || livingBase.getCustomNameTag().startsWith("잠금")){
                        livingBase.setCustomNameTag("잠금해제");
                    }
                }
            }
        }
        if(args[0].equalsIgnoreCase("SKELREEPER")){
            Mojae.skelreeper = parseBoolean(args[1]);
        }
        if(args[0].equalsIgnoreCase("ARROWReper")){
            Mojae.arrowReeper = parseBoolean(args[1]);
        }
        if(args[0].equalsIgnoreCase("ARROWCount")){
            Mojae.arrow_count = parseInt(args[1]);
        }
        if(args[0].equalsIgnoreCase("ARROWRIDING")){
            Mojae.arrowRiding = parseBoolean(args[1]);
        }
        if(args[0].equalsIgnoreCase("skeldelay")){
            Mojae.skelDelay = parseInt(args[1]);
        }
        if(args[0].equalsIgnoreCase("wither")){
            Mojae.wither = parseBoolean(args[1]);//스켈레톤이 위더를 잡기 위해서 있음. 모든 화살은 플레이어가 쏜 게 됨. 이 상태에서는 스켈레톤이 화살에 무조건 맞지 않게 됨
        }
    }

    public void updateAI(World world){
        for(Entity entity : world.loadedEntityList) {
            if(entity instanceof EntityLiving) {
                EntityLiving living = (EntityLiving) entity;
                String monsterName = EntityList.getEntityString(living);
                String attackTarget = Mojae.monterAttack.get(monsterName);
                Class targetClass = EntityList.NAME_TO_CLASS.get(attackTarget);
                if (Mojae.monterAttackRemove.containsKey(monsterName)) {
                    if (living instanceof EntityMob) {
                        EntityMob mob = (EntityMob) living;
                        Iterator iterator = mob.targetTasks.taskEntries.iterator();
                        while (iterator.hasNext()){
                            EntityAITasks.EntityAITaskEntry taskEntry = (EntityAITasks.EntityAITaskEntry) iterator.next();
                            if(taskEntry.action instanceof EntityAINearestAttackableTarget){
                                System.out.println("삭제함");
                                iterator.remove();
                                Mojae.monterAttack.remove(monsterName);
                                mob.setAttackTarget(null);
                            }
                        }
                    }
                }
                if (Mojae.monterAttack.containsKey(monsterName)) {
                    if (living instanceof EntityMob) {
                        EntityMob mob = (EntityMob) living;
                        mob.targetTasks.addTask(1, new EntityAIHurtByTarget(mob, true, new Class[]{EntityPigZombie.class}));
                        mob.targetTasks.addTask(3, new EntityAINearestAttackableTarget(mob, targetClass, false));
                    }
                }
            }
        }
        Mojae.monterAttackRemove.clear();;
    }


    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if(args.length == 1)
        return getListOfStringsMatchingLastWord(args, "dog", "skelreeper", "arrowreper", "arrowcount", "arrowriding", "attack", "block", "unlock", "skeldelay" ,
                "remove");
        else if(args[0].equalsIgnoreCase("attack") || args[0].equalsIgnoreCase("remove")){
            return getListOfStringsMatchingLastWord(args, EntityList.getEntityNameList());
        } else if(args[0].equalsIgnoreCase("block")){
            return getListOfStringsMatchingLastWord(args,  Item.REGISTRY.getKeys());
        }
        return super.getTabCompletionOptions(server, sender, args, pos);
    }
}
