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
#include <linux/input.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include "util.h"
#include "net_java_games_input_LinuxEventDevice.h"

JNIEXPORT jlong JNICALL Java_net_java_games_input_LinuxEventDevice_nOpen(JNIEnv *env, jclass unused, jstring path, jboolean rw_flag) {
	const char *path_str = (*env)->GetStringUTFChars(env, path, NULL);
	if (path_str == NULL)
		return -1;
	int flags = rw_flag == JNI_TRUE ? O_RDWR : O_RDONLY;
	flags = flags | O_NONBLOCK;
	int fd = open(path_str, flags);
	if (fd == -1)
		throwIOException(env, "Failed to open device %s (%d)\n", path_str, errno);
	(*env)->ReleaseStringUTFChars(env, path, path_str);
	return fd;
}

JNIEXPORT void JNICALL Java_net_java_games_input_LinuxEventDevice_nClose(JNIEnv *env, jclass unused, jlong fd_address) {
	int fd = (int)fd_address;
	int result = close(fd);
	if (result == -1)
		throwIOException(env, "Failed to close device (%d)\n", errno);
}

JNIEXPORT jstring JNICALL Java_net_java_games_input_LinuxEventDevice_nGetName(JNIEnv *env, jclass unused, jlong fd_address) {
#define BUFFER_SIZE 1024
	int fd = (int)fd_address;
	char device_name[BUFFER_SIZE];
	
	if (ioctl(fd, EVIOCGNAME(BUFFER_SIZE), device_name) == -1) {
		throwIOException(env, "Failed to get device name (%d)\n", errno);
		return NULL;
	}
	jstring jstr = (*env)->NewStringUTF(env, device_name);
	return jstr;
}

JNIEXPORT void JNICALL Java_net_java_games_input_LinuxEventDevice_nGetKeyStates(JNIEnv *env, jclass unused, jlong fd_address, jbyteArray bits_array) {
	int fd = (int)fd_address;
	jsize len = (*env)->GetArrayLength(env, bits_array);
	jbyte *bits = (*env)->GetByteArrayElements(env, bits_array, NULL);
	if (bits == NULL)
		return;
	int res = ioctl(fd, EVIOCGKEY(len), bits);
	(*env)->ReleaseByteArrayElements(env, bits_array, bits, 0);
	if (res == -1)
		throwIOException(env, "Failed to get device key states (%d)\n", errno);
}

JNIEXPORT jint JNICALL Java_net_java_games_input_LinuxEventDevice_nGetVersion(JNIEnv *env, jclass unused, jlong fd_address) {
	int fd = (int)fd_address;
	int version;
	if (ioctl(fd, EVIOCGVERSION, &version) == -1) {
		throwIOException(env, "Failed to get device version (%d)\n", errno);
		return -1;
	}
	return version;
}

JNIEXPORT jint JNICALL Java_net_java_games_input_LinuxEventDevice_nGetNumEffects(JNIEnv *env, jclass unused, jlong fd_address) {
	int fd = (int)fd_address;
	int num_effects;
	if (ioctl(fd, EVIOCGEFFECTS, &num_effects) == -1) {
		throwIOException(env, "Failed to get number of device effects (%d)\n", errno);
		return -1;
	}
	return num_effects;
}

JNIEXPORT void JNICALL Java_net_java_games_input_LinuxEventDevice_nGetDeviceUsageBits(JNIEnv *env, jclass unused, jlong fd_address, jbyteArray usages_array) {
#if EV_VERSION >= 0x010001
	int fd = (int)fd_address;
	jsize len = (*env)->GetArrayLength(env, usages_array);
	jbyte *usages = (*env)->GetByteArrayElements(env, usages_array, NULL);
	if (usages == NULL)
		return;
	int res = ioctl(fd, EVIOCGUSAGE(len), usages);
	(*env)->ReleaseByteArrayElements(env, usages_array, usages, 0);
	if (res == -1)
		throwIOException(env, "Failed to get device usages (%d)\n", errno);
#endif
}

JNIEXPORT void JNICALL Java_net_java_games_input_LinuxEventDevice_nGetBits(JNIEnv *env, jclass unused, jlong fd_address, jint evtype, jbyteArray bits_array) {
	int fd = (int)fd_address;
	jsize len = (*env)->GetArrayLength(env, bits_array);
	jbyte *bits = (*env)->GetByteArrayElements(env, bits_array, NULL);
	if (bits == NULL)
		return;
	int res = ioctl(fd, EVIOCGBIT(evtype, len), bits);
	(*env)->ReleaseByteArrayElements(env, bits_array, bits, 0);
	if (res == -1)
		throwIOException(env, "Failed to get device bits (%d)\n", errno);
}

JNIEXPORT jobject JNICALL Java_net_java_games_input_LinuxEventDevice_nGetInputID(JNIEnv *env, jclass unused, jlong fd_address) {
	int fd = (int)fd_address;
	jclass input_id_class = (*env)->FindClass(env, "net/java/games/input/LinuxInputID");
	if (input_id_class == NULL)
		return NULL;
	jmethodID input_id_constructor = (*env)->GetMethodID(env, input_id_class, "<init>", "(IIII)V");
	if (input_id_constructor == NULL)
		return NULL;
	struct input_id id;
	int result = ioctl(fd, EVIOCGID, &id);
	if (result == -1) {
		throwIOException(env, "Failed to get input id for device (%d)\n", errno);
		return NULL;
	}
	return (*env)->NewObject(env, input_id_class, input_id_constructor, (jint)id.bustype, (jint)id.vendor, (jint)id.product, (jint)id.version);
}

JNIEXPORT void JNICALL Java_net_java_games_input_LinuxEventDevice_nGetAbsInfo(JNIEnv *env, jclass unused, jlong fd_address, jint abs_axis, jobject abs_info_return) {
	int fd = (int)fd_address;
	jclass abs_info_class = (*env)->GetObjectClass(env, abs_info_return);
	if (abs_info_class == NULL)
		return;
	jmethodID abs_info_set = (*env)->GetMethodID(env, abs_info_class, "set", "(IIIII)V");
	if (abs_info_set == NULL)
		return;
	struct input_absinfo abs_info;
	int result = ioctl(fd, EVIOCGABS(abs_axis), &abs_info);
	if (result == -1) {
		throwIOException(env, "Failed to get abs info for axis (%d)\n", errno);
		return;
	}
	(*env)->CallVoidMethod(env, abs_info_return, abs_info_set, (jint)abs_info.value, (jint)abs_info.minimum, (jint)abs_info.maximum, (jint)abs_info.fuzz, (jint)abs_info.flat);
}

JNIEXPORT jboolean JNICALL Java_net_java_games_input_LinuxEventDevice_nGetNextEvent(JNIEnv *env, jclass unused, jlong fd_address, jobject linux_event_return) {
	int fd = (int)fd_address;
	jclass linux_event_class = (*env)->GetObjectClass(env, linux_event_return);
	if (linux_event_class == NULL)
		return JNI_FALSE;
	jmethodID linux_event_set = (*env)->GetMethodID(env, linux_event_class, "set", "(JJIII)V");
	if (linux_event_set == NULL)
		return JNI_FALSE;
	struct input_event event;
	if (read(fd, &event, sizeof(struct input_event)) == -1) {
		if (errno == EAGAIN)
			return JNI_FALSE;
		throwIOException(env, "Failed to read next device event (%d)\n", errno);
		return JNI_FALSE;
	}
	(*env)->CallVoidMethod(env, linux_event_return, linux_event_set, (jlong)event.time.tv_sec, (jlong)event.time.tv_usec, (jint)event.type, (jint)event.code, (jint)event.value);
	return JNI_TRUE;
}

JNIEXPORT jint JNICALL Java_net_java_games_input_LinuxEventDevice_nUploadRumbleEffect(JNIEnv *env, jclass unused, jlong fd_address, jint id, jint direction, jint trigger_button, jint trigger_interval, jint replay_length, jint replay_delay, jint strong_magnitude, jint weak_magnitude) {
	int fd = (int)fd_address;
	struct ff_effect effect;

	effect.type = FF_RUMBLE;
	effect.id = id;
	effect.trigger.button = trigger_button;
	effect.trigger.interval = trigger_interval;
	effect.replay.length = replay_length;
	effect.replay.delay = replay_delay;
	effect.direction = direction;
	effect.u.rumble.strong_magnitude = strong_magnitude;
	effect.u.rumble.weak_magnitude = weak_magnitude;

	if (ioctl(fd, EVIOCSFF, &effect) == -1) {
		throwIOException(env, "Failed to upload effect (%d)\n", errno);
		return -1;
	}
	return effect.id;
}

JNIEXPORT jint JNICALL Java_net_java_games_input_LinuxEventDevice_nUploadConstantEffect(JNIEnv *env, jclass unused, jlong fd_address, jint id, jint direction, jint trigger_button, jint trigger_interval, jint replay_length, jint replay_delay, jint constant_level, jint constant_env_attack_length, jint constant_env_attack_level, jint constant_env_fade_length, jint constant_env_fade_level) {
	int fd = (int)fd_address;
	struct ff_effect effect;

	effect.type = FF_CONSTANT;
	effect.id = id;
	effect.trigger.button = trigger_button;
	effect.trigger.interval = trigger_interval;
	effect.replay.length = replay_length;
	effect.replay.delay = replay_delay;
	effect.direction = direction;
	effect.u.constant.level = constant_level;
	effect.u.constant.envelope.attack_length = constant_env_attack_length;
	effect.u.constant.envelope.attack_level = constant_env_attack_level;
	effect.u.constant.envelope.fade_length = constant_env_fade_length;
	effect.u.constant.envelope.fade_level = constant_env_fade_level;

	if (ioctl(fd, EVIOCSFF, &effect) == -1) {
		throwIOException(env, "Failed to upload effect (%d)\n", errno);
		return -1;
	}
	return effect.id;
}

JNIEXPORT void JNICALL Java_net_java_games_input_LinuxEventDevice_nWriteEvent(JNIEnv *env, jclass unused, jlong fd_address, jint type, jint code, jint value) {
	int fd = (int)fd_address;
	struct input_event event;
	event.type = type;
	event.code = code;
	event.value = value;

	if (write(fd, &event, sizeof(event)) == -1) {
		throwIOException(env, "Failed to write to device (%d)\n", errno);
	}
}

JNIEXPORT void JNICALL Java_net_java_games_input_LinuxEventDevice_nEraseEffect(JNIEnv *env, jclass unused, jlong fd_address, jint ff_id) {
	int fd = (int)fd_address;
	int ff_id_int = ff_id;

	if (ioctl(fd, EVIOCRMFF, &ff_id_int) == -1)
		throwIOException(env, "Failed to erase effect (%d)\n", errno);
}
