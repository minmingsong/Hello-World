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
	public static int score;
	private Bitmap pic;
	private Bitmap scoreMap;
	public int Width;
	public int Height;

	private static Rect[] RECTl = new Rect[4];
	private static Rect[] RECTr = new Rect[4];

	int ran;

	Rect rect;
	Rect rectl;
	Rect rectr;
	Rect rectScore;
	public WallRect tempRect;

	// public Rect[] walls;

	public WallRect[] walls;

	public Wall(Context context)
	{
		super(context);
		pic = BitmapFactory.decodeResource(context.getResources(), R.drawable.wall);
		scoreMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.score);
		int a = GameView.screenW;
		for (int i = 0; i < RECTl.length; i++)
		{
			RECTr[i] = new Rect();
			RECTr[i].left = 0;
			if (i != 0)
				RECTr[i].right = a * i / 4 - 20;
			else
				RECTr[i].right = a * i / 4;
		}
		for (int i = 0; i < RECTr.length; i++)
		{
			if (i != 3)
				RECTr[i].left = a * (i + 1) / 4 + 20;
			else
				RECTr[i].left = a * (i + 1) / 4;
			RECTr[i].right = a;
		}
		init();
	}

	@Override
	protected void init()
	{
		score = 0;
		Height = pic.getHeight();
		Width = pic.getWidth();
		// rect = new Rect(GameView.screenW - pic.getWidth(), 0,
		// GameView.screenW, this.Height);
		rect = new Rect(0, 0, pic.getWidth(), pic.getHeight());
		rectScore = new Rect(10, 20, scoreMap.getWidth(), scoreMap.getHeight() * 2);
		paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setAntiAlias(true);
		paint.setTextSize(35);

		walls = new WallRect[GameView.screenH / 300 + 1];
		walls[0] = new WallRect(rect, null, WallRect.DL);
		for (int i = 1; i < walls.length; i++)
		{
			rectl = new Rect(rect.left, rect.top + 300, rect.right, rect.bottom + 300);
			walls[i] = new WallRect(rectl, new Rect(), WallRect.DL);
		}
	}

	@Override
	public void Draw(Canvas canvas)
	{

		for (WallRect wall : walls)
		{
			if (wall.flag == WallRect.DL)
			{
				canvas.drawBitmap(pic, null, wall.rectl, paint);
			} else if (wall.flag == WallRect.DR)
			{
				canvas.drawBitmap(pic, null, wall.rectr, paint);
			} else
			{
				canvas.drawBitmap(pic, null, wall.rectl, paint);
				canvas.drawBitmap(pic, null, wall.rectr, paint);
			}

		}
		canvas.drawBitmap(scoreMap, null, rectScore, paint);
		canvas.drawText("" + score, 20, rectScore.top + rectScore.centerY() - 10, paint);
	}

	public void move()
	{
		for (WallRect wall : walls)
		{

			// canvas.drawBitmap(pic, null, wall.rectl, paint);
			// wall.rectl.top -= GameView.speed;
			// wall.rectl.bottom = wall.rectl.top + this.Height;
			wall.setRect(wall.Top - GameView.speed);

		}
		if (walls[0].rectl.bottom < 0)
		{
			tempRect = walls[0];
			tempRect.setRect(GameView.screenH);

			for (int i = 0; i < walls.length - 1; i++)
			{
				walls[i] = walls[i + 1];

			}

			if (((int) (Math.random() * 10)) < 3)
			{
				rectl = RECTl[1];
				rectr = RECTr[1];
			}
			switch (((int) (Math.random() * 10)) + 1)
			{

				case 1:
				case 2:
				case 3:
					rectl = RECTl[1];
					rectr = RECTr[1];
					tempRect.flag = WallRect.DR;
					break;
				case 4:
				case 5:
					rectl = RECTl[2];
					rectr = RECTr[2];
					tempRect.flag = WallRect.DT;
					break;
				case 6:
				case 7:
				case 8:
					rectl = RECTl[3];
					rectr = RECTr[3];
					tempRect.flag = WallRect.DT;
					break;
				case 9:
				case 10:
					rectl = RECTl[4];
					rectr = RECTr[4];
					tempRect.flag = WallRect.DL;
					break;
				default:
					break;
			}
			walls[walls.length - 1] = tempRect;
			score += 5;

		}
		tempRect = walls[2];
	}

	class WallRect
	{
		Rect rectl;
		Rect rectr;
		int Top = 0;
		int flag; // 决定是否画哪个矩形
		/**
		 * 画 rectl
		 */
		public static final int DL = 0;
		/**
		 * 画 rectr
		 */
		public static final int DR = 1;
		/**
		 * 左右两边都要画
		 */
		public static final int DT = 2;

		public WallRect(Rect rectl, Rect rectr, int flag)
		{
			this.rectl = rectl;
			this.rectr = rectr;
			this.flag = flag;
			this.Top = rectl.top;
		}

		public void setRect(int top)
		{
			this.Top = top;
			rectl.top = Top;
			rectl.bottom = rectl.top + Height;
			rectr.top = rectl.top;
			rectr.bottom = rectl.bottom;
		}
	}
}
