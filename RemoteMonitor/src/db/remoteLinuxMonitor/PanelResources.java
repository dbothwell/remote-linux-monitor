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

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class PanelResources extends JPanel {
	
	private GraphPanel graphPanelCPU;
	private GraphPanel graphPanelAverageCPU;
	private GraphPanel graphPanelMemory;
	private GraphPanel graphPanelNetwork;
	
	/**
	 * 
	 */
	public PanelResources() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		panelReset();
	}
	
	public void panelReset() {
		
		removeAll();
		
		graphPanelCPU =  new GraphPanel(new CPUYPoint(), GraphPanel.TYPE_PERCENTAGE);
		graphPanelAverageCPU = new GraphPanel(new AvgCPUYPoint(), GraphPanel.TYPE_PERCENTAGE);
		graphPanelMemory = new GraphPanel(new MemoryYPoint(), GraphPanel.TYPE_PERCENTAGE);
		graphPanelNetwork = new GraphPanel(new NetworkYPoint(), GraphPanel.TYPE_NETWORK);
		

		add(graphPanelCPU);
		add(graphPanelAverageCPU);
		add(graphPanelMemory);
		add(graphPanelNetwork);
		revalidate();
	}
	
	public void display(ResourceInfo resourceIfo) {
		
		graphPanelCPU.plotIt(resourceIfo.getCpuVertices());
		graphPanelAverageCPU.plotIt(resourceIfo.getAvgCpuVertices());
		graphPanelMemory.plotIt(resourceIfo.getMemoryVertices());
		graphPanelNetwork.plotIt(resourceIfo.getNetworkVertices());
		revalidate();
	}
	
	public void setPreference(Preference preference) {

		graphPanelCPU.setVisible(preference.isShowCPUHistory());
		graphPanelAverageCPU.setVisible(preference.isShowAverageCPUHistory());
		graphPanelMemory.setVisible(preference.isShowMemoryHistory());
		graphPanelNetwork.setVisible(preference.isShowNetworkHistory());
		revalidate();
	}
}
