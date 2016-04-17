package cn.song.deepsea;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Callback, Runnable
{

	private Context context;
	public static int speed = 10;
	private static int T = 50;
	
	public static int screenH;
	public static int screenW;
	private SurfaceHolder sh;
	private Paint paint;
	private Canvas canvas;
	private Thread thread;

	private Bitmap[] Bgmap = new Bitmap[2];
	private int argb = 0;

	Rect rect;
	int tochX = 0;
	public static boolean touched = false;

	
	private Wall wall;
	private Role role;
	private GameMenu menu;

	public static Rect[] walls;

	private boolean Dead = false;
	private boolean Pause = false;

	public static enum CHANGE
	{
		NO, YES
	};

	CHANGE changebg = CHANGE.NO;

	public GameView(Context context)
	{
		super(context);
		this.context = context;
		sh = this.getHolder();
		sh.addCallback(this);
		paint = new Paint();
		paint.setColor(Color.CYAN);
		paint.setAntiAlias(true);
		paint.setTextSize(50);
		this.setFocusable(true);

		Bgmap[0] = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		Bgmap[1] = BitmapFactory.decodeResource(getResources(), R.drawable.bg2);
		rect = new Rect();

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{

		screenW = this.getWidth();
		screenH = this.getHeight();
		rect.left = 0;
		rect.top = 0;
		rect.right = screenW;
		rect.bottom = Bgmap[0].getHeight();
		walls = new Rect[screenH / 300 + 1];

		wall = new Wall(context);
		role = new Role(context);
		menu = new GameMenu(context);
		walls[0] = new Rect(wall.rect);
		for (int i = 1; i < walls.length; i++)
		{
			walls[i] = new Rect(walls[i - 1].left, walls[i - 1].top + 300, walls[i - 1].right,
					walls[i - 1].bottom + 300);
		}
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{

		Dead = false;
	}

	@Override
	public void run()
	{

		while (!Dead && !Pause)
		{
			long startTime = System.currentTimeMillis();
			move();
			Draw();
			long endTime = System.currentTimeMillis();

			if (endTime - startTime < T)
			{
				try
				{
					Thread.sleep(T - (endTime - startTime));
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}

		}
		if (Dead)
			wall.score = 0;
	}

	private void move()
	{

		rect.top -= speed;
		if (rect.top <= -Bgmap[0].getHeight())
		{
			rect.top = Bgmap[0].getHeight() + rect.top;
			changebg = CHANGE.YES; // 第一张图片超出屏幕之后就改为只画第二张
		}

		checkdead();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{

		switch (event.getAction() & MotionEvent.ACTION_MASK)
		{
			case MotionEvent.ACTION_DOWN:

				// X = (int) event.getX();
				// Y = (int) event.getY();
				if (menu.rect.contains((int) event.getX(), (int) event.getY()))
				{
					if (Pause)
					{
						Pause = false;
						new Thread(this).start();
					} else
					{
						Bitmap pause = BitmapFactory.decodeResource(getResources(), R.drawable.poppause);
						Pause = true;
						canvas = sh.lockCanvas();
						canvas.drawBitmap(pause, null, new Rect(0, 200, screenW, screenH - 100), paint);
						if (canvas != null)
						{
							sh.unlockCanvasAndPost(canvas);
						}

					}
					break;
				}
				// System.out.println("单指触摸，触摸点数：" + event.getPointerCount());

				touched = true;
				if (event.getX() < screenW / 2)
				{
					role.setHspeed(-5);
//					System.out.println("执行到触摸");

				} else
				{
					role.setHspeed(5);
				}
				// if (Dead)
				// {
				// Dead = false;
				// new Thread(this).start();
				// }
				break;
			case MotionEvent.ACTION_POINTER_DOWN:

				if (event.getX(event.getActionIndex()) < screenW / 2)// event.getActionIndex()
																		// 检测触发这次事件的索引值
				{
					role.setHspeed(-5);

				} else
				{
					role.setHspeed(5);
				}

				break;
			// return super.onTouchEvent(event);
			case MotionEvent.ACTION_POINTER_UP:
				// touched = false;
				// role.setHspeed(5);
				break;
			case MotionEvent.ACTION_UP:

				touched = false;
				role.setHspeed(5);
				// System.out.println("单只释放");
				break;

			default:
				break;
		}

		return true;
		// return super.onTouchEvent(event);
	}

	private void Draw()
	{
		try
		{
			canvas = sh.lockCanvas();

			if (canvas != null)
			{
				//只有一张背景图的画法
				// if (rect.top >= screenH - Bgmap[bitmapIndex].getHeight())
				// {
				// canvas.drawBitmap(Bgmap[bitmapIndex], 0, rect.top, paint);
				// } else
				// {
				// canvas.drawBitmap(Bgmap[bitmapIndex], 0, rect.top, paint);
				// canvas.drawBitmap(Bgmap[bitmapIndex], 0, rect.top +
				// Bgmap[bitmapIndex].getHeight(), paint);
				// }
				switch (changebg)
				{
					case NO:

						canvas.drawBitmap(Bgmap[0], 0, rect.top, paint);
						canvas.drawBitmap(Bgmap[1], 0, rect.top + Bgmap[1].getHeight(), paint);
						break;

					case YES:

						canvas.drawBitmap(Bgmap[1], 0, rect.top, paint);
						canvas.drawBitmap(Bgmap[1], 0, rect.top + Bgmap[1].getHeight(), paint);
						break;
					default:
						break;
				}
				wall.Draw(canvas);

				role.Draw(canvas);
				// canvas.drawText(X + " "+Y, 200, 400, paint); //画出触摸点的坐标
				if (Dead)
				{
					Bitmap pause = BitmapFactory.decodeResource(getResources(), R.drawable.over);
					canvas.drawBitmap(pause, null, new Rect(50, 100, screenW - 50, screenH - 200), paint);
					canvas.drawText(wall.score + "！", 230, 240 + 70, paint);
				} else
				{
					if (!Pause)
					menu.Draw(canvas);
				}

			}
		} catch (Exception e)
		{

		} finally
		{
			if (canvas != null)
			{
				sh.unlockCanvasAndPost(canvas);
			}
		}

	}

	private void checkdead()
	{
		// if ((role.right > wall.left && role.left < wall.right) &&
		// (role.bottom > wall.top && role.top < wall.bottom)) //最初的直接检测碰撞的表达式

		if (isCollsion(role.rect, wall.rect))
		{
			// if (role.top <= wall.top && role.left <=wall.left) {
			if (role.middle < wall.rect.top)
			{
				if (role.rect.left <= wall.rect.left)
				{
					argb = role.pic.getPixel(role.Width - (role.rect.right - wall.rect.left),
							role.Height - (role.rect.bottom - wall.rect.top));
				}
			}
			if (role.middle > wall.rect.top && role.middle < wall.rect.bottom)
			{
				if (role.rect.right > wall.rect.left)
				{
					Dead = true;
					return;
				}
			}
			if (role.middle > wall.rect.bottom)
			{
				if (role.rect.left <= wall.rect.left)
				{
					argb = role.pic.getPixel(role.Width - (role.rect.right - wall.rect.left),
							wall.rect.bottom - role.rect.top);
				}
			}
			//

			if (argb != 0) // 检测wall左上角对应在forg图片内的像素值，不为0，即碰撞
			{
				Dead = true;
			}
		}
	}

	private boolean isCollsion(Rect role, Rect wall)
	{
		if (wall.top < role.top && wall.bottom < role.top)
		{

		} else if (wall.left < role.left && wall.right < role.left)
		{

		} else if (wall.left > role.left && wall.left > role.right)
		{

		} else if (wall.top + 2 > role.top && wall.top + 2 > role.bottom)
		{

		} else
		{
			Dead = true;
			return true;
		}
		argb = 0;
		return false;
	}
}
