package minigameLib.minigame.minerun;

import cmplus.deb.DebAPI;
import map.lopre2.EntityPreBlock;
import map.lopre2.jump1.EntityLavaBlock;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import oneline.map.EntityDefaultNPC;
import oneline.map.TypeModel;
import org.lwjgl.input.Keyboard;

public class EntityMineRunner extends EntityDefaultNPC {
    private static final DataParameter<Boolean> isCollision = EntityDataManager.createKey(EntityMineRunner.class, DataSerializers.BOOLEAN);
    public EntityMineRunner(World worldIn) {
        super(worldIn);
        this.setModel(TypeModel.NPC);
    }

    @Override
    public boolean canRenderOnFire() {
        return false;
    }

    @Override
    public void jump() {
        super.jump();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(isCollision, false);
    }

    @Override
    protected void doBlockCollisions() {
        super.doBlockCollisions();
        dataManager.set(isCollision, true);
    }

    public void updatePos(){
        dataManager.set(isCollision, false);
    }
}
