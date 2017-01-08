package com.sashimi.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sashimi.game.Sashimi;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Sashimi";
        config.width = 360;
        config.height = 640;
		new LwjglApplication(new Sashimi(), config);
	}
}
