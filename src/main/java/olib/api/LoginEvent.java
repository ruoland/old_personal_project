package olib.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

public class LoginEvent extends Event{
	public EntityPlayer player;
	public World world;
	public LoginEvent(EntityPlayer player, World world) {
		this.player = player;
		this.world = world;
		
	}
	
}
