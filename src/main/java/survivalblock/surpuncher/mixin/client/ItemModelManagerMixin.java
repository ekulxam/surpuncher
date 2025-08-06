package survivalblock.surpuncher.mixin.client;

import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.surpuncher.common.init.SurpuncherItems;
import survivalblock.surpuncher.common.item.ExtendingFistItem;

@Mixin(ItemModelManager.class)
public class ItemModelManagerMixin {

    @Inject(method = "update", at = @At("HEAD"))
    private void updateFist(ItemRenderState renderState, ItemStack stack, ItemDisplayContext displayContext, World world, LivingEntity entity, int seed, CallbackInfo ci) {
        if (stack.isOf(SurpuncherItems.EXTENDING_FIST)) {
            renderState.surpuncher$setShouldRenderFist(true);
            renderState.surpuncher$setFistColor(ExtendingFistItem.getColor(stack));
            return;
        }
        renderState.surpuncher$setShouldRenderFist(false);
        renderState.surpuncher$setFistColor(0);
    }
}
