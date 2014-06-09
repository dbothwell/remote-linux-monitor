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

import java.util.ArrayList;

public class Preference {
	
	// Processes
	private int processUpdateInterval = RemoteMain.TIMER_DELAY_PROCESSES / 1000;
	private boolean alertBeforeKillingProcess = true;
	private ArrayList<Integer> unSelectedProcessFields = new ArrayList<Integer>();

	// Resources
	private static final int resourceUpdateInterval = RemoteMain.TIMER_DELAY_RESOURCES / 1000;
	private boolean displayCacheAndBuffersAsUsed = true;

	private boolean showCPUHistory = true;
	private boolean showAverageCPUHistory = false;
	private boolean showMemoryHistory = true;
	private boolean showNetworkHistory = true;
	
	// File Systems
	private int fileSystemsUpdateInterval = RemoteMain.TIMER_DELAY_FILE_SYSTEMS / 1000;
	private boolean showAllFileSystems = false;
	private ArrayList<Integer> unSelectedFileSystemFields = new ArrayList<Integer>();
	
	public Preference() {
		super();
		
		unSelectedProcessFields.add(Process.COLUMN_CPU_TIME);
		unSelectedProcessFields.add(Process.COLUMN_VIRTUAL_MEMORY);
		unSelectedProcessFields.add(Process.COLUMN_RESIDENT_MEMORY);
		unSelectedProcessFields.add(Process.COLUMN_NICE);
		unSelectedProcessFields.add(Process.COLUMN_PARENT_PID);
		unSelectedProcessFields.add(Process.COLUMN_SECURITY_LABEL);
		unSelectedProcessFields.add(Process.COLUMN_WAIT_CHANNEL);
	}

	/**
	 * @return the processUpdateInterval
	 */
	public int getProcessUpdateInterval() {
		return processUpdateInterval;
	}

	/**
	 * @param processUpdateInterval the processUpdateInterval to set
	 */
	public void setProcessUpdateInterval(int processUpdateInterval) {
		this.processUpdateInterval = processUpdateInterval;
	}

	/**
	 * @return the alertBeforeKillingProcess
	 */
	public boolean isAlertBeforeKillingProcess() {
		return alertBeforeKillingProcess;
	}

	/**
	 * @param alertBeforeKillingProcess the alertBeforeKillingProcess to set
	 */
	public void setAlertBeforeKillingProcess(boolean alertBeforeKillingProcess) {
		this.alertBeforeKillingProcess = alertBeforeKillingProcess;
	}

	/**
	 * @return the unSelectedProcessFields
	 */
	public ArrayList<Integer> getSelectedProcessFields() {
		return unSelectedProcessFields;
	}

	/**
	 * @param unSelectedProcessFields the unSelectedProcessFields to set
	 */
	public void setUnSelectedProcessFields(ArrayList<Integer> unSelectedProcessFields) {
		this.unSelectedProcessFields = unSelectedProcessFields;
	}
	
	/**
	 * @return the displayCacheAndBuffersAsUsed
	 */
	public boolean isDisplayCacheAndBuffersAsUsed() {
		return displayCacheAndBuffersAsUsed;
	}

	/**
	 * @param displayCacheAndBuffersAsUsed the displayCacheAndBuffersAsUsed to set
	 */
	public void setDisplayCacheAndBuffersAsUsed(boolean displayCacheAndBuffersAsUsed) {
		this.displayCacheAndBuffersAsUsed = displayCacheAndBuffersAsUsed;
	}

	/**
	 * @return the showCPUHistory
	 */
	public boolean isShowCPUHistory() {
		return showCPUHistory;
	}

	/**
	 * @param showCPUHistory the showCPUHistory to set
	 */
	public void setShowCPUHistory(boolean showCPUHistory) {
		this.showCPUHistory = showCPUHistory;
	}

	/**
	 * @return the showAverageCPUHistory
	 */
	public boolean isShowAverageCPUHistory() {
		return showAverageCPUHistory;
	}

	/**
	 * @param showAverageCPUHistory the showAverageCPUHistory to set
	 */
	public void setShowAverageCPUHistory(boolean showAverageCPUHistory) {
		this.showAverageCPUHistory = showAverageCPUHistory;
	}

	/**
	 * @return the showMemoryHistory
	 */
	public boolean isShowMemoryHistory() {
		return showMemoryHistory;
	}

	/**
	 * @param showMemoryHistory the showMemoryHistory to set
	 */
	public void setShowMemoryHistory(boolean showMemoryHistory) {
		this.showMemoryHistory = showMemoryHistory;
	}

	/**
	 * @return the showNetworkHistory
	 */
	public boolean isShowNetworkHistory() {
		return showNetworkHistory;
	}

	/**
	 * @param showNetworkHistory the showNetworkHistory to set
	 */
	public void setShowNetworkHistory(boolean showNetworkHistory) {
		this.showNetworkHistory = showNetworkHistory;
	}

	/**
	 * @return the fileSystemsUpdateInterval
	 */
	public int getFileSystemsUpdateInterval() {
		return fileSystemsUpdateInterval;
	}

	/**
	 * @param fileSystemsUpdateInterval the fileSystemsUpdateInterval to set
	 */
	public void setFileSystemsUpdateInterval(int fileSystemsUpdateInterval) {
		this.fileSystemsUpdateInterval = fileSystemsUpdateInterval;
	}

	/**
	 * @return the showAllFileSystems
	 */
	public boolean isShowAllFileSystems() {
		return showAllFileSystems;
	}

	/**
	 * @param showAllFileSystems the showAllFileSystems to set
	 */
	public void setShowAllFileSystems(boolean showAllFileSystems) {
		this.showAllFileSystems = showAllFileSystems;
	}

	/**
	 * @return the unSelectedFileSystemFields
	 */
	public ArrayList<Integer> getUnSelectedFileSystemFields() {
		return unSelectedFileSystemFields;
	}

	/**
	 * @param unSelectedFileSystemFields the unSelectedFileSystemFields to set
	 */
	public void setUnSelectedFileSystemFields(ArrayList<Integer> unSelectedFileSystemFields) {
		this.unSelectedFileSystemFields = unSelectedFileSystemFields;
	}

	/**
	 * @return the resourceUpdateinterval
	 */
	public static int getResourceUpdateinterval() {
		return resourceUpdateInterval;
	}	
}
