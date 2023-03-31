package mightydanp.techcore.common.tree;

import mightydanp.techapi.common.resources.asset.AssetPackRegistry;
import mightydanp.techapi.common.resources.asset.data.BlockStateData;
import mightydanp.techapi.common.resources.asset.data.LangData;
import mightydanp.techapi.common.resources.asset.data.ModelData;
import mightydanp.techapi.common.resources.asset.data.TAModelBuilder;
import mightydanp.techapi.common.resources.data.DataPackRegistry;
import mightydanp.techcore.common.handler.RegistryHandler;
import mightydanp.techcore.common.items.TCCreativeModeTab;
import mightydanp.techcore.common.libs.Ref;
import mightydanp.techcore.common.tree.blocks.TCLogBlock;
import mightydanp.techcore.common.tree.blocks.items.TCLogBlockItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

public class TCTree {
    public String name;
    public int barkColor;

    public int woodColor;
    public int leavesColor;

    public Material material;
    public SoundType soundType;
    public float destroyTimeMultiplier;
    public float explosionResistanceMultiplier;

    public AbstractTreeGrower treeGrower;

    public Map<String, ResourceLocation> existingBlocks;
    public Map<String, ResourceLocation> existingItems;

    public RegistryObject<Block> log, stripedLog, planks, leaves, sapling, slab, stairs, button, fence, fenceGate, door, trapDoor, pressurePlate, sign;
    public RegistryObject<Item> stick, plank, resin;//boat-cant do because it requires plank block. If someone adds a plank that's not in this class then there is no plank for it to use because it cant grab it from registry.



    public TCTree(String name, int barkColor, int woodColor, int leavesColor, Material material, SoundType soundType, float destroyTimeMultiplier, float explosionResistanceMultiplier, AbstractTreeGrower treeGrower){
        this.name = name;
        this.barkColor = barkColor;
        this.woodColor = woodColor;
        this.leavesColor = leavesColor;
        this.material = material;
        this.soundType = soundType;
        this.destroyTimeMultiplier = destroyTimeMultiplier;
        this.explosionResistanceMultiplier = explosionResistanceMultiplier;
        this.treeGrower = treeGrower;
    }

    public TCTree existingBlock(String process, ResourceLocation blockResourceLocation){
        existingBlocks.put(process, blockResourceLocation);
         return this;
    }

    public TCTree existingItem(String process, ResourceLocation itemResourceLocation){
        existingItems.put(process, itemResourceLocation);
        return this;
    }

    public void save() {
        //-- Item --\\
        {
            String name = this.name + "_stick";

            if (!existingItems.containsKey("stick")) {
                stick = RegistryHandler.ITEMS.register(name, () -> new Item(new Item.Properties().tab(TCCreativeModeTab.tree_tab)));
            }
        }
//--//--//--//--//--//--//--//--//
        {
            String name = this.name + "_plank";

            if (!existingItems.containsKey("plank")) {
                plank = RegistryHandler.ITEMS.register(name, () -> new Item(new Item.Properties().tab(TCCreativeModeTab.tree_tab)));
            }
        }
//--//--//--//--//--//--//--//--//
        //-- Blocks with Items -- \\
        {
            //--block
            String name = this.name + "_sapling";
            Block block = new SaplingBlock(treeGrower, BlockBehaviour.Properties.of(material).strength(2.0F * destroyTimeMultiplier, 2.0F * explosionResistanceMultiplier).sound(soundType));

            if (!existingBlocks.containsKey("sapling")) {
                sapling = RegistryHandler.BLOCKS.register(name, () -> block);
            }

            //--item
            if (!existingItems.containsKey("sapling")) {
                RegistryHandler.ITEMS.register(name, () -> new BlockItem(block, new Item.Properties().tab(TCCreativeModeTab.tree_tab)));
            }
        }
//--//--//--//--//--//--//--//--//
        {
            //--block
            String name = this.name + "_log";
            if (!existingBlocks.containsKey("log")) {
                log = RegistryHandler.BLOCKS.register(name, () -> new TCLogBlock(BlockBehaviour.Properties.of(material).strength(2.0F * destroyTimeMultiplier, 2.0F * explosionResistanceMultiplier).sound(soundType)));
            }

            //--item
            if (!existingItems.containsKey("log")) {
                RegistryHandler.ITEMS.register(name, () -> new TCLogBlockItem(log, new Item.Properties().tab(TCCreativeModeTab.tree_tab)));
            }
        }
//--//--//--//--//--//--//--//--//
        {
            //--block
            String name = "striped_" + this.name + "_log";
            if (!existingBlocks.containsKey("striped_log")) {
                stripedLog = RegistryHandler.BLOCKS.register(name, () -> new TCLogBlock(BlockBehaviour.Properties.of(material).strength(2.0F * destroyTimeMultiplier, 2.0F * explosionResistanceMultiplier).sound(soundType)));
            }

            //--item
            if (!existingItems.containsKey("striped_log")) {
                RegistryHandler.ITEMS.register(name, () -> new TCLogBlockItem(stripedLog, new Item.Properties().tab(TCCreativeModeTab.tree_tab)));
            }
        }
//--//--//--//--//--//--//--//--//
        {
            //--block
            String name = this.name + "_leaves";
            Block block = new LeavesBlock(BlockBehaviour.Properties.of(material).strength(0.2F  * destroyTimeMultiplier, 0.2F * explosionResistanceMultiplier).randomTicks().sound(soundType).noOcclusion().isValidSpawn(Blocks::ocelotOrParrot).isSuffocating(Blocks::never).isViewBlocking(Blocks::never));
            if (!existingBlocks.containsKey("leaves")) {
                leaves = RegistryHandler.BLOCKS.register(name, () -> block);
            }

            //--item
            if (!existingItems.containsKey("leaves")) {
                RegistryHandler.ITEMS.register(name, () -> new BlockItem(block, new Item.Properties().tab(TCCreativeModeTab.tree_tab)));
            }
        }
//--//--//--//--//--//--//--//--//
        {
            //--block
            String name = this.name + "_planks";
            Block block = new Block(BlockBehaviour.Properties.of(material).strength(2.0F * destroyTimeMultiplier, 2.0F * explosionResistanceMultiplier).sound(soundType));
            if (!existingItems.containsKey("planks")) {
                planks = RegistryHandler.BLOCKS.register(name, () -> block);
            }

            //--item
            if (!existingItems.containsKey("planks")) {
                RegistryHandler.ITEMS.register(name, () -> new BlockItem(block, new Item.Properties().tab(TCCreativeModeTab.tree_tab)));
            }
        }
//--//--//--//--//--//--//--//--//
        {
            //--block
            String name = this.name + "_slab";
            Block block = new SlabBlock(BlockBehaviour.Properties.of(material).strength(2.0F * destroyTimeMultiplier, 3.0F * explosionResistanceMultiplier).sound(soundType));
            if (!existingItems.containsKey("slab")) {
                slab = RegistryHandler.BLOCKS.register(name, () -> block);
            }

            //--item
            if (!existingItems.containsKey("slab")) {
                RegistryHandler.ITEMS.register(name, () -> new BlockItem(block, new Item.Properties().tab(TCCreativeModeTab.tree_tab)));
            }
        }
//--//--//--//--//--//--//--//--//
        {
            //--block
            String name = this.name + "_stairs";
            Block block = new StairBlock(()-> null, BlockBehaviour.Properties.of(material).strength(2.0F * destroyTimeMultiplier, 3.0F * explosionResistanceMultiplier).sound(soundType));
            if (!existingItems.containsKey("stairs")) {
                stairs = RegistryHandler.BLOCKS.register(name, () -> block);
            }

            //--item
            if (!existingItems.containsKey("stairs")) {
                RegistryHandler.ITEMS.register(name, () -> new BlockItem(block, new Item.Properties().tab(TCCreativeModeTab.tree_tab)));
            }
        }
//--//--//--//--//--//--//--//--//
        {
            //--block
            String name = this.name + "_button";
            Block block = new WoodButtonBlock(BlockBehaviour.Properties.of(material).noCollission().strength(0.5F * destroyTimeMultiplier, 0.5F * explosionResistanceMultiplier).sound(soundType));
            if (!existingItems.containsKey("button")) {
                button = RegistryHandler.BLOCKS.register(name, () -> block);
            }

            //--item
            if (!existingItems.containsKey("button")) {
                RegistryHandler.ITEMS.register(name, () -> new BlockItem(block, new Item.Properties().tab(TCCreativeModeTab.tree_tab)));
            }
        }
//--//--//--//--//--//--//--//--//
        {
            //--block
            String name = this.name + "_fence";
            Block block = new FenceBlock(BlockBehaviour.Properties.of(material).strength(2.0F * destroyTimeMultiplier, 3.0F * explosionResistanceMultiplier).sound(soundType));
            if (!existingItems.containsKey("fence")) {
                fence = RegistryHandler.BLOCKS.register(name, () -> block);
            }

            //--item
            if (!existingItems.containsKey("fence")) {
                RegistryHandler.ITEMS.register(name, () -> new BlockItem(block, new Item.Properties().tab(TCCreativeModeTab.tree_tab)));
            }
        }
//--//--//--//--//--//--//--//--//
        {
            //--block
            String name = this.name + "_fence_gate";
            Block block = new FenceGateBlock(BlockBehaviour.Properties.of(material).strength(2.0F * destroyTimeMultiplier, 3.0F * explosionResistanceMultiplier).sound(soundType));
            if (!existingItems.containsKey("fence_gate")) {
                fenceGate = RegistryHandler.BLOCKS.register(name, () -> block);
            }

            //--item
            if (!existingItems.containsKey("fence_gate")) {
                RegistryHandler.ITEMS.register(name, () -> new BlockItem(block, new Item.Properties().tab(TCCreativeModeTab.tree_tab)));
            }
        }
//--//--//--//--//--//--//--//--//
        {
            //--block
            String name = this.name + "_door";
            Block block = new DoorBlock(BlockBehaviour.Properties.of(material).noOcclusion().strength(3.0F * destroyTimeMultiplier, 3.0F * explosionResistanceMultiplier).sound(soundType));
            if (!existingItems.containsKey("door")) {
                door = RegistryHandler.BLOCKS.register(name, () -> block);
            }

            //--item
            if (!existingItems.containsKey("door")) {
                RegistryHandler.ITEMS.register(name, () -> new BlockItem(block, new Item.Properties().tab(TCCreativeModeTab.tree_tab)));
            }
        }
//--//--//--//--//--//--//--//--//
        {
            //--block
            String name = this.name + "_trap_door";
            Block block = new TrapDoorBlock(BlockBehaviour.Properties.of(material).strength(2.0F * destroyTimeMultiplier, 3.0F * explosionResistanceMultiplier).sound(soundType).noOcclusion().isValidSpawn(Blocks::never));
            if (!existingItems.containsKey("trap_door")) {
                trapDoor = RegistryHandler.BLOCKS.register(name, () -> block);
            }

            //--item
            if (!existingItems.containsKey("trap_door")) {
                RegistryHandler.ITEMS.register(name, () -> new BlockItem(block, new Item.Properties().tab(TCCreativeModeTab.tree_tab)));
            }
        }
//--//--//--//--//--//--//--//--//
        {
            //--block
            String name = this.name + "_pressure_plate";
            Block block = new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.of(material).strength(0.5F * destroyTimeMultiplier, 0.5F * explosionResistanceMultiplier).sound(soundType).noCollission());
            if (!existingItems.containsKey("pressure_plate")) {
                pressurePlate = RegistryHandler.BLOCKS.register(name, () -> block);
            }

            //--item
            if (!existingItems.containsKey("pressure_plate")) {
                RegistryHandler.ITEMS.register(name, () -> new BlockItem(block, new Item.Properties().tab(TCCreativeModeTab.tree_tab)));
            }
        }
//--//--//--//--//--//--//--//--//
        {
            //--block
            String name = this.name + "_sign";
            Block block = new StandingSignBlock(BlockBehaviour.Properties.of(material).strength(destroyTimeMultiplier, explosionResistanceMultiplier).sound(soundType).noCollission(), WoodType.register(WoodType.create(name)));
            if (!existingItems.containsKey("sign")) {
                sign = RegistryHandler.BLOCKS.register(name, () -> block);
            }

            //--item
            if (!existingItems.containsKey("sign")) {
                RegistryHandler.ITEMS.register(name, () -> new BlockItem(block, new Item.Properties().tab(TCCreativeModeTab.tree_tab)));
            }
        }
//--//--//--//--//--//--//--//--//
        {
            String name = this.name + "_resin";

            if (!existingItems.containsKey("resin")) {
                resin = RegistryHandler.ITEMS.register(name, () -> new Item(new Item.Properties().tab(TCCreativeModeTab.tree_tab)));
            }
        }
    }
    public void saveResources() throws Exception {
        LangData enLang = AssetPackRegistry.langDataMap.getOrDefault("en_us", new LangData());

        {
            String category = "stick";
            String name = this.name + "_" + category;

            if (!existingItems.containsKey(category)) {
                //--Resources
                {
                    ModelData model = new ModelData(name, ModelData.ITEM_FOLDER, null);
                    model.getModel().parent(TAModelBuilder.ExistingItemModels.item_generated.model)
                            .texture("layer0", new ResourceLocation(Ref.mod_id, "item/tree_icons/" + "stick")
                    );

                    AssetPackRegistry.itemModelDataHashMap.put(name, model);
                }
                enLang.addTranslation("item." + Ref.mod_id + "." + name, LangData.translateUpperCase(name));
                //--Tags
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge", "rods/" + this.name)).add(stick.get()));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge", "rods")).add(stick.get()));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge", "rods/wooden")).add(stick.get()));
                //--LootTable
            }
        }
//--//--//--//--//--//--//--//--//
        /*
        {
            String category = "plank";
            String name = this.name + "_" + category;


            if (!existingItems.containsKey(category)) {
                //--Resources
                {
                    ModelData model = new ModelData(name, ModelData.ITEM_FOLDER, null);
                    model.overrideModel(model.getModel().parent(TAModelBuilder.ExistingItemModels.item_generated.model)
                            .texture("layer0", new ResourceLocation(Ref.mod_id, "item/tree_icons/" + "plank"))
                    );
                    AssetPackRegistry.itemModelDataHashMap.put(name, model);
                }
                enLang.addTranslation("item." + Ref.mod_id + "." + name, LangData.translateUpperCase(name));
                //--Tags
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge", "plank/" + this.name)).add(plank.get()));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge", "plank")).add(plank.get()));
                //--LootTable
            }
        }
        */
//--//--//--//--//--//--//--//--//
        {
            String category = "sapling";
            String name = this.name + "_" + category;

            //--block
            if (!existingBlocks.containsKey(category)) {
                //--Resources
                BlockStateData data = new BlockStateData();
                VariantBlockStateBuilder builder;
                try {
                    builder = data.getVariantBuilder(sapling.get());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                {
                    ModelData modelData = new ModelData(name, ModelData.BLOCK_FOLDER, "tree_icons/" + this.name);
                    modelData.taSaplingCross(new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name + "/" + category + "_wood"), new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name + "/" + category + "_leaves"));

                    AssetPackRegistry.blockModelDataMap.put(name, modelData);

                    builder.partialState().setModels(new ConfiguredModel(modelData.getModel()));
                    data.setBlockState(builder);
                    AssetPackRegistry.blockStateDataMap.put(name, data);
                }
                enLang.addTranslation("block." + Ref.mod_id + "." + name, LangData.translateUpperCase(name));

                //--Tags
                DataPackRegistry.saveBlockTagData(DataPackRegistry.getBlockTagData(new ResourceLocation("forge", "planks/" + this.name)).add(planks.get()));
                DataPackRegistry.saveBlockTagData(DataPackRegistry.getBlockTagData(new ResourceLocation("forge", "planks/")).add(planks.get()));
            }

            //--item
            if (!existingItems.containsKey(category)) {
                //--Resources
                ModelData modelData = new ModelData(name, ModelData.ITEM_FOLDER, null);
                modelData.getModel().setParent(AssetPackRegistry.blockModelDataMap.get(name).getModel());
                AssetPackRegistry.itemModelDataHashMap.put(name, modelData);

                enLang.addTranslation("item." + Ref.mod_id + "." + name, LangData.translateUpperCase(name));

                //--Tags
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge", "planks/" + this.name)).add(planks.get().asItem()));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge", "planks/")).add(planks.get().asItem()));
            }
        }
//--//--//--//--//--//--//--//--//
        {
            //--block
            String category = "log";
            String name = this.name + "_" + category;
            String nameHorizontal = name + "_horizontal";

            if (!existingBlocks.containsKey(category)) {
                BlockStateData data = new BlockStateData();

                ModelData logModel = new ModelData(name, ModelData.BLOCK_FOLDER, "tree_icons/" + this.name);
                logModel.taLog( false, new ResourceLocation(Ref.mod_id,
                                "block/tree_icons/" + this.name + "/" + category + "side"),
                        new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name + "/" + category + "_bark"),
                        new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name + "/" + category + "_wood"));
                AssetPackRegistry.blockModelDataMap.put(name, logModel);

                ModelData logHorizontalModel = new ModelData(nameHorizontal, ModelData.BLOCK_FOLDER, "tree_icons/" + this.name);

                logHorizontalModel.taLog(true, new ResourceLocation(Ref.mod_id,
                                "block/tree_icons/" + this.name + "/" + category + "side"),
                        new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name + "/" + category + "_bark"),
                        new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name + "/" + category + "_wood"));

                AssetPackRegistry.blockModelDataMap.put(nameHorizontal, logHorizontalModel);

                data.axisBlock((RotatedPillarBlock)log.get(), logModel.getModel(), logHorizontalModel.getModel());

                enLang.addTranslation("block." + Ref.mod_id + "." + name, LangData.translateUpperCase(name));

                //--Tags
                DataPackRegistry.saveBlockTagData(DataPackRegistry.getBlockTagData(new ResourceLocation("forge", "logs/" + this.name)).add(log.get()));
                DataPackRegistry.saveBlockTagData(DataPackRegistry.getBlockTagData(new ResourceLocation("forge", "logs/")).add(log.get()));
                DataPackRegistry.saveBlockTagData(DataPackRegistry.getBlockTagData(new ResourceLocation("forge", "logs_that_burn/")).add(log.get()));
            }

            //--item
            if (!existingItems.containsKey(category)) {
                ModelData modelData = new ModelData(name, ModelData.ITEM_FOLDER, null);
                modelData.getModel().setParent(AssetPackRegistry.blockModelDataMap.get(name).getModel());
                AssetPackRegistry.itemModelDataHashMap.put(name, modelData);

                enLang.addTranslation("item." + Ref.mod_id + "." + name, LangData.translateUpperCase(name));

                //--Tags
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge", "logs/" + this.name)).add(log.get().asItem()));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge", "logs/")).add(log.get().asItem()));
            }
        }
//--//--//--//--//--//--//--//--//
        {
            //--block
            String categoryPrefix = "striped";
            String categorySuffix = "log";
            String category = categoryPrefix + "_" + categorySuffix;

            String name = categoryPrefix + "_" + this.name + "_" + categorySuffix;
            String nameHorizontal = name + "_horizontal";

            if (!existingBlocks.containsKey(category)) {
                BlockStateData data = new BlockStateData();

                ModelData logModel = new ModelData(name, ModelData.BLOCK_FOLDER, "tree_icons/" + this.name);
                logModel.taLog(false, new ResourceLocation(Ref.mod_id,
                                "block/tree_icons/" + this.name + "/" + category + "side"),
                        new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name + "/" + category + "_bark"),
                        new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name + "/" + category + "_wood"));

                AssetPackRegistry.blockModelDataMap.put(name, logModel);

                ModelData logHorizontalModel = new ModelData(nameHorizontal, ModelData.BLOCK_FOLDER, "tree_icons/" + this.name);

                logHorizontalModel.taLog(true, new ResourceLocation(Ref.mod_id,
                                "block/tree_icons/" + this.name + "/" + category + "side"),
                        new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name + "/" + category + "_bark"),
                        new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name + "/" + category + "_wood"));

                AssetPackRegistry.blockModelDataMap.put(nameHorizontal, logHorizontalModel);

                data.axisBlock((RotatedPillarBlock)stripedLog.get(), logModel.getModel(), logHorizontalModel.getModel());

                enLang.addTranslation("block." + Ref.mod_id + "." + name, LangData.translateUpperCase(name));

                //--Tags
                DataPackRegistry.saveBlockTagData(DataPackRegistry.getBlockTagData(new ResourceLocation("forge", "logs/" + this.name)).add(stripedLog.get()));
                DataPackRegistry.saveBlockTagData(DataPackRegistry.getBlockTagData(new ResourceLocation("forge", "logs/")).add(stripedLog.get()));
                DataPackRegistry.saveBlockTagData(DataPackRegistry.getBlockTagData(new ResourceLocation("forge", "logs_that_burn/")).add(stripedLog.get()));
            }

            //--item
            if (!existingItems.containsKey(category)) {
                ModelData modelData = new ModelData(name, ModelData.ITEM_FOLDER, null);
                modelData.getModel().setParent(AssetPackRegistry.blockModelDataMap.get(name).getModel());
                AssetPackRegistry.itemModelDataHashMap.put(name, modelData);

                enLang.addTranslation("item." + Ref.mod_id + "." + name, LangData.translateUpperCase(name));

                //--Tags
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge", "logs/" + this.name)).add(stripedLog.get().asItem()));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge", "logs/")).add(stripedLog.get().asItem()));
            }
        }
//--//--//--//--//--//--//--//--//
        {
            //--block

            String category = "leaves";
            String name = this.name + "_" + category;

            if (!existingBlocks.containsKey(category)) {
                BlockStateData data = new BlockStateData();
                VariantBlockStateBuilder builder = data.getVariantBuilder(log.get());
                ModelData modelData = new ModelData(name, ModelData.BLOCK_FOLDER, "tree_icons/" + this.name);

                modelData.getModel().setParent(TAModelBuilder.ExistingBlockModels.leaves.model).texture("all", new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name + "/" + category));
                builder.partialState().addModels(new ConfiguredModel(modelData.getModel()));

                AssetPackRegistry.blockModelDataMap.put(name, modelData);
                AssetPackRegistry.blockStateDataMap.put(name, data);

                enLang.addTranslation("block." + Ref.mod_id + "." + name, LangData.translateUpperCase(name));


            }

            //--item
            if (!existingItems.containsKey(category)) {
                ModelData modelData = new ModelData(name, ModelData.ITEM_FOLDER, null);
                modelData.getModel().setParent(AssetPackRegistry.blockModelDataMap.get(name).getModel());
                AssetPackRegistry.itemModelDataHashMap.put(name, modelData);

                enLang.addTranslation("item." + Ref.mod_id + "." + name, LangData.translateUpperCase(name));
            }

        }
//--//--//--//--//--//--//--//--//
        {
            String category = "planks";
            String name = this.name + "_" + category;

            if (!existingBlocks.containsKey("planks")) {
                //--Resources
                BlockStateData data = new BlockStateData();
                VariantBlockStateBuilder builder = data.getVariantBuilder(planks.get());
                {
                    ModelData model = new ModelData(name, ModelData.BLOCK_FOLDER, "tree_icons/" + this.name);
                    model.getModel().setParent(TAModelBuilder.ExistingBlockModels.cube_all.model);

                    builder.partialState().setModels(new ConfiguredModel(model.getModel()));
                    data.setBlockState(builder);

                    AssetPackRegistry.blockModelDataMap.put(name, model);
                    AssetPackRegistry.blockStateDataMap.put(name, data);

                }
                enLang.addTranslation("block." + Ref.mod_id + "." + name, LangData.translateUpperCase(name));

                //--Tags
                DataPackRegistry.saveBlockTagData(DataPackRegistry.getBlockTagData(new ResourceLocation("forge", "planks/" + this.name)).add(planks.get()));
                DataPackRegistry.saveBlockTagData(DataPackRegistry.getBlockTagData(new ResourceLocation("forge", "planks/")).add(planks.get()));
            }

            if (!existingItems.containsKey("planks")) {
                //--Resources
                {
                    ModelData model = new ModelData(name, ModelData.ITEM_FOLDER, "tree_icons/" + category);
                    model.getModel().setParent(AssetPackRegistry.blockModelDataMap.get(name).getModel());
                    AssetPackRegistry.itemModelDataHashMap.put(name, model);
                }
                enLang.addTranslation("item." + Ref.mod_id + "." + name, LangData.translateUpperCase(name));
                //--Tags
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge", "planks/" + this.name)).add(planks.get().asItem()));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge", "planks")).add(planks.get().asItem()));
                //--LootTable
            }
        }
//--//--//--//--//--//--//--//--//
        {
            String category = "slab";
            String name = this.name + "_" + category;
            String topName = "top_" +  this.name + "_" + category;

            //--block
            if (!existingItems.containsKey(category)) {
                ModelData modelDataSlab = new ModelData(name, ModelData.BLOCK_FOLDER, "tree_icons/" + this.name);
                modelDataSlab.taTintSlab(
                        new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name +"/planks"),
                        new ResourceLocation(Ref.mod_id,"block/tree_icons/" + this.name +"/planks"),
                        new ResourceLocation(Ref.mod_id,"block/tree_icons/" + this.name +"/planks"));
                AssetPackRegistry.blockModelDataMap.put(name, modelDataSlab);

                enLang.addTranslation("block." + Ref.mod_id + "." + name, LangData.translateUpperCase(name));

                ModelData modelDataTopSlab = new ModelData(topName, ModelData.BLOCK_FOLDER, "tree_icons/" + this.name);
                modelDataTopSlab.taTopTintSlab(
                        new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name +"/planks"),
                        new ResourceLocation(Ref.mod_id,"block/tree_icons/" + this.name +"/planks"),
                        new ResourceLocation(Ref.mod_id,"block/tree_icons/" + this.name +"/planks"));
                AssetPackRegistry.blockModelDataMap.put(topName, modelDataTopSlab);

                enLang.addTranslation("item." + Ref.mod_id + "." + topName, LangData.translateUpperCase(topName));

                BlockStateData data = new BlockStateData();
                //todo
                data.slabBlock((SlabBlock)slab.get(), modelDataSlab.getModel(), modelDataTopSlab.getModel(), AssetPackRegistry.blockModelDataMap.get(this.name + "_" + category + "_planks").getModel());
            }

            //--item
            if (!existingItems.containsKey(category)) {
                //--Resources
                {
                    ModelData model = new ModelData(name, ModelData.ITEM_FOLDER, "tree_icons/" + category);
                    model.getModel().setParent(AssetPackRegistry.blockModelDataMap.get(name).getModel());

                    AssetPackRegistry.itemModelDataHashMap.put(name, model);
                }
                enLang.addTranslation("item." + Ref.mod_id + "." + name, LangData.translateUpperCase(name));
                //--Tags
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge", "planks/" + this.name)).add(planks.get().asItem()));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge", "planks")).add(planks.get().asItem()));
                //--LootTable
            }
        }
//--//--//--//--//--//--//--//--//
        {
            //--block
            String category = "stairs";
            String name = this.name + "_" + category;
            String innerName = "inner_" + this.name + "_" + category;
            String outerName = "outer" + this.name + "_" + category;

            if (!existingItems.containsKey(category)) {
                BlockStateData blockStateData = new BlockStateData();

                ModelData stairsModel = new ModelData(name, ModelData.BLOCK_FOLDER, "tree_icons/" + this.name);
                stairsModel.taStairs(1,
                        new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name +"/planks"),
                        new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name +"/planks"),
                        new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name +"/planks"));

                AssetPackRegistry.blockModelDataMap.put(name, stairsModel);

                ModelData innerStairsModel = new ModelData(innerName, ModelData.BLOCK_FOLDER, "tree_icons/" + this.name);
                innerStairsModel.taInnerStairs(1,
                        new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name +"/planks"),
                        new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name +"/planks"),
                        new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name +"/planks"));

                AssetPackRegistry.blockModelDataMap.put(innerName, innerStairsModel);

                ModelData outerStairsModel = new ModelData(name, ModelData.BLOCK_FOLDER, "tree_icons/" + this.name);
                outerStairsModel.taOuterStairs(1,
                        new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name +"/planks"),
                        new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name +"/planks"),
                        new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name +"/planks"));

                AssetPackRegistry.blockModelDataMap.put(outerName, outerStairsModel);

                blockStateData.stairsBlock((StairBlock) stairs.get(), stairsModel.getModel(), innerStairsModel.getModel(), outerStairsModel.getModel());

                enLang.addTranslation("block." + Ref.mod_id + "." + name, LangData.translateUpperCase(name));
            }

            //--item
            if (!existingItems.containsKey(category)) {
                //--Resources
                {
                    ModelData model = new ModelData(name, ModelData.ITEM_FOLDER, "tree_icons/" + category);
                    model.getModel().setParent(AssetPackRegistry.blockModelDataMap.get(name).getModel());

                    AssetPackRegistry.itemModelDataHashMap.put(name, model);
                }
                enLang.addTranslation("item." + Ref.mod_id + "." + name, LangData.translateUpperCase(name));
                //--Tags
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge", category + "/" + this.name)).add(stairs.get().asItem()));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge", category)).add(stairs.get().asItem()));
            }
        }
//--//--//--//--//--//--//--//--//
        {
            //--block
            String category = "button";
            String name = this.name + "_" + category;
            String pressedName = "pressed_" + name;
            String inventoryName = name + "_inventory";

            if (!existingItems.containsKey(category)) {
                BlockStateData blockStateData = new BlockStateData();
                ModelData button = new ModelData(name, ModelData.BLOCK_FOLDER, "tree_icons/" + this.name);
                button.taButton(new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name +"/planks"));

                AssetPackRegistry.blockModelDataMap.put(name, button);

                ModelData buttonPressed = new ModelData(pressedName, ModelData.BLOCK_FOLDER, "tree_icons/" + this.name);
                buttonPressed.taButtonPressed(new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name +"/planks"));

                AssetPackRegistry.blockModelDataMap.put(pressedName, buttonPressed);

                blockStateData.buttonBlock((ButtonBlock)this.button.get(), button.getModel(), buttonPressed.getModel());
            }

            //--item
            if (!existingItems.containsKey(category)) {
                //--Resources
                {
                    ModelData buttonInventory = new ModelData(inventoryName, ModelData.BLOCK_FOLDER, "tree_icons/" + this.name);
                    buttonInventory.taButtonInventory(new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name +"/planks"));

                    AssetPackRegistry.blockModelDataMap.put(inventoryName, buttonInventory);

                    ModelData model = new ModelData(name, ModelData.ITEM_FOLDER, "tree_icons/" + category);
                    model.getModel().setParent(buttonInventory.getModel());

                    AssetPackRegistry.itemModelDataHashMap.put(name, model);
                }
                enLang.addTranslation("item." + Ref.mod_id + "." + name, LangData.translateUpperCase(name));
                //--Tags
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge", category + "/" + this.name)).add(button.get().asItem()));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge", category)).add(button.get().asItem()));
            }
        }
//--//--//--//--//--//--//--//--//
        {
            //--block
            String category = "fence";
            String name = this.name + "_" + category;
            String inventoryName = name + "_inventory";
            String postName = name + "_post";
            String sideName = name + "_side";


            if (!existingItems.containsKey(category)) {
                BlockStateData blockStateData = new BlockStateData();

                ModelData fencePost = new ModelData(postName, ModelData.BLOCK_FOLDER, "tree_icons/" + this.name);
                fencePost.taFencePost(1, new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name +"/planks"));

                AssetPackRegistry.blockModelDataMap.put(postName, fencePost);

                ModelData fenceSide = new ModelData(sideName, ModelData.BLOCK_FOLDER, "tree_icons/" + this.name);
                fenceSide.taFenceSide(1, new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name +"/planks"));

                AssetPackRegistry.blockModelDataMap.put(sideName, fenceSide);

                blockStateData.fenceBlock((FenceBlock)fence.get(), fencePost, fenceSide);
            }

            //--item
            if (!existingItems.containsKey(category)) {
                {
                    ModelData fenceInventory = new ModelData(inventoryName, ModelData.BLOCK_FOLDER, "tree_icons/" + this.name);
                    fenceInventory.taFencePost(1, new ResourceLocation(Ref.mod_id, "block/tree_icons/" + this.name +"/planks"));

                    ModelData model = new ModelData(name, ModelData.ITEM_FOLDER, "tree_icons/" + category);
                    model.getModel().parent(fenceInventory.getModel());

                    AssetPackRegistry.blockModelDataMap.put(inventoryName, fenceInventory);

                    AssetPackRegistry.itemModelDataHashMap.put(name, model);
                }
                enLang.addTranslation("item." + Ref.mod_id + "." + name, LangData.translateUpperCase(name));
                //--Tags
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge", category + "/" + this.name)).add(button.get().asItem()));
                DataPackRegistry.saveItemTagData(DataPackRegistry.getItemTagData(new ResourceLocation("forge", category)).add(button.get().asItem()));
            }
        }
//--//--//--//--//--//--//--//--//
        {
            //--block
            String name = this.name + "_fence_gate";

            if (!existingItems.containsKey("fence_gate")) {

            }


            //--item
            if (!existingItems.containsKey("fence_gate")) {

            }
        }
//--//--//--//--//--//--//--//--//
        {
            //--block
            String name = this.name + "_pressure_plate";

            if (!existingItems.containsKey("pressure_plate")) {

            }

            //--item
            if (!existingItems.containsKey("pressure_plate")) {

            }
        }
//--//--//--//--//--//--//--//--//
        {
            //--block
            String name = this.name + "_sign";

            if (!existingItems.containsKey("sign")) {

            }

            //--item
            if (!existingItems.containsKey("sign")) {

            }
        }
//--//--//--//--//--//--//--//--//
        {
            String name = this.name + "_resin";

            if (!existingItems.containsKey("resin")) {

            }
        }
//--//--//--//--//--//--//--//--//
        {
            //--block
            String name = this.name + "_door";

            if (!existingItems.containsKey("door")) {

            }

            //--item
            if (!existingItems.containsKey("door")) {

            }
        }
//--//--//--//--//--//--//--//--//
        {
            //--block
            String name = this.name + "_trap_door";

            if (!existingItems.containsKey("trap_door")) {

            }

            //--item
            if (!existingItems.containsKey("trap_door")) {

            }
        }
//--//--//--//--//--//--//--//--//
        AssetPackRegistry.langDataMap.put("en_us", enLang);
    }

    public void clientRenderLayerInit(){

    }

    public void registerColorForBlock(){

    }

    public void registerColorForItem(){

    }

    public void setupABlockColor(Block block) {
        ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout());
        Minecraft.getInstance().getBlockColors().register((state, world, pos, tintIndex) -> {
            if (tintIndex != 0)
                return 0xFFFFFFFF;
            return barkColor;
        }, block);
    }

    public void registerAItemColor(Item item, int layerNumberIn) {
        if (item != null) {
            Minecraft.getInstance().getItemColors().register((stack, tintIndex) -> {
                if (tintIndex <= layerNumberIn)
                    return barkColor;
                else
                    return 0xFFFFFFFF;
            }, item);
        }
    }


}
