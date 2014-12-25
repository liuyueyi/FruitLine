package com.july.fruitline.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.july.fruitline.BushEvent;
import com.july.fruitline.MainGame;

public class DesktopLauncher {
	public static BushEvent event = new BushEvent() {
		@Override
		public void notify(Object o, int tag) {
			System.out.println("tag" + tag);
		}
	};

	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 480;
		config.height = 800;
		new LwjglApplication(new MainGame(event), config);
	}
}
