package demo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.util.ArrayList;

import screen.AutoRefreshScreen;
import screen.Screen;

@SuppressWarnings("serial")
public class FunctionPlot extends AutoRefreshScreen{
	
	public static void main(String[] args) {
		new FunctionPlot();
	}

	private ArrayList<PlotableFunction> plotableFunctions = new ArrayList<PlotableFunction>(); 
	private double minx, maxx, miny, maxy;
	
	public FunctionPlot() {
		super("Function plot", 800, 500, 20);
		addFunction(new ExampleFunction(1));
		addFunction(new ExampleFunction(2));
		addFunction(new ExampleFunction(3));
	}

	public void addFunction(PlotableFunction f){
		if(f!=null){
			plotableFunctions.add(f);
			repaint();
		}
	}
	
	private double getX(int px){
		return px*x_per_pix+minx;
	}
	
	private double getY(int py){
		return (getHeight()-py)*y_per_pix+miny;
		
	}
	
	private int getPixelX(double x){
//		System.out.println("min = "+minx+"   x="+x+"   px="+(int) ((x-minx)/x_per_pix));
		return (int) ((x-minx)/x_per_pix);
	}
	
	private int getPixelY(double y){
//		System.out.println(y+"   "+miny+"   "+(int) (getHeight() - y/y_per_pix));
		y -= miny;
		return (int) (getHeight() - y/y_per_pix);
	}
	
	private int F(PlotableFunction f, double x){
//		System.out.println("x="+x+"     px="+p+"    fx="+f.f(x)+"      py="+getPixelY(f.f(x)));
		return getPixelY(f.f(x));
	}
	
	private void calcXYRatios(){
		if(plotableFunctions.size()==0){
			return;
		}
		PlotableFunction first = plotableFunctions.get(0);
		minx = first.getMinX();
		maxx = first.getMaxX();
		miny = first.getMinY();
		maxy = first.getMaxY();
		for(int i=1; i<plotableFunctions.size(); i++){
			minx = Math.min(minx, plotableFunctions.get(i).getMinX());
			maxx = Math.max(maxx, plotableFunctions.get(i).getMaxX());
			miny = Math.min(miny, plotableFunctions.get(i).getMinY());
			maxy = Math.max(maxy, plotableFunctions.get(i).getMaxY());
		}
//		System.out.println("minx="+minx+"  maxx="+maxx+"  miny="+miny+"   maxy="+maxy);
		x_per_pix = (maxx-minx)/getWidth();
		y_per_pix = (maxy-miny)/getHeight();
//		System.out.println("for x  step="+x_per_pix+"   range="+(maxx-minx)+"   "+getWidth());
//		System.out.println("for y  "+y_per_pix+"   "+(maxy-miny)+"   "+getHeight());
	}
	
	private double x_per_pix = 1, y_per_pix = 1;
	@Override
	public void onEachFrame(Graphics g) {
		calcXYRatios();
		
		g.setColor(Color.GRAY);
		if(maxx>0 && minx<0){
			int x0 = getPixelX(0);
			g.drawLine(x0, 0, x0, getHeight());
		}
		if(maxy>0 && miny<0){
			int y0 = getPixelY(0);
			g.drawLine(0, y0, getWidth(), y0);
		}
		
		if(isMouseOnScreen()){			
			
			for(int i=0; i<plotableFunctions.size(); i++){
//				g.setColor(Color.BLACK);
//				double xx = getX(getMouseX());
//				double yy = getY(getMouseY());
//				g.drawString(String.format("(%.3f, %.3f)  (%d, %d)", xx, yy, getMouseX(), getMouseY()), getMouseX()-100, getMouseY()+100);
				PlotableFunction f = plotableFunctions.get(i);
				int stringX = getMouseX();
				double x = getX(stringX);
				if(x<f.getMinX() || x>f.getMaxX()){
//					System.out.println(x+"   "+f.getMinX()+"    "+f.getMaxX());
					continue;
				}
				int stringY = F(f, x);
				
				g.setColor(f.getColor());
				String string = String.format("%.3f", f.f(x));
				int stringW = g.getFontMetrics().stringWidth(string);
				int stringH = g.getFontMetrics().getHeight();
				g.setColor(Color.gray);
				g.drawLine(0, stringY, getWidth(), stringY);
				g.drawLine(stringX, 0, stringX, getHeight());
				g.drawString(String.format("%.3f", x) , stringX, getMouseY());
				if(getWidth()-stringX<stringW){
					stringX -= stringW;
				}
				if(stringY<stringH){
					stringY += stringH;
				}
				g.setColor(f.getColor());
				g.drawString(string, stringX, stringY);
				g.setColor(Color.gray);
				
				
			}
		}
		
		
		for(int i=0; i<plotableFunctions.size(); i++){
			PlotableFunction f = plotableFunctions.get(i);
			g.setColor(f.getColor());
			for(double j=f.getMinX(); j<=f.getMaxX(); j+=x_per_pix){
				g.drawLine(getPixelX(j), F(f, j), getPixelX(j+x_per_pix), F(f, j+x_per_pix));
//				g.drawLine(j, y1, j, y1);
			}
		}
//		System.out.println("asdasd       "+isMouseOnScreen());
		
	}
	
	class ExampleFunction implements PlotableFunction{

		public ExampleFunction(int c) {
			this.c = c;
		}
		
		int c = 1;
		
		@Override
		public double getMinX() {
			// TODO Auto-generated method stub
			return -10*c;
		}

		@Override
		public double getMaxX() {
			// TODO Auto-generated method stub
			return 10*c;
		}

		@Override
		public double f(double x) {
			// TODO Auto-generated method stub
			return x*c;
		}

		@Override
		public double getMinY() {
			// TODO Auto-generated method stub
			return f(getMinX());
		}

		@Override
		public double getMaxY() {
			// TODO Auto-generated method stub
			return f(getMaxX());
		}

		@Override
		public Color getColor() {
			// TODO Auto-generated method stub
			return new Color(30*c%255, 60*c%255, 90*c%255);
		}
		
	}
}
