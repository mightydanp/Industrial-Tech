package mightydanp.industrialcore.common.items;

import net.minecraft.world.item.Item;

/**
 * Created by MightyDanp on 10/10/2020.
 */
public class BasicItem extends Item {
    public BasicItem(Properties properties) {
        super(properties);
        properties.tab(ModItemGroups.item_tab);
    }


}
