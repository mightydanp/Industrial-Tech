package mightydanp.techcore.common.tool;

import com.mojang.datafixers.util.Either;
import mightydanp.techascension.common.TechAscension;
import mightydanp.techcore.client.settings.keybindings.KeyBindings;
import mightydanp.techcore.common.handler.itemstack.TCToolItemInventoryHelper;
import mightydanp.techcore.common.jsonconfig.TCJsonConfigs;
import mightydanp.techcore.common.jsonconfig.recipe.handcrafting.HandCraftingCodec;
import mightydanp.techcore.common.jsonconfig.tool.ToolCodec;
import mightydanp.techcore.common.jsonconfig.trait.item.ItemTraitCodec;
import mightydanp.techcore.common.tool.part.HandleItem;
import mightydanp.techcore.common.tool.part.BindingItem;
import mightydanp.techcore.common.tool.part.HeadItem;
import mightydanp.techcore.common.libs.Ref;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = Ref.mod_id)
public class TCToolHandler {

    @SubscribeEvent
    @SuppressWarnings("ALL")
    public static void onItemRightClickEvent(final PlayerInteractEvent.RightClickItem event) {
        if(event.getItemStack().getItem() == Items.STICK){
            //Minecraft.getInstance().setScreen(new SyncScreen(materialServer));
        }

        if(KeyBindings.handCrafting.isDown()){

            Item offHandItem  = event.getPlayer().getOffhandItem().getItem();
            Item mainHandItem = event.getPlayer().getMainHandItem().getItem();

            if (mainHandItem instanceof HeadItem toolHead) {
                List<TCTool> tool = TCTools.getTools().values().stream().filter(itTool -> Objects.equals(itTool.toolName, toolHead.suggestedCraftedTool)).toList();

                if (tool.size() > 0) {
                    TCToolItem toolItem = (TCToolItem) tool.get(0).toolItem.get();
                    handToolCrafting(toolItem, event, 1, toolItem.getAssembleItems());
                }
            }

            if (offHandItem instanceof HeadItem toolHead) {
                List<TCTool> tool = TCTools.getTools().values().stream().filter(itTool -> Objects.equals(itTool.toolName, toolHead.suggestedCraftedTool)).toList();

                if (tool.size() > 0) {
                    TCToolItem toolItem = (TCToolItem) tool.get(0).toolItem.get();
                    handToolCrafting(toolItem, event, 1, toolItem.getAssembleItems());
                }
            }

            if (mainHandItem instanceof TCToolItem tool) {
                handToolCrafting(tool, event, 1, tool.getAssembleItems());
            }

            if (offHandItem instanceof TCToolItem tool) {
                handToolCrafting(tool, event, 1, tool.getAssembleItems());
            }

            List<HandCraftingCodec> handCraftingList = (List<HandCraftingCodec>)(TCJsonConfigs.handCrafting.getFirst().registryMap.values().stream().toList());

            for(HandCraftingCodec handCrafting : handCraftingList) {
                List<ItemStack> input1 = ToolCodec.giveItemStacks(handCrafting.input1());

                List<ItemStack> input2 = ToolCodec.giveItemStacks(handCrafting.input2());

                List<ItemStack> output = ToolCodec.giveItemStacks(handCrafting.output());

                if(input1.stream().anyMatch(itemStack -> itemStack.equals(event.getPlayer().getMainHandItem())) && input2.stream().anyMatch(itemStack -> itemStack.equals(event.getPlayer().getOffhandItem()))){
                    if(event.getPlayer().getMainHandItem().getCount() == handCrafting.input1Amount() && event.getPlayer().getOffhandItem().getCount() == handCrafting.input2Amount()) {
                        ItemStack itemStack = output.get(0);
                        itemStack.setCount(handCrafting.outputAmount());

                        event.getPlayer().setItemInHand(InteractionHand.MAIN_HAND, itemStack);
                        event.getPlayer().setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                    }
                }
            }
        }
    }

    public static List<Set<Either<List<ItemStack>, ItemStack>>> convertAssembleItems(Integer step, Map<Integer, List<List<Either<String, ItemStack>>>> toolNeededIn){
        List<Set<Either<List<ItemStack>, ItemStack>>> map = new ArrayList<>();

        for(List<Either<String, ItemStack>> stepMap : toolNeededIn.get(step)) {
            Set<Either<List<ItemStack>, ItemStack>> itemStackSet = new HashSet<>();
            stepMap.forEach((either) -> {
                List<ItemStack> list = new ArrayList<>();
                //to-do make it work with tags it adds all items to list instead of just one
                either.ifLeft(s -> {
                    list.addAll(Objects.requireNonNull(ForgeRegistries.ITEMS.tags()).getTag(TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(s))).stream().map(ItemStack::new).toList());
                    itemStackSet.add(Either.left(list));
                });

                either.ifRight(itemStack -> itemStackSet.add(Either.right(itemStack)));
            });
            map.add(itemStackSet);
        }

        return map;
    }

    public static Set<ItemStack> checkAssembleItems(Inventory inventory, ItemStack mainHand, ItemStack offHand, List<Set<Either<List<ItemStack>, ItemStack>>> setIn){
        List<Set<Either<List<ItemStack>, ItemStack>>> filtered = new ArrayList<>();

        Set<ItemStack> firstFound = new HashSet<>();

        for(Set<Either<List<ItemStack>, ItemStack>> combinations : setIn){
            for(Either<List<ItemStack>, ItemStack> either : combinations) {
                either.ifLeft(list -> {
                    for (ItemStack itemStack : list) {
                        if (itemStack.equals(mainHand) || itemStack.equals(offHand) || inventory.contains(itemStack)) {
                            if(itemStack.getItem() instanceof TCToolItem tool){
                                if(tool.canWork(itemStack)) {
                                    filtered.add(combinations);
                                    break;
                                }
                            }else {
                                filtered.add(combinations);
                                break;
                            }
                        }
                    }
                });

                either.ifRight(itemStack -> {
                    if (itemStack.equals(mainHand) || itemStack.equals(offHand) || inventory.contains(itemStack)) {
                        if(itemStack.getItem() instanceof TCToolItem tool) {
                            if (tool.canWork(itemStack)) {
                                filtered.add(combinations);
                            }
                        }else{
                            filtered.add(combinations);
                        }


                    }
                });
            }
        }

        for(Set<Either<List<ItemStack>, ItemStack>> set : filtered) {
            //Set<ItemStack> dummy = new HashSet<>();
            for (int i = 0; i <= 45; i++) {
                ItemStack itemStack = inventory.getItem(i);

                if(!itemStack.isEmpty()) {
                    //todo the item stacks does not match because the tag from the set is null and the slots tag is "{}"
                    for(Either<List<ItemStack>, ItemStack> either : set){
                        either.ifLeft(list -> {
                            for (ItemStack itemStackDummy : list) {
                                if (itemStack.sameItem(itemStackDummy)) {
                                    if(itemStackDummy.getItem() instanceof TCToolItem tool){
                                        if(tool.canWork(itemStackDummy)){
                                            firstFound.add(itemStack);
                                            break;
                                        }
                                }else{
                                        firstFound.add(itemStack);
                                        break;
                                    }
                                }
                            }
                        });

                        either.ifRight(itemStackDummy -> {
                            if(itemStackDummy.getItem() instanceof TCToolItem tool){
                                if(tool.canWork(itemStackDummy)) {
                                    firstFound.add(itemStack);
                                }
                            }else{
                                if (itemStack.sameItem(itemStackDummy)) {
                                    firstFound.add(itemStack);
                                }
                            }
                        });
                    }
                }

                if (set.size() == firstFound.size()){
                    break;
                }
            }
        }

        return firstFound;
    }

    @SuppressWarnings("ALL")
    public static void handToolCrafting(TCToolItem toolItemIn, PlayerInteractEvent.RightClickItem event, int toolNeededDamage, Map<Integer, List<List<Either<String, ItemStack>>>> toolNeededIn) {
        Player playerEntity = event.getPlayer();
        ItemStack mainHand = playerEntity.getMainHandItem();
        ItemStack offHand = playerEntity.getOffhandItem();
        ItemStack toolItem = new ItemStack(toolItemIn.asItem());
        TCToolItemInventoryHelper itemStackHandler = toolItemIn.getInventory(toolItem);

        List<Item> headItems = toolItemIn.getHeads().values().stream().map(itemStack -> itemStack.getItem()).toList();
        List<Item> handleItems = toolItemIn.getHandles().values().stream().map(itemStack -> itemStack.getItem()).toList();
        List<Item> bindingItems = toolItemIn.getBindings().values().stream().map(itemStack -> itemStack.getItem()).toList();

        ItemStack mainHandCheck = headItems.contains(mainHand.getItem()) ? playerEntity.getMainHandItem() : (handleItems.contains(mainHand.getItem()) ? playerEntity.getMainHandItem() : null);
        ItemStack offHandCheck = headItems.contains(offHand.getItem()) ? playerEntity.getOffhandItem() : (handleItems.contains(offHand.getItem()) ? playerEntity.getOffhandItem() : null);

        if (toolItemIn.getParts() >= 2) {
            if (mainHandCheck != null && offHandCheck != null) {
                if (mainHandCheck.getCount() == 1 && (mainHandCheck.getDamageValue() != mainHandCheck.getMaxDamage() || !mainHandCheck.isDamageableItem()) && offHandCheck.getCount() == 1 && (offHandCheck.getDamageValue() != offHandCheck.getMaxDamage() || !offHandCheck.isDamageableItem())) {

                    Set<ItemStack> stepSet = checkAssembleItems(playerEntity.getInventory(), mainHand, offHand, convertAssembleItems(2, toolNeededIn));

                    if (stepSet.size() != 0 && inventoryToolCheck(playerEntity, stepSet)) {
                        ItemStack headItemStack = mainHandCheck.getItem() instanceof HeadItem ? mainHandCheck : offHandCheck.getItem() instanceof HeadItem ? offHandCheck : null;
                        ItemStack handleItemStack = mainHandCheck.getItem() instanceof HandleItem ? mainHandCheck : offHandCheck.getItem() instanceof HandleItem ? offHandCheck : null;

                        itemStackHandler.setToolHead(toolItem, headItemStack);

                        HeadItem toolHeadItem = (HeadItem) headItemStack.getItem();

                        toolItemIn.setHeadColor(toolItem, toolHeadItem.color);

                        if (toolItemIn.getParts() == 2) {
                            toolItemIn.setAttackDamage(toolItem, toolHeadItem.attackDamage);
                            toolItemIn.setEfficiency(toolItem, toolHeadItem.efficiency);
                            toolItemIn.setToolLevel(toolItem, toolHeadItem.tools);
                        }

                        if (handleItemStack == null) {
                            ItemStack stack = null;

                            if (mainHandCheck == mainHand) {
                                stack = mainHandCheck;
                            } else if (offHandCheck == offHand) {
                                stack = offHandCheck;
                            }

                            if (stack != null) {
                                if (TCJsonConfigs.itemTrait.getFirst().registryMap.containsKey(Objects.requireNonNull(stack.getItem().getRegistryName()).toString())) {
                                    ItemTraitCodec trait = (ItemTraitCodec) TCJsonConfigs.itemTrait.getFirst().registryMap.get(Objects.requireNonNull(stack.getItem().getRegistryName()).toString());
                                    Float weight = trait.kilograms().floatValue();

                                    if (!stack.isDamageableItem()) {
                                        CompoundTag tag = stack.getOrCreateTag();
                                        tag.putInt("damage", 0);
                                        tag.putInt("max_damage", trait.maxDamage());
                                        stack.setTag(tag);
                                    }

                                    itemStackHandler.setToolHandle(toolItem, stack);

                                    toolItemIn.setHandleColor(toolItem, trait.color());
                                    toolItemIn.setAttackSpeed(toolItem, weight + toolHeadItem.weight);
                                } else {
                                    TechAscension.LOGGER.warn("The item " + stack.getItem().getRegistryName() + " does not have any traits set and connot be used as a handle for tool " + toolItemIn.name);
                                }
                            }
                        } else {
                            HandleItem toolHandleItem = (HandleItem) handleItemStack.getItem();
                            toolItemIn.setHandleColor(toolItem, toolHandleItem.color);
                            toolItemIn.setAttackSpeed(toolItem, toolHandleItem.weight + toolHeadItem.weight);
                            itemStackHandler.setToolHandle(toolItem, handleItemStack);
                        }

                        if (inventoryToolCheck(playerEntity, stepSet)) {
                            toolItemIn.damageToolsNeededInPlayerInventory(playerEntity, event.getWorld(), toolNeededDamage, stepSet);
                        }

                        playerEntity.setItemInHand(InteractionHand.MAIN_HAND, toolItem);
                        playerEntity.setItemInHand(playerEntity.getMainHandItem().getItem() instanceof TCToolItem ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                    }
                }
            }
        }

        if (toolItemIn.getParts() >= 3){
            mainHand = playerEntity.getMainHandItem();
            offHand = playerEntity.getOffhandItem();

            mainHandCheck = bindingItems.contains(mainHand.getItem()) ? playerEntity.getMainHandItem() : (mainHand.getItem() instanceof TCToolItem ? playerEntity.getMainHandItem() : null);
            offHandCheck =  bindingItems.contains(offHand.getItem()) ? playerEntity.getOffhandItem() : (offHand.getItem() instanceof TCToolItem ? playerEntity.getOffhandItem() : null);

            itemStackHandler = mainHand.getItem() instanceof TCToolItem ? ((TCToolItem)mainHand.getItem()).getInventory(mainHand) : offHand.getItem() instanceof TCToolItem ? ((TCToolItem)offHand.getItem()).getInventory(offHand) : null;

            if(itemStackHandler != null) {
                if (mainHandCheck != null && offHandCheck != null){
                    if (mainHandCheck.getCount() == 1 && (mainHandCheck.getDamageValue() != mainHandCheck.getMaxDamage() || !mainHandCheck.isDamageableItem()) && offHandCheck.getCount() == 1 && (offHandCheck.getDamageValue() != offHandCheck.getMaxDamage() || !offHandCheck.isDamageableItem())) {
                        Set<ItemStack> stepSet = checkAssembleItems(playerEntity.getInventory(), mainHand, offHand, convertAssembleItems(3, toolNeededIn));

                        if (stepSet.size() != 0 && inventoryToolCheck(playerEntity, stepSet)) {
                            ItemStack handleItemStack = mainHandCheck.getItem() instanceof HandleItem ? mainHandCheck : offHandCheck.getItem() instanceof HandleItem ? offHandCheck : null;
                            ItemStack headItemStack = mainHandCheck.getItem() instanceof HeadItem ? mainHandCheck : offHandCheck.getItem() instanceof HeadItem ? offHandCheck : null;
                            ItemStack bindingItemStack = mainHandCheck.getItem() instanceof BindingItem ? mainHandCheck : offHandCheck.getItem() instanceof BindingItem ? offHandCheck : null;


                            toolItem = (mainHandCheck.getItem() instanceof TCToolItem ? mainHandCheck : offHandCheck);
                            TCToolItem newToolItem = (TCToolItem) toolItem.getItem();

                            if (bindingItemStack == null) {
                                ItemStack stack = null;
                                if (mainHandCheck == mainHand) {
                                    stack = mainHandCheck;
                                } else if (offHandCheck == offHand) {
                                    stack = offHandCheck;
                                }

                                if (stack != null) {
                                    if (TCJsonConfigs.itemTrait.getFirst().registryMap.containsKey(Objects.requireNonNull(stack.getItem().getRegistryName()).toString())){
                                        ItemTraitCodec trait = (ItemTraitCodec) TCJsonConfigs.itemTrait.getFirst().registryMap.get(Objects.requireNonNull(stack.getItem().getRegistryName()).toString());

                                        CompoundTag tag = stack.getOrCreateTag();

                                        if (!stack.isDamageableItem()) {

                                            tag.putInt("damage", 0);
                                            tag.putInt("max_damage", trait.maxDamage());
                                            stack.setTag(tag);
                                        }

                                        itemStackHandler.setToolBinding(toolItem, stack);
                                        Float weight = trait.kilograms().floatValue();

                                        newToolItem.setBindingColor(toolItem, trait.color());
                                        newToolItem.setAttackSpeed(toolItem, newToolItem.getAttackSpeed(toolItem) + weight);
                                    } else{
                                        TechAscension.LOGGER.warn("The item " + stack.getItem().getRegistryName() + " does not have any traits set and connot be used as a handle for tool " + toolItemIn.name);
                                    }
                                }
                            } else {
                                BindingItem toolBindingItem = (BindingItem) bindingItemStack.getItem();
                                toolItemIn.setBindingColor(toolItem, toolBindingItem.color);

                                newToolItem.setAttackSpeed(toolItem, newToolItem.getAttackSpeed(toolItem) + toolBindingItem.weight);

                                itemStackHandler.setToolBinding(toolItem, bindingItemStack);
                            }

                            if (toolItemIn.getParts() == 3) {
                                HeadItem headItem = (HeadItem) itemStackHandler.getToolHead(toolItem).getItem();

                                toolItemIn.setAttackDamage(toolItem, headItem.attackDamage);
                                toolItemIn.setEfficiency(toolItem, headItem.efficiency);
                                toolItemIn.setToolLevel(toolItem, headItem.tools);
                            }

                            if (inventoryToolCheck(playerEntity, stepSet)) {
                                toolItemIn.damageToolsNeededInPlayerInventory(playerEntity, event.getWorld(), toolNeededDamage, stepSet);
                            }

                            if (playerEntity.getMainHandItem().getItem() instanceof TCToolItem) {
                                playerEntity.setItemInHand(InteractionHand.MAIN_HAND, toolItem);
                                playerEntity.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                            } else {
                                playerEntity.setItemInHand(InteractionHand.OFF_HAND, toolItem);
                                playerEntity.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("ALL")
    public static boolean inventoryToolCheck(Player playerIn, Set<ItemStack> toolNeededIn){
        List<Item> toolNeededList = toolNeededIn.stream().map(itemStack -> itemStack.getItem()).collect(Collectors.toCollection(ArrayList::new));
        boolean debug = true;

        for(int i = 0; i <= 45; i++){
            ItemStack toolNeeded = playerIn.getInventory().getItem(i);
            if(toolNeededList.stream().anyMatch(itemStack -> itemStack == toolNeeded.getItem())){
                if(toolNeeded.getItem() instanceof TCToolItem){
                    TCToolItem toolNeededItem = (TCToolItem)playerIn.getInventory().getItem(i).getItem();
                    TCToolItemInventoryHelper itemStackHandler = toolNeededItem.getInventory(toolNeeded);

                    List<Boolean> test = new ArrayList<>();

                    if((toolNeededItem.getParts() >= 1) & itemStackHandler.getToolHead(toolNeeded) != null){
                        if(itemStackHandler.getToolHead(toolNeeded).getDamageValue() < itemStackHandler.getToolHead(toolNeeded).getMaxDamage()) {
                            test.add(true);
                        }
                    }

                    if((toolNeededItem.getParts() >= 2) & itemStackHandler.getToolHandle(toolNeeded) != null){
                        if(itemStackHandler.getToolHandle(toolNeeded).getDamageValue() < itemStackHandler.getToolHandle(toolNeeded).getDamageValue()){
                            test.add(true);
                        }
                    }

                    if(toolNeededItem.getParts() >= 3 & itemStackHandler.getToolBinding(toolNeeded) != null){
                        if(itemStackHandler.getToolBinding(toolNeeded).getDamageValue() < itemStackHandler.getToolBinding(toolNeeded).getDamageValue()){
                            test.add(true);
                        }
                    }

                    if(test.size() == toolNeededItem.getParts() || debug){
                        toolNeededIn.remove(toolNeeded.getItem());
                    }
                }else{
                    if(toolNeeded.getDamageValue() < toolNeeded.getMaxDamage() || debug) {
                        toolNeededList.remove(toolNeeded.getItem());
                    }
                }
            }

            if(toolNeededList.isEmpty()){
                break;
            }
        }
        return toolNeededList.isEmpty();
    }

    public static List<String> compareAndAddToNewArray(int numberIn, Map<String, Integer> pairArrayIn){
        List<String> newArray = new ArrayList<>();

        pairArrayIn.forEach((string, integer) -> {
            if(integer == numberIn) {
                newArray.add(string);
            }
        });

        return newArray;
    }

    public static ResourceLocation stringToResourceLocation(String stringIn){
        String modID = stringIn.split(":")[0];
        String item = stringIn.split(":")[1];

        return new ResourceLocation(modID, item);
    }

    public static List<Item> convertToItem(List<String> listIn){
        List<Item> list = new ArrayList<>();

        listIn.forEach(o -> {
            Item item = ForgeRegistries.ITEMS.getValue(stringToResourceLocation(o));

            if(item != null){
                list.add(item);
            }
        });

        return list;
    }
}
