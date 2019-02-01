package map.tycoon;


import minigameLib.api.RenderAPI;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import map.tycoon.block.TileBreadCall;
import map.tycoon.block.TileBreadCallRenderer;
import map.tycoon.block.TileBreadDisplay;
import map.tycoon.block.TileBreadDisplayRenderer;
import map.tycoon.block.bread.ItemFrameMesh;
import map.tycoon.block.bread.TileBreadWorkbench;
import map.tycoon.block.bread.TileBreadWorkbenchRenderer;
import map.tycoon.block.shopping.TileShopping;
import map.tycoon.block.shopping.TileShoppingRenderer;

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
