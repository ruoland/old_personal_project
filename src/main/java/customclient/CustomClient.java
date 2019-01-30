package customclient;


import com.google.common.collect.Lists;
import customclient.beta.CommandDrawTexture;
import customclient.beta.CommandDrawYoutube;
import customclient.beta.CommandGui;
import customclient.beta.CommandUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.util.List;

//@Mod(modid = CustomClient.modid, name = "CustomClientR", version = CustomClient.version)
public class CustomClient {
    public static final String modid = "CustomClientR";
    public static final String version = "2.2";

    @Mod.EventHandler
    public void asdf(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandGui());
        event.registerServerCommand(new CommandDrawTexture());
        event.registerServerCommand(new CommandDrawYoutube());
        event.registerServerCommand(new CommandUI());
    }

    @Mod.EventHandler
    public void asdf(FMLInitializationEvent event) {

    }

    @Mod.EventHandler
    public void asdf(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new CustomClientEvent());
        ResourceHelper.init();
        Minecraft mc = Minecraft.getMinecraft();

        mc.gameSettings.resourcePacks.add("CustomClient");
        mc.gameSettings.saveOptions();
        List<ResourcePackRepository.Entry> list = Lists.<ResourcePackRepository.Entry>newArrayList();
        ResourcePackRepository resourcepackrepository = mc.getResourcePackRepository();
        resourcepackrepository.updateRepositoryEntriesAll();
        for (ResourcePackRepository.Entry resourcepackrepository$entry1 : Lists.reverse(resourcepackrepository.getRepositoryEntries())){
            System.out.println(resourcepackrepository$entry1.getResourcePackName());
            if(resourcepackrepository$entry1.getResourcePackName().equalsIgnoreCase("CustomClient")){
                list.add(resourcepackrepository$entry1);
            }
        }
        mc.getResourcePackRepository().setRepositories(list);

    }
}
	