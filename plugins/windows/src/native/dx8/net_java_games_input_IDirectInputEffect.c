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
#include "net_java_games_input_IDirectInputEffect.h"
#include "util.h"

JNIEXPORT void JNICALL Java_net_java_games_input_IDirectInputEffect_nRelease(JNIEnv *env, jclass unused, jlong address) {
	LPDIRECTINPUTEFFECT ppdeff = (LPDIRECTINPUTEFFECT)(INT_PTR)address;

	IDirectInputEffect_Release(ppdeff);
}

JNIEXPORT jint JNICALL Java_net_java_games_input_IDirectInputEffect_nSetGain(JNIEnv *env, jclass unused, jlong address, jint gain) {
	LPDIRECTINPUTEFFECT ppdeff = (LPDIRECTINPUTEFFECT)(INT_PTR)address;
	DIEFFECT params;

	ZeroMemory(&params, sizeof(params));
	params.dwSize = sizeof(params);
	params.dwGain = gain;
	
	return IDirectInputEffect_SetParameters(ppdeff, &params, DIEP_GAIN);
}

JNIEXPORT jint JNICALL Java_net_java_games_input_IDirectInputEffect_nStart(JNIEnv *env, jclass unused, jlong address, jint iterations, jint flags) {
	LPDIRECTINPUTEFFECT ppdeff = (LPDIRECTINPUTEFFECT)(INT_PTR)address;

	return IDirectInputEffect_Start(ppdeff, iterations, flags);
}

JNIEXPORT jint JNICALL Java_net_java_games_input_IDirectInputEffect_nStop(JNIEnv *env, jclass unused, jlong address) {
	LPDIRECTINPUTEFFECT ppdeff = (LPDIRECTINPUTEFFECT)(INT_PTR)address;

	return IDirectInputEffect_Stop(ppdeff);
}
