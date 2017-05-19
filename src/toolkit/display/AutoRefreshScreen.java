package toolkit.display;

@SuppressWarnings("serial")
public abstract class AutoRefreshScreen extends Screen implements Runnable{

	public static final int NOT_LIMITED_FPS = -1;
	
	private Thread loop_thread;
	
	private int fps = NOT_LIMITED_FPS, actual_fps = 0;
	private long last_frame = -1;
	
	public AutoRefreshScreen(String title, int w, int h, int fps) {
		super(title, w, h);
		setFPS(fps);
		loop_thread = new Thread(this);
	}

	public void run(){
		long last = System.currentTimeMillis();
		int count_fps = 0;
		while(true){
			if(System.currentTimeMillis()-last>=1000){
				actual_fps = count_fps;
				count_fps = 0;
				last = System.currentTimeMillis();
			}
			super.repaint();
			last_frame = System.currentTimeMillis();
			int sleep_time = (int) getFrameTime();
			count_fps++;
			delay(sleep_time);
		}
	}
	
	public void start(){
		loop_thread.start();
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
		if(fps!=0){ 
			this.fps = fps;
		}
	}
	
	public int getFPS(){
		return actual_fps;
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
