package map.lopre2.jump2;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.init.Blocks;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import map.lopre2.jump1.EntityMoveBlock;

public class EntityBigBlockMove extends EntityMoveBlock {
    public EntityBigBlockMove(World worldIn) {
        super(worldIn);
        this.setCollision(true);
        setBlockMode(Blocks.ICE);
        this.noClip = !noClip;
        isFly = true;
        this.setScale(3, 1, 3);
        this.setSize(3, 1);
        this.speed = 0.08;
        this.setBlockMove(true);
        setJumpName("빅 이동 블럭");
    }

    @Override
    public String getText() {
        return "거대한 이동 블럭입니다. 스패너를 들고 우클릭 하면 블럭이 보는 방향으로 블럭이 이동합니다.";
    }

    @Override
    public String getCustomNameTag() {
        return "BigBlockMove " +getFacing().getName();
    }


    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        setSpawnXYZ(posX, posY, posZ);
        this.setTeleport(true);
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    public EntityBigBlockMove spawn(double x, double y, double z) {
        EntityBigBlockMove lavaBlock = new EntityBigBlockMove(worldObj);
        lavaBlock.setSpawnXYZ(x, y, z);
        lavaBlock.setTeleport(false);
        lavaBlock.setPosition(lavaBlock.getSpawnX(), lavaBlock.getSpawnY(), lavaBlock.getSpawnZ());

        lavaBlock.setBlockMode(getCurrentStack());
        worldObj.spawnEntityInWorld(lavaBlock);
        this.copyModel(lavaBlock);
        return lavaBlock;
    }

    @Override
    public void setDead() {
        super.setDead();
        try{
            new NullPointerException();
        }catch (Exception e){
            e.printStackTrace();;
        }
    }
}
