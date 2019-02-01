package map.lot.dungeon.area;

import minigameLib.api.WorldAPI;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import map.lot.EntityDoorBlock;
import map.lot.LOTEvent;

import java.util.ArrayList;
import java.util.List;

public class DungeonArea {
    public static BlockPos selectPos1, selectPos2;
    private BlockPos pos1,pos2;
    private World world;
    private ArrayList<EntityLivingBase> baseList = new ArrayList<>();

    public DungeonArea(World world, BlockPos pos1, BlockPos pos2){
        this.world = world;
        this.pos1 = pos1;
        this.pos2 = pos2;
        LOTEvent.dungeonArea = this;
    }

    public BlockPos getPos1() {
        return pos1;
    }

    public BlockPos getPos2() {
        return pos2;
    }

    public void addEntity(String[] entityList){
        int spawnX = pos1.getX() + ((pos2.getX() - pos1.getX()) / 2);
        int spawnY = Math.min(pos1.getY(), pos2.getY())+1;
        int spawnZ = pos1.getZ() + ((pos2.getZ() - pos1.getZ()) / 2);
        for (String entityName : entityList) {
            EntityLivingBase entity = (EntityLivingBase) EntityList.createEntityByIDFromName(entityName, world);
            entity.setPosition(spawnX, spawnY, spawnZ);
            world.spawnEntityInWorld(entity);
            baseList.add(entity);
        }

    }

    public boolean checkDead(){
        for(EntityLivingBase base : baseList){
            if(!base.isDead)
                return false;
        }
        return true;
    }

    public void openDoor(){
        List<EntityDoorBlock> baseList = WorldAPI.getWorld().getEntitiesWithinAABB(EntityDoorBlock.class, new AxisAlignedBB(pos1,pos2));
        if(checkDead()){
           if(baseList.size() > 0){
               baseList.get(0).open();;
               baseList.get(1).open();;
               LOTEvent.dungeonArea = null;
           }
       }
       else{
            baseList.get(0).close();
            baseList.get(1).close();
        }

    }
}
