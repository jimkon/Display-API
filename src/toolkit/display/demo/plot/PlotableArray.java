package toolkit.display.demo.plot;

public interface PlotableArray extends Plotable{
	
	public int getNumberOfPoints();
	
	public double f(int x);
	
	default double step(){
		 return (getMaxX()-getMinX())/getNumberOfPoints();
	}

}
