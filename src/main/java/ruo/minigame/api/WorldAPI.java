package ruo.minigame.api;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityChest;
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
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.AbstractTick.BlockXYZ;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Random;
import java.util.UUID;

public class WorldAPI {

    public static boolean equalsWorldName(String worldName) {
        return getCurrentWorldName().equalsIgnoreCase(worldName);
    }

    public static boolean equalsBlock(Block block, Block block2) {
        return block != null && block2 != null && block.getLocalizedName().equals(block2.getLocalizedName());
    }

    public static boolean canEntitySeen(EntityLivingBase base, double x, double y, double z) {
        return base.worldObj.rayTraceBlocks(new Vec3d(base.posX, base.posY + (double) base.getEyeHeight(), base.posZ), new Vec3d(x, y, z), false, true, false) == null;
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

    /**
     *
     */
    public static String[] bufferToStringArray(StringBuffer... buffer) {
        String[] str = new String[buffer.length];
        int i = 0;
        for (StringBuffer b : buffer) {
            if (b == null || b.toString().equals(""))
                continue;
            str[i] = b.toString();
            i++;
        }
        return str;
    }

    /**
     * round가 5고, min이 3이라면 -3,-4,-5 3,4,5 이상의 값만 나옵니다
     *
     * @return
     */
    public static int minRand(int min, int round) {
        int random = rand.nextInt(round);
        if (min <= random) {
            return rand.nextBoolean() ? -random : random;
        }
        return minRand(min, round);
    }

    /**
     * round가 5고, min이 3이라면  3,4,5 이상의 값만 나옵니다
     *
     * @return
     */
    public static int minRand2(int min, int round) {
        int random = rand.nextInt(round);
        if (min <= random) {
            return random;
        }
        return minRand2(min, round);
    }

    /**
     * round가 5라면, -5~5까지 렌덤한 값을 보냅니다
     *
     * @return
     */
    public static int rand(int round) {
        return rand.nextBoolean() ? -rand.nextInt(round) : rand.nextInt(round);
    }

    /**
     * round가 5라면, -0.5~0.5까지 렌덤한 값을 보냅니다
     *
     * @return
     */
    public static double rand2(int round) {
        return rand.nextBoolean() ? -Double.valueOf("0." + rand.nextInt(round))
                : Double.valueOf("0." + rand.nextInt(round));
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
            b.append(s + (space ? " " : ""));
        }
        return b.toString();
    }

    /**
     * String 인자를 하나로 묶어주는 메서드 묶을 때 점프로 구분할 수 있게 해줌
     */
    public static String stra(boolean space, String... str) {
        StringBuffer b = new StringBuffer();
        for (String s : str) {
            b.append(s + (space ? " " : ""));
        }
        return b.toString();
    }

    public static void explosion(double x, double y, double z, int strength) {
        getWorld().newExplosion(null, x, y, z, strength, false, true);
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
        i = i % 60;
        return j / 20;
    }

    public static String getWorldTime(World worldObj) {
        return StringUtils.ticksToElapsedTime((int) worldObj.getWorldTime());
    }

    public static String getWorldTotalTime(World worldObj) {
        return StringUtils.ticksToElapsedTime((int) worldObj.getTotalWorldTime());
    }

    public static String getStackName(ItemStack itemstack) {
        return itemstack == null ? "없음" : itemstack.getDisplayName();
    }

    public static String getEntityName(Entity itemstack) {
        return itemstack == null ? "없음" : itemstack.getCustomNameTag();
    }

    public static double[] ppos() {
        return changePosArray(getPlayer());
    }

    public static int floor(double x) {
        return MathHelper.floor_double(x);
    }

    public static int[] floor(double[] a) {
        return new int[]{floor(a[0]), floor(a[1]), floor(a[2])};
    }

    public static double[] changePosArray(EntityLivingBase base) {
        return new double[]{base.posX, base.posY, base.posZ};
    }

    public static int[] changePosArrayInt(EntityLivingBase base) {
        return floor(changePosArray(base));
    }

    public static double[] floorPos(int x, int y, int z) {
        return new double[]{Double.valueOf(x), Double.valueOf(y), Double.valueOf(z)};
    }

    public static int[] floorPos(EntityLivingBase base) {
        return new int[]{floor(base.posX), floor(base.posY - base.getYOffset()), floor(base.posZ)};
    }

    public static int[] floorPos(double x, double y, double z) {
        return new int[]{floor(x), floor(y), floor(z)};
    }

    public static int[] floorPos(BlockPos pos) {
        return new int[]{floor(pos.getX()), floor(pos.getY()), floor(pos.getZ())};
    }

    public static double[] valueOf(BlockPos pos) {
        return new double[]{pos.getX(), pos.getY(), pos.getZ()};
    }

    public static double[] valueOfStr(String x, String y, String z) {
        return new double[]{Double.valueOf(x), Double.valueOf(y), Double.valueOf(z)};
    }

    public static double[] valueOfS(String x, String y, String z) {
        return valueOfStr(x, y, z);
    }

    public static String toXYZString(Entity e) {
        return e.getCustomNameTag() + ":::" + e.posX + "," + e.posY + "," + e.posZ;
    }

    public static boolean checkPos(EntityLivingBase living, int[] xyz) {
        return floor(living.posX) == xyz[0] && floor(living.posY) == xyz[1] && floor(living.posZ) == xyz[2];
    }

    public static boolean checkPos(EntityLivingBase living, EntityLivingBase living2) {
        return checkPos(living, living2.posX, living2.posY, living2.posZ);
    }

    public static boolean checkPos(EntityLivingBase living, double X, double Y, double Z) {
        double xc = cut(X);
        double yc = cut(Y);
        double zc = cut(Z);
        double livingXc = cut(living.posX);
        double livingYc = cut(living.posY);
        double livingZc = cut(living.posZ);
        return (xc == livingXc && yc == livingYc && zc == livingZc)
                || (floor(living.posX) == floor(X) && floor(living.posY) == floor(Y) && floor(living.posZ) == floor(Z));
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

    public static int[] floor_double(Entity entity) {
        return floor_double(entity.posX, entity.posY, entity.posZ);
    }

    public static int[] floor_double(double posX, double posY, double posZ) {
        int[] i = new int[3];
        i[0] = MathHelper.floor_double(posX);
        i[1] = MathHelper.floor_double(posY);
        i[2] = MathHelper.floor_double(posZ);
        return i;
    }

    public static boolean checkPos(EntityLivingBase player, double x, double z, double x2, double z2) {
        int minX = Math.min(floor(x), floor(x2));
        int maxX = Math.max(floor(x), floor(x2));
        int minZ = Math.min(floor(z), floor(z2));
        int maxZ = Math.max(floor(z), floor(z2));
        if (minX <= player.posX && maxX >= player.posX && minZ <= player.posZ && maxZ >= player.posZ) {
            return true;
        }
        return false;
    }

    public static boolean checkPos(EntityLivingBase player, BlockPos stPos, BlockPos endPos) {
        if (stPos == null || endPos == null)
            return false;
        return checkPos(player, stPos.getX(), stPos.getY(), stPos.getZ(), endPos.getX(), endPos.getY(), endPos.getZ());
    }

    public static boolean checkPos(EntityLivingBase player, double x, double y, double z, double x2, double y2, double z2) {
        int minX = Math.min(floor(x), floor(x2));
        int maxX = Math.max(floor(x), floor(x2));
        int minY = Math.min(floor(y), floor(y2));
        int maxY = Math.max(floor(y), floor(y2));
        int minZ = Math.min(floor(z), floor(z2));
        int maxZ = Math.max(floor(z), floor(z2));
        if (minX <= player.posX && maxX >= player.posX && minY <= player.posY && maxY >= player.posY
                && minZ <= player.posZ && maxZ >= player.posZ) {
            return true;
        }
        return false;
    }

    public static void blockTick(World world, double x, double y, double z, int distance,
                                 BlockXYZ xyz) {
        blockTick(world, floor(x) + distance, floor(x) - distance, floor(y) + distance, floor(y) - distance, floor(z) + distance, floor(z) - distance, xyz);
    }

    public static void blockTick(World world, double x, double x2, double y, double y2, double z, double z2,
                                 BlockXYZ xyz) {
        blockTick(world, floor(x), floor(x2), floor(y), floor(y2), floor(z), floor(z2), xyz);
    }

    public static BlockAPI getBlock(World world, double x, double x2, double y, double y2, double z,
                                    double z2) {
        return getBlock(world, floor(x), floor(x2), floor(y), floor(y2), floor(z), floor(z2));
    }
    public static BlockAPI getBlock(World world, BlockPos pos, double expand) {
        return getBlock(world, floor(pos.getX() - expand), floor(pos.getX() + expand), floor(pos.getY() - expand), floor(pos.getY() + expand), floor(pos.getZ() - expand), floor(pos.getZ() + expand));
    }
    public static BlockAPI getBlock(World world, double x, double y, double z, double expand) {
        BlockPos pos = new BlockPos(x,y,z);
        return getBlock(world, floor(pos.getX() - expand), floor(pos.getX() + expand), floor(pos.getY() - expand), floor(pos.getY() + expand), floor(pos.getZ() - expand), floor(pos.getZ() + expand));
    }
    public static BlockAPI getBlock(World world, double x, double x2, double y, double z, double z2) {
        return getBlock(world, floor(x), floor(x2), floor(y), floor(z), floor(z2));
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

    public static BlockAPI blockSave(String filename, World world, int x, int x2, int y, int y2, int z, int z2) {
        int minX = Math.min(x, x2);
        int maxX = Math.max(x, x2);
        int minY = Math.min(y, y2);
        int maxY = Math.max(y, y2);
        int minZ = Math.min(z, z2);
        int maxZ = Math.max(z, z2);
        BlockAPI api = null;

        for (x = minX; x <= maxX; x++) {
            for (y = minY; y <= maxY; y++) {
                for (z = minZ; z <= maxZ; z++) {
                    if (world.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.AIR)
                        continue;
                    if (api == null) {
                        api = new BlockAPI(world, x, y, z);
                        api.addBlock(world, x, y, z);

                        continue;
                    }
                    api.addBlock(world, x, y, z);
                }
            }
        }
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./" + filename));
            oos.writeObject(api);
            oos.flush();
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return api;
    }

    public static BlockAPI blockLoad(String filename) {
        BlockAPI blockapi = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./" + filename));
            blockapi = (BlockAPI) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blockapi;
    }

    static int run;
    public static Random rand = new Random();

    public static int getUPBlock(World world, int x, int y, int z) {
        run++;
        if (world.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.AIR) {
            if (world.getBlockState(new BlockPos(x, y + 1, z)).getBlock() != Blocks.AIR) {
                return getUPBlock(world, x, y + 1, z);
            }
            if (world.getBlockState(new BlockPos(x, y - 1, z)).getBlock() != Blocks.AIR) {
                run = 0;
                return y - 1;
            } else if (run < 20) {
                return getUPBlock(world, x, y - 2, z);
            }
        }
        if (world.getBlockState(new BlockPos(x, y + 1, z)).getBlock() != Blocks.AIR) {// X,Y,Z
            return getUPBlock(world, x, y + 1, z);
        }
        run = 0;
        return y;
    }

    public static EntityPlayerSP getPlayerSP() {
        return Minecraft.getMinecraft().thePlayer;
    }

    public static EntityPlayer getPlayer() {
        MinecraftServer s = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (s == null || s.getPlayerList() == null || s.getPlayerList().getPlayerList() == null
                || s.getPlayerList().getPlayerList().size() == 0)
            return Minecraft.getMinecraft().thePlayer;

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

    /**
     * TP 할 때 0.5를 더함
     *
     * @param pos
     */
    public static void teleport05(BlockPos pos) {
        teleport(pos.add(0.5, 0, 0.5));
    }

    public static void teleport(double x, double y, double z, float yaw, float pitch) {
        ((EntityPlayerMP) getPlayer()).connection.setPlayerLocation(x, y, z, yaw, pitch);
        try {
            throw new NullPointerException("");
        }catch (Exception e){
            e.printStackTrace();;
        }
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

    public static int nextInt(int bound) {
        return getPlayer().worldObj.rand.nextInt(bound);
    }

    public static boolean nextBoolean() {
        return getPlayer().worldObj.rand.nextBoolean();
    }

    public static World getWorld() {
        return getPlayer().worldObj;
    }

    public static String getCurrentWorldName() {
        if(getPlayer() == null || getPlayer().worldObj.getWorldInfo().getWorldName().equalsIgnoreCase("mpserver")) {
            return "noworld";
        }
        return getPlayer().worldObj.getWorldInfo().getWorldName();
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
        if(client)
            command(Minecraft.getMinecraft().thePlayer, command);
        else if(server)
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
        Minecraft.getMinecraft().thePlayer.sendChatMessage(command);
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

    public static void setBlock(int x, int y, int z, Block block) {
        getWorld().setBlockState(new BlockPos(x, y, z), block.getDefaultState());
    }

    /**
     * pos Y에서 y 사이에 블럭이 있는가
     */
    public static boolean isAirBlock(BlockPos pos, int y) {
        int countY = 0;
        while (getWorld().isAirBlock(pos)) {
            if (y != countY) {
                pos.add(0, -1, 0);
                countY++;
            } else if (y == countY) {
                return true;
            }
            continue;
        }
        return false;
    }

    public static IInventory getChestInventory(BlockPos pos) {
        TileEntityChest chest = (TileEntityChest) getWorld().getTileEntity(pos);

        return chest;
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

    // 바로 앞에 블럭이 있는지만 체크함
    public static IBlockState getLookBlock(Entity base) {
        Vec3d v = base.getLook(1.0F);
        double xc = Double.valueOf(String.format("%.2f", v.xCoord));
        double yc = Double.valueOf(String.format("%.2f", v.yCoord));
        double zc = Double.valueOf(String.format("%.2f", v.zCoord));
        double x = base.posX + xc;
        double y = base.posY + yc;
        double z = base.posZ + zc;
        System.out.println("X" + x);
        System.out.println("Y" + y);
        System.out.println("Z" + z);
        System.out.println("XC" + xc);
        System.out.println("YC" + yc);
        System.out.println("ZC" + zc);
        System.out.println("XC+++" + (x + xc));
        System.out.println("YC+++" + (y + yc));
        System.out.println("ZC+++" + (z + zc));
        System.out.println("Block" + getBlock(x, y, z));
        return getBlock(x, y, z);
    }

    /*
     * System.out.println("X"+x); System.out.println("Y"+y);
     * System.out.println("Z"+z); System.out.println("XC"+ v.xCoord);
     * System.out.println("YC"+ v.yCoord); System.out.println("ZC"+ v.zCoord);
     * System.out.println("Block"+getBlock(x+v.xCoord, y+v.yCoord, z+v.zCoord));
     */
    public static IBlockState getLookBlock(double x, double y, double z, float pitch, float yaw) {
        Vec3d v = getVectorForRotation(pitch, yaw);
        for (int i = 1; i < 7; i++) {
            double xc = Double.valueOf(String.format("%.2f", v.xCoord * i));
            double yc = Double.valueOf(String.format("%.2f", v.yCoord * i));
            double zc = Double.valueOf(String.format("%.2f", v.zCoord * i));
            System.out.println("Block" + getBlock(x + xc, y + yc, z + zc));
        }
        return null;
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

    public static void setBlock(double x, double y, double z, Block block) {
        getWorld().setBlockState(new BlockPos(floor(x), floor(y), floor(z)), block.getDefaultState());
    }

    public static String getUUID(EntityLivingBase base) {
        if (base != null)
            return base.getUniqueID().toString();
        else
            return null;
    }

    public static ModelBase getEntityModel(Entity entity) {
        Render r = Minecraft.getMinecraft().getRenderManager().getEntityRenderObject(entity);
        Field f;
        try {
            f = r.getClass().getDeclaredField("mainModel");
            f.setAccessible(true);

            return (ModelBase) f.get(r);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BlockPos[] getBlock4x4Pos(BlockPos pos) {
        BlockPos[] blockList = new BlockPos[]{pos.add(1, 0, 0), pos.add(-1, 0, 0), pos.add(0, 0, 1),
                pos.add(0, 0, -1)};
        return blockList;
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

    public static int findItem(Item e) {
        int appleCount = 0;
        for (ItemStack itemstack : WorldAPI.getPlayer().inventory.mainInventory) {
            if (itemstack != null && itemstack.getItem() == e) {
                appleCount += itemstack.stackSize;
            }
        }
        return appleCount;
    }

    public static Item getPlayerItem() {
        return WorldAPI.getPlayer().getHeldItemMainhand() != null ? WorldAPI.getPlayer().getHeldItemMainhand().getItem() : null;
    }

    public static String getPlayerItemName() {
        return WorldAPI.getPlayer().getHeldItemMainhand() != null ? WorldAPI.getPlayer().getHeldItemMainhand().getDisplayName() : null;
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
}
