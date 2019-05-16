package minigameLib;

import api.player.server.ServerPlayerAPI;
import api.player.server.ServerPlayerBase;
import olib.fakeplayer.FakePlayerHelper;
import net.minecraft.util.DamageSource;

public class MiniGameServerPlayer extends ServerPlayerBase
{

	public MiniGameServerPlayer(ServerPlayerAPI playerAPI) {
		super(playerAPI);
	}

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
