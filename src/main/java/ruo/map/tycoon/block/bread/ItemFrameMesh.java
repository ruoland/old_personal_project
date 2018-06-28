package ruo.map.tycoon.block.bread;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;

public class ItemFrameMesh implements ItemMeshDefinition {
    private static final ModelResourceLocation[] resource = new ModelResourceLocation[]{
            new ModelResourceLocation("breadtycoon:frame", "inventory"),
            new ModelResourceLocation("breadtycoon:frame1", "inventory"),
            new ModelResourceLocation("breadtycoon:frame2", "inventory"),
            new ModelResourceLocation("breadtycoon:frame3", "inventory"),
            new ModelResourceLocation("breadtycoon:frame4", "inventory"),
            new ModelResourceLocation("breadtycoon:frame5", "inventory")
    };

    @Override
    public ModelResourceLocation getModelLocation(ItemStack stack) {
        int custom = stack.getTagCompound().getInteger("custom");
        return resource[custom];
    }
}
