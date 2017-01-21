package screen;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;


@SuppressWarnings("serial")
public abstract class Screen extends Component implements KeyListener, MouseListener, MouseMotionListener{
	
	private JFrame frame;
	
	
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
		
	public boolean isPressed(int key){
		//TODO Check key value
		return keys[key];
	}
    
	public JFrame getJFrame(){
		return frame;
	}
	
	public void close(){
		frame.dispose();
	}
	
	public boolean dealy(int ms){
		//TODO 
		return false;
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
