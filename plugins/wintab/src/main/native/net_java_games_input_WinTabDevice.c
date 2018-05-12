#include <windows.h>
#include <stdio.h>

#include <jni.h>
#include "net_java_games_input_WinTabDevice.h"
#include "net_java_games_input_WinTabComponent.h"
#include "util.h"
#include <wintab.h>
#include <malloc.h>
#define PACKETDATA	( PK_X | PK_Y | PK_Z | PK_BUTTONS | PK_NORMAL_PRESSURE | PK_ORIENTATION | PK_CURSOR )
#define PACKETMODE	0
#include <pktdef.h>

JNIEXPORT jstring JNICALL Java_net_java_games_input_WinTabDevice_nGetName(JNIEnv *env, jclass unused, jint deviceIndex) {
	char name[50];
	WTInfo(WTI_DEVICES + deviceIndex, DVC_NAME, name);
	return (*env)->NewStringUTF(env, name);
}

JNIEXPORT jintArray JNICALL Java_net_java_games_input_WinTabDevice_nGetAxisDetails(JNIEnv *env, jclass unused, jint deviceIndex, jint axisId) {
	UINT type;
	AXIS threeAxisArray[3];
	AXIS axis;
	long threeAxisData[6];
	long axisData[2];
	int res;
	jintArray retVal = NULL;
	
	if(axisId==net_java_games_input_WinTabComponent_XAxis) type = DVC_X;
	else if(axisId==net_java_games_input_WinTabComponent_YAxis) type = DVC_Y;
	else if(axisId==net_java_games_input_WinTabComponent_ZAxis) type = DVC_Z;
	else if(axisId==net_java_games_input_WinTabComponent_NPressureAxis) type = DVC_NPRESSURE;
	else if(axisId==net_java_games_input_WinTabComponent_TPressureAxis) type = DVC_TPRESSURE;
	else if(axisId==net_java_games_input_WinTabComponent_OrientationAxis) type = DVC_ORIENTATION;
	else if(axisId==net_java_games_input_WinTabComponent_RotationAxis) type = DVC_ROTATION;
		
	if(axisId==net_java_games_input_WinTabComponent_RotationAxis || axisId==net_java_games_input_WinTabComponent_OrientationAxis) {
		res = WTInfo(WTI_DEVICES + deviceIndex, type, &threeAxisArray);
		if(res!=0) {
			threeAxisData[0] = threeAxisArray[0].axMin;
			threeAxisData[1] = threeAxisArray[0].axMax;
			threeAxisData[2] = threeAxisArray[1].axMin;
			threeAxisData[3] = threeAxisArray[1].axMax;
			threeAxisData[4] = threeAxisArray[2].axMin;
			threeAxisData[5] = threeAxisArray[2].axMax;
			retVal = (*env)->NewIntArray(env, 6);
			(*env)->SetIntArrayRegion(env, retVal, 0, 6, threeAxisData);
		}
	} else {
		res = WTInfo(WTI_DEVICES + deviceIndex, type, &axis);
		if(res!=0) {
			axisData[0] = axis.axMin;
			axisData[1] = axis.axMax;
			retVal = (*env)->NewIntArray(env, 2);
			(*env)->SetIntArrayRegion(env, retVal, 0, 2, axisData);
		}
	}
	
	if(retVal==NULL) {
		retVal = (*env)->NewIntArray(env, 0);
	}
	
	return retVal;
}

JNIEXPORT jobjectArray JNICALL Java_net_java_games_input_WinTabDevice_nGetCursorNames(JNIEnv *env, jclass unused, jint deviceId) {
	int numberCursorTypes;
	int firstCursorType;
	char name[50];
	int i;
	jclass stringClass = (*env)->FindClass(env, "java/lang/String");
	jstring nameString;
	jobjectArray retval;
	
	WTInfo(WTI_DEVICES + deviceId, DVC_NCSRTYPES, &numberCursorTypes);
	WTInfo(WTI_DEVICES + deviceId, DVC_FIRSTCSR, &firstCursorType);
	
	retval = (*env)->NewObjectArray(env, numberCursorTypes, stringClass, NULL);
	
	for(i=0;i<numberCursorTypes;i++) {
		WTInfo(WTI_CURSORS + i + firstCursorType, CSR_NAME, name);
		nameString = (*env)->NewStringUTF(env, name);
		(*env)->SetObjectArrayElement(env, retval, i-firstCursorType, nameString);
	}
	
	return retval;
}

JNIEXPORT jint JNICALL Java_net_java_games_input_WinTabDevice_nGetMaxButtonCount(JNIEnv *env, jclass unused, jint deviceId) {
	int numberCursorTypes;
	int firstCursorType;
	byte buttonCount;
	int i;
	byte retval=0;
	
	WTInfo(WTI_DEVICES + deviceId, DVC_NCSRTYPES, &numberCursorTypes);
	WTInfo(WTI_DEVICES + deviceId, DVC_FIRSTCSR, &firstCursorType);
	
	for(i=0;i<numberCursorTypes;i++) {
		WTInfo(WTI_CURSORS + i + firstCursorType, CSR_BUTTONS, &buttonCount);
		if(buttonCount>retval) {
			retval = buttonCount;
		}
	}
	
	return (jint)retval;
}
