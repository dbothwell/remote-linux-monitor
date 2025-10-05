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

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class CheckboxList extends JList<Object> {

	public CheckboxList() {
		super();
	    setCellRenderer(new CheckListRenderer());
	    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    setBorder(new EmptyBorder(0, 4, 0, 0));
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				int index = locationToIndex(e.getPoint());
				CheckableItem item = (CheckableItem) getModel().getElementAt(index);
				item.setSelected(!item.isSelected());
				Rectangle rect = getCellBounds(index, index);
				repaint(rect);
			}
		});
		
	}
	
	public void setListData(String[] listData) {
		
		super.setListData(createData(listData));
	}
	
	private CheckableItem[] createData(String[] strs) {
		
		int n = strs.length;
		CheckableItem[] items = new CheckableItem[n];
		for (int i = 0; i < n; i++) {
			items[i] = new CheckableItem(strs[i]);
		}
		return items;
	}
	
	class CheckableItem {
		
		private String str;

		private boolean isSelected;

		public CheckableItem(String str) {
			this.str = str;
			isSelected = false;
		}

		public void setSelected(boolean b) {
			isSelected = b;
		}

		public boolean isSelected() {
			return isSelected;
		}

		public String toString() {
			return str;
		}
	}

	class CheckListRenderer extends JCheckBox implements ListCellRenderer<Object> {

		public CheckListRenderer() {
			setBackground(UIManager.getColor("List.textBackground"));
			setForeground(UIManager.getColor("List.textForeground"));
		}

		public Component getListCellRendererComponent(JList<?> list, Object value,
				int index, boolean isSelected, boolean hasFocus) {
			
			setEnabled(list.isEnabled());
			setSelected(((CheckableItem) value).isSelected());
			setFont(list.getFont());
			setText(value.toString());
			return this;
		}
	}
	
	public void setUnSelectedIndexes(ArrayList<Integer> unSelectedIndexes) {
		
		for (int i = 0; i < this.getModel().getSize(); i++) {
			
			((CheckableItem) this.getModel().getElementAt(i)).setSelected(true);
		}
		
		for (Integer unSelectedIndex: unSelectedIndexes) {
			
			((CheckableItem) this.getModel().getElementAt(unSelectedIndex)).setSelected(false);
		}
	}
	
	public ArrayList<Integer> getUnSelectedIndexes() {
		
		ArrayList<Integer> unSelectedIndexes = new ArrayList<Integer>();
		
		for (int i = 0; i < this.getModel().getSize(); i++) {
			
			if (!((CheckableItem) this.getModel().getElementAt(i)).isSelected) {
				
				unSelectedIndexes.add(i);
			}
		}
		
		return unSelectedIndexes;
	}

}
