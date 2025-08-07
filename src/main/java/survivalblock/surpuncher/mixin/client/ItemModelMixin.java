package survivalblock.surpuncher.mixin.client;

import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.model.BasicItemModel;
import net.minecraft.client.render.item.model.SpecialItemModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.surpuncher.client.SurpuncherClient;

@Debug(export = true)
@Mixin({BasicItemModel.class, SpecialItemModel.class})
public class ItemModelMixin {

    /*@Inject(method = "update", at = @At("HEAD"))
    private void update(ItemRenderState state, ItemStack stack, ItemModelManager resolver, ItemDisplayContext displayContext, ClientWorld world, LivingEntity user, int seed, CallbackInfo ci) {
        SurpuncherClient.updateFistState(stack, state);
    }

     */
}
