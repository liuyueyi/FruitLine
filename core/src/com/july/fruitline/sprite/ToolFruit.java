package com.july.fruitline.sprite;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.july.fruitline.util.Assets;
import com.july.fruitline.util.Constants;

public class ToolFruit extends Fruit {
	public int tool;
	public boolean tag; // true 表示该水果因为道具原因被消除

	public ToolFruit() {
		super();
	}

	@Override
	public void init(int row, int column) {
		init(0, Constants.duration, row, column);
	}

	@Override
	public void init(int wait, int duration, int row, int column) {
		super.init(wait, duration, row, column);
		tag = false;
		if (5 == (int) (Math.random() * 10)) {
			tool = Constants.TIME_TOOL; // time tool
		} else {
			tool = -1;
		}
	}

	public void initSpecial(int wait, int duratio, int row, int column,
			int specialType) {
		super.init(wait, duratio, row, column);
		type = specialType;
		tool = -1;
		tag = false;
	}

	@Override
	public void reset() {
		super.reset();
		tool = -1;
		tag = false;
	}

	public void setTool(int tool) {
		tag = true;
		this.tool = tool;
	}

	public void removeTool() {
		tag = false;
		this.tool = -1;
		anyToolTime = 0;
	}

	/**
	 * 表示设置该水果为道具消除效果，因此该水果的道具功能，除了时间道具外，其他的道具功能无效
	 */
	public void setTaged() {
		if (type < 7 && !canRemove) {
			canRemove = true;
			tag = true;
		}
	}

	/**
	 * 判断两个可消除水果是否为同一类水果
	 * 
	 * @param f
	 * @return
	 */
	public boolean sameRemovedWith(ToolFruit f) {
		if (f == null)
			return false;

		return canRemove && f.canRemove && compareTo(f);
	}

	public boolean sameRemovedWith(ToolFruit f, ToolFruit f2) {
		return sameRemovedWith(f) && sameRemovedWith(f2);
	}

	int anyToolTime;

	@Override
	public void draw(Batch batch) {
		super.draw(batch);
		if (tool == Constants.ANY && ++anyToolTime % 7 == 0) {
			type = (type + 1) % max;
			return;
		}
		if (tool >= 0 && tool != Constants.ANY)
			batch.draw(Assets.instance.tools[tool], bounds.x + addX * duration
					+ bounds.width * 0.5f, bounds.y + addY * duration,
					bounds.width * 0.5f, bounds.height * 0.5f);
	}
}
