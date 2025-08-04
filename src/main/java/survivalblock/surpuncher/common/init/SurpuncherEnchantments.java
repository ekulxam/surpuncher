package survivalblock.surpuncher.common.init;

import com.google.common.collect.ImmutableMap;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import survivalblock.surpuncher.common.Surpuncher;

import java.util.Map;

public class SurpuncherEnchantments {

    public static final RegistryKey<Enchantment> FLURRY = RegistryKey.of(RegistryKeys.ENCHANTMENT, Surpuncher.id("flurry"));

    public static ImmutableMap<RegistryKey<Enchantment>, Enchantment> asEnchantments(Registerable<Enchantment> registerable) {
        RegistryEntryLookup<Item> item = registerable.getRegistryLookup(RegistryKeys.ITEM);
        ImmutableMap.Builder<RegistryKey<Enchantment>, Enchantment> enchantments = ImmutableMap.builder();
        enchantments.put(FLURRY, Enchantment.builder(Enchantment.definition(
                        item.getOrThrow(SurpuncherTags.ItemTags.FIST_ENCHANTABLE),
                        1,
                        5,
                        Enchantment.constantCost(1),
                        Enchantment.constantCost(100),
                        1,
                        AttributeModifierSlot.MAINHAND))
                .build(FLURRY.getValue()));
        return enchantments.build();
    }

    public static void bootstrap(Registerable<Enchantment> registerable) {
        for (Map.Entry<RegistryKey<Enchantment>, Enchantment> entry : asEnchantments(registerable).entrySet()) {
            registerable.register(entry.getKey(), entry.getValue());
        }
    }
}
