package survivalblock.surpuncher.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
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
import survivalblock.surpuncher.common.init.SurpuncherItems;
import survivalblock.surpuncher.mixin.client.ItemRenderStateAccessor;

@SuppressWarnings("UnstableApiUsage")
public class ExtendingFistRenderer implements GeoRenderer<ExtendingFist, Void, GeoRenderState> {

    public static final ExtendingFistRenderer INSTANCE = new ExtendingFistRenderer();

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
