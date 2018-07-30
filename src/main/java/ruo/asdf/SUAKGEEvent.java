package ruo.asdf;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.Sys;
import ruo.awild.EntityWildHorse;
import ruo.cmplus.util.Sky;
import ruo.minigame.MiniGame;
import ruo.minigame.api.BlockAPI;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class SUAKGEEvent {
    private static int creeperSize = 0, spiderSize = 0, skeletonSize = 0;

    //@SubscribeEvent
    public void event(TickEvent.WorldTickEvent e) {
        if (e.world.rand.nextInt(2) == 0) {
            if (e.world.getWorldTime() == 6000 && Sky.getSunbright() == 0) {
                Sky.sunBrightness(-1);
                return;
            }
            if (e.world.getWorldTime() == 6000) {
                WorldAPI.addMessage("햇빛이 들지 않는 날입니다.");
                Sky.sunBrightness(0);
            }
        }
    }
    @SubscribeEvent
    public void event(LivingSpawnEvent e) {
        if(e.getEntityLiving() instanceof EntityMob && e.getWorld().rand.nextInt(200) == 0 && WorldAPI.getPlayer() != null){
            NBTTagCompound compound = new NBTTagCompound();
            e.getEntityLiving().writeEntityToNBT(compound);
            if(compound.hasKey("id")) {
                Entity entity = EntityList.createEntityFromNBT(compound, e.getWorld());
                entity.setPosition(e.getX(), e.getY(), e.getZ());
                e.getWorld().spawnEntityInWorld(entity);
                System.out.println("몬스터 추가 소환됨" + e.getX() + " - " + e.getY() + " - " + e.getZ());
            }
        }
    }
    @SubscribeEvent
    public void event(EntityJoinWorldEvent e) {
        if (e.getEntity() instanceof EntityArrow) {
            EntityArrow arrow = (EntityArrow) e.getEntity();
            if (arrow.shootingEntity instanceof EntitySkelereeper) {
                EntityCreeper creeper = new EntityCreeper(e.getWorld());
                creeper.setPosition(e.getEntity().posX, e.getEntity().posY, e.getEntity().posZ);
                creeper.setVelocity(e.getEntity().motionX, e.getEntity().motionY, e.getEntity().motionZ);
                e.getEntity().setDead();
                e.getWorld().spawnEntityInWorld(creeper);
                creeper.ignite();
            }
        }
    }

    @SubscribeEvent
    public void event(ArrowLooseEvent e) {
        if (EnchantmentHelper.getEnchantmentLevel(SUAKGEMod.chageEnchant, e.getBow()) != 0) {
            e.setCharge(34);
        }
    }

    public BlockAPI breakTree(World world, BlockPos startPos) {
        BlockAPI api = new BlockAPI();
        for (int i = 0; i < 16; i++) {
            BlockPos startPosup = startPos.add(0, i, 0);
            if (world.getBlockState(startPosup).getBlock() == Blocks.LOG || world.getBlockState(startPosup).getBlock() == Blocks.LOG2) {
                api.addBlock(world, startPosup);
                breakTree2(api, world, startPosup, 1, 0, 0, false);
                breakTree2(api, world, startPosup, 0, 0, 1, false);
                breakTree2(api, world, startPosup, -1, 0, 0, false);
                breakTree2(api, world, startPosup, 0, 0, -1, false);
                breakTree2(api, world, startPosup, 1, 0, 1, false);
                breakTree2(api, world, startPosup, -1, 0, -1, false);
                breakTree2(api, world, startPosup, -1, 0, 1, false);
                breakTree2(api, world, startPosup, 1, 0, -1, false);
            } else
                break;
        }

        return api;
    }

    private void breakTree2(BlockAPI api, World world, BlockPos startPos, int x, int y, int z, boolean isSecond) {
        for (int i = 0; i < 8; i++) {
            BlockPos pos = startPos.add(x == 0 ? 0 : x + i, y == 0 ? 0 : y + i, z == 0 ? 0 : z + i);
            if (world.getBlockState(pos).getBlock() == Blocks.LOG || world.getBlockState(pos).getBlock() == Blocks.LOG2) {
                api.addBlock(world, pos);
                if (!isSecond) {
                    breakTree2(api, world, pos, 1, 0, 0, true);
                    breakTree2(api, world, pos, 0, 0, 1, true);
                    breakTree2(api, world, pos, -1, 0, 0, true);
                    breakTree2(api, world, pos, 0, 0, -1, true);
                    breakTree2(api, world, pos, 1, 0, 1, true);
                    breakTree2(api, world, pos, -1, 0, -1, true);
                    breakTree2(api, world, pos, -1, 0, 1, true);
                    breakTree2(api, world, pos, 1, 0, -1, true);
                }
            } else
                break;
        }
    }

    @SubscribeEvent
    public void event(BlockEvent.BreakEvent e) {
        int level = EnchantmentHelper.getEnchantmentLevel(SUAKGEMod.rangeBreakEnchant, e.getPlayer().getHeldItemMainhand());
        World world = e.getWorld();
        EntityPlayer player = e.getPlayer();
        Block breakBlock = e.getState().getBlock();
        BlockPos breakPos = e.getPos();
        Material breakMaterial = breakBlock.getMaterial(e.getState());
        if (player.getHeldItemMainhand() != null && level != 0) {
            Item tool = player.getHeldItemMainhand().getItem();
            if ((tool instanceof ItemPickaxe && breakBlock instanceof BlockOre)
                    || (tool instanceof ItemAxe && breakMaterial == Blocks.LOG.getMaterial(null))
                    || (tool instanceof ItemHoe && (breakBlock == Blocks.WHEAT || breakBlock == Blocks.CARROTS || breakBlock == Blocks.POTATOES))) {
                if (tool instanceof ItemAxe && breakMaterial == Blocks.LOG.getMaterial(null)) {
                    BlockAPI log = breakTree(world, breakPos);
                    for (int i = 0; i < log.size(); i++) {
                        if (log.getBlock(i) == Blocks.LOG || log.getBlock(i) == Blocks.LOG2 || log.getBlock(i) == Blocks.LEAVES || log.getBlock(i) == Blocks.LEAVES2) {
                            world.destroyBlock(log.getPos(i), true);
                            world.playEvent(1021, log.getPos(i), 0);
                            player.getHeldItemMainhand().damageItem(1, player);
                        }
                    }
                    return;
                }
                BlockAPI blockAPI = WorldAPI.getBlock(world, breakPos, level * 2);

                for (int i = 0; i < blockAPI.size(); i++) {
                    Block block = blockAPI.getBlock(i);

                    if (block == breakBlock) {
                        world.destroyBlock(blockAPI.getPos(i), true);
                        world.playEvent(1021, blockAPI.getPos(i), 0);
                        player.getHeldItemMainhand().damageItem(1, player);
                    }
                }
            }
        }


        if (e.getState().getBlock() == Blocks.GRAVEL) {
            if (world.rand.nextInt(2) == 0) {
                EntityItem entityItem = new EntityItem(e.getWorld(), e.getPos().getX(), e.getPos().getY(), e.getPos().getZ(), new ItemStack(Items.FLINT));
                world.spawnEntityInWorld(entityItem);
            }
        }
    }

    @SubscribeEvent
    public void event(ExplosionEvent e) {
        if (e.getExplosion().getExplosivePlacedBy() instanceof EntityCreeper) {
            if (e.getWorld().rand.nextInt(30) == 0) {
                Vec3d pos = e.getExplosion().getPosition();
                EntityCreeper creeper = new EntityCreeper(e.getWorld());
                creeper.setPosition(pos.xCoord, pos.yCoord, pos.zCoord);
                e.getWorld().spawnEntityInWorld(creeper);
                System.out.println("크리퍼가 부활함");
            }
        }
    }

    //@SubscribeEvent
    public void event(RenderGameOverlayEvent.Post e) {
        if (WorldAPI.getCurrentWorldName().equalsIgnoreCase("SUAKGE")) {
            if (e.getType() == RenderGameOverlayEvent.ElementType.ALL) {
                if (WorldAPI.equalsHeldItem(Items.COMPASS)) {
                    Minecraft.getMinecraft().fontRendererObj.drawString("근처 텔레포트 크리퍼 수:" + creeperSize, 0, 10,
                            0xFFFFFF);
                    Minecraft.getMinecraft().fontRendererObj.drawString("근처 크리퍼 거미 수:" + spiderSize, 0, 20,
                            0xFFFFFF);
                    Minecraft.getMinecraft().fontRendererObj.drawString("근처 스켈레톤 수:" + skeletonSize, 0, 30,
                            0xFFFFFF);
                }
            }
        }
    }

    @SubscribeEvent
    public void livingSpawn(LivingSpawnEvent event) {
        Random rand = event.getWorld().rand;
        if(event.getEntityLiving() instanceof EntityLiving){
            EntityLiving living = (EntityLiving) event.getEntityLiving();
            ((PathNavigateGround)living.getNavigator()).setBreakDoors(true);
            ((PathNavigateGround)living.getNavigator()).setEnterDoors(true);
        }
    }

    @SubscribeEvent
    public void skeletonTeamKill(LivingHurtEvent event) {
        if(event.getEntityLiving() instanceof EntitySkeleton && event.getSource().getEntity() instanceof EntitySkeleton){
            event.setCanceled(true);

        }
    }

    @SubscribeEvent
    public void event(LivingExperienceDropEvent event) {
        if(event.getAttackingPlayer().inventory.armorInventory[0] != null) {
            int level = EnchantmentHelper.getEnchantmentLevel(SUAKGEMod.bonusExpEnchant, event.getAttackingPlayer().inventory.armorInventory[0]);
            if (level != 0) {
                event.setDroppedExperience(event.getDroppedExperience() * (level + 60));
            }
        }

    }

    @SubscribeEvent
    public void event(PlayerPickupXpEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        int level = EnchantmentHelper.getEnchantmentLevel(SUAKGEMod.bonusExpEnchant, player.inventory.armorInventory[0]);
        System.out.println(player.inventory.armorInventory[3]);
        if (level != 0) {
            event.getOrb().xpValue = event.getOrb().xpValue * (level + 1000);
        }
    }

    @SubscribeEvent
    public void event(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            int level = EnchantmentHelper.getEnchantmentLevel(SUAKGEMod.rangeEnchant, event.getEntityLiving().getHeldItemMainhand());
            if (level != 0) {
                List<EntityLiving> mobList = EntityAPI.getEntity(event.getEntityLiving().worldObj, event.getEntityLiving().getEntityBoundingBox().expandXyz(level * 2), EntityLiving.class);
                for (EntityLiving mob : mobList) {
                    if (mob.getPassengers().size() > 0) {
                        for (Entity living : mob.getPassengers()) {
                            living.attackEntityFrom(event.getSource(), event.getAmount());
                        }
                    }
                    if ((mob instanceof IMob && mob.getAttackTarget() instanceof EntityPlayer) || !(mob instanceof IMob)) {
                        mob.attackEntityFrom(event.getSource(), event.getAmount());
                    }
                }
            }
        }
    }

    //@SubscribeEvent
    public void event(ItemTooltipEvent event) {
        if (WorldAPI.getCurrentWorldName().equalsIgnoreCase("SUAKGE")) {
            if (event.getItemStack().getDisplayName().equalsIgnoreCase("나침반 - 탐지")) {
                event.getToolTip().add("범위:15");
                event.getToolTip().add("탐지:스파이더 크리퍼 스켈레톤 텔레포트 크리퍼");
            }
        }
    }

    @SubscribeEvent
    public void event(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        World worldObj = event.player.worldObj;
        if (worldObj.isRaining()) {
            int thunder = 0;
            for (ItemStack itemStack : event.player.inventory.armorInventory) {
                if (itemStack.getItem() == Items.IRON_CHESTPLATE
                        || itemStack.getItem() == Items.IRON_HELMET
                        || itemStack.getItem() == Items.IRON_BOOTS
                        || itemStack.getItem() == Items.IRON_LEGGINGS) {
                    thunder++;
                }
            }
            if (player.getHeldItemOffhand().getItem().getUnlocalizedName().indexOf("iron") != -1 || player.getHeldItemMainhand().getItem().getUnlocalizedName().indexOf("iron") != -1) {
                thunder++;
            }
            if (MiniGame.cooldownTracker.getCooldown("lightningDelay", 0) == 0 && worldObj.canSeeSky(player.getPosition()) && worldObj.rand.nextInt(100 - (thunder * 10)) == 0) {
                EntityLightningBolt lightningBolt = null;
                if (thunder >= 2) {
                    double spawnX = player.posX + WorldAPI.rand(10);
                    double spawnZ = player.posZ + WorldAPI.rand(10);
                    int spawnY = WorldAPI.getUPBlock(worldObj, (int) spawnX, (int) player.posY, (int) spawnZ);
                    lightningBolt = new EntityLightningBolt(worldObj, player.posX + WorldAPI.rand(10), spawnY, player.posZ + WorldAPI.rand(10), false);
                }
                if (thunder > 3) {
                    double spawnX = player.posX + WorldAPI.rand(6);
                    double spawnZ = player.posZ + WorldAPI.rand(6);
                    int spawnY = WorldAPI.getUPBlock(worldObj, (int) spawnX, (int) player.posY, (int) spawnZ);
                    lightningBolt = new EntityLightningBolt(worldObj, player.posX + WorldAPI.rand(6), spawnY, player.posZ + WorldAPI.rand(6), false);
                }
                if (thunder > 4) {
                    double spawnX = player.posX + WorldAPI.rand(3);
                    double spawnZ = player.posZ + WorldAPI.rand(3);
                    int spawnY = WorldAPI.getUPBlock(worldObj, (int) spawnX, (int) player.posY, (int) spawnZ);
                    lightningBolt = new EntityLightningBolt(worldObj, player.posX + WorldAPI.rand(3), spawnY, player.posZ + WorldAPI.rand(3), false);
                }
                if (thunder >= 2) {
                    worldObj.addWeatherEffect(lightningBolt);
                    MiniGame.cooldownTracker.setCooldown("lightningDelay", 200);
                }
            }
        }
        if (event.player.getHeldItemMainhand() != null) {
            ItemStack itemStack = event.player.getHeldItemMainhand();

            HashMap<Enchantment, Integer> enchants = (HashMap<Enchantment, Integer>) EnchantmentHelper.getEnchantments(itemStack);
            boolean isAdd = false;
            if (enchants.containsKey(Enchantments.SMITE) && !enchants.containsKey(Enchantments.BANE_OF_ARTHROPODS)) {
                enchants.put(Enchantments.BANE_OF_ARTHROPODS, enchants.get(Enchantments.SMITE));
                isAdd = true;
            }
            if (enchants.containsKey(Enchantments.BANE_OF_ARTHROPODS) && !enchants.containsKey(Enchantments.SMITE)) {
                enchants.put(Enchantments.SMITE, enchants.get(Enchantments.BANE_OF_ARTHROPODS));
                isAdd = true;
            }
            if (enchants.containsKey(Enchantments.PROJECTILE_PROTECTION) && !enchants.containsKey(Enchantments.BLAST_PROTECTION)) {
                enchants.put(Enchantments.BLAST_PROTECTION, enchants.get(Enchantments.PROJECTILE_PROTECTION));
                isAdd = true;
            }
            if (enchants.containsKey(Enchantments.BLAST_PROTECTION) && !enchants.containsKey(Enchantments.PROJECTILE_PROTECTION)) {
                enchants.put(Enchantments.PROJECTILE_PROTECTION, enchants.get(Enchantments.BLAST_PROTECTION));
                isAdd = true;
            }
            if (isAdd) {
                EnchantmentHelper.setEnchantments(enchants, itemStack);
            }
        }
        if (WorldAPI.getCurrentWorldName().equalsIgnoreCase("SUAKGE")) {
            if (event.side == Side.SERVER && event.phase == TickEvent.Phase.END) {
                creeperSize = 0;
                spiderSize = 0;
                ItemStack compass = null;
                if (player.getHeldItemMainhand() != null)
                    if (player.getHeldItemMainhand().getItem() instanceof ItemCompass) {
                        compass = player.getHeldItemMainhand();
                    }
                if (player.getHeldItemOffhand() != null)
                    if (player.getHeldItemOffhand().getItem() instanceof ItemCompass) {
                        compass = player.getHeldItemOffhand();
                    }
                if (compass != null) {
                    List<String> tooltip = compass.getTooltip(event.player, false);
                    int size = 16;
                    boolean tpcreeper = false, creeperSpider = false, skeleton = false;
                    for (String str : tooltip) {
                        if (str.indexOf("범위:") != -1) {
                            size = Integer.valueOf(str.replace("범위:", ""));
                        }
                        if (str.indexOf("탐지:") != -1) {
                            tpcreeper = str.indexOf("텔레포트 크리퍼") != -1;
                            creeperSpider = str.indexOf("스파이더 크리퍼") != -1;
                            skeleton = str.indexOf("스켈레톤") != -1;
                        }
                    }
                    List<EntityLiving> list = player.worldObj.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(player.posX - size, player.posY + size, player.posZ - size, player.posX + size, player.posY + size, player.posZ + size));
                    for (EntityLiving base : list) {
                        if (tpcreeper && base instanceof EntityTeleportCreeper)
                            creeperSize++;
                        if (creeperSpider && base instanceof EntitySpiderJockey)
                            spiderSize++;
                    }

                }
            }
        }
    }

}
