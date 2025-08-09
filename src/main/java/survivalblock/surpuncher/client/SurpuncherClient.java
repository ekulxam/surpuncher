package survivalblock.surpuncher.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import survivalblock.surpuncher.client.render.ExtendingFistRenderer;
import survivalblock.surpuncher.common.networking.ArbitraryCooldownUpdateS2CPayload;

public class SurpuncherClient implements ClientModInitializer {

    public static BipedEntityModel.ArmPose surpuncher$fistPose;

    @Override
    public void onInitializeClient() {
        WorldRenderEvents.AFTER_ENTITIES.register(ExtendingFistRenderer.INSTANCE);

        ClientPlayNetworking.registerGlobalReceiver(ArbitraryCooldownUpdateS2CPayload.ID, (payload, context) -> {
            Entity entity = context.client().world.getEntityById(payload.entityId());
            if (!(entity instanceof PlayerEntity player)) {
                return;
            }
            if (payload.cooldown() == 0) {
                player.getItemCooldownManager().remove(payload.groupId());
            } else {
                player.getItemCooldownManager().set(payload.groupId(), payload.cooldown());
            }
        });
    }
}
