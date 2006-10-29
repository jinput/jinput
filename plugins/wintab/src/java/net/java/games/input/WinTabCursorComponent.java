package net.java.games.input;

import net.java.games.input.Component.Identifier;

public class WinTabCursorComponent extends WinTabComponent {

	private int index;

	protected WinTabCursorComponent(WinTabContext context, int parentDevice, String name, Identifier id, int index) {
		super(context, parentDevice, name, id);
		this.index = index;
	}

	public Event processPacket(WinTabPacket packet) {
		Event newEvent = null;
		if(packet.PK_CURSOR==index && lastKnownValue==0) {
			lastKnownValue = 1;
			newEvent = new Event();
			newEvent.set(this, lastKnownValue, packet.PK_TIME*1000);
		} else if(packet.PK_CURSOR!=index && lastKnownValue==1) {
			lastKnownValue = 0;
			newEvent = new Event();
			newEvent.set(this, lastKnownValue, packet.PK_TIME*1000);
		}
		
		return newEvent;
	}
}
