package map.escaperoom;

import gooditem.GoodItem;
import map.escaperoom.dungeon.EntityRespawnZombie;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import oneline.api.RenderAPI;

public class EscapeClientProxy extends EscapeServerProxy {
    @Override
    public void init() {
        RenderAPI.registerRender(EntityRoomBlock.class);
        RenderAPI.registerRender(EntityRoomBlockButton.class);
        RenderAPI.registerRender(EntityRoomBlockArrow.class);
        RenderAPI.registerRender(EntityRoomWindEntity.EntityPuzzleWindBlock.class);
        RenderAPI.registerRender(EntityRoomWindEntity.class);
        RenderAPI.registerRender(EntityRoomDoor.class);
        RenderAPI.registerRender(EntityRespawnZombie.class);
        RenderAPI.registerRender(EntityRoomMonster.class);
        RenderAPI.registerRender(EntityRoomPathCreeper.class);
        RenderAPI.registerRender(EntityRoomFallingBlock.class);
        RenderAPI.registerRender(EntityRoomBlockJumpMap.class);

    }

    @Override
    public void preInit() {
    }
}
