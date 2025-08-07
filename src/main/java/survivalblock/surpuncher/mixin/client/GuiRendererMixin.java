package survivalblock.surpuncher.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.render.item.KeyedItemRenderState;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Debug(export = true)
@Mixin(GuiRenderer.class)
public class GuiRendererMixin {
    
    @Definition(id = "itemFrame", field = "Lnet/minecraft/client/gui/render/GuiRenderer$RenderedItem;frame:I")
    @Definition(id = "thisFrame", field = "Lnet/minecraft/client/gui/render/GuiRenderer;frame:I")
    @Expression("?.itemFrame != this.thisFrame")
    @ModifyExpressionValue(method = "method_71055", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean lambda$PrepareItemElements$1$1(boolean original, @Local KeyedItemRenderState state) {
        return original || state.surpuncher$shouldRenderFist();
    }
}
