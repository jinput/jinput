package net.java.games.input;

import java.util.HashMap;

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
        0x00, // VOID
        0x29, // ESCAPE
        0x1E, // _1
        0x1F, // _2
        0x20, // _3
        0x21, // _4
        0x22, // _5
        0x23, // _6
        0x24, // _7
        0x25, // _8
        0x26, // _9
        0x27, // _0
        0x2D, // MINUS
        0x2E, // EQUALS
        0x2A, // BACK
        0x2B, // TAB
        0x14, // Q
        0x1A, // W
        0x08, // E
        0x15, // R
        0x17, // T
        0x1C, // Y
        0x18, // U
        0x0C, // I
        0x12, // O
        0x13, // P
        0x2F, // [
        0x30, // ]
        0x28, // RETURN
        0xE0, // LEFT CONTROL
        0x04, // A
        0x16, // S
        0x07, // D
        0x09, // F
        0x0A, // G
        0x0B, // H
        0x0D, // J
        0x0E, // K
        0x0F, // L
        0x33, // ;
        0x34, // '
        0x35, // ~
        0xE1, // /
        0x31, // BACKSLASH (\)
        0x1D, // Z
        0x1B, // X
        0x06, // C
        0x19, // V
        0x05, // B
        0x11, // N
        0x10, // M
        0x36, // ,
        0x37, // .
        0x38, // SLASH (/)
        0xE5, // RSHIFT
        0x55, // MULT (*)
        0xE2, // LEFT ALT
        0x2C, // SPACE
        0x39, // CAPSLOCK
        0x3A, // F1
        0x3B, // F2
        0x3C, // F3
        0x3D, // F4
        0x3E, // F5
        0x3F, // F6
        0x40, // F7
        0x41, // F8
        0x42, // F9
        0x43, // F10
        0x53, // NUMLOCK
        0x47, // SCROLLLOCK
        0x5F, // NUMPAD7
        0x60, // NUMPAD8
        0x61, // NUMPAD9
        0x56, // SUBTRACT (KEYPAD -)
        0x5C, // NUMPAD4
        0x5D, // NUMPAD5
        0x5E, // NUMPAD6
        0x57, // ADD (KEYPAD +)
        0x59, // NUMPAD1
        0x5A, // NUMPAD2
        0x5B, // NUMPAD3
        0x62, // NUMPAD0
        0x63, // DECIMAL (KEYPAD .)
        0x44, // F11
        0x45, // F12
        0x68, // F13
        0x69, // F14
        0x6A, // F15
        0x87, // KANA
        0x88, // CONVERT
        0x89, // NONCONVERT
        0x8A, // YEN
        0x67, // NUMPAD=
        0x8B, // CIRCUMFLEX
        0x8C, // AT
        0x8D, // COLON
        0x9F, // UNDERLINE
        0x8E, // KANJI
        0x78, // STOP
        0x8F, // AX
        0x90, // UNLABELED
        0x58, // NUMPAD ENTER
        0xE4, // RIGHT CONTROL
        0x85, // NUMPAD COMMA
        0x54, // DIVIDE ( NUMPAD /)
        0x9A, // SYSREQ
        0xE6, // RIGHT ALT
        0x48, // PAUSE
        0x4A, // HOME
        0x52, // UP
        0x9D, // PRIOR
        0x50, // LEFT
        0x4F, // RIGHT
        0x4D, // END
        0x51, // DOWN
        0xA2, // NEXT
        0x49, // INSERT
        0x4C, // DELETE
        0xE3, // LEFT WIN
        0xE7, // RIGHT WIN
        0x65, // APPS
        0x66, // POWER
        0x66 // SLEEP
    };

    private OSXEnvironmentPlugin            plugin;
    private long                            lpDevice;
    private long                            lpQueue;
    private HashMap                         keys = new HashMap();

    private static int[]                    COOKIETABLE = new int[CROSSTABLE.length ];

    public OSXKeyboard( OSXEnvironmentPlugin plugin, long lpDevice, String productName )
    {
        super( productName );

        this.plugin = plugin;
        this.lpDevice = lpDevice;

        openDevice();
    }

    public void openDevice()
    {
        this.lpQueue = plugin.openDevice( this.lpDevice, 256 );
    }

    public void closeDevice()
    {
        plugin.closeDevice( this.lpDevice, this.lpQueue );
    }

    public void addControllerElement(InputControllerElement element)
    {
        //System.out.println("Adding keyboard elements usage page[" + element.getUsagePage() + "] usage [" + element.getUsage() + "] type [" + element.getElementType() + "]" );

        switch( element.getUsagePage() )
        {
            case OSXEnvironmentPlugin.HID_USAGEPAGE_KEYBOARD:

                //System.out.println("Found keyboard element");

                if ( element.getElementType() == OSXEnvironmentPlugin.HID_ELEMENTTYPE_INPUT_BUTTON )
                {
                    System.out.println("Adding key [" + element.getUsage() + "]");

                    // register this key with the queue system as all buttons are retrieved from the
                    // input controllers queue
                    //
                    plugin.registerDeviceElement( lpQueue, element.getHidCookie() );

                    //TODO: Optimize this - put the usages in another array the same size as the crosstable so the hidCookies
                    // can be retrieved directly without the Long creation
                    keys.put( new Long( element.getUsage()), element );
                }
                break;

            default:
        }
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

        // get that key code out of the crosstable and find the proper InputControllerElement/button for that key
        //
        //TODO: Optimize this - put the usages in another array the same size as the crosstable so the hidCookies
        // can be retrieved directly without the Long creation
        int usage = CROSSTABLE[keyIndex];
        InputControllerElement element = (InputControllerElement) keys.get( new Long(usage) );


        if ( element != null )
        {
            int value = plugin.pollElement( lpDevice, element.getHidCookie() );

            System.out.println("Key Poll result [" + value + "]");
            if ( value == 1 )
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        return false;
    }
}
