package ruo.minigame;

import api.player.server.ServerPlayerAPI;
import api.player.server.ServerPlayerBase;
import net.minecraft.init.Items;
import net.minecraft.util.DamageSource;
import ruo.minigame.action.ActionEffect;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.fakeplayer.FakePlayerHelper;
import ruo.minigame.minigame.minerun.MineRun;

public class MiniGameServerPlayer extends ServerPlayerBase
{

	public MiniGameServerPlayer(ServerPlayerAPI playerAPI) {
		super(playerAPI);
	}
	public boolean isInWater() {
		if(ActionEffect.isInWater())
			return ActionEffect.getInWater();
		return super.isInWater();
	};

	@Override
	public void jump() {
		super.jump();
		if(FakePlayerHelper.isMove())
			FakePlayerHelper.fakePlayer.jump();
	}


	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
	}

	@Override
	public boolean attackEntityFrom(DamageSource paramDamageSource, float paramFloat) {
		return super.attackEntityFrom(paramDamageSource, paramFloat);
	}
}
