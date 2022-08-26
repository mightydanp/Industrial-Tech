package mightydanp.techcore.common.handler;

import mightydanp.techcore.common.libs.Ref;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

/**
 * Created by MightyDanp on 9/30/2020.
 */
@Mod.EventBusSubscriber(modid = Ref.mod_id)
public class EventHandler {

    @SubscribeEvent
    public static void playerPickup(EntityItemPickupEvent event){
        if(!event.getPlayer().getInventory().getItem(41).isEmpty()){
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public static void onItemRightClickEvent(PlayerInteractEvent.RightClickBlock event) {
        if(!event.getPlayer().getInventory().getItem(41).isEmpty()){
            if (event.getPlayer().isShiftKeyDown() && event.getPlayer().getOffhandItem().isEmpty() && event.getPlayer().getMainHandItem().isEmpty()) {
                if(event.getPlayer().getInventory().getItem(41).getItem() instanceof BlockItem blockItem) {

                    event.getWorld().setBlock(event.getPos().relative(Objects.requireNonNull(event.getFace())), blockItem.getBlock().defaultBlockState(), 2);
                    event.getPlayer().getInventory().setItem(41, ItemStack.EMPTY);
                }
            }
        }
    }

    public static BlockPos copyBlockPos = new BlockPos(0, 0, 0);

    public static boolean newPosition = false;

    public static boolean hasBeenPressed = false;


    /*
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void blockPlacementPreviewEvent(DrawHighlightEvent.HighlightBlock event) {
        Minecraft instance = Minecraft.getInstance();

        //fix by tick https://www.google.com/search?q=forge+get+tick+for+cooldown&oq=forge+get+tick+for+cooldown&aqs=chrome..69i57j33i160.20679j0j7&sourceid=chrome&ie=UTF-8
        if(KeyBindings.activateBlockPreViewer.keyBinding.isDown()){
            if(!hasBeenPressed){
                hasBeenPressed = true;
            }else{
                hasBeenPressed = false;
            }
        }

        if(hasBeenPressed && instance.player != null && instance.player.level != null && !instance.player.getMainHandItem().isEmpty() && instance.player.getMainHandItem().getItem() instanceof BlockItem) {
            LocalPlayer player = instance.player;
            Level world = player.level;

            int rayLength = 100;
            Vec3 playerRotation = player.getViewVector(0);
            Vec3 rayPath = playerRotation.scale(rayLength);

            //RAY START AND END POINTS
            Vec3 from = player.getEyePosition(0);
            Vec3 to = from.add(rayPath);

            //CREATE THE RAY
            ClipContext rayCtx = new ClipContext(from, to, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, null);
            //CAST THE RAY
            BlockHitResult rayHit = world.clip(rayCtx);

            //CHECK THE RESULTS
            if (event.getMatrix() == null || rayHit.getType() == HitResult.Type.MISS){
                return;
            }

            Vec3 hitLocation = rayHit.getLocation();

            BlockPos blockPos = new BlockPos(hitLocation.x, hitLocation.y, hitLocation.z);

            if(copyBlockPos.getX() != blockPos.getX()){
                newPosition = true;
            }

            if(copyBlockPos.getY() != blockPos.getY()){
                newPosition = true;
            }

            if(copyBlockPos.getZ() != blockPos.getZ()){
                newPosition = true;
            }

            UseOnContext itemUseContext = new UseOnContext(player, InteractionHand.MAIN_HAND, rayHit);

            BlockState blockStatePlacement = ((BlockItem)player.getMainHandItem().getItem()).getBlock().getStateForPlacement(new BlockPlaceContext(itemUseContext));

            copyBlockPos = copyBlockPos.equals(new BlockPos(0, 0, 0)) ? blockPos : copyBlockPos;

            if (newPosition && blockStatePlacement != null){
                double x = (blockPos.getX() - (player.getPosition(0).x) - (itemUseContext.getClickedFace() == Direction.WEST ? 1 : 0));
                double y = (-1.62 + (blockPos.getY() - (player.getPosition(0).y)) - (itemUseContext.getClickedFace() == Direction.DOWN ? 1 : 0));
                double z = (blockPos.getZ() - (player.getPosition(0).z) - (itemUseContext.getClickedFace() == Direction.NORTH ? 1 : 0));

                RenderType blockStateRenderType = ItemBlockRenderTypes.getRenderType(blockStatePlacement, false);
                event.getMatrix().pushPose();
                event.getMatrix().scale(1, 1, 1);
                event.getMatrix().translate(x, y, z);

                instance.getBlockRenderer().renderModel(blockStatePlacement, blockPos.relative(itemUseContext.getClickedFace(), 0), world, event.getMatrix(), event.getBuffers().getBuffer(blockStateRenderType), false, new Random(), EmptyModelData.INSTANCE);

                event.getMatrix().popPose();

                //player.sendMessage(ITextComponent.nullToEmpty(itemUseContext.getClickedFace().getName() + ", "+ x + ", " + y + ", "+ z), player.getUUID());
            }

            copyBlockPos = blockPos;
        }
    }

     */

}