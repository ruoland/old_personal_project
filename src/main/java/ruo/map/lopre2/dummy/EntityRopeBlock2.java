package ruo.map.lopre2.dummy;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import ruo.cmplus.cm.CommandChat;
import ruo.map.lopre2.EntityPreBlock;
import ruo.minigame.api.WorldAPI;

public class EntityRopeBlock2 extends EntityPreBlock {
    private static final DataParameter<Integer> COUNT = EntityDataManager.createKey(EntityRopeBlock.class, DataSerializers.VARINT);

    public EntityRopeBlock2(World world) {
        super(world);
        this.setBlockMode(Blocks.STONE);
        this.setCollision(true);
        isFly = true;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(COUNT, 0);
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        setSpawnXYZ(posX, posY, posZ);
        this.setTeleport(true);
        return super.onInitialSpawn(difficulty, livingdata);
    }

    public void addCount(){
        dataManager.set(COUNT, dataManager.get(COUNT)+1);
    }
    public void resetCount(){
        dataManager.set(COUNT, 0);
    }
    public int getCount(){
        return dataManager.get(COUNT);
    }
    public static double nanum = 140, nanum2 = 0.7, nanum3 = 1.5;//nanum2는 THIRD와 FIRST
    double firstVelX = 0, firstVelY = 0, firstVelZ = 0;
    double secondVelX = 0, secondVelY = 0, secondVelZ = 0;
    double thirdVelX = 0, thirdVelY = 0, thirdVelZ = 0;
    private String prevChat = "";
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.setDead();
        System.out.println("rope2블럭2 죽임");
        if (WorldAPI.getPlayerSP() != null) {
            EntityPlayerSP def = WorldAPI.getPlayerSP();
            String lastChat = CommandChat.getLastChat();
            if(lastChat != null && !prevChat.equalsIgnoreCase(lastChat) && lastChat.split(",").length == 3) {
                prevChat = lastChat;
                String[] split = lastChat.split(",");
                nanum = Double.valueOf(split[0]);
                nanum2 = Double.valueOf(split[1]);
                nanum3 = Double.valueOf(split[2]);
            }
            if(lookX == 0 && lookZ == 0) {
                lookReset();
            }
            reset();
            System.out.println(isServerWorld()+" ----------------  "+getCount());
            if (getCount() == 0 && def.getDistance(first()[0], first()[1], first()[2]) > 0.1) {
                def.addVelocity(firstVelX, firstVelY, firstVelZ);
                if (def.getDistance(first()[0], first()[1], first()[2]) <= 5) {
                    addCount();
                }
                System.out.println(getCount()+"TARGET AA" + first()[0] + " - " +  first()[1] + " - " +  first()[2]);
                System.out.println(getCount()+"AA " + firstVelX + " - " + (firstVelY) + " - " + (firstVelZ));

            }
            if (getCount() == 1 && def.getDistance(third()[0], second()[1], third()[2]) > 0.1) {
                def.addVelocity(thirdVelX*nanum2, secondVelY*nanum3, thirdVelZ*nanum2);
                if (def.getDistance(second()[0], second()[1], second()[2]) <= 5) {
                    addCount();
                }
                System.out.println(getCount()+"TARGET BB " + second()[0] + " - " +  second()[1] + " - " +  second()[2]);
                System.out.println(getCount()+"BB " + (secondVelX) + " - " + (secondVelY) + " - " + (secondVelZ));
            }
            if (getCount() == 2 && def.getDistance(third()[0], third()[1], third()[2]) > 0.1) {
                def.addVelocity(thirdVelX, thirdVelY, thirdVelZ);
                if (def.getDistance(third()[0], third()[1], third()[2]) <= 5) {
                    addCount();
                }
                System.out.println(getCount()+"TARGET CC " + third()[0] + " - " +  third()[1] + " - " +  third()[2]);
                System.out.println(getCount()+"CC " + (thirdVelX) + " - " + (thirdVelY) + " - " + (thirdVelZ));

            }
            if (getCount() == 3 && def.getDistance(first()[0], second()[1], first()[2]) > 0.1) {
                def.addVelocity(firstVelX*nanum2, secondVelY*nanum3, firstVelZ*nanum2);
                if (def.getDistance(second()[0], second()[1], second()[2]) <= 5) {
                    resetCount();
                    lookReset();
                }
                System.out.println(getCount()+"TARGET DD " + second()[0] + " - " +  second()[1] + " - " +  second()[2]);
                System.out.println(getCount()+"DD " + (secondVelX) + " - " + (secondVelY) + " - " + (secondVelZ));
            }
        }
    }

    private double lookX, lookZ;

    public int[] first() {
        if(lookX == 0 && lookZ == 0) {
            lookReset();
        }
        return WorldAPI.floor_double(posX - lookX, posY, posZ - lookZ);
    }

    public int[] second() {
        return WorldAPI.floor_double(posX, posY - 5, posZ);
    }

    public int[] third() {
        return WorldAPI.floor_double(posX + lookX, posY, posZ + lookZ);
    }
    public void lookReset(){
        lookX = WorldAPI.getPlayerSP().getLookVec().xCoord * 10.5;
        lookZ = WorldAPI.getPlayerSP().getLookVec().zCoord * 10.5;
    }
    public void reset(){
        EntityPlayerSP def = WorldAPI.getPlayerSP();
        firstVelX = (first()[0] - def.posX) / nanum;
        firstVelY = (first()[1] - def.posY) / nanum;
        firstVelZ = (first()[2] - def.posZ) / nanum;
        secondVelX = (second()[0] - def.posX) / nanum;
        secondVelY = (second()[1] - def.posY) / (nanum - 2 );
        secondVelZ = (second()[2] - def.posZ) / nanum;
        thirdVelX = (third()[0] - def.posX) / nanum;
        thirdVelY = (third()[1] - def.posY) / nanum;
        thirdVelZ = (third()[2] - def.posZ) / nanum;
    }

    double tx, ty = 0.03, tz;

    public void targetSetting() {
        EntityPlayer player = null;
        tx = player.posX + player.getLookVec().xCoord * 15;
        tz = player.posZ + player.getLookVec().zCoord * 15;

    }


}
