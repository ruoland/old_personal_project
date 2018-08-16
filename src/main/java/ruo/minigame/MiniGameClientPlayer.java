package ruo.minigame;

import api.player.client.ClientPlayerAPI;
import api.player.client.ClientPlayerBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import ruo.minigame.action.ActionEffect;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.fakeplayer.FakePlayerHelper;
import ruo.minigame.minigame.minerun.MineRun;

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
		EntityFakePlayer fakePlayer =FakePlayerHelper.fakePlayer;
		if (fakePlayer != null) {
			fakePlayer.rotationPitch = player.rotationPitch;
			fakePlayer.rotationYaw = player.rotationYaw;
			fakePlayer.rotationYawHead = player.rotationYawHead;
			fakePlayer.attackedAtYaw = player.attackedAtYaw;
			fakePlayer.renderYawOffset = player.renderYawOffset;
			fakePlayer.cameraPitch = player.cameraPitch;
			if (MineRun.elytraMode() == 0 || MineRun.elytraMode() == 2) {//플레이어를 페이크 위에 갖다 놓음 - 7월 14일
				if (MineRun.elytraMode() == 2) {
					fakePlayer.motionY = 0;//모드2 는 앞을 향해 날라가기 때문에 공중에 띄워줘야 함 - 7월 14일
					//엘리트라 모드 2에서 위 아래로 움직이면 카메라도 같이 움직여서 고쳐야하는데 아직 안고침 - 7월 14일
					//e.player.motionY = (fakePlayer.posY + 3 - MiniGame.minerun.curY) - e.player.posY; - 이거 안됨 움직일 때 어긋남 - 7월 14일
				}
				if (fakePlayer.isNotColliding() && !fakePlayer.isCollidedHorizontally) {//페이크 플레이어가 어딘가에 막힌 상태가 아닌 경우에만 - 7월 14일
					player.motionX = MineRun.xCoord();//앞으로 나아가게 함 - 7월 14일
					player.motionY = (fakePlayer.posY + 3) - player.posY;
					player.motionZ = MineRun.zCoord();
					MineRun.setFakePositionUpdate();
					fakePlayer.motionX = MineRun.xCoord();//걷는 모션을 주기 위해 있음 - 7월 14일
					fakePlayer.motionZ = MineRun.zCoord();
				}
			}
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
