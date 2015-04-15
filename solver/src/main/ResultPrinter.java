package main;


import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;
import org.math.plot.Plot3DPanel;

public class ResultPrinter {
	
	

	private static final Plot2DPanel plot = new Plot2DPanel();
	private static final Plot3DPanel plot2 = new Plot3DPanel();

    private static final JFrame frame = new JFrame("Plot panel");

    private static final Dimension SIZE = new Dimension(600, 400);
    
    static {
        plot.setPreferredSize(SIZE);
        frame.add(plot2);
        frame.pack();
        frame.setVisible(true);
    }
    
	public static void printResult(List<Double> result){
		double[] y = new double[result.size()];
        double[] x = new double[result.size()];
        for (int i = 0; i < result.size(); ++i) {
            y[i] = result.get(i);
            x[i] = i / (double) (result.size() - 1);
        }

        plot.removeAllPlots();
        plot.addLinePlot("my plot", x, y);
	}
	
	public static void printResult(double [] x, double[] y){
		

//        plot.removeAllPlots();
        plot.addLinePlot("my plot", x, y);
	}
	
	public static void printResult(double [] x, double[] y, double[][]z){
//		double []x = new double[z.length];
//		double []y = new double[z.length];
		for(int i=0;i<z.length;++i){
			for(int j=0;j<y.length;++j){
				x[i] = i/(double)(x.length);
				y[j] = j/(double)(y.length);
				z[i][j] = Math.atan2(x[i], y[j]);
			}
			
		}
		
		plot2.removeAllPlots();
		plot2.addGridPlot("sialal", x, y, z);
		
	}
        
}
