#include <windows.h>
#include <stdio.h>

#include <jni.h>
#include "net_java_games_input_WinTabContext.h"
#include <wintab.h>
//#define PACKETDATA	( PK_X | PK_Y | PK_Z | PK_BUTTONS | PK_NORMAL_PRESSURE | PK_TANGENT_PRESSURE | PK_ROTATION | PK_ORIENTATION | PK_CURSOR )
#define PACKETDATA	( PK_TIME | PK_X | PK_Y | PK_Z | PK_BUTTONS | PK_NORMAL_PRESSURE  | PK_TANGENT_PRESSURE | PK_ORIENTATION | PK_CURSOR )
#define PACKETMODE	0
#include <pktdef.h>
#include <util.h>

#define MAX_PACKETS	20

JNIEXPORT jlong JNICALL Java_net_java_games_input_WinTabContext_nOpen(JNIEnv *env, jclass unused, jlong hWnd_long) {
	LOGCONTEXT context;
	HWND hWnd = (HWND)(INT_PTR)hWnd_long;
	HCTX hCtx = NULL;
	
	jclass booleanClass = (*env)->FindClass(env, "java/lang/Boolean");
    jstring propertyName = (*env)->NewStringUTF(env, "jinput.wintab.detachcursor");
    jmethodID getBooleanMethod = (*env)->GetStaticMethodID(env, booleanClass, "getBoolean", "(Ljava/lang/String;)Z");
    jboolean detachCursor = (*env)->CallStaticBooleanMethod(env, booleanClass, getBooleanMethod, propertyName);

    WTInfo(WTI_DEFCONTEXT, 0, &context);

	wsprintf(context.lcName, "JInput Digitizing");
	context.lcPktData = PACKETDATA;
	context.lcPktMode = PACKETMODE;
	context.lcMoveMask = PACKETDATA;
	context.lcBtnUpMask = context.lcBtnDnMask;
	if(!detachCursor) {
		context.lcOptions |= CXO_SYSTEM;
	}

	/* open the region */
	hCtx = WTOpen(hWnd, &context, TRUE);
	
	return (jlong)(intptr_t)hCtx;
}

JNIEXPORT void JNICALL Java_net_java_games_input_WinTabContext_nClose(JNIEnv *env, jclass unused, jlong hCtx_long) {
	WTClose((HCTX)(INT_PTR)hCtx_long);
}

JNIEXPORT jint JNICALL Java_net_java_games_input_WinTabContext_nGetNumberOfSupportedDevices(JNIEnv *env, jclass unused) {
	int numDevices;
	WTInfo(WTI_INTERFACE, IFC_NDEVICES, &numDevices);
	return numDevices;
}

JNIEXPORT jobjectArray JNICALL Java_net_java_games_input_WinTabContext_nGetPackets(JNIEnv *env, jclass unused, jlong hCtx_long) {

	jobjectArray retval;
	int i=0;
	PACKET packets[MAX_PACKETS];
	int numberRead = WTPacketsGet((HCTX)(INT_PTR)hCtx_long, MAX_PACKETS, packets);
	jclass winTabPacketClass = (*env)->FindClass(env, "net/java/games/input/WinTabPacket");
	jfieldID packetTimeField = (*env)->GetFieldID(env, winTabPacketClass, "PK_TIME", "J");
	jfieldID packetXAxisField = (*env)->GetFieldID(env, winTabPacketClass, "PK_X", "I");
	jfieldID packetYAxisField = (*env)->GetFieldID(env, winTabPacketClass, "PK_Y", "I");
	jfieldID packetZAxisField = (*env)->GetFieldID(env, winTabPacketClass, "PK_Z", "I");
	jfieldID packetButtonsField = (*env)->GetFieldID(env, winTabPacketClass, "PK_BUTTONS", "I");
	jfieldID packetNPressureAxisField = (*env)->GetFieldID(env, winTabPacketClass, "PK_NORMAL_PRESSURE", "I");
	jfieldID packetTPressureAxisField = (*env)->GetFieldID(env, winTabPacketClass, "PK_TANGENT_PRESSURE", "I");
	jfieldID packetCursorField = (*env)->GetFieldID(env, winTabPacketClass, "PK_CURSOR", "I");
	jfieldID packetOrientationAltAxisField = (*env)->GetFieldID(env, winTabPacketClass, "PK_ORIENTATION_ALT", "I");
	jfieldID packetOrientationAzAxisField = (*env)->GetFieldID(env, winTabPacketClass, "PK_ORIENTATION_AZ", "I");
	jfieldID packetOrientationTwistAxisField = (*env)->GetFieldID(env, winTabPacketClass, "PK_ORIENTATION_TWIST", "I");
	jobject prototypePacket = newJObject(env, "net/java/games/input/WinTabPacket", "()V");
	
	retval = (*env)->NewObjectArray(env, numberRead, winTabPacketClass, NULL);
	for(i=0;i<numberRead;i++) {
		jobject tempPacket = newJObject(env, "net/java/games/input/WinTabPacket", "()V");
		
		(*env)->SetLongField(env, tempPacket, packetTimeField, packets[i].pkTime);
		(*env)->SetIntField(env, tempPacket, packetXAxisField, packets[i].pkX);
		(*env)->SetIntField(env, tempPacket, packetYAxisField, packets[i].pkY);
		(*env)->SetIntField(env, tempPacket, packetZAxisField, packets[i].pkZ);
		(*env)->SetIntField(env, tempPacket, packetButtonsField, packets[i].pkButtons);
		(*env)->SetIntField(env, tempPacket, packetNPressureAxisField, packets[i].pkNormalPressure);
		(*env)->SetIntField(env, tempPacket, packetTPressureAxisField, packets[i].pkTangentPressure);
		(*env)->SetIntField(env, tempPacket, packetCursorField, packets[i].pkCursor);
		(*env)->SetIntField(env, tempPacket, packetOrientationAltAxisField, packets[i].pkOrientation.orAzimuth);
		(*env)->SetIntField(env, tempPacket, packetOrientationAzAxisField, packets[i].pkOrientation.orAltitude);
		(*env)->SetIntField(env, tempPacket, packetOrientationTwistAxisField, packets[i].pkOrientation.orTwist);
		
		(*env)->SetObjectArrayElement(env, retval, i, tempPacket);
	}
	
	return retval;
}
