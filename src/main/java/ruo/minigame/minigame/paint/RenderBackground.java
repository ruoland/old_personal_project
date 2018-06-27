package ruo.minigame.minigame.paint;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@SideOnly(Side.CLIENT)
public class RenderBackground extends RenderLiving<EntityBackground>
{
    ResourceLocation customCard = null;

    public RenderBackground(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelBackground(), 0.5F);
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityBackground entity) {
		if(entity.texture != null || !entity.texture.equals("") || !entity.texture.equals(customCard.toString())){
			customCard = new ResourceLocation(entity.texture);
			InputStream f = null;
			try {
				f = Minecraft.getMinecraft().getResourceManager().getResource(customCard).getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			BufferedImage i = null;
			try {
				i = ImageIO.read(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.mainModel.textureWidth = 17;
			this.mainModel.textureHeight = 23;
		}

		return customCard;
	}
}