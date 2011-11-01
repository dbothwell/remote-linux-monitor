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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PreferenceDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	
	private JSpinner spinnerProcess;
	private JCheckBox checkboxAlertBeforeEnding;
	private CheckboxList listProcesses;
	
	private JSpinner spinnerResource;
	private JCheckBox checkboxShowCpuHistory;
	private JCheckBox checkboxShowAverageAll;
	private JCheckBox checkboxShowMemoryHistory;
	private JCheckBox checkboxShowNetworkHistory;
	
	private JSpinner spinnerFileSystem;
	private JCheckBox checkboxShowAllFilesystems;
	private CheckboxList listFileSystem;
	
	private RemoteMain owner;
	private Preference preference;
	
	/**
	 * Create the dialog.
	 */
	public PreferenceDialog(Frame owner, Preference preference) {
		
		super(owner);
		
		this.owner = (RemoteMain) owner;
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Preferences");
		setBounds(100, 100, 383, 460);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			contentPanel.add(tabbedPane);
			{
				JPanel panelProcesses = new JPanel();
				panelProcesses.setOpaque(false);
				tabbedPane.addTab("Processes", null, panelProcesses, null);
				panelProcesses.setLayout(new BorderLayout(0, 0));
				{
					JPanel panelProcessNorth = new JPanel();
					panelProcessNorth.setPreferredSize(new Dimension(10, 135));
					panelProcesses.add(panelProcessNorth, BorderLayout.NORTH);
					panelProcessNorth.setLayout(null);
					
					JLabel labelProcessBehavior = new JLabel("Behavior");
					labelProcessBehavior.setFont(new Font("Dialog", Font.BOLD, 15));
					labelProcessBehavior.setBounds(12, 12, 86, 17);
					panelProcessNorth.add(labelProcessBehavior);
					
					JLabel labelProcessInterval = new JLabel("Update Interval in Seconds:");
					labelProcessInterval.setBounds(20, 41, 202, 17);
					panelProcessNorth.add(labelProcessInterval);
					
					spinnerProcess = new JSpinner();
					spinnerProcess.setModel(new SpinnerNumberModel(1, 1, 120, 1));
					spinnerProcess.setBounds(220, 40, 71, 20);
					((JSpinner.DefaultEditor) spinnerProcess.getEditor()).getTextField().setEditable(false);
					panelProcessNorth.add(spinnerProcess);
					
					checkboxAlertBeforeEnding = new JCheckBox("Alert before ending or killing processes");
					checkboxAlertBeforeEnding.setMargin(new Insets(2, 0, 2, 2));
					checkboxAlertBeforeEnding.setBounds(20, 67, 335, 25);
					panelProcessNorth.add(checkboxAlertBeforeEnding);
					
					JLabel lblInformationFields = new JLabel("Information Fields");
					lblInformationFields.setFont(new Font("Dialog", Font.BOLD, 15));
					lblInformationFields.setBounds(12, 110, 168, 17);
					panelProcessNorth.add(lblInformationFields);
				}
				
				JPanel panelProcessFields = new JPanel();
				panelProcessFields.setBorder(new EmptyBorder(0, 10, 10, 10));
				panelProcesses.add(panelProcessFields, BorderLayout.CENTER);
				panelProcessFields.setLayout(new BorderLayout(0, 0));
				
				listProcesses = new CheckboxList();
				listProcesses.setListData(Process.getColumnHeaders());
				
				JScrollPane scrollPaneProcess = new JScrollPane(listProcesses);
				
				panelProcessFields.add(scrollPaneProcess, BorderLayout.CENTER);

			}
			{
				JPanel panelResources = new JPanel();
				tabbedPane.addTab("Resources", null, panelResources, null);
				panelResources.setLayout(null);
				
				JLabel labelResourceBehavior = new JLabel("Behavior");
				labelResourceBehavior.setFont(new Font("Dialog", Font.BOLD, 15));
				labelResourceBehavior.setBounds(12, 12, 86, 17);
				panelResources.add(labelResourceBehavior);
				
				JLabel labelResourceInterval = new JLabel("Update Interval in Seconds:");
				labelResourceInterval.setBounds(20, 41, 202, 17);
				panelResources.add(labelResourceInterval);
				
				spinnerResource = new JSpinner();
				spinnerResource.setEnabled(false);
				spinnerResource.setModel(new SpinnerNumberModel(1, 1, 120, 1));
				spinnerResource.setBounds(220, 40, 71, 20);
				((JSpinner.DefaultEditor) spinnerResource.getEditor()).getTextField().setEditable(false);
				panelResources.add(spinnerResource);
				
				JLabel labelGraphs = new JLabel("Graphs");
				labelGraphs.setFont(new Font("Dialog", Font.BOLD, 15));
				labelGraphs.setBounds(12, 95, 210, 17);
				panelResources.add(labelGraphs);
				
				checkboxShowCpuHistory = new JCheckBox("Show CPU History");
				checkboxShowCpuHistory.setMargin(new Insets(2, 0, 2, 2));
				checkboxShowCpuHistory.setBounds(22, 120, 296, 25);
				panelResources.add(checkboxShowCpuHistory);
				
				checkboxShowAverageAll = new JCheckBox("Show Average All CPUs");
				checkboxShowAverageAll.setMargin(new Insets(2, 0, 2, 2));
				checkboxShowAverageAll.setBounds(22, 149, 298, 25);
				panelResources.add(checkboxShowAverageAll);
				
				checkboxShowMemoryHistory = new JCheckBox("Show Memory History");
				checkboxShowMemoryHistory.setMargin(new Insets(2, 0, 2, 2));
				checkboxShowMemoryHistory.setBounds(22, 179, 279, 25);
				panelResources.add(checkboxShowMemoryHistory);
				
				checkboxShowNetworkHistory = new JCheckBox("Show Network History");
				checkboxShowNetworkHistory.setMargin(new Insets(2, 0, 2, 2));
				checkboxShowNetworkHistory.setBounds(22, 208, 305, 25);
				panelResources.add(checkboxShowNetworkHistory);
			}
			{
				JPanel panelFileSystems = new JPanel();
				panelFileSystems.setOpaque(false);
				tabbedPane.addTab("File Systems", null, panelFileSystems, null);
				panelFileSystems.setLayout(new BorderLayout(0, 0));
				{
					JPanel panelFileSystemNorth = new JPanel();
					panelFileSystemNorth.setPreferredSize(new Dimension(10, 135));
					panelFileSystems.add(panelFileSystemNorth, BorderLayout.NORTH);
					panelFileSystemNorth.setLayout(null);
					
					JLabel labelFileSystemBehavior = new JLabel("Behavior");
					labelFileSystemBehavior.setFont(new Font("Dialog", Font.BOLD, 15));
					labelFileSystemBehavior.setBounds(12, 12, 86, 17);
					panelFileSystemNorth.add(labelFileSystemBehavior);
					
					JLabel labelFileSystemInterval = new JLabel("Update Interval in Seconds:");
					labelFileSystemInterval.setBounds(20, 41, 202, 17);
					panelFileSystemNorth.add(labelFileSystemInterval);
					
					spinnerFileSystem = new JSpinner();
					spinnerFileSystem.setModel(new SpinnerNumberModel(1, 1, 120, 1));
					spinnerFileSystem.setBounds(220, 40, 71, 20);
					((JSpinner.DefaultEditor) spinnerFileSystem.getEditor()).getTextField().setEditable(false);
					panelFileSystemNorth.add(spinnerFileSystem);
					
					checkboxShowAllFilesystems = new JCheckBox("Show all file systems");
					checkboxShowAllFilesystems.setMargin(new Insets(2, 0, 2, 2));
					checkboxShowAllFilesystems.setBounds(20, 67, 335, 25);
					panelFileSystemNorth.add(checkboxShowAllFilesystems);
					
					JLabel labelFileSystemInformationFields = new JLabel("Information Fields");
					labelFileSystemInformationFields.setFont(new Font("Dialog", Font.BOLD, 15));
					labelFileSystemInformationFields.setBounds(12, 110, 168, 17);
					panelFileSystemNorth.add(labelFileSystemInformationFields);
				}
				JPanel panelFileSystemFields = new JPanel();
				panelFileSystemFields.setBorder(new EmptyBorder(0, 10, 10, 10));
				panelFileSystems.add(panelFileSystemFields, BorderLayout.CENTER);
				panelFileSystemFields.setLayout(new BorderLayout(0, 0));
				
			
				listFileSystem = new CheckboxList();
				listFileSystem.setListData(FileSystem.getColumnHeaders());
				
				JScrollPane scrollPaneFileSystem = new JScrollPane(listFileSystem);
				
				panelFileSystemFields.add(scrollPaneFileSystem, BorderLayout.CENTER);
			}
			
			if (this.owner.getSelectedTabIndex() > 0) {
				
				tabbedPane.setSelectedIndex(this.owner.getSelectedTabIndex() - 1);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						
						try {
							PreferenceDialog.this.setPreference(PreferenceDialog.this.getPreference());
							PreferenceDialog.this.owner.updatePreference();
							PreferenceDialog.this.dispose();
							
						} catch (Exception e) {

							e.printStackTrace();
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
						
						PreferenceDialog.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		setPreference(preference);
		setLocationRelativeTo(owner);
	}

	/**
	 * @return the preference
	 */
	public Preference getPreference() {
		

		preference.setProcessUpdateInterval((Integer) spinnerProcess.getValue());
		
		preference.setAlertBeforeKillingProcess(checkboxAlertBeforeEnding.isSelected());
		preference.setUnSelectedProcessFields(listProcesses.getUnSelectedIndexes());

		preference.setShowCPUHistory(checkboxShowCpuHistory.isSelected());
		preference.setShowAverageCPUHistory(checkboxShowAverageAll.isSelected());
		preference.setShowMemoryHistory(checkboxShowMemoryHistory.isSelected());
		preference.setShowNetworkHistory(checkboxShowNetworkHistory.isSelected());
		
		preference.setFileSystemsUpdateInterval((Integer) spinnerFileSystem.getValue());
		preference.setShowAllFileSystems(checkboxShowAllFilesystems.isSelected());
		preference.setUnSelectedFileSystemFields(listFileSystem.getUnSelectedIndexes());
		
		return preference;
	}

	/**
	 * @param preference the preference to set
	 */
	public void setPreference(Preference preference) {
		this.preference = preference;
		
		spinnerProcess.setValue(preference.getProcessUpdateInterval());
		checkboxAlertBeforeEnding.setSelected(preference.isAlertBeforeKillingProcess());
		listProcesses.setUnSelectedIndexes(preference.getSelectedProcessFields());
		
		spinnerResource.setValue((int) Preference.getResourceUpdateinterval());
		checkboxShowCpuHistory.setSelected(preference.isShowCPUHistory());
		checkboxShowAverageAll.setSelected(preference.isShowAverageCPUHistory());
		checkboxShowMemoryHistory.setSelected(preference.isShowMemoryHistory());
		checkboxShowNetworkHistory.setSelected(preference.isShowNetworkHistory());
		
		spinnerFileSystem.setValue((int) preference.getFileSystemsUpdateInterval());
		checkboxShowAllFilesystems.setSelected(preference.isShowAllFileSystems());
		listFileSystem.setUnSelectedIndexes(preference.getUnSelectedFileSystemFields());
	}
}
