package demo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;
import screen.Screen;

public class VisualSortingDemo extends Screen{

	public static void main(String[] args) {
		Random r = new Random();
		int[] ex = new int[r.nextInt(400)];
		for(int i=0; i<ex.length; i++){
			ex[i] = r.nextInt(600);
		}
		new VisualSortingDemo(ex);
	}
	private boolean done = false;
	private int[] array;
	private Random random= new Random();
	private long start, end;
	
	public VisualSortingDemo(int[] ex) {
		super("Sorting Data Visually", 800, 600);
		array = ex;
		start = System.currentTimeMillis();
		merge_sort(0, array.length-1);
		end = System.currentTimeMillis();
		done = true;
		repaint();
	}

	@Override
	public void onEachFrame(Graphics g) {
		if(getWidth()!=800){
			System.out.println("width  =  "+getWidth());
		}
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, getWidth(), getHeight());
		double ratio = (double)array.length/getWidth();
		int ind = -1;
		for(int i=0; i<getWidth(); i++){
			int index = (int) (i*ratio);
			if(ind==-1 || ind!=index){
				ind = index;
				double color = (double)array[index]/600;
				color *= 255;
				int c = (int) color;
				c *= 0x00010101;
				g.setColor(new Color(c));
			}
			g.drawLine(i, getHeight(), i,	getHeight()-array[index]);
		}
		if(done){
			long time = (end-start)/1000;
//			System.out.println(time+" s to sort "+array.length+" numbers");
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 30));
			g.drawString(time+" s to sort "+array.length+" numbers", 100, 50);
		}
	}
	
	public void bubble_sort(){
		for(int i=0; i<array.length; i++){
			repaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {}
			for(int j = 1; j < (array.length - i); j++) {
				if(array[j-1]>array[j]){
					int temp = array[j-1];
					array[j-1] = array[j];
					array[j] = temp;
				}
			}
		}
	}

	public void merge_sort(int s, int e){
		if(s==e){
			return;
		}
		else{
			int m = s+(e-s)/2;
//			System.out.println(s+"  "+m+"  "+e);
			merge_sort(s, m);
			merge_sort(m+1, e);
			int i1 = s, i2 = m+1;
			int[] t = new int[e-s+1];
			for(int i=0; i<t.length; i++){
				if(i1>m){
					t[i] = array[i2++];
					continue;
				}
				if(i2>e){
					t[i] = array[i1++];
					continue;
				}
				if(array[i1]<array[i2]){
					t[i] = array[i1++];
				}
				else{
					t[i] = array[i2++];
				}
			}
			for(int i=s; i<=e; i++){
				array[i] = t[i-s];
			}
			repaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {}
		}
	}
}
