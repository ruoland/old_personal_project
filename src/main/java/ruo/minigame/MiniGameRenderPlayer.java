package ruo.minigame;

import api.player.client.ClientPlayerAPI;
import api.player.client.ClientPlayerBase;
import api.player.render.RenderPlayerAPI;
import api.player.render.RenderPlayerBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import ruo.minigame.action.ActionEffect;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.fakeplayer.FakePlayerHelper;
import ruo.minigame.minigame.minerun.MineRun;

public class MiniGameRenderPlayer extends RenderPlayerBase {

	public MiniGameRenderPlayer(RenderPlayerAPI playerAPI) {
		super(playerAPI);
		
	}
}
