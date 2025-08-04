package survivalblock.surpuncher.mixin.client;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.EntityHitbox;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
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

    @Inject(method = "appendHitboxes(Lnet/minecraft/entity/LivingEntity;Lcom/google/common/collect/ImmutableList$Builder;F)V", at = @At("RETURN"))
    private void fistInABox(LivingEntity living, ImmutableList.Builder<EntityHitbox> builder, float tickProgress, CallbackInfo ci) {
        if (!(living instanceof PlayerEntity player)) {
            return;
        }
        SurpuncherEntityComponents.EXTENDING_FIST.get(player).getImmutableFists().forEach(fist -> {
            Box box = fist.getHitbox();
            builder.add(new EntityHitbox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, 0.0F, 1.0F, 0.0F));
        });
    }
}
