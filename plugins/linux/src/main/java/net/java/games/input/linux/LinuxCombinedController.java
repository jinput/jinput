package net.java.games.input.linux;

import java.io.IOException;

import net.java.games.input.AbstractController;
import net.java.games.input.Event;

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
