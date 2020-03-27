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
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// id -ur; id -un

//cat /proc/net/dev	

//kill -STOP pid
//kill -CONT pid
//kill -TERM pid
//kill -KILL pid

//renice 10 -p 21827

public class RemoteMain extends JFrame implements WindowListener {

	public static final String DEFAULT_FONT = "Dialog";
	
	public static final int ACTIVE_PROCESS = 0;
	public static final int ALL_PROCESS = 1;
	public static final int MY_PROCESS = 2;
	
	protected static final String TITLE = "Remote Linux Monitor";
	
	private static final int TAB_INDEX_PROCESS = 1;
	
	protected static int TIMER_DELAY_PROCESSES = 3000;
	private Timer timerProcesses = null;

	private ResourceInfo resourceInfo = new ResourceInfo();
	protected static int TIMER_DELAY_RESOURCES = 1000;
	private Timer timerResources = null;
	
	protected static int TIMER_DELAY_FILE_SYSTEMS = 5000;
	private Timer timerFileSystems = null;
	
	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	private PanelSystem panelSystem;
	private PanelFileSystems panelFileSystems;
	private PanelProcesses panelProcesses;
	private PanelResources panelResources;
	
	private JMenuItem mntmConnect;
	private JMenuItem mntmDisconnect;
	private JButton buttonConnect;
	
	private JMenuItem mntmStopProcess;
	private JMenuItem mntmContinueProcess;
	private JMenuItem mntmEndProcess;
	private JMenuItem mntmKillProcess;
	private JMenuItem mntmChangePriority;
	private JRadioButtonMenuItem rdbtnmntmActiveProcesses;
	private JRadioButtonMenuItem rdbtnmntmAllProcesses;
	private JRadioButtonMenuItem rdbtnmntmMyProcesses;
	private JButton buttonEndProcess;
	
	private JMenuItem mntmRefresh;
	
	private SSHUserInfo sshUserInfo = new SSHUserInfo();
	private SSHSession sshSession = new SSHSession();
	private SSHOptions sshOptions = new SSHOptions();
	
	private Preference preference = new Preference();
	
	/**
	 * Create the frame.
	 */
	public RemoteMain() throws Exception {
		
		try {
			setIconImage(Toolkit.getDefaultToolkit().getImage(RemoteMain.class.getResource("/images/binocular_48x48.png")));
			
			initializeMenu();
			initialize();
			toggleConnected();
			
			timerProcesses = new Timer(TIMER_DELAY_PROCESSES, runProcesses);
			timerProcesses.setInitialDelay(0);
			
			timerResources = new Timer(TIMER_DELAY_RESOURCES, runResources);
			timerResources.setInitialDelay(0);
			
			timerFileSystems = new Timer(TIMER_DELAY_FILE_SYSTEMS, runFileSystems);
			timerFileSystems.setInitialDelay(0);
			
			updatePreference();
			
		} catch (Exception e) {

			e.printStackTrace();
			throw e;
		}
	}
	
	private void initializeMenu() {
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnMonitor = new JMenu("Monitor");
		menuBar.add(mnMonitor);
		
		mntmConnect = new JMenuItem("Connect");
		mntmConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					login();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		mnMonitor.add(mntmConnect);
		
		mntmDisconnect = new JMenuItem("Disconnect");
		mntmDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					
					stop();
					sshSession.disconnect();
					toggleConnected();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		mnMonitor.add(mntmDisconnect);

		mnMonitor.addSeparator();
		
		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				
				RemoteMain.this.dispose();
			}
		});
		
		mntmSSHOptions = new JMenuItem("SSH Options");
		mntmSSHOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				
				(new SSHOptionsDialog(RemoteMain.this, sshOptions)).setVisible(true);
			}
		});
		mnMonitor.add(mntmSSHOptions);
		
		separator_4 = new JSeparator();
		mnMonitor.add(separator_4);
		mnMonitor.add(mntmQuit);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		mntmStopProcess = new JMenuItem("Stop Process");
		mntmStopProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
			
				try {
					runKillCommand("-STOP");
					
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		});
		mnEdit.add(mntmStopProcess);
		
		mntmContinueProcess = new JMenuItem("Continue Process");
		mntmContinueProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				
				try {
					runKillCommand("-CONT");
					
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		});
		mnEdit.add(mntmContinueProcess);
		
		JSeparator separator = new JSeparator();
		mnEdit.add(separator);
		
		mntmEndProcess = new JMenuItem("End Process");
		mntmEndProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				
				try {
					runKillCommand("-TERM");
					
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		});
		mnEdit.add(mntmEndProcess);
		
		mntmKillProcess = new JMenuItem("Kill Process");
		mntmKillProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				
				try {
					runKillCommand("-KILL");
					
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		});
		mnEdit.add(mntmKillProcess);
		
		JSeparator separator_1 = new JSeparator();
		mnEdit.add(separator_1);
		
		mntmChangePriority = new JMenuItem("Change Priority");
		mntmChangePriority.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				
				try {
					PriorityDialog priorityDialog = new PriorityDialog(RemoteMain.this);
					int nice = priorityDialog.showDialog(panelProcesses.getSelectedNice());

					if (nice >= -20) {
						
						long pid = panelProcesses.getSelectedProcessId();
						if (pid > -1) {
							
							String command = "renice " + Integer.toString(nice) + " -p  " + Long.toString(pid);
							
							try {
								sshSession.getStandardOutput(command);
								
							} catch (Exception e) {
								
						        JOptionPane.showMessageDialog(RemoteMain.this, e.getMessage(), "Error",
						            JOptionPane.ERROR_MESSAGE);
							}
							
							updatePreference();
						}
					}
					
				} catch (Exception e) {

					e.printStackTrace();
				}


			}
		});
		mnEdit.add(mntmChangePriority);
		
		JSeparator separator_2 = new JSeparator();
		mnEdit.add(separator_2);
		
		JMenuItem mntmPreferences = new JMenuItem("Preferences");
		mntmPreferences.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					(new PreferenceDialog(RemoteMain.this, preference)).setVisible(true);
					
				} catch (Exception e1) {

					e1.printStackTrace();
				}
			}
		});
		mnEdit.add(mntmPreferences);
		
		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);
		
		rdbtnmntmActiveProcesses = new JRadioButtonMenuItem("Active Processes");
		rdbtnmntmActiveProcesses.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				
				try {
					updatePreference();
					
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		});
		rdbtnmntmActiveProcesses.setSelected(false);
		mnView.add(rdbtnmntmActiveProcesses);
		
		rdbtnmntmAllProcesses = new JRadioButtonMenuItem("All Processes");
		rdbtnmntmAllProcesses.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				
				try {
					updatePreference();

				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		});
		rdbtnmntmAllProcesses.setSelected(true);
		mnView.add(rdbtnmntmAllProcesses);
		
		rdbtnmntmMyProcesses = new JRadioButtonMenuItem("My Processes");
		rdbtnmntmMyProcesses.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				
				try {
					updatePreference();
					
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		});
		rdbtnmntmMyProcesses.setSelected(false);
		mnView.add(rdbtnmntmMyProcesses);
		
		ButtonGroup group = new ButtonGroup();
	    group.add(rdbtnmntmActiveProcesses);
	    group.add(rdbtnmntmAllProcesses);
	    group.add(rdbtnmntmMyProcesses);
		
		JSeparator separator_3 = new JSeparator();
		mnView.add(separator_3);
		
		mntmRefresh = new JMenuItem("Refresh");
		mntmRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				
				try {
					updatePreference();
					
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		});
		mnView.add(mntmRefresh);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				
				try {
					(new AboutDialog(RemoteMain.this)).setVisible(true);
					
				} catch (Exception e1) {

					e1.printStackTrace();
				}
			}
		});
		
		mntmThirdPartyLibraries = new JMenuItem("Third Party Libraries");
		mntmThirdPartyLibraries.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				
				(new HtmlViewerDialog(RemoteMain.this, "Third Party Libraries", "/images/ThirdPartyLibraries.html")).setVisible(true);
			}
		});
		mnHelp.add(mntmThirdPartyLibraries);
		
		separator_5 = new JSeparator();
		mnHelp.add(separator_5);
		mnHelp.add(mntmAbout);
	}
	
	private void initialize() {
		
		setTitle(TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		
		this.addWindowListener(this);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		tabbedPane.addChangeListener(new ChangeListener() {
			
			public void stateChanged(ChangeEvent changeEvent) {

				setProcessMenuItemsEnabled();
			}
		});
		
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panelSystemBorder = new JPanel(new BorderLayout(0, 0));
		
		tabbedPane.addTab("System", null, panelSystemBorder, null);
		
		panelSystem = new PanelSystem();
		panelSystemBorder.add(panelSystem, BorderLayout.CENTER);
		
		JPanel panelSystemSouth = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panelSystemSouth.getLayout();
		flowLayout.setHgap(10);
		flowLayout.setAlignment(FlowLayout.RIGHT);
		panelSystemBorder.add(panelSystemSouth, BorderLayout.SOUTH);
		
		buttonConnect = new JButton("Connect");
		buttonConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					if (sshSession.isConnected()) {
						
						stop();
						sshSession.disconnect();
						toggleConnected();
						
					} else {
						
						login();	
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		buttonConnect.setFocusable(false);
		panelSystemSouth.add(buttonConnect);
		
		JPanel panelProcessBorder = new JPanel(new BorderLayout(0, 0));
		tabbedPane.addTab("Processes", null, panelProcessBorder, null);
		
		panelProcesses = new PanelProcesses(this);
		panelProcessBorder.add(panelProcesses, BorderLayout.CENTER);
		
		JPanel panelProcessSouth = new JPanel();
		FlowLayout processFlowLayout = (FlowLayout) panelProcessSouth.getLayout();
		processFlowLayout.setHgap(10);
		processFlowLayout.setAlignment(FlowLayout.RIGHT);
		panelProcessBorder.add(panelProcessSouth, BorderLayout.SOUTH);
		
		buttonEndProcess = new JButton("End Process");
		buttonEndProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				
				try {
					runKillCommand("-TERM");
					
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		});
		buttonEndProcess.setFocusable(false);
		panelProcessSouth.add(buttonEndProcess);

		
		panelResources = new PanelResources();
		tabbedPane.addTab("Resources", null, panelResources, null);
		
		panelFileSystems = new PanelFileSystems();
		tabbedPane.addTab("File Systems", null, panelFileSystems, null);
	}
	
	private void login() throws Exception {

		try {
			stop();
			sshSession.disconnect();

			LoginDialog loginDialog = new LoginDialog(RemoteMain.this, sshUserInfo);
			int result = loginDialog.showDialog();

			if (result == JOptionPane.OK_OPTION) {

				sshSession.login(sshUserInfo, sshOptions);
				toggleConnected();
				start();
			}

		} catch (Exception e) {
			sshUserInfo.setPassword(null);
			sshUserInfo.setPassphrase(null);

//			sshUserInfo = new SSHUserInfo();
			JOptionPane.showMessageDialog(this, "Failed to connect.\nReason: " + e.getMessage(), "Connection error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			throw e;
		}
	}
	
	private void autoLogin() throws Exception {

		try {
			stop();
			sshSession.disconnect();

			sshSession.login(sshUserInfo, sshOptions);
			toggleConnected();
			start();

		} catch (Exception e) {

//			sshUserInfo = new SSHUserInfo();
			JOptionPane.showMessageDialog(this, "Failed to connect.\nReason: " + e.getMessage(), "Connection error", JOptionPane.ERROR_MESSAGE);
			login();
		}
	}
	
	private void toggleConnected() {
		
		setProcessMenuItemsEnabled();
		
		if (sshSession.isConnected()) {
			
			mntmConnect.setEnabled(false);
			mntmDisconnect.setEnabled(true);
			buttonConnect.setText("Disconnect");
			
			mntmRefresh.setEnabled(true);
			buttonEndProcess.setEnabled(true);
			
		} else {
			
			mntmConnect.setEnabled(true);
			mntmDisconnect.setEnabled(false);
			buttonConnect.setText("Connect");
			
			mntmRefresh.setEnabled(false);
			buttonEndProcess.setEnabled(false);
		}
	}
	

	/**
	 * @return the sshUserInfo
	 */
	public SSHUserInfo getUserInfo() {
		return sshUserInfo;
	}

	/**
	 * @param sshUserInfo the sshUserInfo to set
	 */
	public void setUserInfo(SSHUserInfo sshUserInfo) {
		this.sshUserInfo = sshUserInfo;
	}
	
	/**
	 * @return the sshOptions
	 */
	public SSHOptions getSshOptions() {
		return sshOptions;
	}

	/**
	 * @param sshOptions the sshOptions to set
	 */
	public void setSshOptions(SSHOptions sshOptions) {
		this.sshOptions = sshOptions;
	}

	private void start() throws Exception {
		
		try {
			
			panelResources.panelReset();

			resourceInfo.clear();
			
			SystemInfo systemInfo = SystemInfo.factory(sshSession);
			panelSystem.display(systemInfo);
			
			if (systemInfo.getComputerName() != null) {
				
				setTitle(TITLE + " : " + systemInfo.getComputerName());
				
			} else {
				
				setTitle(TITLE);
			}
			
			timerProcesses.start();
			timerResources.start();
			timerFileSystems.start();
			
			updateUserInfo();
			updatePreference();
			
		} catch (Exception e) {

			e.printStackTrace();
			throw e;
		}
	}
	
	private void stop() throws Exception {
		
		try {
			timerProcesses.stop();
			timerResources.stop();
			timerFileSystems.stop();
			
		} catch (Exception e) {

			e.printStackTrace();
			throw e;
		}
	}
	
	private Action runProcesses = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			try {

				ProcessInfo processinfo = ProcessInfo.factory(sshSession, sshUserInfo);
				panelProcesses.display(processinfo);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	};
	
	private Action runResources = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			try {

				resourceInfo = ResourceInfo.addPlotPoints(sshSession, resourceInfo, preference);
				panelResources.display(resourceInfo);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	};
	
	private Action runFileSystems = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			try {

				FileSystemInfo fileSystemInfo = FileSystemInfo.factory(sshSession);
				panelFileSystems.display(fileSystemInfo);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	};
	private JMenuItem mntmSSHOptions;
	private JSeparator separator_4;
	private JMenuItem mntmThirdPartyLibraries;
	private JSeparator separator_5;
	
	protected void updatePreference() throws Exception {
		
		try {
			timerProcesses.setDelay(preference.getProcessUpdateInterval() * 1000);
			timerResources.setDelay(Preference.getResourceUpdateinterval() * 1000);
			timerFileSystems.setDelay(preference.getFileSystemsUpdateInterval() * 1000);
			
			panelProcesses.setPreference(preference);
			panelResources.setPreference(preference);
			panelFileSystems.setPreference(preference);
			
			if (sshSession.isConnected()) {

				FileSystemInfo fileSystemInfo = FileSystemInfo.factory(sshSession);
				panelFileSystems.display(fileSystemInfo);
				
				ProcessInfo processinfo = ProcessInfo.factory(sshSession, sshUserInfo);
				panelProcesses.display(processinfo);
			}
			
			setProcessMenuItemsEnabled();
			
			repaint();
			
		} catch (Exception e) {

			e.printStackTrace();
			throw e;
		}
	}
	
	protected int getSelectedTabIndex() {
		
		return tabbedPane.getSelectedIndex();
	}
	
	protected void setProcessMenuItemsEnabled() {
		
		boolean enabled = (tabbedPane.getSelectedIndex() == TAB_INDEX_PROCESS) && sshSession.isConnected();
		
		rdbtnmntmActiveProcesses.setEnabled(enabled);
		rdbtnmntmAllProcesses.setEnabled(enabled);
		rdbtnmntmMyProcesses.setEnabled(enabled);
		
		if ((panelProcesses != null) && panelProcesses.isRowSelected() && enabled) {
			
			mntmStopProcess.setEnabled(true);
			mntmContinueProcess.setEnabled(true);
			mntmEndProcess.setEnabled(true);
			mntmKillProcess.setEnabled(true);
			mntmChangePriority.setEnabled(true);
		
		} else { 
			
			mntmStopProcess.setEnabled(false);
			mntmContinueProcess.setEnabled(false);
			mntmEndProcess.setEnabled(false);
			mntmKillProcess.setEnabled(false);
			mntmChangePriority.setEnabled(false);
		}
	}
	
	private void runKillCommand(String killArg) throws Exception {
		
		try {
			long pid = panelProcesses.getSelectedProcessId();
			if (pid > -1) {
				
				String command = "kill " + killArg + " " + Long.toString(pid);
				
				try {
					
					if ((killArg.equals("-TERM") || killArg.equals("-KILL")) && preference.isAlertBeforeKillingProcess()) {
					
						int result = JOptionPane.showConfirmDialog(
							    this,
							    "Are you sure you want to terminate this process?",
							    "Confirm",
							    JOptionPane.OK_CANCEL_OPTION);
	
						if (result == JOptionPane.OK_OPTION) {
						
							sshSession.getStandardOutput(command);
						}
						
					} else {
						
						sshSession.getStandardOutput(command);
					}
					
				} catch (Exception e) {
					
			        JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
			            JOptionPane.ERROR_MESSAGE);
				}
				
				updatePreference();
			}
			
		} catch (Exception e) {

			e.printStackTrace();
			throw e;
		}
	}

	private void  updateUserInfo() throws Exception {
		
		try {
			String command = "id -un; id -ur";
			ArrayList<String> strs = sshSession.getStandardOutput(command);
			
			for (int i = 0; i < strs.size(); i++) {
				
				switch (i) {
						
					case 0:
						sshUserInfo.setUser(strs.get(i).trim());
						continue;
						
					case 1:
						sshUserInfo.setUid(strs.get(i).trim());
						continue;
				}
			}
			
		} catch (Exception e) {

			e.printStackTrace();
			throw e;
		}
	}

	public int showProcessesType() {
		
		if (rdbtnmntmActiveProcesses.isSelected()) {
			
			return ACTIVE_PROCESS;
		
		} else if (rdbtnmntmAllProcesses.isSelected()) {
			
			return ALL_PROCESS;
		
		} else if (rdbtnmntmMyProcesses.isSelected()) {
			
			return MY_PROCESS;
		}
		
		return ALL_PROCESS;
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {

	}

	@Override
	public void windowClosed(WindowEvent e) {
		try {
			stop();
			sshSession.disconnect();
			System.exit(0);
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {
		
		try {
			stop();
			sshSession.disconnect();
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowOpened(WindowEvent e) {

	}
	
	/**
	 * Launch the application.
	 */
	public static void main(final String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {

				try {
					String host = SSHUserInfo.DEFAULT_HOST;
					String user = SSHUserInfo.DEFAULT_USER;
					String password = null;
					String passphrase = null; 
					String identityFile = null;
					int port = SSHSession.DEFAULT_PORT;
					int tabIndex = -1;
					boolean useNativeLAndF = false;
					
					boolean attemptLogin = false;
					
					// position
					int left = -1;
					int width = -1;
					int top = -1;
					int height = -1;
					
					
					for (int i = 0; i < args.length; i++) {
						
						if (args[i].equals("-h") || args[i].equals("--help") || args[i].equals("?")) {
							
							displayCommandLineHelp();
							
						} else if (args[i].equals("-n") || args[i].equals("--native")) {
							
							useNativeLAndF = true;
							
						} else if ((args[i].equals("-H") || args[i].equals("--host")) && (i + 1 < args.length)) {
							
							host = args[++i];
							
						} else if ((args[i].equals("-u") || args[i].equals("--user")) && (i + 1 < args.length)) {
							
							user = args[++i];
							
						} else if ((args[i].equals("-p") || args[i].equals("--password")) && (i + 1 < args.length)) {
							
							password = args[++i];
							attemptLogin = true;
							
						} else if ((args[i].equals("-i") || args[i].equals("--identity-file")) && (i + 1 < args.length)) {
							
							identityFile = args[++i];
							attemptLogin = true;
							
						} else if ((args[i].equals("-P") || args[i].equals("--passphrase")) && (i + 1 < args.length)) {
							
							passphrase = args[++i];
							attemptLogin = true;
							
						}  else if ((args[i].equals("-s") || args[i].equals("--ssh_port") || args[i].equals("--ssh-port")) && (i + 1 < args.length)) {
							
							try {
								port = Integer.parseInt(args[++i]);
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else if (args[i].equals("--left") && (i + 1 < args.length)) {
							try {
								left = Integer.parseInt(args[++i]);
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else if (args[i].equals("--top") && (i + 1 < args.length)) {
							try {
								top = Integer.parseInt(args[++i]);
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else if (args[i].equals("--width") && (i + 1 < args.length)) {
							try {
								width = Integer.parseInt(args[++i]);
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}else if (args[i].equals("--height") && (i + 1 < args.length)) {
							try {
								height = Integer.parseInt(args[++i]);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						else if (args[i].equals("--tabIndex") && (i + 1 < args.length)) {
							try {
								tabIndex = Integer.parseInt(args[++i]);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}						
						else {
							
							System.err.println("Invalid command line argument: " + args[i]);
						}
					}

					
					if (!useNativeLAndF) {

						for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
							
					        if ("Nimbus".equals(info.getName())) {
					        	
//					        	UIManager.put( "nimbusBase", new Color(121,119,100) ); //dark base
//					        	UIManager.put( "nimbusBlueGrey", new Color(171,169,150) ); //middle base
//					            UIManager.put( "control", new Color(221,219,200) ); // light base
//					            UIManager.put( "nimbusSelectionBackground", new Color(208,175,42) );
//					            UIManager.put( "nimbusFocus", new Color(228,195,62) );
//					            UIManager.put( "nimbusOrange", new Color(198,165,32) );
					            
					        	UIManager.put( "nimbusBase", new Color(48, 119, 227) ); //dark base
					        	UIManager.put( "nimbusBlueGrey", new Color(164, 172, 186) ); //middle base
					        	UIManager.put( "control", new Color(225, 229, 235) );
					        	UIManager.put( "nimbusSelectionBackground", new Color(48, 119, 227) );
					        	UIManager.put( "nimbusFocus", new Color(48, 119, 227) );
					        	
					            UIManager.setLookAndFeel(info.getClassName());
					            break;
					        }
					    }
					}

					RemoteMain frame = new RemoteMain();
					
					frame.getUserInfo().setHost(host);
					frame.getUserInfo().setUser(user);
					frame.getUserInfo().setPassword(password);
					frame.getUserInfo().setPassphrase(passphrase);
					frame.getSshOptions().setPort(port);
					frame.getSshOptions().setIdentityFile(identityFile);
					
					if (width != -1 && height != -1 && top != -1 && left != -1)
					{
						// set size
						Dimension size = frame.getSize();
						size.setSize(width, size.getHeight());
						size.setSize(size.getWidth(), height);
						frame.setSize(size);
						
						
						// set position
						Point position = frame.getLocation();
						position.setLocation(position.getLocation().getX(), top);
						position.setLocation(left, position.getLocation().getY());
						frame.setLocation(position);
					}
					
					if (tabIndex != -1)
					{
						frame.tabbedPane.setSelectedIndex(tabIndex);											
					}
					
					
					frame.setVisible(true);
					
					if (attemptLogin) {
						frame.autoLogin();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private static void displayCommandLineHelp() {
		
		String usage = "Usage: java -jar remote-linux-monitor.jar [-h] [-n] [-H hostname] [-u username] [-p password] [-s ssh-port] [-P passphrase] [-i --identity-file]";
		String flags[] = { "-H --host : set hostname", 
				"-u --user : set username", 
				"-p --password : set password", 
				"-i --identity-file : set identity file",
				"-P --passphrase : set passphrase",
				"-s --ssh-port : set ssh port",
				"-n --native : set native look & feel",
				"--width --height --top --left : set size and position (all parameters required)",
				"--tabIndex: show tab with index (0-3)",
				"-h --help ? : Display this help message"};
		
		System.out.println(usage);
		System.out.println();
		for (String flag: flags) {
			System.out.println(flag);
		}
		System.out.println();
	}
}
