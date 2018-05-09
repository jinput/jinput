/**
 * Copyright (C) 2007 Jeremy Booth (jeremy@newdawnsoftware.com)
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

/**
 * Combines the list of seperate keyboards and mice found with the raw plugin,
 * with the game controllers found with direct input.
 * 
 * @author Jeremy
 */
public class DirectAndRawInputEnvironmentPlugin extends ControllerEnvironment {

	private RawInputEnvironmentPlugin rawPlugin;
	private DirectInputEnvironmentPlugin dinputPlugin;
	private Controller[] controllers = null;
	
	public DirectAndRawInputEnvironmentPlugin() {
		// These two *must* be loaded in this order for raw devices to work.
		dinputPlugin = new DirectInputEnvironmentPlugin();
		rawPlugin = new RawInputEnvironmentPlugin();	
	}
	
	/**
	 * @see net.java.games.input.ControllerEnvironment#getControllers()
	 */
	public Controller[] getControllers() {
		if(controllers == null) {
			boolean rawKeyboardFound = false;
			boolean rawMouseFound = false;
			List tempControllers = new ArrayList();
			Controller[] dinputControllers = dinputPlugin.getControllers();
			Controller[] rawControllers = rawPlugin.getControllers();
			for(int i=0;i<rawControllers.length;i++) {
				tempControllers.add(rawControllers[i]);
				if(rawControllers[i].getType()==Controller.Type.KEYBOARD) {
					rawKeyboardFound = true;
				} else if(rawControllers[i].getType()==Controller.Type.MOUSE) {
					rawMouseFound = true;
				}
			}
			for(int i=0;i<dinputControllers.length;i++) {
				if(dinputControllers[i].getType()==Controller.Type.KEYBOARD) {
					if(!rawKeyboardFound) {
						tempControllers.add(dinputControllers[i]);
					}
				} else if(dinputControllers[i].getType()==Controller.Type.MOUSE) {
					if(!rawMouseFound) {
						tempControllers.add(dinputControllers[i]);
					}
				} else {
					tempControllers.add(dinputControllers[i]);
				}
			}
			
			controllers = (Controller[]) tempControllers.toArray(new Controller[]{});
		}
		
		return controllers;
	}

	/**
	 * @see net.java.games.input.ControllerEnvironment#isSupported()
	 */
	public boolean isSupported() {
		return rawPlugin.isSupported() || dinputPlugin.isSupported();
	}

}
