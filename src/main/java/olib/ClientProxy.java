package olib;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class ClientProxy extends CommonProxy {
    public static KeyBinding grab = new KeyBinding("액션-", Keyboard.KEY_R, "카카카테고리");

    @Override
    public void preInit() {
        ClientRegistry.registerKeyBinding(grab);


    }

    @Override
    public void init() {
    }
}
