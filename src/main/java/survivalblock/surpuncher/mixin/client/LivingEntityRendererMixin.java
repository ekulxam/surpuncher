package survivalblock.surpuncher.mixin.client;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.surpuncher.client.render.injected_interface.FuryFists;
import survivalblock.surpuncher.common.init.SurpuncherEntityComponents;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @Inject(method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V", at = @At("RETURN"))
    private void extend(LivingEntity living, LivingEntityRenderState state, float f, CallbackInfo ci) {
        if (!(state instanceof FuryFists furyFists)) {
            return;
        }
        if (!(living instanceof PlayerEntity player)) {
            furyFists.surpuncher$setFists(null);
            return;
        }
        furyFists.surpuncher$setFists(SurpuncherEntityComponents.EXTENDING_FIST.get(player).getImmutableFists());
    }
}
