package cn.song.deepsea;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Wall extends Entity
{
	Context context;
	private Bitmap pic;
	public int Width;
	public int Height;

	Rect rect = null,Rect;

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

		rect = new Rect(GameView.screenW - pic.getWidth(),0,GameView.screenW,this.Height);
		
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(50);

	}

	@Override
	public void Draw(Canvas canvas)
	{
		move();
//		canvas.drawBitmap(pic, left = GameView.screenW - pic.getWidth(), top, paint);
//		for (Rect wall : GameView.walls)
//		{
//			canvas.drawBitmap(pic, wall.left, wall.top, paint);
//			canvas.drawText("", wall.left, wall.top, paint);
//		}
		for (int i = 0; i < GameView.walls.length; i++)
		{
			canvas.drawBitmap(pic, null,GameView.walls[i], paint);
			canvas.drawText("index： "+ i, GameView.walls[i].left-20, GameView.walls[i].top, paint);
			
		}

	}

	public void move()
	{
		for (Rect wall : GameView.walls)
		{
			wall.top -= GameView.speed;
			wall.bottom = wall.top + this.Height;
		}
		if (GameView.walls[0].bottom < 0)
		{
			rect = GameView.walls[0];
			rect.top = GameView.screenH;
			rect.bottom = rect.top + this.Height;
			for (int i = 0; i < GameView.walls.length-1; i++)
			{
				GameView.walls[i] = GameView.walls[i+1];
				
			}
			GameView.walls[GameView.walls.length-1] = rect;

		}
		Rect = GameView.walls[2];
	}

}
