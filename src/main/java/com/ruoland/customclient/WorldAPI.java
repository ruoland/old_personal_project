package com.ruoland.customclient;

import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldSummary;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.util.List;
import java.util.Random;

public class WorldAPI {

    public static void worldLoad(String worldName) {
        Minecraft mc = Minecraft.getMinecraft();
        TickRegister.register(new AbstractTick(TickEvent.Type.CLIENT, 1, false) {
            @Override
            public void run(TickEvent.Type type) {

                if (mc.theWorld != null) {
                    mc.theWorld.sendQuittingDisconnectingPacket();
                    mc.loadWorld((WorldClient) null);
                }
                ISaveFormat isaveformat = mc.getSaveLoader();
                if (isaveformat.canLoadWorld(worldName)) {
                    try {
                        for (WorldSummary summary : isaveformat.getSaveList()) {
                            if (summary.getDisplayName().equalsIgnoreCase(worldName)) {
                                net.minecraftforge.fml.client.FMLClientHandler.instance().tryLoadExistingWorld(new GuiWorldSelection(new GuiMainMenu()), summary);
                                break;
                            }
                        }
                    } catch (AnvilConverterException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    /**
     * min 이상 round 이하의 양수 또는 음수의 난수를 반환합니다
     */
    public static int minRand(int min, int round) {
        int random = rand.nextInt(round);
        if (min <= random || min == round) {
            return rand.nextBoolean() ? -random : random;
        }
        return minRand(min, round);
    }

    /**
     * min 이상 round 이하의 난수를 반환합니다
     */
    public static int minRand2(int min, int round) {
        int random = rand.nextInt(round);
        if (min <= random) {
            return random;
        }
        return minRand2(min, round);
    }

    /**
     * 음수 또는 양수의 난수를 반환합니다
     */
    public static int rand(int round) {
        if(round <= 0)
            return 0;
        return rand.nextBoolean() ? -rand.nextInt(round) : rand.nextInt(round);
    }

    public static int round(double a) {
        return (int) Math.round(a);
    }

    public static int[] round(double[] a) {
        return new int[]{round(a[0]), round(a[1]), round(a[2])};
    }
    public static int[] round(Entity entity) {
        return new int[]{round(entity.posX), round(entity.posY), round(entity.posZ)};
    }

    /**
     * 엔티티의 좌표를 배열로 반환합니다
     */
    public static double[] changePosArray(EntityLivingBase base) {
        return new double[]{base.posX, base.posY, base.posZ};
    }

    public static int[] changePosArrayInt(EntityLivingBase base) {
        return round(changePosArray(base));
    }

    public static int[] roundPos(EntityLivingBase base) {
        return new int[]{round(base.posX), round(base.posY - base.getYOffset()), round(base.posZ)};
    }

    public static int[] roundPos(double x, double y, double z) {
        return new int[]{round(x), round(y), round(z)};
    }

    public static double[] valueOf(BlockPos pos) {
        return new double[]{pos.getX(), pos.getY(), pos.getZ()};
    }

    public static double[] valueOfStr(String x, String y, String z) {
        return new double[]{Double.valueOf(x), Double.valueOf(y), Double.valueOf(z)};
    }

    public static boolean checkPos(EntityLivingBase living, double X, double Y, double Z) {
        double xc = cut(X);
        double yc = cut(Y);
        double zc = cut(Z);
        double livingXc = cut(living.posX);
        double livingYc = cut(living.posY);
        double livingZc = cut(living.posZ);
        return (xc == livingXc && yc == livingYc && zc == livingZc)
                || (round(living.posX) == round(X) && round(living.posY) == round(Y) && round(living.posZ) == round(Z));
    }

    public static double cut(double dou) {
        return cut(dou, 1);
    }

    public static double cut(double dou, int length) {
        String dou2 = String.valueOf(dou);
        dou2 = dou2.substring(0, dou2.indexOf(".") + length + 1);
        return Double.parseDouble(dou2);
    }

    public static double[] parseDouble(Object... obj) {
        double[] d = new double[obj.length];
        for (int i = 0; i < obj.length; i++) {
            try {
                d[i] = Double.parseDouble((String) obj[i]);
            } catch (ClassCastException e) {
                e.printStackTrace();
                ;
            }
        }
        return d;
    }

    public static boolean checkPos(EntityLivingBase player, double x, double z, double x2, double z2) {
        int minX = Math.min(round(x), round(x2));
        int maxX = Math.max(round(x), round(x2));
        int minZ = Math.min(round(z), round(z2));
        int maxZ = Math.max(round(z), round(z2));
        return minX <= player.posX && maxX >= player.posX && minZ <= player.posZ && maxZ >= player.posZ;
    }

    public static boolean checkPos(EntityLivingBase player, BlockPos stPos, BlockPos endPos) {
        if (stPos == null || endPos == null)
            return false;
        return checkPos(player, stPos.getX(), stPos.getY(), stPos.getZ(), endPos.getX(), endPos.getY(), endPos.getZ());
    }

    public static boolean checkPos(EntityLivingBase player, double x, double y, double z, double x2, double y2, double z2) {
        int minX = Math.min(round(x), round(x2));
        int maxX = Math.max(round(x), round(x2));
        int minY = Math.min(round(y), round(y2));
        int maxY = Math.max(round(y), round(y2));
        int minZ = Math.min(round(z), round(z2));
        int maxZ = Math.max(round(z), round(z2));
        return minX <= player.posX && maxX >= player.posX && minY <= player.posY && maxY >= player.posY
                && minZ <= player.posZ && maxZ >= player.posZ;
    }

    public static Random rand = new Random();
    public static int getUPBlock(World world, int x, int y, int z){
        return getUPBlock(world, x,y,z,0);
    }
    private static int getUPBlock(World world, int x, int y, int z, int run) {
        final int i = run++;
        if (world.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.AIR) {
            if (world.getBlockState(new BlockPos(x, y + 1, z)).getBlock() != Blocks.AIR) {
                return getUPBlock(world, x, y + 1, z, i);
            }
            if (world.getBlockState(new BlockPos(x, y - 1, z)).getBlock() != Blocks.AIR) {
                return y - 1;
            } else if (run < 20) {
                return getUPBlock(world, x, y - 2, z, i);
            }
        }
        if (world.getBlockState(new BlockPos(x, y + 1, z)).getBlock() != Blocks.AIR) {// X,Y,Z
            return getUPBlock(world, x, y + 1, z, i);
        }
        return y;
    }

    public static EntityPlayer getPlayer() {
        MinecraftServer s = FMLCommonHandler.instance().getMinecraftServerInstance();
        if ((s == null || s.getPlayerList() == null || s.getPlayerList().getPlayerList() == null
                || s.getPlayerList().getPlayerList().size() == 0))
            return null;

        return s.getPlayerList().getPlayerList().get(0);
    }

    public static EntityPlayerMP getPlayerMP() {
        MinecraftServer s = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (s == null || s.getPlayerList() == null || s.getPlayerList().getPlayerList() == null
                || s.getPlayerList().getPlayerList().size() == 0)
            return null;

        return s.getPlayerList().getPlayerList().get(0);
    }


    public static World getWorld() {
        return getServer().getEntityWorld();
    }

    public static String getCurrentWorldName() {
        if(getServer() == null || getServer().worldServers.length == 0 || getPlayerMP() == null || getPlayerMP().worldObj.getWorldInfo().getWorldName().equalsIgnoreCase("mpserver")) {
            return "noworld";
        }
        return getServer().getEntityWorld().getWorldInfo().getWorldName();
    }

    public static File getCurrentWorldFile() {
        if(FMLCommonHandler.instance().getSide() == Side.CLIENT)
        return new File("./saves/" + getCurrentWorldName());
        else
            return new File("./"+getCurrentWorldName());
    }

    public static void command(String command) {
            command(WorldAPI.getPlayer(), command);
    }

    public static void command(ICommandSender sender, String command) {
        boolean client = isClientCommand(command);
        boolean server = isServerCommand(command);
        if (client) {
            ClientCommandHandler.instance.executeCommand(sender, command);
            return;
        }
        if (server) {
            getServer().getCommandManager().executeCommand(sender, command);
            return;
        }
    }

    public static boolean isClientCommand(String command){
        String commandName;
        if (command.split(" ").length > 0)
            commandName = command.split(" ")[0].replace("/", "");//명령어를 찾기 위해 인자를 때어냄
        else
            commandName = command.replaceFirst("/", "");
        return FMLCommonHandler.instance().getSide() == Side.CLIENT && ClientCommandHandler.instance.getCommands().get(commandName) != null;
    }
    public static boolean isServerCommand(String command){
        String commandName;
        if (command.split(" ").length > 0)
            commandName = command.split(" ")[0].replace("/", "");//명령어를 찾기 위해 인자를 때어냄
        else
            commandName = command.replaceFirst("/", "");

        return  getServer().getCommandManager().getCommands().get(commandName) != null;
    }
    public static MinecraftServer getServer() {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
    }


    public static double x() {
        return getPlayer().posX;
    }

    public static double y() {
        return getPlayer().posY;
    }

    public static double z() {
        return getPlayer().posZ;
    }

    public static double[] getVecXZ(EntityLivingBase base, double distance) {
        double[] xz = new double[2];
        xz[0] = base.getLookVec().xCoord * distance;
        xz[1] = base.getLookVec().zCoord * distance;
        return xz;
    }

    public static double[] getVecXZ(float pitch, float yaw, double distance) {
        double[] xz = new double[2];
        Vec3d vec = getVectorForRotation(pitch, yaw);
        xz[0] = vec.xCoord * distance;
        xz[1] = vec.zCoord * distance;
        return xz;
    }

    public static BlockPos getLookBlock() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK
                && mc.objectMouseOver.getBlockPos() != null)
            return mc.objectMouseOver.getBlockPos();
        return null;

    }

    protected final static Vec3d getVectorForRotation(float pitch, float yaw) {

        float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3d((double) (f1 * f2), (double) f3, (double) (f * f2));
    }

    public static String getUUID(EntityLivingBase base) {
        if (base != null)
            return base.getUniqueID().toString();
        else
            return null;
    }

    public static void addMessage(String text) {
        if(getPlayer() != null)
        getPlayer().addChatComponentMessage(new TextComponentString(text));
        else
            Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(text);
    }

    public static EntityPlayer findPlayer(Entity e) {
        return e.worldObj.getClosestPlayerToEntity(e, 10.0D);
    }

    public static int findInventoryItemCount(EntityPlayer player, Item e) {
        int appleCount = 0;
        for (ItemStack itemstack : player.inventory.mainInventory) {
            if (itemstack != null && itemstack.getItem() == e) {
                appleCount += itemstack.stackSize;
            }
        }
        return appleCount;
    }

    public static boolean equalsItem(ItemStack item, Item item2) {
        if (item != null) {
            return item.getItem() == item2;
        } else
            return false;
    }

    public static boolean equalsHeldItem(Item item2) {
        if (WorldAPI.getPlayer() == null)
            return false;
        ItemStack item = WorldAPI.getPlayer().getHeldItemMainhand();
        if (item != null) {
            return item.getItem() == item2;
        } else
            return false;
    }
    public static boolean equalsHeldItem(EntityPlayer player, Item item2) {
        ItemStack item = player.getHeldItemMainhand();
        if (item != null) {
            return item.getItem() == item2;
        } else
            return false;
    }
    public static List<EntityPlayerMP> getPlayerList() {
        if(getServer().getPlayerList() != null)
        return getServer().getPlayerList().getPlayerList();
        else return null;
    }
}
