package survivalblock.surpuncher.client.render.injected_interface;

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
}
