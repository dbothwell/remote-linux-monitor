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
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class PanelFileSystems extends JPanel {
	
	private MonitorTable table;

	private ArrayList<FileSystem> fileSystems = new ArrayList<FileSystem>();
	private Preference preference;
		
	/**
	 * Create the panel.
	 */
	public PanelFileSystems() {
		
		setLayout(new BorderLayout(0, 0));
		
		JPanel panelNorth = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panelNorth.getLayout();
		flowLayout.setVgap(10);
		flowLayout.setHgap(10);
		flowLayout.setAlignment(FlowLayout.LEFT);
		add(panelNorth, BorderLayout.NORTH);
		
		JLabel labelFileSystems = new JLabel("File Systems");
		labelFileSystems.setFont(new Font(RemoteMain.DEFAULT_FONT, Font.BOLD, 13));
		panelNorth.add(labelFileSystems);
		
		JPanel panelCenter = new JPanel();
		panelCenter.setBorder(new EmptyBorder(0, 10, 10, 10));
		add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(new BorderLayout(0, 0));

		table = new MonitorTable();
		table.setModel(new FileSystemTableModel());
		table.setDefaultRenderer(Object.class, new FileSystemTableCellRenderer());
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setOpaque(false);
	    
		panelCenter.add(scrollPane, BorderLayout.CENTER);

	}

	public void display(FileSystemInfo fileSystemInfo) {
		
		int selectedRow = table.getSelectedRow();
		
		table.clearSelection();
		fileSystems.clear();
		
		if (preference.isShowAllFileSystems()) {
		
			fileSystems.addAll(fileSystemInfo.getFileSystems());
		
		} else {
			
			for (FileSystem fileSystem: fileSystemInfo.getFileSystems()) {
				
				if (fileSystem.getDevice().toLowerCase().startsWith("/dev/")) {
					
					fileSystems.add(fileSystem);
				}
			}
			
		}
		((AbstractTableModel) table.getModel()).fireTableDataChanged();
			
		ListSelectionModel selectionModel = table.getSelectionModel();
		selectionModel.setSelectionInterval(selectedRow, selectedRow);
	}
	
	public void setPreference(Preference preference) {
		
		this.preference = preference;
		
		table.createDefaultColumnsFromModel();
		
		for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
			
			table.getColumnModel().getColumn(i).setPreferredWidth(FileSystem.getColumnWidths()[i]);
		}
		
		int i = 0;
		for (Integer selectedIndex: preference.getUnSelectedFileSystemFields()) {
			
			table.getColumnModel().removeColumn(table.getColumnModel().getColumn(selectedIndex - i++));
		}
	}
	
	public class FileSystemTableCellRenderer extends DefaultTableCellRenderer {

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			
			Component c = super.getTableCellRendererComponent(table, value,	isSelected, hasFocus, row, column);

			if (!isSelected) {
			
				if (row % 2 == 0) {
					
					c.setBackground(Color.white);
					
				} else {
					
					c.setBackground(MonitorTable.ALT_ROW_COLOR);
				}
				
				c.setForeground(Color.black);
			}
			
			if (((String) table.getColumnModel().getColumn(column).getHeaderValue()).
					equals(FileSystem.getColumnHeaders()[FileSystem.COLUMN_USAGE_PERCENT])) {
				
				JProgressBar jProgressBar = new JProgressBar(0, 100);
				jProgressBar.setValue((Integer) value);
				jProgressBar.setStringPainted(true);
				
				return jProgressBar;
				
			} else if (((String) table.getColumnModel().getColumn(column).getHeaderValue()).
					equals(FileSystem.getColumnHeaders()[FileSystem.COLUMN_DEVICE])) {
				
				JLabel jLabel = new JLabel((String) value);
				jLabel.setIcon(new ImageIcon(getClass().getResource("/images/harddrive.png")));
				jLabel.setHorizontalAlignment(SwingConstants.LEFT); 
				jLabel.setFont(table.getFont());
				jLabel.setOpaque(true);
				
				if (!isSelected) {
				
					if (row % 2 == 0) {
						
						jLabel.setBackground(Color.white);
						
					} else {
						
						jLabel.setBackground(MonitorTable.ALT_ROW_COLOR);
					}
					
				} else {
					
					UIDefaults defaults = javax.swing.UIManager.getDefaults();
					jLabel.setBackground(defaults.getColor("Table.selectionBackground"));
				}
				
				jLabel.setBorder(null);
				return jLabel;
				
			} else {
			
				((JComponent) c).setBorder(null);
				return c;
			}
		}
	}
	
	class FileSystemTableModel extends AbstractTableModel {
		
		  public String getColumnName(int c) {
			  
		    return FileSystem.getColumnHeaders()[c];
		  }

		  public Class<?> getColumnClass(int c) {
			  
			  switch(c) {

				  case FileSystem.COLUMN_USAGE_PERCENT:
	
					  return JProgressBar.class;
			  }

			  return String.class;
		  }

		  public int getColumnCount() {

			  return FileSystem.getColumnHeaders().length;
		  }

		  public int getRowCount() {

			  return fileSystems.size();
		  }

		  public Object getValueAt(int r, int c) {
			  
			  FileSystem fileSystem = fileSystems.get(r);

			  switch(c) {
			  
				  case FileSystem.COLUMN_DEVICE:
						
					  	return fileSystem.getDevice();
	
				  case FileSystem.COLUMN_DIRECTORY:
	
					  return fileSystem.getDirectory();
					  
				  case FileSystem.COLUMN_TYPE:
						
					  return fileSystem.getType();
	
				  case FileSystem.COLUMN_SIZE:
					  
					  return fileSystem.getSize();
					  
				  case FileSystem.COLUMN_USED:
						
					  return fileSystem.getUsed();
	
				  case FileSystem.COLUMN_AVAILABLE:
						  
						  return fileSystem.getAvailable();
						  
				  case FileSystem.COLUMN_USAGE_PERCENT:
						
					  return fileSystem.getUsagePercent();
			  }

			  return null;
		  }

		  public void setValueAt(Object obj, int r, int c) {

		  }

		  public boolean isCellEditable(int r, int c) {
			  return false;
		  }
	}

}
