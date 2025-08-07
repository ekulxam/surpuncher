package survivalblock.surpuncher.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import survivalblock.surpuncher.client.render.ExtendingFistRenderer;

public class SurpuncherClient implements ClientModInitializer {

    public static BipedEntityModel.ArmPose surpuncher$fistPose;

    @Override
    public void onInitializeClient() {
        WorldRenderEvents.AFTER_ENTITIES.register(ExtendingFistRenderer.INSTANCE);
    }
}
