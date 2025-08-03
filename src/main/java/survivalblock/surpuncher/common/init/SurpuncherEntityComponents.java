package survivalblock.surpuncher.common.init;

import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import survivalblock.surpuncher.common.Surpuncher;
import survivalblock.surpuncher.common.component.ExtendingFistComponent;

public class SurpuncherEntityComponents implements EntityComponentInitializer {

    public static final ComponentKey<ExtendingFistComponent> EXTENDING_FIST = ComponentRegistry.getOrCreate(Surpuncher.id("extending_fist"), ExtendingFistComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(EXTENDING_FIST, ExtendingFistComponent::new, RespawnCopyStrategy.NEVER_COPY);
    }
}
