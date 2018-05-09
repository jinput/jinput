/*
 * %W% %E%
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

#include <windows.h>
#include <jni.h>
#include "dxversion.h"
#include <dinput.h>
#include "net_java_games_input_IDirectInput.h"
#include "util.h"
#include "winutil.h"

typedef struct {
    LPDIRECTINPUT8 lpDirectInput;
	JNIEnv *env;
	jobject obj;
} enum_context_t;

JNIEXPORT jlong JNICALL Java_net_java_games_input_IDirectInput_createIDirectInput(JNIEnv *env, jclass unused) {
    LPDIRECTINPUT8 lpDirectInput;
    HRESULT res = DirectInput8Create(GetModuleHandle(NULL), DIRECTINPUT_VERSION,
        &IID_IDirectInput8, (void *)&lpDirectInput, NULL);
    if (FAILED(res)) {
		throwIOException(env, "Failed to create IDirectInput8 (%d)\n", res);
		return 0;
	}
	return (jlong)(INT_PTR)lpDirectInput;
}

static BOOL CALLBACK enumerateDevicesCallback(LPCDIDEVICEINSTANCE lpddi, LPVOID context) {
	enum_context_t *enum_context = (enum_context_t *)context;
//    LPCDIDATAFORMAT lpDataFormat;
    LPDIRECTINPUTDEVICE8 lpDevice;
	DWORD device_type;
	DWORD device_subtype;
	HRESULT res;
	jclass obj_class;
	jmethodID IDirectInput_addDevice;
	jstring instance_name;
	jstring product_name;
	jbyteArray instance_guid;
	jbyteArray product_guid;

	instance_guid = wrapGUID(enum_context->env, &(lpddi->guidInstance));
	if (instance_guid == NULL)
		return DIENUM_STOP;
	product_guid = wrapGUID(enum_context->env, &(lpddi->guidProduct));
	if (product_guid == NULL)
		return DIENUM_STOP;
	instance_name = (*enum_context->env)->NewStringUTF(enum_context->env, lpddi->tszInstanceName);
	if (instance_name == NULL)
		return DIENUM_STOP;
	product_name = (*enum_context->env)->NewStringUTF(enum_context->env, lpddi->tszProductName);
	if (product_name == NULL)
		return DIENUM_STOP;
	
	obj_class = (*enum_context->env)->GetObjectClass(enum_context->env, enum_context->obj);
	if (obj_class == NULL)
		return DIENUM_STOP;
	
	IDirectInput_addDevice = (*enum_context->env)->GetMethodID(enum_context->env, obj_class, "addDevice", "(J[B[BIILjava/lang/String;Ljava/lang/String;)V");
	if (IDirectInput_addDevice == NULL)
		return DIENUM_STOP;

    res = IDirectInput8_CreateDevice(enum_context->lpDirectInput, &(lpddi->guidInstance), &lpDevice, NULL);
    if (FAILED(res)) {
		throwIOException(enum_context->env, "Failed to create device (%d)\n", res);
        return DIENUM_STOP;
    }
	
    device_type = GET_DIDEVICE_TYPE(lpddi->dwDevType);
	device_subtype = GET_DIDEVICE_SUBTYPE(lpddi->dwDevType);

	(*enum_context->env)->CallVoidMethod(enum_context->env, enum_context->obj, IDirectInput_addDevice, (jlong)(INT_PTR)lpDevice, instance_guid, product_guid, (jint)device_type, (jint)device_subtype, instance_name, product_name);
	if ((*enum_context->env)->ExceptionOccurred(enum_context->env) != NULL) {
		IDirectInputDevice8_Release(lpDevice);
		return DIENUM_STOP;
	}

	return DIENUM_CONTINUE;
}

JNIEXPORT void JNICALL Java_net_java_games_input_IDirectInput_nEnumDevices(JNIEnv *env, jobject obj, jlong address) {
    LPDIRECTINPUT8 lpDirectInput = (LPDIRECTINPUT8)(INT_PTR)address;
	HRESULT res;

	enum_context_t enum_context;
	enum_context.lpDirectInput = lpDirectInput;
	enum_context.env = env;
	enum_context.obj = obj;
	res = IDirectInput8_EnumDevices(lpDirectInput, DI8DEVCLASS_ALL, enumerateDevicesCallback, &enum_context, DIEDFL_ATTACHEDONLY);
	if (FAILED(res)) {
		throwIOException(env, "Failed to enumerate devices (%d)\n", res);
	}
}

JNIEXPORT void JNICALL Java_net_java_games_input_IDirectInput_nRelease(JNIEnv *env, jclass unused, jlong address) {
    LPDIRECTINPUT8 lpDirectInput = (LPDIRECTINPUT8)(INT_PTR)address;
	IDirectInput8_Release(lpDirectInput);
}
