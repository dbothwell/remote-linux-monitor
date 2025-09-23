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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PanelStatistics extends JPanel {
	
	private JLabel labelTitle;
	private JLabel labelLogFileName;
	private JButton btnClipboard;
	private JTextArea textAreaStats;

	public PanelStatistics() {
		
		setLayout(new BorderLayout(0, 0));
		
		JPanel panelNorth = new JPanel(new BorderLayout(0, 0));
		
		JPanel panelNorthWest = new JPanel(new FlowLayout(FlowLayout.LEFT, 2,10));
		labelTitle = new JLabel("  Summary for log file: ");
		labelTitle.setFont(new Font(RemoteMain.DEFAULT_FONT, Font.BOLD, 13));
		panelNorthWest.add(labelTitle);
		
		labelLogFileName = new JLabel();
		labelLogFileName.setFont(new Font(RemoteMain.DEFAULT_FONT, Font.PLAIN, 11));
		panelNorthWest.add(labelLogFileName);
		panelNorth.add(panelNorthWest, BorderLayout.WEST);
		
		JPanel panelNorthEast = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10,2));
		btnClipboard = new JButton(new ImageIcon(getClass().getResource("/images/clipboard-16x20.png")));
		btnClipboard.setToolTipText("Copy to clipboard");
		btnClipboard.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				copyToClipboard();
			} 
		} );

		panelNorthEast.add(btnClipboard);
		panelNorth.add(panelNorthEast, BorderLayout.EAST);
		
		add(panelNorth, BorderLayout.NORTH);
		
		JPanel panelCenter = new JPanel(new BorderLayout(0, 0));
		panelCenter.setBorder(BorderFactory.createEmptyBorder(2, 10, 10, 10));
		
		textAreaStats = new JTextArea();
		textAreaStats.setEditable(false);
		textAreaStats.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
		panelCenter.add(new JScrollPane(textAreaStats),BorderLayout.CENTER);
		
		add(panelCenter);	
	}
	
	protected void setLogFileName(String logFileName) {
		labelLogFileName.setText("\"" + logFileName + "\"");
	}
	
	protected void setText(String text) {
		textAreaStats.setText(text);
		textAreaStats.setCaretPosition(0);
	}
	
	protected void clear() {
		labelLogFileName.setText("");
		textAreaStats.setText("");
	}
	
	private void copyToClipboard() {
		
		String text = textAreaStats.getSelectedText();
		if (text == null) {
			text = textAreaStats.getText();
		}

		StringSelection stringSelection = new StringSelection(text);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}
}
