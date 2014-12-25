package com.july.fruitline.manager;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.july.fruitline.sprite.ToolEffect;

public class ToolEffectManager {
	public static ToolEffectManager manager = new ToolEffectManager();

	private Pool<ToolEffect> effectPool;
	private Array<ToolEffect> effects;

	private ToolEffectManager() {
	}

	public void init() {
		effects = new Array<ToolEffect>();
		effectPool = Pools.get(ToolEffect.class);
		effectPool.peak = 10;
	}

	public void show(float x, float y, int type) {
		autoRemove();

		ToolEffect effect = effectPool.obtain();
		effect.show(x, y, type);
		effects.add(effect);
	}

	public void autoRemove() {
		for (ToolEffect effect : effects) {
			if (!effect.show) {
				effectPool.free(effect);
				effects.removeValue(effect, true);
			}
		}
	}

	public void draw(Batch batch) {
		for (ToolEffect tool : effects)
			tool.draw(batch);
	}

	public void freeAll() {
		effectPool.freeAll(effects);
		effectPool.clear();
		effectPool = null;
		effects.clear();
		effects = null;
	}
}
