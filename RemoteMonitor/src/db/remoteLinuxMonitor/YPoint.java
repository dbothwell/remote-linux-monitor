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
import java.util.ArrayList;
import java.util.Calendar;

public interface YPoint {
	
	/**
	 * @return the title
	 */
	public String getTitle();
	
	/**
	 * @return the timeStamp
	 */
	public Calendar getTimeStamp();
	
	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(Calendar timeStamp);
	
	/**
	 * @param vertex - vertex to add to the vertices array
	 */
	public void addVertex(Vertex vertex);
	
	/**
	 * @return the vertices
	 */
	public ArrayList<Vertex> getVertices();

	/**
	 * @param vertices the vertices to set
	 */
	public void setVertices(ArrayList<Vertex> vertices);

	/**
	 * @param index - index of vertex
	 * @return Color for a given vertex
	 */
	public Color getColorAtIndex(int index);

	public void setColorAtIndex(int index, Color color);
	
	/**
	 * The load is the Y axis point for the the graph
	 * @param yPoint - old plot point needed to calaculate load
	 */
	public void calculateLoad(YPoint oldYPoint);
	
	public String toString();
}
