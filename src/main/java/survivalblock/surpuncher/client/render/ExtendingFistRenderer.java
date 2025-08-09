package survivalblock.surpuncher.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.DefaultedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;
import survivalblock.surpuncher.common.Surpuncher;
import survivalblock.surpuncher.common.component.ExtendingFist;
import survivalblock.surpuncher.common.init.SurpuncherEntityComponents;
import survivalblock.surpuncher.common.item.ExtendingFistItem;
import survivalblock.surpuncher.mixin.client.ItemRenderStateAccessor;

import java.util.ArrayList;
import java.util.List;

import static survivalblock.surpuncher.common.Surpuncher.fastInvCos;
import static survivalblock.surpuncher.common.component.ExtendingFist.BOX_EXPAND_VALUE;
import static survivalblock.surpuncher.common.component.ExtendingFist.INVERSE_SEGMENTS;

@SuppressWarnings("UnstableApiUsage")
public class ExtendingFistRenderer implements GeoRenderer<ExtendingFist, Void, GeoRenderState>, WorldRenderEvents.AfterEntities {

    public static final ExtendingFistRenderer INSTANCE = new ExtendingFistRenderer();

    public static final RenderLayer LINES = RenderLayer.getLines();

    protected ExtendingFistRenderer() {
    }

    public void render(ExtendingFist fist, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float tickProgress) {
        render(fist, new GeoRenderState.Impl(), matrices, vertexConsumers, light, tickProgress);
    }

    public void render(ExtendingFist fist, GeoRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float tickProgress) {
        matrices.push();
        fillRenderState(fist, null, state, tickProgress);
        state.addGeckolibData(DataTickets.PACKED_LIGHT, light);
        defaultRender(state, matrices, vertexConsumers, null, null);
        matrices.pop();
    }

    public void renderFromStack(ItemRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light) {
        renderFromStack(state, matrices, vertexConsumerProvider, light, MinecraftClient.getInstance().getRenderTickCounter().getTickProgress(false));
    }

    public void renderFromStack(ItemRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light, float tickProgress) {
        ExtendingFist fist = new ExtendingFist(Vec3d.ZERO, 0, 0, state.surpuncher$getFistColor());
        matrices.push();
        applyTransforms(state, matrices);
        matrices.translate(0, -0.125, -0.5);
        matrices.scale(0.75f, 0.75f, 0.75f);
        render(fist, matrices, vertexConsumerProvider, light, tickProgress);
        matrices.pop();
    }

    private static void applyTransforms(ItemRenderState state, MatrixStack matrices) {
        if (state.isEmpty()) {
            return;
        }
        ItemRenderState.LayerRenderState layerRenderState = ((ItemRenderStateAccessor) state).surpuncher$invokeGetFirstLayer();
        Transformation transformation = ((ItemRenderStateAccessor.LayerRenderStateAccessor) layerRenderState).surpuncher$getTransform();
        if (transformation == Transformation.IDENTITY) {
            return;
        }
        transformation = new Transformation(transformation.rotation(), transformation.translation().mul(1, new Vector3f()), transformation.scale());
        boolean leftHand = ((ItemRenderStateAccessor) state).surpuncher$getDisplayContext().isLeftHand();
        transformation.apply(leftHand, matrices.peek());
        matrices.peek().translate(0.5F, 0.5F, 0.5F);
    }

    public void applyEntityLikeTransforms(ExtendingFist fist, MatrixStack matrices, float tickProgress) {
        Vec3d relativePos = fist.lerpPos(tickProgress);
        matrices.translate(relativePos.x, relativePos.y, relativePos.z);
        matrices.multiply(new Quaternionf()
                .rotationYXZ(-fist.getYaw() * MathHelper.RADIANS_PER_DEGREE + MathHelper.PI, // rotate by an extra pi radians because the model is backwards
                        -fist.getPitch() * MathHelper.RADIANS_PER_DEGREE,
                        0));
    }

    @Override
    public int getRenderColor(ExtendingFist fist, Void relatedObject, float partialTick) {
        return fist.getColor(255);
    }

    @Override
    public GeoModel<ExtendingFist> getGeoModel() {
        return ExtendingFistModel.INSTANCE;
    }

    @Override
    public void fireCompileRenderLayersEvent() {
    }

    @Override
    public void fireCompileRenderStateEvent(ExtendingFist animatable, Void relatedObject, GeoRenderState renderState) {

    }

    @Override
    public boolean firePreRenderEvent(GeoRenderState renderState, MatrixStack poseStack, BakedGeoModel model, VertexConsumerProvider bufferSource) {
        return true;
    }

    @Override
    public void firePostRenderEvent(GeoRenderState renderState, MatrixStack poseStack, BakedGeoModel model, VertexConsumerProvider bufferSource) {

    }

    @Override
    public void afterEntities(WorldRenderContext context) {
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
        //noinspection resource
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
            Vec3d pos = player.getLerpedPos(tickProgress).add(0, player.getStandingEyeHeight(), 0);
            float swingProgress = player.getHandSwingProgress(tickProgress);
            float h = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float)Math.PI);
            Vec3d handPos = getHandPos(player, h, tickProgress, dispatcher);
            for (ExtendingFist fist : fists) {
                matrices.push();
                int color = ExtendingFistRenderer.INSTANCE.getRenderColor(fist, null, tickProgress);
                matrices.translate(pos.x, pos.y, pos.z);
                {
                    matrices.push();
                    ExtendingFistRenderer.INSTANCE.applyEntityLikeTransforms(fist, matrices, tickProgress);
                    ExtendingFistRenderer.INSTANCE.render(fist, matrices, vertexConsumerProvider, dispatcher.getLight(player, tickProgress), tickProgress);
                    matrices.pop();

                    if (dispatcher.shouldRenderHitboxes()) {
                        matrices.push();
                        Vec3d relativePos = fist.lerpPos(tickProgress);
                        matrices.translate(relativePos.x, relativePos.y, relativePos.z);
                        VertexConsumer lines = vertexConsumerProvider.getBuffer(LINES);
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

                Vec3d fistPos = fist.lerpPos(tickProgress).add(pos);
                Vec3d direction = fistPos.subtract(handPos);
                Vec3d seg = direction.multiply(INVERSE_SEGMENTS);

                float pitch = fist.getPitch();
                float yaw = fist.getYaw();

                float angle = fastInvCos((float) (seg.length() * 0.5), ExtendingFist.SEGMENT_LENGTH) * MathHelper.DEGREES_PER_RADIAN;

                Vec3d up = Vec3d.fromPolar(pitch + angle, yaw).multiply(ExtendingFist.SEGMENT_LENGTH);
                Vec3d down = Vec3d.fromPolar(pitch - angle, yaw).multiply(ExtendingFist.SEGMENT_LENGTH);

                for (int i = 0; i < 2; i++) {
                    List<Vec3d> vec3ds = new ArrayList<>();
                    for (int j = 0; j < ExtendingFist.SEGMENTS; j++) {
                        Vec3d thisSegment = seg.multiply(j).add(handPos);
                        vec3ds.add(thisSegment);
                        vec3ds.add((i == 0 ? down : up).add(thisSegment));
                    }
                    vec3ds.add(fistPos);
                    renderPolygonalChain(vec3ds, matrices, vertexConsumerProvider, color);
                }
            }
            matrices.pop();
        });
        matrices.pop();
    }

    public static Vec3d getHandPos(PlayerEntity player, float angle, float tickProgress, EntityRenderDispatcher dispatcher) {
        int arm = ExtendingFistItem.getArmHoldingFist(player) == Arm.RIGHT ? 1 : -1;
        if (dispatcher.gameOptions.getPerspective().isFirstPerson() && player == MinecraftClient.getInstance().player) {
            return player.getCameraPosVec(tickProgress)
                    .add(dispatcher.camera.getProjection().getPosition(arm * 0.525F, -1F)
                            .multiply(960.0F / dispatcher.gameOptions.getFov().getValue())
                            .rotateX(-angle * 0.7F * arm));
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

    public static void renderPolygonalChain(List<Vec3d> positions, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int color) {
        if (positions.size() <= 1) {
            return;
        }
        Vec3d current;
        Vec3d previous = positions.getFirst();
        for (int i = 1; i < positions.size(); i++) {
            current = positions.get(i);
            matrices.push();
            matrices.translate(previous.x, previous.y, previous.z);
            VertexRendering.drawVector(matrices, vertexConsumerProvider.getBuffer(LINES), new Vector3f(), current.subtract(previous), color);
            matrices.pop();
            previous = current;
        }
    }

    public static final class ExtendingFistModel extends DefaultedGeoModel<ExtendingFist> {

        public static final ExtendingFistModel INSTANCE = new ExtendingFistModel();

        private ExtendingFistModel() {
            super(Surpuncher.id("extending_fist"));
        }

        @Override
        protected String subtype() {
            return "entity";
        }
    }
}
