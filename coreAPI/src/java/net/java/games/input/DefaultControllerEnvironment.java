/*
 * %W% %E%
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
/*****************************************************************************
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import net.java.games.util.plugins.*;

/**
 * The default controller environment.
 *
 * @version %I% %G%
 * @author Michael Martak
 */
class DefaultControllerEnvironment extends ControllerEnvironment {
    static final boolean DEBUG =false;
    /**
     * The name of the properties file to find plugins.
     */
    private final static String PROPERTY_FILENAME =
        "controller.properties";
    
    /**
     * The name of the property for identifying a plugin (used
     * as the value, the key being the class name).
     */
    private final static String ID_PLUGIN =
        "ControllerEnvironment";
    
    /**
     * Location of the LIB directory.
     */
    static String libPath;
    
    /**
     * List of all controllers in this environment
     */
    private ArrayList controllers;
    
    /**
     * Plug-in properties.
     */
    private Properties properties = new Properties();
    
    /**
     * Plug-in class loader.
     */
    private PluginClassLoader pluginLoader = new PluginClassLoader();
	
	private Collection loadedPlugins = new ArrayList();

	/**
	 * Static utility method for loading native libraries.
	 * It will try to load from either the path given by
	 * the net.java.games.input.librarypath property
	 * or through System.loadLibrary().
	 * 
	 */
	static void loadLibrary(final String lib_name) {
		AccessController.doPrivileged(
				new PrivilegedAction() {
					public final Object run() {
						String lib_path = System.getProperty("net.java.games.input.librarypath");
						if (lib_path != null)
							System.load(lib_path + File.separator + System.mapLibraryName(lib_name));
						else
							System.loadLibrary(lib_name);
						return null;
					}
				});
	}
    
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
            // Controller list has not been scanned.
            controllers = new ArrayList();
            AccessController.doPrivileged(new PrivilegedAction() {
                public Object run() {
                    scanControllers();
                    return DefaultControllerEnvironment.this;
                }
            });
            //Check the properties for specified controller classes
            String pluginClasses = System.getProperty("jinput.plugins", "") + " " + System.getProperty("net.java.games.input.plugins", "");
			if(!System.getProperty("jinput.useDefaultPlugin", "true").toLowerCase().trim().equals("false") && !System.getProperty("net.java.games.input.useDefaultPlugin", "true").toLowerCase().trim().equals("false")) {
				String osName = System.getProperty("os.name", "").trim();
				if(osName.equals("Linux")) {
					pluginClasses = pluginClasses + " net.java.games.input.LinuxEnvironmentPlugin";
				} else if(osName.equals("Mac OS X")) {
					pluginClasses = pluginClasses + " net.java.games.input.OSXEnvironmentPlugin";
				} else if(osName.equals("Windows 98") || osName.equals("Windows 2000") || osName.equals("Windows XP")) {
					pluginClasses = pluginClasses + " net.java.games.input.DirectInputEnvironmentPlugin";
//					pluginClasses = pluginClasses + " net.java.games.input.RawInputEnvironmentPlugin";
				} else if (osName.startsWith("Windows")) {
					System.out.println("WARNING: Found unknown Windows version: " + osName);
					System.out.println("Attempting to use default windows plug-in.");
					System.out.flush();
					pluginClasses = pluginClasses + " net.java.games.input.DirectInputEnvironmentPlugin";
//					pluginClasses = pluginClasses + " net.java.games.input.RawInputEnvironmentPlugin";
				} else {
					System.out.println("Trying to use default plugin, OS name " + osName +" not recognised");
				}
			}
			ArrayList pluginClassList = new ArrayList();
			StringTokenizer pluginClassTok = new StringTokenizer(pluginClasses, " \t\n\r\f,;:");
			while(pluginClassTok.hasMoreTokens()) {
				String className = pluginClassTok.nextToken();					
				try {
					if(!loadedPlugins.contains(className)) {
						Class ceClass = Class.forName(className);
						ControllerEnvironment ce = (ControllerEnvironment) ceClass.newInstance();
						addControllers(ce.getControllers());
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
        }
        Controller[] ret = new Controller[controllers.size()];
        Iterator it = controllers.iterator();
        int i = 0;
        while (it.hasNext()) {
            ret[i] = (Controller)it.next();
            i++;
        }
        return ret;
    }
    
    /**
     * Scans for controllers, placing them in the controllers list.
     */
    /*  This is Mike's old plugin code.
    private void scanControllers() {
        // Load properties object.
        try {
            loadProperties();
        } catch (IOException e) {
            // Could not find or read file, simply return.
            return;
        }
        // Create a list of ControllerEnvironment classes.
        // For each ControllerEnvironment, locate the class
        // using the plugin class loader.
        Iterator it = properties.keySet().iterator();
        while (it.hasNext()) {
            Object key = it.next();
            assert key != null;
            Object value = properties.get(key);
            assert value != null;
            if (value.equals(ID_PLUGIN)) {
                try {
                    ControllerEnvironment plugin =
                        newPlugin(key.toString());
                        addControllers(plugin.getControllers());
                } catch (Throwable t) {
                    System.err.println(
                        "Warning : could not load plugin " +
                        key.toString() + ", received exeption " +
                        t.toString());
                    t.printStackTrace(System.err);
                }
            }
        }
    }*/
    
    /* This is jeff's new plugin code using Jeff's Plugin manager */
    private void scanControllers() {
        String pluginPathName = System.getProperty("jinput.controllerPluginPath");
        if(pluginPathName == null) {
            pluginPathName = "controller";
        }
        
        scanControllersAt(System.getProperty("java.home") +
            File.separator + "lib"+File.separator + pluginPathName);
        scanControllersAt(System.getProperty("user.dir")+
            File.separator + pluginPathName);
    }
    
    private void scanControllersAt(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        try {
            Plugins plugins = new Plugins(file);
            Class[] envClasses = plugins.getExtends(ControllerEnvironment.class);
            for(int i=0;i<envClasses.length;i++){
                try {
                    if (DEBUG) {
                        System.out.println("ControllerEnvironment "+
                            envClasses[i].getName()
                            +" loaded by "+envClasses[i].getClassLoader());
                    }
                    ControllerEnvironment ce = (ControllerEnvironment)
                        envClasses[i].newInstance();      					
                    addControllers(ce.getControllers());
					loadedPlugins.add(ce.getClass().getName());
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        
    /**
     * Retrieve the file "lib/control.properties" and
     * load properties into properties object.
     */
    private void loadProperties() throws IOException {
        if (libPath == null) {
            libPath = System.getProperty("java.home") +
            File.separator + "lib";
        }
        File file = new File(libPath + File.separator +
        PROPERTY_FILENAME);
        FileInputStream inputStream = new FileInputStream(file);
        properties.load(inputStream);
        inputStream.close();
    }
    
    /**
     * Create a new plugin ControllerEnvironment object
     */
    /*
    private ControllerEnvironment newPlugin(String name) throws
        ClassNotFoundException, InstantiationException,
        IllegalAccessException {
        Class pluginClass = pluginLoader.loadClass(name);
        if (!ControllerEnvironment.class.isAssignableFrom(pluginClass)) {
            throw new ClassCastException(
                "Plugin class must be assignable from " +
                ControllerEnvironment.class.getName());
        }
        Object instance = pluginClass.newInstance();
        return (ControllerEnvironment)instance;
    }
    */
    /**
     * Add the array of controllers to our list of controllers.
     */
    private void addControllers(Controller[] c) {
        for (int i = 0; i < c.length; i++) {
            controllers.add(c[i]);
        }
    }
}
