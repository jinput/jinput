/**
 * Copyright (C) 2004 Jeremy Booth (jeremy@newdawnsoftware.com)
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

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;

import net.java.games.input.Keyboard.Key;

/**
 * @author Jeremy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AWTKeyboard extends StandardKeyboard implements AWTEventListener {

    private boolean[] buttonValues;
    
    private int[] buttonMap;
    
    /**
     * @param name
     */
    protected AWTKeyboard(String name) {
        super(name);
        
        buttonValues = new boolean[getAxes().length];
        buttonMap = new int[65535]; //has to be this big, as the values of KeyEvent keys are large
        
        buttonMap[KeyEvent.VK_0] = Component.Identifier.Key._0.getKeyIndex();
        buttonMap[KeyEvent.VK_1] = Component.Identifier.Key._1.getKeyIndex();
        buttonMap[KeyEvent.VK_2] = Component.Identifier.Key._2.getKeyIndex();
        buttonMap[KeyEvent.VK_3] = Component.Identifier.Key._3.getKeyIndex();
        buttonMap[KeyEvent.VK_4] = Component.Identifier.Key._4.getKeyIndex();
        buttonMap[KeyEvent.VK_5] = Component.Identifier.Key._5.getKeyIndex();
        buttonMap[KeyEvent.VK_6] = Component.Identifier.Key._6.getKeyIndex();
        buttonMap[KeyEvent.VK_7] = Component.Identifier.Key._7.getKeyIndex();
        buttonMap[KeyEvent.VK_8] = Component.Identifier.Key._8.getKeyIndex();
        buttonMap[KeyEvent.VK_9] = Component.Identifier.Key._9.getKeyIndex();
        
        buttonMap[KeyEvent.VK_Q] = Component.Identifier.Key.Q.getKeyIndex();
        buttonMap[KeyEvent.VK_W] = Component.Identifier.Key.W.getKeyIndex();
        buttonMap[KeyEvent.VK_E] = Component.Identifier.Key.E.getKeyIndex();
        buttonMap[KeyEvent.VK_R] = Component.Identifier.Key.R.getKeyIndex();
        buttonMap[KeyEvent.VK_T] = Component.Identifier.Key.T.getKeyIndex();
        buttonMap[KeyEvent.VK_Y] = Component.Identifier.Key.Y.getKeyIndex();
        buttonMap[KeyEvent.VK_U] = Component.Identifier.Key.U.getKeyIndex();
        buttonMap[KeyEvent.VK_I] = Component.Identifier.Key.I.getKeyIndex();
        buttonMap[KeyEvent.VK_O] = Component.Identifier.Key.O.getKeyIndex();
        buttonMap[KeyEvent.VK_P] = Component.Identifier.Key.P.getKeyIndex();
        buttonMap[KeyEvent.VK_A] = Component.Identifier.Key.A.getKeyIndex();
        buttonMap[KeyEvent.VK_S] = Component.Identifier.Key.S.getKeyIndex();
        buttonMap[KeyEvent.VK_D] = Component.Identifier.Key.D.getKeyIndex();
        buttonMap[KeyEvent.VK_F] = Component.Identifier.Key.F.getKeyIndex();
        buttonMap[KeyEvent.VK_G] = Component.Identifier.Key.G.getKeyIndex();
        buttonMap[KeyEvent.VK_H] = Component.Identifier.Key.H.getKeyIndex();
        buttonMap[KeyEvent.VK_J] = Component.Identifier.Key.J.getKeyIndex();
        buttonMap[KeyEvent.VK_K] = Component.Identifier.Key.K.getKeyIndex();
        buttonMap[KeyEvent.VK_L] = Component.Identifier.Key.L.getKeyIndex();
        buttonMap[KeyEvent.VK_Z] = Component.Identifier.Key.Z.getKeyIndex();
        buttonMap[KeyEvent.VK_X] = Component.Identifier.Key.X.getKeyIndex();
        buttonMap[KeyEvent.VK_C] = Component.Identifier.Key.C.getKeyIndex();
        buttonMap[KeyEvent.VK_V] = Component.Identifier.Key.V.getKeyIndex();
        buttonMap[KeyEvent.VK_B] = Component.Identifier.Key.B.getKeyIndex();
        buttonMap[KeyEvent.VK_N] = Component.Identifier.Key.N.getKeyIndex();
        buttonMap[KeyEvent.VK_M] = Component.Identifier.Key.M.getKeyIndex();
        
        buttonMap[KeyEvent.VK_F1] = Component.Identifier.Key.F1.getKeyIndex();
        buttonMap[KeyEvent.VK_F2] = Component.Identifier.Key.F2.getKeyIndex();
        buttonMap[KeyEvent.VK_F3] = Component.Identifier.Key.F3.getKeyIndex();
        buttonMap[KeyEvent.VK_F4] = Component.Identifier.Key.F4.getKeyIndex();
        buttonMap[KeyEvent.VK_F5] = Component.Identifier.Key.F5.getKeyIndex();
        buttonMap[KeyEvent.VK_F6] = Component.Identifier.Key.F6.getKeyIndex();
        buttonMap[KeyEvent.VK_F7] = Component.Identifier.Key.F7.getKeyIndex();
        buttonMap[KeyEvent.VK_F8] = Component.Identifier.Key.F8.getKeyIndex();
        buttonMap[KeyEvent.VK_F9] = Component.Identifier.Key.F9.getKeyIndex();
        buttonMap[KeyEvent.VK_F10] = Component.Identifier.Key.F10.getKeyIndex();
        buttonMap[KeyEvent.VK_F11] = Component.Identifier.Key.F11.getKeyIndex();
        buttonMap[KeyEvent.VK_F12] = Component.Identifier.Key.F12.getKeyIndex();
        
        buttonMap[KeyEvent.VK_ESCAPE] = Component.Identifier.Key.ESCAPE.getKeyIndex();
        buttonMap[KeyEvent.VK_MINUS] = Component.Identifier.Key.MINUS.getKeyIndex();
        buttonMap[KeyEvent.VK_EQUALS] = Component.Identifier.Key.EQUALS.getKeyIndex();
        buttonMap[KeyEvent.VK_BACK_SPACE] = Component.Identifier.Key.BACKSLASH.getKeyIndex();
        buttonMap[KeyEvent.VK_TAB] = Component.Identifier.Key.TAB.getKeyIndex();
        buttonMap[KeyEvent.VK_OPEN_BRACKET] = Component.Identifier.Key.LBRACKET.getKeyIndex();
        buttonMap[KeyEvent.VK_CLOSE_BRACKET] = Component.Identifier.Key.RBRACKET.getKeyIndex();
        //Enter is a special one
        //Control is a special one
        buttonMap[KeyEvent.VK_SEMICOLON] = Component.Identifier.Key.SEMICOLON.getKeyIndex();
        buttonMap[KeyEvent.VK_QUOTE] = Component.Identifier.Key.APOSTROPHE.getKeyIndex();
        buttonMap[KeyEvent.VK_NUMBER_SIGN] = Component.Identifier.Key.GRAVE.getKeyIndex();
        //Shift is a special one
        buttonMap[KeyEvent.VK_BACK_SLASH] = Component.Identifier.Key.BACKSLASH.getKeyIndex();
        //Comma is special
        buttonMap[KeyEvent.VK_PERIOD] = Component.Identifier.Key.PERIOD.getKeyIndex();
        buttonMap[KeyEvent.VK_SLASH] = Component.Identifier.Key.SLASH.getKeyIndex();  
        buttonMap[KeyEvent.VK_MULTIPLY] = Component.Identifier.Key.MULTIPLY.getKeyIndex();
        //Alt is a special one
        buttonMap[KeyEvent.VK_SPACE] = Component.Identifier.Key.SPACE.getKeyIndex();
        buttonMap[KeyEvent.VK_CAPS_LOCK] = Component.Identifier.Key.CAPITAL.getKeyIndex();
        buttonMap[KeyEvent.VK_NUM_LOCK] = Component.Identifier.Key.NUMLOCK.getKeyIndex();
        buttonMap[KeyEvent.VK_SCROLL_LOCK] = Component.Identifier.Key.SCROLL.getKeyIndex();
        buttonMap[KeyEvent.VK_NUMPAD7] = Component.Identifier.Key.NUMPAD7.getKeyIndex();
        buttonMap[KeyEvent.VK_NUMPAD8] = Component.Identifier.Key.NUMPAD8.getKeyIndex();
        buttonMap[KeyEvent.VK_NUMPAD9] = Component.Identifier.Key.NUMPAD9.getKeyIndex();
        buttonMap[KeyEvent.VK_SUBTRACT] = Component.Identifier.Key.SUBTRACT.getKeyIndex();
        buttonMap[KeyEvent.VK_NUMPAD4] = Component.Identifier.Key.NUMPAD4.getKeyIndex();
        buttonMap[KeyEvent.VK_NUMPAD5] = Component.Identifier.Key.NUMPAD5.getKeyIndex();
        buttonMap[KeyEvent.VK_NUMPAD6] = Component.Identifier.Key.NUMPAD6.getKeyIndex();
        buttonMap[KeyEvent.VK_ADD] = Component.Identifier.Key.ADD.getKeyIndex();
        buttonMap[KeyEvent.VK_NUMPAD1] = Component.Identifier.Key.NUMPAD1.getKeyIndex();
        buttonMap[KeyEvent.VK_NUMPAD2] = Component.Identifier.Key.NUMPAD2.getKeyIndex();
        buttonMap[KeyEvent.VK_NUMPAD3] = Component.Identifier.Key.NUMPAD3.getKeyIndex();
        buttonMap[KeyEvent.VK_NUMPAD0] = Component.Identifier.Key.NUMPAD0.getKeyIndex();
        buttonMap[KeyEvent.VK_DECIMAL] = Component.Identifier.Key.DECIMAL.getKeyIndex();
        
        buttonMap[KeyEvent.VK_KANA] = Component.Identifier.Key.KANA.getKeyIndex();
        buttonMap[KeyEvent.VK_CONVERT] = Component.Identifier.Key.CONVERT.getKeyIndex();
        buttonMap[KeyEvent.VK_NONCONVERT] = Component.Identifier.Key.NOCONVERT.getKeyIndex();

        buttonMap[KeyEvent.VK_CIRCUMFLEX] = Component.Identifier.Key.CIRCUMFLEX.getKeyIndex();
        buttonMap[KeyEvent.VK_AT] = Component.Identifier.Key.AT.getKeyIndex();
        buttonMap[KeyEvent.VK_COLON] = Component.Identifier.Key.COLON.getKeyIndex();
        buttonMap[KeyEvent.VK_UNDERSCORE] = Component.Identifier.Key.UNDERLINE.getKeyIndex();
        buttonMap[KeyEvent.VK_KANJI] = Component.Identifier.Key.KANJI.getKeyIndex();
        
        buttonMap[KeyEvent.VK_STOP] = Component.Identifier.Key.STOP.getKeyIndex();

        buttonMap[KeyEvent.VK_DIVIDE] = Component.Identifier.Key.DIVIDE.getKeyIndex();

        buttonMap[KeyEvent.VK_PAUSE] = Component.Identifier.Key.PAUSE.getKeyIndex();
        buttonMap[KeyEvent.VK_HOME] = Component.Identifier.Key.HOME.getKeyIndex();
        buttonMap[KeyEvent.VK_UP] = Component.Identifier.Key.UP.getKeyIndex();
        buttonMap[KeyEvent.VK_PAGE_UP] = Component.Identifier.Key.PAGEUP.getKeyIndex();
        buttonMap[KeyEvent.VK_LEFT] = Component.Identifier.Key.LEFT.getKeyIndex();
        buttonMap[KeyEvent.VK_RIGHT] = Component.Identifier.Key.RIGHT.getKeyIndex();
        buttonMap[KeyEvent.VK_END] = Component.Identifier.Key.END.getKeyIndex();
        buttonMap[KeyEvent.VK_DOWN] = Component.Identifier.Key.DOWN.getKeyIndex();
        buttonMap[KeyEvent.VK_PAGE_DOWN] = Component.Identifier.Key.PAGEDOWN.getKeyIndex();
        buttonMap[KeyEvent.VK_INSERT] = Component.Identifier.Key.INSERT.getKeyIndex();
        buttonMap[KeyEvent.VK_DELETE] = Component.Identifier.Key.DELETE.getKeyIndex();
        
        //Windows key is a special one

        // start working
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);
    }

    /* (non-Javadoc)
     * @see net.java.games.input.Keyboard#isKeyPressed(net.java.games.input.Keyboard.Key)
     */
    protected boolean isKeyPressed(Key key) {
        int keyId = ((Component.Identifier.Key)key.getIdentifier()).getKeyIndex();
        return buttonValues[keyId];
    }

    /* (non-Javadoc)
     * @see net.java.games.input.Controller#poll()
     */
    public boolean poll() {
        return true;
    }

    /* (non-Javadoc)
     * @see java.awt.event.AWTEventListener#eventDispatched(java.awt.AWTEvent)
     */
    public void eventDispatched(AWTEvent event) {
        if(event instanceof KeyEvent) {
            KeyEvent keyEvent = (KeyEvent)event;
            if(keyEvent.getID() == KeyEvent.KEY_PRESSED) {
                //the key was pressed
                //System.out.println("Key pressed KeyCode: " + keyEvent.getKeyCode() + " KeyChar: " + keyEvent.getKeyChar());
                buttonValues[findKeyIndex(keyEvent)] = true;
            } else if(keyEvent.getID() == KeyEvent.KEY_RELEASED) {
                KeyEvent nextPress = (KeyEvent)Toolkit.getDefaultToolkit().getSystemEventQueue().peekEvent(KeyEvent.KEY_PRESSED);
                
                if ((nextPress == null) || (nextPress.getWhen() != keyEvent.getWhen())) {
                    //the key came really came up
                    //System.out.println("Key released KeyCode: " + keyEvent.getKeyCode() + " KeyChar: " + keyEvent.getKeyChar());
                    buttonValues[findKeyIndex(keyEvent)] = false;
                }
            } else {
                //System.out.println("AWTKeyboard: Ignoring event " + keyEvent.getID());
            }
        } else {
            throw new IllegalArgumentException("AWTKeyboard not expecting event of type " + event.getClass().getName());
        }
    }

    private int findKeyIndex(KeyEvent keyEvent) {
        int buttonIndex = 0;
        if(keyEvent.getKeyCode() == KeyEvent.VK_CONTROL) {
            if(keyEvent.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT) {
                buttonIndex = Component.Identifier.Key.LCONTROL.getKeyIndex();
            } else if(keyEvent.getKeyLocation() == KeyEvent.KEY_LOCATION_RIGHT) {
                buttonIndex = Component.Identifier.Key.RCONTROL.getKeyIndex();
            }
        } else if(keyEvent.getKeyCode() == KeyEvent.VK_SHIFT) {
            if(keyEvent.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT) {
                buttonIndex = Component.Identifier.Key.LSHIFT.getKeyIndex();
            } else if(keyEvent.getKeyLocation() == KeyEvent.KEY_LOCATION_RIGHT) {
                buttonIndex = Component.Identifier.Key.RSHIFT.getKeyIndex();
            }
        } else if(keyEvent.getKeyCode() == KeyEvent.VK_ALT) {
            if(keyEvent.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT) {
                buttonIndex = Component.Identifier.Key.LALT.getKeyIndex();
            } else if(keyEvent.getKeyLocation() == KeyEvent.KEY_LOCATION_RIGHT) {
                buttonIndex = Component.Identifier.Key.RALT.getKeyIndex();
            }
//this is 1.5 only            
/*        } else if(keyEvent.getKeyCode() == KeyEvent.VK_WINDOWS) {
            if(keyEvent.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT) {
                buttonIndex = Component.Identifier.Key.LWIN.getKeyIndex();
            } else if(keyEvent.getKeyLocation() == KeyEvent.KEY_LOCATION_RIGHT) {
                buttonIndex = Component.Identifier.Key.RWIN.getKeyIndex();
            }*/
        } else if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            if(keyEvent.getKeyLocation() == KeyEvent.KEY_LOCATION_NUMPAD) {
                buttonIndex = Component.Identifier.Key.NUMPADENTER.getKeyIndex();
            } else {
                buttonIndex = Component.Identifier.Key.RETURN.getKeyIndex();
            }
        } else if(keyEvent.getKeyCode() == KeyEvent.VK_COMMA) {
            if(keyEvent.getKeyLocation() == KeyEvent.KEY_LOCATION_NUMPAD) {
                buttonIndex = Component.Identifier.Key.NUMPADCOMMA.getKeyIndex();
            } else {
                buttonIndex = Component.Identifier.Key.COMMA.getKeyIndex();
            }
        } else {
            buttonIndex = buttonMap[keyEvent.getKeyCode()];
        }
        if(buttonIndex == 0 ) {
            System.out.println("Unrecognised key: " + keyEvent.getKeyCode() + " (" + keyEvent.getKeyLocation() + " " + keyEvent.getKeyChar() + ")");
        }
        return buttonIndex;
    }
}
