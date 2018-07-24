package ruo.asdfrpg;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ruo.asdfrpg.skill.*;
import ruo.cmplus.deb.DebAPI;

@Mod(modid = "asdfrpg")
public class AsdfRPG {
    public static final PotionFly flyPotion = new PotionFly(false, 0);
    public static final Item respawn = new ItemRespawn().setCreativeTab(CreativeTabs.COMBAT).setUnlocalizedName("respawn").setRegistryName("asdfrpg:respawn");

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        GameRegistry.register(respawn);
        DebAPI.registerEvent(new AsdfEvent());
        SkillHelper.registerSkill(new SkillBlockGrab());
        SkillHelper.registerSkill(new SkillDoubleJump());
        SkillHelper.registerSkill(new SkillFly());
    }
    @Mod.EventHandler
    public void init(FMLServerStartingEvent e) {
        e.registerServerCommand(new CommandSkill());
    }
    @Mod.EventHandler
    public void init(FMLPreInitializationEvent e) {

    }

    @Mod.EventHandler
    public void init(FMLPostInitializationEvent e) {

    }

}

