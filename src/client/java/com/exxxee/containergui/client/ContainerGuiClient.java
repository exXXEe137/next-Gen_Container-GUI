package com.exxxee.containergui.client;

import com.exxxee.containergui.client.network.ClientNetWorking;
import net.fabricmc.api.ClientModInitializer;

public class ContainerGuiClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientNetWorking.register();
	}
}
