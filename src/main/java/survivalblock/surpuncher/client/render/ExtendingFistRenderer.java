package survivalblock.surpuncher.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import survivalblock.surpuncher.common.component.ExtendingFist;

import java.util.List;

public class ExtendingFistRenderer<S extends BipedEntityRenderState, M extends EntityModel<S>> extends FeatureRenderer<S, M> {

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
        for (ExtendingFist fist : fists) {
            Vec3d pos = fist.lerpPos(tickProgress);
            Vec3d relativePos = entityPos.subtract(pos);
            matrices.push();
            matrices.translate(relativePos.x, relativePos.y, relativePos.z);
            // render
            matrices.pop();
        }
    }
}
