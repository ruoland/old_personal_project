package map.puzzle;

import cmplus.deb.DebAPI;
import minigameLib.api.RenderAPI;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid ="PuzzleMap", name = "Puzzle Map")
public class Puzzle {

    @Mod.EventHandler
    public void init(FMLInitializationEvent e){
        DebAPI.registerEntity(this, "PuzzleBlock", EntityPuzzleBlock.class);
        RenderAPI.registerRender(EntityPuzzleBlock.class);
    }

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent e){
        MinecraftForge.EVENT_BUS.register(new PuEvent());
    }
}
