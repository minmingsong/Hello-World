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

	Rect rect;
	Rect rectl;
	Rect rectScore;
	public WallRect tempRect;

	public WallRect[] walls;

	public Wall(Context context)
	{
		super(context);
		pic = BitmapFactory.decodeResource(context.getResources(), R.drawable.wall);
		scoreMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.score);
		Height = pic.getHeight();
		Width = pic.getWidth();
		int a = GameView.screenW;
		for (int i = 0; i < RECTl.length; i++)
		{
			RECTl[i] = new Rect();
			RECTl[i].left = 0;
			if (i != 0)
				RECTl[i].right = a * i / 4 - 20;
			else
				RECTl[i].right = a * i / 4;
			RECTl[i].top = 0;
			RECTl[i].bottom = 0;
		}
		for (int i = 0; i < RECTr.length; i++)
		{
			RECTr[i] = new Rect();
			if (i != 3)
				RECTr[i].left = a * (i + 1) / 4 + 20;
			else
				RECTr[i].left = a * (i + 1) / 4;
			RECTr[i].right = a;

			RECTr[i].top = 0;
			RECTr[i].bottom = 0;
		}
		walls = new WallRect[GameView.screenH / 300 + 1];
		rect = new Rect(0, 0, pic.getWidth(), pic.getHeight());
		rectScore = new Rect(10, 20, scoreMap.getWidth(), scoreMap.getHeight() * 2);
		paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setAntiAlias(true);
		paint.setTextSize(35);
		init();
	}

	@Override
	protected void init()
	{
		score = 0;
		walls[0] = new WallRect(rect, new Rect(0, 0, 0, 0), WallRect.DL);
		walls[0].Top = walls[0].rectl.top;
		for (int i = 1; i < walls.length; i++)
		{
			rectl = new Rect(rect.left, walls[i - 1].rectl.top + 300, rect.right, walls[i - 1].rectl.bottom + 300);
			walls[i] = new WallRect(rectl, new Rect(0, 0, 0, 0), WallRect.DL);
			walls[i].Top = walls[i].rectl.top;
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
		for (int i = 0; i < walls.length; i++)
		{
			walls[i].setRect(walls[i].Top - GameView.speed);
		}
		if (walls[0].rectl.bottom < 0)
		{
			tempRect = walls[0];

			for (int i = 0; i < walls.length - 1; i++)
			{
				walls[i] = walls[i + 1];

			}
			switch (((int) (Math.random() * 10)) + 1)// 通过随机数确定 缺口 在什么位置
														// 来拼接这一行的显示矩形
			{
				case 1:
				case 2:
				case 3:
					exchange(0);
					tempRect.flag = WallRect.DR;
					break;
				case 4:
				case 5:
					exchange(1);
					tempRect.flag = WallRect.DT;
					break;
				case 6:
				case 7:
				case 8:
					exchange(2);
					tempRect.flag = WallRect.DT;
					break;
				case 9:
				case 10:
					exchange(3);
					tempRect.flag = WallRect.DL;
					break;
				default:
					exchange(0);
					tempRect.flag = WallRect.DR;
					break;
			}

			tempRect.setRect(walls[walls.length - 2].Top + 300);
			walls[walls.length - 1] = tempRect;
			score += 5;

		}
		tempRect = walls[2];
	}

	/**
	 * 
	 * @param i
	 *            i 通过随机数确定的需要用到哪一组的 预先定义好的矩形 只能通过int 类型赋值
	 * 
	 *            通过引用类型对象赋值会出现逻辑错误； 引用赋值 实际是指针操作 会改变预定义矩形
	 */
	public void exchange(int i)
	{
		tempRect.rectl.left = RECTl[i].left;
		tempRect.rectl.right = RECTl[i].right;
		tempRect.rectr.left = RECTr[i].left;
		tempRect.rectr.right = RECTr[i].right;
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
			rectl.bottom = rectl.top + pic.getHeight();
			rectr.top = rectl.top;
			rectr.bottom = rectl.bottom;
		}
	}
}
