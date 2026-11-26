package com.exxxee.containergui;

import com.exxxee.containergui.Network.ServerNetWorking;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContainerGui implements ModInitializer {
	public static final String MOD_ID = "containergui";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ServerNetWorking.register();
	}
}
