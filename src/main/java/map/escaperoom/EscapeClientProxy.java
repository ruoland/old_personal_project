package map.escaperoom;

import map.escaperoom.nouse.EntityRoomWindEntity;
import map.escaperoom.nouse.dungeon.EntityRespawnZombie;
import map.escaperoom.nouse.EntityRoomBlockJumpMap;
import map.escaperoom.nouse.EntityRoomDoor;
import map.escaperoom.nouse.EntityRoomPathCreeper;
import olib.api.RenderAPI;

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
        RenderAPI.registerRender(EntityRoomRedBlue.class);

    }

    @Override
    public void preInit() {
    }
}
