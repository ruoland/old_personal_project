package ruo.yout;

import cmplus.CMManager;
import cmplus.cm.CommandUI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import olib.api.WorldAPI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingSkelDelay0 implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        JCheckBox jCheckBox = (JCheckBox) e.getSource();
        if (e.getSource() == SwingEntity.skelDelay0Box) {
            Mojae.skelDelay = jCheckBox.isSelected() ? 0 : -1;
        }
        if (e.getSource() == SwingEntity.uiOffBox) {
            boolean ui = !jCheckBox.isSelected();
            CMManager.setUI(RenderGameOverlayEvent.ElementType.HEALTH, ui);
            CMManager.setUI(RenderGameOverlayEvent.ElementType.FOOD, ui);
            CMManager.setUI(RenderGameOverlayEvent.ElementType.CROSSHAIRS, ui);
            CMManager.setUI(RenderGameOverlayEvent.ElementType.HOTBAR, ui);
            CMManager.setHand(ui);
        }
        if(e.getSource() == SwingEntity.healthBox){
            if(jCheckBox.isSelected()) {
                for (Entity entity : WorldAPI.getWorld().loadedEntityList) {
                    if (entity instanceof EntityLivingBase) {
                        entity.setAlwaysRenderNameTag(true);
                        entity.setCustomNameTag("체력:" + ((EntityLivingBase) entity).getMaxHealth() + " / " + ((EntityLivingBase) entity).getHealth());
                    }
                }
            }
            else{
                for (Entity entity : WorldAPI.getWorld().loadedEntityList) {
                    if (entity instanceof EntityLivingBase) {
                        if(entity.getCustomNameTag().startsWith("체력:")) {
                            entity.setCustomNameTag("");
                            entity.setAlwaysRenderNameTag(false);
                        }
                    }
                }
            }
        }
    }
}
