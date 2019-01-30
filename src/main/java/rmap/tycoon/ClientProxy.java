package rmap.tycoon;


import minigameLib.api.RenderAPI;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import rmap.tycoon.block.TileBreadCall;
import rmap.tycoon.block.TileBreadCallRenderer;
import rmap.tycoon.block.TileBreadDisplay;
import rmap.tycoon.block.TileBreadDisplayRenderer;
import rmap.tycoon.block.bread.ItemFrameMesh;
import rmap.tycoon.block.bread.TileBreadWorkbench;
import rmap.tycoon.block.bread.TileBreadWorkbenchRenderer;
import rmap.tycoon.block.shopping.TileShopping;
import rmap.tycoon.block.shopping.TileShoppingRenderer;

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
