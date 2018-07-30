package ruo.asdfrpg;

import atomicstryker.dynamiclights.client.DynamicLights;
import atomicstryker.dynamiclights.client.IDynamicLightSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.SpawnDirection;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.map.EntityDefaultNPC;

import java.util.UUID;

public class EntityLight extends EntityDefaultNPC {
    private UUID uuid;
    private EntityPlayer player;
    public EntityLight(World worldIn) {
        super(worldIn);
        isFly = true;
        this.setSize(1,1);
        setScale(1,1,1);
    }

    public void setFollower(EntityPlayer player, int skillLevel){
        this.uuid = player.getUniqueID();
        this.player = player;
        this.setDeathTimer(100 + (50 * skillLevel));
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(player != null) {
            double posX = EntityAPI.getX(player, SpawnDirection.RIGHT, 1.5, true);
            double posZ = EntityAPI.getZ(player, SpawnDirection.RIGHT, 1.5, true);
            this.setPosition(posX, player.posY + 4, posZ);
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setString("uuid", uuid.toString());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        uuid = UUID.fromString(compound.getString("uuid"));
        player = WorldAPI.getPlayerByUUID(uuid.toString());
    }

}
