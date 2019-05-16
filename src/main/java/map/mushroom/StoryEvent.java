package map.mushroom;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import olib.api.EntityAPI;
import olib.effect.AbstractTick;
import olib.effect.TickRegister;

import java.util.List;

public class StoryEvent {
    private EntityPlayer player;
    public StoryEvent(EntityPlayer player){
        this.player = player;
    }
    public void start(){
        player.inventory.armorInventory[0] = new ItemStack(Items.DIAMOND_HELMET);
        player.inventory.armorInventory[1] = new ItemStack(Items.DIAMOND_CHESTPLATE);
        player.inventory.armorInventory[2] = new ItemStack(Items.DIAMOND_LEGGINGS);
        player.inventory.armorInventory[3] = new ItemStack(Items.DIAMOND_BOOTS);
        player.inventory.addItemStackToInventory(new ItemStack(Items.DIAMOND_SWORD));

        //흙 집에서 위더를 소환한다
        //위더랑 싸운다
    }

    //위더가 죽으면 네더의 별 소환
    @SubscribeEvent
    public void wither(LivingDeathEvent event){
        EntityLivingBase livingBase = event.getEntityLiving();
        if(livingBase instanceof EntityWither){
            livingBase.setDead();
            EntityItem item = new EntityItem(player.worldObj);
            item.setPosition(livingBase.posX, livingBase.posY, livingBase.posZ);
            item.setEntityItemStack(new ItemStack(Items.NETHER_STAR));
            player.worldObj.spawnEntityInWorld(item);
            deadWither();
        }
    }

    public void deadWither(){
        player.addChatComponentMessage(new TextComponentString("네더읩 ㅕㄹ을 얻얻ㅆ음니 신호기를 만들수아ㅣㅆ다"));
        player.worldObj.setBlockState(new BlockPos(0,0,0), Blocks.OAK_DOOR.getDefaultState());
        //위더를 죽여야 나가는 문이 설치됨
    }

    //계단을 다 올라가면 실행됨
    public void createSignal(){
        player.addChatComponentMessage(new TextComponentString("네더의 별을 작업대에 올리자"));
        double workX=0, workY=0, workZ=0;
        EntityAPI.spawnItem(player.worldObj, new ItemStack(Blocks.OBSIDIAN, 3), workX, workY, workZ).setInfinitePickupDelay();
        EntityAPI.spawnItem(player.worldObj, new ItemStack(Blocks.GLASS, 5), workX, workY, workZ).setInfinitePickupDelay();
        TickRegister.register(new AbstractTick(TickEvent.Type.SERVER, 1, true) {
            @Override
            public void run(TickEvent.Type type) {
                List<EntityItem> list = EntityAPI.getEntityItem(player.worldObj, workX, workY, workZ,1);
                for(EntityItem item : list){
                    if(item.getEntityItem().getUnlocalizedName().equalsIgnoreCase(Blocks.OBSIDIAN.getUnlocalizedName())){
                        createSignal();
                        absLoop = false;
                    }
                }
            }
        });
    }

    public void createSignal2(){

    }



}
