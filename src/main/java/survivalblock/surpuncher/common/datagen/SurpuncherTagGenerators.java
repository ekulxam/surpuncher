package survivalblock.surpuncher.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import survivalblock.surpuncher.common.init.SurpuncherItems;
import survivalblock.surpuncher.common.init.SurpuncherTags;

import java.util.concurrent.CompletableFuture;

public class SurpuncherTagGenerators {

    public static class ItemGenerator extends FabricTagProvider.ItemTagProvider {

        public ItemGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            this.builder(SurpuncherTags.ItemTags.FIST_ENCHANTABLE)
                    .add(SurpuncherItems.EXTENDING_FIST.getRegistryEntry().registryKey());
        }
    }
}
