package screen;

@SuppressWarnings("serial")
public abstract class AutoRefreshScreen extends Screen implements Runnable{

	private static final int DEFAULT_FPS = 30;
	
	private Thread loop_thread;
	
	private int fps = -1;
	private long last_frame = -1;
	
	public AutoRefreshScreen(String title, int w, int h, int fps) {
		super(title, w, h);
		setFPS(fps);
		loop_thread = new Thread(this);
		loop_thread.start();
	}

	public void run(){
		while(true){
			super.repaint();
			last_frame = System.currentTimeMillis();
			try {
				long sleep_time = getFrameTime();
				if(sleep_time>0){
					Thread.sleep(sleep_time);
				}
			} catch (InterruptedException e) {	}
		}
	}
	
	public int getTimePerFrame(){
		return 1000/fps;
	}
	
	public long getFrameTime(){
		return max(0, getTimePerFrame()-getElapsedFrameTime());
	}
	
	public long getElapsedFrameTime(){
		return System.currentTimeMillis()-last_frame;
	}

	public void  setFPS(int fps){
		this.fps = (int) max(fps, DEFAULT_FPS);
	}
	
	public void close(){
		super.close();
		loop_thread = null;
	}
	
	@Override
	public void repaint() {}
	
	
	//---------------------------------------------private methods---------------------------------------------//
	private long max(long n1, long n2){
		return n1>=n2?n1:n2;
	}
	
}
