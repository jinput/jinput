package net.java.games.input;

import net.java.games.input.Component.Identifier;

public class WinTabButtonComponent extends WinTabComponent {

	private int index;

	protected WinTabButtonComponent(WinTabContext context, int parentDevice, String name, Identifier id, int index) {
		super(context, parentDevice, name, id);
		this.index = index;
	}

	public Event processPacket(WinTabPacket packet) {
		Event newEvent = null;
		
		float newValue = ((packet.PK_BUTTONS & (int)Math.pow(2, index))>0) ? 1.0f : 0.0f; 
		if(newValue!=getPollData()) {
			lastKnownValue = newValue;
			
			//Generate an event
			newEvent = new Event();
			newEvent.set(this, newValue, packet.PK_TIME*1000);
			return newEvent;
		}
		
		return newEvent;
	}
}
