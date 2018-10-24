package ruo.minigame.api;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Timer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import ruo.minigame.MiniGame;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.AbstractTick.BlockXYZ;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class WorldAPI {
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

    public static int round(float r){
        return Math.round(r);
    }

    public static boolean equalsWorldName(String worldName) {
        if(getCurrentWorldName().equalsIgnoreCase("noworld"))
            MiniGame.LOG.warning("현재 월드의 이름과 "+worldName+"을 비교 하려 했지만 현재 월드 이름이 noworld임!!!!");
        return getCurrentWorldName().equalsIgnoreCase(worldName);
    }

    public static boolean equalsBlock(Block block, Block block2) {
        return block != null && block2 != null && block.getLocalizedName().equals(block2.getLocalizedName());
    }

    public static void worldtime(float time) {
        try {
            Field f = Minecraft.class.getDeclaredField("timer");
            f.setAccessible(true);
            Timer timer = (Timer) f.get(Minecraft.getMinecraft());
            timer.timerSpeed = time;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void rain() {
        int i = (300 + (new Random()).nextInt(600)) * 20;
        WorldInfo worldinfo = WorldAPI.getWorld().getWorldInfo();
        worldinfo.setCleanWeatherTime(0);
        worldinfo.setRainTime(i);
        worldinfo.setThunderTime(i);
        worldinfo.setRaining(true);
        worldinfo.setThundering(false);
    }

    /**
     * String 인자를 하나로 묶어주는 메서드 묶을 때 점프로 구분할 수 있게 해줌
     */
    public static String str(String[] str, boolean space) {
        StringBuffer b = new StringBuffer();
        for (String s : str) {

            b.append(s);
            if(space)
                b.append(" ");
        }
        return b.toString();
    }

    /**
     * String 인자를 하나로 묶어주는 메서드 묶을 때 점프로 구분할 수 있게 해줌
     */
    public static String strBind(boolean space, String... str) {
        StringBuffer b = new StringBuffer();
        for (String s : str) {
            b.append(s);
            if(space)
            b.append(" ");
        }
        return b.toString();
    }

    public static boolean isDay() {
        if (getPlayer() != null)
            return getPlayer().worldObj.getWorldTime() < 12574 && getPlayer().getEntityWorld().getWorldTime() > 0;
        else
            return false;
    }

    public static boolean isNight() {
        if (getPlayer() != null)
            return getPlayer().getEntityWorld().getWorldTime() > 13875 && getPlayer().worldObj.getWorldTime() < 23999;
        else
            return false;
    }

    /**
     * 현재 플레이어가 있는 월드의 시간을 나타냅니다
     */
    public static int getWorldMinutes() {
        if (getPlayer() != null) {
            int time = (int) Math.abs((getPlayer().worldObj.getWorldTime()) % 24000);
            return (time % 1000) * 6 / 100;
        } else
            return 0;
    }

    public static int getWorldHours() {
        if (getPlayer() != null) {
            int time = (int) Math.abs((getPlayer().worldObj.getWorldTime()) % 24000);
            return (int) ((float) time / 1000F);
        } else
            return 0;
    }

    public static int getWorldDay(World worldObj) {
        int i = (int) (worldObj.getWorldTime() / 20);
        int j = i / 60;
        return j / 20;
    }

    public static String getWorldTime(World worldObj) {
        return StringUtils.ticksToElapsedTime((int) worldObj.getWorldTime());
    }

    public static String getWorldTotalTime(World worldObj) {
        return StringUtils.ticksToElapsedTime((int) worldObj.getTotalWorldTime());
    }

    /**
     * 아이템의 이름을 반환합니다
     */
    public static String getStackName(ItemStack itemstack) {
        if(itemstack == null) {
            MiniGame.LOG.warning("아이템의 이름을 반환하려 했지만 아이템 객체가 null입니다.");
            return "없음";
        }
        return itemstack.getDisplayName();
    }

    public static String getEntityName(Entity itemstack) {
        if(itemstack == null) {
            MiniGame.LOG.warning("엔티티의 이름을 반환하려 했지만 엔티티 객체가 null입니다.");
            return "없음";
        }
        return itemstack.getCustomNameTag();
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
            } catch (java.lang.ClassCastException e) {
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

    public static void blockTick(World world, double x, double y, double z, int distance,
                                 BlockXYZ xyz) {
        blockTick(world, round(x) + distance, round(x) - distance, round(y) + distance, round(y) - distance, round(z) + distance, round(z) - distance, xyz);
    }

    public static void blockTick(World world, double x, double x2, double y, double y2, double z, double z2,
                                 BlockXYZ xyz) {
        blockTick(world, round(x), round(x2), round(y), round(y2), round(z), round(z2), xyz);
    }

    public static BlockAPI getBlock(World world, double x, double x2, double y, double y2, double z,
                                    double z2) {
        return getBlock(world, round(x), round(x2), round(y), round(y2), round(z), round(z2));
    }
    public static BlockAPI getBlock(World world, BlockPos pos, double expand) {
        return getBlock(world, round(pos.getX() - expand), round(pos.getX() + expand), round(pos.getY() - expand), round(pos.getY() + expand), round(pos.getZ() - expand), round(pos.getZ() + expand));
    }
    public static BlockAPI getBlock(World world, double x, double y, double z, double expand) {
        BlockPos pos = new BlockPos(x,y,z);
        return getBlock(world, round(pos.getX() - expand), round(pos.getX() + expand), round(pos.getY() - expand), round(pos.getY() + expand), round(pos.getZ() - expand), round(pos.getZ() + expand));
    }
    public static BlockAPI getBlock(World world, double x, double x2, double y, double z, double z2) {
        return getBlock(world, round(x), round(x2), round(y), round(z), round(z2));
    }

    public static void setBlock(World world, int x, int y, int z, int x2, int y2, int z2, Block block) {
        int minX = Math.min(x, x2);
        int maxX = Math.max(x, x2);
        int minY = Math.min(y, y2);
        int maxY = Math.max(y, y2);
        int minZ = Math.min(z, z2);
        int maxZ = Math.max(z, z2);
        for (x = minX; x <= maxX; x++) {
            for (y = minY; y <= maxY; y++) {
                for (z = minZ; z <= maxZ; z++) {
                    world.setBlockState(new BlockPos(x, y, z), block.getDefaultState());
                }
            }
        }
    }

    /**
     * XZ에서 가장 위에 있는 블럭만 가져옴
     */
    public static BlockAPI getBlock(World world, int x, int x2, int y, int z, int z2) {
        int minX = Math.min(x, x2);
        int maxX = Math.max(x, x2);
        int minZ = Math.min(z, z2);
        int maxZ = Math.max(z, z2);
        BlockAPI api = new BlockAPI();
        for (x = minX; x <= maxX; x++) {
            for (z = minZ; z <= maxZ; z++) {
                if (world.getBlockState(new BlockPos(x, getUPBlock(world, x, y, z), z)).getBlock() == null)
                    continue;
                api.addBlock(WorldAPI.getWorld(), x, getUPBlock(world, x, y, z), z);
            }
        }
        return api;
    }

    public static void blockTick(World world, int x, int x2, int y, int y2, int z, int z2, AbstractTick.BlockXYZ tick) {
        int minX = Math.min(x, x2);
        int maxX = Math.max(x, x2);
        int minY = Math.min(y, y2);
        int maxY = Math.max(y, y2);
        int minZ = Math.min(z, z2);
        int maxZ = Math.max(z, z2);

        for (x = minX; x <= maxX; x++) {
            for (y = minY; y <= maxY; y++) {
                for (z = minZ; z <= maxZ; z++) {
                    tick.x = x;
                    tick.y = y;
                    tick.z = z;
                    tick.run();

                }
            }
        }

    }

    public static BlockAPI getBlock(World world, int x, int x2, int y, int y2, int z, int z2) {
        int minX = Math.min(x, x2);
        int maxX = Math.max(x, x2);
        int minY = Math.min(y, y2);
        int maxY = Math.max(y, y2);
        int minZ = Math.min(z, z2);
        int maxZ = Math.max(z, z2);
        BlockAPI blockAPI = new BlockAPI();
        for (x = minX; x <= maxX; x++) {
            for (y = minY; y <= maxY; y++) {
                for (z = minZ; z <= maxZ; z++) {
                    blockAPI.addBlock(world, x, y, z);
                }
            }
        }
        return blockAPI;
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

    public static void teleport(double x, double y, double z) {
        teleport(x, y, z, getPlayer().rotationYaw,
                getPlayer().rotationPitch);
    }

    public static void teleport(BlockPos pos) {
        teleport(pos.getX(), pos.getY(), pos.getZ(), getPlayer().rotationYaw,
                getPlayer().rotationPitch);
    }

    public static void teleport(BlockPos pos, float yaw, float pitch) {
        teleport(pos.getX(), pos.getY(), pos.getZ(), yaw, pitch);
    }

    public static void teleport(double x, double y, double z, float yaw, float pitch) {
        ((EntityPlayerMP) getPlayer()).connection.setPlayerLocation(x, y, z, yaw, pitch);
    }

    public static EntityPlayerMP getPlayerByName(String username) {
        MinecraftServer s = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (s.getPlayerList().getPlayerList().size() == 0)
            return null;
        return (s.getPlayerList().getPlayerByUsername(username));
    }

    public static EntityPlayerMP getPlayerByUUID(String uuid) {
        MinecraftServer s = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (s.getPlayerList().getPlayerList().size() == 0)
            return null;
        return s.getPlayerList().getPlayerByUUID(UUID.fromString(uuid));
    }

    public static World getWorld() {
        return getServer().getEntityWorld();
    }

    public static String getCurrentWorldName() {

        if(getPlayerMP() == null || getPlayerMP().worldObj.getWorldInfo().getWorldName().equalsIgnoreCase("mpserver")) {
            return "noworld";
        }
        return getServer().getEntityWorld().getWorldInfo().getWorldName();
    }

    public static File getCurrentWorldFile() {
        return new File("./saves/" + getCurrentWorldName());
    }

    public static String[] getSignText(BlockPos block) {
        TileEntitySign sign = (TileEntitySign) WorldAPI.getWorld().getTileEntity(block);
        String[] text = new String[]{sign.signText[0].getFormattedText(), sign.signText[1].getFormattedText(),
                sign.signText[2].getFormattedText(), sign.signText[3].getFormattedText()};
        return text;
    }


    public static void command(String command) {
        boolean client = isClientCommand(command);
        boolean server = isServerCommand(command);
//        if(client)
//            command(Minecraft.getMinecraft().thePlayer, command);
//        else if(server)
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
//        Minecraft.getMinecraft().thePlayer.sendChatMessage(command);
    }

    public static boolean isClientCommand(String command){
        String commandName;
        if (command.split(" ").length > 0)
            commandName = command.split(" ")[0].replace("/", "");//명령어를 찾기 위해 인자를 때어냄
        else
            commandName = command.replaceFirst("/", "");
        return ClientCommandHandler.instance.getCommands().get(commandName) != null;
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


    public static void setBlock(double x, double y, double z, Block block) {
        getWorld().setBlockState(new BlockPos(round(x), round(y), round(z)), block.getDefaultState());
    }

    public static void setBlock(int x, int y, int z, Block block) {
        getWorld().setBlockState(new BlockPos(x, y, z), block.getDefaultState());
    }
    public static IBlockState getBlock(double x, double y, double z) {
        return getBlock(new BlockPos(x, y, z)).getDefaultState();
    }

    public static Block getBlock(BlockPos pos) {
        return getWorld().getBlockState(pos).getBlock();
    }

    public static IBlockState getBlock(int x, int y, int z) {
        return getWorld().getBlockState(new BlockPos(x, y, z));
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

    public static int findInventoryItemCount(Item e) {
        int appleCount = 0;
        for (ItemStack itemstack : WorldAPI.getPlayer().inventory.mainInventory) {
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
