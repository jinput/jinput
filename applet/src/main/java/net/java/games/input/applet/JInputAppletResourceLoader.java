/**
 * Copyright (C) 2003 Jeremy Booth (jeremy@newdawnsoftware.com)
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this 
 * list of conditions and the following disclaimer. Redistributions in binary 
 * form must reproduce the above copyright notice, this list of conditions and 
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. 
 * The name of the author may not be used to endorse or promote products derived
 * from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO 
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
 */
package net.java.games.input.applet;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JInputAppletResourceLoader {
	
	private static final Logger diagnosticLog = Logger.getLogger(JInputAppletResourceLoader.class.getName());
	private int percentageDone = 0;

	private String getPrivilegedProperty(final String property) {
		return (String) AccessController.doPrivileged(new PrivilegedAction() {
			public Object run() {
				return System.getProperty(property);
			}
		});
	}

	private String setPrivilegedProperty(final String property, final String value) {
		return (String) AccessController.doPrivileged(new PrivilegedAction() {
			public Object run() {
				return System.setProperty(property, value);
			}
		});
	}
	
	public void loadResources(URL codeBase) throws IOException {
		downloadNativesJar(codeBase);
		extractNativesJar(codeBase);
		setJInputClasspath(codeBase);
	}
	
	public int getPercentageDone() {
		return percentageDone;
	}

	private void setJInputClasspath(URL codeBase) {
		setPrivilegedProperty("net.java.games.input.librarypath", getTempDir(codeBase) + File.separator + "natives" + File.separator);
	}

	private void extractNativesJar(URL codeBase) throws IOException {
		File tempDir = new File(getTempDir(codeBase));
		String osName = getPrivilegedProperty("os.name");
		String nativeJar = null;
		if (osName.startsWith("Win")) {
			nativeJar = "jinput-windows-native.jar";
		} else if (osName.startsWith("Linux") || osName.startsWith("FreeBSD")) {
			nativeJar = "jinput-linux-native.jar";
		} else if (osName.startsWith("Mac")) {
			nativeJar = "jinput-osx-native.jar";
		} else {
		}
		
		JarFile localJarFile = new JarFile(new File(tempDir, nativeJar), true);
		
		Enumeration jarEntries = localJarFile.entries();
		int totalUncompressedBytes = 0;
		int totalUncompressedBytesWritten = 0;
		List entriesToUse = new ArrayList();
		
		while(jarEntries.hasMoreElements()) {
			JarEntry jarEntry = (JarEntry)jarEntries.nextElement();
			String entryName = jarEntry.getName();
			if(!entryName.startsWith("META-INF")) {
				totalUncompressedBytes+=jarEntry.getSize();
				entriesToUse.add(jarEntry);
				diagnosticLog.log(Level.INFO, "Got entry " + entryName + " " + jarEntry.getSize() + " big, total of " + totalUncompressedBytes);
			}
		}
		
		File tempNativesDir = new File(tempDir, "natives");
		if(!tempNativesDir.exists()) {
			tempNativesDir.mkdirs();
			tempNativesDir.deleteOnExit();
		}
		
		for(int i=0;i<entriesToUse.size();i++) {
			JarEntry jarEntry = (JarEntry) entriesToUse.get(i);
			InputStream inStream = localJarFile.getInputStream(localJarFile.getEntry(jarEntry.getName()));
			File nativeFile = new File(tempNativesDir, jarEntry.getName());
			FileOutputStream fos = new FileOutputStream(nativeFile);
			
			byte[] dataBuffer = new byte[65535];
			int bytesRead = 0;
			
			// Read the first block
			bytesRead = inStream.read(dataBuffer, 0, dataBuffer.length);
			while(bytesRead!=-1) {
				
				fos.write(dataBuffer, 0, bytesRead);
				
				totalUncompressedBytesWritten+=bytesRead;
				
				int unpackingPercentageDone = (int)((((float)totalUncompressedBytesWritten)/totalUncompressedBytes)*100);
				percentageDone = 50 + (unpackingPercentageDone/2); //Reading the file is only 1/2 the job
				diagnosticLog.log(Level.INFO, "Written " + totalUncompressedBytesWritten + " out of " + totalUncompressedBytes + " " + unpackingPercentageDone + "%, (" + percentageDone + "% total)");
				
				// Read the next block
				bytesRead = inStream.read(dataBuffer, 0, dataBuffer.length);
			}
			long entryModifiedTime = jarEntry.getTime();
			nativeFile.setLastModified(entryModifiedTime);
			diagnosticLog.log(Level.INFO, "Setting native modified time to " + new Date(entryModifiedTime));
			fos.close();
			inStream.close();
		}
	}

	private void downloadNativesJar(URL codeBase) throws IOException {
		diagnosticLog.log(Level.INFO, "codebase: " + codeBase);
		File tempDir = new File(getTempDir(codeBase));
		if(!tempDir.exists()) {
			tempDir.mkdirs();
			tempDir.deleteOnExit();
		}
		
		String osName = getPrivilegedProperty("os.name");
		String nativeJar = null;
		if (osName.startsWith("Win")) {
			nativeJar = "jinput-windows-native.jar";
		} else if (osName.startsWith("Linux") || osName.startsWith("FreeBSD")) {
			nativeJar = "jinput-linux-native.jar";
		} else if (osName.startsWith("Mac")) {
			nativeJar = "jinput-osx-native.jar";
		} else {
		}
		
		File localJarFile = new File(tempDir, nativeJar);
		localJarFile.deleteOnExit();
		
		diagnosticLog.log(Level.INFO, "Using local file " + localJarFile.getCanonicalPath());
		
		OutputStream outStream = new FileOutputStream(localJarFile);
		outStream = new BufferedOutputStream(outStream);
		
		URL remoteJarURL = new URL(codeBase, nativeJar);
		
		diagnosticLog.log(Level.INFO, "Using remote file " + remoteJarURL);
		
		URLConnection connection = remoteJarURL.openConnection();
		connection.setUseCaches(false);	
		connection.setConnectTimeout(2000);
		
		int contentLength = connection.getContentLength();
		
		diagnosticLog.log(Level.INFO, "remote jar is " + contentLength + " bytes");

		InputStream inStream = connection.getInputStream();
		byte[] dataBuffer = new byte[65535];
		int bytesRead = 0;
		int totalRead = 0;
		int totalWritten = 0;
		
		// Read the first block
		bytesRead = inStream.read(dataBuffer, 0, dataBuffer.length);
		while(bytesRead!=-1) {
			//Update total read
			totalRead+=bytesRead;
			
			outStream.write(dataBuffer, 0, bytesRead);
			
			totalWritten+=bytesRead;
			
			int thisFilePercent = (int)((((float)totalRead)/contentLength)*100);
			percentageDone = thisFilePercent/2; //Reading the file is only 1/2 the job
			diagnosticLog.log(Level.INFO, "Written " + totalRead + " out of " + contentLength + " " + thisFilePercent + "%, (" + percentageDone + "% total)");
			
			// Read the next block
			bytesRead = inStream.read(dataBuffer, 0, dataBuffer.length);
		}
		
		inStream.close();
		outStream.close();
	}
	
	private String getTempDir(URL codeBase) {
		return getPrivilegedProperty("java.io.tmpdir") + File.separator + codeBase.getHost() + File.separator + "jinput";
	}

}
