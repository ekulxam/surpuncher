package survivalblock.surpuncher.common;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
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
}