/**
 * Copyright (C) 2004 Jeremy Booth (jeremy@newdawnsoftware.com)
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

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;

import java.util.List;
import java.util.ArrayList;

import java.io.IOException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author Jeremy
 * @author elias
 */
final class AWTKeyboard extends Keyboard implements AWTEventListener {
	private final List awt_events = new ArrayList();
	private Event[] processed_events;
	private int processed_events_index;
    
    protected AWTKeyboard() {
        super("AWTKeyboard", createComponents(), new Controller[]{}, new Rumbler[]{});
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);
		resizeEventQueue(EVENT_QUEUE_DEPTH);
	}

	private final static Component[] createComponents() {
		List components = new ArrayList();
		Field[] vkey_fields = KeyEvent.class.getFields();
		for (int i = 0; i < vkey_fields.length; i++) {
			Field vkey_field = vkey_fields[i];
			try {
				if (Modifier.isStatic(vkey_field.getModifiers()) && vkey_field.getType() == int.class &&
						vkey_field.getName().startsWith("VK_")) {
					int vkey_code = vkey_field.getInt(null);
					Component.Identifier.Key key_id = AWTKeyMap.mapKeyCode(vkey_code);
					if (key_id != Component.Identifier.Key.UNKNOWN)
						components.add(new Key(key_id));
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		components.add(new Key(Component.Identifier.Key.RCONTROL));
		components.add(new Key(Component.Identifier.Key.LCONTROL));
		components.add(new Key(Component.Identifier.Key.RSHIFT));
		components.add(new Key(Component.Identifier.Key.LSHIFT));
		components.add(new Key(Component.Identifier.Key.RALT));
		components.add(new Key(Component.Identifier.Key.LALT));
		components.add(new Key(Component.Identifier.Key.NUMPADENTER));
		components.add(new Key(Component.Identifier.Key.RETURN));
		components.add(new Key(Component.Identifier.Key.NUMPADCOMMA));
		components.add(new Key(Component.Identifier.Key.COMMA));
		return (Component[])components.toArray(new Component[]{});
	}

	private final void resizeEventQueue(int size) {
		processed_events = new Event[size];
		for (int i = 0; i < processed_events.length; i++)
			processed_events[i] = new Event();
		processed_events_index = 0;
	}
	
	protected final void setDeviceEventQueueSize(int size) throws IOException {
		resizeEventQueue(size);
	}

    public final synchronized void eventDispatched(AWTEvent event) {
        if (event instanceof KeyEvent)
			awt_events.add(event);
	}

	public final synchronized void pollDevice() throws IOException {
		for (int i = 0; i < awt_events.size(); i++) {
			KeyEvent event = (KeyEvent)awt_events.get(i);
			processEvent(event);
        }
		awt_events.clear();
	}

	private final void processEvent(KeyEvent event) {
		Component.Identifier.Key key_id = AWTKeyMap.map(event);
		if (key_id == null)
			return;
		Key key = (Key)getComponent(key_id);
		if (key == null)
			return;
		long nanos = event.getWhen()*1000000L;
		if (event.getID() == KeyEvent.KEY_PRESSED) {
			//the key was pressed
			addEvent(key, 1, nanos);
		} else if (event.getID() == KeyEvent.KEY_RELEASED) {
			KeyEvent nextPress = (KeyEvent)Toolkit.getDefaultToolkit().getSystemEventQueue().peekEvent(KeyEvent.KEY_PRESSED);
			if ((nextPress == null) || (nextPress.getWhen() != event.getWhen())) {
				//the key came really came up
				addEvent(key, 0, nanos);
			}
		}
    }

	private final void addEvent(Key key, float value, long nanos) {
		key.setValue(value);
		if (processed_events_index < processed_events.length)
			processed_events[processed_events_index++].set(key, value, nanos);
	}

	protected final synchronized boolean getNextDeviceEvent(Event event) throws IOException {
		if (processed_events_index == 0)
			return false;
		processed_events_index--;
		event.set(processed_events[0]);
		Event tmp = processed_events[0];
		processed_events[0] = processed_events[processed_events_index];
		processed_events[processed_events_index] = tmp;
		return true;
	}


	private final static class Key extends AbstractComponent {
		private float value;
		
		public Key(Component.Identifier.Key key_id) {
			super(key_id.getName(), key_id);
		}

		public final void setValue(float value) {
			this.value = value;
		}
		
		protected final float poll() {
			return value;
		}

		public final boolean isAnalog() {
			return false;
		}

		public final boolean isRelative() {
			return false;
		}                    
	}
}
