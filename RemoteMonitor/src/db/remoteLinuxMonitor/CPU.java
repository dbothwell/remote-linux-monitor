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

import java.io.Serializable;


public class CPU implements Vertex, Serializable {
	
	public static final int CPU_ALL = 10000;
	
	private int index;
	protected double loadPercent;
	private long procUser;
	private long procNice;
	private long procSystem;
	private long procIdle;
	
	/**
	 * Constructor 
	 */
	public CPU() {
		super();
	}

	/**
	 * @param cpu
	 * @param processUser
	 * @param processNice
	 * @param processSystem
	 * @param processIdle
	 */
	public CPU(int index, long procUser, long procNice, long procSystem, long procIdle) {
		super();
		this.index = index;
		this.procUser = procUser;
		this.procNice = procNice;
		this.procSystem = procSystem;
		this.procIdle = procIdle;
	}

	public void calculateLoad(Vertex oldVertex) {
		
		// http://ubuntuforums.org/showthread.php?t=148781
		
		CPU oldCPU = (CPU) oldVertex;
		
		long usage = (procUser - oldCPU.getProcUser()) + 
			(procNice - oldCPU.getProcNice()) + (procSystem - oldCPU.procSystem);
		
		long total = usage + (procIdle - oldCPU.procIdle);
		
		loadPercent = (100*usage)/total;
	}
	
	/**
	 * @return the cpu
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param cpu the cpu to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the usagePercent
	 */
	public double getLoadPercent() {
		return loadPercent;
	}

	/**
	 * @param loadPercent the usagePercent to set
	 */
	public void setLoadPercent(double loadPercent) {
		this.loadPercent = loadPercent;
	}

	/**
	 * @return the processUser
	 */
	public long getProcUser() {
		return procUser;
	}

	/**
	 * @param processUser the processUser to set
	 */
	public void setProcUser(long procUser) {
		this.procUser = procUser;
	}

	/**
	 * @return the processNice
	 */
	public long getProcNice() {
		return procNice;
	}

	/**
	 * @param processNice the processNice to set
	 */
	public void setProcNice(long procNice) {
		this.procNice = procNice;
	}

	/**
	 * @return the processSystem
	 */
	public long getProcSystem() {
		return procSystem;
	}

	/**
	 * @param processSystem the processSystem to set
	 */
	public void setProcSystem(long procSystem) {
		this.procSystem = procSystem;
	}

	/**
	 * @return the processIdle
	 */
	public long getProcIdle() {
		return procIdle;
	}

	/**
	 * @param processIdle the processIdle to set
	 */
	public void setProcIdle(long procIdle) {
		this.procIdle = procIdle;
	}

	public String toString() {
		return "index = " + index + ", usagePercent = " + loadPercent + "%"; 
	}

	@Override
	public double getYPosition() {

		return loadPercent;
	}

	@Override
	public String getLegendText() {
		
		String text = " CPU" + Integer.toString(index) + " " + ((int) loadPercent) + "% ";
		return String.format("%-11s", text);
	}
}
