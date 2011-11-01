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

public class ProcessInfo {
	
	private static final String COMMAND = "uptime; ps -eo comm,euser,stat,vsz,rss,%cpu,cputime,start_time,nice,pid,label,%mem,wchan:14,ppid,args";

	private String uptime;
	private ArrayList<Process> processes = new ArrayList<Process>();
	
	public static ProcessInfo factory(SSHSession sshSession, SSHUserInfo userInfo) throws Exception {
		
		try {
//			System.out.println(COMMAND);
			
			ProcessInfo processInfo = new ProcessInfo();

			ArrayList<String> strs = sshSession.getStandardOutput(COMMAND);
			
			String delims = "[ ]+";
			String[] tokens;

			
			for (int i = 0; i < strs.size(); i++) {
				
				Process process= new Process();
				
				if (i == 0) {
					
					tokens = strs.get(i).trim().replaceAll("\t", " ").replace("  ", " ").split(delims, 20);
					processInfo.setUptime(strs.get(i).replace(tokens[0], " ").replace(" up ", " Uptime: ").replace("load average:", "load average for 1, 5, 15 minutes:").trim());					
				
				} else if (i > 1) {
				
					tokens = strs.get(i).replaceAll("\t", " ").replace("  ", " ").split(delims, 20);

					process.setProcessName(tokens[0]);
					
					if (tokens[1].equals(userInfo.getUid())) {
						
						process.setUser(userInfo.getUser());	
						
					} else {
					
						process.setUser(tokens[1]);				
					}
					process.setStatus(Process.getStatusDescription(tokens[2]));
					
					try {
						process.setVirtualMemory(Long.parseLong(tokens[3]));
							
					} catch (Exception e) {

						process.setVirtualMemory(0);
					}
					
					try {
						process.setResidentMemory(Long.parseLong(tokens[4]));
							
					} catch (Exception e) {

						process.setResidentMemory(0);
					}
					
					try {
						process.setCpuUsage(Float.parseFloat(tokens[5]));
							
					} catch (Exception e) {

						process.setCpuUsage(0);
					}
					
					process.setCpuTime(tokens[6]);
					process.setStartTime(tokens[7]);
					
					try {
						process.setNice(Integer.parseInt(tokens[8]));
							
					} catch (Exception e) {

						process.setNice(0);
					}
					
					try {
						process.setPid(Long.parseLong(tokens[9]));
							
					} catch (Exception e) {

						process.setPid(0);
					}

					process.setSecurityLabel(tokens[10]);
					
					try {
						process.setMemUsage(Float.parseFloat(tokens[11]));
							
					} catch (Exception e) {

						process.setMemUsage(0);
					}
					
					process.setWaitChannel(tokens[12]);
					
					try {
						process.setParentPid(Long.parseLong(tokens[13]));
							
					} catch (Exception e) {

						process.setParentPid(0);
					}
					
					StringBuilder sb = new StringBuilder();
					for (int j = 14; j < tokens.length; j++) {
						
						sb.append(tokens[j]);
						sb.append(" ");
					}
					process.setCommandline(sb.toString());
					
					processInfo.getProcesses().add(process);
				}
			}

			return processInfo;
			
		} catch (Exception e) {

			e.printStackTrace();
			throw e;
		}

	}
	
	/**
	 * @return the uptime
	 */
	public String getUptime() {
		return uptime;
	}

	/**
	 * @param uptime the uptime to set
	 */
	public void setUptime(String uptime) {
		this.uptime = uptime;
	}

	/**
	 * @return the processes
	 */
	public ArrayList<Process> getProcesses() {
		return processes;
	}

	/**
	 * @param processes the processes to set
	 */
	public void setProcesses(ArrayList<Process> processes) {
		this.processes = processes;
	}
}
