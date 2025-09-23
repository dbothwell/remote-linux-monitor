/**
* Copyright (C) 2025 David Bothwell
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
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class LogOptionsDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	private RemoteMain remoteMain;
	private int result = JOptionPane.CANCEL_OPTION;
	private LogOptions logOptions;
	
	private JCheckBox checkboxLogServerStats;
	private JTextField textFieldLogFolder;
	private JCheckBox checkboxSummarizeStats;
	private JButton btnFileChooser;
	
	/**
	 * Create the dialog.
	 */
	public LogOptionsDialog(RemoteMain remoteMain, LogOptions logOptions) {
		
		super(remoteMain);
		
		try {
			this.remoteMain = remoteMain;
			this.logOptions = logOptions;
			
			setTitle("Logging Options");
			setBounds(100, 100, 498, 195);
			
			setModalityType(ModalityType.APPLICATION_MODAL);
			setModal(true);
			
//			setResizable(false);
			
			setLocationRelativeTo(remoteMain);
			
			getContentPane().setLayout(new BorderLayout());
			contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			getContentPane().add(contentPanel, BorderLayout.CENTER);
			
			GridBagLayout gbl_contentPanel = new GridBagLayout();
			gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0};
			gbl_contentPanel.rowHeights = new int[]{0, 0, 0};
			gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
			gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			contentPanel.setLayout(gbl_contentPanel);
			{
				checkboxLogServerStats = new JCheckBox("Log resource statistics");
				GridBagConstraints gbc_checkboxLogServerStats = new GridBagConstraints();
				gbc_checkboxLogServerStats.insets = new Insets(20, 10, 7, 5);
				gbc_checkboxLogServerStats.anchor = GridBagConstraints.WEST;
				gbc_checkboxLogServerStats.gridx = 0;
				gbc_checkboxLogServerStats.gridy = 0;
				gbc_checkboxLogServerStats.gridwidth = 2;
				contentPanel.add(checkboxLogServerStats, gbc_checkboxLogServerStats);
				
				checkboxLogServerStats.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						LogOptionsDialog.this.enableLogs(e.getStateChange() == ItemEvent.SELECTED);
						showStatisticsTab();
					}
				});
			}
			{
				JLabel labelIdentityFolder = new JLabel("Log Folder:");
				GridBagConstraints gbc_labelIdentityFolder = new GridBagConstraints();
				gbc_labelIdentityFolder.anchor = GridBagConstraints.WEST;
				gbc_labelIdentityFolder.insets = new Insets(5, 40, 0, 5);
				gbc_labelIdentityFolder.gridx = 0;
				gbc_labelIdentityFolder.gridy = 1;
				contentPanel.add(labelIdentityFolder, gbc_labelIdentityFolder);
			}
			{
				textFieldLogFolder = new JTextField(logOptions.getLogFolder());
				GridBagConstraints gbc_textFieldIdentityFolder = new GridBagConstraints();
				gbc_textFieldIdentityFolder.insets = new Insets(5, 0, 0, 0);
				gbc_textFieldIdentityFolder.fill = GridBagConstraints.HORIZONTAL;
				gbc_textFieldIdentityFolder.gridx = 1;
				gbc_textFieldIdentityFolder.gridy = 1;
				contentPanel.add(textFieldLogFolder, gbc_textFieldIdentityFolder);
				textFieldLogFolder.setColumns(10);
			}
			{
				btnFileChooser = new JButton("...");
				btnFileChooser.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JFileChooser fc = new JFileChooser(textFieldLogFolder.getText());
						fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						int returnVal = fc.showOpenDialog(LogOptionsDialog.this);
				        if (returnVal == JFileChooser.APPROVE_OPTION) {
				            File file = fc.getSelectedFile();
				            textFieldLogFolder.setText(file.getAbsolutePath());
				        }
					}
				});
				GridBagConstraints gbc_btnFileChooser = new GridBagConstraints();
				gbc_btnFileChooser.insets = new Insets(5, 0, 0, 10);
				gbc_btnFileChooser.gridx = 2;
				gbc_btnFileChooser.gridy = 1;
				contentPanel.add(btnFileChooser, gbc_btnFileChooser);
			}
			{
				checkboxSummarizeStats = new JCheckBox("Display summary on disconnect");
				GridBagConstraints gbc_checkboxSummerizeStats = new GridBagConstraints();
				gbc_checkboxSummerizeStats.insets = new Insets(1, 40, 5, 5);
				gbc_checkboxSummerizeStats.anchor = GridBagConstraints.WEST;
				gbc_checkboxSummerizeStats.gridx = 0;
				gbc_checkboxSummerizeStats.gridy = 2;
				gbc_checkboxSummerizeStats.gridwidth = 2;
				contentPanel.add(checkboxSummarizeStats, gbc_checkboxSummerizeStats);
				
				checkboxSummarizeStats.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						showStatisticsTab();
					}
				});
			}
			{
				JPanel buttonPane = new JPanel();
				buttonPane.setBorder(new MatteBorder(1, 0, 0, 0, (Color) Color.LIGHT_GRAY));
				buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
				getContentPane().add(buttonPane, BorderLayout.SOUTH);
				{
					JButton okButton = new JButton("OK");
					okButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							
							try {
								LogOptionsDialog.this.logOptions.setLogServerStats(checkboxLogServerStats.isSelected());
								LogOptionsDialog.this.logOptions.setSummarizeStats(checkboxSummarizeStats.isSelected());
								LogOptionsDialog.this.logOptions.setLogFolder(textFieldLogFolder.getText());
								LogOptionsDialog.this.remoteMain.setLogOptions(LogOptionsDialog.this.logOptions);
								result = JOptionPane.OK_OPTION;
								LogOptionsDialog.this.dispose();
								
							} catch (NumberFormatException e1) {
								e1.printStackTrace();
							}
						}
					});
					okButton.setActionCommand("OK");
					buttonPane.add(okButton);
					getRootPane().setDefaultButton(okButton);
				}
				{
					JButton cancelButton = new JButton("Cancel");
					cancelButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
						
							LogOptionsDialog.this.dispose();
						}
					});
					cancelButton.setActionCommand("Cancel");
					buttonPane.add(cancelButton);
				}
			}
			setFieldValues();
			enableLogs(logOptions.isLogServerStats());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int showDialog() {

	    setVisible(true);
	    return result;
	}

	private void enableLogs(boolean enable) {
		
		textFieldLogFolder.setEnabled(enable);
		checkboxSummarizeStats.setEnabled(enable);
		btnFileChooser.setEnabled(enable);
	}
	
	private void setFieldValues() throws Exception {
		try {
			checkboxLogServerStats.setSelected(logOptions.isLogServerStats());
			checkboxSummarizeStats.setSelected(logOptions.isSummarizeStats());
			
			String pathName = logOptions.getLogFolder();
			if (pathName == null || pathName.isBlank()) {
				pathName = (new File(System.getProperty("user.home"))).getCanonicalPath().toString();
			}
			textFieldLogFolder.setText(pathName);
			
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	private void showStatisticsTab() {
		
		if (checkboxLogServerStats.isSelected() && checkboxSummarizeStats.isSelected()) {
			remoteMain.showStatisticsTab(true);
		} else {
			remoteMain.showStatisticsTab(false);
		}
	}
}
