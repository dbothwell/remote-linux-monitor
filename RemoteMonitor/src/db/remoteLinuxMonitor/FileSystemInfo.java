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

public class FileSystemInfo {
	
	private static final String COMMAND = "df -lahTP";

	private ArrayList<FileSystem> fileSystems = new ArrayList<FileSystem>();
	
	public static FileSystemInfo factory(SSHSession sshSession) throws Exception {
		
		try {
//			System.out.println(COMMAND);
			
			FileSystemInfo fileSystemInfo = new FileSystemInfo();

			ArrayList<String> strs = sshSession.getStandardOutput(COMMAND);
			
			String delims = "[ ]+";
			String[] tokens;

			
			for (int i = 0; i < strs.size(); i++) {
				
				FileSystem fileSystem= new FileSystem();
				
//				System.out.println(strs.get(i));
				
				if (i > 0) {
				
					tokens = strs.get(i).replaceAll("\t", " ").replace("  ", " ").split(delims, 20);
					
					fileSystem.setDevice(tokens[0]);
					fileSystem.setType(tokens[1]);
					fileSystem.setSize(tokens[2]);
					fileSystem.setUsed(tokens[3]);
					fileSystem.setAvailable(tokens[4]);
					fileSystem.setDirectory(tokens[6]);
					
					try {
						
						fileSystem.setUsagePercent(Integer.parseInt(tokens[5].replace("%", "")));
						
					} catch (Exception e) {

						fileSystem.setUsagePercent(0);
					}
					
					fileSystemInfo.getFileSystems().add(fileSystem);
				}
			}

			return fileSystemInfo;
			
		} catch (Exception e) {

			e.printStackTrace();
			throw e;
		}

	}

	/**
	 * @return the fileSystems
	 */
	public ArrayList<FileSystem> getFileSystems() {
		return fileSystems;
	}

	/**
	 * @param fileSystems the fileSystems to set
	 */
	public void setFileSystems(ArrayList<FileSystem> fileSystems) {
		this.fileSystems = fileSystems;
	}
}
