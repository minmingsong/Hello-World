package cn.song.deepsea;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public abstract class Entity
{

	protected Bitmap pic;
	
	protected int left;
	protected int top;
	protected int right;
	protected int bottom;
	protected abstract void init();
	protected abstract void Draw(Canvas canvas);
	protected abstract void move();
	
}
