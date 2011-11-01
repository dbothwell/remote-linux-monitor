/**
* Copyright (C) 2011 David Bothwell
* All rights reserved.
* 
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions
* are met:
* 
* 1. Redistributions of source code must retain the above copyright
*    notice, this list of conditions and the following disclaimer.
*    
* 2. Redistributions in binary form must reproduce the above copyright
*    notice, this list of conditions and the following disclaimer in the
*    documentation and/or other materials provided with the distribution.
*    
* 3. Neither the name of the project nor the names of its contributors
*    may be used to endorse or promote products derived from this software
*    without specific prior written permission.
* 
* THIS SOFTWARE IS PROVIDED BY THE PROJECT AND CONTRIBUTORS ``AS IS'' AND
* ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
* ARE DISCLAIMED.  IN NO EVENT SHALL THE PROJECT OR CONTRIBUTORS BE LIABLE
* FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
* DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
* OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
* HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
* LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
* OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
* SUCH DAMAGE.
*/

package db.remoteLinuxMonitor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GridPanel extends JPanel {
	
	public static final int POINTS_PER_MINUTE = (int) (60);
	public static final double Y_MAX_MINIMUM = 2000;

	private GraphPanel graphPanel;
	private LegendPanel legendPanel;
	private ArrayList<YPoint> yPoints;
	
	protected static int QUARTER_LINE_START_SIZE = 65;
	protected static int VERTICAL_LINE_OFFSET = 3;
	

	protected RenderingHints renderHints =
		  new RenderingHints(RenderingHints.KEY_ANTIALIASING,
		                     RenderingHints.VALUE_ANTIALIAS_ON);

	/**
	 * Constructor
	 */
	public GridPanel(LegendPanel legendPanel, GraphPanel graphPanel) {
		super();
		setLayout(new GridBagLayout());
		
		renderHints.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
		
		this.graphPanel = graphPanel;
		this.legendPanel = legendPanel;

	}
	
	/**
	 * @param yPoints the yPoints to set
	 */
	public void setyPoints(ArrayList<YPoint> yPoints) {
		
		this.yPoints = yPoints;
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);	
		
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHints(renderHints);
		
		drawGrid(g2);
		drawGraph(g2);

	}
	
	private void drawGrid(Graphics2D g2) {

		GeneralPath path = null;
		
		g2.setStroke(new BasicStroke(1.0f));
		g2.setPaint(new Color(230,230,230));

		// Draw Border
		Rectangle2D border = new Rectangle2D.Double (0, 0, getWidth() - 1, getHeight() - 1);
		g2.draw(border);

		// 50% horizontal
		path = new GeneralPath();
		path.moveTo(0, (int) ((getHeight() - 1) / 2));
		path.lineTo(getWidth() , (int) ((getHeight() - 1) / 2));
		g2.draw(path);  

		// Draw 1/4 lines if canvas height is larger then QUARTER_LINE_START_SIZE
		if (getHeight() >= QUARTER_LINE_START_SIZE) {

			// 25% horizontal
			path = new GeneralPath();
			path.moveTo(0, (int) ((getHeight() - 1) / 4));
			path.lineTo(getWidth() , (int) ((getHeight() - 1) / 4));
			g2.draw(path); 

			// 75% horizontal
			path = new GeneralPath();
			path.moveTo(0, (int) (((getHeight() - 1) * 3)/ 4));
			path.lineTo(getWidth() , (int) (((getHeight() - 1) * 3)/ 4));
			g2.draw(path); 
		}
		
		// Draw seconds lines vertical
		for (int i = 5; i > 0; i--) {
		
			path = new GeneralPath();
			path.moveTo((int) ((getWidth() / 6) * i) + VERTICAL_LINE_OFFSET, 0);
			path.lineTo((int) ((getWidth() / 6) * i) + VERTICAL_LINE_OFFSET , getHeight());
			g2.draw(path);
		}
	}

//	private void drawGraph(Graphics2D g2) {
//		
//		g2.setStroke(new BasicStroke(1.2f));
//
//		if ((yPoints != null) && (yPoints.size() > 1)) {
//			
//			GeneralPath path = new GeneralPath();
//		
//			double xFactor = ((float) getWidth() / (POINTS_PER_MINUTE));
//			
//			double xPosCurrent =  0;
//			double yPosCurrent = 0;
//			
//			double xPosPrevious = 0;
//			double yPosPrevious = 0;
//			
//			double xBezierPos1 = 0;
//			double yBezierPos1 = 0;				
//			
//			double xBezierPos2 = 0;
//			double yBezierPos2 = 0;
//
//			
//			for (int i = yPoints.size() - 1; i >= 0; i--) {
//				
//				legendPanel.setYPoint(yPoints.get(i));
//			
//				xPosPrevious =  (int) (getWidth() - (xFactor * (i + 1)));
//				xPosCurrent = (int) (getWidth() - (xFactor * i));
//				
//				ArrayList<Vertex> current = yPoints.get(i).getVertices();
//				ArrayList<Vertex> previous = null;
//				
//				if (i > 0) {
//					previous = yPoints.get(i -1).getVertices();
//				}
//
//				for (int j =0; j < current.size(); j++) {
//					
//					yPosPrevious = (getHeight() - ((current.get(j).getYPosition() / 100 * getHeight())));
//					
//					yPosCurrent = yPosPrevious;
//					
//					if (i > 0) {
//						yPosCurrent = (int) (getHeight() - ((previous.get(j).getYPosition() / 100 * getHeight())));
//					}
//					
//					g2.setPaint(yPoints.get(i).getColorAtIndex(j));
//					path = new GeneralPath();
//					
//					xBezierPos1 = xPosCurrent - (xFactor / 2);
//					yBezierPos1 = yPosPrevious;
//					
//					xBezierPos2 = xPosCurrent - (xFactor / 2);
//					yBezierPos2 = yPosCurrent;
//
//					path.moveTo(xPosPrevious, yPosPrevious);
//					path.curveTo(xBezierPos1, yBezierPos1, xBezierPos2, yBezierPos2, xPosCurrent, yPosCurrent);
//					g2.draw(path); 
//					
////					// Straight lines
////					path.moveTo(xPosPrevious, yPosPrevious);
////					path.lineTo(xPosCurrent, yPosCurrent);
////					g2.draw(path); 
//				}
//			}
//		}
//	}
	
	private void drawGraph(Graphics2D g2) {
		
		g2.setStroke(new BasicStroke(1.2f));

		if ((yPoints != null) && (yPoints.size() > 1)) {
			
			double yMax = Y_MAX_MINIMUM;
			
			if (graphPanel.getType() == GraphPanel.TYPE_NETWORK) {
				
				yMax = getYMax(yPoints);
				setYAxisLables(yMax);
			}
			
			
			GeneralPath path = new GeneralPath();
		
			double xFactor = ((float) getWidth() / (POINTS_PER_MINUTE));
			
			double xPosCurrent =  0;
			double yPosCurrent = 0;
			
			double xPosPrevious = 0;
			double yPosPrevious = 0;
			
			double xBezierPos1 = 0;
			double yBezierPos1 = 0;				
			
			double xBezierPos2 = 0;
			double yBezierPos2 = 0;

			
			for (int i = yPoints.size() - 1; i >= 0; i--) {
				
				legendPanel.setYPoint(yPoints.get(i));
			
				xPosPrevious =  (int) (getWidth() - (xFactor * (i + 1)));
				xPosCurrent = (int) (getWidth() - (xFactor * i));
				
				ArrayList<Vertex> current = yPoints.get(i).getVertices();
				ArrayList<Vertex> previous = null;
				
				if (i > 0) {
					previous = yPoints.get(i -1).getVertices();
				}

				for (int j =0; j < current.size(); j++) {

					switch (graphPanel.getType()) {
					
						case GraphPanel.TYPE_PERCENTAGE:
					
							yPosPrevious = (getHeight() - ((current.get(j).getYPosition() / 100 * getHeight())));
							yPosCurrent = yPosPrevious;
							
							if (i > 0) {
								
								yPosCurrent = (int) (getHeight() - ((previous.get(j).getYPosition() / 100 * getHeight())));
							}							
							break;

					
						case GraphPanel.TYPE_NETWORK:

							yPosPrevious = (int) (getHeight() - ((current.get(j).getYPosition() / yMax) * getHeight()));
							yPosCurrent = yPosPrevious;
							
							if (i > 0) {
								
								yPosCurrent = (int) (getHeight() - ((previous.get(j).getYPosition() / yMax) * getHeight()));
							}
							break;
					}
					
					g2.setPaint(yPoints.get(i).getColorAtIndex(j));
					path = new GeneralPath();
					
					xBezierPos1 = xPosCurrent - (xFactor / 2);
					yBezierPos1 = yPosPrevious;
					
					xBezierPos2 = xPosCurrent - (xFactor / 2);
					yBezierPos2 = yPosCurrent;

					path.moveTo(xPosPrevious, yPosPrevious);
					path.curveTo(xBezierPos1, yBezierPos1, xBezierPos2, yBezierPos2, xPosCurrent, yPosCurrent);
					g2.draw(path); 
					
//					// Straight lines
//					path.moveTo(xPosPrevious, yPosPrevious);
//					path.lineTo(xPosCurrent, yPosCurrent);
//					g2.draw(path); 
				}
			}
		}
	}
	
	private double getYMax(ArrayList<YPoint> yPoints) {
		
		double yMax = Y_MAX_MINIMUM;
		
		for (YPoint yPoint: yPoints) {
			
			for (Vertex vertex: yPoint.getVertices()) {
				
				if (vertex.getYPosition() >= yMax) {

					for (long i = 1; i <= 1000000000000L; i *= 10) {
						
						if (vertex.getYPosition() < Y_MAX_MINIMUM ) {
							break;
						}
						
						if (vertex.getYPosition() <= i) {
							
							yMax = (Math.floor(vertex.getYPosition() / (i / 10))) * (i / 10) + (i / 10);
							break;
						}
					}
				}
			}
		}
		
		return yMax;
	}

	private void setYAxisLables(double yMax) {
		
		String top;
		String middle;
		String bottom;
		
		if (yMax > 1000000000) {
			
			top = String.format("%1.1f GB/s", (double) yMax/1000000000);
			middle = String.format("%1.1f GB/s", (double) (yMax / 2)/1000000000);
			bottom = "0.0 GB/s";
		
		} else if (yMax > 1000000) {

			top = String.format("%1.1f MB/s", (double) yMax/1000000);
			middle = String.format("%1.1f MB/s", (double) (yMax / 2)/1000000);
			bottom = "0.0 MB/s";
		
		} else {
		
			top = String.format("%1.1f KB/s", (double) yMax/1000);
			middle = String.format("%1.1f KB/s", (double) (yMax / 2)/1000);
			bottom = "0.0 KB/s";
		}
		
		graphPanel.setYAxisLabels(bottom, middle, top);
	}
	
}
