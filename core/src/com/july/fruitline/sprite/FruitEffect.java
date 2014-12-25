package com.july.fruitline.sprite;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.july.fruitline.util.Assets;
import com.july.fruitline.util.Constants;

public class FruitEffect extends GameObject implements Poolable {
	float time; // ʱ��
	Vector2 speed; // x�� y������ٶ�
	float accelerate; // y ������ٶ�
	TextureRegion back;
	float preY;

	public FruitEffect() {
		speed = new Vector2();
		bounds = new Rectangle();
		reset();
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		bounds.x = 0;
		bounds.y = 0;
		bounds.width = 0;
		bounds.height = 0;
		speed.x = 0;
		speed.y = 0;
		time = 0;
		accelerate = 0;
	}

	/**
	 * �ж϶����Ƿ���Ա�����
	 * 
	 * @return true ���Ա�����
	 */
	public boolean ifFree() {
		float tempX = bounds.x + speed.x * time;
		float tempY = bounds.y + time * (speed.y + time * accelerate);
		return tempX < -bounds.width || tempX > Constants.width
				|| tempY < -bounds.height;

	}

	/**
	 * ��ʼ�����𶹶�����ֵ�С����Ч��
	 * 
	 * @param speedX
	 *            x����ĳ��ٶȣ���Ϊ����
	 * @param speedY
	 *            y����ĳ��ٶȣ���Ϊ����
	 * @param accelerate
	 *            y������ٶȣ�һ��Ϊ��
	 * @param type
	 *            ��ʾ��С��������
	 * @param x
	 *            ���𶹶���x����
	 * @param y
	 *            ���𶹶���y����
	 */

	public void init(float speedX, float speedY, int type, float x, float y) {
		time = 0;
		speed.x = speedX;
		speed.y = speedY;
		this.accelerate = -1f;
		back = Assets.instance.fruit[type];
		bounds.x = x;
		bounds.y = y;
		float rate = (float) (0.2f + Math.random() * 0.5f);
		bounds.width = Constants.fruitWidth * rate;
		bounds.height = Constants.fruitHeight * rate;
		preY = y;
	}

	private int generateSymble() {
		if (Math.random() > 0.5f)
			return 1;
		else
			return -1;
	}

	public void init(int type, float x, float y) {

		float speedx = (float) (generateSymble() * (Math.random() * 5 + 8));
		float speedy = (float) ((Math.random() * 35 - 10));
		init(speedx, speedy, type, x, y);
		// System.out.println("speed x: " + speedx);
	}

	public void draw(Batch batch) {
		if (ifFree())
			return;
		time += 0.4f;
		preY += speed.y + accelerate * time + accelerate / 2;
		batch.draw(back, bounds.x + speed.x * time, bounds.y + time
				* (speed.y + time * accelerate), bounds.width, bounds.height);

	}
}