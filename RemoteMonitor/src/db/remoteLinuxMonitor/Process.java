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

public class Process {
	
	public static final int COLUMN_PROCESS_NAME = 0;
	public static final int COLUMN_USER = 1;
	public static final int COLUMN_PID = 2;
	public static final int COLUMN_STATUS = 3;
	public static final int COLUMN_CPU_USAGE = 4;
	public static final int COLUMN_MEM_USAGE = 5;
	public static final int COLUMN_CPU_TIME = 6;
	public static final int COLUMN_START_TIME = 7;
	public static final int COLUMN_VIRTUAL_MEMORY = 8;
	public static final int COLUMN_RESIDENT_MEMORY = 9;
	public static final int COLUMN_NICE = 10;
	public static final int COLUMN_PARENT_PID = 11;
	public static final int COLUMN_SECURITY_LABEL = 12;
	public static final int COLUMN_WAIT_CHANNEL = 13;
	public static final int COLUMN_COMMAND_LINE = 14;
	
	public static final String STAT_UNINTERRUPTIBLE_SLEEP = "D";  	// Uninterruptible sleep (usually IO)
	public static final String STAT_RUNNING = "R";					// Running or runnable (on run queue)
	public static final String STAT_SLEEP = "S"; 					// Interruptible sleep (waiting for an event to complete)
	public static final String STAT_STOPPED = "T";					// Stopped, either by a job control signal or because it is being traced.
	public static final String STAT_PAGING = "W"; 					// paging (not valid since the 2.6.xx kernel)
	public static final String STAT_DEAD = "X"; 					// dead (should never be seen)
	public static final String STAT_ZOMBIE = "Z"; 					// Defunct ("zombie") process, terminated but not reaped by its parent.
	
	private String processName; 	// comm
	private String user;			// euser	
	private long virtualMemory;		// vsz
	private long residentMemory;	// rss
	private float cpuUsage;			// %cpu
	private String cpuTime;			// cputime
	private String startTime;		// start_time
	private long pid;				// pid
	private int nice;				// nice
	private String securityLabel;	// label
	private float memUsage;			// %mem
	private String waitChannel;		// wchan:14
	private long parentPid;			// ppid
	private String commandline;		// args
	private String status;			// stat
	
	public static String[] getColumnHeaders() {
		
		String[] headers = { "Process Name", "User", "ID", "Status", "CPU %", "Memory %", "CPU Time", "Start Time", "Virtual Memory", "Resident Memory", "Nice", "Parent ID", "Security Label", "Wait Channel", "Command line" };

		return headers;
	}

	public static int[] getColumnWidths() {

		int[] widths = { 120, 70, 70, 70, 70, 100, 80, 90, 120, 140, 50, 80, 120, 110, 300 };

		return widths;
	}
	
	public static String getStatusDescription(String status) {
		
		if (status.toUpperCase().startsWith(Process.STAT_UNINTERRUPTIBLE_SLEEP)) {
			
			return "IO";
			
		} else if (status.toUpperCase().startsWith(Process.STAT_DEAD)) {
			
			return "Dead";
			
		} else if (status.toUpperCase().startsWith(Process.STAT_PAGING)) {
			
			return "Paging";
			
		} else if (status.toUpperCase().startsWith(Process.STAT_RUNNING)) {
			
			return "Running";
			
		} else if (status.toUpperCase().startsWith(Process.STAT_SLEEP)) {
			
			return "Sleeping";
			
		} else if (status.toUpperCase().startsWith(Process.STAT_STOPPED)) {
			
			return "Stopped";
			
		} else if (status.toUpperCase().startsWith(Process.STAT_ZOMBIE)) {
			
			return "Zombie";
		}
		
		return "Unknown";
	}
	
	/**
	 * @return the processName
	 */
	public String getProcessName() {
		return processName;
	}
	/**
	 * @param processName the processName to set
	 */
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}
	/**
	 * @return the virtualMemory
	 */
	public long getVirtualMemory() {
		return virtualMemory;
	}
	/**
	 * @param virtualMemory the virtualMemory to set
	 */
	public void setVirtualMemory(long virtualMemory) {
		this.virtualMemory = virtualMemory;
	}
	/**
	 * @return the residentMemory
	 */
	public long getResidentMemory() {
		return residentMemory;
	}
	/**
	 * @param residentMemory the residentMemory to set
	 */
	public void setResidentMemory(long residentMemory) {
		this.residentMemory = residentMemory;
	}
	/**
	 * @return the cpuTime
	 */
	public String getCpuTime() {
		return cpuTime;
	}
	/**
	 * @param cpuTime the cpuTime to set
	 */
	public void setCpuTime(String cpuTime) {
		this.cpuTime = cpuTime;
	}
	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the pid
	 */
	public long getPid() {
		return pid;
	}
	/**
	 * @param pid the pid to set
	 */
	public void setPid(long pid) {
		this.pid = pid;
	}
	/**
	 * @return the nice
	 */
	public int getNice() {
		return nice;
	}
	/**
	 * @param nice the nice to set
	 */
	public void setNice(int nice) {
		this.nice = nice;
	}
	/**
	 * @return the securityLabel
	 */
	public String getSecurityLabel() {
		return securityLabel;
	}
	/**
	 * @param securityLabel the securityLabel to set
	 */
	public void setSecurityLabel(String securityLabel) {
		this.securityLabel = securityLabel;
	}
	/**
	 * @return the memUsage
	 */
	public float getMemUsage() {
		return memUsage;
	}
	/**
	 * @param memUsage the memUsage to set
	 */
	public void setMemUsage(float memUsage) {
		this.memUsage = memUsage;
	}
	/**
	 * @return the parentPid
	 */
	public long getParentPid() {
		return parentPid;
	}
	/**
	 * @param parentPid the parentPid to set
	 */
	public void setParentPid(long parentPid) {
		this.parentPid = parentPid;
	}
	/**
	 * @return the commandline
	 */
	public String getCommandline() {
		return commandline;
	}
	/**
	 * @param commandline the commandline to set
	 */
	public void setCommandline(String commandline) {
		this.commandline = commandline;
	}
	/**
	 * @return the cpuUsage
	 */
	public float getCpuUsage() {
		return cpuUsage;
	}
	/**
	 * @param cpuUsage the cpuUsage to set
	 */
	public void setCpuUsage(float cpuUsage) {
		this.cpuUsage = cpuUsage;
	}
	/**
	 * @return the waitChannel
	 */
	public String getWaitChannel() {
		return waitChannel;
	}
	/**
	 * @param waitChannel the waitChannel to set
	 */
	public void setWaitChannel(String waitChannel) {
		this.waitChannel = waitChannel;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
}
