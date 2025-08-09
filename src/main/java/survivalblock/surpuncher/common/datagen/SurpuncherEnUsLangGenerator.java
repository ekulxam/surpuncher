package survivalblock.surpuncher.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import survivalblock.surpuncher.common.init.SurpuncherEnchantments;
import survivalblock.surpuncher.common.init.SurpuncherGameRules;
import survivalblock.surpuncher.common.init.SurpuncherItems;

import java.util.concurrent.CompletableFuture;

public class SurpuncherEnUsLangGenerator extends FabricLanguageProvider {

    protected SurpuncherEnUsLangGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(SurpuncherItems.EXTENDING_FIST, "Extending Fist");

        translationBuilder.addEnchantment(SurpuncherEnchantments.FLURRY, "Flurry");

        translationBuilder.add(SurpuncherGameRules.SYNC_COOLDOWNS.getTranslationKey(), "Surpuncher: Sync Item Cooldown Updates To All Clients");
    }
}
