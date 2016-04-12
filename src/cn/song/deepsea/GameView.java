package cn.song.deepsea;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Callback, Runnable
{

	public static final int speed = 15;
	// private float textX = 20;
	// private float textY = 20;
	public static int screenH;
	public static int screenW;
	private SurfaceHolder sh;
	private Paint paint;
	private Canvas canvas;
	private Thread thread;

	private Bitmap Bgmap;
	private Bitmap overlap;

	private int mapY = 0;

	int tochX = 0;
	public static boolean touched = false;
	private boolean Dead = false;

	private Context context;
	private Wall wall;
	private Role role;

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
		wall = new Wall(context);
		role = new Role(context);
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		// TODO 自动生成的方法存根

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

			if (endTime - startTime < 50)
			{
				try
				{
					Thread.sleep(50 - (endTime - startTime));
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

		switch (event.getAction() & MotionEvent.ACTION_MASK)
		{
			case MotionEvent.ACTION_DOWN:

				System.out.println("触摸" + event.getPointerCount());
				System.out.println("触摸事件！");
				touched = true;
				if (event.getX() < screenW / 2)
				{
					role.setHspeed(-5);

				} else
				{
					role.setHspeed(5);
				}
				if (Dead)
				{
					Dead = false;
					new Thread(this).start();
				}
				return true;
			case MotionEvent.ACTION_POINTER_DOWN:

				touched = false;
				break;
			case MotionEvent.ACTION_UP:

				role.setHspeed(5);
				touched = false;
				System.out.println("单点释放！");
				break;

			default:
				break;
		}

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
		if ((role.right > wall.left && role.left < wall.right) && (role.bottom > wall.top && role.top < wall.bottom))
		{
			if (role.left < wall.left && role.bottom > wall.bottom)
			{
				overlap = Bitmap.createBitmap(role.pic, role.Width - (role.right - wall.left), 0,
						role.right - wall.left, wall.bottom - role.top);
			} else if (role.left < wall.left && role.bottom > wall.bottom && role.top < wall.top)
			{
				overlap = Bitmap.createBitmap(role.pic, role.Width - (role.right - wall.left),
						role.Height - (role.bottom - wall.bottom), role.right - wall.left, role.bottom - wall.bottom);
			}
			int[] pixels = new int[overlap.getWidth() * overlap.getHeight()];
			overlap.getPixels(pixels, 0, overlap.getWidth(), 0, 0, overlap.getWidth(), overlap.getHeight());

			for (int i = 0; i < pixels.length; i++)
			{
				int clr = pixels[i];
				// int alpha = (clr & 0xff000000)>>24;
				int red = (clr & 0x00ff0000) >> 16; // 取高两位
				int green = (clr & 0x0000ff00) >> 8; // 取中两位
				int blue = clr & 0x000000ff; // 取低两位
				System.out.println("r=" + red + ",g=" + green + ",b=" + blue);
				// if (red != 255 || green != 255 || blue != 255)
				// {
				// Dead = true;
				// }
				if (red == 0 && green == 0 && blue == 0)
				{
					continue;
				} else
				{
					Dead = true;
				}
			}
		} else
		{
			Dead = false;
		}
	}

}
