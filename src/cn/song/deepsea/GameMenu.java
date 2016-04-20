package cn.song.deepsea;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class GameMenu extends Entity
{

	Bitmap over;
	Bitmap pausepop;

	public GameMenu(Context context)
	{
		super(context);
		rect = new Rect();
		pic = BitmapFactory.decodeResource(context.getResources(), R.drawable.pause);
		over = BitmapFactory.decodeResource(context.getResources(), R.drawable.over);
		pausepop = BitmapFactory.decodeResource(context.getResources(), R.drawable.poppause);
		init();
	}

	@Override
	protected void init()
	{
		rect.right = GameView.screenW - 20;
		rect.left = rect.right - 50;
		rect.top = 20;
		rect.bottom = rect.top + 50;

		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(50);
	}

	@Override
	protected void Draw(Canvas canvas)
	{

		if (GameView.Dead)
		{
			canvas.drawBitmap(over, null, new Rect(50, 100, GameView.screenW - 50, GameView.screenH - 200), paint);
			canvas.drawText(Wall.score + "！", 230, 240 + 70, paint);
		} else if (GameView.Pause)
		{
			canvas.drawBitmap(pausepop, null, new Rect(0, 200, GameView.screenW, GameView.screenH - 100), paint);
		} else
		{
			canvas.drawBitmap(pic, null, rect, paint);
		}
	}

	@Override
	protected void move()
	{

	}

}
