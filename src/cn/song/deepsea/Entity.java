package cn.song.deepsea;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public abstract class Entity
{

	protected Bitmap pic;
	
	protected abstract void init();
	protected abstract void Draw(Canvas canvas);
	protected abstract void move();
	
}
