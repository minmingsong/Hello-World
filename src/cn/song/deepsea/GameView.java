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

	public static int speed = 15;
	private static int T = 70;
	private int argb = 0;
	public static int screenH;
	public static int screenW;
	private SurfaceHolder sh;
	private Paint paint;
	private Canvas canvas;
	private Thread thread;

	private Bitmap Bgmap;
	private int mapY = 0;
	int tochX = 0;
	public static boolean touched = false;
	private boolean Dead = false;
	private Context context;
	private Wall wall;
	private Role role;
	
	public static Rect[] walls;
	
	int X,Y;

	public GameView(Context context)
	{
		super(context);
		this.context = context;
		sh = this.getHolder();
		sh.addCallback(this);
		paint = new Paint();
		paint.setColor(Color.RED);
		paint.setAntiAlias(true);
		paint.setTextSize(50);
		this.setFocusable(true);

		Bgmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		
		

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{

		screenW = this.getWidth();
		screenH = this.getHeight();
		walls = new Rect[screenH / 300 +1];
		
		wall = new Wall(context);
		role = new Role(context);
		walls[0] = new Rect(wall.rect);
		for (int i = 1; i < walls.length; i++)
		{
			walls[i] = new Rect(walls[i-1].left, walls[i-1].top + 300, walls[i-1].right, walls[i-1].bottom + 300);
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

		while (!Dead)
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
	}

	private void move()
	{

		mapY -= speed;
		if (mapY <= -Bgmap.getHeight())
		{
			mapY = Bgmap.getHeight() + mapY;
		}

		checkdead();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{

		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:

				X = (int) event.getX();
				Y = (int) event.getY();
				System.out.println("单指触摸，触摸点数：" + event.getPointerCount());

				touched = true;
				if (event.getX() < screenW / 2)
				{
					role.setHspeed(-5);
					System.out.println("执行到触摸");

				} else
				{
					role.setHspeed(5);
				}
				if (Dead)
				{
					Dead = false;
					new Thread(this).start();
				}
//				return true;
//			case MotionEvent.ACTION_POINTER_DOWN:
//
//				touched = false;
//				if (event.getX(1) < screenW / 2)
//				{
//					role.setHspeed(-5);
//
//				} else
//				{
//					role.setHspeed(5);
//				}
//				
//				break;
//				return super.onTouchEvent(event);
//			case MotionEvent.ACTION_POINTER_UP:
//				role.setHspeed(5);
//				return super.onTouchEvent(event);
//				break;
			case MotionEvent.ACTION_UP:

				touched = false;
				role.setHspeed(5);
				System.out.println("单只释放");
				break;

			default:
				break;
		}

//		return true;
		return super.onTouchEvent(event);
	}

	private void Draw()
	{
		try
		{
			canvas = sh.lockCanvas();

			if (canvas != null)
			{

				if (mapY >= screenH - Bgmap.getHeight())
				{
					canvas.drawBitmap(Bgmap, 0, mapY, paint);
				} else
				{
					canvas.drawBitmap(Bgmap, 0, mapY, paint);
					canvas.drawBitmap(Bgmap, 0, mapY + Bgmap.getHeight(), paint);
				}
				wall.Draw(canvas);
				role.Draw(canvas);
				canvas.drawText(X + "   "+Y, 200, 400, paint);
				if (Dead)
				{
					canvas.drawText("GameOver", 40, 100, paint);

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

		if (isCollsion(role.rect,wall.rect))
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
					argb = role.pic.getPixel(role.Width - (role.rect.right - wall.rect.left), wall.rect.bottom - role.rect.top);
				}
			}
			//

			if (argb != 0) // 检测wall左上角对应在forg图片内的像素值，不为0，即碰撞
			{
				Dead = true;
			}
		}
	}

	private boolean isCollsion(Rect role,Rect wall)
	{
		if (wall.top < role.top && wall.bottom < role.top)
		{

		} else if (wall.left < role.left && wall.right < role.left)
		{

		} else if (wall.left > role.left && wall.left > role.right)
		{

		} else if (wall.top+2 > role.top && wall.top+2 > role.bottom)
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
