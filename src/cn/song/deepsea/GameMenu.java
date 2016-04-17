package cn.song.deepsea;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class GameMenu extends Entity
{

	

	public GameMenu(Context context)
	{
		super(context);
		rect = new Rect();
		init();
	}

	@Override
	protected void init()
	{
		pic = BitmapFactory.decodeResource(context.getResources(), R.drawable.pause);
		rect.right = GameView.screenW-20;
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
		canvas.drawBitmap(pic, null, rect, paint);
	}

	@Override
	protected void move()
	{

	}

}
