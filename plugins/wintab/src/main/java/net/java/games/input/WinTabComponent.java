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

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WinTabComponent extends AbstractComponent {

	private int min;
	private int max;
	protected float lastKnownValue;
	private boolean analog;

	protected WinTabComponent(WinTabContext context, int parentDevice, String name, Identifier id, int min, int max) {
		super(name, id);
		this.min = min;
		this.max = max;
		analog = true;
	}

	protected WinTabComponent(WinTabContext context, int parentDevice, String name, Identifier id) {
		super(name, id);
		this.min = 0;
		this.max = 1;
		analog = false;
	}

	protected float poll() throws IOException {
		return lastKnownValue;
	}
	
	public boolean isAnalog() {
		return analog;
	}

	public boolean isRelative() {
		// All axis are absolute
		return false;
	}

	public static List<WinTabComponent> createComponents(WinTabContext context, int parentDevice, int axisId, int[] axisRanges) {
		List<WinTabComponent> components = new ArrayList<>();
		Identifier id;
		switch(axisId) {
		case WinTabDevice.XAxis:
			id = Identifier.Axis.X;
			components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[0], axisRanges[1]));
			break;
		case WinTabDevice.YAxis:
			id = Identifier.Axis.Y;
			components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[0], axisRanges[1]));
			break;
		case WinTabDevice.ZAxis:
			id = Identifier.Axis.Z;
			components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[0], axisRanges[1]));
			break;
		case WinTabDevice.NPressureAxis:
			id = Identifier.Axis.X_FORCE;
			components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[0], axisRanges[1]));
			break;
		case WinTabDevice.TPressureAxis:
			id = Identifier.Axis.Y_FORCE;
			components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[0], axisRanges[1]));
			break;
		case WinTabDevice.OrientationAxis:
			id = Identifier.Axis.RX;
			components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[0], axisRanges[1]));
			id = Identifier.Axis.RY;
			components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[2], axisRanges[3]));
			id = Identifier.Axis.RZ;
			components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[4], axisRanges[5]));
			break;
		case WinTabDevice.RotationAxis:
			id = Identifier.Axis.RX;
			components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[0], axisRanges[1]));
			id = Identifier.Axis.RY;
			components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[2], axisRanges[3]));
			id = Identifier.Axis.RZ;
			components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[4], axisRanges[5]));
			break;
		}
		
		return components;
	}

	public static Collection<WinTabButtonComponent> createButtons(WinTabContext context, int deviceIndex, int numberOfButtons) {
		List<WinTabButtonComponent> buttons = new ArrayList<>();
		Identifier id;
		
		for(int i=0;i<numberOfButtons;i++) {
			try {
				Class<Identifier.Button> buttonIdClass = Identifier.Button.class;
				Field idField = buttonIdClass.getField("_" + i);
				id = (Identifier)idField.get(null);
				buttons.add(new WinTabButtonComponent(context, deviceIndex, id.getName(), id, i));
			} catch (SecurityException|NoSuchFieldException|IllegalArgumentException|IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return buttons;
	}

	public Event processPacket(WinTabPacket packet) {
		// Set this to be the old value incase we don't change it		
		float newValue=lastKnownValue;
		
		if(getIdentifier()==Identifier.Axis.X) {
			newValue = normalise(packet.PK_X);
		}
		if(getIdentifier()==Identifier.Axis.Y) {
			newValue = normalise(packet.PK_Y);
		}
		if(getIdentifier()==Identifier.Axis.Z) {
			newValue = normalise(packet.PK_Z);
		}
		if(getIdentifier()==Identifier.Axis.X_FORCE) {
			newValue = normalise(packet.PK_NORMAL_PRESSURE);
		}
		if(getIdentifier()==Identifier.Axis.Y_FORCE) {
			newValue = normalise(packet.PK_TANGENT_PRESSURE);
		}
		if(getIdentifier()==Identifier.Axis.RX) {
			newValue = normalise(packet.PK_ORIENTATION_ALT);
		}
		if(getIdentifier()==Identifier.Axis.RY) {
			newValue = normalise(packet.PK_ORIENTATION_AZ);
		}
		if(getIdentifier()==Identifier.Axis.RZ) {
			newValue = normalise(packet.PK_ORIENTATION_TWIST);
		}
		if(newValue!=getPollData()) {
			lastKnownValue = newValue;
			
			//Generate an event
			Event newEvent = new Event();
			newEvent.set(this, newValue, packet.PK_TIME*1000);
			return newEvent;
		}
		
		return null;
	}
	
	private float normalise(float value) {
		if(max == min) return value;
		float bottom = max - min;
		return (value - min)/bottom;
	}

	public static Collection<WinTabCursorComponent> createCursors(WinTabContext context, int deviceIndex, String[] cursorNames) {
		Identifier id;
		List<WinTabCursorComponent> cursors = new ArrayList<>();
		
		for(int i=0;i<cursorNames.length;i++) {
			if(cursorNames[i].matches("Puck")) {
				id = Identifier.Button.TOOL_FINGER;
			} else if(cursorNames[i].matches("Eraser.*")) {
				id = Identifier.Button.TOOL_RUBBER;
			} else {
				id = Identifier.Button.TOOL_PEN;
			}
			cursors.add(new WinTabCursorComponent(context, deviceIndex, id.getName(), id, i));
		}
		
		return cursors;
	}
}
