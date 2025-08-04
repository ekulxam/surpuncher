package survivalblock.surpuncher.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
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

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class ExtendingFistRenderer<S extends BipedEntityRenderState, M extends EntityModel<S>> extends FeatureRenderer<S, M> implements GeoRenderer<ExtendingFist, Void, GeoRenderState> {

    public ExtendingFistRenderer(FeatureRendererContext<S, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, S state, float limbAngle, float limbDistance) {
        List<ExtendingFist> fists = state.surpuncher$getFists();
        if (fists == null || fists.isEmpty()) {
            return;
        }
        float tickProgress = MinecraftClient.getInstance()
                .getRenderTickCounter()
                .getTickProgress(false);
        GeoRenderState state1 = new GeoRenderState.Impl();
        for (ExtendingFist fist : fists) {
            Vec3d relativePos = fist.lerpPos(tickProgress);
            matrices.push();
            matrices.translate(relativePos.x, relativePos.y, relativePos.z);
            matrices.multiply(new Quaternionf()
                    .rotationYXZ(-fist.getYaw() * MathHelper.RADIANS_PER_DEGREE,
                            fist.getPitch() * MathHelper.RADIANS_PER_DEGREE,
                            0));
            fillRenderState(fist, null, state1, tickProgress);
            state1.addGeckolibData(DataTickets.PACKED_LIGHT, light);
            defaultRender(state1, matrices, vertexConsumers, null, null);
            matrices.pop();
        }
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
