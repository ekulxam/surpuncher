package survivalblock.surpuncher.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;
import survivalblock.surpuncher.client.render.ExtendingFistRenderer;
import survivalblock.surpuncher.common.component.ExtendingFist;
import survivalblock.surpuncher.common.init.SurpuncherEntityComponents;
import survivalblock.surpuncher.common.init.SurpuncherItems;
import survivalblock.surpuncher.common.item.ExtendingFistItem;

import java.util.List;

import static survivalblock.surpuncher.common.component.ExtendingFist.BOX_EXPAND_VALUE;

public class SurpuncherClient implements ClientModInitializer {

    public static BipedEntityModel.ArmPose surpuncher$fistPose;

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
                for (ExtendingFist fist : fists) {
                    matrices.push();
                    matrices.translate(pos.x, pos.y + player.getStandingEyeHeight(), pos.z);
                    {
                        matrices.push();
                        ExtendingFistRenderer.INSTANCE.applyEntityLikeTransforms(fist, matrices, tickProgress);
                        ExtendingFistRenderer.INSTANCE.render(fist, matrices, vertexConsumerProvider, dispatcher.getLight(player, tickProgress), tickProgress);
                        matrices.pop();
                        if (dispatcher.shouldRenderHitboxes()) {
                            matrices.push();
                            int color = ExtendingFistRenderer.INSTANCE.getRenderColor(fist, null, tickProgress);
                            Vec3d relativePos = fist.lerpPos(tickProgress);
                            matrices.translate(relativePos.x, relativePos.y, relativePos.z);
                            VertexConsumer lines = vertexConsumerProvider.getBuffer(RenderLayer.getLines());
                            VertexRendering.drawBox(
                                    matrices, lines,
                                    -BOX_EXPAND_VALUE, -BOX_EXPAND_VALUE, -BOX_EXPAND_VALUE,
                                    BOX_EXPAND_VALUE, BOX_EXPAND_VALUE, BOX_EXPAND_VALUE,
                                    ColorHelper.getRedFloat(color),
                                    ColorHelper.getGreenFloat(color),
                                    ColorHelper.getBlueFloat(color),
                                    ColorHelper.getAlphaFloat(color)
                            );
                            matrices.pop();
                        }
                    }
                    matrices.pop();

                    matrices.push();
                    float g = player.getHandSwingProgress(tickProgress);
                    float h = MathHelper.sin(MathHelper.sqrt(g) * (float)Math.PI);
                    Vec3d handPos = getHandPos(player, h, tickProgress, dispatcher);
                    matrices.translate(handPos.x, handPos.y, handPos.z);
                    Vec3d fistPos = fist.lerpPos(tickProgress).add(pos).add(0, player.getStandingEyeHeight(), 0);
                    VertexRendering.drawVector(matrices, vertexConsumerProvider.getBuffer(RenderLayer.getLines()), new Vector3f(), fistPos.subtract(handPos), 0xFFFFFFFF);
                    matrices.pop();
                }
                matrices.pop();
            });
            matrices.pop();
        });
    }

    public static void updateFistState(ItemStack stack, ItemRenderState renderState, ItemDisplayContext displayContext) {
        if (stack.isOf(SurpuncherItems.EXTENDING_FIST)) {
            boolean renderFist;
            if (displayContext == ItemDisplayContext.GUI) {
                renderFist = true;
            } else {
                ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;
                if (clientPlayer == null) {
                    renderFist = true;
                } else {
                    renderFist = !clientPlayer.getItemCooldownManager().isCoolingDown(stack);
                }
            }
            renderState.surpuncher$setShouldRenderFist(renderFist);
            renderState.addModelKey(renderFist);
            if (renderFist) {
                int color = ExtendingFistItem.getColor(stack);
                renderState.surpuncher$setFistColor(color);
                renderState.addModelKey(color);
            } else {
                renderState.surpuncher$setFistColor(0);
            }
            return;
        }
        renderState.surpuncher$setShouldRenderFist(false);
        renderState.surpuncher$setFistColor(0);
    }

    public static Arm getArmHoldingRod(PlayerEntity player) {
        return player.getMainHandStack().isOf(SurpuncherItems.EXTENDING_FIST) ? player.getMainArm() : player.getMainArm().getOpposite();
    }

    public static Vec3d getHandPos(PlayerEntity player, float angle, float tickProgress, EntityRenderDispatcher dispatcher) {
        int arm = getArmHoldingRod(player) == Arm.RIGHT ? 1 : -1;
        if (dispatcher.gameOptions.getPerspective().isFirstPerson() && player == MinecraftClient.getInstance().player) {
            return player.getCameraPosVec(tickProgress)
                    .add(dispatcher.camera.getProjection().getPosition(arm * 0.525F, -0.1F)
                            .multiply(960.0F / dispatcher.gameOptions.getFov().getValue())
                            .rotateY(angle * 0.5F)
                            .rotateX(-angle * 0.7F))
                    .subtract(0, 0.6, 0);
        } else {
            float yaw = (MathHelper.lerp(tickProgress, player.lastBodyYaw, player.bodyYaw)) * MathHelper.RADIANS_PER_DEGREE;

            double sinYaw = MathHelper.sin(yaw);
            double cosYaw = MathHelper.cos(yaw);

            float scale = player.getScale();
            float sideOffset = arm * 0.35F * scale;
            double forwardOffset = 0.8 * scale;
            float verticalOffset = player.isInSneakingPose() ? -0.1875F : 0.0F;
            return player.getCameraPosVec(tickProgress)
                    .add(-cosYaw * sideOffset - sinYaw * forwardOffset, verticalOffset - 0.45F * scale, -sinYaw * sideOffset + cosYaw * forwardOffset);
        }
    }
}
