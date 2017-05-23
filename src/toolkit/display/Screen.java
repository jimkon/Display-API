package toolkit.display;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

// na dw ti tha kanw me ta scroll bar (auto scale disabled)
// na ftiaksw ta x y se point
//TODO na ftiaksw na mporeis na eksageis arxeio eikonas apo to screen, kai na vgazeis bufferedimage


@SuppressWarnings("serial")
public abstract class Screen extends JPanel {
	
	private JFrame frame;
	
	private Color background = Color.white;
	
	private BufferedImage image;
	private Graphics graphics;
	private boolean autofit = true;
	
	private boolean[] keys = new boolean[256];
	
	private boolean[] mouse_keys = new boolean[3];
	
	private int[] mouse_pos = new int[2];
	private Point mouse_click_pos = null;
	private Point mouse_press_pos = null;
	private Point mouse_release_pos = null;
	
	private boolean mouse_on_screen = false;
	
	public Screen(String title, int w, int h){
		image = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
		graphics = image.getGraphics();
		
		
		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // TODO
		//frame.setSize(w+10, h+40);
		
		Dimension dim = new Dimension(w, h);
		setSize(dim);
		setPreferredSize(dim);
		setMaximumSize(dim);
		setMaximumSize(dim);
		//setBounds(0, 0, dim.width, dim.height);
		frame.setContentPane(this);
		//frame.getContentPane().add(this);
		
		
		requestFocus();
		setFocusable(true);
		addAdapters();
		
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);

	}
	
	public void setBackground(int r, int g, int b){
		background = new Color(r, g, b);
	}
	
	public void setBackground(Color c){
		background = c;
	}
	
	public void paint(Graphics g){
		super.paint(g);
		graphics.setColor(background);
		graphics.fillRect(0, 0, getResolutionWidth(), getResolutionHeight());
		onEachFrame(graphics);
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}
	
	public int getResolutionHeight() {
		return image.getHeight();
	}
	
	public int getResolutionWidth() {
		return image.getWidth();
	}
	
	public void setAutoFit(boolean b){
		autofit = b;
	}
	
	public boolean isMousePressed(int key){
		return mouse_keys[key%mouse_keys.length];
	}
	
	/*public boolean isPressed(int key){
		return keys[key%keys.length];
	}*/
	
	public boolean isMouseOnScreen(){return mouse_on_screen;}
	
	public int getMouseX(){return mouse_pos[0];}
	
	public int getMouseY(){return mouse_pos[1];}
	
	public Point getMouseClick(){
		return mouse_click_pos;
	}
	
	public Point getMousePress(){
		return mouse_press_pos;
	}
	
	public Point getMouseUnpress(){
		return mouse_release_pos;
	}
    
	public JFrame getJFrame(){
		return frame;
	}
	
	public void close(){
		frame.dispose();
	}
	
	public void delay(int ms){
		if(ms<=0){
			return;
		}
		long wakeup = System.currentTimeMillis()+ms;
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(System.currentTimeMillis()<wakeup){
			continue;
		}
	}
	
	public abstract void onEachFrame(Graphics g) ;

	//TODO test
	public BufferedImage getScreenCopy(){
		BufferedImage res = new BufferedImage(getResolutionWidth(), getResolutionHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics graphs = res.getGraphics();
		graphs.drawImage(image, 0, 0, null);
		return res;
	}
	//TODO test
	public void printScreen(String path, String name, String ext) throws IOException{
		File file = new File(path+"\\"+name+"."+ext); 
		ImageIO.write(getScreenCopy(), ext, file);
	}
	
	private void addAdapters(){
		/*addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				keys[e.getKeyCode()] = true;
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				keys[e.getKeyCode()] = false;
			}
		});*/
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouse_press_pos = new Point(getMouseX(), getMouseY());
				mouse_keys[e.getButton()-1] = true; //NOBUTTON = 0
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				mouse_release_pos = new Point(getMouseX(), getMouseY());
				mouse_keys[e.getButton()-1] = false; //NOBUTTON = 0
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				mouse_click_pos = new Point(getMouseX(), getMouseY());
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {mouse_on_screen = true;}
			
			@Override
			public void mouseExited(MouseEvent e) {mouse_on_screen = false;}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				mouse_pos[0] = e.getX();
				mouse_pos[1] = e.getY();
				
				//mouse_pos[0] = SwingUtilities.convertMouseEvent(e.getComponent(), e, Screen.this).getX();TODO
				//mouse_pos[1] = SwingUtilities.convertMouseEvent(e.getComponent(), e, Screen.this).getY();TODO
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				mouse_pos[0] = e.getX();
				mouse_pos[1] = e.getY();
			}
		});
		addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				if(!autofit){
					image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
					graphics = image.getGraphics();
				}
				//System.out.println(getResolutionWidth()+", "+getResolutionHeight());
			}
		});
	}
	
	
}
