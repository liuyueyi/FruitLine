package com.july.fruitline.manager;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.july.fruitline.sprite.FruitEffect;
import com.july.fruitline.util.Constants;

public class FruitEffectManager {
	public static FruitEffectManager manager = new FruitEffectManager();

	Pool<FruitEffect> fruitEffectPool;
	Array<FruitEffect> fruitEffectArray;

	private FruitEffectManager() {
	}

	public void init() {
		fruitEffectPool = Pools.get(FruitEffect.class);
		fruitEffectArray = new Array<FruitEffect>();
	}

	/**
	 * 自动消除所有无效的对象
	 */
	public void autoFree() {
		for (FruitEffect f : fruitEffectArray) {
			if (f.ifFree()) {
				fruitEffectArray.removeValue(f, true);
				fruitEffectPool.free(f);
			}
		}
	}

	public void show(boolean showToolEffect, int tool, int value, float x,
			float y) {
		autoFree();
		if (tool > -1)
			MusicManager.manager.playSound(tool);

		if (showToolEffect || tool == Constants.TIME_TOOL)
			switch (tool) {
			case -1:
				break;
			default:
				ToolEffectManager.manager.show(x, y, tool);
				break;
			}

		for (int i = 0; i < 6; i++) {
			FruitEffect fruit = fruitEffectPool.obtain();
			fruit.init(value, x, y);
			fruitEffectArray.add(fruit);
		}
	}

	public void draw(Batch batch) {
		for (FruitEffect fruit : fruitEffectArray) {
			fruit.draw(batch);
		}
	}

	public void freeAll() {
		fruitEffectPool.freeAll(fruitEffectArray);
		fruitEffectPool.clear();
		fruitEffectPool = null;
		fruitEffectArray.clear();
		fruitEffectArray = null;
	}
}
