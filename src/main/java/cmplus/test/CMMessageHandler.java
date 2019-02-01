package cmplus.test;

import cmplus.deb.DebAPI;
import minigameLib.action.ActionEffect;
import minigameLib.api.WorldAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CMMessageHandler implements IMessageHandler<CMPacketCommand, IMessage> {

    @Override
    public IMessage onMessage(CMPacketCommand message, MessageContext ctx) {
        String command = message.name;
        System.out.println("메세지 핸들러 " + command+ctx.side);
        if (message.name.contains("서버더블점프")) {
            boolean isOn = Boolean.valueOf(message.name.replace("서버더블점프", ""));
            ActionEffect.doubleJump(isOn);
            System.out.println(ActionEffect.canDoubleJump());
        } else if (message.name.contains("더블점프:")) {
            EntityPlayerMP player = WorldAPI.getPlayerByName(message.name.replace("더블점프:", ""));
            ActionEffect.canDoubleJump = false;
            ActionEffect.isPlayerJump = false;
            player.motionY = 0.5F;
            player.fallDistance = 0;
            DebAPI.msgText("MiniGame", "더블점프함");
            if (player.isSprinting()) {
                float f = player.rotationYaw * 0.017453292F;
                player.motionX -= (double) (MathHelper.sin(f) * 0.2F);
                player.motionZ += (double) (MathHelper.cos(f) * 0.2F);
            }
        }else {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                ClientCommandHandler.instance.executeCommand(Minecraft.getMinecraft().thePlayer,
                        command);
            });
        }
        return null;
    }

}
