package survivalblock.surpuncher.common.init;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import survivalblock.surpuncher.common.Surpuncher;
import survivalblock.surpuncher.common.component.ExtendingFist;
import survivalblock.surpuncher.common.item.ExtendingFistItem;

import java.util.function.Function;

import static net.minecraft.component.DataComponentTypes.DYED_COLOR;

public class SurpuncherItems {

    public static final DyedColorComponent DEFAULT_DYE_COMPONENT = new DyedColorComponent(DyedColorComponent.DEFAULT_COLOR);

    public static final ExtendingFistItem EXTENDING_FIST = registerItem(
            "extending_fist",
            new Item.Settings()
                    .maxCount(1)
                    .rarity(Rarity.UNCOMMON)
                    .useCooldown(ExtendingFist.MAX_LIFE_SECONDS)
                    .attributeModifiers(AttributeModifiersComponent.builder()
                            .add(EntityAttributes.ATTACK_DAMAGE, new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, 1, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).build())
                    .component(DYED_COLOR, new DyedColorComponent(DyedColorComponent.DEFAULT_COLOR)),
            ExtendingFistItem::new);

    private static <T extends Item> T registerItem(String name, Item.Settings settings, Function<Item.Settings, T> itemFromSettings) {
        Identifier id = Surpuncher.id(name);
        return Registry.register(Registries.ITEM, id, itemFromSettings.apply(settings.registryKey(RegistryKey.of(RegistryKeys.ITEM, id))));
    }

    public static void init() {

    }
}
