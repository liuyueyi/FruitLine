package com.july.fruitline.sprite;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.july.fruitline.util.Assets;
import com.july.fruitline.util.Constants;
import com.july.fruitline.util.MyUtil;

public class Box {
	Cell[][] box;
	int value[][];
	int total;

	public Box() {
		box = new Cell[9][7];
	}

	public void init(int level) {
		over = false;
		value = MyUtil.util.readLevel(level - 1);
		for (int row = 0; row < box.length; row++) {
			for (int column = 0; column < box[0].length; column++) {
				box[row][column] = new Cell(value[row][column] - 1, row, column);
			}
		}
		total = getLeftNum();
	}

	public void removeBox(int row, int column) {
		box[row][column].remove();
	}

	/**
	 * 判断box中的背景是否消耗完毕，当消耗完毕则添加special fruit
	 * 
	 * @return
	 */
	boolean over;

	public boolean ifOver() {
		if (over)
			return true;

		for (int row = 0; row < box.length; row++) {
			for (int column = 0; column < box[0].length; column++) {
				if (box[row][column].notRemoved())
					return false;
			}
		}
		over = true;
		return over;
	}

	/**
	 * 用于判断是否可以添加星星，当剩余的box数目小于总数的3/4时，可添加第一个， 2/4时再加一个， 1/4时，添加最后一个
	 * 
	 * @param num
	 * @return
	 */
	public boolean ifAddStar(int num) {
		if (getLeftNum() - 1 < total * (1f - num / 4f))
			return true;
		else
			return false;
	}

	private int getLeftNum() {
		int count = 0;
		for (int row = 0; row < box.length; row++) {
			for (int column = 0; column < box[0].length; column++) {
				if (box[row][column].notRemoved())
					count++;
			}
		}
		return count;
	}

	public void draw(Batch batch) {
		for (int row = 0; row < box.length; row++) {
			for (int column = 0; column < box[0].length; column++) {
				box[row][column].draw(batch);
			}
		}
	}
}

class Cell {
	private int type;
	private int row, column;
	private boolean remove;
	private int time;
	private float addX = 1;

	public Cell(int type, int row, int column) {
		this.type = type;
		this.row = row;
		this.column = column;
		remove = false;
		time = 0;
	}

	public void remove() {
		if (type >= 0) {
			remove = true;
			time = 0;
		}
	}

	public boolean notRemoved() {
		return type >= 0;
	}

	public void draw(Batch batch) {
		if (remove) {
			if (++time < 10) {
				addX = 1 - time / 10.0f;
			} else if (time == 10) {
				type--;
				if (type < 0) {
					remove = false;
					addX = 1;
				}
			} else if (time < 20) {
				addX = time / 10.0f - 1;
			} else {
				remove = false;
				addX = 1;
			}
		}
		if (type >= 0)
			batch.draw(Assets.instance.box[type], Constants.fruitX
					+ Constants.fruitWidth * column + Constants.fruitWidth
					* (1 - addX) / 2f, Constants.fruitY + Constants.fruitHeight
					* row, Constants.fruitWidth * addX, Constants.fruitHeight);
	}
}