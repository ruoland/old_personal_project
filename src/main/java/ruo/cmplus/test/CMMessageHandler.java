package ruo.cmplus.test;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CMMessageHandler implements IMessageHandler<CMPacketCommand, IMessage> {

    @Override
    public IMessage onMessage(CMPacketCommand message, MessageContext ctx) {
        String command = message.name;
        System.out.println("메세지 핸들러 " + command);
        Minecraft.getMinecraft().addScheduledTask(() -> {
            ClientCommandHandler.instance.executeCommand(Minecraft.getMinecraft().thePlayer,
                    command);
        });
        return null;
    }

}
