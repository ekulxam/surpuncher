package survivalblock.surpuncher.common.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import survivalblock.surpuncher.common.component.ExtendingFist;
import survivalblock.surpuncher.common.component.ExtendingFistComponent;
import survivalblock.surpuncher.common.init.SurpuncherEnchantments;
import survivalblock.surpuncher.common.init.SurpuncherEntityComponents;
import survivalblock.surpuncher.common.init.SurpuncherItems;

import static survivalblock.surpuncher.common.component.ExtendingFist.VELOCITY_MULTIPLIER;

public class ExtendingFistItem extends Item {

    public ExtendingFistItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient()) {
            ItemStack stack = user.getStackInHand(hand);
            float pitch = user.getPitch();
            float yaw = user.getYaw();
            int flurry = EnchantmentHelper.getLevel(
                    world.getRegistryManager()
                    .getOrThrow(RegistryKeys.ENCHANTMENT)
                            .getOrThrow(SurpuncherEnchantments.FLURRY),
                    stack);
            Vec3d velocity = Vec3d.fromPolar(pitch, yaw);
            int color = stack.getOrDefault(DataComponentTypes.DYED_COLOR,
                            SurpuncherItems.DEFAULT_DYE_COMPONENT)
                    .rgb();
            ExtendingFistComponent extendingFistComponent = SurpuncherEntityComponents.EXTENDING_FIST.get(user);
            if (flurry > 0) {
                Random random = user.getRandom();
                for (int i = 0; i < flurry + 1; i++) {
                    Vec3d vec3d = velocity.addRandom(random, 0.04f).multiply(VELOCITY_MULTIPLIER);
                    //noinspection SuspiciousNameCombination
                    yaw = (float) MathHelper.atan2(vec3d.x, vec3d.z) * MathHelper.DEGREES_PER_RADIAN;
                    pitch = (float) (MathHelper.atan2(vec3d.y, vec3d.horizontalLength()) * MathHelper.DEGREES_PER_RADIAN);
                    extendingFistComponent.add(new ExtendingFist(vec3d, pitch, yaw, color));
                }
            } else {
                extendingFistComponent.add(
                        new ExtendingFist(velocity.multiply(VELOCITY_MULTIPLIER), pitch, yaw, color)
                );
            }
            return ActionResult.SUCCESS_SERVER;
        }
        return ActionResult.SUCCESS;
    }
}
