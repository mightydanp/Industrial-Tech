package mightydanp.industrialtech.api.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import mightydanp.industrialtech.api.common.capabilities.ITToolItemCapabilityProvider;
import mightydanp.industrialtech.api.common.handler.itemstack.ITToolItemItemStackHandler;
import mightydanp.industrialtech.api.common.jsonconfig.tool.type.IToolType;
import mightydanp.industrialtech.common.IndustrialTech;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;

/**
 * Created by MightyDanp on 3/29/2021.
 */
public class ITToolItem extends Item {
    public String name;
    private final List<String> effectiveBlocks;
    private final Item.Properties properties;
    public int partsToWork;
    public boolean preformAction;
    public Map<String, Integer> craftingToolsNeeded = new HashMap<>();
    public Map<String, Integer> parts = new HashMap<>();
    public List<String> disassembleTools = new ArrayList<>();

    public ITToolItem(String nameIn, List<String> effectiveBlocksIn, Properties propertiesIn) {
        super(propertiesIn.stacksTo(1));
        name = nameIn;
        effectiveBlocks = effectiveBlocksIn;
        properties = propertiesIn;
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack itemStackIn) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack itemStackIn, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        List<Component> tooltipList = tooltip;
        CompoundTag nbt = itemStackIn.getOrCreateTag();

        tooltip.add(Component.nullToEmpty(""));

        if (!getPartsDamage(itemStackIn).isEmpty()) {
            for (Pair<String, String> pair : getPartsDamage(itemStackIn)) {
                tooltip.add(Component.nullToEmpty("\u00A7f" + pair.getFirst() + ":" + "\u00A7f" + " " + "\u00A7a" + pair.getSecond() + "\u00A7a"));
            }
        }

        tooltip.add(Component.nullToEmpty(""));

        if (nbt.contains("tool_levels") && nbt.contains("it_tool_types")) {
            Map<IToolType, Tag.Named<Block>> toolTypeList = getToolLevelsList(itemStackIn);
            for (int i = 0; i < toolTypeList.size(); i++) {
                String toolTypeName = toolTypeList.keySet().stream().toList().get(i).getName();
                Tag.Named<Block> toolTypeLevel = toolTypeList.values().stream().toList().get(i);
                tooltip.add(Component.nullToEmpty("\u00A7f" + toolTypeName + " level:" + "\u00A7f" + " " + "\u00A7a" + toolTypeLevel.getName().getPath() + "\u00A7a"));
            }
        }

        if (nbt.contains("efficiency")) {
            tooltip.add(Component.nullToEmpty("\u00A7f" + "efficiency:" + "\u00A7f" + " " + "\u00A7a" + getEfficiency(itemStackIn) + "\u00A7a"));
        }
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean mineBlock(ItemStack itemStackIn, Level worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {

        if (canWork(itemStackIn)) {
            if (state.requiresCorrectToolForDrops()) {
                damageToolParts(entityLiving.getMainHandItem(), (Player) entityLiving, worldIn, 1);
                return true;
            } else {
                damageToolParts(entityLiving.getMainHandItem(), (Player) entityLiving, worldIn, 2);
                return false;
            }
        }
        return true;
    }

    @Override
    public float getDestroySpeed(ItemStack itemStackIn, BlockState state) {
        if (canWork(itemStackIn)) {
            if (state.requiresCorrectToolForDrops()) {
                return getEfficiency(itemStackIn);
            } else {
                return 1F;
            }
        }
        return 1F;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerEntityIn, InteractionHand handIn) {
        ItemStack mainHandItemStack = playerEntityIn.getMainHandItem();
        ItemStack offHandItemStack = playerEntityIn.getOffhandItem();
        ItemStack toolHeadOnTool = getItemStackHandler(mainHandItemStack).getToolHead();
        ItemStack toolBindingOnTool = getItemStackHandler(mainHandItemStack).getToolBinding();
        ItemStack toolHandleOnTool = getItemStackHandler(mainHandItemStack).getToolHandle();

        if (canWork(mainHandItemStack) && preformAction) {
            if(preformAction) {
                damageToolParts(playerEntityIn.getMainHandItem(), playerEntityIn, worldIn, 1);
            }
        }

        if (!canWork(mainHandItemStack)){
            disassembleTool(playerEntityIn.getMainHandItem(), playerEntityIn, worldIn, 1, disassembleTools);
            playerEntityIn.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        }

        //playerEntityIn.getCooldowns().addCooldown(this, 20);
        /*
        if (!worldIn.isClientSide) {
            INamedContainerProvider itToolItemContainerProvider = new ITToolItemContainerProvider(this, itemStackIn);
            NetworkHooks.openGui((ServerPlayerEntity) playerEntityIn, itToolItemContainerProvider,
                    (packetBuffer)->{packetBuffer.writeInt(40);});
        }
        */
        return InteractionResultHolder.sidedSuccess(mainHandItemStack, worldIn.isClientSide());
    }

    @Override
    public void onUseTick(Level worldIn, LivingEntity playerEntityIn, ItemStack itemStack, int p_219972_4_) {
        /*if(canWork(itemStack) && preformAction) {
            damageItemstack(playerEntityIn.getMainHandItem(), (PlayerEntity) playerEntityIn, worldIn, null, 1);
        }*/
    }

    public void setToolLevel(ItemStack itemStackIn, Map<IToolType, Integer> toolTypeIn) {
        CompoundTag nbt = itemStackIn.getOrCreateTag();
        int[] harvestArray = new int[toolTypeIn.size()];
        StringBuilder itToolTypeString = new StringBuilder();

        toolTypeIn.forEach(((iToolType, integer) -> {
            for (int i = 0; i < toolTypeIn.size(); i++) {
                if (i == 0) {
                    harvestArray[i] = integer;
                    itToolTypeString.append(iToolType.getName());
                } else {
                    harvestArray[i] = integer;
                    itToolTypeString.append(", ").append(iToolType.getName());
                }
            }
        }));

        nbt.putString("it_tool_types", String.valueOf(itToolTypeString));
        nbt.putIntArray("tool_levels", harvestArray);
    }

    public Map<IToolType, Tag.Named<Block>> getToolLevelsList(ItemStack itemStackIn) {
        CompoundTag nbt = itemStackIn.getOrCreateTag();
        Map<IToolType, Tag.Named<Block>> toolTypes = new HashMap<>();
        int[] intArray = nbt.getIntArray("tool_levels");
        String[] stringArray = nbt.getString("it_tool_types").split(", ");
        for (int i = 0; i < intArray.length; i++) {
            toolTypes.putIfAbsent((IToolType)IndustrialTech.configSync.toolType.getFirst().registryMap.get(stringArray[i]), BlockTags.bind("tool_level/" + intArray[i]));
        }
        return toolTypes;
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        AtomicBoolean isCorrect = new AtomicBoolean(true);

        getToolLevelsList(stack).forEach((iToolType, toolLevel) -> {
            if (state.is(iToolType.getToolTypeTag())) {
                int level = Integer.parseInt(toolLevel.getName().toString().split("/")[1]);

                for(int i = 0; i <= level; i++){
                    if(!state.is(BlockTags.bind("tool_level/" + i))){
                        isCorrect.set(false);
                    }
                }
            }});

        if(!isCorrect.get()){
            List<Block> blocks = new ArrayList<>();

            effectiveBlocks.forEach(string -> {
                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(string));

                blocks.add(block);
            });

            return blocks.contains(state.getBlock());
        }

        return isCorrect.get();
    }

    public void setAttackDamage(ItemStack itemStackIn, float attackDamageIn) {
        CompoundTag nbt = itemStackIn.getOrCreateTag();
        nbt.putFloat("attack_damage", attackDamageIn);
    }

    public float getAttackDamage(ItemStack itemStackIn) {
        CompoundTag nbt = itemStackIn.getOrCreateTag();
        if (nbt.contains("attack_damage")) {
            return nbt.getFloat("attack_damage");
        } else {
            return 0.00F;
        }
    }

    public void setAttackSpeed(ItemStack itemStackIn, float attackSpeedIn) {
        CompoundTag nbt = itemStackIn.getOrCreateTag();
        nbt.putFloat("attack_speed", attackSpeedIn);
    }

    public float getAttackSpeed(ItemStack itemStackIn) {
        CompoundTag nbt = itemStackIn.getOrCreateTag();
        if (nbt.contains("attack_speed")) {
            return nbt.getFloat("attack_speed");
        } else {
            return 0.00F;
        }

    }

    public void setEfficiency(ItemStack itemStackIn, float efficiencyIn) {
        CompoundTag nbt = itemStackIn.getOrCreateTag();
        nbt.putFloat("efficiency", efficiencyIn);
    }

    public float getEfficiency(ItemStack itemStackIn) {
        CompoundTag nbt = itemStackIn.getOrCreateTag();
        if (nbt.contains("efficiency")) {
            return nbt.getFloat("efficiency");
        } else {
            return 0.00F;
        }
    }

    public void setMaterialName(ItemStack itemStackIn, String materialNameIn) {
        CompoundTag nbt = itemStackIn.getOrCreateTag();
        nbt.putString("material_name", materialNameIn);
    }

    public String getMaterialName(ItemStack itemStackIn) {
        CompoundTag nbt = itemStackIn.getOrCreateTag();
        if (nbt.contains("material_name")) {
            return nbt.getString("material_name");
        } else {
            return "";
        }
    }

    public void setHeadColor(ItemStack itemStackIn, int colorIn) {
        CompoundTag nbt = itemStackIn.getOrCreateTag();
        nbt.putInt("head_color", colorIn);
    }

    @OnlyIn(Dist.CLIENT)
    public int getHeadColor(ItemStack itemStackIn) {
        if (itemStackIn != null) {
            CompoundTag nbt = itemStackIn.getOrCreateTag();
            if (nbt.contains("head_color")) {
                return nbt.getInt("head_color");
            } else {
                return 0xFFFFFFFF;
            }
        } else {
            return 0;
        }
    }

    public void setBindingColor(ItemStack itemStackIn, int colorIn) {
        CompoundTag nbt = itemStackIn.getOrCreateTag();
        nbt.putInt("binding_color", colorIn);
    }

    @OnlyIn(Dist.CLIENT)
    public int getBindingColor(ItemStack itemStackIn) {
        if (itemStackIn != null) {
            CompoundTag nbt = itemStackIn.getOrCreateTag();
            if (nbt.contains("binding_color")) {
                return nbt.getInt("binding_color");
            } else {
                return 0xFFFFFFFF;
            }
        } else {
            return 0;
        }
    }

    public void setHandleColor(ItemStack itemStackIn, int colorIn) {
        CompoundTag nbt = itemStackIn.getOrCreateTag();
        nbt.putInt("handle_color", colorIn);
    }

    public int getHandleColor(ItemStack itemStackIn) {
        if (itemStackIn != null) {
            CompoundTag nbt = itemStackIn.getOrCreateTag();
            if (nbt.contains("handle_color")) {
                return nbt.getInt("handle_color");
            } else {
                return 0xFFFFFFFF;
            }
        } else {
            return 0;
        }
    }

    public CompoundTag getCompoundNBT(ItemStack itemStackIn) {
        return itemStackIn.getOrCreateTag();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        if (nbt == null) {
            return ImmutableMultimap.of();
        }

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        if (slot == EquipmentSlot.MAINHAND) {
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", getAttackDamage(stack), AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", getAttackSpeed(stack), AttributeModifier.Operation.ADDITION));
        }

        return builder.build();
    }

    @OnlyIn(Dist.CLIENT)
    public boolean getIsHoldingShift() {
        long minecraftWindow = Minecraft.getInstance().getWindow().getWindow();
        return InputConstants.isKeyDown(minecraftWindow, GLFW.GLFW_KEY_LEFT_SHIFT);
    }

    @Nonnull
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag oldCapNbt) {
        return new ITToolItemCapabilityProvider();//bucket
    }

    public ITToolItemItemStackHandler getItemStackHandler(ItemStack itemStack) {
        IItemHandler flowerBag = itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        if (!(flowerBag instanceof ITToolItemItemStackHandler)) {
            IndustrialTech.LOGGER.error("ITToolItem did not have the expected ITEM_HANDLER_CAPABILITY");
            return new ITToolItemItemStackHandler(3);
        }
        return (ITToolItemItemStackHandler) flowerBag;
    }

    @Nullable
    @Override
    public CompoundTag getShareTag(ItemStack stack) {
        CompoundTag baseTag = stack.getTag();
        ITToolItemItemStackHandler itemStackHandlerFlowerBag = getItemStackHandler(stack);
        CompoundTag capabilityTag = itemStackHandlerFlowerBag.serializeNBT();
        CompoundTag combinedTag = new CompoundTag();

        if (baseTag != null) {
            combinedTag.put("base", baseTag);
        }

        if (capabilityTag != null) {
            combinedTag.put("capability", capabilityTag);
        }
        return combinedTag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        if (nbt == null) {
            stack.setTag(null);
            return;
        }
        CompoundTag baseTag = nbt.getCompound("base");              // empty if not found
        CompoundTag capabilityTag = nbt.getCompound("capability"); // empty if not found
        stack.setTag(baseTag);
        ITToolItemItemStackHandler itemStackHandlerFlowerBag = getItemStackHandler(stack);
        itemStackHandlerFlowerBag.deserializeNBT(capabilityTag);
    }

    public boolean canWork(ItemStack itemStackIn){
        ITToolItemItemStackHandler handler = getItemStackHandler(itemStackIn);
        ITToolItem toolItem = (ITToolItem) itemStackIn.getItem();
        if ((toolItem.partsToWork == 1 || toolItem.partsToWork == 2 || toolItem.partsToWork == 3) & handler.getToolHead() != null) {
            if(handler.getToolHead().getDamageValue() < handler.getToolHead().getMaxDamage()){
                return true;
            }
        }

        if ((toolItem.partsToWork == 2 || toolItem.partsToWork == 3) && handler.getToolHandle() != null && handler.getToolHead() != null) {
            if(handler.getToolHandle().getDamageValue() < handler.getToolHandle().getMaxDamage() && handler.getToolHead().getDamageValue() < handler.getToolHead().getMaxDamage()){
                return true;
            }
        }

        if (toolItem.partsToWork == 3 && handler.getToolHandle() != null && handler.getToolHead() != null && handler.getToolBinding() != null) {
            if(handler.getToolHandle().getDamageValue() < handler.getToolHandle().getMaxDamage() && handler.getToolHead().getDamageValue() < handler.getToolHead().getMaxDamage() && handler.getToolBinding().getDamageValue() < handler.getToolBinding().getMaxDamage()){
                return true;
            }
        }

        return false;
    }

    public Boolean damageToolParts(ItemStack itemStackIn, Player playerIn, Level worldIn, int amountIn) {
        Random random = new Random();
        ITToolItemItemStackHandler itemStackHandler = getItemStackHandler(itemStackIn);
        ITToolItem toolItem = (ITToolItem) itemStackIn.getItem();
        ItemStack toolHeadOnTool = itemStackHandler.getToolHead();
        ItemStack toolBindingOnTool = itemStackHandler.getToolBinding();
        ItemStack toolHandleOnTool = itemStackHandler.getToolHandle();

        if (toolHeadOnTool != null && (toolItem.partsToWork == 3 || toolItem.partsToWork == 2 || toolItem.partsToWork == 1)) {
            int headDamage = toolHeadOnTool.getDamageValue();
            int headMaxDamage = toolHeadOnTool.getMaxDamage();

            if (headDamage != headMaxDamage) {
                toolHeadOnTool.setDamageValue(headDamage + amountIn);
                return true;
            } else {
                playerIn.broadcastBreakEvent(playerIn.getUsedItemHand());
                worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.ITEM_BREAK, SoundSource.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
                return false;
            }
        }

        if (toolHandleOnTool != null && (toolItem.partsToWork == 3 || toolItem.partsToWork == 2)){
            int handleDamage = toolHandleOnTool.getDamageValue();
            int handleMaxDamage = toolHandleOnTool.getMaxDamage();

            if (handleDamage != handleMaxDamage) {
                toolHandleOnTool.setDamageValue(handleDamage + amountIn);
                return true;
            } else {
                playerIn.broadcastBreakEvent(playerIn.getUsedItemHand());
                worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.ITEM_BREAK, SoundSource.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
                return false;
            }
        }

        if (toolBindingOnTool != null && toolItem.partsToWork == 3){
            int bindingDamage = toolBindingOnTool.getDamageValue();
            int bindingMaxDamage = toolBindingOnTool.getMaxDamage();
            if (bindingDamage != bindingMaxDamage) {
                toolBindingOnTool.setDamageValue(bindingDamage + amountIn);
                return true;
            } else {
                playerIn.broadcastBreakEvent(playerIn.getUsedItemHand());
                worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.ITEM_BREAK, SoundSource.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
                return false;
            }
        }

        if(!canWork(itemStackIn)){
            return false;
        }

        return true;
    }

    public void disassembleTool(ItemStack itemStackIn, Player playerIn, Level worldIn, int toolInDamage, List<String> toolNeededIn) {
        Random random = new Random();
        ITToolItemItemStackHandler itemStackHandler = getItemStackHandler(itemStackIn);
        ItemStack toolHeadItemStack = itemStackHandler.getToolHead();
        ItemStack toolBindingItemStack = itemStackHandler.getToolBinding();
        ItemStack toolHandleItemStack = itemStackHandler.getToolHandle();

        damageToolsNeededInPlayerInventory(playerIn, worldIn, toolInDamage, toolNeededIn);

        if (toolHandleItemStack != null){
            if (playerIn.getInventory().canPlaceItem(1, toolHandleItemStack)) {
                playerIn.getInventory().add(toolHandleItemStack);
                itemStackHandler.setToolHandle(ItemStack.EMPTY);
                worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
            } else {
                worldIn.addFreshEntity(new ItemEntity(worldIn, playerIn.getX(), playerIn.getY(), playerIn.getZ(), toolHandleItemStack));
            }

        }

        if (toolHeadItemStack != null){
            if (playerIn.getInventory().canPlaceItem(1, toolHeadItemStack)) {
                playerIn.getInventory().add(toolHeadItemStack);
                itemStackHandler.setToolHead(ItemStack.EMPTY);
                worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
            } else {
                worldIn.addFreshEntity(new ItemEntity(worldIn, playerIn.getX(), playerIn.getY(), playerIn.getZ(), toolHeadItemStack));
            }
        }

        if (toolBindingItemStack != null){
            if (playerIn.getInventory().canPlaceItem(1, toolBindingItemStack)) {
                playerIn.getInventory().add(toolBindingItemStack);
                itemStackHandler.setToolBinding(ItemStack.EMPTY);
                worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
            } else {
                worldIn.addFreshEntity(new ItemEntity(worldIn, playerIn.getX(), playerIn.getY(), playerIn.getZ(), toolBindingItemStack));
            }
        }

        if (toolHandleItemStack == null && toolHeadItemStack == null && toolBindingItemStack == null) {

        }
    }

    public void damageToolsNeededInPlayerInventory(Player playerIn, Level worldIn, int Damage, List<String> toolNeededIn){


        if(inventoryToolCheck(playerIn, getItemsFromForge(toolNeededIn)) && toolNeededIn.size() != 0){
            for(Item toolThatIsNeeded : getItemsFromForge(toolNeededIn)){
                for(int i = 9; i <= 45; i++){
                    if (toolThatIsNeeded == playerIn.getInventory().getItem(i).getItem()){
                        toolNeededIn.remove(String.valueOf(playerIn.getInventory().getItem(i).getItem().getRegistryName()));
                        ItemStack toolItem = playerIn.getInventory().getItem(i);
                        if(toolThatIsNeeded instanceof ITToolItem) {
                            damageToolParts(toolItem, playerIn, worldIn, 1);
                        }else{
                            if(toolItem.getMaxDamage() > 0) {
                                toolItem.setDamageValue(toolItem.getDamageValue() + Damage);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean inventoryToolCheck(Player playerIn, List<Item> toolNeededIn){
        for(int i = 9; i <= 45; i++){
            ItemStack toolNeeded = playerIn.getInventory().getItem(i);
            if(toolNeededIn.contains(toolNeeded.getItem())){
                if(toolNeeded.getItem() instanceof ITToolItem){
                    ITToolItem toolNeededItem = (ITToolItem)playerIn.getInventory().getItem(i).getItem();
                    ITToolItemItemStackHandler itemStackHandler = getItemStackHandler(toolNeeded);

                    if((toolNeededItem.partsToWork == 1 || toolNeededItem.partsToWork == 2 || toolNeededItem.partsToWork == 3) & itemStackHandler.getToolHead() != null){
                        if(itemStackHandler.getToolHead().getDamageValue() < itemStackHandler.getToolHead().getMaxDamage()) {
                            toolNeededIn.remove(toolNeeded.getItem());
                        }
                    }

                    if((toolNeededItem.partsToWork == 2 || toolNeededItem.partsToWork == 3) & itemStackHandler.getToolHandle() != null){
                       if(itemStackHandler.getToolHandle().getDamageValue() < itemStackHandler.getToolHandle().getDamageValue()){
                           toolNeededIn.remove(toolNeeded.getItem());
                       }
                    }

                    if(toolNeededItem.partsToWork == 3 & itemStackHandler.getToolBinding() != null){
                       if(itemStackHandler.getToolBinding().getDamageValue() < itemStackHandler.getToolBinding().getDamageValue()){
                           toolNeededIn.remove(toolNeeded.getItem());
                       }
                    }
                }else{
                    if(toolNeeded.getDamageValue() < toolNeeded.getMaxDamage()) {
                        toolNeededIn.remove(toolNeeded.getItem());
                    }
                }
            }
        }
        return toolNeededIn.size() == 0;
    }

    public List<Pair<String, String>> getPartsDamage(ItemStack itemstackIn) {
        ITToolItemItemStackHandler itemStackHandler = getItemStackHandler(itemstackIn);
        ItemStack toolHeadOnTool = itemStackHandler.getToolHead();
        ItemStack toolBindingOnTool = itemStackHandler.getToolBinding();
        ItemStack toolHandleOnTool = itemStackHandler.getToolHandle();

        List<Pair<String, String>> list = new ArrayList<>();

        if (toolHandleOnTool != null) {
            list.add(new Pair<>(toolHandleOnTool.getDisplayName().getString().replace("[", "").replace("]", "") + " ", toolHandleOnTool.getMaxDamage() - toolHandleOnTool.getDamageValue() + "/" + toolHandleOnTool.getMaxDamage()));
        }

        if (toolHeadOnTool != null) {
            list.add(new Pair<>(toolHeadOnTool.getDisplayName().getString().replace("[", "").replace("]", "") + " ", toolHeadOnTool.getMaxDamage() - toolHeadOnTool.getDamageValue() + "/" + toolHeadOnTool.getMaxDamage()));
        }

        if (toolBindingOnTool != null) {
            list.add(new Pair<>(toolBindingOnTool.getDisplayName().getString().replace("[", "").replace("]", "") + " ", toolBindingOnTool.getMaxDamage() - toolBindingOnTool.getDamageValue() + "/" + toolBindingOnTool.getMaxDamage()));
        }

        return list;
    }

    public int getPartsToWork() {
        return partsToWork;
    }

    public List<Item> getItemsFromForge(List<String> listIn){
        List<Item> items = new ArrayList<>();

        for(String string : listIn){
            String modID = string.split(":")[0];
            String blockLocalization = string.split(":")[1];
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(modID, blockLocalization));

            if(item != null){
                items.add(item);
            }
        }

        return items;
    }
}