package survivalblock.surpuncher.common.init;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import survivalblock.surpuncher.common.Surpuncher;

public class SurpuncherTags {

    public static final class ItemTags {
        public static final TagKey<Item> FIST_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, Surpuncher.id("fist_enchantable"));
    }
}
