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
	private int x0 = 400, y0 = 300;
	
	public FunctionPlot() {
		super("Function plot", 800, 500, 10);
		addFunction(new ExampleFunction());
	}

	public void addFunction(PlotableFunction f){
		if(f!=null){
			plotableFunctions.add(f);
			repaint();
		}
	}
	
	public int getPixelX(double x){
		return (int) (x/x_per_pix);
	}
	
	public int getPixelY(double y){
		return (int) (getHeight() - y/y_per_pix);
	}
	
	private void calcXYRatios(){
		PlotableFunction first = plotableFunctions.get(0);
		double minx = first.getMinX(), maxx = first.getMaxX();
		double miny = first.getMinY(), maxy = first.getMaxY();
		for(int i=1; i<plotableFunctions.size(); i++){
			minx = Math.min(minx, plotableFunctions.get(i).getMinX());
			maxx = Math.max(maxx, plotableFunctions.get(i).getMaxX());
			miny = Math.min(miny, plotableFunctions.get(i).getMinY());
			maxy = Math.max(maxy, plotableFunctions.get(i).getMaxY());
		}
		x_per_pix = (maxx-minx)/getWidth();
		y_per_pix = (maxy-miny)/getHeight();
//		System.out.println(x_per_pix+"   "+(maxx-minx)+"   "+getWidth());
//		System.out.println(y_per_pix+"   "+(maxy-miny)+"   "+getHeight());
	}
	
	private double x_per_pix = 1, y_per_pix = 1;
	@Override
	public void onEachFrame(Graphics g) {
		calcXYRatios();
		for(int i=0; i<plotableFunctions.size(); i++){
			PlotableFunction f = plotableFunctions.get(i);
			g.setColor(f.getColor());
			int s = getPixelX(f.getMinX());
			int e = getPixelX(f.getMaxX());
//			int sy = (int) (f.getMinY()/y_per_pix);
//			int ey = (int) (f.getMaxY()/y_per_pix);
//			System.out.println(s+"->"+s*x_per_pix+"   "+e+"->"+e*x_per_pix);
//			System.out.println(sy+"->"+sy*y_per_pix+"   "+ey+"->"+ey*y_per_pix);
			for(int j=s; j<=e; j++){
				double x1 = j*x_per_pix, y1 = f.f(x1), x2 = (j+1)*x_per_pix, y2 = f.f(x2);
				int py1 = getPixelY(y1), py2 =  getPixelY(y2);
//				System.out.println("x="+x1+"   y="+y1+"    px="+j+"  py="+py1);
				g.drawLine(j, py1, j+1, py2);
//				g.drawLine(j, y1, j, y1);
			}
		}
//		System.out.println("asdasd       "+isMouseOnScreen());
		if(isMouseOnScreen()){			
			for(int i=0; i<plotableFunctions.size(); i++){
				int stringX = getMouseX();
				int stringY = getPixelY(plotableFunctions.get(i).f(stringX*x_per_pix));
				PlotableFunction f = plotableFunctions.get(i);
				g.setColor(f.getColor());
				String string = "("+stringX*x_per_pix+", "+f.f(stringX*x_per_pix)+")";
				int stringW = g.getFontMetrics().stringWidth(string);
				if(getWidth()-stringX<stringW){
					stringX -= stringW;
				}
				g.drawString(string, stringX, stringY);
			}
		}
	}
	
	class ExampleFunction implements PlotableFunction{

		@Override
		public double getMinX() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double getMaxX() {
			// TODO Auto-generated method stub
			return 200;
		}

		@Override
		public double f(double x) {
			// TODO Auto-generated method stub
			return x*x;
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
			return Color.red;
		}
		
	}
}
