package survivalblock.surpuncher.client.render;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.DefaultedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;
import survivalblock.surpuncher.common.Surpuncher;
import survivalblock.surpuncher.common.component.ExtendingFist;

import java.util.List;
import java.util.Map;

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
        float tickProgress = MinecraftClient.getInstance().getRenderTickCounter().getTickProgress(false);
        Vec3d entityPos = new Vec3d(state.x, state.y, state.z);
        GeoRenderState state1 = new GeoRenderStateImpl();
        for (ExtendingFist fist : fists) {
            Vec3d pos = fist.lerpPos(tickProgress);
            Vec3d relativePos = entityPos.subtract(pos);
            matrices.push();
            matrices.translate(relativePos.x, relativePos.y, relativePos.z);
            int color = fist.getColor(255);
            fillRenderState(fist, (Void) null, state1, tickProgress);
            state1.addGeckolibData(DataTickets.RENDER_COLOR, color);
            defaultRender(state1, matrices, vertexConsumers, null, null);
            matrices.pop();
        }
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

    public static class ExtendingFistModel extends DefaultedGeoModel<ExtendingFist> {

        public static final ExtendingFistModel INSTANCE = new ExtendingFistModel(Surpuncher.id("extending_fist"));

        private ExtendingFistModel(Identifier assetSubpath) {
            super(assetSubpath);
        }

        @Override
        protected String subtype() {
            return "entity";
        }
    }

    public static class GeoRenderStateImpl implements GeoRenderState {

        private final Map<DataTicket<?>, Object> storage = new Reference2ObjectOpenHashMap<>();

        @Override
        public <D> void addGeckolibData(DataTicket<D> dataTicket, @Nullable D data) {
            this.storage.put(dataTicket, data);
        }

        @Override
        public boolean hasGeckolibData(DataTicket<?> dataTicket) {
            return this.storage.containsKey(dataTicket);
        }

        @Override
        public <D> @Nullable D getGeckolibData(DataTicket<D> dataTicket) {
            Object obj = this.storage.get(dataTicket);
            if (obj == null) {
                return null;
            }
            try {
                return (D) obj;
            } catch (ClassCastException e) {
                Surpuncher.LOGGER.error("An error occurred while attempting to retrieve Geckolib data! DataTicket was {} and data was {}", dataTicket, obj);
                throw e;
            }
        }

        @Override
        public Map<DataTicket<?>, Object> getDataMap() {
            return this.storage;
        }
    }
}
