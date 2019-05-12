package grab;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class GrabEvent {
    @SubscribeEvent
    public void grabEvent(PlayerInteractEvent.EntityInteract event) {
        Entity target = event.getTarget();
        NBTTagCompound tagCompound = event.getTarget().getEntityData();
        if (target instanceof EntityLivingBase) {
            tagCompound.setBoolean("playerGrab", true);
        }
    }

    @SubscribeEvent
    public void ungrabEvent(LivingAttackEvent event) {
        System.out.println(event.getEntityLiving() + " - " + event.getSource().getEntity());
        if (event.getEntityLiving() instanceof EntityCreeper) {
            if (event.getSource().getEntity() instanceof EntityPlayer) {
                if (event.getEntityLiving().getEntityData().getBoolean("playerGrab")) {
                    event.getEntityLiving().getEntityData().setBoolean("playerGrab", false);
                    Vec3d vec3d = event.getEntityLiving().getLookVec();
                    event.getEntityLiving().addVelocity(vec3d.xCoord * 2.3, vec3d.yCoord * 1.3, vec3d.zCoord * 2.3);
                }
            }
        }
        /*
EntityCreeper['크리퍼'/63, l='MpServer', x=427.72, y=4.00, z=-1024.48]EntityPlayerSP['Player527'/179, l='MpServer', x=427.68, y=4.00, z=-1022.88]
EntityCreeper['크리퍼'/63, l='test', x=427.72, y=4.00, z=-1024.48]EntityPlayerMP['Player527'/179, l='test', x=427.68, y=4.00, z=-1022.88]
EntityCreeper['크리퍼'/63, l='MpServer', x=427.72, y=4.00, z=-1024.48]null
         */
    }

    @SubscribeEvent
    public void position(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase living = event.getEntityLiving();
        if (living.getEntityData().getBoolean("playerGrab") && Minecraft.getMinecraft().thePlayer != null) {
            EntityPlayerSP playerSP = Minecraft.getMinecraft().thePlayer;
            Vec3d vec3d = playerSP.getLookVec();
            living.setPosition(playerSP.posX + vec3d.xCoord * 1.3, playerSP.posY + playerSP.getEyeHeight() + vec3d.yCoord * 1.3, playerSP.posZ + vec3d.zCoord * 1.3);
            System.out.println(playerSP.posX + vec3d.xCoord);
            System.out.println(living.getCustomNameTag());
            living.fallDistance = 0;
        }
    }
}
