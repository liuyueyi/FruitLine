package com.july.fruitline.sprite;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.july.fruitline.util.Assets;
import com.july.fruitline.util.Constants;

public class Fruit extends GameObject implements Poolable {
	protected int type;
	public boolean canRemove; // true 表示可以消除该水果

	private boolean justExchanged;
	private int wait;
	protected int duration;
	protected float addX, addY;
	protected int max;

	public Fruit() {
		bounds = new Rectangle();
		bounds.width = Constants.fruitWidth - 4;
		bounds.height = Constants.fruitHeight - 4;
	}

	public void init(int row, int column) {
		init(0, 10, row, column);
	}

	public void init(int wait, int duration, int row, int column) {
		reset();
		max = 5;
		if (Assets.instance.currentLevel >= 200)
			max = 7;
		else if (Assets.instance.currentLevel >= 50)
			max = 6;
		type = (int) (Math.random() * max);
		bounds.x = Constants.fruitX + Constants.fruitWidth * column + 2;
		bounds.y = Constants.fruitY + Constants.fruitHeight * row + 2
				+ Constants.height;
		addMoveDownAction(wait, duration, row);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		canRemove = false;
		justExchanged = false;
		lastX = lastY = 0;
		lastDuration = 0;
		wait = 0;
		duration = 0;
		addX = 0;
		addY = 0;
		type = -1;
		bounds.x = Constants.width;
		bounds.y = Constants.height;
	}

	public int getType() {
		return type;
	}

	public boolean compareTo(Fruit f) {
		if (type >= Constants.STAR)
			return false;

		return f != null && f.getType() == type;
	}

	/**
	 * 添加水果向下移动的动画
	 * 
	 * @param waitTime
	 *            开始执行动画的等待时间
	 * @param duration
	 *            动画的持续时间
	 * @param row
	 *            下移的行数
	 */
	public void addMoveDownAction(int waitTime, int duration, int toRow) {
		this.wait = waitTime;
		this.duration = duration;

		addY = (bounds.y - Constants.fruitY - Constants.fruitHeight * toRow - 2)
				/ duration;
		addX = 0;
		bounds.y = Constants.fruitY + Constants.fruitHeight * toRow + 2;
	}

	private float lastX, lastY;
	private int lastDuration;

	/**
	 * 交换两个水果的动画，主要消除时引起
	 * 
	 * @param f
	 *            待交换的水果
	 * @param duration
	 *            动画持续时间
	 * @param wait
	 *            等待时间
	 * @param justExchanged
	 *            true表示交换后不能消除水果； false表示交换后可以消除水果
	 */
	public void addExchangeAction(Fruit f, int wait, int duration,
			boolean justExchanged) {
		addExchangeAction(f.bounds.x, f.bounds.y, wait, duration, justExchanged);
	}

	/**
	 * 交换两个水果的动画，主要消除时引起
	 * 
	 * @param x
	 * @param y
	 * @param wait
	 * @param duration
	 * @param justExchanged
	 */
	public void addExchangeAction(float x, float y, int wait, int duration,
			boolean justExchanged) {
		this.wait = wait;
		this.justExchanged = justExchanged;
		if (justExchanged) {
			this.duration = duration / 2;
			lastX = bounds.x;
			lastY = bounds.y;
			lastDuration = this.duration;
		} else {
			this.duration = duration;
		}
		addX = (bounds.x - x) / this.duration;
		addY = (bounds.y - y) / this.duration;
		bounds.x = x;
		bounds.y = y;
	}

	public void draw(Batch batch) {
		if (wait > 0) {
			wait--;
		} else if (duration > 0) {
			if (--duration == 0 && justExchanged) {
				justExchanged = false;
				duration = lastDuration;
				addX = (bounds.x - lastX) / lastDuration;
				addY = (bounds.y - lastY) / lastDuration;
				bounds.x = lastX;
				bounds.y = lastY;
			}
		}

		batch.draw(Assets.instance.fruit[type], bounds.x + addX * duration,
				bounds.y + addY * duration, bounds.width, bounds.height);
	}
}
