package survivalblock.surpuncher.mixin.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.surpuncher.client.render.ExtendingFistRenderer;
import survivalblock.surpuncher.client.render.injected_interface.FistItemRenderState;

@Mixin(ItemRenderState.class)
public class ItemRenderStateMixin implements FistItemRenderState {

    @Unique
    private boolean surpuncher$renderFist = false;

    @Unique
    private int surpuncher$fistColor = 0;

    @Override
    public boolean surpuncher$shouldRenderFist() {
        return this.surpuncher$renderFist;
    }

    @Override
    public void surpuncher$setShouldRenderFist(boolean renderFist) {
        this.surpuncher$renderFist = renderFist;
    }

    @Override
    public int surpuncher$getFistColor() {
        return this.surpuncher$fistColor;
    }

    @Override
    public void surpuncher$setFistColor(int color) {
        this.surpuncher$fistColor = color;
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void renderFist(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
        if (!this.surpuncher$shouldRenderFist()) {
            return;
        }
        ExtendingFistRenderer.INSTANCE.renderFromStack((ItemRenderState) (Object) this, matrices, vertexConsumers, light);
    }
}