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

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

public class MonitorTable extends JTable {
	
//	public static final Color ALT_ROW_COLOR = new Color(230, 230, 230);
	public static final Color ALT_ROW_COLOR = UIManager.getColor("Label.background");

	public MonitorTable() {
		super();
		
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setFont(new Font(RemoteMain.DEFAULT_FONT, Font.PLAIN, 13));
		setIntercellSpacing(new Dimension(0, 1));
		setFillsViewportHeight(true);
		setShowVerticalLines(false);
		setShowHorizontalLines(false);
		setAutoCreateRowSorter(true);
		setShowGrid(false);
		setRowHeight(24);
		
//		this.getTableHeader().setBorder(new MatteBorder(1,0,1,0,Color.LIGHT_GRAY));

		getTableHeader().setFont( new Font(RemoteMain.DEFAULT_FONT, Font.BOLD, 13 ));
		getTableHeader().setReorderingAllowed(false);
	}	
}
