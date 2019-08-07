package map.lopre2.dev;

import cmplus.deb.DebAPI;
import map.lopre2.CommandJB;
import map.lopre2.ItemCopy;
import map.lopre2.jump1.EntityLavaBlock;
import map.lopre2.jump3.EntityBoatBuildBlock;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class DevEvent {

    @SubscribeEvent
    public void event(MouseEvent event) {
        if (CommandJB.isDebMode) {
            if (event.getDwheel() == 120) {
                if (DebAPI.isKeyDown(Keyboard.KEY_LCONTROL)) {
                    EntityLavaBlock.ax -= 0.3;
                } else
                    EntityLavaBlock.ax -= 0.05;
            }
            if (event.getDwheel() == -120) {
                if (DebAPI.isKeyDown(Keyboard.KEY_LCONTROL)) {
                    EntityLavaBlock.ax += 0.3;
                } else
                    EntityLavaBlock.ax += 0.05;
            }
        }
    }


    @SubscribeEvent
    public void a(ServerChatEvent event) {
        String dis = event.getMessage();
        if (dis != null) {
            if (dis.startsWith("dis:")) {
                CommandJB.isDebMode = true;
                ItemCopy.setDistance(Integer.valueOf(dis.replace("dis:", "")));
            }
            if (dis.startsWith("yinte:")) {
                CommandJB.isDebMode = true;
                ItemCopy.setYinterval(Double.valueOf(dis.replace("yinte:", "")));
            }
            if (dis.startsWith("inte:")) {
                CommandJB.isDebMode = true;
                ItemCopy.setInterval(Double.valueOf(dis.replace("inte:", "")));
            }
            if (dis.startsWith("mox:")) {
                CommandJB.isDebMode = true;
                EntityBoatBuildBlock.moX = (Float.valueOf(dis.replace("mox:", "")));
            }
            if (dis.startsWith("moz:")) {
                CommandJB.isDebMode = true;
                EntityBoatBuildBlock.moZ = (Float.valueOf(dis.replace("moz:", "")));
            }
        }
    }
}
