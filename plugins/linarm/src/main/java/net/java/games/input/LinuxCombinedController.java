package net.java.games.input;

import java.io.IOException;

public class LinuxCombinedController extends AbstractController {

	private LinuxAbstractController eventController;
	private LinuxJoystickAbstractController joystickController;

	LinuxCombinedController(LinuxAbstractController eventController, LinuxJoystickAbstractController joystickController) {
		super(eventController.getName(), joystickController.getComponents(), eventController.getControllers(), eventController.getRumblers());
		this.eventController = eventController;
		this.joystickController = joystickController;
	}

	protected boolean getNextDeviceEvent(Event event) throws IOException {
		return joystickController.getNextDeviceEvent(event);
	}

	public final PortType getPortType() {
		return eventController.getPortType();
	}

	public final void pollDevice() throws IOException {
		eventController.pollDevice();
		joystickController.pollDevice();
	}

	public Type getType() {
		return eventController.getType();
	}
}
