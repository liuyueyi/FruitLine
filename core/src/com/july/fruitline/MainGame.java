package com.july.fruitline;

import com.badlogic.gdx.Game;
import com.july.fruitline.manager.FruitEffectManager;
import com.july.fruitline.manager.MusicManager;
import com.july.fruitline.manager.ToolEffectManager;
import com.july.fruitline.screen.Start;
import com.july.fruitline.util.Assets;

public class MainGame extends Game {
	public BushEvent event;
	public Start start;

	public MainGame(BushEvent event) {
		this.event = event;
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		start = new Start(this);
		setScreen(start);
	}

	@Override
	public void dispose() {
		System.out.println("main game exit!");

		ToolEffectManager.manager.freeAll();
		FruitEffectManager.manager.freeAll();
		Assets.instance.dispose();
		MusicManager.manager.dispose();
	}
}
