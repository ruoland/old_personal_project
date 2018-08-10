package ruo.asdf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ruo.asdf.enchant.*;
import ruo.awild.EntityWildZombie;
import ruo.cmplus.deb.DebAPI;

//@Mod(modid = "SUAKGE", name = "Super Ultra King God Emperor Mod")
public class SUAKGEMod {
    public static final Enchantment bonusExpEnchant = new BonusExpEnchant();
    public static final Enchantment explosionEnchant = new ExplosionEnchant();
    public static final Enchantment levitationEnchant = new LevitaionEnchant();
    public static final Enchantment rangeEnchant = new RangeAttackEnchant();
    public static final Enchantment chageEnchant = new ChargeEnchant();
    public static final Enchantment rangeBreakEnchant = new RangeBreakEnchant();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        DebAPI.registerEntity(this, "TPCreeper", EntityTeleportCreeper.class, new RenderCreeper(Minecraft.getMinecraft().getRenderManager()));
        DebAPI.registerEntity(this, "SpiderCreeper", EntitySpiderJockey.class, new RenderSpider(Minecraft.getMinecraft().getRenderManager()));
        DebAPI.registerEntity(this, "MissileCreeper", EntityMissileCreeper.class);
        DebAPI.registerEntity(this, "Skelereeper", EntitySkelereeper.class, new RenderSkeleton(Minecraft.getMinecraft().getRenderManager()));
        DebAPI.registerEntity(this, "FlyingSkeleton", EntityFlyingZombie.class);
        DebAPI.registerEntity(this, "FlyingCreeper", EntityFlyingCreeper.class);

        DebAPI.registerEntity(this, "WildZombie", EntityWildZombie.class, new RenderZombie(Minecraft.getMinecraft().getRenderManager()));
        ForgeRegistries.ENCHANTMENTS.register(levitationEnchant);
        ForgeRegistries.ENCHANTMENTS.register(explosionEnchant);
        ForgeRegistries.ENCHANTMENTS.register(rangeEnchant);
        ForgeRegistries.ENCHANTMENTS.register(chageEnchant);
        ForgeRegistries.ENCHANTMENTS.register(bonusExpEnchant);
        ForgeRegistries.ENCHANTMENTS.register(rangeBreakEnchant);

        Biome[] biomes = BiomeProvider.allowedBiomes.toArray(new Biome[BiomeProvider.allowedBiomes.size()]);
        EntityRegistry.addSpawn(EntityTeleportCreeper.class, 50, 1, 5, EnumCreatureType.MONSTER, biomes);
        EntityRegistry.addSpawn(EntitySpiderJockey.class, 50, 1, 5, EnumCreatureType.MONSTER, biomes);
        //EntityRegistry.addSpawn(EntityGhast.class, 3, 1, 3, EnumCreatureType.MONSTER, biomes);
        DebAPI.registerEvent(new SUAKGEEvent());
        ItemStack stack = new ItemStack(Items.COMPASS, 1);
        stack.setStackDisplayName("나침반 - 탐지");
        GameRegistry.addShapelessRecipe(stack,
                new ItemStack(Items.COMPASS, 1),
                new ItemStack(Items.COMPASS, 1));
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {

    }

}
