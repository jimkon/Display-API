package toolkit.display.demo.plot;

import java.awt.Color;

public class TestPlot {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TestPlot();
	}
	
	public TestPlot(){
		FunctionPlot fp = new FunctionPlot();
		//fp.addPlotable(new ExampleFunction(1));
		//fp.addPlotable(new ExampleFunction(2));
		//fp.addPlotable(new ExampleFunction(3));
		//fp.addPlotable(new SinExampleFunction());
		fp.addPlotable(new ArrayExample());
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
		public Color getColor() {
			// TODO Auto-generated method stub
			return Color.red;
		}
		
	}
	
	class ArrayExample implements PlotableArray{

		
		private int[] array = new int[]{-1, 3, 2, 3, 1, 3, 0};
		
		@Override
		public double getMinX() {
			// TODO Auto-generated method stub
			return -1;
		}

		@Override
		public double getMaxX() {
			// TODO Auto-generated method stub
			return 5;
		}

		@Override
		public Color getColor() {
			// TODO Auto-generated method stub
			return Color.red;
		}

		@Override
		public int getNumberOfPoints() {
			// TODO Auto-generated method stub
			return array.length;
		}

		@Override
		public double f(int x) {
			// TODO Auto-generated method stub
			return array[x];
		}
		
	}

}
