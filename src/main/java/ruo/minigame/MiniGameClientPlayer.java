package ruo.minigame;

import api.player.client.ClientPlayerAPI;
import api.player.client.ClientPlayerBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHand;
import ruo.minigame.action.ActionEffect;
import ruo.minigame.fakeplayer.FakePlayerHelper;

public class MiniGameClientPlayer extends ClientPlayerBase {

	public MiniGameClientPlayer(ClientPlayerAPI playerAPI) {
		super(playerAPI);
		
	}

	public boolean isInWater() {
		if(ActionEffect.isInWater())
			return ActionEffect.getInWater();
		return super.isInWater();
	};
	
	public void onUpdate() {
		if (FakePlayerHelper.fakePlayer != null) {
			FakePlayerHelper.fakePlayer.rotationPitch = player.rotationPitch;
			FakePlayerHelper.fakePlayer.rotationYaw = player.rotationYaw;
			FakePlayerHelper.fakePlayer.rotationYawHead = player.rotationYawHead;
			FakePlayerHelper.fakePlayer.attackedAtYaw = player.attackedAtYaw;
			FakePlayerHelper.fakePlayer.renderYawOffset = player.renderYawOffset;
			FakePlayerHelper.fakePlayer.cameraPitch = player.cameraPitch;
		}
		super.onUpdate();
	}

	@Override
	public void knockBack(Entity paramEntity, float paramFloat, double paramDouble1, double paramDouble2) {
		super.knockBack(paramEntity, paramFloat, paramDouble1, paramDouble2);
		if (FakePlayerHelper.fakePlayer != null && Minecraft.getMinecraft().currentScreen == null)
			FakePlayerHelper.fakePlayer.knockBack(paramEntity, paramFloat, paramDouble1, paramDouble2);
	}

	@Override
	public void setSprinting(boolean paramBoolean) {
		super.setSprinting(paramBoolean);
		if(FakePlayerHelper.isMove())
		FakePlayerHelper.fakePlayer.setSprinting(paramBoolean);
	}

	@Override
	public void swingItem(EnumHand paramEnumHand) {
		super.swingItem(paramEnumHand);
		if(FakePlayerHelper.isMove())
		FakePlayerHelper.fakePlayer.swingArm(paramEnumHand);
	}

	@Override
	public void jump() {
		super.jump();
		if(FakePlayerHelper.isMove())
			FakePlayerHelper.fakePlayer.jump();
	}
	@Override
	public void moveEntityWithHeading(float paramFloat1, float paramFloat2) {

		if (FakePlayerHelper.isMove()) {
			FakePlayerHelper.fakePlayer.moveEntityWithHeading(paramFloat1 / 7, paramFloat2 / 7);
			super.moveEntityWithHeading(paramFloat1, paramFloat2);
		} else
			super.moveEntityWithHeading(paramFloat1, paramFloat2);
	}
	//if(paramFloat1 != 0 || paramFloat2 != 0)//float1 왼쪽 0.98 오른쪽 -0.98,  float2 앞 0.98
	//System.out.println(paramFloat1+" - "+paramFloat2);
}
