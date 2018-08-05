package ruo.cmplus.cm.beta.model;

import api.player.client.ClientPlayerAPI;
import api.player.client.ClientPlayerBase;
import ruo.cmplus.CMManager;

public class CMClientPlayer extends ClientPlayerBase{

	public CMClientPlayer(ClientPlayerAPI playerAPI) {
		super(playerAPI);
	}

	@Override
	public void moveEntityWithHeading(float paramFloat1, float paramFloat2) {
		if(CMManager.isMoveLock())
			return;
		
		super.moveEntityWithHeading(paramFloat1, paramFloat2);
	}
}
