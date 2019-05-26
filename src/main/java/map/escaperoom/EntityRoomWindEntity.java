package map.escaperoom;

import map.escaperoom.nouse.base.EntityRoomWindBase;
import olib.map.EntityDefaultNPC;
import olib.map.TypeModel;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class EntityRoomWindEntity extends EntityRoomWindBase {

    public EntityRoomWindEntity(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.PUMPKIN);
        this.typeModel = TypeModel.SHAPE_BLOCK;
        this.setSize(width, height);
        this.setTra(0, 2, 0);

    }

    @Override
    public void onUpdate() {
        super.onUpdate();

    }

    @Override
    public void onEffect() {
        float y = (1F / 40F) + ((float) getDelay() / 200F);
        System.out.println(((float) getDelay() / 200F));
        List<Entity> entityList = worldObj.getEntitiesWithinAABB(Entity.class, getCollisionBoundingBox().addCoord(0, 10, 0));
        for (Entity entity : entityList) {
            if (entity != this)
                entity.addVelocity(0, y, 0);
            if (entity instanceof EntityPuzzleWindBlock) {
                EntityPuzzleWindBlock puzzleWindBlock = (EntityPuzzleWindBlock) entity;
                puzzleWindBlock.setIsWind(true, 9);
                Vec3d targetPosition = new Vec3d(posX, puzzleWindBlock.posY + 9, posZ);
                Vec3d targetVec = targetPosition.subtract(puzzleWindBlock.getPositionVector()).normalize();
                puzzleWindBlock.setVelocity(targetVec);
            }

        }
    }

    public static class EntityPuzzleWindBlock extends EntityDefaultNPC {
        private static final DataParameter<Boolean> IS_WIND = EntityDataManager.createKey(EntityPuzzleWindBlock.class, DataSerializers.BOOLEAN);
        private static final DataParameter<Float> TARGET_Y = EntityDataManager.createKey(EntityPuzzleWindBlock.class, DataSerializers.FLOAT);

        public EntityPuzzleWindBlock(World worldIn) {
            super(worldIn);
            this.setBlockMode(Blocks.PUMPKIN);
            this.typeModel = TypeModel.BLOCK;
            this.setSize(width, height);
            this.setCollision(true);
        }

        @Override
        protected void entityInit() {
            super.entityInit();
            dataManager.register(IS_WIND, false);
            dataManager.register(TARGET_Y, 0F);
        }


        public boolean isWind() {
            return dataManager.get(IS_WIND);
        }

        public void setIsWind(boolean isWind, double y) {
            dataManager.set(IS_WIND, isWind);
            dataManager.set(TARGET_Y, (float) y);
        }

        @Override
        public void onLivingUpdate() {
            super.onLivingUpdate();
            fallDistance = 0;
            if(getSpawnY() + getDataManager().get(TARGET_Y) >= posY){
                this.setVelocity(0,0,0);
            }
        }
    }
}
