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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;


public class LoginDialog extends JDialog {
	
	private JPanel contentPane;
	private JPanel panelMain;
	private JLabel labelHostName;
	private JTextField textFieldHostName;
	private JLabel labelPassword;
	private JPasswordField passwordField;
	
	private JPanel panelButton;
	private JButton buttonOk;
	private JButton buttonCancel;
	
	private String host;
	private String user;
	private String passwd;
	
	public String localFile;
	public String remoteFile;	
	
	private int result = JOptionPane.CANCEL_OPTION;
	
	private SSHUserInfo userInfo;

	public String getPassword() {
		return passwd;
	}
	
	public void showMessage(String message) {
	}
	
	/**
	 * @param owner
	 */
	public LoginDialog(Frame owner, SSHUserInfo userInfo) {
		
		super(owner);
		this.userInfo = userInfo;
		
		initialize();
		setLocationRelativeTo(owner);
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {

		setBounds(100, 100, 375, 165);
		
		setTitle("SSH Login");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setModal(true);
		
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		
		panelMain = new JPanel();
		panelMain.setBorder(new MatteBorder(1, 0, 0, 0, (Color) Color.LIGHT_GRAY));
		panelMain.setLayout(new GridBagLayout());		
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.insets = new Insets(10, 30, 3, 3);
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridy = 0;
		labelHostName = new JLabel();
		labelHostName.setText("user@hostname");
		labelHostName.setFont(new Font("Dialog", Font.PLAIN, 12));
		panelMain.add(labelHostName, gbc);
		
		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.fill = GridBagConstraints.BOTH;
		gbc1.gridy = 0;
		gbc1.weightx = 1.0;
		gbc1.insets = new Insets(10, 3, 3, 30);
		gbc1.gridx = 1;		
		textFieldHostName = new JTextField(userInfo.getUser() + "@" + userInfo.getHost());
		panelMain.add(textFieldHostName, gbc1);
		
		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = 0;
		gbc2.insets = new Insets(3, 30, 3, 3);
		gbc2.anchor = GridBagConstraints.EAST;
		gbc2.gridy = 1;
		labelPassword = new JLabel();
		labelPassword.setText("Password/Passphrase");
		labelPassword.setFont(new Font("Dialog", Font.PLAIN, 12));
		panelMain.add(labelPassword, gbc2);
		
		GridBagConstraints gbc4 = new GridBagConstraints();
		gbc4.fill = GridBagConstraints.BOTH;
		gbc4.gridy = 1;
		gbc4.weightx = 1.0;
		gbc4.insets = new Insets(3, 3, 3, 30);
		gbc4.gridx = 1;		
		passwordField = new JPasswordField();
		panelMain.add(passwordField, gbc4);
		
		GridBagConstraints gbc6 = new GridBagConstraints();
		gbc6.fill = GridBagConstraints.HORIZONTAL;
		gbc6.gridy = 3;
		gbc6.weightx = 1.0;
		gbc6.weighty = 0.0;
		gbc6.gridwidth = 2;
		gbc6.insets = new Insets(0, 0, 0, 0);
		gbc6.gridx = 0;		
		panelButton = new JPanel();
		panelButton.setLayout(new FlowLayout());
		panelButton.setBorder(new MatteBorder(1, 0, 0, 0, (Color) Color.LIGHT_GRAY));
		
		buttonOk = new JButton("Ok");
		buttonOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				
				try {
					user = textFieldHostName.getText().substring(0, textFieldHostName.getText().indexOf('@'));
					host = textFieldHostName.getText().substring(textFieldHostName.getText().indexOf('@')+1);
					passwd = new String(passwordField.getPassword());
					
					userInfo = new SSHUserInfo(host, user, passwd, passwd);
					
					((RemoteMain) LoginDialog.this.getParent()).setUserInfo(userInfo);
					result = JOptionPane.OK_OPTION;
					LoginDialog.this.dispose();
					
				} catch (Exception e1) {

					e1.printStackTrace();
				}
			}
		});		
		panelButton.add(buttonOk, null);
		
		buttonCancel = new JButton("Cancel");
		buttonCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				LoginDialog.this.dispose();
			}
		});		
		panelButton.add(buttonCancel, null);	
		contentPane.add(panelButton, BorderLayout.SOUTH);

		contentPane.add(panelMain, BorderLayout.CENTER);
		this.getRootPane().setDefaultButton(buttonOk);
		this.setContentPane(contentPane);
	}
	
	public int showDialog() {

	    setVisible(true);
	    return result;
	}
}

