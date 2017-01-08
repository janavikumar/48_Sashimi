package com.sashimi.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.sashimi.game.Sashimi;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        // Saves battery by disabling accelerometer and compass
		config.useAccelerometer = false;
		config.useCompass = false;

		initialize(new Sashimi(), config);
	}
}
