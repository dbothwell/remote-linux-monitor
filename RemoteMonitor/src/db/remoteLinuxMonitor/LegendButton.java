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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.plaf.basic.BasicButtonUI;

public class LegendButton extends JButton implements MouseListener, ActionListener {
	
	private LegendPanel legendPanel = null;
	private int index = 0;
	
	public LegendButton(Vertex vertex, Color color, LegendPanel legendPanel) {
		super();
		
		this.legendPanel = legendPanel;
		this.index = vertex.getIndex();
		
        setBackground(color);
        this.setUI(new BasicButtonUI());
        this.setForeground(Color.white);
        
        setFont(new Font("Monospaced", Font.BOLD, 13));

        setText(vertex.getLegendText());
        
		setRolloverEnabled(false);
		setFocusable(false);
        setContentAreaFilled(false);
        setOpaque(true);

        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1),
        		BorderFactory.createLineBorder(Color.lightGray)));

        addMouseListener(this);
        addActionListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		setBorder(BorderFactory.createRaisedBevelBorder());
	}

	@Override
	public void mouseExited(MouseEvent e) {
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1),
        		BorderFactory.createLineBorder(Color.lightGray)));

	}

	@Override
	public void mousePressed(MouseEvent e) {
		setBorder(BorderFactory.createLoweredBevelBorder());	
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		setBorder(BorderFactory.createRaisedBevelBorder());
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		Color color = getBackground();
		
	    color = JColorChooser.showDialog(
                this.getParent().getParent().getParent(),
                "Choose Graph Color",
                color);
	    
	    if (color != null) {
	    	legendPanel.setColorAtIndex(index, color);
	    	setBackground(color);
	    }
	}
}
