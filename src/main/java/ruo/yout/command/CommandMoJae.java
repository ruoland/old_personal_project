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
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ruo.cmplus.cm.v18.function.CommandFor;
import ruo.cmplus.util.CommandPlusBase;
import ruo.yout.Mojae;

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
            Mojae.monterAttack.remove(args[1]);
            Mojae.monterAttack.remove(args[2]);
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
            Mojae.arrow_reeper = parseBoolean(args[1]);
        }
        if(args[0].equalsIgnoreCase("ARROWCount")){
            Mojae.arrow_count = parseInt(args[1]);
        }
        if(args[0].equalsIgnoreCase("ARROWRIDING")){
            Mojae.arrow_riding = parseBoolean(args[1]);
        }
        if(args[0].equalsIgnoreCase("skeldelay")){
            Mojae.skelDelay = parseInt(args[1]);
        }
    }

    public void updateAI(World world){
        for(Entity entity : world.loadedEntityList) {
            if(entity instanceof EntityLiving) {
                EntityLiving living = (EntityLiving) entity;
                String monsterName = EntityList.getEntityString(living);
                if (Mojae.monterAttack.containsKey(monsterName)) {
                    String attackKey = Mojae.monterAttack.get(monsterName);
                    Class entityClass = EntityList.NAME_TO_CLASS.get(attackKey);
                    if (living instanceof EntityMob) {
                        EntityMob mob = (EntityMob) living;
                        mob.targetTasks.addTask(1, new EntityAIHurtByTarget(mob, true, new Class[]{EntityPigZombie.class}));
                        mob.targetTasks.addTask(3, new EntityAINearestAttackableTarget(mob, entityClass, false));
                    }
                }
            }
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if(args.length == 1)
        return getListOfStringsMatchingLastWord(args, "dog", "skelreeper", "arrowreper", "arrowcount", "arrowriding", "attack", "block", "unlock", "skeldelay" ,
                "");
        else if(args[0].equalsIgnoreCase("attack")){
            return getListOfStringsMatchingLastWord(args, EntityList.getEntityNameList());
        } else if(args[0].equalsIgnoreCase("block")){
            return getListOfStringsMatchingLastWord(args,  Item.REGISTRY.getKeys());
        }
        return super.getTabCompletionOptions(server, sender, args, pos);
    }
}
