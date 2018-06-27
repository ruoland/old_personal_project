package ruo.cmplus.deb;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.Move;

public class DebEvent {
	//@SubscribeEvent
	public void event(LivingHurtEvent event) {
		if (event.getEntityLiving() instanceof EntityLiving && WorldAPI.getPlayer() != null
				&& WorldAPI.getPlayer().isSneaking()) {
			EntityPlayer player = WorldAPI.getPlayer();
			if (player != null) {
				System.out.println("산타 이벤트 공격받음");
				if (WorldAPI.equalsHeldItem(Items.APPLE)) {
					System.out.println("플레이어를 따라감");
					EntityAPI.move(new Move((EntityLiving) event.getEntityLiving(), player.posX, player.posY, player.posZ, false) {
						@Override
						public void complete() {
							if (movecount == 0) {
								System.out.println("목적지에 도달ㄴ했습니다"+getDistance());
							}
						}
					}.setDistance(1));
				}
				if (WorldAPI.equalsHeldItem(Items.ARROW)) {
					System.out.println("플레이어의 좌표로 이동함");
					EntityAPI.move(new Move((EntityLiving) event.getEntityLiving(), player.posX, player.posY, player.posZ, false) {
						@Override
						public void complete() {
							if (movecount == 0) {
								System.out.println("목적지에 도달ㄴ했습니다"+getDistance());
							}
						}
					}.setDistance(10));
				}
				if (WorldAPI.equalsHeldItem(Items.IRON_AXE)) {
					System.out.println("네비게이션을 이동해 플레이어 좌표로 이동함");
					EntityAPI.move(new Move((EntityLiving) event.getEntityLiving(), player.posX, player.posY, player.posZ, false) {
						@Override
						public void complete() {
							if (movecount == 0) {
								System.out.println("목적지에 도달ㄴ했습니다"+getDistance());
							}
						}
					}.setDistance(25));
				}
			}
		}
	}
	//@SubscribeEvent
	public void server(ServerChatEvent e) {
		if (DebAPI.debAPI.size() > 0 && e.getMessage().split(" ").length > 2) {
			try {
				String[] split = e.getMessage().split(" ");
				String name = e.getMessage().split(" ")[0];
				double[] xyz = WorldAPI.valueOfStr(split[1], split[2], split[3]);
				System.out.println(name + DebAPI.debAPI.containsKey(name) + xyz[0] + " - " + xyz[1] + " - " + xyz[2]);
				if (DebAPI.debAPI.containsKey(name)) {
					DebAPI debapi = DebAPI.debAPI.get(name);
					debapi.x = (float) xyz[0];
					debapi.y = (float) xyz[1];
					debapi.z = (float) xyz[2];
				}
			}catch (Exception e2){
				e2.printStackTrace();
			}
		}
	}
}
