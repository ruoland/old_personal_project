package ruo.minigame;

import api.player.render.RenderPlayerAPI;
import api.player.render.RenderPlayerBase;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import ruo.minigame.api.Direction;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.minigame.minerun.MineRun;

public class MiniGameRenderPlayer extends RenderPlayerBase {

	public MiniGameRenderPlayer(RenderPlayerAPI playerAPI) {
		super(playerAPI);
	}

}

