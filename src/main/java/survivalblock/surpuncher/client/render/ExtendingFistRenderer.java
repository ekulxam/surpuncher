package survivalblock.surpuncher.client.render;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.DefaultedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;
import survivalblock.surpuncher.common.Surpuncher;
import survivalblock.surpuncher.common.component.ExtendingFist;

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

    public void applyEntityLikeTransforms(ExtendingFist fist, MatrixStack matrices, float tickProgress) {
        Vec3d relativePos = fist.lerpPos(tickProgress);
        matrices.translate(relativePos.x, relativePos.y, relativePos.z);
        matrices.multiply(new Quaternionf()
                .rotationYXZ(-fist.getYaw() * MathHelper.RADIANS_PER_DEGREE,
                        fist.getPitch() * MathHelper.RADIANS_PER_DEGREE,
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
