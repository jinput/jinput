/*
 * %W% %E%
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * Copyright (c) 2003 Sun Microsystems, Inc.  All Rights Reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistribution of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materails provided with the distribution.
 *
 * Neither the name Sun Microsystems, Inc. or the names of the contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANT OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMEN, ARE HEREBY EXCLUDED.  SUN MICROSYSTEMS, INC. ("SUN") AND
 * ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS
 * A RESULT OF USING, MODIFYING OR DESTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES.  IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES.  HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OUR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed or intended for us in
 * the design, construction, operation or maintenance of any nuclear facility
 *
 *****************************************************************************/
package net.java.games.input;

/**
 * A AbstractControllerEnvironment represents a collection of controllers that are
 * physically or logically linked.  By default, this corresponds to the
 * environment for the local machine.
 * <p>
 * In this reference implementation, this class can also be used to register
 * controllers with the default environment as "plug-ins".  A plug-in is
 * created by subclassing AbstractControllerEnvironment with a class that has a public
 * no-argument constructor, implements the net.java.games.util.plugins.Plugin
 * interface and has a name ending in "Plugin".
 * (See net.java.games.input.DirectInputEnvironmentPlugin in the DXplugin
 *  part of the source tree for an example.)
 *
 * When the DefaultControllerEnvrionment is instanced it uses the plugin library
 * to look for Plugins in both [java.home]/lib/controller and
 * [user.dir]/controller.  This allows controller plugins to be installed either
 * globally for the entire Java environment or locally for just one particular
 * Java app.
 *
 * For more information on the organization of plugins within the controller
 * root directories, see net.java.games.util.plugins.Plugins (Note the
 * plural -- "Plugins" not "Plugin" which is just a marker interface.)
 *
 */
public interface ControllerEnvironment {

	public final static class Defaults {

		private Defaults() {}

	    /**
	     * The default controller environment
	     */
	    private static ControllerEnvironment defaultEnvironment =
	        new DefaultControllerEnvironment();
	}

    /**
     * Returns the default environment for input controllers.
     * This usually corresponds to the environment for the local machine.
     */
    public static ControllerEnvironment getDefaultEnvironment() {
        return Defaults.defaultEnvironment;
    }
	/**
	 * Returns a list of all controllers available to this environment,
	 * or an empty array if there are no controllers in this environment.
	 */
	Controller[] getControllers();

	/**
	 * Adds a listener for controller state change events.
	 */
	void addControllerListener(ControllerListener l);

	/**
	 * Returns the isSupported status of this environment.
	 * What makes an environment supported or not is up to the
	 * particular plugin, but may include OS or available hardware.
	 */
	boolean isSupported();

	/**
	 * Removes a listener for controller state change events.
	 */
	void removeControllerListener(ControllerListener l);
}
