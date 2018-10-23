package ruo.map.tycoon;


import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraftforge.client.model.ModelLoader;
import ruo.cmplus.deb.DebAPI;
import ruo.map.platformer.EntityCoin;
import ruo.map.platformer.RenderCoin;
import ruo.map.platformer.chapter1.EntityBossHorse;
import ruo.map.tycoon.block.TileBreadCall;
import ruo.map.tycoon.block.TileBreadCallRenderer;
import ruo.map.tycoon.block.TileBreadDisplay;
import ruo.map.tycoon.block.TileBreadDisplayRenderer;
import ruo.map.tycoon.block.bread.ItemFrameMesh;
import ruo.map.tycoon.block.bread.TileBreadWorkbench;
import ruo.map.tycoon.block.bread.TileBreadWorkbenchRenderer;
import ruo.map.tycoon.block.shopping.TileShopping;
import ruo.map.tycoon.block.shopping.TileShoppingRenderer;
import ruo.minigame.api.RenderAPI;

public class ClientProxy extends CommonProxy {
    @Override
    public void init() {
        RenderAPI.registerTileEntity(TyconMain.shopdisplay, TileShopping.class, new TileShoppingRenderer());
        RenderAPI.registerTileEntity(TyconMain.breaddisplay, TileBreadDisplay.class, new TileBreadDisplayRenderer());
        RenderAPI.registerTileEntity(TyconMain.breadcall, TileBreadCall.class, new TileBreadCallRenderer());
        RenderAPI.registerTileEntity(TyconMain.breadWorkbench, TileBreadWorkbench.class, new TileBreadWorkbenchRenderer());
        ModelBakery.registerItemVariants(TyconMain.breadFrame,
                new ModelResourceLocation("breadtycoon:frame", "inventory")
                ,new ModelResourceLocation("breadtycoon:frame1", "inventory")
                ,new ModelResourceLocation("breadtycoon:frame2", "inventory")
                ,new ModelResourceLocation("breadtycoon:frame3", "inventory"),
                new ModelResourceLocation("breadtycoon:frame4", "inventory"),
                new ModelResourceLocation("breadtycoon:frame5", "inventory")
        );
        ModelLoader.setCustomMeshDefinition(TyconMain.flour, new ItemFrameMesh());

    }
}
