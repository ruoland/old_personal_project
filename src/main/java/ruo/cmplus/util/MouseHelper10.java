package ruo.cmplus.util;

import net.minecraft.util.MouseHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

@SideOnly(Side.CLIENT)
public class MouseHelper10 extends MouseHelper
{
    public void ungrabMouseCursor()
    {
        Mouse.setGrabbed(false);
    }
}