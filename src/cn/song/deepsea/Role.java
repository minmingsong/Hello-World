package cn.song.deepsea;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Role extends Entity
{
	// public Bitmap pic;

	// private Paint paint = null;

	public int Width;
	public int Height;

	private int X = 10;
	private int Y = 550;

	// Rect rect;
	int middle;
	private int Hspeed;
//	private Matrix matrix;
//	private int flag;

	public void setHspeed(int hspeed)
	{
		Hspeed = hspeed;
	}

	public Role(Context context)
	{
		super(context);
//		flag = 0;
		init();
	}

	@Override
	protected void init()
	{
		pic = BitmapFactory.decodeResource(context.getResources(), R.drawable.forg);
		// pic = Bitmap.createScaledBitmap(pic, 10, 10, true);
		Height = pic.getHeight();
		Width = pic.getWidth();

		// rect = new Rect(X, Y, this.Width + X, Y + this.Height);
		rect = new Rect(GameView.screenW - pic.getWidth(), Y, GameView.screenW, Y + this.Height);
		middle = rect.centerY();
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(50);
//		matrix = new Matrix();
//		matrix.setTranslate(rect.left + pic.getWidth() / 2, rect.top + pic.getHeight() / 2);
////		matrix.setTranslate(X + pic.getWidth() / 2, Y + pic.getHeight() / 2);
//		X = rect.centerX();
//		Y = rect.centerY();

	}

	@Override
	protected void Draw(Canvas canvas)
	{
		 canvas.drawBitmap(pic, null, rect, paint);

//		canvas.drawBitmap(pic, matrix, paint);
	}

	@Override
	public void move()
	{

		if (GameView.touched && rect.right <= GameView.screenW && rect.left >= 0)
		{
			if (Hspeed >= 0)
				Hspeed += 2;
			else
				Hspeed -= 2;

			rect.left += Hspeed;
			rect.right = rect.left + this.Width;
//			matrix.setTranslate(rect.left + pic.getWidth() / 2, rect.top + pic.getHeight() / 2);
//			X = rect.centerX();
//			Y = rect.centerY();
		} else if (rect.right > GameView.screenW)
		{
			GameView.touched = false;
			rect.right = GameView.screenW;
			rect.left = rect.right - this.Width;
		} else if (rect.left < 0)
		{
			GameView.touched = false;
			rect.left = 0;
			rect.right = rect.left + this.Width;
		}
		
//		if (flag < 5)
//		{
//			flag++;
//			matrix.preRotate(15, X, Y);
//
//		} else
//		{
//			flag++;
//			matrix.preRotate(-15, X, Y);
//		}
//		if (flag % 10 == 0)
//		{
//			flag = 0;
//		}

	}

}
