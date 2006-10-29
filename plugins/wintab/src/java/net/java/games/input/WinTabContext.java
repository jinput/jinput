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
		ControllerEnvironment.logln("Opening context");
		this.hCTX = nOpen(window.getHwnd());
		List devices = new ArrayList();
		
		int numSupportedDevices = nGetNumberOfSupportedDevices();
		ControllerEnvironment.logln(numSupportedDevices + " devices maximum supported");
		for(int i=0;i<numSupportedDevices;i++) {
			WinTabDevice newDevice = WinTabDevice.createDevice(this,i);
			if(newDevice!=null) {
				devices.add(newDevice);
			}
		}
		
		controllers = (Controller[])devices.toArray(new Controller[0]);
	}
	
	public synchronized void close() {
		ControllerEnvironment.logln("Closing context");
		nClose(hCTX);
	}
	
	public synchronized void processEvents() {
		WinTabPacket[] packets = nGetPackets(hCTX);
		//ControllerEnvironment.logln("Packets read: " + packets.length);
		for(int i=0;i<packets.length;i++) {
			//ControllerEnvironment.logln("Packet time: " + packets[i].PK_TIME);
			//ControllerEnvironment.logln("Packet x: " + packets[i].PK_X);
			//ControllerEnvironment.logln("Packet y: " + packets[i].PK_Y);
			//ControllerEnvironment.logln("Packet z: " + packets[i].PK_Z);
			//ControllerEnvironment.logln("Packet buttons: " + packets[i].PK_BUTTONS);
			//ControllerEnvironment.logln("Packet cursor: " + packets[i].PK_CURSOR);
			//ControllerEnvironment.logln("Packet Normal Pressure: " + packets[i].PK_NORMAL_PRESSURE);
			//ControllerEnvironment.logln("Packet Tangent Pressure: " + packets[i].PK_TANGENT_PRESSURE);
			//ControllerEnvironment.logln("Packet Alt: " + packets[i].PK_ORIENTATION_ALT);
			//ControllerEnvironment.logln("Packet Az: " + packets[i].PK_ORIENTATION_AZ);
			//ControllerEnvironment.logln("Packet Twist: " + packets[i].PK_ORIENTATION_TWIST);
			
			// TODO I can't seem to find a way to identify which device the packet is for
			// This is not good.
			// NASTY HACK based of assumptions that might very well be wrong
			// Just send it to the first device
			((WinTabDevice)(getControllers()[0])).processPacket(packets[i]);
		}
	}

	private final static native int nGetNumberOfSupportedDevices();
	private final static native long nOpen(long hwnd);
	private final static native void nClose(long hCtx);
	private final static native WinTabPacket[] nGetPackets(long hCtx);

}
