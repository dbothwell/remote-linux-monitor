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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ResourceInfo {
	
	private static final String COMMAND = "date +\"date %m-%d-%y %r\"; grep cpu /proc/stat; grep \"MemTotal\\|MemFree\\|Buffers\\|Cached\\|SwapTotal\\|SwapFree\" /proc/meminfo; grep \":\" /proc/net/dev  | sed  's/^/Interface /;s/:/ /'";
	
	private static final int POINTS_PER_MINUTE = (int) (60);
	
	private static final int CPU_HISTORY_INFO = 0;
	private static final int AVG_CPU_HISTORY_INFO = 1;
	private static final int MEMORY_HISTORY_INFO = 2;
	private static final int NETWORK_HISTORY_INFO = 3;
	
	private static final String DATE_FORMAT = "MM-dd-yyyy hh:mm:ss a";
	
	private ArrayList<YPoint> cpuVertices = new ArrayList<YPoint>();
	private ArrayList<YPoint> avgCpuVertices = new ArrayList<YPoint>();
	private ArrayList<YPoint> memoryVertices = new ArrayList<YPoint>();
	private ArrayList<YPoint> networkVertices = new ArrayList<YPoint>();
	
	public void clear() {
		
		cpuVertices.clear();
		avgCpuVertices.clear();
		memoryVertices.clear();
		networkVertices.clear();
	}
	
	public static ResourceInfo addPlotPoints(SSHSession sshSession, ResourceInfo resourceInfo, Preference preference) throws Exception {
		
		try {
//			System.out.println(COMMAND);

			ArrayList<String> strs = sshSession.getStandardOutput(COMMAND);
			
			addYPoint(resourceInfo.getCpuVertices(), CPU_HISTORY_INFO, strs, preference);
			addYPoint(resourceInfo.getAvgCpuVertices(), AVG_CPU_HISTORY_INFO, strs, preference);
			addYPoint(resourceInfo.getMemoryVertices(), MEMORY_HISTORY_INFO, strs, preference);
			addYPoint(resourceInfo.getNetworkVertices(), NETWORK_HISTORY_INFO, strs, preference);
			
		} catch (Exception e) {

			e.printStackTrace();
			throw e;
		}
		
		
		return resourceInfo;
	}
	
	private static void addYPoint(ArrayList<YPoint> minutePoints, int yPointType, ArrayList<String> strs, Preference preference) throws Exception {

		try {

			if (minutePoints.size() == POINTS_PER_MINUTE) {
				minutePoints.remove(POINTS_PER_MINUTE - 1);
			}

			YPoint yp = null;
			
			switch (yPointType) {
			
				case CPU_HISTORY_INFO:
					yp = getCPUYPoint(strs);
					break;
					
				case AVG_CPU_HISTORY_INFO:
					yp = getAverageCPUYPoint(strs);
					break;
					
				case MEMORY_HISTORY_INFO:
					yp = getMemoryYPoint(strs, preference);
					break;
					
				case NETWORK_HISTORY_INFO:
					yp = getNetworkYPoint(strs);
					break;
			}

			if (minutePoints.size() > 0) {
				yp.calculateLoad(minutePoints.get(0));
			}

			minutePoints.add(0, yp);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	private static YPoint getAverageCPUYPoint(ArrayList<String> strs) throws Exception {
    	
    	try {
    		YPoint acpuyp = new AvgCPUYPoint();

    		for (String str: strs) {
    			 			
    			if (str.startsWith("date")) {
    				
					try {
						DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
						Date date = (Date)formatter.parse(str.replace("date ", "")); 
						Calendar cal=Calendar.getInstance();
						cal.setTime(date);
						acpuyp.setTimeStamp(cal);
						
					} catch (Exception e) {
//						e.printStackTrace();
//						System.err.println("Defaulting to current client time.");
						acpuyp.setTimeStamp(Calendar.getInstance());
					}
    			
    			}  else if (str.startsWith("cpu")) {
    				
    				String[] tokens = parseString(str);
    				
    				long procUser = Long.parseLong(tokens[1]);
    				long procNice = Long.parseLong(tokens[2]);
    				long procSystem = Long.parseLong(tokens[3]);
    				long procIdle = Long.parseLong(tokens[4]);

    				if ("cpu".equals(tokens[0])) {
    					
    					acpuyp.addVertex(new CPU(0, procUser, procNice, procSystem, procIdle));
    					break;
    				}
				}
    		}

    		return acpuyp;

    	} catch (Exception e) {
    		e.printStackTrace();
    		throw e;
    	}
    }

    
	private static YPoint getCPUYPoint(ArrayList<String> strs) throws Exception {
    	
    	try {
    		YPoint cpuhyp = new CPUYPoint();
   	
    		for (String str: strs) {
    			
    			if (str.startsWith("date")) {
    				
					try {
						DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
						Date date = (Date)formatter.parse(str.replace("date ", "")); 
						Calendar cal=Calendar.getInstance();
						cal.setTime(date);
						cpuhyp.setTimeStamp(cal);
						
					} catch (Exception e) {
//						e.printStackTrace();
//						System.err.println("Defaulting to current client time.");
						cpuhyp.setTimeStamp(Calendar.getInstance());
					}
    			
    			}  else if (str.startsWith("cpu")) {
    				
    				String[] tokens = parseString(str);

    				int cpu = 0;
    				
    				long procUser = Long.parseLong(tokens[1]);
    				long procNice = Long.parseLong(tokens[2]);
    				long procSystem = Long.parseLong(tokens[3]);
    				long procIdle = Long.parseLong(tokens[4]);

    				if (!"cpu".equals(tokens[0])) {
    					
    					cpu = Integer.parseInt(tokens[0].replace("cpu", ""));
    					cpuhyp.addVertex(new CPU(cpu, procUser, procNice, procSystem, procIdle));
    				}
    			}
    		}

    		return cpuhyp;

    	} catch (Exception e) {
    		e.printStackTrace();
    		throw e;
    	}
    }

    
	private static YPoint getMemoryYPoint(ArrayList<String> strs, Preference preference) throws Exception {
    	
    	try {
    		YPoint mhyp = new MemoryYPoint();
    		
    		long memTotal = 0;
    		long memFree = 0;
    		long buffers = 0;
    		long cached = 0;
    		long swapTotal = 0;
    		long swapFree = 0;
    		String unit = null;
    	
    		for (String str: strs) {
    			
    			if (str.startsWith("date")) {
    				
					try {
						DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
						Date date = (Date)formatter.parse(str.replace("date ", "")); 
						Calendar cal=Calendar.getInstance();
						cal.setTime(date);
						mhyp.setTimeStamp(cal);
						
					} catch (Exception e) {
//						e.printStackTrace();
//						System.err.println("Defaulting to current client time.");
						mhyp.setTimeStamp(Calendar.getInstance());
					}
    			
    			} else if (str.startsWith("MemTotal")) {
    				
    				String[] tokens = parseString(str);
    				memTotal = Long.parseLong(tokens[1]);
    				unit = tokens[2];
    				
    			}  else if (str.startsWith("MemFree")) {
    				
    				String[] tokens = parseString(str);
    				memFree = Long.parseLong(tokens[1]);
    				
    			}  else if (str.startsWith("Buffers")) {
    				
    				String[] tokens = parseString(str);
    				buffers = Long.parseLong(tokens[1]);
    				
    			}  else if (str.startsWith("Cached")) {
    				
    				String[] tokens = parseString(str);
    				cached = Long.parseLong(tokens[1]);
    				
    			}  else if (str.startsWith("SwapTotal")) {
    				
    				String[] tokens = parseString(str);
    				swapTotal = Long.parseLong(tokens[1]);
    				 
    			}  else if (str.startsWith("SwapFree")) {
    				
    				String[] tokens = parseString(str);
    				swapFree = Long.parseLong(tokens[1]);
    				
    			}
    		}
    		
    		Long freeMemory;
    		if (preference.isDisplayCacheAndBuffersAsUsed()) {
    			
    			freeMemory = memFree;
    			
    		} else {
    			
    			freeMemory = memFree + buffers + cached;
    		}
    		Long usedMemory = memTotal - freeMemory;
    		mhyp.addVertex(new Memory(Memory.PHYSICAL_MEMORY, memTotal, usedMemory, freeMemory, unit));
    		
    		usedMemory = swapTotal - swapFree;
			mhyp.addVertex(new Memory(Memory.SWAP_MEMORY, swapTotal, usedMemory, swapFree, unit));

    		return mhyp;

    	} catch (Exception e) {
    		e.printStackTrace();
    		throw e;
    	}
    }
	
	private static YPoint getNetworkYPoint(ArrayList<String> strs) throws Exception {
    	
    	try {
    		YPoint networkhyp = new NetworkYPoint();
    		
			long receiving = 0;
			long sending = 0;
   	
    		for (String str: strs) {
    			
    			if (str.startsWith("Interface")) {

    				String[] tokens = parseString(str);
    				
    				// do not add in loopback device
    				if (!tokens[1].toLowerCase().startsWith("lo")) {

    					receiving += Long.parseLong(tokens[2]);
    					sending += Long.parseLong(tokens[10]);
    				}
    			}
    		}
    		
    		networkhyp.addVertex(new Network(Network.RECEIVING, receiving));
    		networkhyp.addVertex(new Network(Network.SENDING, sending));
    		
    		return networkhyp;

    	} catch (Exception e) {
    		e.printStackTrace();
    		throw e;
    	}
    }
    
    private static String[] parseString(String str) throws Exception {
        
    	try {
			String delims = "[ ]+";
			String[] strs = str.replaceAll("\t", " ").replace("  ", " ").split(delims, 20);
			
			return strs;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
    }

	/**
	 * @return the cpuVertices
	 */
	public ArrayList<YPoint> getCpuVertices() {
		return cpuVertices;
	}

	/**
	 * @param cpuVertices the cpuVertices to set
	 */
	public void setCpuVertices(ArrayList<YPoint> cpuVertices) {
		this.cpuVertices = cpuVertices;
	}

	/**
	 * @return the avgCpuVertices
	 */
	public ArrayList<YPoint> getAvgCpuVertices() {
		return avgCpuVertices;
	}

	/**
	 * @param avgCpuVertices the avgCpuVertices to set
	 */
	public void setAvgCpuVertices(ArrayList<YPoint> avgCpuVertices) {
		this.avgCpuVertices = avgCpuVertices;
	}

	/**
	 * @return the memoryVertices
	 */
	public ArrayList<YPoint> getMemoryVertices() {
		return memoryVertices;
	}

	/**
	 * @param memoryVertices the memoryVertices to set
	 */
	public void setMemoryVertices(ArrayList<YPoint> memoryVertices) {
		this.memoryVertices = memoryVertices;
	}

	/**
	 * @return the networkVertices
	 */
	public ArrayList<YPoint> getNetworkVertices() {
		return networkVertices;
	}

	/**
	 * @param networkVertices the networkVertices to set
	 */
	public void setNetworkVertices(ArrayList<YPoint> networkVertices) {
		this.networkVertices = networkVertices;
	}
}
