package survivalblock.surpuncher.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import survivalblock.surpuncher.common.Surpuncher;

import java.util.concurrent.CompletableFuture;

public class SurpuncherEnchantmentGenerator extends FabricDynamicRegistryProvider {

    public SurpuncherEnchantmentGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup, Entries entries) {
        RegistryWrapper.Impl<Enchantment> wrapper = wrapperLookup.getOrThrow(RegistryKeys.ENCHANTMENT);
        wrapper.streamKeys().forEachOrdered(key -> {
            if (!key.getValue().getNamespace().equals(Surpuncher.MOD_ID)) {
                return;
            }
            entries.add(key, wrapper.getOrThrow(key).value());
        });
    }

    @Override
    public String getName() {
        return "Enchantments";
    }
}
