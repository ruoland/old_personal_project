package cmplus.test;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CMPacketCommand implements IMessage {
	public String name;
	public CMPacketCommand() {
	}
	
	public CMPacketCommand(String name) {
		this.name = name;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		name = ByteBufUtils.readUTF8String(buf);
		System.out.println("읽음"+name);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, name);
		System.out.println("보냄"+name);

	}

}
