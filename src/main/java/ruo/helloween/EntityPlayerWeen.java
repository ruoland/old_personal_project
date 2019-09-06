package ruo.helloween;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import olib.api.WorldAPI;
import olib.effect.TickTask;
import olib.effect.TickRegister;
import olib.map.EntityDefaultNPC;
import olib.map.TypeModel;

public class EntityPlayerWeen extends EntityDefaultNPC {
    public static boolean thisKill;
    private double x, y, z,yaw;
    private int tickc;
    private static boolean isNether;
    private boolean isChange;
    public static boolean noDeath;

    @Override
    protected void collideWithEntity(Entity entityIn) {
        //super.collideWithEntity(entityIn);
    }

    public EntityPlayerWeen(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.PUMPKIN);
        this.setElytra(true);
        TickRegister.register(new TickTask(1300, false) {
            @Override
            public boolean stopCondition() {
                return isDead() || noDeath;
            }

            @Override
            public void run(Type type) {
                System.out.println("1300이 지나서 죽음");
                setDead();
            }
        });
    }

    public static void change() {
        isNether = true;
        Minecraft.getMinecraft().thePlayer.timeInPortal = 1F;
    }

    public EntityDefaultNPC setTarget(double taX, double taY, double taZ) {
        x = taX;
        y = taY;
        z = taZ;
        tickc = 0;
        return this;
    }

    @Override
    public void onLivingUpdate() {
        if (thisKill) {
            this.setDead();
        }
        if (!isChange && isNether && getModel() != TypeModel.NPC) {
            if (worldObj.rand.nextBoolean() || (worldObj.rand.nextBoolean() && worldObj.rand.nextBoolean())) {
                setModel(TypeModel.NPC);
                this.setSize(0.6F, 1.95F);
            }
            isChange = true;
        }
        this.getLookHelper().setLookPosition(x, y, z, getHorizontalFaceSpeed(), getVerticalFaceSpeed());
        super.onLivingUpdate();
        tickc++;
        if (tickc == 15) {
            yaw = rotationYawHead;
            this.setRotation((float) yaw, rotationPitch);
            setTarget(WorldAPI.rand(15) + posX + getLookVec().xCoord * 8.5, WorldAPI.y() + WorldAPI.getPlayer().eyeHeight + WorldAPI.minRand2(4, 15),
                    WorldAPI.rand(15) + posZ + getLookVec().zCoord * 8.5);
            tickc = 18;
        }
        if (tickc >= 20) {
            if (x != 0 && y != 0 && z != 0)
                this.setVelocity((x - posX) / 7, (y - posY) / 7, (z - posZ) / 7);
        } else
            this.setVelocity(0, 0, 0);
        this.setRotation((float) yaw, rotationPitch);
        if (getDistance(x, y, z) < 0.3) {
            //setPosition(spx,spy,spz);
            Vec3d v = WorldAPI.getPlayer().getLookVec();
            setTarget(WorldAPI.x() + v.xCoord * 10.5, WorldAPI.y() + WorldAPI.getPlayer().eyeHeight + 2,
                    WorldAPI.z() + v.zCoord * 10.5);
        }
    }
}
