/*
 * Copyright (C) 2006 Jeremy Booth (jeremy@newdawnsoftware.com)
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
package net.java.games.input;

import java.util.ArrayList;
import java.util.List;

public class WinTabContext {

	private DummyWindow window;
	private long hCTX;
	private Controller[] controllers;

	public WinTabContext(DummyWindow window) {
		this.window = window;
	}
	
	public Controller[] getControllers() {
		if(hCTX==0) {
			throw new IllegalStateException("Context must be open before getting the controllers");
		}
		return controllers;
	}
	
	public synchronized void open() {
		this.hCTX = nOpen(window.getHwnd());
		List<WinTabDevice> devices = new ArrayList<>();
		
		int numSupportedDevices = nGetNumberOfSupportedDevices();
		for(int i=0;i<numSupportedDevices;i++) {
			devices.add(WinTabDevice.createDevice(this,i));
		}
		
		controllers = devices.toArray(new Controller[0]);
	}
	
	public synchronized void close() {
		nClose(hCTX);
	}
	
	public synchronized void processEvents() {
		WinTabPacket[] packets = nGetPackets(hCTX);
		for(int i=0;i<packets.length;i++) {
			// TODO I can't seem to find a way to identify which device the packet is for
			// This is not good.
			// NASTY HACK based of assumptions that might very well be wrong
			// Just send it to the first device
			((WinTabDevice)(getControllers()[0])).processPacket(packets[i]);
		}
	}

	private static native int nGetNumberOfSupportedDevices();
	private static native long nOpen(long hwnd);
	private static native void nClose(long hCtx);
	private static native WinTabPacket[] nGetPackets(long hCtx);

}
