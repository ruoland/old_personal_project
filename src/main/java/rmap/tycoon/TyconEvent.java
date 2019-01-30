package rmap.tycoon;

import cmplus.WorldConfig;
import com.google.common.io.Files;
import minigameLib.api.LoginEvent;
import minigameLib.api.WorldAPI;
import minigameLib.effect.AbstractTick;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockRedstoneLight;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import rmap.tycoon.consumer.EntityConsumer;
import rmap.tycoon.map.MapHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.Charset;

public class TyconEvent {
    private int npcCreateDelay = 0;
    @SubscribeEvent
    public void spawnEgg(EntityJoinWorldEvent event) {
        if(event.getEntity() instanceof EntityEgg){
            EntityTycoonEgg tycoonEgg = new EntityTycoonEgg(event.getWorld());
            NBTTagCompound tagCompound = new NBTTagCompound();
            ((EntityEgg) event.getEntity()).writeEntityToNBT(tagCompound);
            tycoonEgg.readEntityFromNBT(tagCompound);
            event.getWorld().spawnEntityInWorld(tycoonEgg);
        }
    }
    @SubscribeEvent
    public void consumerGenerator(TickEvent.PlayerTickEvent event) {
        if (WorldAPI.equalsWorldName("타이쿤") && MapHelper.isTyconOpen()) {
            if (npcCreateDelay > 0)
                npcCreateDelay--;
            if (event.side == Side.SERVER && event.phase == TickEvent.Phase.END && npcCreateDelay == 0) {
                if (event.player.worldObj.rand.nextInt(MapHelper.npcPercent()) == 0) {
                    EntityConsumer consumer = new EntityConsumer(event.player.worldObj);
                    consumer.setPosition(-467.6, 4.0, -807.9);
                    consumer.worldObj.spawnEntityInWorld(consumer);
                    consumer.onInitialSpawn(null, null);
                    npcCreateDelay = 20;
                }
            }
        }
    }

    @SubscribeEvent
    public void playerLogin(LoginEvent event) {
        if (WorldAPI.equalsWorldName("타이쿤")) {
            WorldConfig worldConfig = WorldConfig.getWorldConfig();
            TyconHelper.playermoney = Float.valueOf(worldConfig.getProperty("돈").getString());
            MapHelper.setFlowerBonus(Integer.valueOf(worldConfig.getProperty("플라워보너스").getInt()));
            MapHelper.setTyconOpen(Boolean.valueOf(worldConfig.getProperty("열림").getBoolean()));
        }
    }

    @SubscribeEvent
    public void playerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (WorldAPI.equalsWorldName("타이쿤")) {
            WorldConfig worldConfig = WorldConfig.getWorldConfig();

            worldConfig.setProperty("돈", "" + TyconHelper.playermoney);
            worldConfig.setProperty("플라워보너스", "" + MapHelper.getFlowerBonus());
            worldConfig.setProperty("열림", "" + MapHelper.isTyconOpen());
        }
    }

    @SubscribeEvent
    public void placeFlower(BlockEvent.PlaceEvent event) {
        if (WorldAPI.equalsWorldName("타이쿤")) {
            if (event.getPlacedBlock().getBlock() instanceof BlockFlower) {
                MapHelper.addFlowerBonus(50);
            }
        }
    }

    @SubscribeEvent
    public void placeFlower(BlockEvent.BreakEvent event) {
        if (WorldAPI.equalsWorldName("타이쿤")) {
            if (event.getState().getBlock() instanceof BlockFlower) {
                MapHelper.subFlowerBonus(50);//이거 꽃 아래 블럭을 부수면 발동 안할 것 같음
            }
        }
    }

    @SubscribeEvent
    public void thunderEvent(EntityJoinWorldEvent event2) {
        if (event2.getEntity() instanceof EntityLightningBolt) {
            if (event2.getWorld().rand.nextInt(100) == 0) {
                WorldAPI.blockTick(event2.getWorld(), 0, 0, 0, 0, 0, 0, new AbstractTick.BlockXYZ() {
                    @Override
                    public void run(TickEvent.Type type) {
                        Block block = event2.getWorld().getBlockState(getPos()).getBlock();
                        if (block instanceof BlockRedstoneLight) {
                            BlockRedstoneLight redstoneLight = (BlockRedstoneLight) block;
                            event2.getWorld().setBlockState(getPos(), Blocks.REDSTONE_LAMP.getDefaultState(), 2);
                        }
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public void dayEndEvent(PlayerSleepInBedEvent event) {
        if (WorldAPI.equalsWorldName("타이쿤")) {
            if (event.getEntityPlayer().worldObj.getWorldTime() >= 13000L && TyconHelper.waitConsumerSize() == 0) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiDayEnd());
            } else {
                WorldAPI.addMessage("아직 손님이 있습니다.");
            }
            event.setResult(EntityPlayer.SleepResult.NOT_SAFE);
        }


    }

    @SubscribeEvent
    public void dayEndEvent(TickEvent.WorldTickEvent event) {
        if (WorldAPI.equalsWorldName("타이쿤")) {
            if (event.world.getWorldTime() >= 13000) {
                event.world.setWorldTime(13000L);
                if (MapHelper.isTyconOpen()) {
                    MapHelper.setTyconOpen(false);
                    WorldAPI.addMessage("밤이 되어 문이 닫혔습니다. 잠을 자기 전까지는 시간이 흐르지 않습니다.");
                }
            }
        }
    }

    @SubscribeEvent
    public void saveData(WorldEvent.Save event) {
        if (WorldAPI.equalsWorldName("타이쿤")) {
            if (!event.getWorld().isRemote) {
                File file = new File(WorldAPI.getCurrentWorldFile(), "tycoon.dat");
                if (file.isFile())
                    try {
                        BufferedWriter writer = Files.newWriter(file, Charset.forName("UTF-8"));
                        writer.write("돈:" + TyconHelper.playermoney);
                        writer.newLine();
                        writer.write("꽃:" + MapHelper.getFlowerBonus());

                        writer.flush();
                        writer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    @SubscribeEvent
    public void loadData(LoginEvent event) {
        if (WorldAPI.equalsWorldName("타이쿤")) {
            if (!event.world.isRemote) {
                File file = new File(WorldAPI.getCurrentWorldFile(), "tycoon.dat");
                if (file.isFile())
                    try {
                        BufferedReader writer = Files.newReader(file, Charset.forName("UTF-8"));
                        TyconHelper.playermoney = Integer.valueOf(writer.readLine().replace("돈:", ""));
                        MapHelper.setFlowerBonus(Integer.valueOf(writer.readLine().replace("꽃:", "")));
                        writer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    @SubscribeEvent
    public void rightclickSlab(RightClickBlock event) {
        if (WorldAPI.equalsWorldName("타이쿤")) {
            Block slab = event.getWorld().getBlockState(event.getPos()).getBlock();
            if (event.getItemStack() != null && slab == Blocks.STONE_SLAB) {
                event.getItemStack().stackSize--;
                EntityItem item = new EntityItem(event.getWorld());
                item.setPosition(event.getPos().getX(), event.getPos().getY() + 1, event.getPos().getZ());
                item.setInfinitePickupDelay();
                event.getWorld().spawnEntityInWorld(item);
            }
        }
    }

    @SubscribeEvent
    public void renderUI(RenderGameOverlayEvent.Post event) {
        if (WorldAPI.equalsWorldName("타이쿤")) {
            if (event.getType() == ElementType.ALL) {
                Minecraft.getMinecraft().fontRendererObj.drawString("현재 시간:" + WorldAPI.getWorldTime(WorldAPI.getWorld()), 0, 0,
                        0xFFFFFF);
            }
        }
    }

}
 