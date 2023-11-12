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

import net.java.games.util.plugins.Plugins;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The default controller environment.
 *
 * @version %I% %G%
 *
 * @author Michael Martak
 * @author Valkryst
 */
class DefaultControllerEnvironment extends ControllerEnvironment {
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * List of all controllers in this environment
	 */
	private ArrayList<Controller> controllers;

	private Collection<String> loadedPluginNames = new ArrayList<>();/* This is jeff's new plugin code using Jeff's Plugin manager */

	/**
	 * Attempts to scan the system for loadable {@link ControllerEnvironment} classes, and adds them to the set of
	 * {@code controllers}.
	 *
	 * @return {@code null}
	 */
	private Void scanControllers() {
		String pluginPathName = getPrivilegedProperty("jinput.controllerPluginPath");
		if(pluginPathName == null) {
			pluginPathName = "controller";
		}

		scanControllers(getPrivilegedProperty("java.home") +
				File.separator + "lib"+File.separator + pluginPathName);
		scanControllers(getPrivilegedProperty("user.dir")+
				File.separator + pluginPathName);

		return null;
	}

	/**
	 * Attempts to scan a {@link Path} for loadable {@link ControllerEnvironment} classes, and adds them to the set of
	 * {@code controllers}.
	 *
	 * @param path The {@link String} path to scan.
	 *
	 * @throws NullPointerException If {@code path} is {@code null}.
	 */
	private void scanControllers(final String path) {
		Objects.requireNonNull(path);

		if (!path.isEmpty()) {
			scanControllers(Paths.get(path));
		}
	}

	/**
	 * Attempts to scan a {@link Path} for loadable {@link ControllerEnvironment} classes, and adds them to the set of
	 * {@code controllers}.
	 *
	 * @param path The {@link Path} to scan.
	 *
	 * @throws NullPointerException If {@code path} is {@code null}.
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	private void scanControllers(final Path path) {
		Objects.requireNonNull(path);

		if (Files.notExists(path)) {
			logger.warning("Skipping ' + path + ' because it does not exist.");
			return;
		}

		if (!Files.isReadable(path)) {
			logger.warning("Skipping ' + path + ' because it is not readable.");
			return;
		}

		if (!Files.isDirectory(path)) {
			logger.warning("Skipping ' + path + ' because it is not a directory.");
			return;
		}

		final Plugins plugins;
		try {
			plugins = new Plugins(path.toFile());
		} catch (final IOException e) {
			logger.log(Level.SEVERE, "Failed to create an instance of Plugins for the path '" + path + "'", e);
			return;
		}

		final Class<ControllerEnvironment>[] environmentClasses = plugins.getExtends(ControllerEnvironment.class);
		for (final Class environmentClass : environmentClasses) {
			logger.info("ControllerEnvironment " + environmentClass.getName() + " loaded by " + environmentClass.getClassLoader());

			final ControllerEnvironment environment;
			try {
				environment = (ControllerEnvironment) environmentClass.getDeclaredConstructor().newInstance();
			} catch (final InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                logger.log(Level.SEVERE, "Failed to create an instance of " + environmentClass.getName(), e);
				continue;
            }

			if (!environment.isSupported()) {
				logger.warning(environmentClass.getName() + " is not supported");
				continue;
			}

			addControllers(environment.getControllers());
			loadedPluginNames.add(environment.getClass().getName());
        }

	}

	/**
	 * Add the array of controllers to our list of controllers.
	 */
	private void addControllers(Controller[] c) {
        Collections.addAll(controllers, c);
	}
    
    /**
     * Returns a list of all controllers available to this environment,
     * or an empty array if there are no controllers in this environment.
     */
    public Controller[] getControllers() {
        if (controllers == null) {
            // Controller list has not been scanned.
            controllers = new ArrayList<>();
            AccessController.doPrivileged((PrivilegedAction<Void>) () -> scanControllers());
            //Check the properties for specified controller classes
            String pluginClasses = getPrivilegedProperty("jinput.plugins", "") + " " + getPrivilegedProperty("net.java.games.input.plugins", "");
			if(!getPrivilegedProperty("jinput.useDefaultPlugin", "true").toLowerCase().trim().equals("false") && !getPrivilegedProperty("net.java.games.input.useDefaultPlugin", "true").toLowerCase().trim().equals("false")) {
				String osName = getPrivilegedProperty("os.name", "").trim();

				switch(osName) {
					case "Linux": {
						pluginClasses = pluginClasses + " net.java.games.input.LinuxEnvironmentPlugin";
						break;
					}

					case "Mac OS X": {
						pluginClasses = pluginClasses + " net.java.games.input.OSXEnvironmentPlugin";
						break;
					}

					case "Windows 98":
					case "Windows 2000": {
						pluginClasses = pluginClasses + " net.java.games.input.DirectInputEnvironmentPlugin";
						break;
					}

					case "Windows XP":
					case "Windows Vista":
					case "Windows 7":
					case "Windows 8":
					case "Windows 8.1":
					case "Windows 10":
					case "Windows 11": {
						pluginClasses = pluginClasses + " net.java.games.input.DirectAndRawInputEnvironmentPlugin";
						break;
					}

					default: {
						if (osName.startsWith("Windows")) {
							logger.warning("Found unknown Windows version: " + osName);
							logger.warning("Attempting to use default windows plug-in.");
							pluginClasses = pluginClasses + " net.java.games.input.DirectAndRawInputEnvironmentPlugin";
						} else {
							logger.warning("Trying to use default plugin, OS name " + osName + " not recognised");
						}
					}
				}
			}

			StringTokenizer pluginClassTok = new StringTokenizer(pluginClasses, " \t\n\r\f,;:");
			while(pluginClassTok.hasMoreTokens()) {
				String className = pluginClassTok.nextToken();					
				try {
					if(!loadedPluginNames.contains(className)) {
						logger.fine("Loading: " + className);
						Class<?> ceClass = Class.forName(className);
						ControllerEnvironment ce = (ControllerEnvironment) ceClass.getDeclaredConstructor().newInstance();
						if(ce.isSupported()) {
							addControllers(ce.getControllers());
							loadedPluginNames.add(ce.getClass().getName());
						} else {
							log(ceClass.getName() + " is not supported");
						}
					}
				} catch (Throwable e) {
					e.printStackTrace();
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

	static String getPrivilegedProperty(final String property) {
		return AccessController.doPrivileged((PrivilegedAction<String>) () ->  System.getProperty(property));
	}


	static String getPrivilegedProperty(final String property, final String default_value) {
		return AccessController.doPrivileged((PrivilegedAction<String>) () -> System.getProperty(property, default_value));
	}

	@Override
	public boolean isSupported() {
		return true;
	}
}
