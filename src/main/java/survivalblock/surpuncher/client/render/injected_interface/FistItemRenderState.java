package survivalblock.surpuncher.client.render.injected_interface;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

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

    default void surpuncher$updateFistState(ItemStack stack, ItemDisplayContext displayContext, @Nullable PlayerEntity player) {
        throw new UnsupportedOperationException("Injected interface");
    }
}
