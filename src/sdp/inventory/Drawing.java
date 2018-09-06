package sdp.inventory;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * @author: Zhen Chen
 * @email: 15011074486@163.com
 * @date: Jul 10, 2018---6:54:43 PM
 * @description: drawing pictures For SDP problems to analysis
 */

public class Drawing {

	/**
	 * drawing a picture about optimal ordering quantities for different initial
	 * inventory levels
	 */
	public void drawXQ(double[][] xQ) {
		XYSeries seriesQ = new XYSeries("xQSeries");
		int N = xQ.length;
		for (int i = 0; i < N; i++) {
			seriesQ.add(xQ[i][0], xQ[i][1]);
		}

		XYSeriesCollection seriesCollection = new XYSeriesCollection();
		seriesCollection.addSeries(seriesQ);

		JFreeChart chart = ChartFactory.createXYLineChart("Optimal Q with different x", // chart title
				"x", // x axis label
				"Q", // y axis label
				seriesCollection, // data
				PlotOrientation.VERTICAL, false, // include legend
				true, // tooltips
				false // urls
				);
		ChartFrame frame = new ChartFrame("chen zhen's picture", chart);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * 
	 * drawing a simple picture for G(), with initial cash
	 */
	public void drawSimpleG(double[][] yG, double iniCash) {
		XYSeries seriesG = new XYSeries("yQSeries");
		int N = yG.length;
		for (int i = 0; i < N; i++) {
			seriesG.add(yG[i][0], yG[i][1]);
		}

		XYSeriesCollection seriesCollection = new XYSeriesCollection();
		seriesCollection.addSeries(seriesG);
		String cashString = String.valueOf(iniCash);
		String title = "G(y) with different order-up-to level y, B0 = " + cashString;

		JFreeChart chart = ChartFactory.createXYLineChart(title, // chart title
				"y", // x axis label
				"G(y)", // y axis label
				seriesCollection, // data
				PlotOrientation.VERTICAL, false, // include legend
				true, // tooltips
				false // urls
				);

		ChartFrame frame = new ChartFrame("chen zhen's picture", chart);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * 
	 * drawing a simple picture for G()
	 */	
	public void drawSimpleG(double[][] yG) {
		XYSeries seriesG = new XYSeries("yQSeries");
		int N = yG.length;
		for (int i = 0; i < N; i++) {
			seriesG.add(yG[i][0], yG[i][1]);
		}

		XYSeriesCollection seriesCollection = new XYSeriesCollection();
		seriesCollection.addSeries(seriesG);

		JFreeChart chart = ChartFactory.createXYLineChart("G(y) with different order-up-to level y", // chart title
				"y", // x axis label
				"G(y)", // y axis label
				seriesCollection, // data
				PlotOrientation.VERTICAL, false, // include legend
				true, // tooltips
				false // urls
				);

		ChartFrame frame = new ChartFrame("chen zhen's picture", chart);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * 
	 * drawing G() with s, S in different colors
	 */
	public void drawGAndsS(double[][] yG, double fixedOrderingCost) {
		XYSeriesCollection seriesCollection = new XYSeriesCollection();
		XYSeries seriesG = new XYSeries("yQSeries");
		XYSeries seriesS = new XYSeries("SSeries");
		XYSeries seriesSmalls = new XYSeries("seriesSmalls");
		ArrayList<Double> recordS = new ArrayList<>();
		int N = yG.length; int SNum = 0;
		for (int i = 0; i < N; i++) {
			seriesG.add(yG[i][0], yG[i][1]);
			if (N > 2 && i > 1)
				if (yG[i - 1][1] < yG[i - 2][1] - 0.01 && yG[i - 1][1] < yG[i][1] - 0.01) {
					seriesS.add(yG[i - 1][0], yG[i - 1][1]);
					recordS.add(yG[i - 1][1]);
					SNum++;
					// make S in descending order
					if (SNum >1) {
						if (recordS.get(SNum - 1) > recordS.get(SNum-2)) {
							seriesS.remove(SNum - 1);
							SNum--;
						}
					}
					for (int j = i - 1; j >= 0; j--) 
						if (yG[j][1] > yG[i - 1][1] + fixedOrderingCost) {
							seriesSmalls.add(yG[j][0], yG[j][1]);
							System.out.printf("the slope at s is: %.2f\n", yG[j][1] - yG[j - 1][1]);
							break;
						}
				}
			if (i == N - 1 && SNum == 0) {
				seriesS.add(yG[i][0], yG[i][1]);
				for (int j = i - 1; j >= 0; j--) 
					if (yG[j][1] > yG[i - 1][1] + fixedOrderingCost) {
						seriesSmalls.add(yG[j][0], yG[j][1]);
						break;
					}
			}
		}
		
		// show the slope below s
		

		seriesCollection.addSeries(seriesG);
		seriesCollection.addSeries(seriesS);
		seriesCollection.addSeries(seriesSmalls);
		JFreeChart chart = ChartFactory.createXYLineChart("Optimal cost related with initial inventory level", // chart
				// title
				"y", // x axis label
				"G(y)", // y axis label
				seriesCollection, // data
				PlotOrientation.VERTICAL, false, // include legend
				true, // tooltips
				false // urls
				);

		XYPlot plot = (XYPlot) chart.getPlot();
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

		// "0" is the line plot
		renderer.setSeriesLinesVisible(0, true);
		renderer.setSeriesShapesVisible(0, false);
		// "1" and "2" is the scatter plot
		renderer.setSeriesLinesVisible(1, false);
		renderer.setSeriesShapesVisible(1, true);
		renderer.setSeriesLinesVisible(2, false);
		renderer.setSeriesShapesVisible(2, true);
		Shape circle = new Ellipse2D.Double(0, 0, 5, 5); // circle
		renderer.setSeriesShape(1, circle);
		renderer.setSeriesShape(2, circle);
		
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(0); 
		XYItemLabelGenerator generator1 = new StandardXYItemLabelGenerator("S ({1}, {2})", format, format); 
		XYItemLabelGenerator generator2 = new StandardXYItemLabelGenerator("s ({1}, {2})", format, format); 
		//XYItemLabelGenerator generator3 = new StandardXYItemLabelGenerator("({1}, {2})", format, format); // coordinates
		renderer.setSeriesItemLabelGenerator(1, generator1);
		renderer.setSeriesItemLabelGenerator(2, generator2);
		
		renderer.setBaseItemLabelsVisible(true);

		plot.setRenderer(renderer);
		ChartFrame frame = new ChartFrame("chen zhen's picture", chart);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * 
	 * drawing a picture for C() and different B with fixed x
	 */
	public void drawBC(double[][] BC) {
		XYSeries seriesG = new XYSeries("BCSeries");
		int N = BC.length;
		for (int i = 0; i < N; i++) {
			seriesG.add(BC[i][0], BC[i][1]);
		}

		XYSeriesCollection seriesCollection = new XYSeriesCollection();
		seriesCollection.addSeries(seriesG);

		JFreeChart chart = ChartFactory.createXYLineChart("C() with different ini cash B", // chart title
				"B", // x axis label
				"C()", // y axis label
				seriesCollection, // data
				PlotOrientation.VERTICAL, false, // include legend
				true, // tooltips
				false // urls
				);

		ChartFrame frame = new ChartFrame("chen zhen's picture", chart);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	/**
	 * 
	 * drawing a picture for Q() and different B with fixed x
	 */
	public void drawBQ(double[][] BQ) {
		XYSeries seriesG = new XYSeries("BQSeries");
		int N = BQ.length;
		for (int i = 0; i < N; i++) {
			seriesG.add(BQ[i][0], BQ[i][1]);
		}

		XYSeriesCollection seriesCollection = new XYSeriesCollection();
		seriesCollection.addSeries(seriesG);

		JFreeChart chart = ChartFactory.createXYLineChart("Q with different ini cash B", // chart title
				"B", // x axis label
				"Q", // y axis label
				seriesCollection, // data
				PlotOrientation.VERTICAL, false, // include legend
				true, // tooltips
				false // urls
				);

		ChartFrame frame = new ChartFrame("chen zhen's picture", chart);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}