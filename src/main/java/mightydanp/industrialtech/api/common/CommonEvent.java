package mightydanp.industrialtech.api.common;

import mightydanp.industrialtech.api.common.handler.NetworkHandler;
import mightydanp.industrialtech.api.common.inventory.container.ITToolItemContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Created by MightyDanp on 4/7/2021.
 */
public class CommonEvent {

    public static ContainerType<ITToolItemContainer> itToolItemContainerContainerType;

    public static void init(FMLCommonSetupEvent event) {
        NetworkHandler.onCommonSetup();
    }
}
