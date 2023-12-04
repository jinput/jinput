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
import java.util.stream.Collectors;

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

	/** All {@link Controller}s available to this environment. */
	private ArrayList<Controller> controllers;

	/** Class names of all loaded plugins. */
	private Collection<String> loadedPluginNames = new ArrayList<>();

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

		scanControllers(
			getPrivilegedProperty("java.home") +
			File.separator +
			"lib" +
			File.separator +
			pluginPathName
		);

		scanControllers(
		getPrivilegedProperty("user.dir") +
			File.separator +
			pluginPathName
		);

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

			final ControllerEnvironment environment = instantiateControllerEnvironment(environmentClass);
			if (environment == null) {
				continue;
			}

			if (!environment.isSupported()) {
				logger.warning(environmentClass.getName() + " is not supported");
				continue;
			}

			Collections.addAll(controllers, environment.getControllers());
			loadedPluginNames.add(environment.getClass().getName());
        }
	}

	/**
	 * Retrieves a list of all {@link Controller}s available to this environment.
	 *
	 * @return All {@link Controller}s available to this environment, or an empty array if there are no {@link Controller}s.
	 */
    public Controller[] getControllers() {
        if (controllers == null) {
            controllers = new ArrayList<>();

            AccessController.doPrivileged((PrivilegedAction<Void>) this::scanControllers);

			final String pluginClasses = getPluginClasses().stream().reduce("", (a, b) -> a + b + " ");

			final StringTokenizer tokenizer = new StringTokenizer(pluginClasses, " \t\n\r\f,;:");
			while (tokenizer.hasMoreTokens()) {
				final String className = tokenizer.nextToken();

				if (loadedPluginNames.contains(className)) {
					continue;
				}

				logger.fine("Loading: " + className);

				final ControllerEnvironment environment;
				try {
					final Class<?> clazz = Class.forName(className);
					environment = instantiateControllerEnvironment(clazz);
				} catch (final ClassNotFoundException e) {
                    logger.log(Level.SEVERE, "Failed to load class: " + className, e);
					continue;
                }

                if (environment == null) {
					continue;
				}

				if (environment.isSupported()) {
					Collections.addAll(controllers, environment.getControllers());
					loadedPluginNames.add(environment.getClass().getName());
				} else {
					log(environment.getClass().getName() + " is not supported.");
				}
            }
        }

		return Arrays.copyOf(controllers.toArray(), controllers.size(), Controller[].class);
    }

	/**
	 * Retrieves the list of plugin classes.
	 *
	 * @return List of plugin classes.
	 */
	private List<String> getPluginClasses() {
		AccessController.doPrivileged((PrivilegedAction<Void>) this::scanControllers);

		final List<String> classes = new ArrayList<>();
		classes.add(getPrivilegedProperty("jinput.plugins", ""));
		classes.add(getPrivilegedProperty("net.java.games.input.plugins", ""));

		if (!getPrivilegedProperty("jinput.useDefaultPlugin", "true").toLowerCase().trim().equals("false")) {
			return Collections.emptyList();
		}

		if (!getPrivilegedProperty("net.java.games.input.useDefaultPlugin", "true").toLowerCase().trim().equals("false")) {
			return Collections.emptyList();
		}

		final String osName = getPrivilegedProperty("os.name", "").trim();
		switch(osName) {
			case "Linux": {
				classes.add("net.java.games.input.LinuxEnvironmentPlugin");
				break;
			}

			case "Mac OS X": {
				classes.add("net.java.games.input.OSXEnvironmentPlugin");
				break;
			}

			case "Windows 98":
			case "Windows 2000": {
				classes.add("net.java.games.input.DirectInputEnvironmentPlugin");
				break;
			}

			case "Windows XP":
			case "Windows Vista":
			case "Windows 7":
			case "Windows 8":
			case "Windows 8.1":
			case "Windows 10":
			case "Windows 11": {
				classes.add("net.java.games.input.DirectAndRawInputEnvironmentPlugin");
				break;
			}

			default: {
				if (osName.startsWith("Windows")) {
					logger.warning("Found unknown Windows version: " + osName);
					logger.warning("Attempting to use default windows plug-in.");
					classes.add("net.java.games.input.DirectAndRawInputEnvironmentPlugin");
				} else {
					logger.warning("Trying to use default plugin, OS name " + osName + " not recognised");
				}
			}
		}

		return classes.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
	}

	// todo Document
	static String getPrivilegedProperty(final String property) {
		return AccessController.doPrivileged((PrivilegedAction<String>) () ->  System.getProperty(property));
	}

	// todo Document
	static String getPrivilegedProperty(final String property, final String default_value) {
		return AccessController.doPrivileged((PrivilegedAction<String>) () -> System.getProperty(property, default_value));
	}

	@Override
	public boolean isSupported() {
		return true;
	}

	/**
	 * Instantiates a {@link ControllerEnvironment} from the provided {@link Class}.
	 *
	 * @param clazz {@link Class} to instantiate.
	 *
	 * @return An instance of the specified {@link Class}.
	 */
	private ControllerEnvironment instantiateControllerEnvironment(final Class<?> clazz) {
		if (!ControllerEnvironment.class.isAssignableFrom(clazz)) {
			logger.warning("Cannot instantiate '" + clazz.getName() + "' as it does not extend ControllerEnvironment.");
			return null;
		}

		try {
			return (ControllerEnvironment) clazz.getDeclaredConstructor().newInstance();
		} catch (final InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
			logger.log(Level.SEVERE, "Failed to construct instance of " + clazz.getName(), e);
			return null;
		}
	}
}
