/**
 * Copyright (C) 2003 Jeremy Booth (jeremy@newdawnsoftware.com)
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this 
 * list of conditions and the following disclaimer. Redistributions in binary 
 * form must reproduce the above copyright notice, this list of conditions and 
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. 
 * The name of the author may not be used to endorse or promote products derived
 * from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO 
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
 */
package net.java.games.input;

/**
 * Mapping utility class between native type ints and string names or
 * Key.Identifiers
 * @author Jeremy Booth (jeremy@newdawnsoftware.com)
 */
public class LinuxNativeTypesMap {
    
    /** Instance of they key map
     */    
    private static LinuxNativeTypesMap INSTANCE = new LinuxNativeTypesMap();
    
    /** Indexed array of key names
     */    
    private String keyNames[];
    /** Inexed array of relative axes names
     */    
    private String relAxesNames[];
    /** Indexed array of absolute axis names
     */    
    private String absAxesNames[];
    /** Indexed array of relative axis ID's
     */    
    private Axis.Identifier relAxesIDs[];
    /** Indexed array of absoulte axis ID's
     */    
    private Axis.Identifier absAxesIDs[];
    /** Indexed array of button axis ID's
     */    
    private Axis.Identifier buttonIDs[];
    
    /** create an empty, uninitialsed map
     */    
    private LinuxNativeTypesMap() {
        keyNames = new String[NativeDefinitions.KEY_MAX];
    }
    
    /** Initialise the map, has to be done like this because the map references staic
     * classes, as this would otherwise be done in a static class initialiser the whole
     * thing blew up as the static classes this class references refer to this class
     * this meant that the other classes could not be initialsed until this one was,
     * but this class couldn't be loaded until the others were, all went horibly wrong
     * and INSTANCE was null whilst initialising the class, ouch.
     */    
    public static void init() {
        INSTANCE.reInit();
    }
        
    /** Do the work.
     */    
    private void reInit() {
        keyNames[NativeDefinitions.KEY_ESC] = "Escape";
        keyNames[NativeDefinitions.KEY_1] = "1";
        keyNames[NativeDefinitions.KEY_2] = "2";
        keyNames[NativeDefinitions.KEY_3] = "3";
        keyNames[NativeDefinitions.KEY_4] = "4";
        keyNames[NativeDefinitions.KEY_5] = "5";
        keyNames[NativeDefinitions.KEY_6] = "6";
        keyNames[NativeDefinitions.KEY_7] = "7";
        keyNames[NativeDefinitions.KEY_8] = "8";
        keyNames[NativeDefinitions.KEY_9] = "9";
        keyNames[NativeDefinitions.KEY_0] = "0";
        keyNames[NativeDefinitions.KEY_MINUS] = "-";
        keyNames[NativeDefinitions.KEY_EQUAL] = "=";
        keyNames[NativeDefinitions.KEY_BACKSPACE] = "Backspace";
        keyNames[NativeDefinitions.KEY_TAB] = "Tab";
        keyNames[NativeDefinitions.KEY_Q] = "Q";
        keyNames[NativeDefinitions.KEY_W] = "W";
        keyNames[NativeDefinitions.KEY_E] = "E";
        keyNames[NativeDefinitions.KEY_R] = "R";
        keyNames[NativeDefinitions.KEY_T] = "T";
        keyNames[NativeDefinitions.KEY_Y] = "Y";
        keyNames[NativeDefinitions.KEY_U] = "U";
        keyNames[NativeDefinitions.KEY_I] = "I";
        keyNames[NativeDefinitions.KEY_O] = "O";
        keyNames[NativeDefinitions.KEY_P] = "P";
        keyNames[NativeDefinitions.KEY_LEFTBRACE] = "[";
        keyNames[NativeDefinitions.KEY_RIGHTBRACE] = "]";
        keyNames[NativeDefinitions.KEY_ENTER] = "Enter";
        keyNames[NativeDefinitions.KEY_LEFTCTRL] = "LH Control";
        keyNames[NativeDefinitions.KEY_A] = "A";
        keyNames[NativeDefinitions.KEY_S] = "S";
        keyNames[NativeDefinitions.KEY_D] = "D";
        keyNames[NativeDefinitions.KEY_F] = "F";
        keyNames[NativeDefinitions.KEY_G] = "G";
        keyNames[NativeDefinitions.KEY_H] = "H";
        keyNames[NativeDefinitions.KEY_J] = "J";
        keyNames[NativeDefinitions.KEY_K] = "K";
        keyNames[NativeDefinitions.KEY_L] = "L";
        keyNames[NativeDefinitions.KEY_SEMICOLON] = ";";
        keyNames[NativeDefinitions.KEY_APOSTROPHE] = "'";
        keyNames[NativeDefinitions.KEY_GRAVE] = "`";
        keyNames[NativeDefinitions.KEY_LEFTSHIFT] = "LH Shift";
        keyNames[NativeDefinitions.KEY_BACKSLASH] = "\\";
        keyNames[NativeDefinitions.KEY_Z] = "Z";
        keyNames[NativeDefinitions.KEY_X] = "X";
        keyNames[NativeDefinitions.KEY_C] = "C";
        keyNames[NativeDefinitions.KEY_V] = "V";
        keyNames[NativeDefinitions.KEY_B] = "B";
        keyNames[NativeDefinitions.KEY_N] = "N";
        keyNames[NativeDefinitions.KEY_M] = "M";
        keyNames[NativeDefinitions.KEY_COMMA] = ",";
        keyNames[NativeDefinitions.KEY_DOT] = ".";
        keyNames[NativeDefinitions.KEY_SLASH] = "/";
        keyNames[NativeDefinitions.KEY_RIGHTSHIFT] = "RH Shift";
        keyNames[NativeDefinitions.KEY_KPASTERISK] = "*";
        keyNames[NativeDefinitions.KEY_LEFTALT] = "LH Alt";
        keyNames[NativeDefinitions.KEY_SPACE] = "Space";
        keyNames[NativeDefinitions.KEY_CAPSLOCK] = "CapsLock";
        keyNames[NativeDefinitions.KEY_F1] = "F1";
        keyNames[NativeDefinitions.KEY_F2] = "F2";
        keyNames[NativeDefinitions.KEY_F3] = "F3";
        keyNames[NativeDefinitions.KEY_F4] = "F4";
        keyNames[NativeDefinitions.KEY_F5] = "F5";
        keyNames[NativeDefinitions.KEY_F6] = "F6";
        keyNames[NativeDefinitions.KEY_F7] = "F7";
        keyNames[NativeDefinitions.KEY_F8] = "F8";
        keyNames[NativeDefinitions.KEY_F9] = "F9";
        keyNames[NativeDefinitions.KEY_F10] = "F10";
        keyNames[NativeDefinitions.KEY_NUMLOCK] = "NumLock";
        keyNames[NativeDefinitions.KEY_SCROLLLOCK] = "ScrollLock";
        keyNames[NativeDefinitions.KEY_KP7] = "KeyPad 7";
        keyNames[NativeDefinitions.KEY_KP8] = "KeyPad 8";
        keyNames[NativeDefinitions.KEY_KP9] = "Keypad 9";
        keyNames[NativeDefinitions.KEY_KPMINUS] = "KeyPad Minus";
        keyNames[NativeDefinitions.KEY_KP4] = "KeyPad 4";
        keyNames[NativeDefinitions.KEY_KP5] = "KeyPad 5";
        keyNames[NativeDefinitions.KEY_KP6] = "KeyPad 6";
        keyNames[NativeDefinitions.KEY_KPPLUS] = "KeyPad Plus";
        keyNames[NativeDefinitions.KEY_KP1] = "KeyPad 1";
        keyNames[NativeDefinitions.KEY_KP2] = "KeyPad 2";
        keyNames[NativeDefinitions.KEY_KP3] = "KeyPad 3";
        keyNames[NativeDefinitions.KEY_KP0] = "KeyPad 0";
        keyNames[NativeDefinitions.KEY_KPDOT] = "KeyPad decimal point";
        keyNames[NativeDefinitions.KEY_103RD] = "Huh?";
        keyNames[NativeDefinitions.KEY_F13] = "F13";
        keyNames[NativeDefinitions.KEY_102ND] = "Beats me...";
        keyNames[NativeDefinitions.KEY_F11] = "F11";
        keyNames[NativeDefinitions.KEY_F12] = "F12";
        keyNames[NativeDefinitions.KEY_F14] = "F14";
        keyNames[NativeDefinitions.KEY_F15] = "F15";
        keyNames[NativeDefinitions.KEY_F16] = "F16";
        keyNames[NativeDefinitions.KEY_F17] = "F17";
        keyNames[NativeDefinitions.KEY_F18] = "F18";
        keyNames[NativeDefinitions.KEY_F19] = "F19";
        keyNames[NativeDefinitions.KEY_F20] = "F20";
        keyNames[NativeDefinitions.KEY_KPENTER] = "Keypad Enter";
        keyNames[NativeDefinitions.KEY_RIGHTCTRL] = "RH Control";
        keyNames[NativeDefinitions.KEY_KPSLASH] = "KeyPad Forward Slash";
        keyNames[NativeDefinitions.KEY_SYSRQ] = "System Request";
        keyNames[NativeDefinitions.KEY_RIGHTALT] = "RH Alternate";
        keyNames[NativeDefinitions.KEY_LINEFEED] = "Line Feed";
        keyNames[NativeDefinitions.KEY_HOME] = "Home";
        keyNames[NativeDefinitions.KEY_UP] = "Up";
        keyNames[NativeDefinitions.KEY_PAGEUP] = "Page Up";
        keyNames[NativeDefinitions.KEY_LEFT] = "Left";
        keyNames[NativeDefinitions.KEY_RIGHT] = "Right";
        keyNames[NativeDefinitions.KEY_END] = "End";
        keyNames[NativeDefinitions.KEY_DOWN] = "Down";
        keyNames[NativeDefinitions.KEY_PAGEDOWN] = "Page Down";
        keyNames[NativeDefinitions.KEY_INSERT] = "Insert";
        keyNames[NativeDefinitions.KEY_DELETE] = "Delete";
        keyNames[NativeDefinitions.KEY_MACRO] = "Macro";
        keyNames[NativeDefinitions.KEY_MUTE] = "Mute";
        keyNames[NativeDefinitions.KEY_VOLUMEDOWN] = "Volume Down";
        keyNames[NativeDefinitions.KEY_VOLUMEUP] = "Volume Up";
        keyNames[NativeDefinitions.KEY_POWER] = "Power";
        keyNames[NativeDefinitions.KEY_KPEQUAL] = "KeyPad Equal";
        keyNames[NativeDefinitions.KEY_KPPLUSMINUS] = "KeyPad +/-";
        keyNames[NativeDefinitions.KEY_PAUSE] = "Pause";
        keyNames[NativeDefinitions.KEY_F21] = "F21";
        keyNames[NativeDefinitions.KEY_F22] = "F22";
        keyNames[NativeDefinitions.KEY_F23] = "F23";
        keyNames[NativeDefinitions.KEY_F24] = "F24";
        keyNames[NativeDefinitions.KEY_KPCOMMA] = "KeyPad comma";
        keyNames[NativeDefinitions.KEY_LEFTMETA] = "LH Meta";
        keyNames[NativeDefinitions.KEY_RIGHTMETA] = "RH Meta";
        keyNames[NativeDefinitions.KEY_COMPOSE] = "Compose";
        keyNames[NativeDefinitions.KEY_STOP] = "Stop";
        keyNames[NativeDefinitions.KEY_AGAIN] = "Again";
        keyNames[NativeDefinitions.KEY_PROPS] = "Properties";
        keyNames[NativeDefinitions.KEY_UNDO] = "Undo";
        keyNames[NativeDefinitions.KEY_FRONT] = "Front";
        keyNames[NativeDefinitions.KEY_COPY] = "Copy";
        keyNames[NativeDefinitions.KEY_OPEN] = "Open";
        keyNames[NativeDefinitions.KEY_PASTE] = "Paste";
        keyNames[NativeDefinitions.KEY_FIND] = "Find";
        keyNames[NativeDefinitions.KEY_CUT] = "Cut";
        keyNames[NativeDefinitions.KEY_HELP] = "Help";
        keyNames[NativeDefinitions.KEY_MENU] = "Menu";
        keyNames[NativeDefinitions.KEY_CALC] = "Calculator";
        keyNames[NativeDefinitions.KEY_SETUP] = "Setup";
        keyNames[NativeDefinitions.KEY_SLEEP] = "Sleep";
        keyNames[NativeDefinitions.KEY_WAKEUP] = "Wakeup";
        keyNames[NativeDefinitions.KEY_FILE] = "File";
        keyNames[NativeDefinitions.KEY_SENDFILE] = "Send File";
        keyNames[NativeDefinitions.KEY_DELETEFILE] = "Delete File";
        keyNames[NativeDefinitions.KEY_XFER] = "Transfer";
        keyNames[NativeDefinitions.KEY_PROG1] = "Program 1";
        keyNames[NativeDefinitions.KEY_PROG2] = "Program 2";
        keyNames[NativeDefinitions.KEY_WWW] = "Web Browser";
        keyNames[NativeDefinitions.KEY_MSDOS] = "DOS mode";
        keyNames[NativeDefinitions.KEY_COFFEE] = "Coffee";
        keyNames[NativeDefinitions.KEY_DIRECTION] = "Direction";
        keyNames[NativeDefinitions.KEY_CYCLEWINDOWS] = "Window cycle";
        keyNames[NativeDefinitions.KEY_MAIL] = "Mail";
        keyNames[NativeDefinitions.KEY_BOOKMARKS] = "Book Marks";
        keyNames[NativeDefinitions.KEY_COMPUTER] = "Computer";
        keyNames[NativeDefinitions.KEY_BACK] = "Back";
        keyNames[NativeDefinitions.KEY_FORWARD] = "Forward";
        keyNames[NativeDefinitions.KEY_CLOSECD] = "Close CD";
        keyNames[NativeDefinitions.KEY_EJECTCD] = "Eject CD";
        keyNames[NativeDefinitions.KEY_EJECTCLOSECD] = "Eject / Close CD";
        keyNames[NativeDefinitions.KEY_NEXTSONG] = "Next Song";
        keyNames[NativeDefinitions.KEY_PLAYPAUSE] = "Play and Pause";
        keyNames[NativeDefinitions.KEY_PREVIOUSSONG] = "Previous Song";
        keyNames[NativeDefinitions.KEY_STOPCD] = "Stop CD";
        keyNames[NativeDefinitions.KEY_RECORD] = "Record";
        keyNames[NativeDefinitions.KEY_REWIND] = "Rewind";
        keyNames[NativeDefinitions.KEY_PHONE] = "Phone";
        keyNames[NativeDefinitions.KEY_ISO] = "ISO";
        keyNames[NativeDefinitions.KEY_CONFIG] = "Config";
        keyNames[NativeDefinitions.KEY_HOMEPAGE] = "Home";
        keyNames[NativeDefinitions.KEY_REFRESH] = "Refresh";
        keyNames[NativeDefinitions.KEY_EXIT] = "Exit";
        keyNames[NativeDefinitions.KEY_MOVE] = "Move";
        keyNames[NativeDefinitions.KEY_EDIT] = "Edit";
        keyNames[NativeDefinitions.KEY_SCROLLUP] = "Scroll Up";
        keyNames[NativeDefinitions.KEY_SCROLLDOWN] = "Scroll Down";
        keyNames[NativeDefinitions.KEY_KPLEFTPAREN] = "KeyPad LH parenthesis";
        keyNames[NativeDefinitions.KEY_KPRIGHTPAREN] = "KeyPad RH parenthesis";
        keyNames[NativeDefinitions.KEY_INTL1] = "Intl 1";
        keyNames[NativeDefinitions.KEY_INTL2] = "Intl 2";
        keyNames[NativeDefinitions.KEY_INTL3] = "Intl 3";
        keyNames[NativeDefinitions.KEY_INTL4] = "Intl 4";
        keyNames[NativeDefinitions.KEY_INTL5] = "Intl 5";
        keyNames[NativeDefinitions.KEY_INTL6] = "Intl 6";
        keyNames[NativeDefinitions.KEY_INTL7] = "Intl 7";
        keyNames[NativeDefinitions.KEY_INTL8] = "Intl 8";
        keyNames[NativeDefinitions.KEY_INTL9] = "Intl 9";
        keyNames[NativeDefinitions.KEY_LANG1] = "Language 1";
        keyNames[NativeDefinitions.KEY_LANG2] = "Language 2";
        keyNames[NativeDefinitions.KEY_LANG3] = "Language 3";
        keyNames[NativeDefinitions.KEY_LANG4] = "Language 4";
        keyNames[NativeDefinitions.KEY_LANG5] = "Language 5";
        keyNames[NativeDefinitions.KEY_LANG6] = "Language 6";
        keyNames[NativeDefinitions.KEY_LANG7] = "Language 7";
        keyNames[NativeDefinitions.KEY_LANG8] = "Language 8";
        keyNames[NativeDefinitions.KEY_LANG9] = "Language 9";
        keyNames[NativeDefinitions.KEY_PLAYCD] = "Play CD";
        keyNames[NativeDefinitions.KEY_PAUSECD] = "Pause CD";
        keyNames[NativeDefinitions.KEY_PROG3] = "Program 3";
        keyNames[NativeDefinitions.KEY_PROG4] = "Program 4";
        keyNames[NativeDefinitions.KEY_SUSPEND] = "Suspend";
        keyNames[NativeDefinitions.KEY_CLOSE] = "Close";
        keyNames[NativeDefinitions.KEY_UNKNOWN] = "Specifically unknown";
        keyNames[NativeDefinitions.KEY_BRIGHTNESSDOWN] = "Brightness Down";
        keyNames[NativeDefinitions.KEY_BRIGHTNESSUP] = "Brightness Up";
        keyNames[NativeDefinitions.BTN_0] = "Button 0";
        keyNames[NativeDefinitions.BTN_1] = "Button 1";
        keyNames[NativeDefinitions.BTN_2] = "Button 2";
        keyNames[NativeDefinitions.BTN_3] = "Button 3";
        keyNames[NativeDefinitions.BTN_4] = "Button 4";
        keyNames[NativeDefinitions.BTN_5] = "Button 5";
        keyNames[NativeDefinitions.BTN_6] = "Button 6";
        keyNames[NativeDefinitions.BTN_7] = "Button 7";
        keyNames[NativeDefinitions.BTN_8] = "Button 8";
        keyNames[NativeDefinitions.BTN_9] = "Button 9";
        keyNames[NativeDefinitions.BTN_LEFT] = "Left Button";
        keyNames[NativeDefinitions.BTN_RIGHT] = "Right Button";
        keyNames[NativeDefinitions.BTN_MIDDLE] = "Middle Button";
        keyNames[NativeDefinitions.BTN_SIDE] = "Side Button";
        keyNames[NativeDefinitions.BTN_EXTRA] = "Extra Button";
        keyNames[NativeDefinitions.BTN_FORWARD] = "Forward Button";
        keyNames[NativeDefinitions.BTN_BACK] = "Back Button";
        keyNames[NativeDefinitions.BTN_TRIGGER] = "Trigger Button";
        keyNames[NativeDefinitions.BTN_THUMB] = "Thumb Button";
        keyNames[NativeDefinitions.BTN_THUMB2] = "Second Thumb Button";
        keyNames[NativeDefinitions.BTN_TOP] = "Top Button";
        keyNames[NativeDefinitions.BTN_TOP2] = "Second Top Button";
        keyNames[NativeDefinitions.BTN_PINKIE] = "Pinkie Button";
        keyNames[NativeDefinitions.BTN_BASE] = "Base Button";
        keyNames[NativeDefinitions.BTN_BASE2] = "Second Base Button";
        keyNames[NativeDefinitions.BTN_BASE3] = "Third Base Button";
        keyNames[NativeDefinitions.BTN_BASE4] = "Fourth Base Button";
        keyNames[NativeDefinitions.BTN_BASE5] = "Fifth Base Button";
        keyNames[NativeDefinitions.BTN_BASE6] = "Sixth Base Button";
        keyNames[NativeDefinitions.BTN_DEAD] = "Dead Button";
        keyNames[NativeDefinitions.BTN_A] = "Button A";
        keyNames[NativeDefinitions.BTN_B] = "Button B";
        keyNames[NativeDefinitions.BTN_C] = "Button C";
        keyNames[NativeDefinitions.BTN_X] = "Button X";
        keyNames[NativeDefinitions.BTN_Y] = "Button Y";
        keyNames[NativeDefinitions.BTN_Z] = "Button Z";
        keyNames[NativeDefinitions.BTN_TL] = "Thumb Left Button";
        keyNames[NativeDefinitions.BTN_TR] = "Thumb Right Button ";
        keyNames[NativeDefinitions.BTN_TL2] = "Second Thumb Left Button";
        keyNames[NativeDefinitions.BTN_TR2] = "Second Thumb Right Button ";
        keyNames[NativeDefinitions.BTN_SELECT] = "Select Button";
        keyNames[NativeDefinitions.BTN_MODE] = "Mode Button";
        keyNames[NativeDefinitions.BTN_THUMBL] = "Another Left Thumb Button ";
        keyNames[NativeDefinitions.BTN_THUMBR] = "Another Right Thumb Button ";
        keyNames[NativeDefinitions.BTN_TOOL_PEN] = "Digitiser Pen Tool";
        keyNames[NativeDefinitions.BTN_TOOL_RUBBER] = "Digitiser Rubber Tool";
        keyNames[NativeDefinitions.BTN_TOOL_BRUSH] = "Digitiser Brush Tool";
        keyNames[NativeDefinitions.BTN_TOOL_PENCIL] = "Digitiser Pencil Tool";
        keyNames[NativeDefinitions.BTN_TOOL_AIRBRUSH] = "Digitiser Airbrush Tool";
        keyNames[NativeDefinitions.BTN_TOOL_FINGER] = "Digitiser Finger Tool";
        keyNames[NativeDefinitions.BTN_TOOL_MOUSE] = "Digitiser Mouse Tool";
        keyNames[NativeDefinitions.BTN_TOOL_LENS] = "Digitiser Lens Tool";
        keyNames[NativeDefinitions.BTN_TOUCH] = "Digitiser Touch Button ";
        keyNames[NativeDefinitions.BTN_STYLUS] = "Digitiser Stylus Button ";
        keyNames[NativeDefinitions.BTN_STYLUS2] = "Second Digitiser Stylus Button ";

        buttonIDs = new Axis.Identifier[NativeDefinitions.KEY_MAX];
        buttonIDs[NativeDefinitions.KEY_ESC] = StandardKeyboard.KeyID.ESCAPE;
        buttonIDs[NativeDefinitions.KEY_1] = StandardKeyboard.KeyID._1;
        buttonIDs[NativeDefinitions.KEY_2] = StandardKeyboard.KeyID._2;
        buttonIDs[NativeDefinitions.KEY_3] = StandardKeyboard.KeyID._3;
        buttonIDs[NativeDefinitions.KEY_4] = StandardKeyboard.KeyID._4;
        buttonIDs[NativeDefinitions.KEY_5] = StandardKeyboard.KeyID._5;
        buttonIDs[NativeDefinitions.KEY_6] = StandardKeyboard.KeyID._6;
        buttonIDs[NativeDefinitions.KEY_7] = StandardKeyboard.KeyID._7;
        buttonIDs[NativeDefinitions.KEY_8] = StandardKeyboard.KeyID._8;
        buttonIDs[NativeDefinitions.KEY_9] = StandardKeyboard.KeyID._9;
        buttonIDs[NativeDefinitions.KEY_0] = StandardKeyboard.KeyID._0;
        buttonIDs[NativeDefinitions.KEY_MINUS] = StandardKeyboard.KeyID.MINUS;
        buttonIDs[NativeDefinitions.KEY_EQUAL] = StandardKeyboard.KeyID.EQUALS;
        buttonIDs[NativeDefinitions.KEY_BACKSPACE] = StandardKeyboard.KeyID.BACK;
        buttonIDs[NativeDefinitions.KEY_TAB] = StandardKeyboard.KeyID.TAB;
        buttonIDs[NativeDefinitions.KEY_Q] = StandardKeyboard.KeyID.Q;
        buttonIDs[NativeDefinitions.KEY_W] = StandardKeyboard.KeyID.W;
        buttonIDs[NativeDefinitions.KEY_E] = StandardKeyboard.KeyID.E;
        buttonIDs[NativeDefinitions.KEY_R] = StandardKeyboard.KeyID.R;
        buttonIDs[NativeDefinitions.KEY_T] = StandardKeyboard.KeyID.T;
        buttonIDs[NativeDefinitions.KEY_Y] = StandardKeyboard.KeyID.Y;
        buttonIDs[NativeDefinitions.KEY_U] = StandardKeyboard.KeyID.U;
        buttonIDs[NativeDefinitions.KEY_I] = StandardKeyboard.KeyID.I;
        buttonIDs[NativeDefinitions.KEY_O] = StandardKeyboard.KeyID.O;
        buttonIDs[NativeDefinitions.KEY_P] = StandardKeyboard.KeyID.P;
        buttonIDs[NativeDefinitions.KEY_LEFTBRACE] = StandardKeyboard.KeyID.LBRACKET;
        buttonIDs[NativeDefinitions.KEY_RIGHTBRACE] = StandardKeyboard.KeyID.RBRACKET;
        buttonIDs[NativeDefinitions.KEY_ENTER] = StandardKeyboard.KeyID.RETURN;
        buttonIDs[NativeDefinitions.KEY_LEFTCTRL] = StandardKeyboard.KeyID.LCONTROL;
        buttonIDs[NativeDefinitions.KEY_A] = StandardKeyboard.KeyID.A;
        buttonIDs[NativeDefinitions.KEY_S] = StandardKeyboard.KeyID.S;
        buttonIDs[NativeDefinitions.KEY_D] = StandardKeyboard.KeyID.D;
        buttonIDs[NativeDefinitions.KEY_F] = StandardKeyboard.KeyID.F;
        buttonIDs[NativeDefinitions.KEY_G] = StandardKeyboard.KeyID.G;
        buttonIDs[NativeDefinitions.KEY_H] = StandardKeyboard.KeyID.H;
        buttonIDs[NativeDefinitions.KEY_J] = StandardKeyboard.KeyID.J;
        buttonIDs[NativeDefinitions.KEY_K] = StandardKeyboard.KeyID.K;
        buttonIDs[NativeDefinitions.KEY_L] = StandardKeyboard.KeyID.L;
        buttonIDs[NativeDefinitions.KEY_SEMICOLON] = StandardKeyboard.KeyID.SEMICOLON;
        buttonIDs[NativeDefinitions.KEY_APOSTROPHE] = StandardKeyboard.KeyID.APOSTROPHE;
        buttonIDs[NativeDefinitions.KEY_GRAVE] = StandardKeyboard.KeyID.GRAVE;
        buttonIDs[NativeDefinitions.KEY_LEFTSHIFT] = StandardKeyboard.KeyID.LSHIFT;
        buttonIDs[NativeDefinitions.KEY_BACKSLASH] = StandardKeyboard.KeyID.BACKSLASH;
        buttonIDs[NativeDefinitions.KEY_Z] = StandardKeyboard.KeyID.Z;
        buttonIDs[NativeDefinitions.KEY_X] = StandardKeyboard.KeyID.X;
        buttonIDs[NativeDefinitions.KEY_C] = StandardKeyboard.KeyID.C;
        buttonIDs[NativeDefinitions.KEY_V] = StandardKeyboard.KeyID.V;
        buttonIDs[NativeDefinitions.KEY_B] = StandardKeyboard.KeyID.B;
        buttonIDs[NativeDefinitions.KEY_N] = StandardKeyboard.KeyID.N;
        buttonIDs[NativeDefinitions.KEY_M] = StandardKeyboard.KeyID.M;
        buttonIDs[NativeDefinitions.KEY_COMMA] = StandardKeyboard.KeyID.COMMA;
        buttonIDs[NativeDefinitions.KEY_DOT] = StandardKeyboard.KeyID.PERIOD;
        buttonIDs[NativeDefinitions.KEY_SLASH] = StandardKeyboard.KeyID.SLASH;
        buttonIDs[NativeDefinitions.KEY_RIGHTSHIFT] = StandardKeyboard.KeyID.RSHIFT;
        buttonIDs[NativeDefinitions.KEY_KPASTERISK] = StandardKeyboard.KeyID.MULTIPLY;
        buttonIDs[NativeDefinitions.KEY_LEFTALT] = StandardKeyboard.KeyID.LALT;
        buttonIDs[NativeDefinitions.KEY_SPACE] = StandardKeyboard.KeyID.SPACE;
        buttonIDs[NativeDefinitions.KEY_CAPSLOCK] = StandardKeyboard.KeyID.CAPITAL;
        buttonIDs[NativeDefinitions.KEY_F1] = StandardKeyboard.KeyID.F1;
        buttonIDs[NativeDefinitions.KEY_F2] = StandardKeyboard.KeyID.F2;
        buttonIDs[NativeDefinitions.KEY_F3] = StandardKeyboard.KeyID.F3;
        buttonIDs[NativeDefinitions.KEY_F4] = StandardKeyboard.KeyID.F4;
        buttonIDs[NativeDefinitions.KEY_F5] = StandardKeyboard.KeyID.F5;
        buttonIDs[NativeDefinitions.KEY_F6] = StandardKeyboard.KeyID.F6;
        buttonIDs[NativeDefinitions.KEY_F7] = StandardKeyboard.KeyID.F7;
        buttonIDs[NativeDefinitions.KEY_F8] = StandardKeyboard.KeyID.F8;
        buttonIDs[NativeDefinitions.KEY_F9] = StandardKeyboard.KeyID.F9;
        buttonIDs[NativeDefinitions.KEY_F10] = StandardKeyboard.KeyID.F10;
        buttonIDs[NativeDefinitions.KEY_NUMLOCK] = StandardKeyboard.KeyID.NUMLOCK;
        buttonIDs[NativeDefinitions.KEY_SCROLLLOCK] = StandardKeyboard.KeyID.SCROLL;
        buttonIDs[NativeDefinitions.KEY_KP7] = StandardKeyboard.KeyID.NUMPAD7;
        buttonIDs[NativeDefinitions.KEY_KP8] = StandardKeyboard.KeyID.NUMPAD8;
        buttonIDs[NativeDefinitions.KEY_KP9] = StandardKeyboard.KeyID.NUMPAD9;
        buttonIDs[NativeDefinitions.KEY_KPMINUS] = StandardKeyboard.KeyID.SUBTRACT;
        buttonIDs[NativeDefinitions.KEY_KP4] = StandardKeyboard.KeyID.NUMPAD4;
        buttonIDs[NativeDefinitions.KEY_KP5] = StandardKeyboard.KeyID.NUMPAD5;
        buttonIDs[NativeDefinitions.KEY_KP6] = StandardKeyboard.KeyID.NUMPAD6;
        buttonIDs[NativeDefinitions.KEY_KPPLUS] = StandardKeyboard.KeyID.ADD;
        buttonIDs[NativeDefinitions.KEY_KP1] = StandardKeyboard.KeyID.NUMPAD1;
        buttonIDs[NativeDefinitions.KEY_KP2] = StandardKeyboard.KeyID.NUMPAD2;
        buttonIDs[NativeDefinitions.KEY_KP3] = StandardKeyboard.KeyID.NUMPAD3;
        buttonIDs[NativeDefinitions.KEY_KP0] = StandardKeyboard.KeyID.NUMPAD0;
        buttonIDs[NativeDefinitions.KEY_KPDOT] = StandardKeyboard.KeyID.DECIMAL;
        buttonIDs[NativeDefinitions.KEY_103RD] = null;
        buttonIDs[NativeDefinitions.KEY_F13] = StandardKeyboard.KeyID.F13;
        buttonIDs[NativeDefinitions.KEY_102ND] = null;
        buttonIDs[NativeDefinitions.KEY_F11] = StandardKeyboard.KeyID.F11;
        buttonIDs[NativeDefinitions.KEY_F12] = StandardKeyboard.KeyID.F12;
        buttonIDs[NativeDefinitions.KEY_F14] = StandardKeyboard.KeyID.F14;
        buttonIDs[NativeDefinitions.KEY_F15] = StandardKeyboard.KeyID.F15;
        buttonIDs[NativeDefinitions.KEY_F16] = null;
        buttonIDs[NativeDefinitions.KEY_F17] = null;
        buttonIDs[NativeDefinitions.KEY_F18] = null;
        buttonIDs[NativeDefinitions.KEY_F19] = null;
        buttonIDs[NativeDefinitions.KEY_F20] = null;
        buttonIDs[NativeDefinitions.KEY_KPENTER] = StandardKeyboard.KeyID.NUMPADENTER;
        buttonIDs[NativeDefinitions.KEY_RIGHTCTRL] = StandardKeyboard.KeyID.RCONTROL;
        buttonIDs[NativeDefinitions.KEY_KPSLASH] = StandardKeyboard.KeyID.DIVIDE;
        buttonIDs[NativeDefinitions.KEY_SYSRQ] = StandardKeyboard.KeyID.SYSRQ;
        buttonIDs[NativeDefinitions.KEY_RIGHTALT] = StandardKeyboard.KeyID.RALT;
        buttonIDs[NativeDefinitions.KEY_LINEFEED] = null;
        buttonIDs[NativeDefinitions.KEY_HOME] = StandardKeyboard.KeyID.HOME;
        buttonIDs[NativeDefinitions.KEY_UP] = StandardKeyboard.KeyID.UP;
        buttonIDs[NativeDefinitions.KEY_PAGEUP] = LinuxKeyboard.KeyID.PAGEUP;
        buttonIDs[NativeDefinitions.KEY_LEFT] = StandardKeyboard.KeyID.LEFT;
        buttonIDs[NativeDefinitions.KEY_RIGHT] = StandardKeyboard.KeyID.RIGHT;
        buttonIDs[NativeDefinitions.KEY_END] = StandardKeyboard.KeyID.END;
        buttonIDs[NativeDefinitions.KEY_DOWN] = StandardKeyboard.KeyID.DOWN;
        buttonIDs[NativeDefinitions.KEY_PAGEDOWN] = LinuxKeyboard.KeyID.PAGEDOWN;
        buttonIDs[NativeDefinitions.KEY_INSERT] = StandardKeyboard.KeyID.INSERT;
        buttonIDs[NativeDefinitions.KEY_DELETE] = StandardKeyboard.KeyID.DELETE;
        buttonIDs[NativeDefinitions.KEY_PAUSE] = StandardKeyboard.KeyID.PAUSE;
/*        buttonIDs[NativeDefinitions.KEY_MACRO] = "Macro";
        buttonIDs[NativeDefinitions.KEY_MUTE] = "Mute";
        buttonIDs[NativeDefinitions.KEY_VOLUMEDOWN] = "Volume Down";
        buttonIDs[NativeDefinitions.KEY_VOLUMEUP] = "Volume Up";
        buttonIDs[NativeDefinitions.KEY_POWER] = "Power";*/
        buttonIDs[NativeDefinitions.KEY_KPEQUAL] = StandardKeyboard.KeyID.NUMPADEQUAL;
        //buttonIDs[NativeDefinitions.KEY_KPPLUSMINUS] = "KeyPad +/-";
/*        buttonIDs[NativeDefinitions.KEY_F21] = "F21";
        buttonIDs[NativeDefinitions.KEY_F22] = "F22";
        buttonIDs[NativeDefinitions.KEY_F23] = "F23";
        buttonIDs[NativeDefinitions.KEY_F24] = "F24";
        buttonIDs[NativeDefinitions.KEY_KPCOMMA] = "KeyPad comma";
        buttonIDs[NativeDefinitions.KEY_LEFTMETA] = "LH Meta";
        buttonIDs[NativeDefinitions.KEY_RIGHTMETA] = "RH Meta";
        buttonIDs[NativeDefinitions.KEY_COMPOSE] = "Compose";
        buttonIDs[NativeDefinitions.KEY_STOP] = "Stop";
        buttonIDs[NativeDefinitions.KEY_AGAIN] = "Again";
        buttonIDs[NativeDefinitions.KEY_PROPS] = "Properties";
        buttonIDs[NativeDefinitions.KEY_UNDO] = "Undo";
        buttonIDs[NativeDefinitions.KEY_FRONT] = "Front";
        buttonIDs[NativeDefinitions.KEY_COPY] = "Copy";
        buttonIDs[NativeDefinitions.KEY_OPEN] = "Open";
        buttonIDs[NativeDefinitions.KEY_PASTE] = "Paste";
        buttonIDs[NativeDefinitions.KEY_FIND] = "Find";
        buttonIDs[NativeDefinitions.KEY_CUT] = "Cut";
        buttonIDs[NativeDefinitions.KEY_HELP] = "Help";
        buttonIDs[NativeDefinitions.KEY_MENU] = "Menu";
        buttonIDs[NativeDefinitions.KEY_CALC] = "Calculator";
        buttonIDs[NativeDefinitions.KEY_SETUP] = "Setup";*/
        buttonIDs[NativeDefinitions.KEY_SLEEP] = StandardKeyboard.KeyID.SLEEP;
        /*buttonIDs[NativeDefinitions.KEY_WAKEUP] = "Wakeup";
        buttonIDs[NativeDefinitions.KEY_FILE] = "File";
        buttonIDs[NativeDefinitions.KEY_SENDFILE] = "Send File";
        buttonIDs[NativeDefinitions.KEY_DELETEFILE] = "Delete File";
        buttonIDs[NativeDefinitions.KEY_XFER] = "Transfer";
        buttonIDs[NativeDefinitions.KEY_PROG1] = "Program 1";
        buttonIDs[NativeDefinitions.KEY_PROG2] = "Program 2";
        buttonIDs[NativeDefinitions.KEY_WWW] = "Web Browser";
        buttonIDs[NativeDefinitions.KEY_MSDOS] = "DOS mode";
        buttonIDs[NativeDefinitions.KEY_COFFEE] = "Coffee";
        buttonIDs[NativeDefinitions.KEY_DIRECTION] = "Direction";
        buttonIDs[NativeDefinitions.KEY_CYCLEWINDOWS] = "Window cycle";
        buttonIDs[NativeDefinitions.KEY_MAIL] = "Mail";
        buttonIDs[NativeDefinitions.KEY_BOOKMARKS] = "Book Marks";
        buttonIDs[NativeDefinitions.KEY_COMPUTER] = "Computer";
        buttonIDs[NativeDefinitions.KEY_BACK] = "Back";
        buttonIDs[NativeDefinitions.KEY_FORWARD] = "Forward";
        buttonIDs[NativeDefinitions.KEY_CLOSECD] = "Close CD";
        buttonIDs[NativeDefinitions.KEY_EJECTCD] = "Eject CD";
        buttonIDs[NativeDefinitions.KEY_EJECTCLOSECD] = "Eject / Close CD";
        buttonIDs[NativeDefinitions.KEY_NEXTSONG] = "Next Song";
        buttonIDs[NativeDefinitions.KEY_PLAYPAUSE] = "Play and Pause";
        buttonIDs[NativeDefinitions.KEY_PREVIOUSSONG] = "Previous Song";
        buttonIDs[NativeDefinitions.KEY_STOPCD] = "Stop CD";
        buttonIDs[NativeDefinitions.KEY_RECORD] = "Record";
        buttonIDs[NativeDefinitions.KEY_REWIND] = "Rewind";
        buttonIDs[NativeDefinitions.KEY_PHONE] = "Phone";
        buttonIDs[NativeDefinitions.KEY_ISO] = "ISO";
        buttonIDs[NativeDefinitions.KEY_CONFIG] = "Config";
        buttonIDs[NativeDefinitions.KEY_HOMEPAGE] = "Home";
        buttonIDs[NativeDefinitions.KEY_REFRESH] = "Refresh";
        buttonIDs[NativeDefinitions.KEY_EXIT] = "Exit";
        buttonIDs[NativeDefinitions.KEY_MOVE] = "Move";
        buttonIDs[NativeDefinitions.KEY_EDIT] = "Edit";
        buttonIDs[NativeDefinitions.KEY_SCROLLUP] = "Scroll Up";
        buttonIDs[NativeDefinitions.KEY_SCROLLDOWN] = "Scroll Down";
        buttonIDs[NativeDefinitions.KEY_KPLEFTPAREN] = "KeyPad LH parenthesis";
        buttonIDs[NativeDefinitions.KEY_KPRIGHTPAREN] = "KeyPad RH parenthesis";
        buttonIDs[NativeDefinitions.KEY_INTL1] = "Intl 1";
        buttonIDs[NativeDefinitions.KEY_INTL2] = "Intl 2";
        buttonIDs[NativeDefinitions.KEY_INTL3] = "Intl 3";
        buttonIDs[NativeDefinitions.KEY_INTL4] = "Intl 4";
        buttonIDs[NativeDefinitions.KEY_INTL5] = "Intl 5";
        buttonIDs[NativeDefinitions.KEY_INTL6] = "Intl 6";
        buttonIDs[NativeDefinitions.KEY_INTL7] = "Intl 7";
        buttonIDs[NativeDefinitions.KEY_INTL8] = "Intl 8";
        buttonIDs[NativeDefinitions.KEY_INTL9] = "Intl 9";
        buttonIDs[NativeDefinitions.KEY_LANG1] = "Language 1";
        buttonIDs[NativeDefinitions.KEY_LANG2] = "Language 2";
        buttonIDs[NativeDefinitions.KEY_LANG3] = "Language 3";
        buttonIDs[NativeDefinitions.KEY_LANG4] = "Language 4";
        buttonIDs[NativeDefinitions.KEY_LANG5] = "Language 5";
        buttonIDs[NativeDefinitions.KEY_LANG6] = "Language 6";
        buttonIDs[NativeDefinitions.KEY_LANG7] = "Language 7";
        buttonIDs[NativeDefinitions.KEY_LANG8] = "Language 8";
        buttonIDs[NativeDefinitions.KEY_LANG9] = "Language 9";
        buttonIDs[NativeDefinitions.KEY_PLAYCD] = "Play CD";
        buttonIDs[NativeDefinitions.KEY_PAUSECD] = "Pause CD";
        buttonIDs[NativeDefinitions.KEY_PROG3] = "Program 3";
        buttonIDs[NativeDefinitions.KEY_PROG4] = "Program 4";
        buttonIDs[NativeDefinitions.KEY_SUSPEND] = "Suspend";
        buttonIDs[NativeDefinitions.KEY_CLOSE] = "Close";*/
        buttonIDs[NativeDefinitions.KEY_UNKNOWN] = StandardKeyboard.KeyID.UNLABELED;
        /*buttonIDs[NativeDefinitions.KEY_BRIGHTNESSDOWN] = "Brightness Down";
        buttonIDs[NativeDefinitions.KEY_BRIGHTNESSUP] = "Brightness Up";*/
        
        //Msic keys
        buttonIDs[NativeDefinitions.BTN_0] = LinuxDevice.ButtonID.BTN_0;
        buttonIDs[NativeDefinitions.BTN_1] = LinuxDevice.ButtonID.BTN_1;
        buttonIDs[NativeDefinitions.BTN_2] = LinuxDevice.ButtonID.BTN_2;
        buttonIDs[NativeDefinitions.BTN_3] = LinuxDevice.ButtonID.BTN_3;
        buttonIDs[NativeDefinitions.BTN_4] = LinuxDevice.ButtonID.BTN_4;
        buttonIDs[NativeDefinitions.BTN_5] = LinuxDevice.ButtonID.BTN_5;
        buttonIDs[NativeDefinitions.BTN_6] = LinuxDevice.ButtonID.BTN_6;
        buttonIDs[NativeDefinitions.BTN_7] = LinuxDevice.ButtonID.BTN_7;
        buttonIDs[NativeDefinitions.BTN_8] = LinuxDevice.ButtonID.BTN_8;
        buttonIDs[NativeDefinitions.BTN_9] = LinuxDevice.ButtonID.BTN_9;
        
        // Mouse
        buttonIDs[NativeDefinitions.BTN_LEFT] = Mouse.ButtonID.LEFT;
        buttonIDs[NativeDefinitions.BTN_RIGHT] = Mouse.ButtonID.RIGHT;
        buttonIDs[NativeDefinitions.BTN_MIDDLE] = Mouse.ButtonID.MIDDLE;
        buttonIDs[NativeDefinitions.BTN_SIDE] = Mouse.ButtonID.SIDE;
        buttonIDs[NativeDefinitions.BTN_EXTRA] = Mouse.ButtonID.EXTRA;
        buttonIDs[NativeDefinitions.BTN_FORWARD] = Mouse.ButtonID.FORWARD;
        buttonIDs[NativeDefinitions.BTN_BACK] = Mouse.ButtonID.BACK;
        
        // Joystick
        buttonIDs[NativeDefinitions.BTN_TRIGGER] = LinuxDevice.ButtonID.BTN_TRIGGER;
        buttonIDs[NativeDefinitions.BTN_THUMB] = LinuxDevice.ButtonID.BTN_THUMB;
        buttonIDs[NativeDefinitions.BTN_THUMB2] = LinuxDevice.ButtonID.BTN_THUMB2;
        buttonIDs[NativeDefinitions.BTN_TOP] = LinuxDevice.ButtonID.BTN_TOP;
        buttonIDs[NativeDefinitions.BTN_TOP2] = LinuxDevice.ButtonID.BTN_TOP2;
        buttonIDs[NativeDefinitions.BTN_PINKIE] = LinuxDevice.ButtonID.BTN_PINKIE;
        buttonIDs[NativeDefinitions.BTN_BASE] = LinuxDevice.ButtonID.BTN_BASE;
        buttonIDs[NativeDefinitions.BTN_BASE2] = LinuxDevice.ButtonID.BTN_BASE2;
        buttonIDs[NativeDefinitions.BTN_BASE3] = LinuxDevice.ButtonID.BTN_BASE3;
        buttonIDs[NativeDefinitions.BTN_BASE4] = LinuxDevice.ButtonID.BTN_BASE4;
        buttonIDs[NativeDefinitions.BTN_BASE5] = LinuxDevice.ButtonID.BTN_BASE5;
        buttonIDs[NativeDefinitions.BTN_BASE6] = LinuxDevice.ButtonID.BTN_BASE6;
        buttonIDs[NativeDefinitions.BTN_DEAD] = LinuxDevice.ButtonID.BTN_DEAD;
        
        // Gamepad
        buttonIDs[NativeDefinitions.BTN_A] = LinuxDevice.ButtonID.BTN_A;
        buttonIDs[NativeDefinitions.BTN_B] = LinuxDevice.ButtonID.BTN_B;
        buttonIDs[NativeDefinitions.BTN_C] = LinuxDevice.ButtonID.BTN_C;
        buttonIDs[NativeDefinitions.BTN_X] = LinuxDevice.ButtonID.BTN_X;
        buttonIDs[NativeDefinitions.BTN_Y] = LinuxDevice.ButtonID.BTN_Y;
        buttonIDs[NativeDefinitions.BTN_Z] = LinuxDevice.ButtonID.BTN_Z;
        buttonIDs[NativeDefinitions.BTN_TL] = LinuxDevice.ButtonID.BTN_TL;
        buttonIDs[NativeDefinitions.BTN_TR] = LinuxDevice.ButtonID.BTN_TR;
        buttonIDs[NativeDefinitions.BTN_TL2] = LinuxDevice.ButtonID.BTN_TL2;
        buttonIDs[NativeDefinitions.BTN_TR2] = LinuxDevice.ButtonID.BTN_TR2;
        buttonIDs[NativeDefinitions.BTN_SELECT] = LinuxDevice.ButtonID.BTN_SELECT;
        buttonIDs[NativeDefinitions.BTN_MODE] = LinuxDevice.ButtonID.BTN_MODE;
        buttonIDs[NativeDefinitions.BTN_THUMBL] = LinuxDevice.ButtonID.BTN_THUMBL;
        buttonIDs[NativeDefinitions.BTN_THUMBR] = LinuxDevice.ButtonID.BTN_THUMBR;
        
        // Digitiser
        buttonIDs[NativeDefinitions.BTN_TOOL_PEN] = LinuxDevice.ButtonID.BTN_TOOL_PEN;
        buttonIDs[NativeDefinitions.BTN_TOOL_RUBBER] = LinuxDevice.ButtonID.BTN_TOOL_RUBBER;
        buttonIDs[NativeDefinitions.BTN_TOOL_BRUSH] = LinuxDevice.ButtonID.BTN_TOOL_BRUSH;
        buttonIDs[NativeDefinitions.BTN_TOOL_PENCIL] = LinuxDevice.ButtonID.BTN_TOOL_PENCIL;
        buttonIDs[NativeDefinitions.BTN_TOOL_AIRBRUSH] = LinuxDevice.ButtonID.BTN_TOOL_AIRBRUSH;
        buttonIDs[NativeDefinitions.BTN_TOOL_FINGER] = LinuxDevice.ButtonID.BTN_TOOL_FINGER;
        buttonIDs[NativeDefinitions.BTN_TOOL_MOUSE] = LinuxDevice.ButtonID.BTN_TOOL_MOUSE;
        buttonIDs[NativeDefinitions.BTN_TOOL_LENS] = LinuxDevice.ButtonID.BTN_TOOL_LENS;
        buttonIDs[NativeDefinitions.BTN_TOUCH] = LinuxDevice.ButtonID.BTN_TOUCH;
        buttonIDs[NativeDefinitions.BTN_STYLUS] = LinuxDevice.ButtonID.BTN_STYLUS;
        buttonIDs[NativeDefinitions.BTN_STYLUS2] = LinuxDevice.ButtonID.BTN_STYLUS2;
        
        relAxesNames = new String[NativeDefinitions.REL_MAX];
        relAxesNames[NativeDefinitions.REL_X] = "X axis";
        relAxesNames[NativeDefinitions.REL_Y] = "Y axis";
        relAxesNames[NativeDefinitions.REL_Z] = "Z axis";
        relAxesNames[NativeDefinitions.REL_HWHEEL] ="Horizontal wheel";
        relAxesNames[NativeDefinitions.REL_DIAL] = "Dial";
        relAxesNames[NativeDefinitions.REL_WHEEL] = "Vertical wheel";
        relAxesNames[NativeDefinitions.REL_MISC] = "Miscellaneous";
        
        relAxesIDs = new Axis.Identifier[NativeDefinitions.REL_MAX];
        relAxesIDs[NativeDefinitions.REL_X] = Axis.Identifier.X;
        relAxesIDs[NativeDefinitions.REL_Y] = Axis.Identifier.Y;
        relAxesIDs[NativeDefinitions.REL_Z] = Axis.Identifier.Z;
        relAxesIDs[NativeDefinitions.REL_WHEEL] = Axis.Identifier.SLIDER;
        // There are guesses as I have no idea what they would be used for
        relAxesIDs[NativeDefinitions.REL_HWHEEL] = Axis.Identifier.SLIDER;
        relAxesIDs[NativeDefinitions.REL_DIAL] = Axis.Identifier.SLIDER;
        relAxesIDs[NativeDefinitions.REL_MISC] = Axis.Identifier.SLIDER;
        
        absAxesNames = new String[NativeDefinitions.ABS_MAX];
        absAxesNames[NativeDefinitions.ABS_X] = "X axis";
        absAxesNames[NativeDefinitions.ABS_Y] = "Y axis";
        absAxesNames[NativeDefinitions.ABS_Z] = "Z axis";        
        absAxesNames[NativeDefinitions.ABS_RX] = "X rate axis";
        absAxesNames[NativeDefinitions.ABS_RY] = "Y rate axis";
        absAxesNames[NativeDefinitions.ABS_RZ] = "Z rate axis";
        absAxesNames[NativeDefinitions.ABS_THROTTLE] = "Throttle";
        absAxesNames[NativeDefinitions.ABS_RUDDER] = "Rudder";
        absAxesNames[NativeDefinitions.ABS_WHEEL] = "Wheel";
        absAxesNames[NativeDefinitions.ABS_GAS] = "Accelerator";
        absAxesNames[NativeDefinitions.ABS_BRAKE] = "Brake";
        // Hats are done this way as they are mapped from two axis down to one
        absAxesNames[NativeDefinitions.ABS_HAT0X] = "Hat 1";
        absAxesNames[NativeDefinitions.ABS_HAT0Y] = "Hat 1";
        absAxesNames[NativeDefinitions.ABS_HAT1X] = "Hat 2";
        absAxesNames[NativeDefinitions.ABS_HAT1Y] = "Hat 2";
        absAxesNames[NativeDefinitions.ABS_HAT2X] = "Hat 3";
        absAxesNames[NativeDefinitions.ABS_HAT2Y] = "Hat 3";
        absAxesNames[NativeDefinitions.ABS_HAT3X] = "Hat 4";
        absAxesNames[NativeDefinitions.ABS_HAT3Y] = "Hat 4";
        absAxesNames[NativeDefinitions.ABS_PRESSURE] = "Pressure";
        absAxesNames[NativeDefinitions.ABS_DISTANCE] = "Distance";
        absAxesNames[NativeDefinitions.ABS_TILT_X] = "X axis tilt";
        absAxesNames[NativeDefinitions.ABS_TILT_Y] = "Y axis tilt";
        absAxesNames[NativeDefinitions.ABS_MISC] = "Miscellaneous";
        
        absAxesIDs = new Axis.Identifier[NativeDefinitions.ABS_MAX];
        absAxesIDs[NativeDefinitions.ABS_X] = Axis.Identifier.X;
        absAxesIDs[NativeDefinitions.ABS_Y] = Axis.Identifier.Y;
        absAxesIDs[NativeDefinitions.ABS_Z] = Axis.Identifier.Z;
        absAxesIDs[NativeDefinitions.ABS_RX] = Axis.Identifier.RX;
        absAxesIDs[NativeDefinitions.ABS_RY] = Axis.Identifier.RY;
        absAxesIDs[NativeDefinitions.ABS_RZ] = Axis.Identifier.RZ;
        absAxesIDs[NativeDefinitions.ABS_THROTTLE] = Axis.Identifier.SLIDER;
        absAxesIDs[NativeDefinitions.ABS_RUDDER] = Axis.Identifier.RZ;
        absAxesIDs[NativeDefinitions.ABS_WHEEL] = Axis.Identifier.Y;
        absAxesIDs[NativeDefinitions.ABS_GAS] = Axis.Identifier.SLIDER;
        absAxesIDs[NativeDefinitions.ABS_BRAKE] = Axis.Identifier.SLIDER;
        // Hats are done this way as they are mapped from two axis down to one
        absAxesIDs[NativeDefinitions.ABS_HAT0X] = Axis.Identifier.POV;
        absAxesIDs[NativeDefinitions.ABS_HAT0Y] = Axis.Identifier.POV;
        absAxesIDs[NativeDefinitions.ABS_HAT1X] = Axis.Identifier.POV;
        absAxesIDs[NativeDefinitions.ABS_HAT1Y] = Axis.Identifier.POV;
        absAxesIDs[NativeDefinitions.ABS_HAT2X] = Axis.Identifier.POV;
        absAxesIDs[NativeDefinitions.ABS_HAT2Y] = Axis.Identifier.POV;
        absAxesIDs[NativeDefinitions.ABS_HAT3X] = Axis.Identifier.POV;
        absAxesIDs[NativeDefinitions.ABS_HAT3Y] = Axis.Identifier.POV;
        // erm, yeah
        absAxesIDs[NativeDefinitions.ABS_PRESSURE] = null;
        absAxesIDs[NativeDefinitions.ABS_DISTANCE] = null;
        absAxesIDs[NativeDefinitions.ABS_TILT_X] = null;
        absAxesIDs[NativeDefinitions.ABS_TILT_Y] = null;
        absAxesIDs[NativeDefinitions.ABS_MISC] = null;
        
    }
    
    /** Return port type from a native port type int id
     * @param nativeid The native port type
     * @return The jinput port type
     */    
    public static Controller.PortType getPortType(int nativeid) {
        // Have to do this one this way as there is no BUS_MAX
        switch (nativeid) {
            case NativeDefinitions.BUS_GAMEPORT :
                return Controller.PortType.GAME;
            case NativeDefinitions.BUS_I8042 :
                return Controller.PortType.I8042;
            case NativeDefinitions.BUS_PARPORT :
                return Controller.PortType.PARALLEL;
            case NativeDefinitions.BUS_RS232 :
                return Controller.PortType.SERIAL;
            case NativeDefinitions.BUS_USB :
                return Controller.PortType.USB;
            default:
                return Controller.PortType.UNKNOWN;
        }
    }
    
    /** Returns the name of a native button
     * @param nativeID The native button type id
     * @return The button name
     */    
    public static String getButtonName(int nativeID) {
        String retval = INSTANCE.keyNames[nativeID];
        //if(retval == null){
        //    retval = "Unknown button id";
        //    INSTANCE.keyNames[nativeID] = retval;
        //}
        return retval;
    }
    
    /** Retursn the name of the native relative axis
     * @param nativeID The axis type ID
     * @return The axis name
     */    
    public static String getRelAxisName(int nativeID) {
        String retval = INSTANCE.relAxesNames[nativeID];
        if(retval == null) {
            retval = "Unknown relative axis id";
            INSTANCE.relAxesNames[nativeID] = retval;
        }
        return retval;
    }
    
    /** Retursn the name of the native absolute axis
     * @param nativeID The native axis type ID
     * @return The name of the axis
     */    
    public static String getAbsAxisName(int nativeID) {
        String retval = INSTANCE.absAxesNames[nativeID];
        if(retval == null) {
            retval = "Unknown absolute axis id";
            INSTANCE.absAxesNames[nativeID] = retval;
        }
        return retval;
    }
    
    /** Gets the identifier for a relative axis
     * @param nativeID The axis type ID
     * @return The jinput id
     */    
    public static Axis.Identifier getRelAxisID(int nativeID) {
        Axis.Identifier retval = INSTANCE.relAxesIDs[nativeID];
        if(retval == null) {
            retval = Axis.Identifier.SLIDER_VELOCITY;
            INSTANCE.relAxesIDs[nativeID] = retval;
        }
        return retval;
    }
    
    /** Gets the identifier for a absolute axis
     * @param nativeID The native axis type id
     * @return The jinput id
     */    
    public static Axis.Identifier getAbsAxisID(int nativeID) {
        Axis.Identifier retval = INSTANCE.absAxesIDs[nativeID];
        if(retval == null) {
            retval = Axis.Identifier.SLIDER;
            INSTANCE.absAxesIDs[nativeID] = retval;
        }
        return retval;
    }
    
    /** Gets the identifier for a button
     * @param nativeID The native button type id
     * @return The jinput id
     */    
    public static Axis.Identifier getButtonID(int nativeID) {
        Axis.Identifier retval = INSTANCE.buttonIDs[nativeID];
        if(retval == null) {
            retval = new LinuxKeyboard.KeyID(nativeID, getButtonName(nativeID));
            INSTANCE.buttonIDs[nativeID] = retval;
        }
        return retval;
    }
    
}
