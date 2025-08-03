package survivalblock.surpuncher.client.render.injected_interface;

import org.jetbrains.annotations.Nullable;
import survivalblock.surpuncher.common.component.ExtendingFist;

import java.util.List;

@SuppressWarnings("unused")
public interface FuryFists {

    default void surpuncher$setFists(List<ExtendingFist> fists) {
        throw new UnsupportedOperationException("Injected interface");
    }

    @Nullable
    default List<ExtendingFist> surpuncher$getFists() {
        throw new UnsupportedOperationException("Injected interface");
    }
}
