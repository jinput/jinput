/*
 * %W% %E%
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

#include <windows.h>
#include "dxversion.h"
#include <jni.h>
#include <dinput.h>
#include "net_java_games_input_IDirectInputDevice.h"
#include "util.h"
#include "winutil.h"

typedef struct {
	JNIEnv *env;
	jobject device_obj;
} enum_context_t;

JNIEXPORT jint JNICALL Java_net_java_games_input_IDirectInputDevice_nSetBufferSize(JNIEnv *env, jclass unused, jlong address, jint size) {
    LPDIRECTINPUTDEVICE8 lpDevice = (LPDIRECTINPUTDEVICE8)(INT_PTR)address;
	DIPROPDWORD dipropdw;
	HRESULT res;
	
	dipropdw.diph.dwSize = sizeof(DIPROPDWORD);
	dipropdw.diph.dwHeaderSize = sizeof(DIPROPHEADER);
	dipropdw.diph.dwObj = 0;
	dipropdw.diph.dwHow = DIPH_DEVICE;
	dipropdw.dwData = size;
	res = IDirectInputDevice8_SetProperty(lpDevice, DIPROP_BUFFERSIZE, &dipropdw.diph);
	return res;
}

static jint mapGUIDType(const GUID *guid) {
	if (IsEqualGUID(guid, &GUID_XAxis)) {
		return net_java_games_input_IDirectInputDevice_GUID_XAxis;
	} else if (IsEqualGUID(guid, &GUID_YAxis)) {
		return net_java_games_input_IDirectInputDevice_GUID_YAxis;
	} else if (IsEqualGUID(guid, &GUID_ZAxis)) {
		return net_java_games_input_IDirectInputDevice_GUID_ZAxis;
	} else if (IsEqualGUID(guid, &GUID_RxAxis)) {
		return net_java_games_input_IDirectInputDevice_GUID_RxAxis;
	} else if (IsEqualGUID(guid, &GUID_RyAxis)) {
		return net_java_games_input_IDirectInputDevice_GUID_RyAxis;
	} else if (IsEqualGUID(guid, &GUID_RzAxis)) {
		return net_java_games_input_IDirectInputDevice_GUID_RzAxis;
	} else if (IsEqualGUID(guid, &GUID_Slider)) {
		return net_java_games_input_IDirectInputDevice_GUID_Slider;
	} else if (IsEqualGUID(guid, &GUID_Button)) {
		return net_java_games_input_IDirectInputDevice_GUID_Button;
	} else if (IsEqualGUID(guid, &GUID_Key)) {
		return net_java_games_input_IDirectInputDevice_GUID_Key;
	} else if (IsEqualGUID(guid, &GUID_POV)) {
		return net_java_games_input_IDirectInputDevice_GUID_POV;
	} else if (IsEqualGUID(guid, &GUID_ConstantForce)) {
		return net_java_games_input_IDirectInputDevice_GUID_ConstantForce;
	} else if (IsEqualGUID(guid, &GUID_RampForce)) {
		return net_java_games_input_IDirectInputDevice_GUID_RampForce;
	} else if (IsEqualGUID(guid, &GUID_Square)) {
		return net_java_games_input_IDirectInputDevice_GUID_Square;
	} else if (IsEqualGUID(guid, &GUID_Sine)) {
		return net_java_games_input_IDirectInputDevice_GUID_Sine;
	} else if (IsEqualGUID(guid, &GUID_Triangle)) {
		return net_java_games_input_IDirectInputDevice_GUID_Triangle;
	} else if (IsEqualGUID(guid, &GUID_SawtoothUp)) {
		return net_java_games_input_IDirectInputDevice_GUID_SawtoothUp;
	} else if (IsEqualGUID(guid, &GUID_SawtoothDown)) {
		return net_java_games_input_IDirectInputDevice_GUID_SawtoothDown;
	} else if (IsEqualGUID(guid, &GUID_Spring)) {
		return net_java_games_input_IDirectInputDevice_GUID_Spring;
	} else if (IsEqualGUID(guid, &GUID_Damper)) {
		return net_java_games_input_IDirectInputDevice_GUID_Damper;
	} else if (IsEqualGUID(guid, &GUID_Inertia)) {
		return net_java_games_input_IDirectInputDevice_GUID_Inertia;
	} else if (IsEqualGUID(guid, &GUID_Friction)) {
		return net_java_games_input_IDirectInputDevice_GUID_Friction;
	} else if (IsEqualGUID(guid, &GUID_CustomForce)) {
		return net_java_games_input_IDirectInputDevice_GUID_CustomForce;
	} else
		return net_java_games_input_IDirectInputDevice_GUID_Unknown;
}

static BOOL CALLBACK enumEffectsCallback(LPCDIEFFECTINFO pdei, LPVOID pvRef) {
	enum_context_t *enum_context = (enum_context_t *)pvRef;
	jmethodID add_method;
	jstring name;
	jbyteArray guid;
	JNIEnv *env = enum_context->env;
	jobject device_obj = enum_context->device_obj;
	jint guid_id;
	jclass obj_class = (*env)->GetObjectClass(env, device_obj);

	
	if (obj_class == NULL)
		return DIENUM_STOP;
	guid = wrapGUID(env, &(pdei->guid));
	if (guid == NULL)
		return DIENUM_STOP;
	add_method = (*env)->GetMethodID(env, obj_class, "addEffect", "([BIIIILjava/lang/String;)V");
	if (add_method == NULL)
		return DIENUM_STOP;
	name = (*env)->NewStringUTF(env, pdei->tszName);
	if (name == NULL)
		return DIENUM_STOP;
	guid_id = mapGUIDType(&(pdei->guid));
	(*env)->CallBooleanMethod(env, device_obj, add_method, guid, guid_id, (jint)pdei->dwEffType, (jint)pdei->dwStaticParams, (jint)pdei->dwDynamicParams, name);
	if ((*env)->ExceptionOccurred(env)) {
		return DIENUM_STOP;
	}
	return DIENUM_CONTINUE;
}

static BOOL CALLBACK enumObjectsCallback(LPCDIDEVICEOBJECTINSTANCE lpddoi, LPVOID pvRef) {
	enum_context_t *enum_context = (enum_context_t *)pvRef;
	jmethodID add_method;
	jstring name;
	DWORD instance;
	DWORD type;
	jint guid_type;
	jbyteArray guid;
	JNIEnv *env = enum_context->env;
	jobject device_obj = enum_context->device_obj;
	jclass obj_class = (*env)->GetObjectClass(env, device_obj);
	
	if (obj_class == NULL)
		return DIENUM_STOP;
	guid = wrapGUID(env, &(lpddoi->guidType));
	if (guid == NULL)
		return DIENUM_STOP;
	add_method = (*env)->GetMethodID(env, obj_class, "addObject", "([BIIIIILjava/lang/String;)V");
	if (add_method == NULL)
		return DIENUM_STOP;
	name = (*env)->NewStringUTF(env, lpddoi->tszName);
	if (name == NULL)
		return DIENUM_STOP;
	instance = DIDFT_GETINSTANCE(lpddoi->dwType);
	type = DIDFT_GETTYPE(lpddoi->dwType);
	guid_type = mapGUIDType(&(lpddoi->guidType));
//printfJava(env, "name %s guid_type %d id %d\n", lpddoi->tszName, guid_type, lpddoi->dwType);
	(*env)->CallBooleanMethod(env, device_obj, add_method, guid, (jint)guid_type, (jint)lpddoi->dwType, (jint)type, (jint)instance, (jint)lpddoi->dwFlags, name);
	if ((*env)->ExceptionOccurred(env)) {
		return DIENUM_STOP;
	}
	return DIENUM_CONTINUE;
}

JNIEXPORT jint JNICALL Java_net_java_games_input_IDirectInputDevice_nGetRangeProperty(JNIEnv *env, jclass unused, jlong address, jint object_id, jlongArray range_array_obj) {
    LPDIRECTINPUTDEVICE8 lpDevice = (LPDIRECTINPUTDEVICE8)(INT_PTR)address;
	DIPROPRANGE range;
	HRESULT res;
	jlong range_array[2];

	range.diph.dwSize = sizeof(DIPROPRANGE);
	range.diph.dwHeaderSize = sizeof(DIPROPHEADER);
	range.diph.dwObj = object_id;
	range.diph.dwHow = DIPH_BYID;
	res = IDirectInputDevice8_GetProperty(lpDevice, DIPROP_RANGE, &(range.diph));
	range_array[0] = range.lMin;
	range_array[1] = range.lMax;
	(*env)->SetLongArrayRegion(env, range_array_obj, 0, 2, range_array);
	return res;
}

JNIEXPORT jint JNICALL Java_net_java_games_input_IDirectInputDevice_nGetDeadzoneProperty(JNIEnv *env, jclass unused, jlong address, jint object_id) {
    LPDIRECTINPUTDEVICE8 lpDevice = (LPDIRECTINPUTDEVICE8)(INT_PTR)address;
	DIPROPDWORD deadzone;
	HRESULT res;

	deadzone.diph.dwSize = sizeof(deadzone);
	deadzone.diph.dwHeaderSize = sizeof(DIPROPHEADER);
	deadzone.diph.dwObj = object_id;
	deadzone.diph.dwHow = DIPH_BYID;
	res = IDirectInputDevice8_GetProperty(lpDevice, DIPROP_DEADZONE, &(deadzone.diph));
	if (res != DI_OK && res != S_FALSE)
		throwIOException(env, "Failed to get deadzone property (%x)\n", res);
	return deadzone.dwData;
}

JNIEXPORT jint JNICALL Java_net_java_games_input_IDirectInputDevice_nSetDataFormat(JNIEnv *env, jclass unused, jlong address, jint flags, jobjectArray objects) {
    LPDIRECTINPUTDEVICE8 lpDevice = (LPDIRECTINPUTDEVICE8)(INT_PTR)address;
	DIDATAFORMAT data_format;
	jsize num_objects = (*env)->GetArrayLength(env, objects);
	/*
	 * Data size must be a multiple of 4, but since sizeof(jint) is
	 * 4, we're safe
	 */
	DWORD data_size = num_objects*sizeof(jint);
	GUID *guids;
	DIOBJECTDATAFORMAT *object_formats;
	int i;
	HRESULT res;
	jclass clazz;
	jmethodID getGUID_method;
	jmethodID getFlags_method;
	jmethodID getType_method;
	jmethodID getInstance_method;
	jobject object;
	jint type;
	jint object_flags;
	jint instance;
	jobject guid_array;
	DWORD composite_type;
	DWORD flags_masked;
	LPDIOBJECTDATAFORMAT object_format;
	
	data_format.dwSize = sizeof(DIDATAFORMAT);
	data_format.dwObjSize = sizeof(DIOBJECTDATAFORMAT);
	data_format.dwFlags = flags;
	data_format.dwDataSize = data_size;
	data_format.dwNumObjs = num_objects;

	clazz = (*env)->FindClass(env, "net/java/games/input/DIDeviceObject");
	if (clazz == NULL)
		return -1;
	getGUID_method = (*env)->GetMethodID(env, clazz, "getGUID", "()[B");
	if (getGUID_method == NULL)
		return -1;
	getFlags_method = (*env)->GetMethodID(env, clazz, "getFlags", "()I");
	if (getFlags_method == NULL)
		return -1;
	getType_method = (*env)->GetMethodID(env, clazz, "getType", "()I");
	if (getType_method == NULL)
		return -1;
	getInstance_method = (*env)->GetMethodID(env, clazz, "getInstance", "()I");
	if (getInstance_method == NULL)
		return -1;

	guids = (GUID *)malloc(num_objects*sizeof(GUID));
	if (guids == NULL) {
		throwIOException(env, "Failed to allocate GUIDs");
		return -1;
	}
	object_formats = (DIOBJECTDATAFORMAT *)malloc(num_objects*sizeof(DIOBJECTDATAFORMAT));
	if (object_formats == NULL) {
		free(guids);
		throwIOException(env, "Failed to allocate data format");
		return -1;
	}
	for (i = 0; i < num_objects; i++) {
		object = (*env)->GetObjectArrayElement(env, objects, i);
		if ((*env)->ExceptionOccurred(env)) {
			free(guids);
			free(object_formats);
			return -1;
		}
		guid_array = (*env)->CallObjectMethod(env, object, getGUID_method);
		if ((*env)->ExceptionOccurred(env)) {
			free(guids);
			free(object_formats);
			return -1;
		}
		unwrapGUID(env, guid_array, guids + i);
		if ((*env)->ExceptionOccurred(env)) {
			free(guids);
			free(object_formats);
			return -1;
		}
		type = (*env)->CallIntMethod(env, object, getType_method);
		if ((*env)->ExceptionOccurred(env)) {
			free(guids);
			free(object_formats);
			return -1;
		}
		object_flags = (*env)->CallIntMethod(env, object, getFlags_method);
		if ((*env)->ExceptionOccurred(env)) {
			free(guids);
			free(object_formats);
			return -1;
		}
		instance = (*env)->CallIntMethod(env, object, getInstance_method);
		if ((*env)->ExceptionOccurred(env)) {
			free(guids);
			free(object_formats);
			return -1;
		}
		(*env)->DeleteLocalRef(env, object);
		composite_type = type | DIDFT_MAKEINSTANCE(instance);
		flags_masked = flags & (DIDOI_ASPECTACCEL | DIDOI_ASPECTFORCE | DIDOI_ASPECTPOSITION | DIDOI_ASPECTVELOCITY);		
		object_format = object_formats + i;
		object_format->pguid = guids + i;
		object_format->dwType = composite_type;
		object_format->dwFlags = flags_masked;
		// dwOfs must be multiple of 4, but sizeof(jint) is 4, so we're safe
		object_format->dwOfs = i*sizeof(jint);
	}
	data_format.rgodf = object_formats;
	res = IDirectInputDevice8_SetDataFormat(lpDevice, &data_format);
	free(guids);
	free(object_formats);
	return res;
}

JNIEXPORT jint JNICALL Java_net_java_games_input_IDirectInputDevice_nAcquire(JNIEnv *env, jclass unused, jlong address) {
    LPDIRECTINPUTDEVICE8 lpDevice = (LPDIRECTINPUTDEVICE8)(INT_PTR)address;

	HRESULT res = IDirectInputDevice8_Acquire(lpDevice);
	return res;
}

JNIEXPORT jint JNICALL Java_net_java_games_input_IDirectInputDevice_nUnacquire(JNIEnv *env, jclass unused, jlong address) {
    LPDIRECTINPUTDEVICE8 lpDevice = (LPDIRECTINPUTDEVICE8)(INT_PTR)address;

	HRESULT res = IDirectInputDevice8_Unacquire(lpDevice);
	return res;
}

JNIEXPORT jint JNICALL Java_net_java_games_input_IDirectInputDevice_nPoll(JNIEnv *env, jclass unused, jlong address) {
    LPDIRECTINPUTDEVICE8 lpDevice = (LPDIRECTINPUTDEVICE8)(INT_PTR)address;

	HRESULT res = IDirectInputDevice8_Poll(lpDevice);
	return res;
}

JNIEXPORT jint JNICALL Java_net_java_games_input_IDirectInputDevice_nGetDeviceState(JNIEnv *env, jclass unused, jlong address, jintArray device_state_array) {
    LPDIRECTINPUTDEVICE8 lpDevice = (LPDIRECTINPUTDEVICE8)(INT_PTR)address;
	jsize state_length = (*env)->GetArrayLength(env, device_state_array);
	DWORD state_size = state_length*sizeof(jint);
	HRESULT res;
	jint *device_state = (*env)->GetIntArrayElements(env, device_state_array, NULL);
	if (device_state == NULL)
		return -1;

	res = IDirectInputDevice8_GetDeviceState(lpDevice, state_size, device_state);
	(*env)->ReleaseIntArrayElements(env, device_state_array, device_state, 0);
	return res;
}

JNIEXPORT jint JNICALL Java_net_java_games_input_IDirectInputDevice_nGetDeviceData(JNIEnv *env, jclass unused, jlong address, jint flags, jobject queue, jobject queue_array, jint position, jint remaining) {
    LPDIRECTINPUTDEVICE8 lpDevice = (LPDIRECTINPUTDEVICE8)(INT_PTR)address;
	DWORD num_events = remaining;
	DIDEVICEOBJECTDATA *data;
	DIDEVICEOBJECTDATA *data_element;
	jmethodID set_method;
	HRESULT res;
	int i;
	jclass data_class;
	jclass queue_class;
	jmethodID position_method;
	
	data_class = (*env)->FindClass(env, "net/java/games/input/DIDeviceObjectData");
	if (data_class == NULL)
		return -1;
	set_method = (*env)->GetMethodID(env, data_class, "set", "(IIII)V");
	if (set_method == NULL)
		return -1;
	queue_class = (*env)->GetObjectClass(env, queue);
	if (queue_class == NULL)
		return -1;
	position_method = (*env)->GetMethodID(env, queue_class, "position", "(I)V");
	if (position_method == NULL)
		return -1;

	data = (DIDEVICEOBJECTDATA *)malloc(num_events*sizeof(DIDEVICEOBJECTDATA));
	if (data == NULL)
		return -1;
	
	res = IDirectInputDevice8_GetDeviceData(lpDevice, sizeof(DIDEVICEOBJECTDATA), data, &num_events, flags);
	if (res == DI_OK || res == DI_BUFFEROVERFLOW) {
		for (i = 0; i < num_events; i++) {
			jobject queue_element = (*env)->GetObjectArrayElement(env, queue_array, position + i);
			if (queue_element == NULL) {
				free(data);
				return -1;
			}
			data_element = data + i;
			(*env)->CallVoidMethod(env, queue_element, set_method, (jint)data_element->dwOfs, (jint)data_element->dwData, (jint)data_element->dwTimeStamp, (jint)data_element->dwSequence);
			if ((*env)->ExceptionOccurred(env)) {
				free(data);
				return -1;
			}
		}
		(*env)->CallVoidMethod(env, queue, position_method, position + num_events);
	}
	free(data);
	return res;
}

JNIEXPORT jint JNICALL Java_net_java_games_input_IDirectInputDevice_nEnumEffects(JNIEnv *env, jobject device_obj, jlong address, jint flags) {
    LPDIRECTINPUTDEVICE8 lpDevice = (LPDIRECTINPUTDEVICE8)(INT_PTR)address;
	HRESULT res;
	enum_context_t enum_context;

	enum_context.env = env;
	enum_context.device_obj = device_obj;
	res = IDirectInputDevice8_EnumEffects(lpDevice, enumEffectsCallback, &enum_context, flags);
	return res;
}

JNIEXPORT jint JNICALL Java_net_java_games_input_IDirectInputDevice_nEnumObjects(JNIEnv *env, jobject device_obj, jlong address, jint flags) {
    LPDIRECTINPUTDEVICE8 lpDevice = (LPDIRECTINPUTDEVICE8)(INT_PTR)address;
	HRESULT res;
	enum_context_t enum_context;

	enum_context.env = env;
	enum_context.device_obj = device_obj;
	res = IDirectInputDevice8_EnumObjects(lpDevice, enumObjectsCallback, &enum_context, flags);
	return res;
}

JNIEXPORT jint JNICALL Java_net_java_games_input_IDirectInputDevice_nSetCooperativeLevel(JNIEnv *env, jclass unused, jlong address, jlong hwnd_address, jint flags) {
    LPDIRECTINPUTDEVICE8 lpDevice = (LPDIRECTINPUTDEVICE8)(INT_PTR)address;
	HWND hwnd = (HWND)(INT_PTR)hwnd_address;

	HRESULT res = IDirectInputDevice8_SetCooperativeLevel(lpDevice, hwnd, flags);
	return res;
}

JNIEXPORT void JNICALL Java_net_java_games_input_IDirectInputDevice_nRelease(JNIEnv *env, jclass unused, jlong address) {
    LPDIRECTINPUTDEVICE8 lpDevice = (LPDIRECTINPUTDEVICE8)(INT_PTR)address;

	IDirectInputDevice8_Release(lpDevice);
}

JNIEXPORT jlong JNICALL Java_net_java_games_input_IDirectInputDevice_nCreatePeriodicEffect(JNIEnv *env, jclass unused, jlong address, jbyteArray effect_guid_array, jint flags, jint duration, jint sample_period, jint gain, jint trigger_button, jint trigger_repeat_interval, jintArray axis_ids_array, jlongArray directions_array, jint envelope_attack_level, jint envelope_attack_time, jint envelope_fade_level, jint envelope_fade_time, jint periodic_magnitude, jint periodic_offset, jint periodic_phase, jint periodic_period, jint start_delay) {
    LPDIRECTINPUTDEVICE8 lpDevice = (LPDIRECTINPUTDEVICE8)(INT_PTR)address;
	LPDIRECTINPUTEFFECT lpdiEffect;
	DIEFFECT effect;
	GUID effect_guid;
	jint *axis_ids;
	jlong *directions;
	jsize num_axes;
	jsize num_directions;
	LONG *directions_long;
	DWORD *axis_ids_dword;
	HRESULT res;
	DIPERIODIC periodic;
	DIENVELOPE envelope;
	int i;

	num_axes = (*env)->GetArrayLength(env, axis_ids_array);
	num_directions = (*env)->GetArrayLength(env, directions_array);

	if (num_axes != num_directions) {
		throwIOException(env, "axis_ids.length != directions.length\n");
		return 0;
	}

	unwrapGUID(env, effect_guid_array, &effect_guid);
	if ((*env)->ExceptionOccurred(env))
		return 0;
	axis_ids = (*env)->GetIntArrayElements(env, axis_ids_array, NULL);
	if (axis_ids == NULL)
		return 0;
	directions = (*env)->GetLongArrayElements(env, directions_array, NULL);
	if (axis_ids == NULL)
		return 0;
	axis_ids_dword = (DWORD *)malloc(sizeof(DWORD)*num_axes);
	if (axis_ids_dword == NULL) {
		throwIOException(env, "Failed to allocate axes array\n");
		return 0;
	}
	directions_long = (LONG *)malloc(sizeof(LONG)*num_directions);
	if (directions_long == NULL) {
		free(axis_ids_dword);
		throwIOException(env, "Failed to allocate directions array\n");
		return 0;
	}
	for (i = 0; i < num_axes; i++) {
		axis_ids_dword[i] = axis_ids[i];
	}
	for (i = 0; i < num_directions; i++) {
		directions_long[i] = directions[i];
	}

	envelope.dwSize = sizeof(DIENVELOPE);
	envelope.dwAttackLevel = envelope_attack_level;
	envelope.dwAttackTime = envelope_attack_time;
	envelope.dwFadeLevel = envelope_fade_level;
	envelope.dwFadeTime = envelope_fade_time;

	periodic.dwMagnitude = periodic_magnitude;
	periodic.lOffset = periodic_offset;
	periodic.dwPhase = periodic_phase;
	periodic.dwPeriod = periodic_period;

	effect.dwSize = sizeof(DIEFFECT);
	effect.dwFlags = flags;
	effect.dwDuration = duration;
	effect.dwSamplePeriod = sample_period;
	effect.dwGain = gain;
	effect.dwTriggerButton = trigger_button;
	effect.dwTriggerRepeatInterval = trigger_repeat_interval;
	effect.cAxes = num_axes;
	effect.rgdwAxes = axis_ids_dword;
	effect.rglDirection = directions_long;
	effect.lpEnvelope = &envelope;
	effect.cbTypeSpecificParams = sizeof(periodic);
	effect.lpvTypeSpecificParams = &periodic;
 	effect.dwStartDelay = start_delay;

	res = IDirectInputDevice8_CreateEffect(lpDevice, &effect_guid, &effect, &lpdiEffect, NULL);
	(*env)->ReleaseIntArrayElements(env, axis_ids_array, axis_ids, 0);
	(*env)->ReleaseLongArrayElements(env, directions_array, directions, 0);
	free(axis_ids_dword);
	free(directions_long);
	if (res != DI_OK) {
		throwIOException(env, "Failed to create effect (0x%x)\n", res);
		return 0;
	}
	return (jlong)(INT_PTR)lpdiEffect;
}
