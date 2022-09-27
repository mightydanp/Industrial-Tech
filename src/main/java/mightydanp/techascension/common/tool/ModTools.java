package mightydanp.techascension.common.tool;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import mightydanp.techapi.common.jsonconfig.sync.ConfigSync;
import mightydanp.techcore.common.handler.RegistryHandler;
import mightydanp.techcore.common.jsonconfig.TCJsonConfigs;
import mightydanp.techcore.common.jsonconfig.tool.ITool;
import mightydanp.techcore.common.jsonconfig.tool.ToolRegistry;
import mightydanp.techcore.common.libs.Ref;
import mightydanp.techcore.common.tool.TCTool;
import mightydanp.techcore.common.tool.TCTools;
import mightydanp.techcore.common.tool.part.*;
import mightydanp.techascension.common.tool.part.TABindingItem;
import mightydanp.techascension.common.tool.part.TADullHeadItem;
import mightydanp.techascension.common.tool.part.TAHandleItem;
import mightydanp.techascension.common.tool.part.TAHeadItem;
import mightydanp.techcore.common.material.tools.HammerToolItem;
import mightydanp.techcore.common.material.tools.KnifeToolItem;
import mightydanp.techcore.common.material.tools.PickaxeToolItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by MightyDanp on 4/23/2021.
 */
public class ModTools extends TCTools{
    public static TCTool hammer, chisel, pickaxe, knife;

    public static String chiselName = "chisel";

    public static String hammerName = "hammer";
    public static String pickaxeName = "pickaxe";
    public static String knifeName = "knife";

    public static void init(){
        chisel = addTool(chiselName,
                new PartHolders.handlePartHolder(new Pair<>("", "_handle"), true, true, TAHandleItem.class),
                null,
                new PartHolders.headPartHolder(new Pair<>("", "_head"), true, true, TAHeadItem.class),
                new PartHolders.bindingPartHolder(new Pair<>("", "_binding"), false, false, TABindingItem.class),
                HammerToolItem.class
        );

        hammer = addTool(hammerName,
                new PartHolders.handlePartHolder(new Pair<>("", "_wedge_handle"), false, true, TAHandleItem.class),
                null,
                new PartHolders.headPartHolder(new Pair<>("", "_head"), true, true, TAHeadItem.class),
                new PartHolders.bindingPartHolder(new Pair<>("", "_wedge"), false, true, TABindingItem.class),
                HammerToolItem.class
        );

        pickaxe = addTool(pickaxeName,
                new PartHolders.handlePartHolder(new Pair<>("", "_wedge_handle"), false, true, TAHandleItem.class),
                new PartHolders.dullHeadPartHolder(new Pair<>("dull_", "_head"), false, false, TADullHeadItem.class),
                new PartHolders.headPartHolder(new Pair<>("", "_head"), true, true, TAHeadItem.class),
                new PartHolders.bindingPartHolder(new Pair<>("", "_wedge"), false, true, TABindingItem.class),
                PickaxeToolItem.class
        );

        knife = addTool(knifeName,
                new PartHolders.handlePartHolder(new Pair<>("", "_handle"), true, true, TAHandleItem.class),
                new PartHolders.dullHeadPartHolder(new Pair<>("dull_", "_head"), true, true, TADullHeadItem.class),
                new PartHolders.headPartHolder(new Pair<>("", "_head"), true, true, TAHeadItem.class),
                new PartHolders.bindingPartHolder(new Pair<>("", "_binding"), false, false, TABindingItem.class),
                KnifeToolItem.class
        );
    }

    public static void postInit(){
        /*
        List<String> chiselEffectiveOn = List.of(String.valueOf(Blocks.ACTIVATOR_RAIL.getRegistryName()), String.valueOf(Blocks.COAL_ORE.getRegistryName()), String.valueOf(Blocks.COBBLESTONE.getRegistryName()), String.valueOf(Blocks.DETECTOR_RAIL.getRegistryName()), String.valueOf(Blocks.DIAMOND_BLOCK.getRegistryName()), String.valueOf(Blocks.DIAMOND_ORE.getRegistryName()), String.valueOf(Blocks.POWERED_RAIL.getRegistryName()), String.valueOf(Blocks.GOLD_BLOCK.getRegistryName()), String.valueOf(Blocks.GOLD_ORE.getRegistryName()), String.valueOf(Blocks.NETHER_GOLD_ORE.getRegistryName()), String.valueOf(Blocks.ICE.getRegistryName()), String.valueOf(Blocks.IRON_BLOCK.getRegistryName()), String.valueOf(Blocks.IRON_ORE.getRegistryName()), String.valueOf(Blocks.LAPIS_BLOCK.getRegistryName()), String.valueOf(Blocks.LAPIS_ORE.getRegistryName()), String.valueOf(Blocks.MOSSY_COBBLESTONE.getRegistryName()), String.valueOf(Blocks.NETHERRACK.getRegistryName()), String.valueOf(Blocks.PACKED_ICE.getRegistryName()), String.valueOf(Blocks.BLUE_ICE.getRegistryName()), String.valueOf(Blocks.RAIL.getRegistryName()), String.valueOf(Blocks.REDSTONE_ORE.getRegistryName()), String.valueOf(Blocks.SANDSTONE.getRegistryName()), String.valueOf(Blocks.CHISELED_SANDSTONE.getRegistryName()), String.valueOf(Blocks.CUT_SANDSTONE.getRegistryName()), String.valueOf(Blocks.CHISELED_RED_SANDSTONE.getRegistryName()), String.valueOf(Blocks.CUT_RED_SANDSTONE.getRegistryName()), String.valueOf(Blocks.RED_SANDSTONE.getRegistryName()), String.valueOf(Blocks.STONE.getRegistryName()), String.valueOf(Blocks.GRANITE.getRegistryName()), String.valueOf(Blocks.POLISHED_GRANITE.getRegistryName()), String.valueOf(Blocks.DIORITE.getRegistryName()), String.valueOf(Blocks.POLISHED_DIORITE.getRegistryName()), String.valueOf(Blocks.ANDESITE.getRegistryName()), String.valueOf(Blocks.POLISHED_ANDESITE.getRegistryName()), String.valueOf(Blocks.STONE_SLAB.getRegistryName()), String.valueOf(Blocks.SMOOTH_STONE_SLAB.getRegistryName()), String.valueOf(Blocks.SANDSTONE_SLAB.getRegistryName()), String.valueOf(Blocks.PETRIFIED_OAK_SLAB.getRegistryName()), String.valueOf(Blocks.COBBLESTONE_SLAB.getRegistryName()), String.valueOf(Blocks.BRICK_SLAB.getRegistryName()), String.valueOf(Blocks.STONE_BRICK_SLAB.getRegistryName()), String.valueOf(Blocks.NETHER_BRICK_SLAB.getRegistryName()), String.valueOf(Blocks.QUARTZ_SLAB.getRegistryName()), String.valueOf(Blocks.RED_SANDSTONE_SLAB.getRegistryName()), String.valueOf(Blocks.PURPUR_SLAB.getRegistryName()), String.valueOf(Blocks.SMOOTH_QUARTZ.getRegistryName()), String.valueOf(Blocks.SMOOTH_RED_SANDSTONE.getRegistryName()), String.valueOf(Blocks.SMOOTH_SANDSTONE.getRegistryName()), String.valueOf(Blocks.SMOOTH_STONE.getRegistryName()), String.valueOf(Blocks.STONE_BUTTON.getRegistryName()), String.valueOf(Blocks.STONE_PRESSURE_PLATE.getRegistryName()), String.valueOf(Blocks.POLISHED_GRANITE_SLAB.getRegistryName()), String.valueOf(Blocks.SMOOTH_RED_SANDSTONE_SLAB.getRegistryName()), String.valueOf(Blocks.MOSSY_STONE_BRICK_SLAB.getRegistryName()), String.valueOf(Blocks.POLISHED_DIORITE_SLAB.getRegistryName()), String.valueOf(Blocks.MOSSY_COBBLESTONE_SLAB.getRegistryName()), String.valueOf(Blocks.END_STONE_BRICK_SLAB.getRegistryName()), String.valueOf(Blocks.SMOOTH_SANDSTONE_SLAB.getRegistryName()), String.valueOf(Blocks.SMOOTH_QUARTZ_SLAB.getRegistryName()), String.valueOf(Blocks.GRANITE_SLAB.getRegistryName()), String.valueOf(Blocks.ANDESITE_SLAB.getRegistryName()), String.valueOf(Blocks.RED_NETHER_BRICK_SLAB.getRegistryName()), String.valueOf(Blocks.POLISHED_ANDESITE_SLAB.getRegistryName()), String.valueOf(Blocks.DIORITE_SLAB.getRegistryName()), String.valueOf(Blocks.SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.BLACK_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.BLUE_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.BROWN_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.CYAN_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.GRAY_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.GREEN_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.LIGHT_BLUE_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.LIGHT_GRAY_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.LIME_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.MAGENTA_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.ORANGE_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.PINK_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.PURPLE_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.RED_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.WHITE_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.YELLOW_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.PISTON.getRegistryName()), String.valueOf(Blocks.STICKY_PISTON.getRegistryName()), String.valueOf(Blocks.PISTON_HEAD.getRegistryName()));
        Map<Integer, List<Map<Ingredient, Integer>>> chiselAssembleItems = Map.of(
                1, List.of(
                        Map.of(Ingredient.of())
                )
        );
        //Map.ofEntries(Map.entry(new ResourceLocation(Ref.mod_id, "hammer").toString(), 1));
        List<Map<Ingredient, Integer>> chiselDisassembleItems = List.of(
                new ResourceLocation(Ref.mod_id, "hammer").toString(), new ResourceLocation(Ref.mod_id, "chisel").toString()
        );

        List<String> chiselHandlePartItems = List.of();
        List<String> chiselHeadPartItems = List.of();
        List<String> chiselBindingPartItems = List.of();

        ((ToolRegistry)TCJsonConfigs.tool.getFirst()).buildAndRegisterTool(toITool());

//----------

        List<String> hammerEffectiveOn = List.of(String.valueOf(Blocks.ACTIVATOR_RAIL.getRegistryName()), String.valueOf(Blocks.COAL_ORE.getRegistryName()), String.valueOf(Blocks.COBBLESTONE.getRegistryName()), String.valueOf(Blocks.DETECTOR_RAIL.getRegistryName()), String.valueOf(Blocks.DIAMOND_BLOCK.getRegistryName()), String.valueOf(Blocks.DIAMOND_ORE.getRegistryName()), String.valueOf(Blocks.POWERED_RAIL.getRegistryName()), String.valueOf(Blocks.GOLD_BLOCK.getRegistryName()), String.valueOf(Blocks.GOLD_ORE.getRegistryName()), String.valueOf(Blocks.NETHER_GOLD_ORE.getRegistryName()), String.valueOf(Blocks.ICE.getRegistryName()), String.valueOf(Blocks.IRON_BLOCK.getRegistryName()), String.valueOf(Blocks.IRON_ORE.getRegistryName()), String.valueOf(Blocks.LAPIS_BLOCK.getRegistryName()), String.valueOf(Blocks.LAPIS_ORE.getRegistryName()), String.valueOf(Blocks.MOSSY_COBBLESTONE.getRegistryName()), String.valueOf(Blocks.NETHERRACK.getRegistryName()), String.valueOf(Blocks.PACKED_ICE.getRegistryName()), String.valueOf(Blocks.BLUE_ICE.getRegistryName()), String.valueOf(Blocks.RAIL.getRegistryName()), String.valueOf(Blocks.REDSTONE_ORE.getRegistryName()), String.valueOf(Blocks.SANDSTONE.getRegistryName()), String.valueOf(Blocks.CHISELED_SANDSTONE.getRegistryName()), String.valueOf(Blocks.CUT_SANDSTONE.getRegistryName()), String.valueOf(Blocks.CHISELED_RED_SANDSTONE.getRegistryName()), String.valueOf(Blocks.CUT_RED_SANDSTONE.getRegistryName()), String.valueOf(Blocks.RED_SANDSTONE.getRegistryName()), String.valueOf(Blocks.STONE.getRegistryName()), String.valueOf(Blocks.GRANITE.getRegistryName()), String.valueOf(Blocks.POLISHED_GRANITE.getRegistryName()), String.valueOf(Blocks.DIORITE.getRegistryName()), String.valueOf(Blocks.POLISHED_DIORITE.getRegistryName()), String.valueOf(Blocks.ANDESITE.getRegistryName()), String.valueOf(Blocks.POLISHED_ANDESITE.getRegistryName()), String.valueOf(Blocks.STONE_SLAB.getRegistryName()), String.valueOf(Blocks.SMOOTH_STONE_SLAB.getRegistryName()), String.valueOf(Blocks.SANDSTONE_SLAB.getRegistryName()), String.valueOf(Blocks.PETRIFIED_OAK_SLAB.getRegistryName()), String.valueOf(Blocks.COBBLESTONE_SLAB.getRegistryName()), String.valueOf(Blocks.BRICK_SLAB.getRegistryName()), String.valueOf(Blocks.STONE_BRICK_SLAB.getRegistryName()), String.valueOf(Blocks.NETHER_BRICK_SLAB.getRegistryName()), String.valueOf(Blocks.QUARTZ_SLAB.getRegistryName()), String.valueOf(Blocks.RED_SANDSTONE_SLAB.getRegistryName()), String.valueOf(Blocks.PURPUR_SLAB.getRegistryName()), String.valueOf(Blocks.SMOOTH_QUARTZ.getRegistryName()), String.valueOf(Blocks.SMOOTH_RED_SANDSTONE.getRegistryName()), String.valueOf(Blocks.SMOOTH_SANDSTONE.getRegistryName()), String.valueOf(Blocks.SMOOTH_STONE.getRegistryName()), String.valueOf(Blocks.STONE_BUTTON.getRegistryName()), String.valueOf(Blocks.STONE_PRESSURE_PLATE.getRegistryName()), String.valueOf(Blocks.POLISHED_GRANITE_SLAB.getRegistryName()), String.valueOf(Blocks.SMOOTH_RED_SANDSTONE_SLAB.getRegistryName()), String.valueOf(Blocks.MOSSY_STONE_BRICK_SLAB.getRegistryName()), String.valueOf(Blocks.POLISHED_DIORITE_SLAB.getRegistryName()), String.valueOf(Blocks.MOSSY_COBBLESTONE_SLAB.getRegistryName()), String.valueOf(Blocks.END_STONE_BRICK_SLAB.getRegistryName()), String.valueOf(Blocks.SMOOTH_SANDSTONE_SLAB.getRegistryName()), String.valueOf(Blocks.SMOOTH_QUARTZ_SLAB.getRegistryName()), String.valueOf(Blocks.GRANITE_SLAB.getRegistryName()), String.valueOf(Blocks.ANDESITE_SLAB.getRegistryName()), String.valueOf(Blocks.RED_NETHER_BRICK_SLAB.getRegistryName()), String.valueOf(Blocks.POLISHED_ANDESITE_SLAB.getRegistryName()), String.valueOf(Blocks.DIORITE_SLAB.getRegistryName()), String.valueOf(Blocks.SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.BLACK_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.BLUE_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.BROWN_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.CYAN_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.GRAY_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.GREEN_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.LIGHT_BLUE_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.LIGHT_GRAY_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.LIME_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.MAGENTA_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.ORANGE_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.PINK_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.PURPLE_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.RED_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.WHITE_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.YELLOW_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.PISTON.getRegistryName()), String.valueOf(Blocks.STICKY_PISTON.getRegistryName()), String.valueOf(Blocks.PISTON_HEAD.getRegistryName()));
        Map<Integer, List<Map<Ingredient, Integer>>> hammerAssembleItems = Map.ofEntries(Map.entry(new ResourceLocation(Ref.mod_id, "hammer").toString(), 1));
        List<Map<Ingredient, Integer>> hammerDisassembleItems = List.of(new ResourceLocation(Ref.mod_id, "chisel").toString(), new ResourceLocation(Ref.mod_id, "hammer").toString());

        List<String> hammerHandlePartItems = List.of();
        List<String> hammerHeadPartItems = List.of();
        List<String> hammerBindingPartItems = List.of();

        ((ToolRegistry)TCJsonConfigs.tool.getFirst()).buildAndRegisterTool(toITool());

//----------

        List<String> pickaxeEffectiveOn = List.of(String.valueOf(Blocks.ACTIVATOR_RAIL.getRegistryName()), String.valueOf(Blocks.COAL_ORE.getRegistryName()), String.valueOf(Blocks.COBBLESTONE.getRegistryName()), String.valueOf(Blocks.DETECTOR_RAIL.getRegistryName()), String.valueOf(Blocks.DIAMOND_BLOCK.getRegistryName()), String.valueOf(Blocks.DIAMOND_ORE.getRegistryName()), String.valueOf(Blocks.POWERED_RAIL.getRegistryName()), String.valueOf(Blocks.GOLD_BLOCK.getRegistryName()), String.valueOf(Blocks.GOLD_ORE.getRegistryName()), String.valueOf(Blocks.NETHER_GOLD_ORE.getRegistryName()), String.valueOf(Blocks.ICE.getRegistryName()), String.valueOf(Blocks.IRON_BLOCK.getRegistryName()), String.valueOf(Blocks.IRON_ORE.getRegistryName()), String.valueOf(Blocks.LAPIS_BLOCK.getRegistryName()), String.valueOf(Blocks.LAPIS_ORE.getRegistryName()), String.valueOf(Blocks.MOSSY_COBBLESTONE.getRegistryName()), String.valueOf(Blocks.NETHERRACK.getRegistryName()), String.valueOf(Blocks.PACKED_ICE.getRegistryName()), String.valueOf(Blocks.BLUE_ICE.getRegistryName()), String.valueOf(Blocks.RAIL.getRegistryName()), String.valueOf(Blocks.REDSTONE_ORE.getRegistryName()), String.valueOf(Blocks.SANDSTONE.getRegistryName()), String.valueOf(Blocks.CHISELED_SANDSTONE.getRegistryName()), String.valueOf(Blocks.CUT_SANDSTONE.getRegistryName()), String.valueOf(Blocks.CHISELED_RED_SANDSTONE.getRegistryName()), String.valueOf(Blocks.CUT_RED_SANDSTONE.getRegistryName()), String.valueOf(Blocks.RED_SANDSTONE.getRegistryName()), String.valueOf(Blocks.STONE.getRegistryName()), String.valueOf(Blocks.GRANITE.getRegistryName()), String.valueOf(Blocks.POLISHED_GRANITE.getRegistryName()), String.valueOf(Blocks.DIORITE.getRegistryName()), String.valueOf(Blocks.POLISHED_DIORITE.getRegistryName()), String.valueOf(Blocks.ANDESITE.getRegistryName()), String.valueOf(Blocks.POLISHED_ANDESITE.getRegistryName()), String.valueOf(Blocks.STONE_SLAB.getRegistryName()), String.valueOf(Blocks.SMOOTH_STONE_SLAB.getRegistryName()), String.valueOf(Blocks.SANDSTONE_SLAB.getRegistryName()), String.valueOf(Blocks.PETRIFIED_OAK_SLAB.getRegistryName()), String.valueOf(Blocks.COBBLESTONE_SLAB.getRegistryName()), String.valueOf(Blocks.BRICK_SLAB.getRegistryName()), String.valueOf(Blocks.STONE_BRICK_SLAB.getRegistryName()), String.valueOf(Blocks.NETHER_BRICK_SLAB.getRegistryName()), String.valueOf(Blocks.QUARTZ_SLAB.getRegistryName()), String.valueOf(Blocks.RED_SANDSTONE_SLAB.getRegistryName()), String.valueOf(Blocks.PURPUR_SLAB.getRegistryName()), String.valueOf(Blocks.SMOOTH_QUARTZ.getRegistryName()), String.valueOf(Blocks.SMOOTH_RED_SANDSTONE.getRegistryName()), String.valueOf(Blocks.SMOOTH_SANDSTONE.getRegistryName()), String.valueOf(Blocks.SMOOTH_STONE.getRegistryName()), String.valueOf(Blocks.STONE_BUTTON.getRegistryName()), String.valueOf(Blocks.STONE_PRESSURE_PLATE.getRegistryName()), String.valueOf(Blocks.POLISHED_GRANITE_SLAB.getRegistryName()), String.valueOf(Blocks.SMOOTH_RED_SANDSTONE_SLAB.getRegistryName()), String.valueOf(Blocks.MOSSY_STONE_BRICK_SLAB.getRegistryName()), String.valueOf(Blocks.POLISHED_DIORITE_SLAB.getRegistryName()), String.valueOf(Blocks.MOSSY_COBBLESTONE_SLAB.getRegistryName()), String.valueOf(Blocks.END_STONE_BRICK_SLAB.getRegistryName()), String.valueOf(Blocks.SMOOTH_SANDSTONE_SLAB.getRegistryName()), String.valueOf(Blocks.SMOOTH_QUARTZ_SLAB.getRegistryName()), String.valueOf(Blocks.GRANITE_SLAB.getRegistryName()), String.valueOf(Blocks.ANDESITE_SLAB.getRegistryName()), String.valueOf(Blocks.RED_NETHER_BRICK_SLAB.getRegistryName()), String.valueOf(Blocks.POLISHED_ANDESITE_SLAB.getRegistryName()), String.valueOf(Blocks.DIORITE_SLAB.getRegistryName()), String.valueOf(Blocks.SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.BLACK_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.BLUE_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.BROWN_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.CYAN_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.GRAY_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.GREEN_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.LIGHT_BLUE_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.LIGHT_GRAY_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.LIME_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.MAGENTA_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.ORANGE_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.PINK_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.PURPLE_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.RED_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.WHITE_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.YELLOW_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.PISTON.getRegistryName()), String.valueOf(Blocks.STICKY_PISTON.getRegistryName()), String.valueOf(Blocks.PISTON_HEAD.getRegistryName()));
        Map<Integer, List<Map<Ingredient, Integer>>> pickaxeAssembleItems = Map.ofEntries(Map.entry(new ResourceLocation(Ref.mod_id, "hammer").toString(), 1));
        List<Map<Ingredient, Integer>> pickaxeDisassembleItems = List.of(new ResourceLocation(Ref.mod_id, "chisel").toString(), new ResourceLocation(Ref.mod_id, "hammer").toString());

        List<String> pickaxeHandlePartItems = List.of();
        List<String> pickaxeHeadPartItems = List.of();
        List<String> pickaxeBindingPartItems = List.of();

        ((ToolRegistry)TCJsonConfigs.tool.getFirst()).buildAndRegisterTool(toITool());

//----------

        List<String> knifeEffectiveOn = List.of(String.valueOf(Blocks.SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.BLACK_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.BLUE_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.BROWN_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.CYAN_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.GRAY_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.GREEN_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.LIGHT_BLUE_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.LIGHT_GRAY_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.LIME_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.MAGENTA_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.ORANGE_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.PINK_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.PURPLE_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.RED_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.WHITE_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.YELLOW_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.PISTON.getRegistryName()), String.valueOf(Blocks.STICKY_PISTON.getRegistryName()), String.valueOf(Blocks.PISTON_HEAD.getRegistryName()));
        Map<Integer, List<Map<Ingredient, Integer>>> knifeAssembleItems = Map.ofEntries(Map.entry(new ResourceLocation(Ref.mod_id, "hammer").toString(), 1));
        List<Map<Ingredient, Integer>> knifeDisassembleItems = List.of(new ResourceLocation(Ref.mod_id, "hammer").toString(), new ResourceLocation(Ref.mod_id, "chisel").toString());

        List<String> knifeHandlePartItems = List.of();
        List<String> knifeHeadPartItems = List.of();
        List<String> knifeBindingPartItems = List.of();

        ((ToolRegistry)TCJsonConfigs.tool.getFirst()).buildAndRegisterTool(toITool());
         */
    }

    public static ITool toITool(String name, Integer useDamage, List<String> effectiveOn, List<Ingredient> handleItems, List<Ingredient> headItems, List<Ingredient> bindingItems, Map<Integer, List<Map<Ingredient, Integer>>> assembleStepsItems, List<Map<Ingredient, Integer>> disassembleItems){
        return new ITool() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public Integer getUseDamage() {
                return useDamage;
            }

            @Override
            public List<String> getEffectiveOn() {
                return effectiveOn;
            }

            @Override
            public List<Ingredient> getHandleItems() {
                return handleItems;
            }

            @Override
            public List<Ingredient> getHeadItems() {
                return headItems;
            }

            @Override
            public List<Ingredient> getBindingItems() {
                return bindingItems;
            }

            @Override
            public Map<Integer, List<Map<Ingredient, Integer>>> getAssembleStepsItems() {
                return assembleStepsItems;
            }

            @Override
            public List<Map<Ingredient, Integer>> getDisassembleItems() {
                return disassembleItems;
            }
        };
    }
}
