package ruo.helloween.miniween;

import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.world.World;
import ruo.minigame.map.EntityDefaultNPC;
import ruo.minigame.map.RenderDefaultNPC;
import ruo.minigame.map.TypeModel;

public class EntityCreeperWeen extends EntityDefaultNPC {
    public EntityCreeperWeen(World worldIn) {
        super(worldIn);
        this.setModel(TypeModel.CREEPER);
    }
}
