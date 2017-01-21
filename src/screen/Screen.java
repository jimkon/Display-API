package screen;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JFrame;


public abstract class Screen extends Component implements Runnable, KeyListener, MouseListener, MouseMotionListener{

	public static void main(String[] args)  {
		MyScreen ms = new MyScreen();
		
	}
	
	private JFrame frame;
	private Thread loop_thread;
	
	private int fps = -1;
	
	private boolean[] keys = new boolean[256];
	
	private boolean[] mouse_keys = new boolean[3];
	
	private int[] mouse_pos = new int[2];
	
	public Screen(String title, int w, int h){
		
		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     //
		frame.setResizable(false);
		
		Dimension dim = new Dimension(w, h);
		setSize(dim);
		setPreferredSize(dim);
		setMaximumSize(dim);
		setMaximumSize(dim);
		frame.add(this);
		
		requestFocus();
		setFocusable(true);
		addKeyListener(this);
		
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		
	}
	
	public void paint(Graphics g){
		super.paint(g);
		onEachFrame(g);
	}

	public void run(){
		while(true){
			repaint();
			try {
				int sleep_time = getFrameTime();
				if(sleep_time>0){
					Thread.sleep(sleep_time);
				}
			} catch (InterruptedException e) {	}
		}
	}
	
	public int getTimePerFrame(){
		if(fps>0){
			return 1000/fps;
		}
		return 0;
	}
	
	public int getFrameTime(){
		//TODO get the time for the next frame
		//if fps = -1 return 0;
		return 0;
	}
	
	public int getElapsedFrameTime(){
		//TODO get the time pasts from the last frame
		return 0;
	}
	
	public void setAutoRepaint(int n){// n for the times per second
		//TODO check if loop_thread is running or have to be created before setting the time
		//setfps
	}
	
	public void repaint(){
		if(fps<=0){
			super.repaint();
		}
	}
		
	public boolean isPressed(int key){
		//TODO Check key value
		return keys[key];
	}
    
	public JFrame getJFrame(){
		return frame;
	}
	
	public void close(){
		loop_thread = null;
		frame.dispose();
	}
	
	public abstract void onEachFrame(Graphics g) ;
	
	/// overrides
	@Override
	public void mousePressed(MouseEvent e) {
		mouse_keys[e.getButton()-1] = true; //NOBUTTON = 0
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		mouse_keys[e.getButton()-1] = false; //NOBUTTON = 0
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		mouse_pos[0] = e.getX();
		mouse_pos[1] = e.getY();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	
	@Override
	public void mouseExited(MouseEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}

}

class MyScreen extends Screen{

	public MyScreen() {
		super("Test", 600, 400);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onEachFrame(Graphics g) {
		Random r = new Random();
		g.setColor(new Color(r.nextInt()));
		g.fillRect(r.nextInt(200), r.nextInt(200), r.nextInt(400), r.nextInt(200));
	}
	
}
