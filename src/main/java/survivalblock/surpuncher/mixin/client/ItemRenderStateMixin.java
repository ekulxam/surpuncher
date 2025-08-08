package survivalblock.surpuncher.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.surpuncher.client.render.ExtendingFistRenderer;
import survivalblock.surpuncher.client.render.injected_interface.FistItemRenderState;
import survivalblock.surpuncher.common.init.SurpuncherItems;
import survivalblock.surpuncher.common.item.ExtendingFistItem;

@Mixin(ItemRenderState.class)
public abstract class ItemRenderStateMixin implements FistItemRenderState {

    @Shadow public abstract void addModelKey(Object modelKey);

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

    @Inject(method = "clear", at = @At("RETURN"))
    private void clearSurpuncherData(CallbackInfo ci) {
        this.surpuncher$fistColor = 0;
        this.surpuncher$renderFist = false;
    }

    @Override
    public void surpuncher$updateFistState(ItemStack stack, ItemDisplayContext displayContext) {
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
            this.surpuncher$setShouldRenderFist(renderFist);
            this.addModelKey(renderFist);
            if (renderFist) {
                int color = ExtendingFistItem.getColor(stack);
                this.surpuncher$setFistColor(color);
                this.addModelKey(color);
            } else {
                this.surpuncher$setFistColor(0);
            }
            return;
        }
        this.surpuncher$setShouldRenderFist(false);
        this.surpuncher$setFistColor(0);
    }
}