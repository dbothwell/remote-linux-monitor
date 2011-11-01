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

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class MemoryYPoint implements YPoint, Serializable {
	
	public static Color[] COLORS = { new Color(0x008B45), new Color(0x800080) };

	public static final String TITLE = "Memory History";
	private Calendar timeStamp = null;
	private ArrayList<Vertex> memories = new ArrayList<Vertex>();

	@Override
	public String getTitle() {
		return TITLE;
	}

	@Override
	public void calculateLoad(YPoint oldYPoint) {

		for (int i = 0; i < memories.size(); i++) {
			((Memory) memories.get(i)).calculateLoad(oldYPoint.getVertices().get(i));
		}
	}

	@Override
	public Color getColorAtIndex(int index) {

		return COLORS[index];
	}

	@Override
	public Calendar getTimeStamp() {

		return timeStamp;
	}

	@Override
	public ArrayList<Vertex> getVertices() {

		return memories;
	}

	@Override
	public void setVertices(ArrayList<Vertex> vertices) {

		memories = vertices;
	}

	@Override
	public void setTimeStamp(Calendar timeStamp) {

		this.timeStamp = timeStamp;
	}

	@Override
	public void addVertex(Vertex vertex) {

		memories.add(vertex);	
	}


	@Override
	public void setColorAtIndex(int index, Color color) {
		
		COLORS[index] = color;
	}

	
	public String toString() {
		
		StringBuilder sb = new StringBuilder(TITLE + " " + timeStamp.getTime() + "\n");

		for (Vertex memory: memories) {
			sb.append("\tAverage CPU: " + memory + "\n");
		}

		return sb.toString();
	}

}
