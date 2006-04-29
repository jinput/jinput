/*
 * %W% %E%
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

#include "rawwinver.h"
#include <windows.h>
#include <setupapi.h>
#include <devguid.h>
#include <regstr.h>
#include <jni.h>
#include "net_java_games_input_RawInputEnvironmentPlugin.h"
#include "util.h"
#include "winutil.h"

JNIEXPORT jbyteArray JNICALL Java_net_java_games_input_RawInputEnvironmentPlugin_getKeyboardClassGUID(JNIEnv *env, jclass unused) {
	return wrapGUID(env, &GUID_DEVCLASS_KEYBOARD);
}

JNIEXPORT jbyteArray JNICALL Java_net_java_games_input_RawInputEnvironmentPlugin_getMouseClassGUID(JNIEnv *env, jclass unused) {
	return wrapGUID(env, &GUID_DEVCLASS_MOUSE);
}

JNIEXPORT void JNICALL Java_net_java_games_input_RawInputEnvironmentPlugin_nEnumSetupAPIDevices(JNIEnv *env, jclass unused, jbyteArray guid_array, jobject device_list) {
	jclass list_class;
	jmethodID add_method;
	HDEVINFO hDevInfo;
	SP_DEVINFO_DATA DeviceInfoData;
	int i;
	GUID setup_class_guid;
	jstring device_name;
	jstring device_instance_id;
	jobject setup_api_device;

	list_class = (*env)->GetObjectClass(env, device_list);
	if (list_class == NULL)
		return;
	add_method = (*env)->GetMethodID(env, list_class, "add", "(Ljava/lang/Object;)Z");
	if (add_method == NULL)
		return;
	unwrapGUID(env, guid_array, &setup_class_guid);
	if ((*env)->ExceptionOccurred(env))
		return;

	hDevInfo = SetupDiGetClassDevs(&setup_class_guid,
			NULL,   
		 	NULL,
			DIGCF_PRESENT);

	if (hDevInfo == INVALID_HANDLE_VALUE) {
		throwIOException(env, "Failed to create device enumerator (%d)\n", GetLastError());
		return;
	}


	DeviceInfoData.cbSize = sizeof(SP_DEVINFO_DATA);
	for (i = 0; SetupDiEnumDeviceInfo(hDevInfo, i, &DeviceInfoData); i++) {
		DWORD DataT;
		LPTSTR buffer = NULL;
		DWORD buffersize = 0;


		while (!SetupDiGetDeviceRegistryProperty(
					hDevInfo,
					&DeviceInfoData,
					SPDRP_DEVICEDESC,
					&DataT,
					(PBYTE)buffer,
					buffersize,
					&buffersize)) {
			if (buffer != NULL)
				free(buffer);
			if (GetLastError() == ERROR_INSUFFICIENT_BUFFER) {
				buffer = malloc(buffersize);
			} else {
				throwIOException(env, "Failed to get device description (%x)\n", GetLastError());
				SetupDiDestroyDeviceInfoList(hDevInfo);
				return;
			}
		}

		device_name = (*env)->NewStringUTF(env, buffer);
		if (device_name == NULL) {
			free(buffer);
			SetupDiDestroyDeviceInfoList(hDevInfo);
			return;
		}

		while (!SetupDiGetDeviceInstanceId(
					hDevInfo,
					&DeviceInfoData,
					buffer,
					buffersize,
					&buffersize))
		{
			if (buffer != NULL)
				free(buffer);
			if (GetLastError() == ERROR_INSUFFICIENT_BUFFER) {
				buffer = malloc(buffersize);
			} else {
				throwIOException(env, "Failed to get device instance id (%x)\n", GetLastError());
				SetupDiDestroyDeviceInfoList(hDevInfo);
				return;
			}
		}

		device_instance_id = (*env)->NewStringUTF(env, buffer);
		if (buffer != NULL)
			free(buffer);
		if (device_instance_id == NULL) {
			SetupDiDestroyDeviceInfoList(hDevInfo);
			return;
		}
		setup_api_device = newJObject(env, "net/java/games/input/SetupAPIDevice", "(Ljava/lang/String;Ljava/lang/String;)V", device_instance_id, device_name);
		if (setup_api_device == NULL) {
			SetupDiDestroyDeviceInfoList(hDevInfo);
			return;
		}
		(*env)->CallBooleanMethod(env, device_list, add_method, setup_api_device);
		if ((*env)->ExceptionOccurred(env)) {
			SetupDiDestroyDeviceInfoList(hDevInfo);
			return;
		}
	}
	SetupDiDestroyDeviceInfoList(hDevInfo);
}

JNIEXPORT void JNICALL Java_net_java_games_input_RawInputEnvironmentPlugin_enumerateDevices(JNIEnv *env, jclass unused, jobject queue, jobject device_list) {
	UINT num_devices;
	UINT res;
	RAWINPUTDEVICELIST *devices;
	RAWINPUTDEVICELIST *device;
	jobject device_object;
	jclass list_class;
	jmethodID add_method;
	int i;

	list_class = (*env)->GetObjectClass(env, device_list);
	if (list_class == NULL)
		return;
	add_method = (*env)->GetMethodID(env, list_class, "add", "(Ljava/lang/Object;)Z");
	if (add_method == NULL)
		return;
	
	res = GetRawInputDeviceList(NULL, &num_devices, sizeof(RAWINPUTDEVICELIST));
	if ((UINT)-1 == res) {
		throwIOException(env, "Failed to get number of devices (%d)\n", GetLastError());
		return;
	}
	devices = (RAWINPUTDEVICELIST *)malloc(num_devices*sizeof(RAWINPUTDEVICELIST));
	GetRawInputDeviceList(devices, &num_devices, sizeof(RAWINPUTDEVICELIST));
	for (i = 0; i < num_devices; i++) {
		device = devices + i;
		device_object = newJObject(env, "net/java/games/input/RawDevice", "(Lnet/java/games/input/RawInputEventQueue;JI)V", queue, (jlong)(INT_PTR)device->hDevice, (jint)device->dwType);
		if (device_object == NULL) {
			free(devices);
			return;
		}
		(*env)->CallBooleanMethod(env, device_list, add_method, device_object);
		(*env)->DeleteLocalRef(env, device_object);
	}
	free(devices);
}

