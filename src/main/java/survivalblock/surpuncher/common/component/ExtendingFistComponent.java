package survivalblock.surpuncher.common.component;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.world.World;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;
import survivalblock.surpuncher.common.init.SurpuncherEntityComponents;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExtendingFistComponent implements CommonTickingComponent, AutoSyncedComponent {

    protected final List<ExtendingFist> fists = new ArrayList<>();
    protected final PlayerEntity obj;

    protected boolean dirty;

    public ExtendingFistComponent(PlayerEntity player) {
        this.obj = player;
    }

    @Override
    public void tick() {
        Iterator<ExtendingFist> itr = fists.iterator();
        World world = this.obj.getWorld();
        ExtendingFist fist;
        while (itr.hasNext()) {
            fist = itr.next();
            boolean discard = itr.next().tick(this.obj, world);
            if (discard) {
                this.fists.remove(fist);
                this.markDirty();
            }
        }
        if (!world.isClient() && this.dirty) {
            SurpuncherEntityComponents.EXTENDING_FIST.sync(this.obj);
        }
    }

    public void markDirty() {
        this.dirty = true;
    }

    @Override
    public void readData(ReadView readView) {
        readView.read("fists", ExtendingFist.LIST_CODEC).ifPresent(list -> {
            this.fists.clear();
            this.fists.addAll(list);
        });
    }

    @Override
    public void writeData(WriteView writeView) {
        writeView.put("fists", ExtendingFist.LIST_CODEC, this.fists);
    }

    public List<ExtendingFist> getImmutableFists() {
        return ImmutableList.copyOf(this.fists);
    }
}
