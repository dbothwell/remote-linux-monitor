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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class PanelSystem extends JPanel {
	
	private JLabel labelComputerName;
	private JLabel labelKernel;
	private JLabel labelAvailableDiskSpace;
	private JLabel labelSystemMemory;
	private JTextArea textAreaSystemProcessors;
	
	public PanelSystem() {
		
		setLayout(new BorderLayout(0, 0));
		
		JPanel panelImage = new JPanel();
		panelImage.setMinimumSize(new Dimension(150, 360));
		add(panelImage, BorderLayout.WEST);
		panelImage.setLayout(new BorderLayout(20, 10));
		
		JLabel labelSystemSideBar = new JLabel("");
		labelSystemSideBar.setBorder(new EmptyBorder(20, 5, 0, 0));
		labelSystemSideBar.setAlignmentX(0.5f);
		labelSystemSideBar.setVerticalAlignment(SwingConstants.TOP);
		labelSystemSideBar.setIcon(new ImageIcon(getClass().getResource("/images/linux_system_sidebar.png")));
		panelImage.add(labelSystemSideBar, BorderLayout.CENTER);
		
		JPanel panelInfo = new JPanel();
		panelInfo.setOpaque(false);
		add(panelInfo, BorderLayout.CENTER);
		panelInfo.setLayout(new BorderLayout(0, 0));
		
		JPanel panelNorth = new JPanel();
		panelNorth.setPreferredSize(new Dimension(10, 275));
		panelInfo.add(panelNorth, BorderLayout.NORTH);
		panelNorth.setLayout(null);
		
		labelComputerName = new JLabel();
		labelComputerName.setFont(new Font(RemoteMain.DEFAULT_FONT, Font.BOLD, 18));
		labelComputerName.setBounds(30, 20, 380, 27);
		panelNorth.add(labelComputerName);
		
		JLabel labelOS = new JLabel("Kernel");
		labelOS.setFont(new Font(RemoteMain.DEFAULT_FONT, Font.BOLD, 16));
		labelOS.setBounds(30, 58, 380, 27);
		panelNorth.add(labelOS);
		
		labelKernel = new JLabel();
		labelKernel.setFont(new Font(RemoteMain.DEFAULT_FONT, Font.PLAIN, 12));
		labelKernel.setBounds(60, 95, 347, 15);
		panelNorth.add(labelKernel);
		
		JLabel labelSystemStatus = new JLabel("System Status");
		labelSystemStatus.setFont(new Font(RemoteMain.DEFAULT_FONT, Font.BOLD, 16));
		labelSystemStatus.setBounds(30, 129, 380, 27);
		panelNorth.add(labelSystemStatus);
		
		labelAvailableDiskSpace = new JLabel();
		labelAvailableDiskSpace.setFont(new Font(RemoteMain.DEFAULT_FONT, Font.PLAIN, 12));
		labelAvailableDiskSpace.setBounds(60, 168, 347, 15);
		panelNorth.add(labelAvailableDiskSpace);
		
		JLabel labelHardware = new JLabel("Hardware");
		labelHardware.setFont(new Font(RemoteMain.DEFAULT_FONT, Font.BOLD, 16));
		labelHardware.setBounds(30, 206, 380, 27);
		panelNorth.add(labelHardware);
		
		labelSystemMemory = new JLabel();
		labelSystemMemory.setFont(new Font(RemoteMain.DEFAULT_FONT, Font.PLAIN, 12));
		labelSystemMemory.setBounds(60, 245, 347, 15);
		panelNorth.add(labelSystemMemory);
		
		JPanel panelCenter = new JPanel();
		panelInfo.add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new EmptyBorder(0, 60, 10, 10));
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setAutoscrolls(true);
		scrollPane.setViewportBorder(null);
		panelCenter.add(scrollPane);
		
		textAreaSystemProcessors = new JTextArea();
		textAreaSystemProcessors.setOpaque(false);
		textAreaSystemProcessors.setFocusable(false);
		textAreaSystemProcessors.setBorder(BorderFactory.createEmptyBorder());
		textAreaSystemProcessors.setEditable(false);
		textAreaSystemProcessors.setBackground(new Color(0,0,0,0));
		
		scrollPane.setViewportView(textAreaSystemProcessors);
	}
	
	public void display(SystemInfo systemInfo) {
		
		labelComputerName.setText(systemInfo.getComputerName());
		labelKernel.setText(systemInfo.getKernelName());
		labelAvailableDiskSpace.setText(systemInfo.getDiskSpace());
		labelSystemMemory.setText(systemInfo.getAvailableMemory());
		
		textAreaSystemProcessors.setText(null);
		
		ArrayList<String> processors = systemInfo.getProcessors();
		for(int i = 0; i < processors.size(); i++) {
			
			if (i == 0) {
				textAreaSystemProcessors.setText(processors.get(i));
				
			} else {
				textAreaSystemProcessors.append(processors.get(i));
			}
			
			if (i < (processors.size() - 1)) {
				textAreaSystemProcessors.append("\n\n");
			}
		}
		textAreaSystemProcessors.setCaretPosition(0);
	}

}
