package survivalblock.surpuncher.common.init;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class SurpuncherGameRules {

    public static final GameRules.Key<GameRules.BooleanRule> SYNC_COOLDOWNS = registerBool("surpuncher:syncCooldownsToOtherClients", true);

    public static GameRules.Key<GameRules.BooleanRule> registerBool(String name, boolean defaultValue) {
        return register(name, GameRuleFactory.createBooleanRule(defaultValue));
    }

    public static <T extends GameRules.Rule<T>> GameRules.Key<T> register(String name, GameRules.Type<T> type) {
        return GameRuleRegistry.register(name, GameRules.Category.PLAYER, type);
    }

    public static void init() {

    }
}
