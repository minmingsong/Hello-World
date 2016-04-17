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
	public int score = 0;
	private Bitmap pic;
	private Bitmap scoreMap;
	public int Width;
	public int Height;

	Rect rect,Rect;
	Rect rectScore;
	
	public Wall(Context context)
	{
		super(context);
		init();
	}

	@Override
	protected void init()
	{
		pic = BitmapFactory.decodeResource(context.getResources(), R.drawable.wall);
		scoreMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.score);
		Height = pic.getHeight();
		Width = pic.getWidth();

		rect = new Rect(GameView.screenW - pic.getWidth(),0,GameView.screenW,this.Height);
		rectScore = new Rect(10, 20, scoreMap.getWidth(), scoreMap.getHeight()*2);
		paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setAntiAlias(true);
		paint.setTextSize(35);

	}

	@Override
	public void Draw(Canvas canvas)
	{
		move();
//		canvas.drawBitmap(pic, left = GameView.screenW - pic.getWidth(), top, paint);
		for (Rect wall : GameView.walls)
		{
			canvas.drawBitmap(pic, wall.left, wall.top, paint);
			canvas.drawBitmap(scoreMap, null, rectScore, paint);
			canvas.drawText(""+score, 20, rectScore.top+rectScore.centerY()-10, paint);
		}
//		for (int i = 0; i < GameView.walls.length; i++)
//		{
//			canvas.drawBitmap(pic, null,GameView.walls[i], paint);
//			canvas.drawText("index： "+ i, GameView.walls[i].left-20, GameView.walls[i].top, paint);
//			
//		}//This code snippet used to display the Rect's info that closest role;

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
			score+=5;

		}
		Rect = GameView.walls[2];
	}

}
