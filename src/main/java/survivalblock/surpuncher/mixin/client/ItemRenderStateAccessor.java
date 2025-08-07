package survivalblock.surpuncher.mixin.client;

import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Debug(export = true)
@Mixin(ItemRenderState.class)
public interface ItemRenderStateAccessor {

    @Invoker("getFirstLayer")
    ItemRenderState.LayerRenderState surpuncher$invokeGetFirstLayer();

    @Accessor("displayContext")
    ItemDisplayContext surpuncher$getDisplayContext();

    @Mixin(ItemRenderState.LayerRenderState.class)
    interface LayerRenderStateAccessor {

        @Accessor("transform")
        Transformation surpuncher$getTransform();
    }
}