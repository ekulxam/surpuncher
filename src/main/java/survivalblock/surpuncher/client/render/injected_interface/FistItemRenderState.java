package survivalblock.surpuncher.client.render.injected_interface;

import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;

public interface FistItemRenderState {

    default boolean surpuncher$shouldRenderFist() {
        throw new UnsupportedOperationException("Injected interface");
    }

    default void surpuncher$setShouldRenderFist(boolean renderFist) {
        throw new UnsupportedOperationException("Injected interface");
    }

    default int surpuncher$getFistColor() {
        throw new UnsupportedOperationException("Injected interface");
    }

    default void surpuncher$setFistColor(int color) {
        throw new UnsupportedOperationException("Injected interface");
    }

    default void surpuncher$updateFistState(ItemStack stack, ItemDisplayContext displayContext) {
        throw new UnsupportedOperationException("Injected interface");
    }
}
