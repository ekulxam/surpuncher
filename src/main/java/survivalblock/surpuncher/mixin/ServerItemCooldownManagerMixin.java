package survivalblock.surpuncher.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.s2c.play.CooldownUpdateS2CPacket;
import net.minecraft.server.network.ServerItemCooldownManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.surpuncher.common.init.SurpuncherGameRules;
import survivalblock.surpuncher.common.networking.ArbitraryCooldownUpdateS2CPayload;

@Mixin(ServerItemCooldownManager.class)
public abstract class ServerItemCooldownManagerMixin {

    @Shadow @Final private ServerPlayerEntity player;

    @SuppressWarnings("MixinAnnotationTarget")
    @WrapOperation(method = "*", at = @At(value = "NEW", target = "(Lnet/minecraft/util/Identifier;I)Lnet/minecraft/network/packet/s2c/play/CooldownUpdateS2CPacket;"))
    private CooldownUpdateS2CPacket onCooldownUpdate(Identifier groupId, int cooldown, Operation<CooldownUpdateS2CPacket> original) {
        CooldownUpdateS2CPacket value = original.call(groupId, cooldown);
        if (!this.player.getWorld().getGameRules().getBoolean(SurpuncherGameRules.SYNC_COOLDOWNS)) {
            return value;
        }
        ArbitraryCooldownUpdateS2CPayload payload = new ArbitraryCooldownUpdateS2CPayload(this.player, groupId, cooldown);
        PlayerLookup.tracking(this.player).forEach(serverPlayerEntity -> {
            if (serverPlayerEntity == this.player) return;
            ServerPlayNetworking.send(serverPlayerEntity, payload);
        });
        return value;
    }
}
