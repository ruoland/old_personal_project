package map.tycoon;

import oneline.api.WorldAPI;
import oneline.effect.AbstractTick.BlockXYZ;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.BlockFence;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import map.tycoon.consumer.EntityConsumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class TyconHelper {
    public static ArrayList<BlockPos> dustList = new ArrayList<>();
    private static ArrayList<BlockPos> tableList = new ArrayList<BlockPos>();
    private static HashMap<BlockPos, Boolean> useTableList = new HashMap<BlockPos, Boolean>();

    private static ArrayList<EntityConsumer> waitConsumerList = new ArrayList<>();
    private static HashMap<String, BreadData> breadHash = new HashMap<>();
    public static double calcX, calcY, calcZ, playermoney;
    private static BlockPos pos1, pos2;//왼쪽 아래 좌표와 오른쪽 위 좌표
    private static BlockPos breadDoorPos = new BlockPos(-468,6,-816);

    public static BlockPos getTyconDoorPos(){
        return breadDoorPos;
    }
    public static void setSellerPos(EntityConsumer consumer) {
        calcX = -471.7 ;//index가 0일 수도 있으므로
        calcY = 5;
        calcZ = -819.4 + waitConsumerList.indexOf(consumer) + 1;
    }

    public static void addTable(BlockPos pos) {
        tableList.add(pos);
    }

    public static void removeTable(BlockPos pos) {
        tableList.remove(pos);
    }

    public void findTable(World worldObj) {
        WorldAPI.blockTick(worldObj, pos1.getX(), pos2.getX(), pos1.getY(), pos2.getY(), pos1.getZ(), pos2.getZ(),
                new BlockXYZ() {
                    @Override
                    public void run(Type type) {
                        Block block = worldObj.getBlockState(getPos()).getBlock();
                        Block block2 = worldObj.getBlockState(getPos().add(0, 1, 0)).getBlock();
                        if (block instanceof BlockFence && block2 instanceof BlockBasePressurePlate) {
                            addTable(getPos().add(1, 0, 0));
                        }
                    }
                });
    }

    public static BlockPos findEmptyTable() {
        for (BlockPos table : tableList) {
            if (useTableList.containsKey(table) && useTableList.get(table)) {
                continue;
            }
            useTableList.put(table, true);
            return table;
        }
        return null;
    }

    public static void spawnDust(World worldObj){
        for(BlockPos pos : TyconHelper.dustList){//문열고 들어갔을 때 먼지가 많으면 가게에 먼지가 생김
            worldObj.spawnParticle(EnumParticleTypes.BLOCK_DUST, pos.getX(), pos.getY(), pos.getZ(),0,0,0.01);
            worldObj.spawnParticle(EnumParticleTypes.BLOCK_DUST, pos.getX(), pos.getY(), pos.getZ(),0,0,1);
            worldObj.spawnParticle(EnumParticleTypes.BLOCK_DUST, pos.getX(), pos.getY(), pos.getZ(),0,0,0);
        }
    }
    public static int waitConsumerSize(){
        return waitConsumerList.size();
    }

    public static int waitConsumerIndexOf(EntityConsumer con) {
        return waitConsumerList.indexOf(con);
    }

    public static void addWaitConsumer(EntityConsumer consumer) {
        waitConsumerList.add(consumer);
    }

    public static void subWaitConsumer(EntityConsumer consumer) {
        waitConsumerList.remove(consumer);
        Iterator<EntityConsumer> tor = waitConsumerList.iterator();
        while (tor.hasNext()) {
            EntityConsumer cons = tor.next();
            cons.moveBreadSell(cons);
        }
    }

    public static int calcBread(EntityConsumer con) {
        int money = 0;
        for (BreadData bread : con.breadList) {
            System.out.println("가져온 아이템" + bread.getEntityItem().getEntityItem().getDisplayName());
            money += bread.money;
        }
        TyconHelper.playermoney += money;
        return money;
    }

    public static TileEntity getTileEntity(double x, double y, double z) {
        return WorldAPI.getWorld().getTileEntity(new BlockPos(x, y, z));
    }

    public static TileEntity getTileEntity(BlockPos ois) {
        return WorldAPI.getWorld().getTileEntity(ois);
    }

    public static int calcBreadAmount(EntityConsumer con) {
        int money = 0;
        for (BreadData bread : con.breadList) {
            money += bread.getAmount();
        }
        return money;
    }

}
