package toolkit.display.demo;

import java.awt.Color;

public interface PlotableFunction {
	
	public double getMinX();
	
	public double getMaxX();
	
	public double f(double x);
	
	public double getMinY();
	
	public double getMaxY();
	
	public Color getColor();

}
