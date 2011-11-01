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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHSession {
	
	public static final  int DEFAULT_PORT = 22; 
	
    private JSch jsch = new JSch();  
    private Session session = null;
    
    public String login(SSHUserInfo sshUserInfo, SSHOptions sshOptions) throws JSchException {
    	

			try {
				session = jsch.getSession(sshUserInfo.getUser(), sshUserInfo.getHost(), sshOptions.getPort());
				
				java.util.Properties config = new java.util.Properties(); 
				config.put("StrictHostKeyChecking", "no");
				session.setConfig(config);

				session.setUserInfo(sshUserInfo);
				session.connect();
				
				return sshUserInfo.getUser() + "@" + sshUserInfo.getHost();
				
			} catch (JSchException e) {
				e.printStackTrace();
				throw e;
			}
    }
    
    public boolean isConnected() {
    	
    	return (session != null) && session.isConnected();
    }
    
    public void disconnect() throws Exception {
    	

		try {
			
			if (isConnected()) {

				session.disconnect();
			}
			
		} catch (Exception e) {

			e.printStackTrace();
			throw e;
		}
    }
    
//    public ArrayList<String> getStandardOutput(String COMMAND) throws Exception {
//    	
//    	Channel channel = null;
//
//    	try {
//    		ArrayList<String> strs = new ArrayList<String>();
//
//    		channel = session.openChannel("exec");
//
//    		((ChannelExec)channel).setCommand(COMMAND);
//    		channel.setInputStream(null);
////    		((ChannelExec)channel).setErrStream(System.out);
//
//    		InputStream in = channel.getInputStream();
//
//    		channel.connect();
//
//    		String line;
//
//    		BufferedReader br = new BufferedReader(new InputStreamReader(in));
//    		while ((line = br.readLine()) != null) {
//    			strs.add(line);
////    			System.out.println(line);
//    		}
//
//    		channel.disconnect();
//    		return strs;
//
//    	}
//    	catch (Exception e) {
//    		
//    		channel.disconnect();
//    		e.printStackTrace();
//    		throw e;
//    	}
//    }

    public ArrayList<String> getStandardOutput(String COMMAND) throws Exception {
    	
    	Channel channel = null;

    	try {
    		ArrayList<String> strs = new ArrayList<String>();
    		ArrayList<String> errStrs = new ArrayList<String>();

    		channel = session.openChannel("exec");

    		((ChannelExec)channel).setCommand(COMMAND);
    		channel.setInputStream(null);

    		InputStream in = channel.getInputStream();
    		InputStream err = channel.getExtInputStream();

    		channel.connect();

    		String line;
    		BufferedReader br = new BufferedReader(new InputStreamReader(in));
    		while ((line = br.readLine()) != null) {
    			strs.add(line);
//    			System.out.println(line);
    		}

    		String errLine;

    		BufferedReader errBr = new BufferedReader(new InputStreamReader(err));
    		while ((errLine = errBr.readLine()) != null) {
    			errStrs.add(errLine);
//    			System.out.println(errLine);
    		}

    		channel.disconnect();
    		
    		if (errStrs.size() > 0) {
    			
    			throw new Exception(errStrs.toString());
    		}
    		
    		return strs;

    	}
    	catch (Exception e) {
    		
    		channel.disconnect();
    		e.printStackTrace();
    		throw e;
    	}
    }

	/**
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}
}
