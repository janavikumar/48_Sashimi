package com.sashimi.game.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.sashimi.game.Sashimi;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(360, 640);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new Sashimi();
        }
}