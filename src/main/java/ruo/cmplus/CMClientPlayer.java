package ruo.cmplus;

import api.player.client.ClientPlayerAPI;
import api.player.client.ClientPlayerBase;

public class CMClientPlayer extends ClientPlayerBase{

	public CMClientPlayer(ClientPlayerAPI playerAPI) {
		super(playerAPI);
	}

	@Override
	public void moveEntityWithHeading(float paramFloat1, float paramFloat2) {
		super.moveEntityWithHeading(paramFloat1, paramFloat2);
	}
	
}
