package survivalblock.surpuncher.mixin.client;

import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import survivalblock.surpuncher.client.render.injected_interface.FuryFists;
import survivalblock.surpuncher.common.component.ExtendingFist;

import java.util.List;

@Mixin(BipedEntityRenderState.class)
public class BipedEntityRenderStateMixin implements FuryFists {

    @Unique
    List<ExtendingFist> surpuncher$fists = null;

    @Override
    public void surpuncher$setFists(List<ExtendingFist> fists) {
        this.surpuncher$fists = fists;
    }

    @Override
    @Nullable
    public List<ExtendingFist> surpuncher$getFists() {
        return this.surpuncher$fists;
    }
}
