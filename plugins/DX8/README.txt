This file modified last on 06/06/2003 by Jeff Kesselman

This is the source tree for the core input API.

Directory Organization:

The root contains a master ANT build.xml.
After a successful build you will have the following sub directories:
 -- apidocs   Where the javadocs get built to
 -- lib    Where dependant libraries are kept.
 -- bin    Where the plugin dll and jar files ar built to.
 -- src    The source files.


Build instructions:

To clean: ant clean
To build:  ant all (or just ant)
To build docs: ant javadoc
To test:
	This build will install the plug-in into the coreAPI's test
	directories.		
	Use the tests in the coreAPI build.xml to test.

Release Info:
    Initial Release:  This release contains an implementation of the input
        API designed by Mike Martak of Sun and Thomas (?) of Sony CEA for
        the WIn32 platform.  All the code in src/input is cross platform. The
        Win32 code is segregated to the DirectX plugin (src/DXplugin) which
        depends on DirectInput from DX7 (or later).

    05/09/2003:  A number of bugs and problems with the DXPlugin are fixed in this
	release.  This release also brings the code up to date using the DI8
	interface.  This thus is the first release that requries Peter Puck's
	DX8 bindings to compile with MinGW.

    05/09/2003 (second update):
	This version adds a new standard value type to the API.
	Axis.POV holds standard definitions for values for POV (hat) switches
		-- Axis.POV.CENTER and Axis.POV.OFF are synonmous and are
			the center position.
		-- Axis.POV.UP, Axis.POV.DOWN, Axis.POV.LEFT and Axis.POV.RIGHT
		   should be self explainatory.
	Any hat that claims to be "normalized" will return these values. (It is
	recommended that all hats be normalized by the systemn specific plugins.)

