package mightydanp.industrialtech.common;

import mightydanp.industrialtech.api.client.ClientEvent;
import mightydanp.industrialtech.api.client.settings.keybindings.KeyBindings;
import mightydanp.industrialtech.api.common.CommonEvent;
import mightydanp.industrialtech.api.common.ISidedReference;
import mightydanp.industrialtech.api.common.blocks.ITBlocks;
import mightydanp.industrialtech.api.common.crafting.recipe.Recipes;
import mightydanp.industrialtech.api.common.handler.RegistryHandler;
import mightydanp.industrialtech.api.common.inventory.container.Containers;
import mightydanp.industrialtech.api.common.items.ITItems;
import mightydanp.industrialtech.api.common.libs.Ref;
import mightydanp.industrialtech.api.common.tileentities.TileEntities;
import mightydanp.industrialtech.api.server.DedicatedServerReference;
import mightydanp.industrialtech.client.ModClientEvent;
import mightydanp.industrialtech.client.settings.KeyBindings.ModKeyBindings;
import mightydanp.industrialtech.common.blocks.ModBlocks;
import mightydanp.industrialtech.common.crafting.recipe.ModRecipes;
import mightydanp.industrialtech.common.items.ModItems;
import mightydanp.industrialtech.common.materials.ModMaterials;
import mightydanp.industrialtech.common.stonelayers.ModStoneLayers;
import mightydanp.industrialtech.common.tileentities.ModTileEntities;
import mightydanp.industrialtech.common.tools.ModTools;
import mightydanp.industrialtech.common.trees.ModTrees;
import mightydanp.industrialtech.data.config.DataConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by MightyDanp on 9/26/2020.
 */
@Mod(Ref.mod_id)
public class IndustrialTech {
    public IndustrialTech INSTANCE;
    public static final Logger LOGGER = LogManager.getLogger();
    public static final ISidedReference SIDED_SYSTEM = DistExecutor.safeRunForDist(() -> ModClientEvent::new, () -> DedicatedServerReference::new);

    public IndustrialTech(){
        INSTANCE = this;
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus(), forge = MinecraftForge.EVENT_BUS;
        SIDED_SYSTEM.setup(modEventBus, forge);

        RegistryHandler.init(modEventBus);
        ModStoneLayers.init();
        ModMaterials.commonInit();
        ModTools.init();
        ITItems.init();
        ModItems.init();
        ITBlocks.init();
        ModBlocks.init();
        ITItems.initBlockItems();
        ModItems.initBlockItems();
        ModTrees.commonInit();
        Containers.init();
        TileEntities.init();
        ModTileEntities.init();

        KeyBindings.init();
        ModKeyBindings.init();

        Recipes.init();
        ModRecipes.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(CommonEvent::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModCommonEvent::init);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientEvent::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModClientEvent::init);

        modLoadingContext.registerConfig(ModConfig.Type.SERVER, DataConfig.SERVER_SPEC);
        modLoadingContext.registerConfig(ModConfig.Type.CLIENT, DataConfig.CLIENT_SPEC);
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, DataConfig.COMMON_SPEC);
    }
}
