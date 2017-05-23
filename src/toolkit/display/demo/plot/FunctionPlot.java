package toolkit.display.demo.plot;

import java.awt.Color;
import toolkit.display.demo.plot.Plotable;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import toolkit.display.AutoRefreshScreen;

@SuppressWarnings("serial")
public class FunctionPlot extends AutoRefreshScreen{
	
	public static void main(String[] args) {
		new FunctionPlot();
	}

	private final static Color BACKGROUND = new Color(200, 200, 200)
											,AXIS = new Color(0, 0, 0)
											,SUBAXIS = new Color(130, 130, 130)
											,MOUSE_AXIS = new Color(255, 0, 0, 100)
											,MOUSE_COORDS = new Color(155, 0, 0)
											,MARK_AXIS = new Color(0, 0, 255, 100)
											,MARK_COORDS = new Color(0, 0, 155);
	
	private final static int MAX_NUMBER_OF_SUBAXIS = 15, MIN_NUMBER_OF_SUBAXIS = 4;
	
	private ArrayList<Plotable> plotable = new ArrayList<Plotable>(); 
	
	private double minx = -20, maxx = 10, miny = -20, maxy = 10;
	private boolean min_max_flag = false;
	
	private boolean showMouseAxis = true; 
	private Point mark_point = null;
	
	
	public FunctionPlot() {
		super("Function plot", 800, 500, 60);
		setBackground(BACKGROUND);
		getJFrame().setResizable(false);
		start();
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) { 
				if(SwingUtilities.isRightMouseButton(e)){
					showMouseAxis = !showMouseAxis;
				}
				if(SwingUtilities.isLeftMouseButton(e)){
					if(mark_point == null){
						mark_point = new Point(getMouseX(), getMouseY());
					}
					else{
						mark_point = null;
					}
				}
			}
		});
		
	}

	public void addPlotable(Plotable f){
		if(f!=null){
			plotable.add(f);
			repaint();
		}
	}
	
	@Override
	public void onEachFrame(Graphics g) {		
//		final int axis_x = mapDouble(0, minx, maxx, 0, getWidth());
//		final int axis_y = mapDouble(0, miny, maxy, getHeight(), 0);
//		
//		g.setColor(AXIS);
//		g.drawLine(0, axis_y, getWidth(), axis_y);
//		g.drawLine(axis_x, 0, axis_x, getHeight());
		
		if(min_max_flag){
			drawLine(g, minx, 0, maxx, 0, AXIS);
			drawLine(g, 0	, miny, 0, maxy, AXIS);
		}
		
		for(Plotable f:plotable){
			if(f instanceof PlotableFunction){
				PlotableFunction t = (PlotableFunction)f;
				drawFunction(g, t);
			}
			if(f instanceof PlotableArray){
				PlotableArray t = (PlotableArray)f;
				drawArray(g, t);
			}
		}
		
		if(isMouseOnScreen() && showMouseAxis){
			final int x =  getMouseX(), y = getMouseY();
			//System.out.println(x+"   "+y);//TODO
		    
			g.setColor(MOUSE_AXIS);
			g.drawLine(x, 0, x, getHeight());
			g.drawLine(0, y, getWidth(), y);
			
			final String text = String.format("(%.2f, %.2f)", mapInt(x, 0, getWidth(), minx, maxx), mapInt(y, getHeight(), 0, miny, maxy));
			FontRenderContext frc = ((Graphics2D)g).getFontRenderContext();
			GlyphVector gv = ((Graphics2D)g).getFont().createGlyphVector(frc, text);
			Rectangle rect = gv.getPixelBounds(null, x, y);

			g.setColor(MOUSE_COORDS);
			g.drawString(text, rect.x+rect.width<getWidth()?rect.x:rect.x-rect.width, rect.y>0?y:rect.height-1);
		}
		
		if(mark_point != null){
			final int x =  mark_point.x, y = mark_point.y;

		    
			g.setColor(MARK_AXIS);
			g.drawLine(x, 0, x, getHeight());
			g.drawLine(0, y, getWidth(), y);
			
			final String text = String.format("(%.2f, %.2f)", mapInt(x, 0, getWidth(), minx, maxx), mapInt(y, getHeight(), 0, miny, maxy));
			FontRenderContext frc = ((Graphics2D)g).getFontRenderContext();
			GlyphVector gv = ((Graphics2D)g).getFont().createGlyphVector(frc, text);
			Rectangle rect = gv.getPixelBounds(null, x, y);

			g.setColor(MARK_COORDS);
			g.drawString(text, rect.x+rect.width<getWidth()?rect.x:getWidth()-rect.width-2, rect.y>0?y:rect.height-1);
		}
		
		
	}
	
	private void drawFunction(Graphics g, PlotableFunction f){
		for(double i = f.getMinX(); i<f.getMaxX(); i += getXStep()){
			//System.out.println(String.format("i=%f  f(i)=%f   i+1 =%f   f(i+1)=%f", i, f.f(i), i+getXStep(), f.f(i+getXStep())));
			drawLine(g, i, f.f(i), i+getXStep(), f.f(i+getXStep()), f.getColor());
		}
	}
	
	private void drawArray(Graphics g, PlotableArray f){
		final double x_step = f.step();
		for(int i = 0; i<f.getNumberOfPoints()-1; i ++){
			drawLine(g, f.getMinX()+i*x_step, f.f(i), f.getMinX()+(i+1)*x_step, f.f(i+1), f.getColor());
		}
	}
	
	private void drawLine(Graphics g, double x0, double y0, double x1, double y1, Color color){
		//System.out.print(minx+"   "+maxx+"   "+miny+"   "+maxy);
		if(min_max_flag){
			minx = min(minx, x0);
			maxx = max(maxx, x0);
			miny = min(miny, y0);
			maxy = max(maxy, y0);
			
			minx = min(minx, x1);
			maxx = max(maxx, x1);
			miny = min(miny, y1);
			maxy = max(maxy, y1);
		}
		else{
			minx = min(x0, x1);
			maxx = max(x0, x1);
			miny = min(y0, y1);
			maxy = max(y0, y1);
			min_max_flag = true;
		}
		//System.out.println("\t\t"+minx+"   "+maxx+"   "+miny+"   "+maxy);
		g.setColor(color);
		g.drawLine(mapDouble(x0, minx, maxx, 0, getWidth()), mapDouble(y0, miny, maxy, getHeight(), 0), mapDouble(x1, minx, maxx, 0, getWidth()), mapDouble(y1, miny, maxy, getHeight(), 0));
	}
	
	private double getXStep(){
		return (maxx-minx)/getWidth();
	}
	
	private double min(double d1, double d2){
		return d1<=d2?d1:d2;
	}
	
	private double max(double d1, double d2){
		return d1>d2?d1:d2;
	}
	
	private static int mapDouble(double x, double xl, double xh, int pl, int ph){
		return (int) (((x-xl)/(xh-xl))*(ph-pl)+pl);
	}
	
	private static double mapInt(double p, int pl, int ph, double xl, double xh){
		return  (((double)(p-pl)/(ph-pl))*(xh-xl)+xl);
	}
	
	
	
}
