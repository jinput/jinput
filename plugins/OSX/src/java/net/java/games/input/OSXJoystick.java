package net.java.games.input;

import java.util.ArrayList;
import java.util.List;


/**
 * Joystick class which recognizes most of the enhanced joystick features
 * including:
 *      x, y, and z axis
 *      x, y axis rotation
 *      slider
 *      hat
 *
 */
public class OSXJoystick extends AbstractController implements InputController
{
    private OSXEnvironmentPlugin            plugin;
    private long                            lpDevice;
    private long                            lpQueue;
    private int                             buttonCount = 0;
    private List                            buttons = new ArrayList();
    
    private Axis xAxis = null;
    private Axis yAxis = null;
    private Axis zAxis = null;
    private Axis xAxisRotation = null;
    private Axis yAxisRotation = null;
    private Axis zAxisRotation = null;
    private Axis slider = null;
    private Axis hat = null;

    public OSXJoystick( OSXEnvironmentPlugin plugin, long lpDevice, String productName )
    {
        super( productName );

        this.plugin = plugin;
        this.lpDevice = lpDevice;

        openDevice();
    }
    
    /** 
     * Returns the type of the Controller.
     */
    public Type getType() {
        return Type.STICK;
    }
    
    public boolean poll()
    {
        plugin.pollDevice( lpQueue );

        return true;
    }

    public void openDevice()
    {
        this.lpQueue = plugin.openDevice( this.lpDevice, 32 );
    }

    public void closeDevice()
    {
        plugin.closeDevice( this.lpDevice, this.lpQueue );
    }

    public void pollDevice()
    {
        plugin.pollDevice( this.lpQueue );
    }

    public Component[] getComponents() {
        List cpList = new ArrayList(buttons);
        if (xAxis != null) cpList.add(xAxis);
        if (yAxis != null) cpList.add(yAxis);
        if (zAxis != null) cpList.add(zAxis);
        if (xAxisRotation != null) cpList.add(xAxisRotation);
        if (yAxisRotation != null) cpList.add(yAxisRotation);
        if (zAxisRotation != null) cpList.add(zAxisRotation);
        if (slider != null) cpList.add(slider);
        if (hat != null) cpList.add(hat);
        Component[] ca = new Component[cpList.size()];
        return (Component[]) cpList.toArray(ca);
    }

    
    public void addControllerElement(InputControllerElement element)
    {

        switch ( element.getElementType() )
        {

            case OSXEnvironmentPlugin.HID_ELEMENTTYPE_INPUT_BUTTON:
                Button button = null;
                switch (buttonCount) {
                    case 0:
                        button = new Button(Component.Identifier.Button.TRIGGER, element);
                        break;
                    case 1:
                        button = new Button(Component.Identifier.Button._2, element);
                        break;
                    case 2:
                        button = new Button(Component.Identifier.Button._3, element);
                        break;
                    case 3:
                        button = new Button(Component.Identifier.Button._4, element);
                        break;
                    case 4:
                        button = new Button(Component.Identifier.Button._5, element);
                        break;
                    case 5:
                        button = new Button(Component.Identifier.Button._6, element);
                        break;
                    case 6:
                        button = new Button(Component.Identifier.Button._7, element);
                        break;
                    case 7:
                        button = new Button(Component.Identifier.Button._8, element);
                        break;
                    case 8:
                        button = new Button(Component.Identifier.Button._9, element);
                        break;
                    default:
                        String name = String.valueOf(buttonCount + 1);
                        button = new Button(new Component.Identifier.Button(name), element);
                        break;
                }
                buttons.add(button);
                buttonCount++;
                System.out.println("Adding button [" + buttonCount + "]");
                break;

            case OSXEnvironmentPlugin.HID_ELEMENTTYPE_INPUT_MISC:
            case OSXEnvironmentPlugin.HID_ELEMENTTYPE_INPUT_AXIS:
                switch (element.getUsage()) {
                    case OSXEnvironmentPlugin.HID_USAGE_XAXIS:
                        xAxis = new Axis(Component.Identifier.Axis.X, element);
                        break;
                    case OSXEnvironmentPlugin.HID_USAGE_YAXIS:
                        yAxis = new Axis(Component.Identifier.Axis.Y, element);
                        break;
                    case OSXEnvironmentPlugin.HID_USAGE_ZAXIS:
                        zAxis = new Axis(Component.Identifier.Axis.Z, element);
                        break;
                    case OSXEnvironmentPlugin.HID_USAGE_XAXIS_ROTATION:
                        xAxisRotation = new Axis(Component.Identifier.Axis.RX, element);
                        break;
                    case OSXEnvironmentPlugin.HID_USAGE_YAXIS_ROTATION:
                        yAxisRotation = new Axis(Component.Identifier.Axis.RY, element);
                        break;
                    case OSXEnvironmentPlugin.HID_USAGE_ZAXIS_ROTATION:
                        zAxisRotation = new Axis(Component.Identifier.Axis.RZ, element);
                        break;
                    case OSXEnvironmentPlugin.HID_USAGE_SLIDER:
                        slider = new Axis(Component.Identifier.Axis.SLIDER, element);
                        break;
                    case OSXEnvironmentPlugin.HID_USAGE_HAT:
                        hat = new Axis(Component.Identifier.Axis.POV, element);
                        break;
                    default:
                        System.out.println("*Unknown axis");
                        break;
                    }
             
                System.out.println("*Adding axis");
                break;

            case OSXEnvironmentPlugin.HID_ELEMENTTYPE_INPUT_SCANCODES:
                System.out.println("*Adding scancode");
                break;

            case OSXEnvironmentPlugin.HID_ELEMENTTYPE_OUTPUT:
                System.out.println("*Adding forcefeedback");
                break;

            case OSXEnvironmentPlugin.HID_ELEMENTTYPE_FEATURE:

                System.out.println("*Adding feature");
                break;

            case OSXEnvironmentPlugin.HID_ELEMENTTYPE_COLLECTION:
                System.out.println("*Adding collection");
                break;

        }
    }
    
    /**
     * Mouse button axis implementation
     */
    class Button extends AbstractComponent
    {

        private long hidCookie;
        private boolean isRelative;
   


        /** Public constructor
         * @param id An ID of a button to create an obejct to represent.
         *
         */
        public Button(Component.Identifier.Button id, InputControllerElement element)
        {
            super(id.getName(), id);
            this.hidCookie = element.getHidCookie();
            this.isRelative = element.isRelative();
        }

        /** Returns the data from the last time the control has been polled.
         * If this axis is a button, the value returned will be either 0.0f or 1.0f.
         * If this axis is normalized, the value returned will be between -1.0f and
         * 1.0f.
         * @return  state of controller. (Note: DX8 mice actually
         * queue state so what is returned is the next state,
         * not necessarily the most current one.)
         */
        public float getPollData()
        {
            return (float) plugin.pollElement( lpDevice, hidCookie );
        }

        /** Returns <code>true</code> if data returned from <code>poll</code>
         * is relative to the last call, or <code>false</code> if data
         * is absolute.
         * @return true if data is relative, otherwise false.
         */
        public boolean isRelative()
        {
            return isRelative;
        }
    }

    
    /**
     * Mouse button axis implementation
     */
    class Axis extends AbstractComponent
    {

        private long hidCookie;
        private boolean isRelative;
   
        /** Public constructor
         * @param id An ID of a button to create an obejct to represent.
         *
         */
        public Axis(Component.Identifier id, InputControllerElement element)
        {
            super(id.getName(), id);
            this.hidCookie = element.getHidCookie();
            this.isRelative = element.isRelative();
        }

        /** Returns the data from the last time the control has been polled.
         * If this axis is a button, the value returned will be either 0.0f or 1.0f.
         * If this axis is normalized, the value returned will be between -1.0f and
         * 1.0f.
         * @return  state of controller. (Note: DX8 mice actually
         * queue state so what is returned is the next state,
         * not necessarily the most current one.)
         */
        public float getPollData()
        {
            return (float) plugin.pollElement( lpDevice, hidCookie );
        }

        /** Returns <code>true</code> if data returned from <code>poll</code>
         * is relative to the last call, or <code>false</code> if data
         * is absolute.
         * @return true if data is relative, otherwise false.
         */
        public boolean isRelative()
        {
            return isRelative;
        }
    }

    
}
