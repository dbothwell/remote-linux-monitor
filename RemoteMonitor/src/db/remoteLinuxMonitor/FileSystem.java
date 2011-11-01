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

public class FileSystem {
	
	public static final int COLUMN_DEVICE = 0;
	public static final int COLUMN_DIRECTORY = 1;
	public static final int COLUMN_TYPE = 2;
	public static final int COLUMN_SIZE = 3;
	public static final int COLUMN_USED = 4;
	public static final int COLUMN_AVAILABLE = 5;
	public static final int COLUMN_USAGE_PERCENT = 6;
	
	private String device;
	private String directory;
	private String type;
	private String size;
	private String available;
	private String used;
	private int usagePercent;
	

	public static String[] getColumnHeaders() {
		
		String[] headers = { "Device", "Directory", "Type", "Size", "Used", "Available", "Usage" };

		return headers;
	}

	public static int[] getColumnWidths() {
		
		int[] widths = { 70, 50, 50, 30, 30, 40, 200 };

		return widths;
	}
	
	/**
	 * @return the device
	 */
	public String getDevice() {
		return device;
	}

	/**
	 * @param device the device to set
	 */
	public void setDevice(String device) {
		this.device = device;
	}

	/**
	 * @return the directory
	 */
	public String getDirectory() {
		return directory;
	}

	/**
	 * @param directory the directory to set
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * @return the available
	 */
	public String getAvailable() {
		return available;
	}

	/**
	 * @param available the available to set
	 */
	public void setAvailable(String available) {
		this.available = available;
	}

	/**
	 * @return the used
	 */
	public String getUsed() {
		return used;
	}

	/**
	 * @param used the used to set
	 */
	public void setUsed(String used) {
		this.used = used;
	}

	/**
	 * @return the usagePercent
	 */
	public int getUsagePercent() {
		return usagePercent;
	}

	/**
	 * @param usagePercent the usagePercent to set
	 */
	public void setUsagePercent(int usagePercent) {
		this.usagePercent = usagePercent;
	}
}
