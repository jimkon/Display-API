package demo;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import screen.AutoRefreshScreen;

@SuppressWarnings("serial")
public class FunctionPlot extends AutoRefreshScreen{
	
	public static void main(String[] args) {
		new FunctionPlot();
	}

	private ArrayList<PlotableFunction> plotableFunctions = new ArrayList<PlotableFunction>(); 
	private double minx, maxx, miny, maxy;
	
	public FunctionPlot() {
		super("Function plot", 800, 500, 60);
		addFunction(new ExampleFunction(1));
		addFunction(new ExampleFunction(2));
		addFunction(new ExampleFunction(3));
		addFunction(new SinExampleFunction());
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
	
	@SuppressWarnings("unused")
	private double getY(int py){
		return (getResolutionHeight()-py)*y_per_pix+miny;
	}
	
	private int getPixelX(double x){
		return (int) ((x-minx)/x_per_pix);
	}
	
	private int getPixelY(double y){
		y -= miny;
		return (int) (getResolutionHeight() - y/y_per_pix);
	}
	
	private int F(PlotableFunction f, double x){
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
//		System.out.println(getResolutionWidth()+","+getResolutionHeight());
		x_per_pix = (maxx-minx)/getResolutionWidth();
		y_per_pix = (maxy-miny)/getResolutionHeight();
	}
	
	private double x_per_pix = 1, y_per_pix = 1;
	@Override
	public void onEachFrame(Graphics g) {
		calcXYRatios();
		
		g.setColor(Color.GRAY);
		if(maxx>0 && minx<0){
			int x0 = getPixelX(0);
			g.drawLine(x0, 0, x0, getResolutionHeight());
		}
		if(maxy>0 && miny<0){
			int y0 = getPixelY(0);
			g.drawLine(0, y0, getResolutionWidth(), y0);
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
					continue;
				}
				int stringY = F(f, x);
				
				g.setColor(f.getColor());
				String string = String.format("%.3f", f.f(x));
				int stringW = g.getFontMetrics().stringWidth(string);
				int stringH = g.getFontMetrics().getHeight();
				g.setColor(Color.gray);
				g.drawLine(0, stringY, getResolutionWidth(), stringY);
				g.drawLine(stringX, 0, stringX, getResolutionHeight());
				g.drawString(String.format("%.3f", x) , stringX, getMouseY());
				if(getResolutionWidth()-stringX<stringW){
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
			}
		}
		
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
	class SinExampleFunction implements PlotableFunction{

		@Override
		public double getMinX() {
			// TODO Auto-generated method stub
			return -20;
		}

		@Override
		public double getMaxX() {
			// TODO Auto-generated method stub
			return 60;
		}

		@Override
		public double f(double x) {
			// TODO Auto-generated method stub
			return 5*Math.sin(x);
		}

		@Override
		public double getMinY() {
			// TODO Auto-generated method stub
			return -5;
		}

		@Override
		public double getMaxY() {
			// TODO Auto-generated method stub
			return 5;
		}

		@Override
		public Color getColor() {
			// TODO Auto-generated method stub
			return Color.red;
		}
		
	}
}
