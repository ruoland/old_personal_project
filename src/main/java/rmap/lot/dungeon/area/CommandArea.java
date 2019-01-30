package rmap.lot.dungeon.area;

import cmplus.util.CommandPlusBase;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import minigameLib.api.WorldAPI;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rmap.lot.EntityDoorBlock;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

public class CommandArea extends CommandPlusBase {

    //구역을 지정함
    //그 구역 안에 몬스터가 한마리도 없는 경우 문이 열림
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        
        String name = args[1];
        File jsonFile = new File("./" + name + ".json");
        Gson gson = new Gson();
        if (args[0].equalsIgnoreCase("create")) {
            try {
                JsonObject object = new JsonObject();
                object.addProperty("name", name);
                object.addProperty("pos1", gson.toJson(DungeonArea.selectPos1));
                object.addProperty("pos2", gson.toJson(DungeonArea.selectPos2));
                object.addProperty("entityList", findEntityName(sender.getEntityWorld()));
                //문은 알아서 찾아서 열게 할 생각임
                Files.write(gson.toJson(object), jsonFile, Charset.forName("UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (args[0].equalsIgnoreCase("run")) {
            try {
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(Files.newReader(jsonFile, Charset.forName("UTF-8")));
                BlockPos pos1 = gson.fromJson(element.getAsJsonObject().get("pos1").getAsString(), BlockPos.class);
                BlockPos pos2 = gson.fromJson(element.getAsJsonObject().get("pos2").getAsString(), BlockPos.class);
                List<EntityDoorBlock> baseList = WorldAPI.getWorld().getEntitiesWithinAABB(EntityDoorBlock.class, new AxisAlignedBB(pos1,pos2));

                System.out.println(baseList.toString());
                DungeonArea dungeonArea = new DungeonArea(sender.getEntityWorld(), pos1,pos2);
                dungeonArea.addEntity(element.getAsJsonObject().get("entityList").getAsString().split(","));
                dungeonArea.openDoor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public String findEntityName(World world) {
        AxisAlignedBB aabb = new AxisAlignedBB(DungeonArea.selectPos1, DungeonArea.selectPos2);
        List<EntityLivingBase> entityList = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
        StringBuffer entityName = new StringBuffer();

        for (EntityLivingBase livingBase : entityList) {
            entityName.append(EntityList.getEntityString(livingBase) + ",");
        }
        return entityName.toString();
    }
}
