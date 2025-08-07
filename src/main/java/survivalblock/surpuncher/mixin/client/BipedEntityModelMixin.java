package survivalblock.surpuncher.mixin.client;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;

import static survivalblock.surpuncher.client.SurpuncherClient.surpuncher$fistPose;

@Mixin(BipedEntityModel.class)
public class BipedEntityModelMixin {

    @Shadow @Final public ModelPart leftArm;

    @Shadow @Final public ModelPart rightArm;

    @Inject(method = "positionRightArm", at = @At("HEAD"), cancellable = true)
    private void holdRightFist(BipedEntityRenderState state, BipedEntityModel.ArmPose armPose, CallbackInfo ci) {
        if (armPose == surpuncher$fistPose) {
            surpuncher$zombieArms(this.rightArm, true, state.handSwingProgress);
            ci.cancel();
        }
    }

    @Inject(method = "positionLeftArm", at = @At("HEAD"), cancellable = true)
    private void holdLeftFist(BipedEntityRenderState state, BipedEntityModel.ArmPose armPose, CallbackInfo ci) {
        if (armPose == surpuncher$fistPose) {
            surpuncher$zombieArms(this.leftArm, false, state.handSwingProgress);
            ci.cancel();
        }
    }

    @Unique
    void surpuncher$zombieArms(ModelPart arm, boolean right, float swingProgress) {
        float f = MathHelper.sin(swingProgress * (float) Math.PI);
        float g = MathHelper.sin((1.0F - (1.0F - swingProgress) * (1.0F - swingProgress)) * (float) Math.PI);
        float h = (float) -Math.PI / (2.25F);
        if (right) {
            arm.roll = 0.0F;
            arm.yaw = -(0.1F - f * 0.6F);
            arm.pitch = h;
            arm.pitch += f * 1.2F - g * 0.4F;
        } else {
            arm.roll = 0.0F;
            arm.yaw = 0.1F - f * 0.6F;
            arm.pitch = h;
            arm.pitch += f * 1.2F - g * 0.4F;
        }
    }

    @Mixin(BipedEntityModel.ArmPose.class)
    public static class ArmPoseMixin {

        @Shadow @Final private static BipedEntityModel.ArmPose[] field_3404;

        @Invoker("<init>")
        static BipedEntityModel.ArmPose surpuncher$invokeInit(String name, int id, final boolean twoHanded) {
            throw new UnsupportedOperationException();
        }

        static {
            ArrayList<BipedEntityModel.ArmPose> list = new ArrayList<>(Arrays.asList(field_3404));
            int size = list.size();
            surpuncher$fistPose = surpuncher$invokeInit("spud_slingers_cannon_pose", size, false);
            list.add(surpuncher$fistPose);
            field_3404 = list.toArray(new BipedEntityModel.ArmPose[size + 1]);
        }
    }
}