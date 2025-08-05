package survivalblock.surpuncher.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class ExtendingFist implements GeoAnimatable {

    public static final int DEFAULT_MAX_LIFE = 60;
    public static final float MAX_LIFE_SECONDS = 3;
    public static final double VELOCITY_MULTIPLIER = 1;
    public static final double BOX_EXPAND_VALUE = 0.2;

    public static final Codec<ExtendingFist> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Vec3d.CODEC.fieldOf("velocity").forGetter(extendingFist -> extendingFist.velocity),
                            Vec3d.CODEC.fieldOf("relativePos").forGetter(extendingFist -> extendingFist.relativePos),
                            Vec3d.CODEC.fieldOf("prevPos").forGetter(extendingFist -> extendingFist.prevPos),
                            Codec.FLOAT.fieldOf("pitch").forGetter(extendingFist -> extendingFist.pitch),
                            Codec.FLOAT.fieldOf("yaw").forGetter(extendingFist -> extendingFist.yaw),
                            Codec.INT.fieldOf("color").forGetter(extendingFist -> extendingFist.color)
                    )
                    .apply(instance, ExtendingFist::new)
    );

    public static final Codec<List<ExtendingFist>> LIST_CODEC = CODEC.listOf();

    protected final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected final int maxLife;
    protected int life;
    protected Vec3d velocity;
    protected Vec3d relativePos;
    protected Vec3d prevPos;
    protected float pitch;
    protected float yaw;
    protected int color;

    public ExtendingFist(Vec3d velocity, float pitch, float yaw, int color) {
        this(velocity, Vec3d.ZERO, pitch, yaw, color);
    }

    public ExtendingFist(Vec3d velocity, Vec3d relativePos, float pitch, float yaw, int color) {
        this(velocity, relativePos, relativePos, pitch, yaw, color);
    }

    protected ExtendingFist(int maxLife, int life, Vec3d velocity, Vec3d relativePos, Vec3d prevPos, float pitch, float yaw, int color) {
        this.maxLife = maxLife;
        this.life = life;
        this.velocity = velocity;
        this.relativePos = relativePos;
        this.prevPos = prevPos;
        this.pitch = pitch;
        this.yaw = yaw;
        this.color = color;
    }

    protected ExtendingFist(Vec3d velocity, Vec3d relativePos, Vec3d prevPos, float pitch, float yaw, int color) {
        this(DEFAULT_MAX_LIFE, 0, velocity, relativePos, prevPos, pitch, yaw, color);
    }

    /**
     * Ticks the fist
     * @param owner the player who owns this fist
     * @param world the world, given by {@link PlayerEntity#getWorld()}
     * @return whether the fist should be discarded
     * @see ExtendingFistComponent#tick()
     */
    @SuppressWarnings("unused")
    public boolean tick(PlayerEntity owner, World world) {
        this.life++;
        if (this.life == this.maxLife * 0.5) {
            this.velocity = this.velocity.multiply(-1);
        } else if (this.life > this.maxLife) {
            return true;
        }
        this.prevPos = new Vec3d(this.relativePos.x, this.relativePos.y, this.relativePos.z);
        this.relativePos = this.relativePos.add(this.velocity);
        return false;
    }

    public Vec3d lerpPos(float delta) {
        return MathHelper.lerp(delta, this.prevPos, this.relativePos);
    }

    public int getColor(int alpha) {
        return ColorHelper.withAlpha(alpha, this.color);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        AnimationController<ExtendingFist> animController = new AnimationController<>("default", 5, event -> PlayState.STOP);

        registrar.add(animController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(@Nullable Object object) {
        return this.life;
    }

    public Box getHitbox(Vec3d ownerPos) {
        Vec3d pos = ownerPos.add(this.relativePos);
        return new Box(pos.subtract(BOX_EXPAND_VALUE), pos.add(BOX_EXPAND_VALUE));
    }

    public Box getHitbox() {
        return new Box(this.relativePos.subtract(BOX_EXPAND_VALUE), this.relativePos.add(BOX_EXPAND_VALUE));
    }

    public Vec3d getVelocity() {
        return this.velocity;
    }

    public float getPitch() {
        return this.pitch;
    }

    public float getYaw() {
        return this.yaw;
    }
}
