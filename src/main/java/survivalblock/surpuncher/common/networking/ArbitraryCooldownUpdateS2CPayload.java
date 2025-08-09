package survivalblock.surpuncher.common.networking;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import survivalblock.surpuncher.common.Surpuncher;

public record ArbitraryCooldownUpdateS2CPayload(int entityId, Identifier groupId, int cooldown) implements CustomPayload {

    public static final Id<ArbitraryCooldownUpdateS2CPayload> ID = new Id<>(Surpuncher.id("arbitrary_cooldown_update_s2c"));

    public static final PacketCodec<RegistryByteBuf, ArbitraryCooldownUpdateS2CPayload> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT, payload -> payload.entityId,
            Identifier.PACKET_CODEC, payload -> payload.groupId,
            PacketCodecs.VAR_INT, payload -> payload.cooldown,
            ArbitraryCooldownUpdateS2CPayload::new
    );

    public ArbitraryCooldownUpdateS2CPayload(PlayerEntity player, Identifier groupId, int cooldown) {
        this(player.getId(), groupId, cooldown);
    }

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
