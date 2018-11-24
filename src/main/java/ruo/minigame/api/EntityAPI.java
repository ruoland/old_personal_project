package ruo.minigame.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import ruo.cmplus.deb.DebAPI;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.Move;
import ruo.minigame.effect.TickRegister;
import ruo.minigame.map.EntityDefaultNPC;

import javax.annotation.Nullable;
import java.util.List;

public class EntityAPI {

    public static NBTTagCompound getNBT(Entity entity){
        NBTTagCompound tagCompound = new NBTTagCompound();
        entity.writeToNBT(tagCompound);
        return tagCompound;
    }
    /**
     * Creates a Vec3 using the pitch and yaw of the entities rotation.
     */
    protected static Vec3d getVectorForRotation(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3d((double) (f1 * f2), (double) f3, (double) (f * f2));
    }

    public static double lookPosX(EntityLivingBase base, float plusYaw, double plus) {
        Vec3d vec3d = getVectorForRotation(base.rotationPitch, base.rotationYaw + plusYaw);
        return base.posX + vec3d.xCoord * plus;
    }

    public static double lookPosZ(EntityLivingBase base, float plusYaw, double plus) {
        Vec3d vec3d = getVectorForRotation(base.rotationPitch, base.rotationYaw + plusYaw);
        return base.posZ + vec3d.zCoord * plus;
    }
    public static double lookX(EntityLivingBase base, double plus) {
        return lookX(base.getHorizontalFacing(), plus);
    }

    public static double lookZ(EntityLivingBase base, double plus) {
        return lookZ(base.getHorizontalFacing(), plus);
    }

    public static double lookX(EntityLivingBase base, float plusYaw, double plus) {
        return lookPosX(base, plusYaw, plus) - base.posX;
    }

    public static double lookZ(EntityLivingBase base, float plusYaw, double plus) {
        return lookPosZ(base, plusYaw, plus) - base.posZ;
    }

    public static double lookX(EnumFacing base, double plus) {
        switch (base) {
            case NORTH:
                return 0;
            case SOUTH:
                return 0;
            case EAST:
                return plus;
            case WEST:
                return -plus;
        }
        return 0;
    }

    public static double lookZ(EnumFacing base, double plus) {
        switch (base) {
            case NORTH:
                return -plus;
            case SOUTH:
                return plus;
            case EAST:
                return 0;
            case WEST:
                return 0;
        }
        return 0;
    }

    public static double getX(EntityLivingBase base, Direction spawnDirection, double plus, boolean pos) {
        switch (spawnDirection) {
            case FORWARD:
                return forwardX(base, plus, pos);
            case BACK:
                return backX(base, plus, pos);
            case RIGHT:
                return rightX(base, plus, pos);
            case LEFT:
                return leftX(base, plus, pos);
        }
        DebAPI.msgText("MiniGame","주의! 해당하는 방향 없음!");
        return 0;
    }

    public static double getX(EntityLivingBase base, Direction spawnDirection, double plus, double rlplus, boolean pos) {
        switch (spawnDirection) {
            case FORWARD_LEFT:
                return forwardX(base, plus, pos)+leftX(base, rlplus, false);
            case FORWARD_RIGHT:
                return forwardX(base, plus, pos)+rightX(base, rlplus, false);
            case BACK_LEFT:
                return backX(base, plus, pos)+leftX(base, rlplus, false);
            case BACK_RIGHT:
                return backX(base, plus, pos)+rightX(base, rlplus, false);
        }
        DebAPI.msgText("MiniGame","주의! 해당하는 방향 없음!");
        return getX(base, spawnDirection, plus, pos);
    }

    public static double getZ(EntityLivingBase base, Direction spawnDirection, double plus, boolean pos) {
        switch (spawnDirection) {
            case FORWARD:
                return forwardZ(base, plus, pos);
            case BACK:
                return backZ(base, plus, pos);
            case RIGHT:
                return rightZ(base, plus, pos);
            case LEFT:
                return leftZ(base, plus, pos);
        }
        DebAPI.msgText("MiniGame","주의! 해당하는 방향 없음!");
        return 0;
    }

    public static double getZ(EntityLivingBase base, Direction spawnDirection, double plus, double rlplus, boolean pos) {
        switch (spawnDirection) {
            case FORWARD_LEFT:
                return forwardZ(base, plus, pos)+leftZ(base, rlplus, false);
            case FORWARD_RIGHT:
                return forwardZ(base, plus, pos)+rightZ(base, rlplus, false);
            case BACK_LEFT:
                return backZ(base, plus, pos)+leftZ(base, rlplus, false);
            case BACK_RIGHT:
                return backZ(base, plus, pos)+rightZ(base, rlplus, false);
        }

        DebAPI.msgText("MiniGame","주의! 해당하는 방향 없음!");
        return getZ(base, spawnDirection, plus, pos);
    }

    private static double leftX(EntityLivingBase base, double plus, boolean pos) {
        EnumFacing left = EnumFacing.fromAngle(base.getHorizontalFacing().getHorizontalAngle() - 90);
        return forwardX(base, left, plus, pos);
    }

    private static double leftZ(EntityLivingBase base, double plus, boolean pos) {
        EnumFacing left = EnumFacing.fromAngle(base.getHorizontalFacing().getHorizontalAngle() - 90);
        return forwardZ(base, left, plus, pos);

    }

    private static double rightX(EntityLivingBase base, double plus, boolean pos) {
        EnumFacing right = EnumFacing.fromAngle(base.getHorizontalFacing().getHorizontalAngle() + 90);
        return forwardX(base, right, plus, pos);
    }

    private static double rightZ(EntityLivingBase base, double plus, boolean pos) {
        EnumFacing right = EnumFacing.fromAngle(base.getHorizontalFacing().getHorizontalAngle() + 90);
        return forwardZ(base, right, plus, pos);
    }

    public static double forwardX(EntityLivingBase base, double plus, boolean pos) {
        return forwardX(base, base.getHorizontalFacing(), plus, pos);
    }

    public static double forwardZ(EntityLivingBase base, double plus, boolean pos) {
        return forwardZ(base, base.getHorizontalFacing(), plus, pos);
    }

    public static double backX(EntityLivingBase base, double minus, boolean pos) {
        return forwardX(base, -minus, pos);
    }

    public static double backZ(EntityLivingBase base, double minus, boolean pos) {
        return forwardZ(base, -minus, pos);
    }

    private static double forwardX(EntityLivingBase base, EnumFacing facing, double plus, boolean pos) {
        double position = base.posX;
        if (!pos)
            position = 0;
        if (facing.getName().equalsIgnoreCase("NORTH")) {
            return position;
        }
        if (facing.getName().equalsIgnoreCase("SOUTH")) {
            return position;
        }
        if (facing.getName().equalsIgnoreCase("EAST")) {
            return position + plus;
        }
        if (facing.getName().equalsIgnoreCase("WEST")) {
            return position - plus;
        }
        return position;
    }

    private static double forwardZ(EntityLivingBase base, EnumFacing facing, double plus, boolean pos) {
        double position = base.posZ;
        if (!pos)
            position = 0;
        if (facing.getName().equalsIgnoreCase("NORTH")) {
            return position - plus;
        }
        if (facing.getName().equalsIgnoreCase("SOUTH")) {
            return position + plus;
        }
        if (facing.getName().equalsIgnoreCase("EAST")) {
            return position;
        }
        if (facing.getName().equalsIgnoreCase("WEST")) {
            return position;
        }
        return position;
    }

    public static EntityDefaultNPC spawn(double x, double y, double z) {
        EntityDefaultNPC defPlayer = new EntityDefaultNPC(WorldAPI.getWorld());
        defPlayer.setPosition(x, y, z);
        WorldAPI.getWorld().spawnEntityInWorld(defPlayer);
        return defPlayer;
    }

    public static ItemStack createBook(String title, String author, String... text) {
        ItemStack b = new ItemStack(Items.WRITTEN_BOOK);
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        for (int i = 0; i < 8; i++) {
            if (text.length <= i || text[i] == null) {
                list.appendTag(new NBTTagString("{\"text\":\"" + "페이지를 찾을 수 없습니다." + "\"}"));
                continue;
            }
            list.appendTag(new NBTTagString("{\"text\":\"" + text[i] + "\"}"));
        }
        nbt.setString("title", title);
        nbt.setString("author", author);
        nbt.setTag("pages", list);
        b.setTagInfo("author", new NBTTagString(author));
        b.setTagInfo("title", new NBTTagString(title));
        b.setTagInfo("pages", list);
        b.setTagCompound(nbt);
        return b;
    }

    @Nullable
    public static void moveFly(EntityLivingBase base, double targetX, double targetY, double targetZ, double distance, AbstractTick abs) {
        TickRegister.register(new AbstractTick(20, true) {
            @Override
            public void run(Type type) {
                if (base.getDistance(targetX, targetY, targetZ) < distance) {
                    absLoop = false;
                    if (abs != null) {
                        abs.run();
                    }
                }
                base.setVelocity((targetX - base.posX) / 20, (targetY - base.posY) / 20, (targetZ - base.posZ) / 20);
            }
        });
    }

    public static void move(double speed, EntityLiving mob, double x, double y, double z, final double... xyz) {
        move(new Move(mob, x, y, z, false) {
            @Override
            public void complete() {
                if (xyz.length > movecount + 2) {
                    setPosition(xyz[movecount], xyz[movecount + 1], xyz[movecount + 2]);
                    movecount += 2;
                }
            }
        }.setSpeed(speed));
    }

    public static void move(EntityLiving mob, double x, double y, double z, final double... xyz) {
        move(1, mob, x, y, z, xyz);
    }

    /*
    특정 시간이 지날 때까지 해당 방향으로 이동하고
    시간이 지나면 반대 방향으로 다시 이동함
     */
    public static void moveAndReturn(int tick, EntityLivingBase base, EnumFacing facing) {
        TickRegister.register(new AbstractTick(Type.SERVER, 1, true) {
            @Override
            public void run(Type type) {
                if (absRunCount == tick) {
                    absLoop = false;
                    System.out.println("뒤로 감");
                    TickRegister.register(new AbstractTick(1, true) {
                        @Override
                        public void run(Type type) {
                            if (absRunCount == tick) {
                                absLoop = false;
                                return;
                            }
                            base.setVelocity(-getFacingX(facing) / 15, 0, -getFacingZ(facing) / 15);
                        }
                    });
                    return;
                }
                base.setVelocity(getFacingX(facing) / 15, 0, getFacingZ(facing) / 15);
            }
        });
    }

    public static boolean isMove(EntityLivingBase base) {
        boolean abs = TickRegister.isAbsTickRun(base.getUniqueID().toString() + "-MOVE");
        return abs;
    }

    public static void move(final Move move) {
        boolean abs = TickRegister.isAbsTickRun(move.mob.getUniqueID().toString());
        if (abs) {
            removeMove(move.mob);
            System.out.println("이동 코드가 이미 실행 중이어서 기존 코드는 삭제됐습니다");
        }
        move.moveStart();
    }

    public static void removeMove(Entity entity) {
        AbstractTick abs = TickRegister.getAbsTick(entity.getName() + "-MOVE");
        abs.stopTick();
        TickRegister.remove(abs);
    }

    public static void removeMove(String name) {
        AbstractTick abs = TickRegister.getAbsTick(name + "-MOVE");
        TickRegister.remove(abs);
    }

    public static void pauseMove(String name, boolean pause) {
        AbstractTick abs = TickRegister.getAbsTick(name + "-MOVE");
        abs.pauseTick(pause);
    }

    public static void pauseMove(EntityLivingBase livingbase, boolean pause, int tick) {
        pauseMove(livingbase.getName(), pause);
        TickRegister.register(new AbstractTick(tick, false) {
            @Override
            public void run(Type type) {
                if (isMove(livingbase))
                    pauseMove(livingbase.getName(), !pause);
                else
                    System.out.println("이동 중이지 않아서 일시 정지를 해제하지 못함.");
            }
        });
    }

    public static void pauseMove(EntityLivingBase livingbase, boolean pause) {
        pauseMove(livingbase.getName(), pause);
    }

    public static void moveTimer(final int time, final Move move) {
        TickRegister.register(new AbstractTick(Type.SERVER, time, false) {
            @Override
            public void run(Type type) {
                move(move);
            }
        });
    }

    public static <T extends Entity> List<T> getEntity(World worldObj, AxisAlignedBB aabb, Class<? extends T> entity) {
        return worldObj.getEntitiesWithinAABB(entity, aabb);
    }

    public static List<EntityItem> getEntityItem(World worldObj, AxisAlignedBB aabb) {
        return worldObj.getEntitiesWithinAABB(EntityItem.class, aabb);
    }

    public static Item getPlayerItem() {
        if (WorldAPI.getPlayer().getHeldItemMainhand() == null)
            return null;
        return WorldAPI.getPlayer().getHeldItemMainhand().getItem();
    }

    /**
     * 플레이어와 mob의 거리가 distance 안에 있는 경우 실행함(반복됨)
     */
    public static void distanceTick(int distance, EntityLiving mob, AbstractTick tick2) {
        TickRegister.register(new AbstractTick(10, true) {
            @Override
            public void run(Type type) {
                if (WorldAPI.getPlayer().getDistanceToEntity(mob) < distance) {
                    tick2.run();
                    absLoop = false;
                }
            }
        });
    }

    /**
     * 플레이어와 XYZ 거리가 distance 안에 있는 경우
     */
    public static void distanceTick(int distance, double x, double y, double z, AbstractTick tick2) {
        TickRegister.register(new AbstractTick(10, true) {
            @Override
            public void run(Type type) {
                if (WorldAPI.getPlayer().getDistance(x, y, z) < distance) {
                    tick2.run();
                    absLoop = false;
                }
            }
        });
    }

    public static double getDistance(EntityLiving mob) {
        return mob.getDistanceToEntity(WorldAPI.getPlayer());
    }

    public static void position(EntityLivingBase base, double x, double y, double z, double distance,
                                AbstractTick.Position abs) {
        abs.mob = base;
        abs.posX = x;
        abs.posY = y;
        abs.posZ = z;
        abs.distance = distance;
        TickRegister.register(abs);
    }

    public static void position(EntityLivingBase base, double x, double y, double z, double x2, double y2, double z2, double distance,
                                AbstractTick.Position abs) {
        abs.mob = base;
        abs.posX = x;
        abs.posY = y;
        abs.posZ = z;
        abs.distance = distance;
        abs.setPos(x, y, z, x2, y2, z2);
        TickRegister.register(abs);
    }

    public static void position(EntityLivingBase base, BlockPos pos, BlockPos pos2, double distance,
                                AbstractTick.Position abs) {
        position(base, pos.getX(), pos.getY(), pos.getZ(), pos2.getX(), pos2.getY(), pos2.getZ(), distance, abs);
    }

    public static void position(BlockPos pos, BlockPos pos2, double distance,
                                AbstractTick.Position abs) {
        position(WorldAPI.getPlayer(), pos.getX(), pos.getY(), pos.getZ(), pos2.getX(), pos2.getY(), pos2.getZ(), distance, abs);
    }

    public static void position(double x, double y, double z, double x2, double y2, double z2, double distance,
                                AbstractTick.Position abs) {
        position(WorldAPI.getPlayer(), x, y, z, x2, y2, z2, distance, abs);

    }

    public static void position(double x, double y, double z, double distance, AbstractTick.Position abs) {
        position(WorldAPI.getPlayer(), x, y, z, distance, abs);

    }

    public static void position(EntityLivingBase mob, BlockPos pos, double distance, AbstractTick.Position abs) {
        position(mob, pos.getX(), pos.getY(), pos.getZ(), distance, abs);
    }

    public static void position(EntityLivingBase mob, BlockPos pos, AbstractTick.Position abs) {
        position(mob, pos.getX(), pos.getY(), pos.getZ(), 0, abs);
    }

    public static void position(EntityLivingBase mob, double x, double y, double z, AbstractTick.Position abs) {
        position(mob, x, y, z, 0, abs);
    }

    public static void position(BlockPos pos, AbstractTick.Position abs) {
        position(WorldAPI.getPlayer(), pos.getX(), pos.getY(), pos.getZ(), 0, abs);
    }

    public static void position(double x, double y, double z, AbstractTick.Position abs) {
        position(WorldAPI.getPlayer(), x, y, z, 0, abs);
    }

    public static void position(BlockPos pos, double distance, AbstractTick.Position abs) {
        position(WorldAPI.getPlayer(), pos.getX(), pos.getY(), pos.getZ(), distance, abs);
    }


    public static void removeAllPosition() {
        TickRegister.removeAllPosition();
    }

    public static boolean isLook(EntityLiving mob) {
        return TickRegister.isAbsTickRun(mob.getName() + "-LOOK");
    }

    public static void look(final EntityLiving mob, final BlockPos pos) {
        look(mob, pos.getX(), pos.getY(), pos.getZ());
    }

    public static void look(final EntityLiving mob, final double x, final double y, final double z) {
        if (isLook(mob)) {
            removeLook(mob);
        }
        AbstractTick tick = new AbstractTick(mob.getName() + "-LOOK", Type.SERVER, 1, true) {
            @Override
            public void run(Type type) {
                mob.getLookHelper().setLookPosition(x, y, z, mob.getHorizontalFaceSpeed(), mob.getVerticalFaceSpeed());
            }
        };
        TickRegister.register(tick);
    }

    public static void look(final EntityLiving mob, final EntityLivingBase mob2) {
        if (isLook(mob)) {
            removeLook(mob);
        }
        AbstractTick tick = new AbstractTick(mob.getName() + "-LOOK", Type.SERVER, 1, true) {
            @Override
            public boolean stopCondition() {
                if (mob == null || mob2 == null || (mob.isDead || mob2.isDead)) {
                    removeLook(mob);
                    return true;
                } else
                    return false;
            }

            @Override
            public void run(Type type) {
                if (mob == null || mob2 == null || (mob.isDead || mob2.isDead)) {
                    absLoop = false;
                    return;
                }
                mob.getLookHelper().setLookPositionWithEntity(mob2, mob.getHorizontalFaceSpeed(),
                        mob.getVerticalFaceSpeed());
            }
        };
        TickRegister.register(tick);
    }

    /**
     * mob이 플레이어를 보게 함
     */
    public static void lookPlayer(final EntityLiving mob) {
        look(mob, WorldAPI.getPlayer());
    }

    public static void removeLook(EntityLiving mob) {
        if (isLook(mob)) {
            TickRegister.remove(mob.getName() + "-LOOK");
        }
    }

    public static double getFacingYaw(double rotationYaw) {
        EnumFacing facing = EnumFacing.fromAngle(rotationYaw);
        return facing.getHorizontalAngle();
    }

    public static double getFacingX(Entity entity) {
        return getFacingX(entity.rotationYaw);
    }

    public static double getFacingX(double rotationYaw) {
        EnumFacing facing = EnumFacing.fromAngle(rotationYaw);
        return getFacingX(EnumFacing.fromAngle(rotationYaw));
    }

    public static double getFacingX(EnumFacing facing) {
        String index = facing.getName();
        double veloX = 0;
        if (index.equalsIgnoreCase("WEST")) {
            veloX = -1;
        }
        if (index.equalsIgnoreCase("EAST")) {
            veloX = 1;
        }
        return veloX;
    }

    public static double getFacingY(double rotationPitch) {
        EnumFacing facing = EnumFacing.fromAngle(rotationPitch);
        String index = facing.getName();
        double veloX = 0;
        if (index.equalsIgnoreCase("UP")) {
            veloX = 1;
        }
        if (index.equalsIgnoreCase("DOWN")) {
            veloX = -1;
        }
        return veloX;
    }

    public static double getFacingZ(Entity entity) {
        return getFacingZ(entity.rotationYaw);
    }

    public static double getFacingZ(double rotationYaw) {
        EnumFacing facing = EnumFacing.fromAngle(rotationYaw);
        return getFacingZ(EnumFacing.fromAngle(rotationYaw));
    }

    public static double getFacingZ(EnumFacing facing) {
        String index = facing.getName();
        double veloZ = 0;
        if (index.equalsIgnoreCase("NORTH")) {
            veloZ = -1;
        }
        if (index.equalsIgnoreCase("SOUTH")) {
            veloZ = 1;
        }
        return veloZ;
    }
}
