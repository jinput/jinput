/*
 * %W% %E%
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

#include "rawwinver.h"
#include <windows.h>
#include <jni.h>
#include "net_java_games_input_RawInputEventQueue.h"
#include "util.h"

static void handleMouseEvent(JNIEnv *env, jobject self, jmethodID add_method, LONG time, RAWINPUT *data) {
	(*env)->CallVoidMethod(env, self, add_method,
		(jlong)(INT_PTR)data->header.hDevice,
		(jlong)time,
		(jint)data->data.mouse.usFlags,
		(jint)data->data.mouse.usButtonFlags,
		/*
		 * The Raw Input spec says that the usButtonData
		 * is a signed value, if RI_MOUSE_WHEEL
		 * is set in usFlags. However, usButtonData
		 * is an unsigned value, for unknown reasons,
		 * and since its only known use is the wheel
		 * delta, we'll convert it to a signed value here
		 */
		(jint)(SHORT)data->data.mouse.usButtonData,
		(jlong)data->data.mouse.ulRawButtons,
		(jlong)data->data.mouse.lLastX,
		(jlong)data->data.mouse.lLastY,
		(jlong)data->data.mouse.ulExtraInformation
		);
}

static void handleKeyboardEvent(JNIEnv *env, jobject self, jmethodID add_method, LONG time, RAWINPUT *data) {
	(*env)->CallVoidMethod(env, self, add_method,
		(jlong)(INT_PTR)data->header.hDevice,
		(jlong)time,
		(jint)data->data.keyboard.MakeCode,
		(jint)data->data.keyboard.Flags,
		(jint)data->data.keyboard.VKey,
		(jint)data->data.keyboard.Message,
		(jlong)data->data.keyboard.ExtraInformation
		);
}

JNIEXPORT void JNICALL Java_net_java_games_input_RawInputEventQueue_nRegisterDevices(JNIEnv *env, jclass unused, jint flags, jlong hwnd_addr, jobjectArray device_infos) {
	BOOL res;
	jclass device_info_class;
	jmethodID getUsage_method;
	jmethodID getUsagePage_method;
	RAWINPUTDEVICE *devices;
	RAWINPUTDEVICE *device;
	jsize num_devices = (*env)->GetArrayLength(env, device_infos);
	USHORT usage;
	USHORT usage_page;
	int i;
	HWND hwnd = (HWND)(INT_PTR)hwnd_addr;

/*	res = GetRegisteredRawInputDevices(NULL, &num_devices, sizeof(RAWINPUTDEVICE));
	if (num_devices > 0) {
		devices = (RAWINPUTDEVICE *)malloc(num_devices*sizeof(RAWINPUTDEVICE));
		res = GetRegisteredRawInputDevices(devices, &num_devices, sizeof(RAWINPUTDEVICE));
		if (res == -1) {
			throwIOException(env, "Failed to get registered raw devices (%d)\n", GetLastError());
			return;
		}
		for (i = 0; i < num_devices; i++) {
			printfJava(env, "from windows: registered: %d %d %p (of %d)\n", devices[i].usUsagePage, devices[i].usUsage, devices[i].hwndTarget, num_devices);
		}
		free(devices);
	}*/
	device_info_class = (*env)->FindClass(env, "net/java/games/input/RawDeviceInfo");
	if (device_info_class == NULL)
		return;
	getUsage_method = (*env)->GetMethodID(env, device_info_class, "getUsage", "()I");
	if (getUsage_method == NULL)
		return;
	getUsagePage_method = (*env)->GetMethodID(env, device_info_class, "getUsagePage", "()I");
	if (getUsagePage_method == NULL)
		return;
	devices = (RAWINPUTDEVICE *)malloc(num_devices*sizeof(RAWINPUTDEVICE));
	if (devices == NULL) {
		throwIOException(env, "Failed to allocate device structs\n");
		return;
	}
	for (i = 0; i < num_devices; i++) {
		jobject device_obj = (*env)->GetObjectArrayElement(env, device_infos, i);
		if (device_obj == NULL) {
			free(devices);
			return;
		}
		usage = (*env)->CallIntMethod(env, device_obj, getUsage_method);
		if ((*env)->ExceptionOccurred(env)) {
			free(devices);
			return;
		}
		usage_page = (*env)->CallIntMethod(env, device_obj, getUsagePage_method);
		if ((*env)->ExceptionOccurred(env)) {
			free(devices);
			return;
		}
		device = devices + i;
		device->usUsagePage = usage_page;
		device->usUsage = usage;
		device->dwFlags = flags;
		device->hwndTarget = hwnd;
	}
	res = RegisterRawInputDevices(devices, num_devices, sizeof(RAWINPUTDEVICE));
	free(devices);
	if (!res)
		throwIOException(env, "Failed to register raw devices (%d)\n", GetLastError());
}

JNIEXPORT void JNICALL Java_net_java_games_input_RawInputEventQueue_nPoll(JNIEnv *env, jobject self, jlong hwnd_handle) {
	MSG msg;
	HWND hwnd = (HWND)(INT_PTR)hwnd_handle;
	jmethodID addMouseEvent_method;
	jmethodID addKeyboardEvent_method;
	UINT input_size;
	RAWINPUT *input_data;
	LONG time;
	jclass self_class = (*env)->GetObjectClass(env, self);

	if (self_class == NULL)
		return;
	addMouseEvent_method = (*env)->GetMethodID(env, self_class, "addMouseEvent", "(JJIIIJJJJ)V");
	if (addMouseEvent_method == NULL)
		return;
	addKeyboardEvent_method = (*env)->GetMethodID(env, self_class, "addKeyboardEvent", "(JJIIIIJ)V");
	if (addKeyboardEvent_method == NULL)
		return;
	if (GetMessage(&msg, hwnd, 0, 0) != 0) {
		if (msg.message != WM_INPUT) {
			DefWindowProc(hwnd, msg.message, msg.wParam, msg.lParam);
			return; // ignore it
		}
		time = msg.time;
		if (GetRawInputData((HRAWINPUT)msg.lParam, RID_INPUT, NULL, &input_size, sizeof(RAWINPUTHEADER)) == (UINT)-1) {
			throwIOException(env, "Failed to get raw input data size (%d)\n", GetLastError());
			DefWindowProc(hwnd, msg.message, msg.wParam, msg.lParam);
			return;
		}
		input_data = (RAWINPUT *)malloc(input_size);
		if (input_data == NULL) {
			throwIOException(env, "Failed to allocate input data buffer\n");
			DefWindowProc(hwnd, msg.message, msg.wParam, msg.lParam);
			return;
		}
		if (GetRawInputData((HRAWINPUT)msg.lParam, RID_INPUT, input_data, &input_size, sizeof(RAWINPUTHEADER)) == (UINT)-1) {
			free(input_data);
			throwIOException(env, "Failed to get raw input data (%d)\n", GetLastError());
			DefWindowProc(hwnd, msg.message, msg.wParam, msg.lParam);
			return;
		}
		switch (input_data->header.dwType) {
			case RIM_TYPEMOUSE:
				handleMouseEvent(env, self, addMouseEvent_method, time, input_data);
				break;
			case RIM_TYPEKEYBOARD:
				handleKeyboardEvent(env, self, addKeyboardEvent_method, time, input_data);
				break;
			default:
				/* ignore other types of message */
				break;
		}
		free(input_data);
		DefWindowProc(hwnd, msg.message, msg.wParam, msg.lParam);
	}		
}
