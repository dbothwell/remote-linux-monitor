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

public class SystemInfo {
	
//	private static final String COMMAND = "uname -srm; uname -n; df -lh | grep -m 1 \" /\"$; grep \"MemTotal\" /proc/meminfo; grep \"^processor\\|^model name\"  /proc/cpuinfo";
	private static final String COMMAND = "uname -srm; uname -n; df -h | grep -m 1 \" /\"$; grep \"MemTotal\" /proc/meminfo; grep \"^processor\\|^model name\"  /proc/cpuinfo";
	
	private String kernelName;
	private String computerName;
	private String diskSpace;
	private String availableMemory;
	private ArrayList<String> processors = new ArrayList<String>();
	
	public static SystemInfo factory(SSHSession sshSession) throws Exception {
		
		try {
//			System.out.println(COMMAND);
			
			SystemInfo systemInfo = new SystemInfo();

			ArrayList<String> strs = sshSession.getStandardOutput(COMMAND);
			
			String delims = "[ ]+";
			String[] tokens;
			String processorNumber = "0";
			String processorName = "";
			
			for (int i = 0; i < strs.size(); i++) {
				
				switch(i) {
				
					case 0:
						
						systemInfo.setKernelName(strs.get(i));
						break;
						
					case 1:
						
						systemInfo.setComputerName(strs.get(i));
						break;
						
					case 2:
						
						tokens = strs.get(i).replaceAll("\t", " ").replace("  ", " ").split(delims, 20);
	    				
						systemInfo.setDiskSpace("Available disk space on / (root): " + tokens[3]);
						break;
						
					case 3:

						tokens = strs.get(i).replaceAll("\t", " ").replace("  ", " ").split(delims, 20);
						
						Double memory = Double.parseDouble(tokens[1]);
						
						if (tokens[2].toLowerCase().startsWith("k")) {
							
							memory /= 1000000L;
						
						} else if (tokens[0].toLowerCase().startsWith("m")) {
							
							memory /= 1000L;
						} 
						
						systemInfo.setAvailableMemory(String.format("Memory:     %1.2fG", memory));
						break;
						
					default:
						
						tokens = strs.get(i).replaceAll("\t", " ").replace("  ", " ").split(delims, 20);
						
						if (tokens[0].toLowerCase().equals("processor")) {
							
							processorNumber = tokens[2];
						
						} else if (tokens[0].toLowerCase().equals("model")) {
							
							processorName = strs.get(i).replace("model name", "").replace(":", "").trim();
							systemInfo.processors.add(String.format("Processor %s: %s", processorNumber, processorName));
						}
				}
			}

			return systemInfo;
			
		} catch (Exception e) {

			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * @return the kernelName
	 */
	public String getKernelName() {
		return kernelName;
	}

	/**
	 * @param kernelName the kernelName to set
	 */
	public void setKernelName(String kernelName) {
		this.kernelName = kernelName;
	}

	/**
	 * @return the computerName
	 */
	public String getComputerName() {
		return computerName;
	}

	/**
	 * @param computerName the computerName to set
	 */
	public void setComputerName(String computerName) {
		this.computerName = computerName;
	}

	/**
	 * @return the diskSpace
	 */
	public String getDiskSpace() {
		return diskSpace;
	}

	/**
	 * @param diskSpace the diskSpace to set
	 */
	public void setDiskSpace(String diskSpace) {
		this.diskSpace = diskSpace;
	}

	/**
	 * @return the availableMemory
	 */
	public String getAvailableMemory() {
		return availableMemory;
	}

	/**
	 * @param availableMemory the availableMemory to set
	 */
	public void setAvailableMemory(String availableMemory) {
		this.availableMemory = availableMemory;
	}

	/**
	 * @return the processors
	 */
	public ArrayList<String> getProcessors() {
		return processors;
	}

	/**
	 * @param processors the processors to set
	 */
	public void setProcessors(ArrayList<String> processors) {
		this.processors = processors;
	}
}
