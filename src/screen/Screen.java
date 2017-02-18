package screen;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
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

//TODO na ftiaksw to resize me to scale
// na dw ti tha kanw me ta scroll bar (auto scale disabled)
//TODO na ftiaksw na mporeis na eksageis arxeio eikonas apo to screen, kai na vgazeis bufferedimage


@SuppressWarnings("serial")
public abstract class Screen extends Component {
	
	private JFrame frame;
	
	private BufferedImage image;
	private Graphics graphics;
	private boolean autofit = true;
	
	private boolean[] keys = new boolean[256];
	
	private boolean[] mouse_keys = new boolean[3];
	
	private int[] mouse_pos = new int[2];
	
	private boolean mouse_on_screen = false;
	
	public Screen(String title, int w, int h){
		image = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
		graphics = image.getGraphics();
		
		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     //
		
		Dimension dim = new Dimension(w, h);
		setSize(dim);
		setPreferredSize(dim);
		setMaximumSize(dim);
		setMaximumSize(dim);
		frame.add(this);
		
		requestFocus();
		setFocusable(true);
		addAdapters();
		
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		
	}
	
	public void paint(Graphics g){
		super.paint(g);
		graphics.setColor(Color.WHITE);
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
	
	public boolean isPressed(int key){
		return keys[key%keys.length];
	}
	
	public boolean isMouseOnScreen(){return mouse_on_screen;}
	
	public int getMouseX(){return mouse_pos[0];}
	
	public int getMouseY(){return mouse_pos[1];}
    
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
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				keys[e.getKeyCode()] = true;
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				keys[e.getKeyCode()] = false;
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouse_keys[e.getButton()-1] = true; //NOBUTTON = 0
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				mouse_keys[e.getButton()-1] = false; //NOBUTTON = 0
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
