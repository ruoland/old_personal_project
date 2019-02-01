package cmplus;

import api.player.server.ServerPlayerAPI;
import api.player.server.ServerPlayerBase;

public class CMServerPlayer extends ServerPlayerBase {

	public CMServerPlayer(ServerPlayerAPI playerAPI) {
		super(playerAPI);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
	}
}
