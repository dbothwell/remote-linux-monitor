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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

public class GraphPanel extends JPanel {
	
	public static final int TYPE_PERCENTAGE = 0;
	public static final int TYPE_NETWORK = 1;

	private GridPanel gridPanel;
	private JLabel label10Sec;
	private JLabel label20Sec;
	private JLabel label30Sec;
	private JLabel label40Sec;
	private JLabel label50Sec;
	private JLabel label60Sec;
	private JLabel labelTitle;
	private JLabel labelYAxis0;
	private JLabel labelYAxis100;
	private JLabel labelYAxis50;
	private LegendPanel legendPanel;
	private JPanel panelGraph;
	private JPanel panelHeader;
	private JPanel panelXAxis;
	private JPanel panelYAxis;

	private YPoint yPoint = null;
	
	private GridBagConstraints gbc_scrollPane;
	private GridBagConstraints gbc_panelYAxis;
	private GridBagConstraints gbc_panelXAxis;
	private GridBagConstraints gbc_panelHeader;
	private GridBagConstraints gbc_panelGraph;
	
	private int type = TYPE_PERCENTAGE;

	public GraphPanel(YPoint yPoint, int type) {

		this.yPoint = yPoint;
		this.setType(type);

		initComponents();
	}

	private void initComponents() {

		GridBagConstraints gridBagConstraints;

		legendPanel = new LegendPanel(this);

		panelHeader = new JPanel();
		labelTitle = new JLabel();
		panelGraph = new JPanel();
		panelYAxis = new JPanel();
		labelYAxis100 = new JLabel();
		labelYAxis50 = new JLabel();
		labelYAxis0 = new JLabel();
		gridPanel = new GridPanel(legendPanel, this);
		panelXAxis = new JPanel();
		label60Sec = new JLabel();
		label50Sec = new JLabel();
		label40Sec = new JLabel();
		label30Sec = new JLabel();
		label20Sec = new JLabel();
		label10Sec = new JLabel();

		setLayout(new GridBagLayout());

		panelHeader.setLayout(new GridLayout(1, 1));

		labelTitle.setFont(new Font(RemoteMain.DEFAULT_FONT, Font.BOLD, 13));
		labelTitle.setText(yPoint.getTitle());
		panelHeader.add(labelTitle);

		gbc_panelHeader = new GridBagConstraints();
		gbc_panelHeader.gridx = 0;
		gbc_panelHeader.gridy = 0;
		gbc_panelHeader.fill = GridBagConstraints.BOTH;
		gbc_panelHeader.insets = new Insets(5, 10, 0, 10);
		add(panelHeader, gbc_panelHeader);

		panelGraph.setLayout(new GridBagLayout());

		panelYAxis.setMaximumSize(new Dimension(50, 0));
		panelYAxis.setMinimumSize(new Dimension(50, 0));
		panelYAxis.setPreferredSize(new Dimension(50, 0));
		panelYAxis.setLayout(new GridLayout(3, 1));

		labelYAxis100.setHorizontalAlignment(SwingConstants.TRAILING);
		labelYAxis100.setVerticalAlignment(SwingConstants.TOP);
		labelYAxis100.setHorizontalTextPosition(SwingConstants.RIGHT);
		labelYAxis100.setVerticalTextPosition(SwingConstants.BOTTOM);
		panelYAxis.add(labelYAxis100);

		labelYAxis50.setHorizontalAlignment(SwingConstants.TRAILING);
		labelYAxis50.setHorizontalTextPosition(SwingConstants.RIGHT);
		labelYAxis50.setMaximumSize(new Dimension(25, 13));
		labelYAxis50.setMinimumSize(new Dimension(25, 13));
		labelYAxis50.setPreferredSize(new Dimension(16, 13));
		labelYAxis50.setVerticalTextPosition(SwingConstants.TOP);
		panelYAxis.add(labelYAxis50);

		labelYAxis0.setHorizontalAlignment(SwingConstants.TRAILING);
		labelYAxis0.setVerticalAlignment(SwingConstants.BOTTOM);
		labelYAxis0.setHorizontalTextPosition(SwingConstants.RIGHT);
		panelYAxis.add(labelYAxis0);
		
		
		switch (type) {
		
			case GraphPanel.TYPE_PERCENTAGE:
				
				labelYAxis100.setFont(new Font(RemoteMain.DEFAULT_FONT, Font.PLAIN, 10));
				labelYAxis100.setText("100%");
				labelYAxis50.setFont(new Font(RemoteMain.DEFAULT_FONT, Font.PLAIN, 10));
				labelYAxis50.setText("50%");
				labelYAxis0.setFont(new Font(RemoteMain.DEFAULT_FONT, Font.PLAIN, 10));
				labelYAxis0.setHorizontalAlignment(SwingConstants.TRAILING);
				labelYAxis0.setText("0%");
				break;
	
			case GraphPanel.TYPE_NETWORK:
				
				labelYAxis100.setFont(new Font(RemoteMain.DEFAULT_FONT, Font.PLAIN, 8));
				labelYAxis100.setText("2.0 KB/s");
				labelYAxis50.setFont(new Font(RemoteMain.DEFAULT_FONT, Font.PLAIN, 8));
				labelYAxis50.setText("1.0 KB/s");
				labelYAxis0.setFont(new Font(RemoteMain.DEFAULT_FONT, Font.PLAIN, 8));
				labelYAxis0.setHorizontalAlignment(SwingConstants.TRAILING);
				labelYAxis0.setText("0.0 KB/s");
				break;
		}

		gbc_panelYAxis = new GridBagConstraints();
		gbc_panelYAxis.gridx = 0;
		gbc_panelYAxis.gridy = 0;
		gbc_panelYAxis.fill = GridBagConstraints.VERTICAL;
		gbc_panelYAxis.insets = new Insets(0, 0, 0, 2);
		panelGraph.add(panelYAxis, gbc_panelYAxis);

		gridPanel.setBackground(Color.white);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 10.0;
		gridBagConstraints.weighty = 10.0;
		panelGraph.add(gridPanel, gridBagConstraints);

		gbc_panelGraph = new GridBagConstraints();
		gbc_panelGraph.gridx = 0;
		gbc_panelGraph.gridy = 1;
		gbc_panelGraph.fill = GridBagConstraints.BOTH;
		gbc_panelGraph.anchor = GridBagConstraints.WEST;
		gbc_panelGraph.weightx = 10.0;
		gbc_panelGraph.weighty = 10.0;
		gbc_panelGraph.insets = new Insets(5, 10, 0, 10);
		add(panelGraph, gbc_panelGraph);

		panelXAxis.setMinimumSize(new Dimension(0, 20));
		panelXAxis.setPreferredSize(new Dimension(0, 20));
		panelXAxis.setLayout(new GridLayout(1, 6));

		label60Sec.setFont(new Font(RemoteMain.DEFAULT_FONT, Font.PLAIN, 10));
		label60Sec.setHorizontalAlignment(SwingConstants.LEFT);
		label60Sec.setText("60 Seconds");
		panelXAxis.add(label60Sec);

		label50Sec.setFont(new Font(RemoteMain.DEFAULT_FONT, Font.PLAIN, 10));
		label50Sec.setHorizontalAlignment(SwingConstants.LEFT);
		label50Sec.setText("50");
		panelXAxis.add(label50Sec);

		label40Sec.setFont(new Font(RemoteMain.DEFAULT_FONT, Font.PLAIN, 10));
		label40Sec.setHorizontalAlignment(SwingConstants.LEFT);
		label40Sec.setText("40");
		panelXAxis.add(label40Sec);

		label30Sec.setFont(new Font(RemoteMain.DEFAULT_FONT, Font.PLAIN, 10));
		label30Sec.setHorizontalAlignment(SwingConstants.LEFT);
		label30Sec.setText("30");
		panelXAxis.add(label30Sec);

		label20Sec.setFont(new Font(RemoteMain.DEFAULT_FONT, Font.PLAIN, 10));
		label20Sec.setHorizontalAlignment(SwingConstants.LEFT);
		label20Sec.setText("20");
		panelXAxis.add(label20Sec);

		label10Sec.setFont(new Font(RemoteMain.DEFAULT_FONT, Font.PLAIN, 10));
		label10Sec.setHorizontalAlignment(SwingConstants.LEFT);
		label10Sec.setText("10");
		panelXAxis.add(label10Sec);

		gbc_panelXAxis = new GridBagConstraints();
		gbc_panelXAxis.gridx = 0;
		gbc_panelXAxis.gridy = 2;
		gbc_panelXAxis.fill = GridBagConstraints.BOTH;
		gbc_panelXAxis.anchor = GridBagConstraints.WEST;
		gbc_panelXAxis.insets = new Insets(2, 55, 0, 10);
		add(panelXAxis, gbc_panelXAxis);

		gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.weighty = 2.75;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 3;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.anchor = GridBagConstraints.WEST;
		gbc_scrollPane.insets = new Insets(0, 10, 0, 5);

		JScrollPane scrollPane = new JScrollPane(legendPanel);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setOpaque(false);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null);
		scrollPane.setViewportBorder(null);

		add(scrollPane, gbc_scrollPane);

		getAccessibleContext().setAccessibleName("GraphPanel");
	}


	public void plotIt(ArrayList<YPoint> yPoints) {

		gridPanel.setyPoints(yPoints);
	}

	protected void setColorAtIndex(int index, Color color) {

		yPoint.setColorAtIndex(index, color);
	}
	
	public void setYAxisLabels(String bottom, String middle, String top) {
		
		labelYAxis100.setText(top);
		labelYAxis50.setText(middle);
		labelYAxis0.setText(bottom);
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
}

