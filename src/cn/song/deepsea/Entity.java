package cn.song.deepsea;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public abstract class Entity
{

	protected Bitmap pic;
	public Rect rect;
	protected Paint paint;
	Context context;
	public Entity(Context context)
	{
		this.context = context;
	}
	protected abstract void init();
	protected abstract void Draw(Canvas canvas);
	protected abstract void move();
	
}
