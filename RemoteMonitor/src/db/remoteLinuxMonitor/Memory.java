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


public class Memory implements Vertex, Serializable {

	public static final int PHYSICAL_MEMORY = 0;
	public static final int SWAP_MEMORY = 1;
	
	private static final String[] typeDesc = {"Physical", "  Swap" };
	
	private int type = PHYSICAL_MEMORY;
	private double loadPercent;
	private long totalMemory;
	private long usedMemory;
	private long freeMemory;
	private String unit;
	
	/**
	 * Constructor 
	 */
	public Memory() {
		super();
	}

	/**
	 * @param totalMemory
	 * @param usedMemory
	 * @param freeMemory
	 */
	public Memory(int type, long totalMemory, long usedMemory, long freeMemory, String unit) {
		super();
		this.type = type;
		this.totalMemory = totalMemory;
		this.usedMemory = usedMemory;
		this.freeMemory = freeMemory;
		this.unit = unit;
	}
	
	@Override
	public void calculateLoad(Vertex oldVertex) {

		loadPercent = (double) (((double) usedMemory / totalMemory) * 100);
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
//		color = PlotPoint.COLORS[type];
	}

	/**
	 * @return the totalMemory
	 */
	public long getTotalMemory() {
		return totalMemory;
	}

	/**
	 * @param totalMemory the totalMemory to set
	 */
	public void setTotalMemory(long totalMemory) {
		this.totalMemory = totalMemory;
	}

	/**
	 * @return the usedMemory
	 */
	public long getUsedMemory() {
		return usedMemory;
	}

	/**
	 * @param usedMemory the usedMemory to set
	 */
	public void setUsedMemory(long usedMemory) {
		this.usedMemory = usedMemory;
	}

	/**
	 * @return the freeMemory
	 */
	public long getFreeMemory() {
		return freeMemory;
	}

	/**
	 * @param freeMemory the freeMemory to set
	 */
	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}
	
	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public String toString() {
		return "type = " + type + ", totalMemory = " + totalMemory + ", usedMemory = " + usedMemory + ", freeMemory = " + freeMemory + " load% = " + loadPercent; 
	}

	public void setLoadPercent(double loadPercent) {
		this.loadPercent = loadPercent;
	}

	public double getLoadPercent() {
		return loadPercent;
	}

	@Override
	public int getIndex() {
		return type;
	}

	@Override
	public double getYPosition() {
		return loadPercent;
	}

	@Override
	public String getLegendText() {
		
		long gigDivFactor = 1000000000;
		
		if (unit.equalsIgnoreCase("kb")) {
			gigDivFactor = 1000000;
			
		} else if (unit.equalsIgnoreCase("mb")) {
			gigDivFactor = 1000;
		}
		
		String text =" " + typeDesc[type] + " (" + Integer.toString((int) loadPercent) + "%) of " + 
			String.format("%.2f", ((float) totalMemory / gigDivFactor)) + " GB ";
		
		return String.format("%-28s", text); 
	}

}
