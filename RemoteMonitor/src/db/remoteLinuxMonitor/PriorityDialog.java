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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PriorityDialog extends JDialog implements ChangeListener {

	private final JPanel contentPanel = new JPanel();
	private JLabel labelNice;
	private JSlider slider;
	private int value = -21;

	/**
	 * Create the dialog.
	 */
	public PriorityDialog(Frame owner) {
		
		super(owner);
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		
		setBounds(100, 100, 375, 165);
		setLocationRelativeTo(owner);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(null);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			labelNice = new JLabel("Nice value 0: Normal Priority");
			labelNice.setFont(new Font("Dialog", Font.PLAIN, 12));
			GridBagConstraints gbc_labelNice = new GridBagConstraints();
			gbc_labelNice.insets = new Insets(20, 0, 5, 0);
			gbc_labelNice.gridx = 0;
			gbc_labelNice.gridy = 0;
			contentPanel.add(labelNice, gbc_labelNice);
		}
		{
			slider = new JSlider();
			slider.addChangeListener(this);
			slider.setFont(new Font("Dialog", Font.PLAIN, 12));
			slider.setPaintLabels(true);
			slider.setMajorTickSpacing(20);
			slider.setValue(0);
			slider.setMinimum(-20);
			slider.setMaximum(20);
			GridBagConstraints gbc_slider = new GridBagConstraints();
			gbc_slider.insets = new Insets(5, 10, 10, 10);
			gbc_slider.weightx = 1.0;
			gbc_slider.fill = GridBagConstraints.HORIZONTAL;
			gbc_slider.gridx = 0;
			gbc_slider.gridy = 1;
			contentPanel.add(slider, gbc_slider);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new MatteBorder(1, 0, 0, 0, (Color) Color.LIGHT_GRAY));
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						value = slider.getValue();
						PriorityDialog.this.dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						PriorityDialog.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public int showDialog(int value) {
		
		slider.setValue(value);
		setNicelabel(value);
	    setVisible(true);
	    return this.value;
	}


	@Override
	public void stateChanged(ChangeEvent evt) {

		JSlider source = (JSlider) evt.getSource();

		if (source.getValueIsAdjusting()) {
        	
        	setNicelabel((int) source.getValue());
        }   
	}
	
	private void setNicelabel(int value) {
        
		String priority = "";
		
        if (value <= -8) {
        	
        	priority = "Very High Priority";
        
        } else if ((value >= -7) && (value <= -3)) {
        	
        	priority = "High Priority";
        
        } else if ((value >= -2) && (value <= 2)) {
        	
        	priority = "Normal Priority";
        
        } else if ((value >= 3) && (value <= 6)) {
        	
        	priority = "Low Priority";
        
        } else if (value >= 7) {
        	
        	priority = "Very Low Priority";
        }

        labelNice.setText(String.format("Nice value %2d: %s", value, priority));
	}
}
