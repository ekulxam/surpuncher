package survivalblock.surpuncher.common.component;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;
import survivalblock.surpuncher.common.init.SurpuncherEntityComponents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExtendingFistComponent implements CommonTickingComponent, AutoSyncedComponent {

    protected final List<ExtendingFist> fists = new ArrayList<>();
    protected final PlayerEntity obj;

    protected boolean dirty;

    public ExtendingFistComponent(PlayerEntity player) {
        this.obj = player;
    }

    @Override
    public void tick() {
        World world = this.obj.getWorld();

        if (world instanceof ServerWorld serverWorld && !this.fists.isEmpty()) {
            Vec3d pos = this.obj.getEyePos();
            DamageSource playerAttack = serverWorld.getDamageSources().playerAttack(this.obj);
            this.fists.forEach(extendingFist -> {
                Vec3d fistPos = extendingFist.relativePos.add(pos);
                Vec3d velocity = extendingFist.getVelocity();
                EntityHitResult entityHitResult = extendingFist.getEntityCollision(
                        world, extendingFist.getHitbox(pos), this.obj, fistPos, fistPos.add(velocity));
                if (entityHitResult != null) {
                    Entity entity = entityHitResult.getEntity();
                    entity.damage(serverWorld, playerAttack, 4);
                    if (entity instanceof LivingEntity) {
                        entity.addVelocity(velocity.multiply(1.5));
                        if (entity instanceof ServerPlayerEntity serverPlayer) {
                            serverPlayer.networkHandler.sendPacket(
                                    new EntityVelocityUpdateS2CPacket(serverPlayer)
                            );
                        }
                    }
                }
            });
        }

        Iterator<ExtendingFist> itr = this.fists.iterator();
        ExtendingFist fist;
        while (itr.hasNext()) {
            fist = itr.next();
            boolean discard = fist.tick(this.obj, world);
            if (discard) {
                itr.remove();
                this.markDirty();
            }
        }
        if (this.dirty) {
            this.dirty = false;
            if (!world.isClient()) {
                SurpuncherEntityComponents.EXTENDING_FIST.sync(this.obj);
            }
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

    @SuppressWarnings("UnusedReturnValue")
    public boolean add(@NotNull ExtendingFist fist) {
        this.markDirty();
        return this.fists.add(fist);
    }
}
