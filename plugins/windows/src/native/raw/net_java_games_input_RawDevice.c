/*
 * %W% %E%
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

#include "rawwinver.h"
#include <windows.h>
#include <winuser.h>
#include <jni.h>
#include "net_java_games_input_RawDevice.h"
#include "util.h"

JNIEXPORT jstring JNICALL Java_net_java_games_input_RawDevice_nGetName(JNIEnv *env, jclass unused, jlong handle_addr) {
	HANDLE handle = (HANDLE)(INT_PTR)handle_addr;
	UINT res;
	UINT name_length;
	char *name;
	jstring name_str;

	res = GetRawInputDeviceInfo(handle, RIDI_DEVICENAME, NULL, &name_length);
	name = (char *)malloc(name_length*sizeof(char));
	res = GetRawInputDeviceInfo(handle, RIDI_DEVICENAME, name, &name_length);
	if ((UINT)-1 == res) {
		free(name);
		throwIOException(env, "Failed to get device name (%d)\n", GetLastError());
		return NULL;
	}
	name_str = (*env)->NewStringUTF(env, name);
	free(name);
	return name_str;
}

static jobject createKeyboardInfo(JNIEnv *env, jobject device_obj, RID_DEVICE_INFO_KEYBOARD *device_info) {
	return newJObject(env, "net/java/games/input/RawKeyboardInfo", "(Lnet/java/games/input/RawDevice;IIIIII)V", device_obj, (jint)device_info->dwType, (jint)device_info->dwSubType, (jint)device_info->dwKeyboardMode, (jint)device_info->dwNumberOfFunctionKeys, (jint)device_info->dwNumberOfIndicators, (jint)device_info->dwNumberOfKeysTotal);
}

static jobject createMouseInfo(JNIEnv *env, jobject device_obj, RID_DEVICE_INFO_MOUSE *device_info) {
	return newJObject(env, "net/java/games/input/RawMouseInfo", "(Lnet/java/games/input/RawDevice;III)V", device_obj, (jint)device_info->dwId, (jint)device_info->dwNumberOfButtons, (jint)device_info->dwSampleRate);
}

static jobject createHIDInfo(JNIEnv *env, jobject device_obj, RID_DEVICE_INFO_HID *device_info) {
	return newJObject(env, "net/java/games/input/RawHIDInfo", "(Lnet/java/games/input/RawDevice;IIIII)V", device_obj, (jint)device_info->dwVendorId, (jint)device_info->dwProductId, (jint)device_info->dwVersionNumber, (jint)device_info->usUsagePage, (jint)device_info->usUsage);
}

JNIEXPORT jobject JNICALL Java_net_java_games_input_RawDevice_nGetInfo(JNIEnv *env, jclass unused, jobject device_obj, jlong handle_addr) {
	HANDLE handle = (HANDLE)(INT_PTR)handle_addr;
	RID_DEVICE_INFO device_info;
	UINT size = sizeof(RID_DEVICE_INFO);
	UINT res;
	
	device_info.cbSize = sizeof(RID_DEVICE_INFO);
	res = GetRawInputDeviceInfo(handle, RIDI_DEVICEINFO, &device_info, &size);
	if ((UINT)-1 == res) {
		throwIOException(env, "Failed to get device info (%d)\n", GetLastError());
		return NULL;
	}
	switch (device_info.dwType) {
		case RIM_TYPEHID:
			return createHIDInfo(env, device_obj,&(device_info.hid));
		case RIM_TYPEKEYBOARD:
			return createKeyboardInfo(env, device_obj, &(device_info.keyboard));
		case RIM_TYPEMOUSE:
			return createMouseInfo(env, device_obj, &(device_info.mouse));
		default:
			throwIOException(env, "Unknown device type: %d\n", device_info.dwType);
			return NULL;
	}
}
