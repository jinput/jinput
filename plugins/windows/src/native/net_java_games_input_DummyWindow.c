/*
 * %W% %E%
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

#include <windows.h>
#include <jni.h>
#include "net_java_games_input_DummyWindow.h"
#include "util.h"

static const TCHAR* DUMMY_WINDOW_NAME = "JInputControllerWindow";

static LRESULT CALLBACK DummyWndProc(
    HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam) {
    return DefWindowProc(hWnd, message, wParam, lParam);
}

static BOOL RegisterDummyWindow(HINSTANCE hInstance)
{
	WNDCLASSEX wcex;
	wcex.cbSize = sizeof(WNDCLASSEX); 
	wcex.style			= CS_HREDRAW | CS_VREDRAW;
	wcex.lpfnWndProc	= (WNDPROC)DummyWndProc;
	wcex.cbClsExtra		= 0;
	wcex.cbWndExtra		= 0;
	wcex.hInstance		= hInstance;
	wcex.hIcon			= NULL;
	wcex.hCursor		= NULL;
	wcex.hbrBackground	= (HBRUSH)(COLOR_WINDOW+1);
	wcex.lpszMenuName	= (LPCSTR)NULL;
	wcex.lpszClassName	= DUMMY_WINDOW_NAME;
	wcex.hIconSm		= NULL;
	return RegisterClassEx(&wcex);
}

JNIEXPORT jlong JNICALL Java_net_java_games_input_DummyWindow_createWindow(JNIEnv *env, jclass unused) {
    HINSTANCE hInst = GetModuleHandle(NULL);
	HWND hwndDummy;
	WNDCLASSEX class_info;
	class_info.cbSize = sizeof(WNDCLASSEX);
	class_info.cbClsExtra = 0;
	class_info.cbWndExtra = 0;
	
	if (!GetClassInfoEx(hInst, DUMMY_WINDOW_NAME, &class_info)) {
		// Register the dummy input window
		if (!RegisterDummyWindow(hInst)) {
			throwIOException(env, "Failed to register window class (%d)\n", GetLastError());
			return 0;
		}
	}

    // Create the dummy input window
    hwndDummy = CreateWindow(DUMMY_WINDOW_NAME, NULL,
        WS_POPUP | WS_ICONIC,
        0, 0, 0, 0, NULL, NULL, hInst, NULL);
    if (hwndDummy == NULL) {
		throwIOException(env, "Failed to create window (%d)\n", GetLastError());
        return 0;
    }
	return (jlong)(intptr_t)hwndDummy;
}

JNIEXPORT void JNICALL Java_net_java_games_input_DummyWindow_nDestroy(JNIEnv *env, jclass unused, jlong hwnd_address) {
	HWND hwndDummy = (HWND)(INT_PTR)hwnd_address;
	BOOL result = DestroyWindow(hwndDummy);
	if (!result) {
		throwIOException(env, "Failed to destroy window (%d)\n", GetLastError());
	}
}
