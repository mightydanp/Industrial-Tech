package mightydanp.industrialtech.common.tool.tools;

import com.google.common.collect.ImmutableSet;
import mightydanp.industrialtech.api.common.items.ITToolItem;
import mightydanp.industrialtech.api.common.items.ModItemGroups;
import mightydanp.industrialtech.api.common.libs.Ref;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

/**
 * Created by MightyDanp on 3/8/2021.
 */
public class PickaxeToolItem extends ITToolItem {
    private static final Set<String> EFFECTIVE_ON = ImmutableSet.of(String.valueOf(Blocks.ACTIVATOR_RAIL.getRegistryName()), String.valueOf(Blocks.COAL_ORE.getRegistryName()), String.valueOf(Blocks.COBBLESTONE.getRegistryName()), String.valueOf(Blocks.DETECTOR_RAIL.getRegistryName()), String.valueOf(Blocks.DIAMOND_BLOCK.getRegistryName()), String.valueOf(Blocks.DIAMOND_ORE.getRegistryName()), String.valueOf(Blocks.POWERED_RAIL.getRegistryName()), String.valueOf(Blocks.GOLD_BLOCK.getRegistryName()), String.valueOf(Blocks.GOLD_ORE.getRegistryName()), String.valueOf(Blocks.NETHER_GOLD_ORE.getRegistryName()), String.valueOf(Blocks.ICE.getRegistryName()), String.valueOf(Blocks.IRON_BLOCK.getRegistryName()), String.valueOf(Blocks.IRON_ORE.getRegistryName()), String.valueOf(Blocks.LAPIS_BLOCK.getRegistryName()), String.valueOf(Blocks.LAPIS_ORE.getRegistryName()), String.valueOf(Blocks.MOSSY_COBBLESTONE.getRegistryName()), String.valueOf(Blocks.NETHERRACK.getRegistryName()), String.valueOf(Blocks.PACKED_ICE.getRegistryName()), String.valueOf(Blocks.BLUE_ICE.getRegistryName()), String.valueOf(Blocks.RAIL.getRegistryName()), String.valueOf(Blocks.REDSTONE_ORE.getRegistryName()), String.valueOf(Blocks.SANDSTONE.getRegistryName()), String.valueOf(Blocks.CHISELED_SANDSTONE.getRegistryName()), String.valueOf(Blocks.CUT_SANDSTONE.getRegistryName()), String.valueOf(Blocks.CHISELED_RED_SANDSTONE.getRegistryName()), String.valueOf(Blocks.CUT_RED_SANDSTONE.getRegistryName()), String.valueOf(Blocks.RED_SANDSTONE.getRegistryName()), String.valueOf(Blocks.STONE.getRegistryName()), String.valueOf(Blocks.GRANITE.getRegistryName()), String.valueOf(Blocks.POLISHED_GRANITE.getRegistryName()), String.valueOf(Blocks.DIORITE.getRegistryName()), String.valueOf(Blocks.POLISHED_DIORITE.getRegistryName()), String.valueOf(Blocks.ANDESITE.getRegistryName()), String.valueOf(Blocks.POLISHED_ANDESITE.getRegistryName()), String.valueOf(Blocks.STONE_SLAB.getRegistryName()), String.valueOf(Blocks.SMOOTH_STONE_SLAB.getRegistryName()), String.valueOf(Blocks.SANDSTONE_SLAB.getRegistryName()), String.valueOf(Blocks.PETRIFIED_OAK_SLAB.getRegistryName()), String.valueOf(Blocks.COBBLESTONE_SLAB.getRegistryName()), String.valueOf(Blocks.BRICK_SLAB.getRegistryName()), String.valueOf(Blocks.STONE_BRICK_SLAB.getRegistryName()), String.valueOf(Blocks.NETHER_BRICK_SLAB.getRegistryName()), String.valueOf(Blocks.QUARTZ_SLAB.getRegistryName()), String.valueOf(Blocks.RED_SANDSTONE_SLAB.getRegistryName()), String.valueOf(Blocks.PURPUR_SLAB.getRegistryName()), String.valueOf(Blocks.SMOOTH_QUARTZ.getRegistryName()), String.valueOf(Blocks.SMOOTH_RED_SANDSTONE.getRegistryName()), String.valueOf(Blocks.SMOOTH_SANDSTONE.getRegistryName()), String.valueOf(Blocks.SMOOTH_STONE.getRegistryName()), String.valueOf(Blocks.STONE_BUTTON.getRegistryName()), String.valueOf(Blocks.STONE_PRESSURE_PLATE.getRegistryName()), String.valueOf(Blocks.POLISHED_GRANITE_SLAB.getRegistryName()), String.valueOf(Blocks.SMOOTH_RED_SANDSTONE_SLAB.getRegistryName()), String.valueOf(Blocks.MOSSY_STONE_BRICK_SLAB.getRegistryName()), String.valueOf(Blocks.POLISHED_DIORITE_SLAB.getRegistryName()), String.valueOf(Blocks.MOSSY_COBBLESTONE_SLAB.getRegistryName()), String.valueOf(Blocks.END_STONE_BRICK_SLAB.getRegistryName()), String.valueOf(Blocks.SMOOTH_SANDSTONE_SLAB.getRegistryName()), String.valueOf(Blocks.SMOOTH_QUARTZ_SLAB.getRegistryName()), String.valueOf(Blocks.GRANITE_SLAB.getRegistryName()), String.valueOf(Blocks.ANDESITE_SLAB.getRegistryName()), String.valueOf(Blocks.RED_NETHER_BRICK_SLAB.getRegistryName()), String.valueOf(Blocks.POLISHED_ANDESITE_SLAB.getRegistryName()), String.valueOf(Blocks.DIORITE_SLAB.getRegistryName()), String.valueOf(Blocks.SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.BLACK_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.BLUE_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.BROWN_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.CYAN_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.GRAY_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.GREEN_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.LIGHT_BLUE_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.LIGHT_GRAY_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.LIME_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.MAGENTA_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.ORANGE_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.PINK_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.PURPLE_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.RED_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.WHITE_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.YELLOW_SHULKER_BOX.getRegistryName()), String.valueOf(Blocks.PISTON.getRegistryName()), String.valueOf(Blocks.STICKY_PISTON.getRegistryName()), String.valueOf(Blocks.PISTON_HEAD));

    public PickaxeToolItem() {
        super("pickaxe", new ArrayList<>(EFFECTIVE_ON), new Item.Properties().tab(ModItemGroups.tool_tab));

        craftingToolsNeeded.put(String.valueOf(new ResourceLocation(Ref.mod_id, "hammer")), 1);

        parts.put("headPickaxe", 1);
        parts.put("handleWedge", 2);
        parts.put("wedge", 3);

        disassembleTools.add(String.valueOf(new ResourceLocation(Ref.mod_id, "chisel")));
        disassembleTools.add(String.valueOf(new ResourceLocation(Ref.mod_id, "hammer")));
    }
}