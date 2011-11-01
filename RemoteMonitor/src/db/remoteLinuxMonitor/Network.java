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

public class Network implements Vertex, Serializable {
	
	public static final int RECEIVING = 0;
	public static final int SENDING = 1;
	
	private static final String[] typeDesc = {"Receiving", "Sending" };
	
	private int type = RECEIVING;
	
	private long totalBytes;
	private long bytesPerSecond;

	@Override
	public int getIndex() {

		return type;
	}
	
	public Network(int type, long totalBytes) {
		
		super();
		this.type = type;
		this.totalBytes = totalBytes;
	}

	@Override
	public double getYPosition() {

		return (double) bytesPerSecond;
	}

	@Override
	public void calculateLoad(Vertex oldVertex) {

		Network oldNetwork = (Network) oldVertex;
		bytesPerSecond = totalBytes - oldNetwork.getTotalBytes();
	}

	@Override
	public String getLegendText() {
		
		String bytesPerSecondString = getBytesString(bytesPerSecond);
		String totalBytesString = getBytesString(totalBytes);
		
		String text = String.format(" %s %s/Sec Total: %s ", typeDesc[type], bytesPerSecondString, totalBytesString);
		
		return text;
	}
	
	private String getBytesString(long bytes) {
		
		String text;
		
		if (bytes > 1000000000) {
			
			text = String.format("%6.1f GB", (double) bytes/1000000000);
		
		} else if (bytes > 1000000) {
			
			text = String.format("%6.1f MB", (double) bytes/1000000);
		
		} else if (bytes > 1000) {
			
			text = String.format("%6.1f KB", (double) bytes/1000);
		
		} else {
			
			text = String.format("%3d Bytes", bytes);
		}
		
		return text;
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
	}

	/**
	 * @return the totalBytes
	 */
	public long getTotalBytes() {
		return totalBytes;
	}

	/**
	 * @param totalBytes the totalBytes to set
	 */
	public void setTotalBytes(long totalBytes) {
		this.totalBytes = totalBytes;
	}

	/**
	 * @return the bytesPerSecond
	 */
	public long getBytesPerSecond() {
		return bytesPerSecond;
	}

	/**
	 * @param bytesPerSecond the bytesPerSecond to set
	 */
	public void setBytesPerSecond(long bytesPerSecond) {
		this.bytesPerSecond = bytesPerSecond;
	}

	public String toString() {
		return "type = " + type + ", Total Bytes = " + totalBytes + " Bytes/sec = " + bytesPerSecond; 
	}
}
