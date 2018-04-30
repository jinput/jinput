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

import java.awt.event.KeyEvent;

/**
 * @author Jeremy
 * @author elias
 */
final class AWTKeyMap {
	public final static Component.Identifier.Key mapKeyCode(int key_code) {
		switch (key_code) {
			case KeyEvent.VK_0:
				return Component.Identifier.Key._0;
			case KeyEvent.VK_1:
				return Component.Identifier.Key._1;
			case KeyEvent.VK_2:
				return Component.Identifier.Key._2;
			case KeyEvent.VK_3:
				return Component.Identifier.Key._3;
			case KeyEvent.VK_4:
				return Component.Identifier.Key._4;
			case KeyEvent.VK_5:
				return Component.Identifier.Key._5;
			case KeyEvent.VK_6:
				return Component.Identifier.Key._6;
			case KeyEvent.VK_7:
				return Component.Identifier.Key._7;
			case KeyEvent.VK_8:
				return Component.Identifier.Key._8;
			case KeyEvent.VK_9:
				return Component.Identifier.Key._9;

			case KeyEvent.VK_Q:
				return Component.Identifier.Key.Q;
			case KeyEvent.VK_W:
				return Component.Identifier.Key.W;
			case KeyEvent.VK_E:
				return Component.Identifier.Key.E;
			case KeyEvent.VK_R:
				return Component.Identifier.Key.R;
			case KeyEvent.VK_T:
				return Component.Identifier.Key.T;
			case KeyEvent.VK_Y:
				return Component.Identifier.Key.Y;
			case KeyEvent.VK_U:
				return Component.Identifier.Key.U;
			case KeyEvent.VK_I:
				return Component.Identifier.Key.I;
			case KeyEvent.VK_O:
				return Component.Identifier.Key.O;
			case KeyEvent.VK_P:
				return Component.Identifier.Key.P;
			case KeyEvent.VK_A:
				return Component.Identifier.Key.A;
			case KeyEvent.VK_S:
				return Component.Identifier.Key.S;
			case KeyEvent.VK_D:
				return Component.Identifier.Key.D;
			case KeyEvent.VK_F:
				return Component.Identifier.Key.F;
			case KeyEvent.VK_G:
				return Component.Identifier.Key.G;
			case KeyEvent.VK_H:
				return Component.Identifier.Key.H;
			case KeyEvent.VK_J:
				return Component.Identifier.Key.J;
			case KeyEvent.VK_K:
				return Component.Identifier.Key.K;
			case KeyEvent.VK_L:
				return Component.Identifier.Key.L;
			case KeyEvent.VK_Z:
				return Component.Identifier.Key.Z;
			case KeyEvent.VK_X:
				return Component.Identifier.Key.X;
			case KeyEvent.VK_C:
				return Component.Identifier.Key.C;
			case KeyEvent.VK_V:
				return Component.Identifier.Key.V;
			case KeyEvent.VK_B:
				return Component.Identifier.Key.B;
			case KeyEvent.VK_N:
				return Component.Identifier.Key.N;
			case KeyEvent.VK_M:
				return Component.Identifier.Key.M;

			case KeyEvent.VK_F1:
				return Component.Identifier.Key.F1;
			case KeyEvent.VK_F2:
				return Component.Identifier.Key.F2;
			case KeyEvent.VK_F3:
				return Component.Identifier.Key.F3;
			case KeyEvent.VK_F4:
				return Component.Identifier.Key.F4;
			case KeyEvent.VK_F5:
				return Component.Identifier.Key.F5;
			case KeyEvent.VK_F6:
				return Component.Identifier.Key.F6;
			case KeyEvent.VK_F7:
				return Component.Identifier.Key.F7;
			case KeyEvent.VK_F8:
				return Component.Identifier.Key.F8;
			case KeyEvent.VK_F9:
				return Component.Identifier.Key.F9;
			case KeyEvent.VK_F10:
				return Component.Identifier.Key.F10;
			case KeyEvent.VK_F11:
				return Component.Identifier.Key.F11;
			case KeyEvent.VK_F12:
				return Component.Identifier.Key.F12;

			case KeyEvent.VK_ESCAPE:
				return Component.Identifier.Key.ESCAPE;
			case KeyEvent.VK_MINUS:
				return Component.Identifier.Key.MINUS;
			case KeyEvent.VK_EQUALS:
				return Component.Identifier.Key.EQUALS;
			case KeyEvent.VK_BACK_SPACE:
				return Component.Identifier.Key.BACKSLASH;
			case KeyEvent.VK_TAB:
				return Component.Identifier.Key.TAB;
			case KeyEvent.VK_OPEN_BRACKET:
				return Component.Identifier.Key.LBRACKET;
			case KeyEvent.VK_CLOSE_BRACKET:
				return Component.Identifier.Key.RBRACKET;
			case KeyEvent.VK_SEMICOLON:
				return Component.Identifier.Key.SEMICOLON;
			case KeyEvent.VK_QUOTE:
				return Component.Identifier.Key.APOSTROPHE;
			case KeyEvent.VK_NUMBER_SIGN:
				return Component.Identifier.Key.GRAVE;
			case KeyEvent.VK_BACK_SLASH:
				return Component.Identifier.Key.BACKSLASH;
			case KeyEvent.VK_PERIOD:
				return Component.Identifier.Key.PERIOD;
			case KeyEvent.VK_SLASH:
				return Component.Identifier.Key.SLASH;
			case KeyEvent.VK_MULTIPLY:
				return Component.Identifier.Key.MULTIPLY;
			case KeyEvent.VK_SPACE:
				return Component.Identifier.Key.SPACE;
			case KeyEvent.VK_CAPS_LOCK:
				return Component.Identifier.Key.CAPITAL;
			case KeyEvent.VK_NUM_LOCK:
				return Component.Identifier.Key.NUMLOCK;
			case KeyEvent.VK_SCROLL_LOCK:
				return Component.Identifier.Key.SCROLL;
			case KeyEvent.VK_NUMPAD7:
				return Component.Identifier.Key.NUMPAD7;
			case KeyEvent.VK_NUMPAD8:
				return Component.Identifier.Key.NUMPAD8;
			case KeyEvent.VK_NUMPAD9:
				return Component.Identifier.Key.NUMPAD9;
			case KeyEvent.VK_SUBTRACT:
				return Component.Identifier.Key.SUBTRACT;
			case KeyEvent.VK_NUMPAD4:
				return Component.Identifier.Key.NUMPAD4;
			case KeyEvent.VK_NUMPAD5:
				return Component.Identifier.Key.NUMPAD5;
			case KeyEvent.VK_NUMPAD6:
				return Component.Identifier.Key.NUMPAD6;
			case KeyEvent.VK_ADD:
				return Component.Identifier.Key.ADD;
			case KeyEvent.VK_NUMPAD1:
				return Component.Identifier.Key.NUMPAD1;
			case KeyEvent.VK_NUMPAD2:
				return Component.Identifier.Key.NUMPAD2;
			case KeyEvent.VK_NUMPAD3:
				return Component.Identifier.Key.NUMPAD3;
			case KeyEvent.VK_NUMPAD0:
				return Component.Identifier.Key.NUMPAD0;
			case KeyEvent.VK_DECIMAL:
				return Component.Identifier.Key.DECIMAL;

			case KeyEvent.VK_KANA:
				return Component.Identifier.Key.KANA;
			case KeyEvent.VK_CONVERT:
				return Component.Identifier.Key.CONVERT;
			case KeyEvent.VK_NONCONVERT:
				return Component.Identifier.Key.NOCONVERT;

			case KeyEvent.VK_CIRCUMFLEX:
				return Component.Identifier.Key.CIRCUMFLEX;
			case KeyEvent.VK_AT:
				return Component.Identifier.Key.AT;
			case KeyEvent.VK_COLON:
				return Component.Identifier.Key.COLON;
			case KeyEvent.VK_UNDERSCORE:
				return Component.Identifier.Key.UNDERLINE;
			case KeyEvent.VK_KANJI:
				return Component.Identifier.Key.KANJI;

			case KeyEvent.VK_STOP:
				return Component.Identifier.Key.STOP;

			case KeyEvent.VK_DIVIDE:
				return Component.Identifier.Key.DIVIDE;

			case KeyEvent.VK_PAUSE:
				return Component.Identifier.Key.PAUSE;
			case KeyEvent.VK_HOME:
				return Component.Identifier.Key.HOME;
			case KeyEvent.VK_UP:
				return Component.Identifier.Key.UP;
			case KeyEvent.VK_PAGE_UP:
				return Component.Identifier.Key.PAGEUP;
			case KeyEvent.VK_LEFT:
				return Component.Identifier.Key.LEFT;
			case KeyEvent.VK_RIGHT:
				return Component.Identifier.Key.RIGHT;
			case KeyEvent.VK_END:
				return Component.Identifier.Key.END;
			case KeyEvent.VK_DOWN:
				return Component.Identifier.Key.DOWN;
			case KeyEvent.VK_PAGE_DOWN:
				return Component.Identifier.Key.PAGEDOWN;
			case KeyEvent.VK_INSERT:
				return Component.Identifier.Key.INSERT;
			case KeyEvent.VK_DELETE:
				return Component.Identifier.Key.DELETE;
			default:
				return Component.Identifier.Key.UNKNOWN;
		}
	}

	public final static Component.Identifier.Key map(KeyEvent event) {
		int key_code = event.getKeyCode();
		int key_location = event.getKeyLocation();
		switch (key_code) {
			case KeyEvent.VK_CONTROL:
				if (key_location == KeyEvent.KEY_LOCATION_RIGHT)
					return Component.Identifier.Key.RCONTROL;
				else
					return Component.Identifier.Key.LCONTROL;
			case KeyEvent.VK_SHIFT:
				if (key_location == KeyEvent.KEY_LOCATION_RIGHT)
					return Component.Identifier.Key.RSHIFT;
				else
					return Component.Identifier.Key.LSHIFT;
			case KeyEvent.VK_ALT:
				if (key_location == KeyEvent.KEY_LOCATION_RIGHT)
					return Component.Identifier.Key.RALT;
				else
					return Component.Identifier.Key.LALT;
				//this is 1.5 only            
/*			case KeyEvent.VK_WINDOWS:
				if (key_location == KeyEvent.KEY_LOCATION_RIGHT)
					return Component.Identifier.Key.RWIN;
				else
					return Component.Identifier.Key.LWIN;*/
			case KeyEvent.VK_ENTER:
				if (key_location == KeyEvent.KEY_LOCATION_NUMPAD)
					return Component.Identifier.Key.NUMPADENTER;
				else
					return Component.Identifier.Key.RETURN;
			case KeyEvent.VK_COMMA:
				if (key_location == KeyEvent.KEY_LOCATION_NUMPAD)
					return Component.Identifier.Key.NUMPADCOMMA;
				else
					return Component.Identifier.Key.COMMA;
			default:
				return mapKeyCode(key_code);
		}
    }
}
