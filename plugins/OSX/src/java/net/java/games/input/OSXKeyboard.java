package net.java.games.input;

/**
 * Created by IntelliJ IDEA.
 * User: gpierce
 * Date: Aug 2, 2003
 * Time: 3:57:58 PM
 * To change this template use Options | File Templates.
 */
public class OSXKeyboard extends StandardKeyboard implements InputController
{

    private final static int[] CROSSTABLE = {
        0x00, 0x29, 0x1E, 0x1F, 0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, // _9
        0x27, 0x2D, 0x2E, 0x2A, 0x2B, 0x14, 0x1A, 0x08, 0x15, 0x17, 0x1C, // Y
        0x18, 0x0C, 0x12, 0x13, 0x2F, 0x30, 0x28, 0xE0, 0x04, 0x16, 0x07, // D
        0x09, 0x0A, 0x0B, 0x0D, 0x0E, 0x0F, 0x33, 0x34, 0x35, 0xE1, 0x31, // BACKSLASH
        0x1D, 0x1B, 0x06, 0x19, 0x05, 0x11, 0x10, 0x36, 0x37, 0x38, 0xE5, // RSHIFT
        0x55, 0xE2, 0x2C, 0x39, 0x3A, 0x3B, 0x3C, 0x3D, 0x3E, 0x3F, 0x40, // F7
        0x41, 0x42, 0x43, 0x53, 0x47, 0x5F, 0x60, 0x61, 0x56, 0x5C, 0x5D, // NUMPAD5
        0x5E, 0x57, 0x59, 0x5A, 0x5B, 0x62, 0x63, 0x44, 0x45, 0x68, 0x69, // F14
        0x6A, 0x87, 0x88, 0x89, 0x8A, 0x67, 0x8B, 0x8C, 0x8D, 0x9F, 0x8E, // KANJI
        0x78, 0x8F, 0x90, 0x58, 0xE4, 0x85, 0x54, 0x9A, 0xE6, 0x48, 0x4A, // HOME
        0x52, 0x9D, 0x50, 0x4F, 0x4D, 0x51, 0xA2, 0x49, 0x4C, 0xE3, 0xE7, // RWIN
        0x65, 0x66, 0x66 // SLEEP
    };

    private OSXEnvironmentPlugin            plugin;
    private long                            lpDevice;
    private long                            lpQueue;

    public OSXKeyboard( OSXEnvironmentPlugin plugin, long lpDevice, String productName )
    {
        super( productName );

        this.plugin = plugin;
        this.lpDevice = lpDevice;

        openDevice();
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

        switch( element.getUsagePage() )
        {
            case OSXEnvironmentPlugin.HID_USAGEPAGE_KEYBOARD:
                System.out.println("Adding key [" + element.getUsage() + "]");
                break;
        }
/*
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

            default:
                break;
        }

        System.out.println("Cookie [" + element.getHidCookie() + "]");
*/
    }

    public boolean poll()
    {
        plugin.pollDevice( lpQueue );


        return true;
    }


     /** Returns whether or not the given key has been pressed since the last
     * call to poll.
     * @param key The key whose state to check.
     * @return true if this key has changed state since last read of its state, false otherwise.
     */
    protected boolean isKeyPressed(Keyboard.Key key)
    {
        KeyID id = (KeyID)key.getIdentifier();
        int keyIndex = id.getKeyIndex();

        return false;
    }
}
