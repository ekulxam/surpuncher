package survivalblock.surpuncher.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ExtendingFist {

    public static final Codec<ExtendingFist> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Vec3d.CODEC.fieldOf("velocity").forGetter(extendingFist -> extendingFist.velocity),
                            Vec3d.CODEC.fieldOf("pos").forGetter(extendingFist -> extendingFist.pos),
                            Vec3d.CODEC.fieldOf("prevPos").forGetter(extendingFist -> extendingFist.prevPos),
                            Codec.FLOAT.fieldOf("pitch").forGetter(extendingFist -> extendingFist.pitch),
                            Codec.FLOAT.fieldOf("yaw").forGetter(extendingFist -> extendingFist.yaw),
                            Codec.INT.fieldOf("color").forGetter(extendingFist -> extendingFist.color)
                    )
                    .apply(instance, ExtendingFist::new)
    );

    public static final Codec<List<ExtendingFist>> LIST_CODEC = CODEC.listOf();

    protected final int maxLife;
    protected int life = 0;
    protected Vec3d velocity;
    protected Vec3d pos;
    protected Vec3d prevPos;
    protected float pitch;
    protected float yaw;
    protected int color;

    public ExtendingFist(Vec3d velocity, Vec3d pos, float pitch, float yaw) {
        this(velocity, pos, pitch, yaw, 0xFFFF0000);
    }

    public ExtendingFist(Vec3d velocity, Vec3d pos, float pitch, float yaw, int color) {
        this(velocity, pos, pos, pitch, yaw, color);
    }

    protected ExtendingFist(int maxLife, int life, Vec3d velocity, Vec3d pos, Vec3d prevPos, float pitch, float yaw, int color) {
        this.maxLife = maxLife;
        this.life = life;
        this.velocity = velocity;
        this.pos = pos;
        this.prevPos = prevPos;
        this.pitch = pitch;
        this.yaw = yaw;
        this.color = color;
    }

    protected ExtendingFist(Vec3d velocity, Vec3d pos, Vec3d prevPos, float pitch, float yaw, int color) {
        this(120, 0, velocity, pos, prevPos, pitch, yaw, color);
    }

    // returns whether the fist should be discarded
    @SuppressWarnings("unused")
    public boolean tick(PlayerEntity owner, World world) {
        this.life++;
        if (this.life == this.maxLife * 0.5) {
            this.velocity = this.velocity.multiply(-1);
        } else if (this.life > this.maxLife) {
            return true;
        }
        this.prevPos = new Vec3d(this.pos.x, this.pos.y, this.pos.z);
        this.pos = this.pos.add(this.velocity);
        return false;
    }

    public Vec3d lerpPos(float delta) {
        return MathHelper.lerp(delta, this.prevPos, this.pos);
    }
}
