package ruo.yout.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandFindEntity extends CommandBaseEntity {
    private int entityCount = 0;

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        entityCount = 0;
        super.execute(server, sender, args);
        StringBuffer buffer = new StringBuffer("현재 월드에 ");
        buffer.append(args[0]).append("는(은) ").append(entityCount).append("마리가 있습니다.");
        sender.addChatMessage(new TextComponentString(buffer.toString()));
    }

    @Override
    public void runCommand(Entity livingBase, String[] args) throws CommandException {
        entityCount++;
    }
}
