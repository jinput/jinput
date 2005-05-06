package net.java.games.input;

/**
 * Created by IntelliJ IDEA.
 * User: gpierce
 * Date: Aug 2, 2003
 * Time: 3:57:00 PM
 * To change this template use Options | File Templates.
 */
public class OSXMouse extends Mouse implements InputController
{
    private OSXEnvironmentPlugin            plugin;
    private long                            lpDevice;
    private long                            lpQueue;
    private int                             buttonCount = 0;


    public OSXMouse( OSXEnvironmentPlugin plugin, long lpDevice, String productName )
    {
        super( productName );

        this.plugin = plugin;
        this.lpDevice = lpDevice;

        openDevice();

        buttons = new ButtonsImpl();
        ball = new BallImpl();
    }

    public void openDevice()
    {
        this.lpQueue = plugin.openDevice( this.lpDevice, 32 );
    }

    public void closeDevice()
    {
        plugin.closeDevice( this.lpDevice, this.lpQueue );
    }

    public boolean poll()
    {
        plugin.pollDevice( this.lpQueue );

        return true;
    }

    public void addControllerElement(InputControllerElement element)
    {

        switch ( element.getUsagePage() )
        {
            case OSXEnvironmentPlugin.HID_USAGEPAGE_BUTTON:
                buttonCount ++;
                System.out.println("Adding button [" + buttonCount + "]");
                ((ButtonsImpl)buttons).addButton(element);
                break;


            case OSXEnvironmentPlugin.HID_USAGEPAGE_GENERICDESKTOP:
                switch( element.getUsage() )
                {
                    case OSXEnvironmentPlugin.HID_USAGE_POINTER:
                        System.out.println("Adding pointer - this will contain axis");
                        break;

                    case OSXEnvironmentPlugin.HID_USAGE_XAXIS:
                        ((BallImpl)ball).addXAxis(element);
                        System.out.println("Adding X Axis") ;
                        break;

                    case OSXEnvironmentPlugin.HID_USAGE_YAXIS:
                        ((BallImpl)ball).addYAxis(element);
                        System.out.println("Adding Y Axis");
                        break;

                    case OSXEnvironmentPlugin.HID_USAGE_WHEEL:
                        ((BallImpl)ball).addWheelAxis(element);
                        System.out.println("Adding wheel");
                        break;

                    default:

                }
                break;


            default:

        }
    }

    /**
     * Implementation class representing the mouse ball
     */
    class BallImpl extends Ball
    {
        /**
         * Public constructor
         */
        public BallImpl()
        {
            super(OSXMouse.this.getName() + " ball");
        }

        public void addXAxis( InputControllerElement element )
        {
            x = new BallAxis(Component.Identifier.Axis.X, element );
        }

        public void addYAxis( InputControllerElement element )
        {
            y = new BallAxis( Component.Identifier.Axis.Y, element );
        }

        public void addWheelAxis( InputControllerElement element )
        {
            wheel = new BallAxis( Component.Identifier.Axis.SLIDER, element );
        }
    }


    /**
     * Implementation class representing the mouse buttons
     */
    class ButtonsImpl extends Buttons
    {
        /**
         * Public constructor
         */
        public ButtonsImpl()
        {
            super(OSXMouse.this.getName() + " buttons");
        }

        public void addButton( InputControllerElement element )
        {
            if ( left == null )
            {
                left = new ButtonImpl( Component.Identifier.Button.LEFT, element );
            }
            else if ( right == null )
            {
                right = new ButtonImpl( Component.Identifier.Button.RIGHT, element );
            }
            else if ( middle == null )
            {
                middle = new ButtonImpl( Component.Identifier.Button.MIDDLE, element );
            }
        }
    }

    /**
     * Mouse button axis implementation
     */
    class ButtonImpl extends Button
    {

        private long hidCookie;
        private boolean isRelative;


        /** Public constructor
         * @param id An ID of a button to create an obejct to represent.
         *
         */
        public ButtonImpl(Component.Identifier.Button id, InputControllerElement element)
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
     * Mouse ball axis implementation
     */
    class BallAxis extends AbstractComponent
    {

        private long hidCookie;
        private boolean isRelative;

        /** Public constructor
         * @param id  An ID for a mouse axis to create an object to represent.
         */
        public BallAxis(Component.Identifier.Axis id, InputControllerElement element)
        {
            super(id.getName(), id);

            this.hidCookie = element.getHidCookie();
            this.isRelative = element.isRelative();
        }

        /** Returns the data from the last time the control has been polled.
         * If this axis is a button, the value returned will be either 0.0f or 1.0f.
         * If this axis is normalized, the value returned will be between -1.0f and
         * 1.0f.
         * @return  data.  (Note that mice queue state in DX8 so what
         * is returned is the next stae in the queue, not
         * necessarily the most current one.)
         */
        public float getPollData()
        {
            return (float) plugin.pollElement( lpDevice, hidCookie );
        }

        /** Returns <code>true</code> if data returned from <code>poll</code>
         * is relative to the last call, or <code>false</code> if data
         * is absolute.
         * @return true if relative, otherwise false.
         */
        public boolean isRelative()
        {
            return isRelative;
        }

        /** Returns whether or not the axis is analog, or false if it is digital.
         * @return true if analog, false if digital
         */
        public boolean isAnalog()
        {
            return true;
        }
    }

}
