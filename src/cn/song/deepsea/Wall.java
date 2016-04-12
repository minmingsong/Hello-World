package cn.song.deepsea;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Wall extends Entity
{
	Context context;
	private Bitmap pic;
	private int Width;
	private int Height;

	int left;
	int top;
	int right;
	int bottom;

	private Paint paint;

	public Wall(Context context)
	{
		super();
		this.context = context;

		init();
	}

	@Override
	protected void init()
	{
		pic = BitmapFactory.decodeResource(context.getResources(), R.drawable.wall);
		Height = pic.getHeight();
		Width = pic.getWidth();
		
		left = GameView.screenW-pic.getWidth();
		top = GameView.screenH;
		right = left + Width;
		bottom = top + Height;
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(50);
		
	}

	@Override
	public void Draw(Canvas canvas)
	{
		move();
		canvas.drawBitmap(pic, left = GameView.screenW-pic.getWidth(), top, paint);
//		left = GameView.screenW-pic.getWidth();
//		paint.setColor(Color.RED);
//		canvas.drawRect(left, top, right, bottom, paint);
		canvas.drawText(GameView.screenH +"X"+GameView.screenW, 50, 100, paint);
		
	}

	public void move()
	{
		top -= GameView.speed;
		if (top < 0)
		{
			top = GameView.screenH;
		}
		bottom = top + Height;
//		right = left + Width;
	}

}
