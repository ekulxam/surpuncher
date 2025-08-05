package survivalblock.surpuncher.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import survivalblock.surpuncher.client.render.ExtendingFistRenderer;
import survivalblock.surpuncher.common.component.ExtendingFist;
import survivalblock.surpuncher.common.init.SurpuncherEntityComponents;

import java.util.List;

import static survivalblock.surpuncher.common.component.ExtendingFist.BOX_EXPAND_VALUE;

public class SurpuncherClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            MatrixStack matrices = context.matrixStack();
            if (matrices == null) {
                return;
            }
            VertexConsumerProvider vertexConsumerProvider = context.consumers();
            if (vertexConsumerProvider == null) {
                return;
            }
            matrices.push();
            Vec3d cameraPos = context.camera().getCameraPos();
            matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
            EntityRenderDispatcher dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
            ClientWorld world = context.world();
            world.getPlayers().forEach(player -> {
                float tickProgress = MinecraftClient.getInstance()
                        .getRenderTickCounter()
                        .getTickProgress(!world.getTickManager().shouldSkipTick(player));
                List<ExtendingFist> fists = SurpuncherEntityComponents.EXTENDING_FIST.get(player).getImmutableFists();
                if (fists == null || fists.isEmpty()) {
                    return;
                }
                matrices.push();
                Vec3d pos = player.getLerpedPos(tickProgress);
                matrices.translate(pos.x, pos.y + player.getStandingEyeHeight(), pos.z);
                for (ExtendingFist fist : fists) {
                    matrices.push();
                    ExtendingFistRenderer.INSTANCE.applyEntityLikeTransforms(fist, matrices, tickProgress);
                    ExtendingFistRenderer.INSTANCE.render(fist, matrices, vertexConsumerProvider, dispatcher.getLight(player, tickProgress), tickProgress);
                    matrices.pop();
                    if (dispatcher.shouldRenderHitboxes()) {
                        matrices.push();
                        Vec3d relativePos = fist.lerpPos(tickProgress);
                        matrices.translate(relativePos.x, relativePos.y, relativePos.z);
                        VertexConsumer lines = vertexConsumerProvider.getBuffer(RenderLayer.getLines());
                        VertexRendering.drawBox(
                                matrices, lines, -BOX_EXPAND_VALUE, -BOX_EXPAND_VALUE, -BOX_EXPAND_VALUE, BOX_EXPAND_VALUE, BOX_EXPAND_VALUE, BOX_EXPAND_VALUE, 0, 1, 0, 1
                        );
                        matrices.pop();
                    }
                }
                matrices.pop();
            });
            matrices.pop();
        });
    }
}
