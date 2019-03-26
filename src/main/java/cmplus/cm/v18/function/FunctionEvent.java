package cmplus.cm.v18.function;

import oneline.api.WorldAPI;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.ScreenshotEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;
import net.minecraftforge.event.brewing.PotionBrewEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.minecart.MinecartCollisionEvent;
import net.minecraftforge.event.entity.minecart.MinecartInteractEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

public class FunctionEvent {

    @SubscribeEvent
    public void event(PlayerInteractEvent e) {
        if (Function.hasFunction("우클릭") || Function.hasFunction("좌클릭")) {
            ItemStack is = e.getItemStack();
            net.minecraft.block.Block blockbbb = WorldAPI.getBlock(e.getPos());
            String item;
            if (is == null)
                item = "없음";
            else
                item = is.getDisplayName();
            String blockX = e.getPos() == null ? "없음" : "" + e.getPos().getX();
            String blockY = e.getPos() == null ? "없음" : "" + e.getPos().getY();
            String blockZ = e.getPos() == null ? "없음" : "" + e.getPos().getZ();
            String block = blockbbb == null ? "없음" : blockbbb.getLocalizedName();

            if (e.getEntityPlayer() != null
                    && (e instanceof RightClickBlock || e instanceof RightClickEmpty || e instanceof RightClickItem)) {
                Function function = Function.addFunction("이벤트", "우클릭", "@이벤트아이템", item, "@블럭X", blockX, "@블럭Y", blockY,
                        "@블럭Z", blockZ, "@블럭", block);
                if (!e.getWorld().isRemote) {
                    function.runScript();
                    if (e.isCancelable())
                        e.setCanceled(function.isCanceled);
                    else if (WorldAPI.getPlayer() != null && function.isCanceled)
                        WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");

                }
            }
            if (e.getEntityPlayer() != null && e instanceof LeftClickBlock || e instanceof LeftClickEmpty) {
                if (!e.getWorld().isRemote) {
                    Function function = Function.addFunction("이벤트", "좌클릭", "@이벤트아이템", item, "@블럭X", blockX, "@블럭Y", blockY,
                            "@블럭Z", blockZ, "@블럭", block);
                    function.runScript();
                    if (e.isCancelable())
                        e.setCanceled(function.isCanceled);
                    else if (WorldAPI.getPlayer() != null && function.isCanceled)
                        WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");

                }
            }
        }
    }

    @SubscribeEvent
    public void event(PlayerBrewedPotionEvent e) {
        if (Function.hasFunction("포션마심")) {
            if (!e.getEntityPlayer().worldObj.isRemote) {
                Function function = Function.addFunction("이벤트", "포션마심", "@이벤트아이템", "" + e.getStack().getDisplayName());
                function.runScript();
                if (e.isCancelable())
                    e.setCanceled(function.isCanceled);
                else if (WorldAPI.getPlayer() != null && function.isCanceled)
                    WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");
            }

        }
    }

    @SubscribeEvent
    public void event(PlayerChangedDimensionEvent e) {
        if (Function.hasFunction("차원이동")) {
            if (!e.player.worldObj.isRemote) {
                Function function = Function.addFunction("이벤트", "차원이동", "@차원", "" + e.toDim);
                function.runScript();
                if (e.isCancelable())
                    e.setCanceled(function.isCanceled);
                else if (WorldAPI.getPlayer() != null && function.isCanceled)
                    WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");
            }

        }
    }

    @SubscribeEvent
    public void event(PlayerWakeUpEvent e) {
        if (Function.hasFunction("일어남")) {
            if (!e.getEntityPlayer().worldObj.isRemote) {
                Function function = Function.addFunction("이벤트", "일어남");
                function.runScript();
                if (e.isCancelable())
                    e.setCanceled(function.isCanceled);
                else if (WorldAPI.getPlayer() != null && function.isCanceled)
                    WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");
            }

        }
    }

    @SubscribeEvent
    public void event(PlayerDestroyItemEvent e) {
        if (Function.hasFunction("아이템파괴")) {
            if (!e.getEntityPlayer().worldObj.isRemote) {
                Function function = Function.addFunction("이벤트", "아이템파괴");
                function.runScript();
                if (e.isCancelable())
                    e.setCanceled(function.isCanceled);
                else if (WorldAPI.getPlayer() != null && function.isCanceled)
                    WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");
            }

        }
    }

    @SubscribeEvent
    public void event(PlayerPickupXpEvent e) {
        if (Function.hasFunction("경험치획득")) {
            if (!e.getEntityPlayer().worldObj.isRemote) {
                Function function = Function.addFunction("이벤트", "경험치획득", "@경험치", "" + e.getOrb().getXpValue());
                function.runScript();
                if (e.isCancelable())
                    e.setCanceled(function.isCanceled);
                else if (WorldAPI.getPlayer() != null && function.isCanceled)
                    WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");
            }

        }
    }

    @SubscribeEvent
    public void event(PlayerRespawnEvent e) {
        if (Function.hasFunction("리스폰")) {
            if (!e.player.worldObj.isRemote) {
                Function function = Function.addFunction("이벤트", "리스폰");
                function.runScript();
                if (e.isCancelable())
                    e.setCanceled(function.isCanceled);
                else if (WorldAPI.getPlayer() != null && function.isCanceled)
                    WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");
            }

        }
    }

    @SubscribeEvent
    public void event(ServerChatEvent e) {
        if (Function.hasFunction("채팅")) {
            if (!e.getPlayer().worldObj.isRemote) {
                Function function = Function.addFunction("이벤트", "채팅", "@채팅", e.getMessage());
                function.runScript();
                if (e.isCancelable())
                    e.setCanceled(function.isCanceled);
                else if (WorldAPI.getPlayer() != null && function.isCanceled)
                    WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");
            }

        }
    }

    @SubscribeEvent
    public void event(MouseEvent e) {
        if (Function.hasFunction("마우스")) {
            Function function = Function.addFunction("이벤트", "마우스", "@버튼", "" + e.getButton(), "@마우스X", "" + e.getX(),
                    "@마우스Y", "" + e.getY());
            function.runScript();
            if (e.isCancelable())
                e.setCanceled(function.isCanceled);
            else if (WorldAPI.getPlayer() != null && function.isCanceled)
                WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");
        }
    }

    @SubscribeEvent
    public void event(PlaySoundEvent e) {
        if (Function.hasFunction("소리재생")) {
            Function function = Function.addFunction("이벤트", "소리재생", "@이름", "" + e.getName());
            function.runScript();
            if (e.isCancelable())
                e.setCanceled(function.isCanceled);
            else if (WorldAPI.getPlayer() != null && function.isCanceled)
                WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");
        }
    }


    @SubscribeEvent
    public void event(net.minecraftforge.event.CommandEvent e) {
        if (Function.hasFunction("명령어")) {
            String[] str = new String[e.getParameters().length * 2 + 2];// +2는 @이름과 커맨드 이름을 담기 위해서
            str[0] = "@이름";
            str[1] = e.getCommand().getCommandName();
            for (int i = 0; i < e.getParameters().length; i += 3) {
                str[i + 2] = "@인자" + i;
                str[i + 3] = e.getParameters()[i];
            }
            Function function = Function.addFunction("이벤트", "명령어", str);
            function.runScript();
            if (e.isCancelable())
                e.setCanceled(function.isCanceled);
            else if (WorldAPI.getPlayer() != null && function.isCanceled)
                WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");
        }
    }

    @SubscribeEvent
    public void event(PotionBrewEvent e) {
        if (Function.hasFunction("포션마심")) {
            Function function = Function.addFunction("이벤트", "포션마심", "@이벤트아이템", e.getItem(0).getDisplayName());
            function.runScript();
            if (e.isCancelable())
                e.setCanceled(function.isCanceled);
            else if (WorldAPI.getPlayer() != null && function.isCanceled)
                WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");
        }
    }

    @SubscribeEvent
    public void event(PotionBrewEvent.Pre e) {
        if (Function.hasFunction("포션마시기전")) {
            Function function = Function.addFunction("이벤트", "포션마시기전", "@이벤트아이템", e.getItem(0).getDisplayName());
            function.runScript();
            if (e.isCancelable())
                e.setCanceled(function.isCanceled);
            else if (WorldAPI.getPlayer() != null && function.isCanceled)
                WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");
        }
    }

    @SubscribeEvent
    public void event(EntityJoinWorldEvent e) {
        if (Function.hasFunction("엔티티스폰")) {
            if (!e.getWorld().isRemote) {
                Function function = Function.addFunction("이벤트", "엔티티스폰", "@엔티티", e.getEntity().getCustomNameTag());
                function.runScript();
                if (e.isCancelable())
                    e.setCanceled(function.isCanceled);
                else if (WorldAPI.getPlayer() != null && function.isCanceled)
                    WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");

            }
        }
    }

    @SubscribeEvent
    public void event(EntityMountEvent e) {
        if (Function.hasFunction("라이딩")) {
            if (!e.getEntity().worldObj.isRemote) {
                Function function = Function.addFunction("이벤트", "라이딩", "@엔티티", e.getEntityBeingMounted().getCustomNameTag(),
                        "@엔티티2", e.getEntityMounting().getCustomNameTag());
                function.runScript();
                if (e.isCancelable())
                    e.setCanceled(function.isCanceled);
                else if (WorldAPI.getPlayer() != null && function.isCanceled)
                    WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");

            }
        }
    }

    @SubscribeEvent
    public void event(EntityStruckByLightningEvent e) {
        if (Function.hasFunction("번개맞음")) {
            if (!e.getEntity().worldObj.isRemote) {
                Function function = Function.addFunction("이벤트", "번개맞음", "@엔티티", e.getEntity().getCustomNameTag());
                function.runScript();
                if (e.isCancelable())
                    e.setCanceled(function.isCanceled);
                else if (WorldAPI.getPlayer() != null && function.isCanceled)
                    WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");

            }
        }
    }

    @SubscribeEvent
    public void event(ItemTossEvent e) {
        if (Function.hasFunction("아이템버림")) {
            Function function = Function.addFunction("이벤트", "아이템버림", "@아이템",
                    e.getEntityItem().getEntityItem().getDisplayName());
            function.runScript();
            if (e.isCancelable())
                e.setCanceled(function.isCanceled);
            else if (WorldAPI.getPlayer() != null && function.isCanceled)
                WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");
        }
    }

    @SubscribeEvent
    public void event(EnderTeleportEvent e) {
        if (Function.hasFunction("엔더텔포")) {
            Function function = Function.addFunction("이벤트", "엔더텔포", "@엔티티", e.getEntityLiving().getCustomNameTag());
            function.runScript();
            if (e.isCancelable())
                e.setCanceled(function.isCanceled);
            else if (WorldAPI.getPlayer() != null && function.isCanceled)
                WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");
        }
    }

    @SubscribeEvent
    public void event(LivingAttackEvent e) {
        if (Function.hasFunction("공격")) {
            Function function = Function.addFunction("이벤트", "공격", "@공격", e.getEntityLiving().getCustomNameTag(), "@피해",
                    e.getSource().getEntity() == null ? "없음" : e.getSource().getEntity().getCustomNameTag());
            function.runScript();
            if (e.isCancelable())
                e.setCanceled(function.isCanceled);
            else if (WorldAPI.getPlayer() != null && function.isCanceled)
                WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");
        }
    }

    @SubscribeEvent
    public void event(LivingDeathEvent e) {
        if (Function.hasFunction("죽음")) {
            if (e.getEntityLiving().isServerWorld()) {
                Function function = Function.addFunction("이벤트", "죽음", "@죽음", e.getEntityLiving().getCustomNameTag());
                function.runScript();
                if (e.isCancelable())
                    e.setCanceled(function.isCanceled);
                else if (WorldAPI.getPlayer() != null && function.isCanceled)
                    WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");

            }
        }
    }

    @SubscribeEvent
    public void event(LivingFallEvent e) {
        if (Function.hasFunction("추락")) {
            Function function = Function.addFunction("이벤트", "추락", "@엔티티", e.getEntityLiving().getCustomNameTag());
            function.runScript();
            if (e.isCancelable())
                e.setCanceled(function.isCanceled);
            else if (WorldAPI.getPlayer() != null && function.isCanceled)
                WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");
        }
    }

    @SubscribeEvent
    public void event(LivingHealEvent e) {
        if (Function.hasFunction("회복")) {
            Function function = Function.addFunction("이벤트", "회복", "@회복량", "" + e.getAmount());
            function.runScript();
            if (e.isCancelable())
                e.setCanceled(function.isCanceled);
            else if (WorldAPI.getPlayer() != null && function.isCanceled)
                WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");
        }
    }

    @SubscribeEvent
    public void event(MinecartCollisionEvent e) {
        if (Function.hasFunction("카트충돌")) {
            Function function = Function.addFunction("이벤트", "카트충돌", "@카트", e.getMinecart().getCustomNameTag(), "@충돌엔티티",
                    e.getCollider().getCustomNameTag());
            function.runScript();
            if (e.isCancelable())
                e.setCanceled(function.isCanceled);
            else if (WorldAPI.getPlayer() != null && function.isCanceled)
                WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");
        }
    }

    @SubscribeEvent
    public void event(MinecartInteractEvent e) {
        if (Function.hasFunction("카트클릭")) {
            Function function = Function.addFunction("이벤트", "카트클릭", "@카트", e.getMinecart().getCustomNameTag());
            function.runScript();
            if (e.isCancelable())
                e.setCanceled(function.isCanceled);
            else if (WorldAPI.getPlayer() != null && function.isCanceled)
                WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");
        }
    }

    @SubscribeEvent
    public void event(AchievementEvent e) {
        if (Function.hasFunction("업적")) {
            if (!e.getEntityPlayer().worldObj.isRemote) {
                Function function = Function.addFunction("이벤트", "업적", "@업적", e.getAchievement().statId);
                function.runScript();
                if (e.isCancelable())
                    e.setCanceled(function.isCanceled);
                else if (WorldAPI.getPlayer() != null && function.isCanceled)
                    WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");

            }
        }
    }

    @SubscribeEvent
    public void event(FillBucketEvent e) {
        if (Function.hasFunction("양동이채움")) {
            if (!e.getWorld().isRemote) {
                Function function = Function.addFunction("이벤트", "양동이채움");
                function.runScript();
                if (e.isCancelable())
                    e.setCanceled(function.isCanceled);
                else if (WorldAPI.getPlayer() != null && function.isCanceled)
                    WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");

            }
        }
    }

    @SubscribeEvent
    public void event(EntityItemPickupEvent e) {
        if (Function.hasFunction("아이템주움")) {
            if (!e.getEntityPlayer().worldObj.isRemote) {
                Function function = Function.addFunction("이벤트", "아이템주움", "@이벤트아이템", e.getItem().getCustomNameTag());
                function.runScript();
                if (e.isCancelable())
                    e.setCanceled(function.isCanceled);
                else if (WorldAPI.getPlayer() != null && function.isCanceled)
                    WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");

            }
        }
    }

    @SubscribeEvent
    public void event(UseHoeEvent e) {
        if (Function.hasFunction("낫")) {
            if (!e.getWorld().isRemote) {
                Function function = Function.addFunction("이벤트", "낫", "@이벤트아이템", e.getCurrent().getDisplayName());
                function.runScript();
                if (e.isCancelable())
                    e.setCanceled(function.isCanceled);
                else if (WorldAPI.getPlayer() != null && function.isCanceled)
                    WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");

            }
        }
    }

    @SubscribeEvent
    public void event(ItemCraftedEvent e) {
        if (Function.hasFunction("아이템조합")) {
            if (!e.player.worldObj.isRemote) {
                Function function = Function.addFunction("이벤트", "아이템조합", "@이벤트아이템", e.crafting.getDisplayName());
                function.runScript();
                if (e.isCancelable())
                    e.setCanceled(function.isCanceled);
                else if (WorldAPI.getPlayer() != null && function.isCanceled)
                    WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");

            }
        }
    }

    @SubscribeEvent
    public void event(ItemSmeltedEvent e) {
        if (Function.hasFunction("아이템태움")) {
            if (!e.player.worldObj.isRemote) {
                Function function = Function.addFunction("이벤트", "아이템태움", "@이벤트아이템", e.smelting.getDisplayName());
                function.runScript();
                if (e.isCancelable())
                    e.setCanceled(function.isCanceled);
                else if (WorldAPI.getPlayer() != null && function.isCanceled)
                    WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");
            }
        }
    }

    /**
     * 실제 실행시 설정 메서드가 제대로 작동할지 확인해야함 아니면 그냥 커맨드 플러스 명령어로 추가해도 됨
     */
    @SubscribeEvent
    public void event(ScreenshotEvent e) {
        if (Function.hasFunction("스샷")) {
            Function function = Function.addFunction("이벤트", "스샷", "@파일", e.getScreenshotFile().getName());
            function.runScript();
            if (e.isCancelable())
                e.setCanceled(function.isCanceled);
            else if (WorldAPI.getPlayer() != null && function.isCanceled)
                WorldAPI.addMessage(function.getName() + " 이벤트는 캔슬할 수 없습니다");
        }
    }

    public String subString(String com) {
        return com.substring(com.indexOf("("), com.indexOf(")"));
    }

    public String replaceEntity(LivingEvent e, String com) {
        return com.replace("@엔티티", e.getEntity().getCustomNameTag()).replace("@엔티티X", "" + e.getEntity().posX)
                .replace("@엔티티Y", "" + e.getEntity().posY).replace("@엔티티Z", "" + e.getEntity().posZ)
                .replace("@차원", "" + e.getEntity().dimension).replace("@피치", "" + e.getEntity().rotationPitch)
                .replace("@요", "" + e.getEntity().rotationPitch).replace("@밝기", "" + e.getEntity().getBrightness(0))
                .replace("@이름", "" + e.getEntity().getCustomNameTag());
    }

    public String replaceEntity(EntityEvent e, String com) {
        return com.replace("@엔티티", e.getEntity().getCustomNameTag()).replace("@엔티티X", "" + e.getEntity().posX)
                .replace("@엔티티Y", "" + e.getEntity().posY).replace("@엔티티Z", "" + e.getEntity().posZ)
                .replace("@차원", "" + e.getEntity().dimension).replace("@피치", "" + e.getEntity().rotationPitch)
                .replace("@요", "" + e.getEntity().rotationPitch).replace("@밝기", "" + e.getEntity().getBrightness(0))
                .replace("@이름", "" + e.getEntity().getCustomNameTag());
    }

    public static void add(String event, String command) {
        Function func = Function.addFunction("이벤트", event);
        func.write(command);
    }

    public static void edit(String event) {
        Function func = Function.addFunction("이벤트", event);
        func.openFile();
    }
}
