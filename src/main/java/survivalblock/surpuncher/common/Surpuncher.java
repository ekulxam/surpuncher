package survivalblock.surpuncher.common;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import survivalblock.surpuncher.common.init.SurpuncherItems;

public class Surpuncher implements ModInitializer {
	
	public static final String MOD_ID = "surpuncher";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		SurpuncherItems.init();
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	public static float fastInvCos(float adj, float hyp) {
		return (float) fastInvCos(adj, (double) hyp);
	}

	public static double fastInvCos(double adj, double hyp) {
		return fastInvCos(adj / hyp);
	}

	/**
	 * Calculates arccos(x) using {@link MathHelper#atan2(double, double)}
	 * @param x the cosine value
	 * @return the corresponding angle in radians
	 */
	public static double fastInvCos(double x) {
		return MathHelper.atan2(Math.sqrt(1 - x * x), x);
	}
}