This file modified last on 06/06/2003 by Jeff Kesselman

This is the source tree for the core input API.

Directory Organization:

The root contains a master ANT build.xml.
After a successful build of the project you will have the following sub directories:
 -- apidocs   Where the javadocs get built to
 -- lib    Where dependant libraries are kept.
 -- bin    Where the actual API is built to
 -- src    The source files.
 -- src/test   Execution directories and data for tests.

Build instructions:

To clean: ant clean
To build:  ant all (or just ant)
To build docs: ant javadoc
To test:		
	Textest: A simple discovery test that dumps
		the data about the discovered controllers to stdout
		To run: ant textest
	Readtest:  A test that creates a window for each discovered
		controller (or sub-controller) which displays the
		current state of all the controller's axiis.
		(Note: The windows currrently all open at the same
		 place on the screen so yo uwill have to move them to
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

