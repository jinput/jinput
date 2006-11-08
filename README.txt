Project: net.java.games.*
Purpose: Open source game libraries
Authors:
    -- input API design:
            Michael Martak, Sun Microsystems
            Thomas Daniel, Sony Computer Entertainment
    -- input API original author:
            Michael Martak,Sun Microsystems
    -- input API original release author:
            Jeff Kesselman, Game Technology Architect,
            Advanced Software Technologies Group,
            Sun Microsystems.
    -- this file updated on 06/06/2003 by Jeff Kesselman


Introduction:

This is the source tree for the Java Game Initiative (JGI) Open Source
client game programming APIs.

Build Requirements:

Note: This build depends on there being a jutils.jar in the lib directories
of both the coreAPI and the plug-ins.  If you install the JGI Jutils project
at the same root as this project and build it first it will put jars in the
right places.

This project has been built in the following environment.
 -- Win32 (Win 2000 in the case of our machine)
 -- Sun J2SDK 1.4 (available at java.sun.com)
 -- MinGW 2.0.0  plus the following updates: (all available at www.mingw.org)
     -- binutils 2.13.90
     -- w32api-2.2
     -- mingw-runtime-2.4
     -- "Peter Puck's" directx8 binding
	(http://www.urebelscum.speedhost.com/download.html, file: dx8libs.zip
	(Copy all of his *.a library files into the MingW "lib" directory)
     -- The DirectX 9 SDK available at the Microsoft DirectX site.
	(http://www.microsoft.com/directx)
	The Win32 plug-in build.xml file expects this to be installed in
	c:\dx9.  You can install it elsewhere but then you will have to modify
	the reference in that build.xml ant script.
 -- ANT 1.4.1 (available at apache.org)


Directory Organization:

The root contains a master ANT build.xml and the following sub directories:
 -- coreAPI:  The actual API
 -- plugins:  Directories for building controller plugins.
		(Currently the only plug in is the Win32 DX8 plugin.)

Build instructions:

To clean: ant clean
To build:  ant all (or just ant)
To build docs: ant javadoc
To test:
	First cd to coreAPI.  There are currently 2 tests there.
		Textest: A simple discovery test that dumps
			the data about the discovered controllers to stdout
			To run: ant textest
		Readtest:  A test that creates a window for each discovered
			controller (or sub-controller) which displays the
			current state of all the controller's axes.
			(Note: The windows currently all open at the same
			 place on the screen so you will have to move them to
			 see them all.)
			To run: ant readtest

Release Info:
    Initial Release:  This release contains an implementation of the input
        API designed by Mike Martak of Sun and Thomas (?) of Sony CEA for
        the WIn32 platform.  All the code in src/input is cross platform. The
        Win32 code is segregated to the DirectX plugin (src/DXplugin) which
        depends on DirectInput from DX7 (or later).

    05/09/2003:  A number of bugs and problems with the DXPlugin are fixed in this
	release.  This release also brings the code up to date using the DI8
	interface.  This thus is the first release that requires Peter Puck's
	DX8 bindings to compile with MinGW.

    05/09/2003 (second update):
	This version adds a new standard value type to the API.
	Axis.POV holds standard definitions for values for POV (hat) switches
		-- Axis.POV.CENTER and Axis.POV.OFF are synonymous and are
			the center position.
		-- Axis.POV.UP, Axis.POV.DOWN, Axis.POV.LEFT and Axis.POV.RIGHT
		   should be self explanatory.
	Any hat that claims to be "normalized" will return these values. (It is
	recommended that all hats be normalized by the system specific plugins.)

