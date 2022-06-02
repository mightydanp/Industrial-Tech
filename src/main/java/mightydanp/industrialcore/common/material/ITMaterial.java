package mightydanp.industrialcore.common.material;

import com.mojang.datafixers.util.Pair;
import mightydanp.industrialcore.common.blocks.*;
import mightydanp.industrialcore.common.handler.RegistryHandler;
import mightydanp.industrialcore.common.items.*;
import mightydanp.industrialcore.common.jsonconfig.fluidstate.DefaultFluidState;
import mightydanp.industrialcore.common.jsonconfig.fluidstate.IFluidState;
import mightydanp.industrialcore.common.jsonconfig.icons.ITextureIcon;
import mightydanp.industrialcore.common.jsonconfig.material.data.MaterialRegistry;
import mightydanp.industrialcore.common.jsonconfig.material.ore.DefaultOreType;
import mightydanp.industrialcore.common.jsonconfig.material.ore.IOreType;
import mightydanp.industrialcore.common.jsonconfig.tool.part.DefaultToolPart;
import mightydanp.industrialcore.common.jsonconfig.tool.part.IToolPart;
import mightydanp.industrialcore.common.libs.Ref;
import mightydanp.industrialcore.common.material.fluid.ITFluid;
import mightydanp.industrialcore.common.material.fluid.ITFluidBlock;
import mightydanp.industrialcore.common.resources.asset.AssetPackRegistry;
import mightydanp.industrialcore.common.resources.asset.data.BlockModelData;
import mightydanp.industrialcore.common.resources.asset.data.BlockStateData;
import mightydanp.industrialcore.common.resources.asset.data.ItemModelData;
import mightydanp.industrialcore.common.resources.asset.data.LangData;
import mightydanp.industrialcore.common.resources.data.DataPackRegistry;
import mightydanp.industrialcore.common.items.*;
import mightydanp.industrialcore.common.blocks.*;
import mightydanp.industrialcore.common.holder.MCMaterialHolder;
import mightydanp.industrialcore.common.items.*;
import mightydanp.industrialcore.common.jsonconfig.flag.IMaterialFlag;
import mightydanp.industrialtech.common.IndustrialTech;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static mightydanp.industrialcore.common.jsonconfig.flag.DefaultMaterialFlag.*;
import static mightydanp.industrialcore.common.jsonconfig.flag.DefaultMaterialFlag.INGOT;

/**
 * Created by MightyDanp on 12/1/2021.
 */
public class ITMaterial extends net.minecraftforge.registries.ForgeRegistryEntry<ITMaterial> {
    public final String name;
    public final int color;

    public Pair<String, ITextureIcon> textureIcon;

    public String symbol = null;

    public Integer denseOreDensity = 8;
    public IOreType oreType = null;

    public Boolean isStoneLayer = null;
    public String stoneLayerBlock = null;
    public String stoneLayerTextureLocation = null;

    public Integer miningLevel = null;

    public Integer meltingPoint = null;
    public Integer boilingPoint = null;

    public Float fluidAcceleration = 0.014F;
    public IFluidState fluidState = null;
    public Integer fluidDensity = null;
    public Integer fluidViscosity = null;
    public Integer fluidLuminosity = null;
    public Integer durability = null;
    public Integer attackSpeed = null;
    public Float attackDamage = null;
    public Float weight = null;
    public Map<String, Integer> toolTypes;
    public List<IToolPart> toolParts = new ArrayList<>();

    public  List<IMaterialFlag> materialFlags = new ArrayList<>();
    public List<RegistryObject<Block>> oreList = new ArrayList<>();
    public List<RegistryObject<Block>> smallOreList = new ArrayList<>();
    public List<RegistryObject<Block>> denseOreList = new ArrayList<>();

    public List<RegistryObject<Block>> thinSlabList = new ArrayList<>();

    public List<RegistryObject<Item>> oreItemList = new ArrayList<>();
    public List<RegistryObject<Item>> smallOreItemList = new ArrayList<>();
    public List<RegistryObject<Item>> denseOreItemList = new ArrayList<>();

    public List<RegistryObject<Item>> thinSlabItemList = new ArrayList<>();


    public RegistryObject<Item> ingot, gem, chippedGem, flawedGem, flawlessGem, legendaryGem, crushedOre, purifiedOre, centrifugedOre, dust, smallDust, tinyDust;
    public RegistryObject<FlowingFluid> fluid, fluid_flowing;
    public RegistryObject<Block> fluidBlock;

    public RegistryObject<Block> layerBlock, rockBlock, thinSlabBlock;

    public RegistryObject<Item> layerItemBlock, rockItemBlock, thinSlabItemBlock;

    public RegistryObject<Item> bucket, dullAxeHead, dullBuzzSawHead, dullChiselHead, dullHoeHead, dullPickaxeHead, dullArrowHead, dullSawHead, dullSwordHead;
    public RegistryObject<Item> drillHead, axeHead, buzzSawHead, chiselHead, fileHead, hammerHead, hoeHead, pickaxeHead, arrowHead, sawHead, shovelHead, swordHead, screwdriverHead;
    public RegistryObject<Item> wedge, wedgeHandle;




    public ITMaterial(String materialNameIn, int colorIn, Pair<String, ITextureIcon> textureIconLocationIn) {
        name = materialNameIn;
        color = colorIn;
        textureIcon = textureIconLocationIn;
    }

    public ITMaterial setElementalLocalization(String elementIn){
        symbol = elementIn;
        return this;
    }

    public ITMaterial setTemperatureProperties(int meltingPointIn, int boilingPointIn){
        meltingPoint = meltingPointIn;
        boilingPoint = boilingPointIn;
        return this;
    }

    public ITMaterial setStoneLayerProperties(Boolean isStoneLayerIn, String stoneLayerBlockIn, String stoneLayerTextureLocationIn) {
        isStoneLayer = isStoneLayerIn;
        stoneLayerBlock = stoneLayerBlockIn;
        stoneLayerTextureLocation = stoneLayerTextureLocationIn;

        if(isStoneLayerIn) {
            materialFlags.add(STONE_LAYER);
        }

        return this;
    }

    public ITMaterial setBlockProperties(int miningLevelIn) {
        miningLevel = miningLevelIn;
        return this;
    }

    public ITMaterial setOreType(IOreType oreTypeIn){
        oreType = oreTypeIn;

        if(oreTypeIn == DefaultOreType.ORE) {
            materialFlags.add(ORE);
        }

        if(oreTypeIn == DefaultOreType.GEM) {
            materialFlags.add(GEM);
        }

        if(oreTypeIn == DefaultOreType.CRYSTAL) {

        }
        return this;
    }

    public ITMaterial setDenseOreDensity(int densityIn){
        denseOreDensity = densityIn;
        return this;
    }

    public ITMaterial setFluidProperties(IFluidState stateIn, float accelerationIn, Integer densityIn, Integer luminosityIn, Integer viscosityIn){
        fluidState = stateIn;
        fluidAcceleration = accelerationIn;
        if(densityIn != null) fluidDensity = densityIn;
        if(luminosityIn != null) fluidLuminosity = luminosityIn;
        if(viscosityIn != null) fluidViscosity = viscosityIn;

        if(stateIn == DefaultFluidState.FLUID){
            materialFlags.add(FLUID);
        }

        if(stateIn == DefaultFluidState.GAS){
            materialFlags.add(GAS);
        }

        return this;
    }

    public ITMaterial setToolProperties(int attackSpeedIn, int durabilityIn, float attackDamageIn, float weightIn, Map<String, Integer> toolTypesIn, List<IToolPart> toolPartIn){
        attackSpeed = attackSpeedIn;
        durability = durabilityIn;
        attackDamage = attackDamageIn;
        weight = weightIn;
        toolTypes = toolTypesIn;
        toolParts = toolPartIn;
        return this;
    }



    public ITMaterial save() {
        LangData enLang = AssetPackRegistry.langDataMap.getOrDefault("en_us", new LangData());
        List<ITMaterial> stoneLayerList = ((MaterialRegistry) IndustrialTech.configSync.material.getFirst()).getAllValues().stream().filter(i -> i.isStoneLayer != null && i.isStoneLayer).toList();


        for(IMaterialFlag flag : materialFlags){
            if(flag == ORE || flag == GEM || flag == STONE_LAYER){
                if(flag == STONE_LAYER){
                    String stoneLayerBlockName = stoneLayerBlock.equals("")? String.valueOf(layerBlock.get().getRegistryName()) : stoneLayerBlock;
                    String stoneLayerModId = stoneLayerTextureLocation.split(":")[0].equals("minecraft") ? "" : stoneLayerTextureLocation.split(":")[0];

                    String stoneLayerBlock = stoneLayerTextureLocation.split(":")[1];
                    //String resourceID = useMinecraftResource ? "" : Ref.mod_id;
                    AssetPackRegistry.blockModelDataMap.put(name + "_ore", new BlockModelData().setParent(new ResourceLocation( Ref.mod_id,"block/ore/state/ore"))
                            .setParentFolder("/ore").setTexturesLocation("particle", new ResourceLocation(stoneLayerModId, stoneLayerBlock)).setTexturesLocation("sourceblock", new ResourceLocation(stoneLayerModId, stoneLayerBlock)));
                    AssetPackRegistry.blockModelDataMap.put("small_" + name + "_ore", new BlockModelData().setParent(new ResourceLocation(Ref.mod_id, "block/ore/state/small_ore"))
                            .setParentFolder("/small_ore").setTexturesLocation("particle", new ResourceLocation(stoneLayerModId, stoneLayerBlock)).setTexturesLocation("sourceblock", new ResourceLocation(stoneLayerModId, stoneLayerBlock)));
                    AssetPackRegistry.blockModelDataMap.put("dense_" + name + "_ore", new BlockModelData().setParent(new ResourceLocation(Ref.mod_id, "block/ore/state/dense_ore"))
                            .setParentFolder("/dense_ore").setTexturesLocation("particle", new ResourceLocation(stoneLayerModId, stoneLayerBlock)).setTexturesLocation("sourceblock", new ResourceLocation(stoneLayerModId, stoneLayerBlock)));
                    AssetPackRegistry.blockModelDataMap.put("dense_" + name + "_ore", new BlockModelData().setParent(new ResourceLocation(Ref.mod_id, "block/ore/state/dense_ore"))
                            .setParentFolder("/dense_ore").setTexturesLocation("particle", new ResourceLocation(stoneLayerModId, stoneLayerBlock)).setTexturesLocation("sourceblock", new ResourceLocation(stoneLayerModId, stoneLayerBlock)));
                    //--//
                    rockBlock = RegistryHandler.BLOCKS.register(name + "_rock", ()-> new RockBlock(stoneLayerBlockName, BlockBehaviour.Properties.of(MCMaterialHolder.rock)));
                    DataPackRegistry.saveBlockTagData(DataPackRegistry.getBlockTagData(new ResourceLocation("forge", "rocks/" + name)).addValue(rockBlock.getId()));
                    DataPackRegistry.saveBlockTagData(DataPackRegistry.getBlockTagData(new ResourceLocation("forge","rocks")).addValue(rockBlock.getId()));
                    //
                    AssetPackRegistry.blockStateDataMap.put(name + "_rock", new BlockStateData().setBlockStateModelLocation("", new ResourceLocation(Ref.mod_id, "block/stone_layer/rock/" + name + "_rock")));
                    AssetPackRegistry.blockModelDataMap.put(name + "_rock", new BlockModelData().setParent(new ResourceLocation(Ref.mod_id,"block/stone_layer/state/rock"))
                            .setParentFolder("/stone_layer/rock").setTexturesLocation("particle", new ResourceLocation(stoneLayerModId, stoneLayerBlock))
                                    //.setTexturesLocation("sourceblock", new ResourceLocation(stoneLayerModId, stoneLayerBlock))
                            //.setTexturesLocation("texture", new ResourceLocation(stoneLayerModId, stoneLayerBlock))
                    );
                    enLang.addTranslation("block." + Ref.mod_id + "." +  name + "_rock", LangData.translateUpperCase(name + "_rock"));
                    //--
                    rockItemBlock = RegistryHandler.ITEMS.register(name + "_rock", ()-> new RockBlockItem(rockBlock.get(), stoneLayerBlockName, new Item.Properties().stacksTo(64).tab(ModItemGroups.stone_layer_tab)));
                    DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","rocks/" + name)).addValue(rockItemBlock.getId()));
                    DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","rocks")).addValue(rockItemBlock.getId()));
                    //
                    AssetPackRegistry.itemModelDataHashMap.put(name + "_rock", new ItemModelData().setParent(new ResourceLocation(Ref.mod_id,"item/material_icons/" + textureIcon.getSecond().getName() + "/rock")));
                    AssetPackRegistry.itemModelDataHashMap.put(name + ":rock", new ItemModelData().setParentFolder("/material_icons/" + textureIcon.getSecond().getName().toLowerCase()).setParent(new ResourceLocation("item/generated"))
                            .setTexturesLocation("layer0", new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName().toLowerCase() + "/rock"))
                            .setTexturesLocation("layer1", new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName().toLowerCase() + "/rock_overlay"))
                    );
                    enLang.addTranslation("item." + Ref.mod_id + "." + name + "_rock", LangData.translateUpperCase(name + "_rock"));
                    //--//
                    thinSlabBlock = RegistryHandler.BLOCKS.register("thin_" + name + "_slab", ()-> new ThinSlabBlock(BlockBehaviour.Properties.of(MCMaterialHolder.rock), stoneLayerBlockName));
                    DataPackRegistry.saveBlockTagData(DataPackRegistry.getBlockTagData(new ResourceLocation("forge","thin_slabs/" + name)).addValue(rockBlock.getId()));
                    DataPackRegistry.saveBlockTagData(DataPackRegistry.getBlockTagData(new ResourceLocation("forge","thin_slabs")).addValue(rockBlock.getId()));
                    //
                    AssetPackRegistry.blockStateDataMap.put("thin_" + name + "_slab", new BlockStateData().setBlockStateModelLocation("", new ResourceLocation(Ref.mod_id, "block/stone_layer/thin_slab/" + "thin_" + name + "_slab")));
                    AssetPackRegistry.blockModelDataMap.put("thin_" + name + "_slab", new BlockModelData().setParent(new ResourceLocation(Ref.mod_id, "block/stone_layer/state/thin_slab"))
                            .setParentFolder("/stone_layer/thin_slab").setTexturesLocation("particle", new ResourceLocation(stoneLayerModId, stoneLayerBlock)).setTexturesLocation("sourceblock", new ResourceLocation(stoneLayerModId, stoneLayerBlock))
                            .setTexturesLocation("texture", new ResourceLocation(stoneLayerModId, stoneLayerBlock)));
                    enLang.addTranslation("block." + Ref.mod_id + ".thin_" + name + "_slab", LangData.translateUpperCase("thin_" + name + "_slab"));
                    //--
                    thinSlabItemBlock = RegistryHandler.ITEMS.register("thin_" + name + "_slab", ()-> new ThinSlabItemBlock(thinSlabBlock.get(), new Item.Properties().stacksTo(1).tab(ModItemGroups.stone_layer_tab)));
                    DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","thin_slabs/" + name)).addValue(rockItemBlock.getId()));
                    DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","thin_slabs")).addValue(rockItemBlock.getId()));
                    //
                    AssetPackRegistry.itemModelDataHashMap.put("thin_" + name + "_slab", new ItemModelData().setParent(new ResourceLocation(Ref.mod_id, "block/stone_layer/thin_slab/thin_" + name + "_slab")));
                    enLang.addTranslation("item." + Ref.mod_id + ".thin_" + name + "_slab", LangData.translateUpperCase("thin_" + name + "_slab"));
                    //--//
                        /*
                        Block leg_block = RegistryHandler.BLOCKS.register(name + "_leg", new LegBlock(AbstractBlock.Properties.of(Material.STONE), new ResourceLocation("textures/" + stoneLayer.getBlock())));
                        Item leg_item_block = RegistryHandler.ITEMS.register( name + "_leg", new LegItemBlock(leg_block, new Item.Properties().stacksTo(1)));
                         */
                }


                for(ITMaterial  stoneLayer : stoneLayerList){
                    String stoneLayerModId = stoneLayer.stoneLayerTextureLocation.split(":")[0].equals("minecraft") ? "" : stoneLayer.stoneLayerTextureLocation.split(":")[0];
                    String stoneLayerBlock = stoneLayer.stoneLayerTextureLocation.split(":")[1];
                    //String resourceID = stoneLayerModId.equals("resourceID") ? "" : Ref.mod_id;



                    if(flag == ORE || flag == GEM) {
                        String stoneLayerBlockName = stoneLayer.stoneLayerBlock.equals("")? String.valueOf(layerBlock.get().getRegistryName()) : stoneLayer.stoneLayerBlock;
                        //--//
                        RegistryObject<Block> ore = RegistryHandler.BLOCKS.register( stoneLayer.name + "_" + name + "_ore", ()-> new OreBlock(name + "_ore", BlockBehaviour.Properties.of(net.minecraft.world.level.material.Material.STONE), stoneLayerBlockName));
                        DataPackRegistry.saveBlockTagData(DataPackRegistry.getBlockTagData(new ResourceLocation("forge","ores/" + name)).addValue(ore.getId()));
                        DataPackRegistry.saveBlockTagData(DataPackRegistry.getBlockTagData(new ResourceLocation("forge","ores")).addValue(ore.getId()));
                        oreList.add(ore);
                        //
                        AssetPackRegistry.blockStateDataMap.put(stoneLayer.name + "_" + name + "_ore", new BlockStateData().setBlockStateModelLocation("", new ResourceLocation(Ref.mod_id, "block/ore/" + stoneLayer.name + "_ore")));
                        enLang.addTranslation("block." + Ref.mod_id + "." + stoneLayer.name + "_" + name + "_ore", LangData.translateUpperCase(stoneLayer.name + "_" + name + "_ore"));
                        //--
                        RegistryObject<Item> oreItemR = RegistryHandler.ITEMS.register( stoneLayer.name + "_" + name + "_ore",  ()->
                                new BlockOreItem(ore, new Item.Properties().tab(ModItemGroups.ore_tab)
                                , boilingPoint, meltingPoint, symbol));
                        DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","ores/" + name)).addValue(oreItemR.getId()));
                        DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","ores")).addValue(oreItemR.getId()));
                        oreItemList.add(oreItemR);
                        //
                        AssetPackRegistry.itemModelDataHashMap.put(stoneLayer.name + "_" + name + "_ore", new ItemModelData().setParent(new ResourceLocation(Ref.mod_id, "block/ore/" + stoneLayer.name + "_ore")));
                        enLang.addTranslation("item." + Ref.mod_id + "." + stoneLayer.name + "_" + name + "_ore", LangData.translateUpperCase(stoneLayer.name + "_" + name + "_ore"));
                        //--//
                        RegistryObject<Block> smallOreBlockR = RegistryHandler.BLOCKS.register( "small_" + stoneLayer.name + "_" + name + "_ore", ()->
                                new SmallOreBlock("small_" + name + "_ore", BlockBehaviour.Properties.of(net.minecraft.world.level.material.Material.STONE), stoneLayerBlockName));
                        DataPackRegistry.saveBlockTagData(DataPackRegistry.getBlockTagData(new ResourceLocation("forge","small_ores/" + stoneLayer.name + "_" + name)).addValue(smallOreBlockR.getId()));
                        DataPackRegistry.saveBlockTagData(DataPackRegistry.getBlockTagData(new ResourceLocation("forge","small_ores")).addValue(smallOreBlockR.getId()));
                        smallOreList.add(smallOreBlockR);
                        //
                        AssetPackRegistry.blockStateDataMap.put("small_" + stoneLayer.name + "_" + name + "_ore", new BlockStateData().setBlockStateModelLocation("", new ResourceLocation(Ref.mod_id, "block/small_ore/" + "small_" + stoneLayer.name + "_ore")));
                        enLang.addTranslation("block." + Ref.mod_id + "." + "small_" + stoneLayer.name + "_" + name + "_ore", LangData.translateUpperCase("small_" + stoneLayer.name + "_" + name + "_ore"));
                        //--
                        RegistryObject<Item> smallOreItemR = RegistryHandler.ITEMS.register( "small_" + stoneLayer.name + "_" + name + "_ore", ()->
                                new BlockOreItem(smallOreBlockR, new Item.Properties().tab(ModItemGroups.ore_tab), boilingPoint, meltingPoint, symbol));
                        DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","small_ores/" + stoneLayer.name + "_" + name)).addValue(smallOreBlockR.getId()));
                        DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","small_ores")).addValue(smallOreBlockR.getId()));
                        smallOreItemList.add(smallOreItemR);
                        //
                        AssetPackRegistry.itemModelDataHashMap.put("small_" + stoneLayer.name + "_" + name + "_ore", new ItemModelData().setParent(new ResourceLocation(Ref.mod_id, "block/small_ore/" + "small_" + stoneLayer.name + "_ore")));
                        enLang.addTranslation("item." + Ref.mod_id + "." + "small_" + stoneLayer.name + "_" + name + "_ore", LangData.translateUpperCase("small_" + stoneLayer.name + "_" + name + "_ore"));
                        //--//
                        RegistryObject<Block> denseOreBlockR = RegistryHandler.BLOCKS.register( "dense_" + stoneLayer.name + "_" + name + "_ore", ()->
                                new DenseOreBlock("dense_" + name + "_ore", BlockBehaviour.Properties.of(net.minecraft.world.level.material.Material.STONE), denseOreDensity, stoneLayerBlockName));
                        DataPackRegistry.saveBlockTagData(DataPackRegistry.getBlockTagData(new ResourceLocation("forge","dense_ores/" + stoneLayer.name + "_" + name)).addValue(denseOreBlockR.getId()));
                        DataPackRegistry.saveBlockTagData(DataPackRegistry.getBlockTagData(new ResourceLocation("forge","dense_ores")).addValue(denseOreBlockR.getId()));
                        denseOreList.add(denseOreBlockR);
                        //
                        AssetPackRegistry.blockStateDataMap.put("dense_" + stoneLayer.name + "_" + name + "_ore", new BlockStateData().setBlockStateModelLocation("", new ResourceLocation(Ref.mod_id, "block/dense_ore/" + "dense_" + stoneLayer.name + "_ore")));
                        enLang.addTranslation("block." + Ref.mod_id + "." + "dense_" + stoneLayer.name + "_" + name + "_ore", LangData.translateUpperCase("dense_" + stoneLayer.name + "_" + name + "_ore"));
                        //--
                        RegistryObject<Item> denseOreItemR = RegistryHandler.ITEMS.register( "dense_" + stoneLayer.name + "_" + name + "_ore", ()->
                                new BlockOreItem(denseOreBlockR, new Item.Properties().tab(ModItemGroups.ore_tab), boilingPoint, meltingPoint, symbol));
                        DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","dense_ores/" + stoneLayer.name + "_" + name)).addValue(denseOreItemR.getId()));
                        DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","dense_ores")).addValue(denseOreItemR.getId()));
                        denseOreItemList.add(denseOreItemR);
                        //
                        AssetPackRegistry.itemModelDataHashMap.put("dense_" + stoneLayer.name + "_" + name + "_ore", new ItemModelData().setParent(new ResourceLocation(Ref.mod_id, "block/dense_ore/" + "dense_" + stoneLayer.name + "_ore")));
                        enLang.addTranslation("item." + Ref.mod_id + "." + "dense_" + stoneLayer.name + "_" + name + "_ore", LangData.translateUpperCase("dense_" + stoneLayer.name + "_" + name + "_ore"));
                        //--//
                    }
                }

                if(flag == ORE || flag == GEM){
                    //--//
                    crushedOre = RegistryHandler.ITEMS.register("crushed_" + name + "_ore", ()-> new OreProductsItem(new Item.Properties()
                            .tab(ModItemGroups.ore_products_tab), boilingPoint, meltingPoint, symbol));
                    DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","crushed_ores/" + name)).addValue(crushedOre.getId()));
                    DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","crushed_ores")).addValue(crushedOre.getId()));
                    AssetPackRegistry.itemModelDataHashMap.put("crushed_" + name + "_ore", new ItemModelData().setParent(new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName() + "/crushed_ore")));
                    AssetPackRegistry.itemModelDataHashMap.put(name + ":crushed_ore", new ItemModelData().setParentFolder("/material_icons/" + textureIcon.getSecond().getName().toLowerCase()).setParent(new ResourceLocation("item/generated"))
                            .setTexturesLocation("layer0", new ResourceLocation(Ref.mod_id, "item/material_icons/" +  textureIcon.getSecond().getName().toLowerCase() + "/crushed_ore"))
                            .setTexturesLocation("layer1",  new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName().toLowerCase() + "/crushed_ore_overlay")));
                    enLang.addTranslation("item." + Ref.mod_id + "." + "crushed_" + name + "_ore", LangData.translateUpperCase("crushed_" + name + "_ore"));
                    //--//
                    purifiedOre = RegistryHandler.ITEMS.register("purified_" + name + "_ore", ()-> new OreProductsItem(new Item.Properties()
                            .tab(ModItemGroups.ore_products_tab), boilingPoint, meltingPoint, symbol));
                    DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","purified_ores/" + name)).addValue(purifiedOre.getId()));
                    DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","purified_ores")).addValue(purifiedOre.getId()));
                    AssetPackRegistry.itemModelDataHashMap.put("purified_" + name + "_ore", new ItemModelData().setParent(new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName() + "/purified_ore")));
                    AssetPackRegistry.itemModelDataHashMap.put(name + ":purified_ore", new ItemModelData().setParentFolder("/material_icons/" + textureIcon.getSecond().getName().toLowerCase()).setParent(new ResourceLocation("item/generated"))
                            .setTexturesLocation("layer0", new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName().toLowerCase() + "/purified_ore"))
                            .setTexturesLocation("layer1",  new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName().toLowerCase() + "/purified_ore_overlay")));
                    enLang.addTranslation("item." + Ref.mod_id + "." + "purified_" + name + "_ore", LangData.translateUpperCase("purified_" + name + "_ore"));
                    //--//
                    centrifugedOre = RegistryHandler.ITEMS.register("centrifuged_" + name + "_ore", ()-> new OreProductsItem(new Item.Properties()
                            .tab(ModItemGroups.ore_products_tab), boilingPoint, meltingPoint, symbol));
                    DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","centrifuged_ores/" + name)).addValue(centrifugedOre.getId()));
                    DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","centrifuged_ores")).addValue(centrifugedOre.getId()));
                    AssetPackRegistry.itemModelDataHashMap.put("centrifuged_" + name + "_ore", new ItemModelData().setParent(new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName() + "/centrifuged_ore")));
                    AssetPackRegistry.itemModelDataHashMap.put(name + ":centrifuged_ore", new ItemModelData().setParentFolder("/material_icons/" + textureIcon.getSecond().getName().toLowerCase()).setParent(new ResourceLocation("item/generated"))
                            .setTexturesLocation("layer0", new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName().toLowerCase() + "/centrifuged_ore"))
                            .setTexturesLocation("layer1",  new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName().toLowerCase() + "/centrifuged_ore_overlay")));
                    enLang.addTranslation("item." + Ref.mod_id + "." + "centrifuged_" + name + "_ore", LangData.translateUpperCase("centrifuged_" + name + "_ore"));
                    //--//
                }

                if(flag == GEM){
                    //--//
                    gem = RegistryHandler.ITEMS.register(name + "_gem", ()-> new GemItem(new Item.Properties()
                            .tab(ModItemGroups.gem_tab), symbol));
                    DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","gems/" + name)).addValue(gem.getId()));
                    DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","gems")).addValue(gem.getId()));
                    AssetPackRegistry.itemModelDataHashMap.put(name + "_gem", new ItemModelData().setParent(new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName() + "/gem")));
                    AssetPackRegistry.itemModelDataHashMap.put(name + ":gem", new ItemModelData().setParentFolder("/material_icons/" + textureIcon.getSecond().getName().toLowerCase()).setParent(new ResourceLocation("item/generated"))
                            .setTexturesLocation("layer0", new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName().toLowerCase() + "/gem"))
                            .setTexturesLocation("layer1",  new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName().toLowerCase() + "/gem_overlay")));
                    enLang.addTranslation("item." + Ref.mod_id + "." + name + "_gem", LangData.translateUpperCase(name + "_gem"));
                    //--//
                    chippedGem = RegistryHandler.ITEMS.register("chipped_" + name + "_gem", ()-> new GemItem(new Item.Properties()
                            .tab(ModItemGroups.gem_tab), symbol));
                    DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","chipped_gems/" + name)).addValue(chippedGem.getId()));
                    DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","chipped_gems")).addValue(chippedGem.getId()));
                    AssetPackRegistry.itemModelDataHashMap.put("chipped_" + name + "_gem", new ItemModelData().setParent(new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName() + "/chipped_gem")));
                    AssetPackRegistry.itemModelDataHashMap.put(name + ":chipped_gem", new ItemModelData().setParentFolder("/material_icons/" + textureIcon.getSecond().getName().toLowerCase()).setParent(new ResourceLocation("item/generated"))
                            .setTexturesLocation("layer0", new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName().toLowerCase() + "/chipped_gem"))
                            .setTexturesLocation("layer1",  new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName().toLowerCase() + "/chipped_gem_overlay")));
                    enLang.addTranslation("item." + Ref.mod_id + "." + "chipped_" + name + "_gem", LangData.translateUpperCase("chipped_" + name + "_gem"));
                    //--//
                    flawedGem = RegistryHandler.ITEMS.register("flawed_" + name + "_gem", ()-> new GemItem(new Item.Properties()
                            .tab(ModItemGroups.gem_tab), symbol));
                    DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","flawed_gems/" + name)).addValue(flawedGem.getId()));
                    DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","flawed_gems")).addValue(flawedGem.getId()));
                    AssetPackRegistry.itemModelDataHashMap.put("flawed_" + name + "_gem", new ItemModelData().setParent(new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName() + "/flawed_gem")));
                    AssetPackRegistry.itemModelDataHashMap.put(name + ":flawed_gem", new ItemModelData().setParentFolder("/material_icons/" + textureIcon.getSecond().getName().toLowerCase()).setParent(new ResourceLocation("item/generated"))
                            .setTexturesLocation("layer0", new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName().toLowerCase() + "/flawed_gem"))
                            .setTexturesLocation("layer1",  new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName().toLowerCase() + "/flawed_gem_overlay")));
                    enLang.addTranslation("item." + Ref.mod_id + "." + "flawed_" + name + "_gem", LangData.translateUpperCase("flawed_" + name + "_gem"));
                    //--//
                    flawlessGem = RegistryHandler.ITEMS.register(  "flawless_" + name + "_gem", ()-> new GemItem(new Item.Properties()
                            .tab(ModItemGroups.gem_tab), symbol));
                    DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","flawless_gems/" + name)).addValue(flawlessGem.getId()));
                    DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","flawless_gems")).addValue(flawlessGem.getId()));
                    AssetPackRegistry.itemModelDataHashMap.put("flawless_" + name + "_gem", new ItemModelData().setParent(new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName() + "/flawless_gem")));
                    AssetPackRegistry.itemModelDataHashMap.put(name + ":flawless_gem", new ItemModelData().setParentFolder("/material_icons/" + textureIcon.getSecond().getName().toLowerCase()).setParent(new ResourceLocation("item/generated"))
                            .setTexturesLocation("layer0", new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName().toLowerCase() + "/flawless_gem"))
                            .setTexturesLocation("layer1",  new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName().toLowerCase() + "/flawless_gem_overlay")));
                    enLang.addTranslation("item." + Ref.mod_id + "." + "flawless_" + name + "_gem", LangData.translateUpperCase("flawless_" + name + "_gem"));
                    //--//
                    legendaryGem = RegistryHandler.ITEMS.register("legendary_" + name + "_gem", ()-> new GemItem(new Item.Properties()
                            .tab(ModItemGroups.gem_tab), symbol));
                    DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","legendary_gems/" + name)).addValue(legendaryGem.getId()));
                    DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","legendary_gems")).addValue(legendaryGem.getId()));
                    AssetPackRegistry.itemModelDataHashMap.put("legendary_" + name + "_gem", new ItemModelData().setParent(new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName() + "/legendary_gem")));
                    AssetPackRegistry.itemModelDataHashMap.put(name + ":legendary_gem", new ItemModelData().setParentFolder("/material_icons/" + textureIcon.getSecond().getName().toLowerCase()).setParent(new ResourceLocation("item/generated"))
                            .setTexturesLocation("layer0", new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName().toLowerCase() + "/legendary_gem"))
                            .setTexturesLocation("layer1",  new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName().toLowerCase() + "/legendary_gem_overlay")));
                    enLang.addTranslation("item." + Ref.mod_id + "." + "legendary_" + name + "_gem", LangData.translateUpperCase("legendary_" + name + "_gem"));
                    //--//
                }

                //--//
                dust = RegistryHandler.ITEMS.register("" + name + "_dust", ()->  new OreProductsItem(new Item.Properties()
                        .tab(ModItemGroups.ore_products_tab), boilingPoint, meltingPoint, symbol));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","dusts/" + name)).addValue(dust.getId()));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","dusts")).addValue(dust.getId()));
                AssetPackRegistry.itemModelDataHashMap.put(name + "_dust", new ItemModelData().setParent(new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName() + "/dust")));
                AssetPackRegistry.itemModelDataHashMap.put(name + ":dust", new ItemModelData().setParentFolder("/material_icons/" + textureIcon.getSecond().getName()).setParent(new ResourceLocation("item/generated"))
                        .setTexturesLocation("layer0", new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName() + "/dust"))
                        .setTexturesLocation("layer1",  new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName() + "/dust_overlay")));
                enLang.addTranslation("item." + Ref.mod_id + "." + name + "_dust", LangData.translateUpperCase(name + "_dust"));
                //--//
                smallDust = RegistryHandler.ITEMS.register("small_" + name + "_dust", ()-> new OreProductsItem(new Item.Properties()
                        .tab(ModItemGroups.ore_products_tab), boilingPoint, meltingPoint, symbol));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","small_dusts/" + name)).addValue(smallDust.getId()));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","small_dusts")).addValue(smallDust.getId()));
                AssetPackRegistry.itemModelDataHashMap.put("small_" + name + "_dust", new ItemModelData().setParent(new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName() + "/small_dust")));
                AssetPackRegistry.itemModelDataHashMap.put(name + ":small_dust", new ItemModelData().setParentFolder("/material_icons/" + textureIcon.getSecond().getName()).setParent(new ResourceLocation("item/generated"))
                        .setTexturesLocation("layer0", new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName() + "/small_dust"))
                        .setTexturesLocation("layer1",  new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName() + "/small_dust_overlay")));
                enLang.addTranslation("item." + Ref.mod_id + "." + "small_" + name + "_dust", LangData.translateUpperCase("small_" + name + "_dust"));
                //--//
                tinyDust = RegistryHandler.ITEMS.register("tiny_" + name + "_dust", ()-> new OreProductsItem(new Item.Properties()
                        .tab(ModItemGroups.ore_products_tab), boilingPoint, meltingPoint, symbol));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","tiny_dusts/" + name)).addValue(tinyDust.getId()));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","tiny_dusts")).addValue(tinyDust.getId()));
                AssetPackRegistry.itemModelDataHashMap.put("tiny_" + name + "_dust", new ItemModelData().setParent(new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName() + "/tiny_dust")));
                AssetPackRegistry.itemModelDataHashMap.put(name + ":tiny_dust", new ItemModelData().setParentFolder("/material_icons/" + textureIcon.getSecond().getName()).setParent(new ResourceLocation("item/generated"))
                        .setTexturesLocation("layer0", new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName() + "/tiny_dust"))
                        .setTexturesLocation("layer1",  new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName() + "/tiny_dust_overlay")));
                enLang.addTranslation("item." + Ref.mod_id + "." + "tiny_" + name + "_dust", LangData.translateUpperCase("tiny_" + name + "_dust"));
                //--//
            }

            if(flag == FLUID || flag == GAS) {
                FluidAttributes.Builder attributes;

                if (flag == FLUID) {
                    attributes = FluidAttributes.builder(new ResourceLocation( "fluid/" + name), new ResourceLocation( "fluid/" + name + "_flowing")).temperature(meltingPoint).color(color);
                    if(fluidDensity != null) attributes.density(fluidDensity);
                    if(fluidLuminosity != null)attributes.luminosity(fluidLuminosity);
                    if(fluidViscosity != null) attributes.viscosity(fluidViscosity);
                    ForgeFlowingFluid.Properties properties = new ForgeFlowingFluid.Properties(() -> fluid.get(), () -> fluid_flowing.get(), attributes);
                    fluid = RegistryHandler.FLUIDS.register( name + "_still", ()-> new ITFluid(properties, true, color));
                    fluid_flowing = RegistryHandler.FLUIDS.register( name + "_flowing", ()-> new ITFluid(properties, false, color));

                    fluidBlock = RegistryHandler.BLOCKS.register( name, ()-> new ITFluidBlock(()-> fluid.get(), fluidAcceleration, color));
                }

                if (flag == GAS) {
                    attributes = FluidAttributes.builder(new ResourceLocation( "fluid/" + name), new ResourceLocation( "fluid/" + name)).temperature(boilingPoint).color(color).gaseous();
                    if(fluidDensity != null) attributes.density(fluidDensity);
                    if(fluidLuminosity != null)attributes.luminosity(fluidLuminosity);
                    if(fluidViscosity != null) attributes.viscosity(fluidViscosity);
                    ForgeFlowingFluid.Properties properties = new ForgeFlowingFluid.Properties(() -> fluid.get(), () -> fluid_flowing.get(), attributes);
                    fluid = RegistryHandler.FLUIDS.register( name + "_still", ()-> new ITFluid(properties, true, color));
                    fluid_flowing = RegistryHandler.FLUIDS.register( name + "_flowing", ()-> new ITFluid(properties, false, color));

                    fluidBlock = RegistryHandler.BLOCKS.register( name, ()-> new ITFluidBlock(()-> fluid.get(), fluidAcceleration, color));
                }
            }

            if(flag == INGOT){
                //--//
                ingot = RegistryHandler.ITEMS.register( name + "_" + INGOT.name(), ()->  new IngotItem(new Item.Properties().tab(ModItemGroups.item_tab), boilingPoint, meltingPoint, symbol));
                AssetPackRegistry.itemModelDataHashMap.put(name + "_" + INGOT.name(), new ItemModelData().setParent(new ResourceLocation(Ref.mod_id, "/material_icons/" + textureIcon.getSecond().getName() + "/ingot")));
                enLang.addTranslation("item." + Ref.mod_id + "." + name + "_" + INGOT.name(), LangData.translateUpperCase(name + "_" + INGOT.name()));
                //--//
            }
        }

        for(IToolPart flag : toolParts){
            if(flag == DefaultToolPart.TOOL_HEAD){
                //--//
                dullPickaxeHead = RegistryHandler.ITEMS.register( "dull_" + name + "_pickaxe_head", ()-> new DullToolHeadItem(new Item.Properties().tab(ModItemGroups.tool_parts_tab)));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","dull_pickaxe_heads/" + name)).addValue(dullPickaxeHead.getId()));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","dull_pickaxe_heads")).addValue(dullPickaxeHead.getId()));
                AssetPackRegistry.itemModelDataHashMap.put("dull_" + name + "_pickaxe_head", new ItemModelData().setParent(new ResourceLocation("item/generated"))
                        .setTexturesLocation("layer0", new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName() + "/dull_pickaxe_head")));
                enLang.addTranslation("item." + Ref.mod_id + "." + "dull_" + name + "_pickaxe_head", LangData.translateUpperCase("dull_" + name + "_pickaxe_head"));
                //TagHandler.addItemToTag("dull_pickaxe_head", new ResourceLocation(Ref.mod_id, "dull_" + name + "_pickaxe_head"));
                //--//
                pickaxeHead = RegistryHandler.ITEMS.register(  name + "_pickaxe_head", ()-> new ToolHeadItem(new Item.Properties().tab(ModItemGroups.tool_parts_tab), "pickaxe", name, symbol, color, textureIcon, boilingPoint, meltingPoint, attackSpeed, durability, attackDamage, weight, toolTypes));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","pickaxe_heads/" + name)).addValue(pickaxeHead.getId()));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","pickaxe_heads")).addValue(pickaxeHead.getId()));
                AssetPackRegistry.itemModelDataHashMap.put(name + "_pickaxe_head", new ItemModelData().setParent(new ResourceLocation("item/generated"))
                        .setTexturesLocation("layer0", new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName() + "/pickaxe_head")));
                enLang.addTranslation("item." + Ref.mod_id + "." + name + "_pickaxe_head", LangData.translateUpperCase(name + "_pickaxe_head"));
                //--//
                hammerHead = RegistryHandler.ITEMS.register(  name + "_hammer_head", ()-> new ToolHeadItem(new Item.Properties().tab(ModItemGroups.tool_parts_tab), "hammer", name, symbol, color, textureIcon, boilingPoint, meltingPoint, attackSpeed, durability, attackDamage, weight, toolTypes));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","hammer_heads/" + name)).addValue(hammerHead.getId()));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","hammer_heads")).addValue(hammerHead.getId()));
                AssetPackRegistry.itemModelDataHashMap.put(name + "_hammer_head", new ItemModelData().setParent(new ResourceLocation("item/generated"))
                        .setTexturesLocation("layer0", new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName() + "/hammer_head")));
                enLang.addTranslation("item." + Ref.mod_id + "." + name + "_hammer_head", LangData.translateUpperCase(name + "_hammer_head"));
                //--//
                dullChiselHead = RegistryHandler.ITEMS.register( "dull_" + name + "_chisel_head", ()-> new DullToolHeadItem(new Item.Properties().tab(ModItemGroups.tool_parts_tab)));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","dull_chisel_heads/" + name)).addValue(dullChiselHead.getId()));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","dull_chisel_heads")).addValue(dullChiselHead.getId()));
                AssetPackRegistry.itemModelDataHashMap.put("dull_" + name + "_chisel_head", new ItemModelData().setParent(new ResourceLocation("item/generated"))
                        .setTexturesLocation("layer0", new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName() + "/dull_chisel_head")));
                enLang.addTranslation("item." + Ref.mod_id + "." + "dull_" + name + "_chisel_head", LangData.translateUpperCase("dull_" + name + "_chisel_head"));
                //--//
                chiselHead = RegistryHandler.ITEMS.register(  name + "_chisel_head", ()-> new ToolHeadItem(new Item.Properties().tab(ModItemGroups.tool_parts_tab), "chisel", name, symbol, color, textureIcon, boilingPoint, meltingPoint, attackSpeed, durability, attackDamage, weight, toolTypes));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","chisel_heads/" + name)).addValue(chiselHead.getId()));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","chisel_heads")).addValue(chiselHead.getId()));
                AssetPackRegistry.itemModelDataHashMap.put(name + "_chisel_head", new ItemModelData().setParent(new ResourceLocation("item/generated"))
                        .setTexturesLocation("layer0", new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName() + "/chisel_head")));
                enLang.addTranslation("item." + Ref.mod_id + "." + name + "_chisel_head", LangData.translateUpperCase(name + "_chisel_head"));
                //--//
            }

            if(flag == DefaultToolPart.TOOL_WEDGE){
                //--//
                wedge = RegistryHandler.ITEMS.register(  name + "_wedge", ()->  new ToolBindingItem(new Item.Properties().tab(ModItemGroups.tool_parts_tab), name, symbol, color, textureIcon, boilingPoint, meltingPoint, durability, weight));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","wedges/" + name)).addValue(wedge.getId()));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","wedges")).addValue(wedge.getId()));
                AssetPackRegistry.itemModelDataHashMap.put(name + "_wedge", new ItemModelData().setParent(new ResourceLocation("item/generated")).setTexturesLocation("layer0", new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName() + "/wedge")));
                enLang.addTranslation("item." + Ref.mod_id + "." + name + "_wedge", LangData.translateUpperCase(name + "_wedge"));
                //--//
            }

            if(flag == DefaultToolPart.TOOL_WEDGE_HANDLE){
                //--//
                wedgeHandle = RegistryHandler.ITEMS.register(  name + "_wedge_handle", ()-> new ToolHandleItem(new Item.Properties().tab(ModItemGroups.tool_parts_tab), name, symbol, color, textureIcon, boilingPoint, meltingPoint, durability, weight));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","wedge_handles/" + name)).addValue(wedgeHandle.getId()));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge","wedge_handles")).addValue(wedgeHandle.getId()));
                AssetPackRegistry.itemModelDataHashMap.put(name + "_wedge_handle", new ItemModelData().setParent(new ResourceLocation("item/generated")).setTexturesLocation("layer0", new ResourceLocation(Ref.mod_id, "item/material_icons/" + textureIcon.getSecond().getName() + "/wedge_handle")));
                enLang.addTranslation("item." + Ref.mod_id + "." + name + "_wedge_handle", LangData.translateUpperCase(name + "_wedge_handle"));
                //--//
            }
        }

        AssetPackRegistry.langDataMap.put("en_us", enLang);

        return this;
    }

    public void clientRenderLayerInit(){
        if(rockBlock != null) {
            ItemBlockRenderTypes.setRenderLayer(rockBlock.get(), RenderType.cutout());
        }

        if(thinSlabBlock != null) {
            ItemBlockRenderTypes.setRenderLayer(thinSlabBlock.get(), RenderType.cutout());
        }
    }

    public void registerColorForBlock() {
        for (RegistryObject<Block> block : oreList) {
            setupABlockColor(block.get());
        }
        for (RegistryObject<Block> block : smallOreList) {
            setupABlockColor(block.get());
        }
        for (RegistryObject<Block> block : denseOreList) {
            setupABlockColor(block.get());
        }

        if(rockBlock != null) {
            setupABlockColor(rockBlock.get());
        }
    }

    public void setupABlockColor(Block block){
        ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout());
        Minecraft.getInstance().getBlockColors().register((state, world, pos, tintIndex) -> {
            if (tintIndex != 0)
                return 0xFFFFFFFF;
            return color;
        }, block);
    }

    public void registerColorForItem(){
        if(rockItemBlock != null) {
            registerAItemColor(rockItemBlock.get(), 0);
            registerAItemColor(rockItemBlock.get(), 1);
        }

        for (RegistryObject<Item> item : oreItemList) {
            Minecraft.getInstance().getItemColors().register((stack, tintIndex) -> {
                if (tintIndex != 0)
                    return 0xFFFFFFFF;
                return color;
            }, item.get());
        }
        for (RegistryObject<Item> item : smallOreItemList) {
            Minecraft.getInstance().getItemColors().register((stack, tintIndex) -> {
                if (tintIndex != 0)
                    return 0xFFFFFFFF;
                return color;
            }, item.get());
        }

        for (RegistryObject<Item> item : denseOreItemList) {
            Minecraft.getInstance().getItemColors().register((stack, tintIndex) -> {
                if (tintIndex != 0)
                    return 0xFFFFFFFF;
                return color;
            }, item.get());
        }

        if(dust != null) {
            registerAItemColor(dust.get(), 0);
        }

        if(smallDust != null) {
            registerAItemColor(smallDust.get(), 0);
        }

        if(tinyDust != null) {
            registerAItemColor(tinyDust.get(), 0);
        }
        if(ingot != null) {
            registerAItemColor(ingot.get(), 0);
        }
        if(gem != null) {
            registerAItemColor(gem.get(), 0);
        }
        if(chippedGem != null) {
            registerAItemColor(chippedGem.get(), 0);
        }
        if(flawedGem != null) {
            registerAItemColor(flawedGem.get(), 0);
        }
        if(flawlessGem != null) {
            registerAItemColor(flawlessGem.get(), 0);
        }
        if(legendaryGem != null) {
            registerAItemColor(legendaryGem.get(), 0);
        }
        if(crushedOre != null) {
            registerAItemColor(crushedOre.get(), 0);
        }
        if(purifiedOre != null) {
            registerAItemColor(purifiedOre.get(), 0);
        }
        if(centrifugedOre != null) {
            registerAItemColor(centrifugedOre.get(), 0);
        }
        if(dullPickaxeHead != null) {
            registerAItemColor(dullPickaxeHead.get(), 0);
        }
        if(pickaxeHead != null) {
            registerAItemColor(pickaxeHead.get(), 0);
        }
        if(hammerHead != null) {
            registerAItemColor(hammerHead.get(), 0);
        }
        if(wedge != null) {
            registerAItemColor(wedge.get(), 0);
        }
        if(wedgeHandle != null) {
            registerAItemColor(wedgeHandle.get(), 0);
        }
        if(dullChiselHead != null) {
            registerAItemColor(dullChiselHead.get(), 0);
        }
        if(chiselHead != null) {
            registerAItemColor(chiselHead.get(), 0);
        }
    }

    public void registerAItemColor(Item item, int layerNumberIn){
        if(item != null) {
            Minecraft.getInstance().getItemColors().register((stack, tintIndex) -> {
                if (tintIndex == layerNumberIn)
                    return color;
                else
                    return 0xFFFFFFFF;
            }, item);
        }
    }



    /*
    public int ColorToInt() {
        int ret = 0;
        ret += red; ret = ret << 8; ret += green; ret = ret << 8; ret += blue;
        return ret;
    }
     */
}