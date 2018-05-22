/*
 * %W% %E%
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
/*****************************************************************************
* Copyright (c) 2003 Sun Microsystems, Inc.  All Rights Reserved.
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
* - Redistribution of source code must retain the above copyright notice,
*   this list of conditions and the following disclaimer.
*
* - Redistribution in binary form must reproduce the above copyright notice,
*   this list of conditions and the following disclaimer in the documentation
*   and/or other materails provided with the distribution.
*
* Neither the name Sun Microsystems, Inc. or the names of the contributors
* may be used to endorse or promote products derived from this software
* without specific prior written permission.
*
* This software is provided "AS IS," without a warranty of any kind.
* ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
* ANY IMPLIED WARRANT OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
* NON-INFRINGEMEN, ARE HEREBY EXCLUDED.  SUN MICROSYSTEMS, INC. ("SUN") AND
* ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS
* A RESULT OF USING, MODIFYING OR DESTRIBUTING THIS SOFTWARE OR ITS 
* DERIVATIVES.  IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
* REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
* INCIDENTAL OR PUNITIVE DAMAGES.  HOWEVER CAUSED AND REGARDLESS OF THE THEORY
* OF LIABILITY, ARISING OUT OF THE USE OF OUR INABILITY TO USE THIS SOFTWARE,
* EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
*
* You acknowledge that this software is not designed or intended for us in
* the design, construction, operation or maintenance of any nuclear facility
*
*****************************************************************************/

#include <sys/types.h>
#include <sys/stat.h>
#include <sys/ioctl.h>
#include <unistd.h>
#include <errno.h>
#include <fcntl.h>
#include <linux/joystick.h>
#include "util.h"
#include "net_java_games_input_LinuxJoystickDevice.h"

JNIEXPORT jlong JNICALL Java_net_java_games_input_LinuxJoystickDevice_nOpen(JNIEnv *env, jclass unused, jstring path) {
	const char *path_str = (*env)->GetStringUTFChars(env, path, NULL);
	if (path_str == NULL)
		return -1;
	int fd = open(path_str, O_RDONLY | O_NONBLOCK);
	if (fd == -1)
		throwIOException(env, "Failed to open device %s (%d)\n", path_str, errno);
	(*env)->ReleaseStringUTFChars(env, path, path_str);
	return fd;
}

JNIEXPORT void JNICALL Java_net_java_games_input_LinuxJoystickDevice_nClose(JNIEnv *env, jclass unused, jlong fd_address) {
	int fd = (int)fd_address;
	int result = close(fd);
	if (result == -1)
		throwIOException(env, "Failed to close device (%d)\n", errno);
}

JNIEXPORT jstring JNICALL Java_net_java_games_input_LinuxJoystickDevice_nGetName(JNIEnv *env, jclass unused, jlong fd_address) {
#define BUFFER_SIZE 1024
	int fd = (int)fd_address;
	char device_name[BUFFER_SIZE];
	
	if (ioctl(fd, JSIOCGNAME(BUFFER_SIZE), device_name) == -1) {
		throwIOException(env, "Failed to get device name (%d)\n", errno);
		return NULL;
	}
	jstring jstr = (*env)->NewStringUTF(env, device_name);
	return jstr;
}

JNIEXPORT jint JNICALL Java_net_java_games_input_LinuxJoystickDevice_nGetVersion(JNIEnv *env, jclass unused, jlong fd_address) {
	int fd = (int)fd_address;
	__u32 version;
	if (ioctl(fd, JSIOCGVERSION, &version) == -1) {
		throwIOException(env, "Failed to get device version (%d)\n", errno);
		return -1;
	}
	return version;
}

JNIEXPORT jint JNICALL Java_net_java_games_input_LinuxJoystickDevice_nGetNumButtons(JNIEnv *env, jclass unused, jlong fd_address) {
	int fd = (int)fd_address;
	__u8 num_buttons;
	if (ioctl(fd, JSIOCGBUTTONS, &num_buttons) == -1) {
		throwIOException(env, "Failed to get number of buttons (%d)\n", errno);
		return -1;
	}
	return num_buttons;
}

JNIEXPORT jint JNICALL Java_net_java_games_input_LinuxJoystickDevice_nGetNumAxes(JNIEnv *env, jclass unused, jlong fd_address) {
	int fd = (int)fd_address;
	__u8 num_axes;
	if (ioctl(fd, JSIOCGAXES, &num_axes) == -1) {
		throwIOException(env, "Failed to get number of buttons (%d)\n", errno);
		return -1;
	}
	return num_axes;
}

JNIEXPORT jbyteArray JNICALL Java_net_java_games_input_LinuxJoystickDevice_nGetAxisMap(JNIEnv *env, jclass unused, jlong fd_address) {
	int fd = (int)fd_address;
	__u8 axis_map[ABS_MAX + 1];
	if (ioctl(fd, JSIOCGAXMAP, axis_map) == -1) {
		throwIOException(env, "Failed to get axis map (%d)\n", errno);
		return NULL;
	}
	
	jbyteArray axis_map_array = (*env)->NewByteArray(env, (ABS_MAX + 1));
	if (axis_map_array == NULL)
		return NULL;
	(*env)->SetByteArrayRegion(env, axis_map_array, 0, (ABS_MAX + 1), (jbyte *)axis_map);
	return axis_map_array;
}

JNIEXPORT jcharArray JNICALL Java_net_java_games_input_LinuxJoystickDevice_nGetButtonMap(JNIEnv *env, jclass unused, jlong fd_address) {
	int fd = (int)fd_address;
	__u16 button_map[KEY_MAX - BTN_MISC + 1];
	if (ioctl(fd, JSIOCGBTNMAP, button_map) == -1) {
		throwIOException(env, "Failed to get button map (%d)\n", errno);
		return NULL;
	}
	
	jcharArray button_map_array = (*env)->NewCharArray(env, (KEY_MAX - BTN_MISC + 1));
	if (button_map_array == NULL)
		return NULL;
	(*env)->SetCharArrayRegion(env, button_map_array, 0, (KEY_MAX - BTN_MISC + 1), (jchar *)button_map);
	return button_map_array;
}

JNIEXPORT jboolean JNICALL Java_net_java_games_input_LinuxJoystickDevice_nGetNextEvent(JNIEnv *env, jclass unused, jlong fd_address, jobject event_return) {
	int fd = (int)fd_address;
	jclass event_class = (*env)->GetObjectClass(env, event_return);
	if (event_class == NULL)
		return JNI_FALSE;
	jmethodID event_set = (*env)->GetMethodID(env, event_class, "set", "(JIII)V");
	if (event_set == NULL)
		return JNI_FALSE;
	struct js_event event;
	if (read(fd, &event, sizeof(event)) == -1) {
		if (errno == EAGAIN)
			return JNI_FALSE;
		throwIOException(env, "Failed to read next device event (%d)\n", errno);
		return JNI_FALSE;
	}
	(*env)->CallVoidMethod(env, event_return, event_set, (jlong)event.time, (jint)event.value, (jint)event.type, (jint)event.number);
	return JNI_TRUE;
}
