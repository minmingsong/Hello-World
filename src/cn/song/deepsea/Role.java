package cn.song.deepsea;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Role extends Entity
{
	Context context;
	public Bitmap pic;

	private Paint paint = null;

	public int Width;
	public int Height;
	int left;

	int top;
	int right;
	int bottom;
	private int Hspeed;

	public void setHspeed(int hspeed)
	{
		Hspeed = hspeed;
	}

	public Role(Context context)
	{
		super();
		this.context = context;
		init();
	}

	@Override
	protected void init()
	{
		pic = BitmapFactory.decodeResource(context.getResources(), R.drawable.forg);
		Height = pic.getHeight();
		Width = pic.getWidth();

		left = 20;
		top = 500;
		right = left + Width;
		bottom = top + Height;
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(50);

	}

	@Override
	protected void Draw(Canvas canvas)
	{
		move();
		canvas.drawBitmap(pic, left, 500, paint);
		// paint.setColor(Color.RED);
		// canvas.drawRect(left, top, right, bottom, paint);
		// canvas.drawText(left+""+top+""+right+""+bottom +""+ "",50, 80,
		// paint);
	}

	@Override
	public void move()
	{
		if (GameView.touched)
		{
			if (Hspeed >= 0)
				Hspeed += 2;
			else
				Hspeed -= 2;

			left += Hspeed;
		}
		right = left + Width;
		if (right > GameView.screenW)
		{
			left = GameView.screenW - Width;
		} else if (left < 0)
		{
			left = 0;
		}
	}

}
