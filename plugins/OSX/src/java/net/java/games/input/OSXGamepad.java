package net.java.games.input;

/**
 * Created by IntelliJ IDEA.
 * User: gpierce
 * Date: Aug 2, 2003
 * Time: 3:59:09 PM
 * To change this template use Options | File Templates.
 */
public class OSXGamepad extends AbstractController implements InputController
{
    private OSXEnvironmentPlugin            plugin;
    private long                            lpDevice;
    private long                            lpQueue;

    public OSXGamepad( OSXEnvironmentPlugin plugin, long lpDevice, String productName )
    {
        super( productName );

        this.plugin = plugin;
        this.lpDevice = lpDevice;

        openDevice();
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

    public void addControllerElement(InputControllerElement element)
    {
        switch ( element.getElementType() )
        {
            case OSXEnvironmentPlugin.HID_ELEMENTTYPE_INPUT_MISC:
                System.out.println("*Adding misc component");
                break;

            case OSXEnvironmentPlugin.HID_ELEMENTTYPE_INPUT_BUTTON:
                System.out.println("*Adding button");
                break;

            case OSXEnvironmentPlugin.HID_ELEMENTTYPE_INPUT_AXIS:
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
}
