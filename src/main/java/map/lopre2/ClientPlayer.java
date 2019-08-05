package map.lopre2;

import api.player.client.ClientPlayerAPI;
import api.player.client.ClientPlayerBase;
import api.player.server.ServerPlayerAPI;
import api.player.server.ServerPlayerBase;
import net.minecraft.util.DamageSource;

public class ClientPlayer extends ClientPlayerBase {

    public ClientPlayer(ClientPlayerAPI playerAPI) {
        super(playerAPI);
    }

    @Override
    public boolean attackEntityFrom(DamageSource paramDamageSource, float paramFloat) {
        if(paramDamageSource == DamageSource.inFire)
            return false;
        return super.attackEntityFrom(paramDamageSource, paramFloat);
    }
}
