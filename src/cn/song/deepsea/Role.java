package cn.song.deepsea;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Role extends Entity
{
	Context context;
	public Bitmap pic;

	private Paint paint = null;

	public int Width;
	public int Height;

	Rect rect;
	int middle;
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
		// pic = Bitmap.createScaledBitmap(pic, 10, 10, true);
		Height = pic.getHeight();
		Width = pic.getWidth();

		rect = new Rect(10, 550, this.Width+10, 500 + this.Height);
		middle = this.rect.top + this.Height / 2;
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(50);

	}

	@Override
	protected void Draw(Canvas canvas)
	{
		move();
		canvas.drawBitmap(pic, null, rect, paint);
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

			rect.left += Hspeed;
			rect.right = rect.left + this.Width;
		}
		if (rect.right > GameView.screenW)
		{
			GameView.touched = false;
			rect.right = GameView.screenW;
		} else if (rect.left < 0)
		{
			GameView.touched = false;
			rect.left = 0;
		}
	}

}
