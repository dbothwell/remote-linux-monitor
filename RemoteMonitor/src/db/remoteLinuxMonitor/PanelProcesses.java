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

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class PanelProcesses extends JPanel implements ListSelectionListener {
	
	private MonitorTable table;
	private JLabel labelUptime;
	
	private RemoteMain remoteMain = null;

	private ArrayList<Process> processes = new ArrayList<Process>();

	public PanelProcesses(RemoteMain remoteMain) {
		
		this.remoteMain = remoteMain;
		
		setLayout(new BorderLayout(0, 0));
		
		JPanel panelNorth = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panelNorth.getLayout();
		flowLayout.setVgap(10);
		flowLayout.setHgap(10);
		flowLayout.setAlignment(FlowLayout.LEFT);
		add(panelNorth, BorderLayout.NORTH);
		
		labelUptime = new JLabel(" ");
		labelUptime.setFont(new Font(RemoteMain.DEFAULT_FONT, Font.PLAIN, 13));
		panelNorth.add(labelUptime);
		
		JPanel panelCenter = new JPanel();
		panelCenter.setBorder(new EmptyBorder(0, 10, 0, 10));
		add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(new BorderLayout(0, 0));
		
		table = new MonitorTable();
		table.getSelectionModel().addListSelectionListener(this);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setModel(new ProcessTableModel());
		table.setDefaultRenderer(Object.class, new ProcessTableCellRenderer());
		table.setDefaultRenderer(Long.class, new ProcessTableCellRenderer());
		table.setDefaultRenderer(Float.class, new ProcessTableCellRenderer());
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setOpaque(false);
	    
		panelCenter.add(scrollPane, BorderLayout.CENTER);
	}
	
	public void display(ProcessInfo processInfo) {

		labelUptime.setText(processInfo.getUptime());
		
		int selectedRow = table.getSelectedRow();
		
		table.clearSelection();
		processes.clear();
		
		switch(remoteMain.showProcessesType()) {
		
			case RemoteMain.ALL_PROCESS:
				
				processes.addAll(processInfo.getProcesses());
				break;
				
			case RemoteMain.ACTIVE_PROCESS:
				
				for (Process process: processInfo.getProcesses()) {
					
					if (process.getStatus().equals(Process.getStatusDescription(Process.STAT_RUNNING))) {
						processes.add(process);
					}
				}
				break;
				
			case RemoteMain.MY_PROCESS:
				
				for (Process process: processInfo.getProcesses()) {
					
					if (process.getUser().equals(remoteMain.getUserInfo().getUser())) {
						processes.add(process);
					}
				}
				break;
				
			default:
				
				processes.addAll(processInfo.getProcesses());
				break;
			}

		((AbstractTableModel) table.getModel()).fireTableDataChanged();
		
		if ( (table.getRowCount() > 0) &&  selectedRow < (table.getRowCount() - 1)) {
			
			ListSelectionModel selectionModel = table.getSelectionModel();
			selectionModel.setSelectionInterval(selectedRow, selectedRow);
		}
	}
	
	public void setPreference(Preference preference) {
		
		table.createDefaultColumnsFromModel();
		
		for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
			
			table.getColumnModel().getColumn(i).setPreferredWidth(Process.getColumnWidths()[i]);
		}
		
		int i = 0;
		for (Integer selectedIndex: preference.getSelectedProcessFields()) {
			
			table.getColumnModel().removeColumn(table.getColumnModel().getColumn(selectedIndex - i++));
		}
	}
	
	public class ProcessTableCellRenderer extends DefaultTableCellRenderer {

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
			
			((JComponent) c).setBorder(null);
			return c;
		}
	}

	class ProcessTableModel extends AbstractTableModel {
		
		  public String getColumnName(int c) {
			  
		    return Process.getColumnHeaders()[c];
		  }

		  public Class<?> getColumnClass(int c) {
				  
			  switch(c) {

				  case Process.COLUMN_CPU_USAGE:
				  case Process.COLUMN_MEM_USAGE:
	
					  return Float.class;
	
				  case Process.COLUMN_PARENT_PID:
				  case Process.COLUMN_PID:
				  case Process.COLUMN_RESIDENT_MEMORY:
				  case Process.COLUMN_VIRTUAL_MEMORY:
	
					  return Long.class;
					  
				  case Process.COLUMN_NICE:
					  
					  return Integer.class;
				  }

			  return String.class;
		  }

		  public int getColumnCount() {

			  return Process.getColumnHeaders().length;
		  }

		  public int getRowCount() {

			  return processes.size();
		  }

		  public Object getValueAt(int r, int c) {
			  
			  Process process = processes.get(r);

			  switch(c) {
			  
				  case Process.COLUMN_COMMAND_LINE:
						
					  	return process.getCommandline();
					  	
				  case Process.COLUMN_CPU_TIME:
						
					  	return process.getCpuTime();
		
				  case Process.COLUMN_CPU_USAGE:
						
					  	return process.getCpuUsage();
		
				  case Process.COLUMN_MEM_USAGE:
						
					  	return process.getMemUsage();
		
				  case Process.COLUMN_NICE:
						
					  	return process.getNice();
		
				  case Process.COLUMN_PARENT_PID:
						
					  	return process.getParentPid();
		
				  case Process.COLUMN_PID:
						
					  	return process.getPid();
		
				  case Process.COLUMN_PROCESS_NAME:
						
					  	return process.getProcessName();
		
				  case Process.COLUMN_RESIDENT_MEMORY:
						
					  	return process.getResidentMemory();
		
				  case Process.COLUMN_SECURITY_LABEL:
						
					  	return process.getSecurityLabel();
		
				  case Process.COLUMN_START_TIME:
						
					  	return process.getStartTime();
					  	
				  case Process.COLUMN_STATUS:
						
					  	return process.getStatus();
		
				  case Process.COLUMN_USER:
						
					  	return process.getUser();
		
				  case Process.COLUMN_VIRTUAL_MEMORY:
						
					  	return process.getVirtualMemory();
		
				  case Process.COLUMN_WAIT_CHANNEL:
						
					  	return process.getWaitChannel();
				  }

			  return null;
		  }

		  public void setValueAt(Object obj, int r, int c) {

		  }

		  public boolean isCellEditable(int r, int c) {
			  return false;
		  }
	}

	@Override
	public void valueChanged(ListSelectionEvent evt) {
		
        if (evt.getSource() == table.getSelectionModel()) {

        	remoteMain.setProcessMenuItemsEnabled();
          }
	}
	
	protected boolean isRowSelected() {
		
		if (table.getSelectedRow() >= 0) {
			
			return true;
		
		} else { 
		
			return false; 
		}
	}
	
	protected long getSelectedProcessId() {
		
		if (table.getSelectedRow() > -1) {

			int selected = table.convertRowIndexToModel(table.getSelectedRow());
			return Long.parseLong(table.getModel().getValueAt(selected, Process.COLUMN_PID).toString());
		}
		
		return -1;
	}
	
	protected int getSelectedNice() {
		
		if (table.getSelectedRow() > -1) {

			int selected = table.convertRowIndexToModel(table.getSelectedRow());
			return Integer.parseInt(table.getModel().getValueAt(selected, Process.COLUMN_NICE).toString());
		}
		
		return -1;
	}
}
