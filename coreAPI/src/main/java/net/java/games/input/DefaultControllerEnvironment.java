/*
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
 */
package net.java.games.input;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The default controller environment.
 *
 * @version %I% %G%
 * @author Michael Martak
 */
class DefaultControllerEnvironment extends AbstractControllerEnvironment {
	static String libPath;

	private static Logger log = Logger.getLogger(DefaultControllerEnvironment.class.getName());

	/**
	 * Static utility method for loading native libraries.
	 * It will try to load from either the path given by
	 * the net.java.games.input.librarypath property
	 * or through System.loadLibrary().
	 *
	 */
	static void loadLibrary(final String lib_name) {
		String lib_path = System.getProperty("net.java.games.input.librarypath");
		if (lib_path != null)
			System.load(lib_path + File.separator + System.mapLibraryName(lib_name));
		else
			System.loadLibrary(lib_name);
	}

    /**
     * List of all controllers in this environment
     */
    private ArrayList<Controller> controllers;


    /**
     * Public no-arg constructor.
     */
    public DefaultControllerEnvironment() {
    }

    /**
     * Returns a list of all controllers available to this environment,
     * or an empty array if there are no controllers in this environment.
     */
    public Controller[] getControllers() {
        if (controllers == null) {

            //Check the properties for specified controller classes
            String pluginClasses = System.getProperty("jinput.plugins", "") + " " + System.getProperty("net.java.games.input.plugins", "");

			if(!System.getProperty("jinput.useDefaultPlugin", "true").toLowerCase().trim().equals("false") && !System.getProperty("net.java.games.input.useDefaultPlugin", "true").toLowerCase().trim().equals("false")) {
				String osName = System.getProperty("os.name", "").trim();

				switch(osName) {
					case "Linux": {
						pluginClasses = pluginClasses + " net.java.games.input.linux.LinuxEnvironmentPlugin";
						break;
					}

					case "Mac OS X": {
						pluginClasses = pluginClasses + " net.java.games.input.osx.OSXEnvironmentPlugin";
						break;
					}

					case "Windows 98":
					case "Windows 2000": {
						pluginClasses = pluginClasses + " net.java.games.input.windows.DirectInputEnvironmentPlugin";
						break;
					}

					case "Windows XP":
					case "Windows Vista":
					case "Windows 7":
					case "Windows 8":
					case "Windows 8.1":
					case "Windows 10":
					case "Windows 11": {
						pluginClasses = pluginClasses + " net.java.games.input.windows.DirectAndRawInputEnvironmentPlugin";
						break;
					}

					default: {
						if (osName.startsWith("Windows")) {
							log.warning("Found unknown Windows version: " + osName);
							log.warning("Attempting to use default windows plug-in.");
							pluginClasses = pluginClasses + " net.java.games.input.windows.DirectAndRawInputEnvironmentPlugin";
						} else {
							log.warning("Trying to use default plugin, OS name " + osName + " not recognised");
						}
					}
				}
			}

			List<String> pluginClassNames = Arrays.asList(pluginClasses.trim().split("\\s+"));


            // Controller list has not been scanned.
            controllers = new ArrayList<>();
        	for(ControllerEnvironment ce :ServiceLoader.load(ControllerEnvironment.class).stream().filter(prv -> pluginClassNames.contains(prv.type().getName())).map(prv -> prv.get()).collect(Collectors.toList())) {
        		if(ce.isSupported()) {
        			addControllers(ce.getControllers());
        		}
        		else {
					log(ce.getClass().getName() + " is not supported");
        		}
        	}
        }
        Controller[] ret = new Controller[controllers.size()];
        Iterator<Controller> it = controllers.iterator();
        int i = 0;
        while (it.hasNext()) {
            ret[i] = it.next();
            i++;
        }
        return ret;
    }

    /**
     * Add the array of controllers to our list of controllers.
     */
    private void addControllers(Controller[] c) {
        for (int i = 0; i < c.length; i++) {
            controllers.add(c[i]);
        }
    }

	public boolean isSupported() {
		return true;
	}
}
