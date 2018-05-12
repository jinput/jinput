/**
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WinTabDevice extends AbstractController {
	private WinTabContext context;
	private List eventList = new ArrayList();

	private WinTabDevice(WinTabContext context, int index, String name, Component[] components) {
		super(name, components, new Controller[0], new Rumbler[0]);
		this.context = context;
	}

	protected boolean getNextDeviceEvent(Event event) throws IOException {
		if(eventList.size()>0) {
			Event ourEvent = (Event)eventList.remove(0);
			event.set(ourEvent);
			return true;
		} else {
			return false;
		}
	}
	
	protected void pollDevice() throws IOException {
		// Get the data off the native queue.
		context.processEvents();

		super.pollDevice();
	}
	
	public Type getType() {
		return Type.TRACKPAD;
	}
	
	public void processPacket(WinTabPacket packet) {
		Component[] components = getComponents();
		for(int i=0;i<components.length;i++) {
			Event event = ((WinTabComponent)components[i]).processPacket(packet);
			if(event!=null) {
				eventList.add(event);
			}
		}
	}

	public static WinTabDevice createDevice(WinTabContext context, int deviceIndex) {
		String name = nGetName(deviceIndex);
		WinTabEnvironmentPlugin.logln("Device " + deviceIndex + ", name: " + name);
		List componentsList = new ArrayList();
		
		int[] axisDetails = nGetAxisDetails(deviceIndex, WinTabComponent.XAxis);
		if(axisDetails.length==0) {
			WinTabEnvironmentPlugin.logln("ZAxis not supported");
		} else {
			WinTabEnvironmentPlugin.logln("Xmin: " + axisDetails[0] + ", Xmax: " + axisDetails[1]);
			componentsList.addAll(WinTabComponent.createComponents(context, deviceIndex, WinTabComponent.XAxis, axisDetails));
		}

		axisDetails = nGetAxisDetails(deviceIndex, WinTabComponent.YAxis);
		if(axisDetails.length==0) {
			WinTabEnvironmentPlugin.logln("YAxis not supported");
		} else {
			WinTabEnvironmentPlugin.logln("Ymin: " + axisDetails[0] + ", Ymax: " + axisDetails[1]);
			componentsList.addAll(WinTabComponent.createComponents(context, deviceIndex, WinTabComponent.YAxis, axisDetails));
		}

		axisDetails = nGetAxisDetails(deviceIndex, WinTabComponent.ZAxis);
		if(axisDetails.length==0) {
			WinTabEnvironmentPlugin.logln("ZAxis not supported");
		} else {
			WinTabEnvironmentPlugin.logln("Zmin: " + axisDetails[0] + ", Zmax: " + axisDetails[1]);
			componentsList.addAll(WinTabComponent.createComponents(context, deviceIndex, WinTabComponent.ZAxis, axisDetails));
		}

		axisDetails = nGetAxisDetails(deviceIndex, WinTabComponent.NPressureAxis);
		if(axisDetails.length==0) {
			WinTabEnvironmentPlugin.logln("NPressureAxis not supported");
		} else {
			WinTabEnvironmentPlugin.logln("NPressMin: " + axisDetails[0] + ", NPressMax: " + axisDetails[1]);
			componentsList.addAll(WinTabComponent.createComponents(context, deviceIndex, WinTabComponent.NPressureAxis, axisDetails));
		}

		axisDetails = nGetAxisDetails(deviceIndex, WinTabComponent.TPressureAxis);
		if(axisDetails.length==0) {
			WinTabEnvironmentPlugin.logln("TPressureAxis not supported");
		} else {
			WinTabEnvironmentPlugin.logln("TPressureAxismin: " + axisDetails[0] + ", TPressureAxismax: " + axisDetails[1]);
			componentsList.addAll(WinTabComponent.createComponents(context, deviceIndex, WinTabComponent.TPressureAxis, axisDetails));
		}

		axisDetails = nGetAxisDetails(deviceIndex, WinTabComponent.OrientationAxis);
		if(axisDetails.length==0) {
			WinTabEnvironmentPlugin.logln("OrientationAxis not supported");
		} else {
			WinTabEnvironmentPlugin.logln("OrientationAxis mins/maxs: " + axisDetails[0] + "," + axisDetails[1] + ", " + axisDetails[2] + "," + axisDetails[3] + ", " + axisDetails[4] + "," + axisDetails[5]);			
			componentsList.addAll(WinTabComponent.createComponents(context, deviceIndex, WinTabComponent.OrientationAxis, axisDetails));
		}

		axisDetails = nGetAxisDetails(deviceIndex, WinTabComponent.RotationAxis);
		if(axisDetails.length==0) {
			WinTabEnvironmentPlugin.logln("RotationAxis not supported");
		} else {
			WinTabEnvironmentPlugin.logln("RotationAxis is supported (by the device, not by this plugin)");
			componentsList.addAll(WinTabComponent.createComponents(context, deviceIndex, WinTabComponent.RotationAxis, axisDetails));
		}
		
		String[] cursorNames = nGetCursorNames(deviceIndex);
		componentsList.addAll(WinTabComponent.createCursors(context, deviceIndex, cursorNames));
		for(int i=0;i<cursorNames.length;i++) {
			WinTabEnvironmentPlugin.logln("Cursor " + i + "'s name: " + cursorNames[i]);
		}
		
		int numberOfButtons = nGetMaxButtonCount(deviceIndex);
		WinTabEnvironmentPlugin.logln("Device has " + numberOfButtons + " buttons");
		componentsList.addAll(WinTabComponent.createButtons(context, deviceIndex, numberOfButtons));
		
		Component[] components = (Component[])componentsList.toArray(new Component[0]);
		
		return new WinTabDevice(context, deviceIndex, name, components);
	}
	
	private final static native String nGetName(int deviceIndex);
	private final static native int[] nGetAxisDetails(int deviceIndex, int axisId);
	private final static native String[] nGetCursorNames(int deviceIndex);
	private final static native int nGetMaxButtonCount(int deviceIndex);
}
