package cn.song.deepsea;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import cn.song.deepsea.Wall.WallRect;

public class GameView extends SurfaceView implements Callback, Runnable
{
	private final String tag = "GameView";
	private Context context;
	public static int speed = 10;
	private static int T = 40; //画面的更新周期

	public static int screenH;
	public static int screenW;
	private SurfaceHolder sh;
	private Paint paint;
	private Canvas canvas;

	private Bitmap[] Bgmap = new Bitmap[2];
	private int argb = 0;

	Rect rect;				//Bgmap的矩形
	Rect SCREENRECT;		//屏幕的矩形
	int tochX = 0;			//触摸点的X值
	public static boolean touched = false;

	private Wall wall;
	private Role role;
	private GameMenu menu;

	

	public static boolean Dead = false;
	public static boolean Pause = false;

	public static enum CHANGE
	{
		NO, YES
	};

	CHANGE changebg = CHANGE.NO;
	public boolean continuerun = false;

	public GameView(Context context)
	{
		super(context);
		this.context = context;
		sh = this.getHolder();
		sh.addCallback(this);
		Bgmap[0] = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		Bgmap[1] = BitmapFactory.decodeResource(getResources(), R.drawable.bg2);
		init();
	}

	public void init()
	{
		paint = new Paint();
		paint.setColor(Color.CYAN);
		paint.setAntiAlias(true);
		paint.setTextSize(50);
		this.setFocusable(true);
		rect = new Rect();
		Dead = false;
		Pause = false;
	}

	public void reset()
	{
		this.init();
		wall.init();
		role.init();
		menu.init();
	}

	@Override
	public void run()
	{
		// try
		// {
		// Thread.sleep(3000);
		// } catch (InterruptedException e1)
		// {
		// }
		try
		{
			while (true)
			{
				System.out.println("正常运转!");
				while (!Dead)
				{
					while (!Pause)
					{
						if (continuerun)
						{
							continuerun = false;
						}
						long startTime = System.currentTimeMillis();
						move();
						Draw();
						long endTime = System.currentTimeMillis();

						if (endTime - startTime < T)
						{
							Thread.sleep(T - (endTime - startTime));
						}
					}
					// Draw();
				}

				Draw();
				// reset();
				if (continuerun)
				{
					reset();
				}
				Thread.sleep(500);
			}
		} catch (InterruptedException e)
		{
			// e.printStackTrace();
			Log.e(tag, "对不起,程序睡眠失常了");
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{

		switch (event.getAction() & MotionEvent.ACTION_MASK)
		{
			case MotionEvent.ACTION_DOWN:

				if (menu.rect.contains((int) event.getX(), (int) event.getY()))
				{
					if (!Pause)
					{
						Pause = true;
					}
					break;
				}
				touched = true;
				if (event.getX() < screenW / 2)
				{
					role.setHspeed(-5);
				} else
				{
					role.setHspeed(5);
				}
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
			case MotionEvent.ACTION_POINTER_UP:
				break;
			case MotionEvent.ACTION_UP:
				touched = false;
				role.setHspeed(5);
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
				switch (changebg)
				{
					case NO:

						canvas.drawBitmap(Bgmap[0], 0, rect.top, paint);
						canvas.drawBitmap(Bgmap[1], 0, rect.top + Bgmap[0].getHeight(), paint);
						break;

					case YES:

						// canvas.drawBitmap(Bgmap[1], 0, rect.top, paint);
						// canvas.drawBitmap(Bgmap[1], 0, rect.top +
						// Bgmap[1].getHeight(), paint);
						canvas.drawBitmap(Bgmap[1], null, SCREENRECT, paint);
						break;
					default:
						break;
				}
				if (!Dead && !Pause)
				{
					wall.move();
					role.move();
				}
				wall.Draw(canvas);
				role.Draw(canvas);
				menu.Draw(canvas);
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
		checkdead();
	}

	private void move()
	{

		rect.top -= speed;
		if (changebg != CHANGE.YES)
		{
			if (rect.top <= -Bgmap[0].getHeight())
			{
				rect.top = Bgmap[0].getHeight() + rect.top;
				changebg = CHANGE.YES; // 第一张图片超出屏幕之后就改为只画第二张
			}
		}
		else {
			SCREENRECT.top = 0;
			SCREENRECT.left = 0;
			SCREENRECT.bottom = SCREENRECT.top + this.getHeight();
		}

	}

	private void checkdead()
	{

		
		if (wall.tempRect.flag == WallRect.DL)
		{
			if (isCollsion(role.rect, wall.tempRect.rectl)) {
				
			}
		}
		
		
		if (isCollsion(role.rect, wall.tempRect.rectl))
		{
			
			checkDead(wall.tempRect.rectl);
		}
	}

	/**
	 * @param wall 障碍物  需要检测的矩形
	 */
	public void checkDead(Rect wall)
	{
		if (role.middle < wall.top)
		{
			if (role.rect.left <= wall.left)
			{
				argb = role.pic.getPixel(role.Width - (role.rect.right - wall.left),
						role.Height - (role.rect.bottom - wall.top));
			} else if (role.rect.left > wall.left && role.rect.right < wall.right)
			{
				Dead = true;
				Pause = true;
				return;
			} else
			{
				argb = role.pic.getPixel(wall.right - role.rect.left, wall.top - role.rect.top);
			}
		} else if (role.middle > wall.top && role.middle < wall.bottom)
		{
			// 只要检测到矩形相交，在role的middle属于本种情况就Dead
			Dead = true;
			Pause = true;
			return;

		} else if (role.middle > wall.bottom)
		{
			if (role.rect.left <= wall.left)
			{
				argb = role.pic.getPixel(role.Width - (role.rect.right - wall.left),
						wall.bottom - role.rect.top);
			} else if (role.rect.left > wall.left && role.rect.right < wall.right)
			{
				Dead = true;
				Pause = true;
				return;
			} else
			{
				argb = role.pic.getPixel(wall.right - role.rect.left, wall.bottom - role.rect.top);
			}
		}
		//

		if (argb != 0) // 检测wall左上角对应在forg图片内的像素值，不为0，即碰撞
		{
			Dead = true;
			Pause = true;
		}
	}

	private boolean isCollsion(Rect role, Rect wall)
	{
		// if (wall.top < role.top && wall.bottom < role.top)
		// {
		//
		// } else if (wall.left < role.left && wall.right < role.left)
		// {
		//
		// } else if (wall.left > role.left && wall.left > role.right)
		// {
		//
		// } else if (wall.top + 2 > role.bottom && wall.top + 2 > role.top)
		if (wall.top > role.bottom)
		{
		} else if (wall.right < role.left)
		{
		} else if (wall.left > role.right)
		{
		} else if (wall.bottom < role.top)
		{

		} else
		{
			// Dead = true;
			return true;
		}
		argb = 0;
		return false;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{

		screenW = this.getWidth();
		screenH = this.getHeight();

		SCREENRECT = new Rect(0, 0, screenW, screenH);
		rect.left = 0;
		rect.top = 0;
		rect.right = screenW;
		rect.bottom = Bgmap[0].getHeight();
		

		wall = new Wall(context);
		role = new Role(context);
		menu = new GameMenu(context);
		
		new Thread(this).start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{

		// Dead = true;
		// Pause = true;
	}

}
