/*
 * %W% %E%
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

#include "winutil.h"

jbyteArray wrapGUID(JNIEnv *env, const GUID *guid) {
	jbyteArray guid_array = (*env)->NewByteArray(env, sizeof(GUID));
	if (guid_array == NULL)
		return NULL;
	(*env)->SetByteArrayRegion(env, guid_array, 0, sizeof(GUID), (jbyte *)guid);
	return guid_array;
}

void unwrapGUID(JNIEnv *env, const jobjectArray byte_array, GUID *guid) {
	(*env)->GetByteArrayRegion(env, byte_array, 0, sizeof(GUID), (jbyte *)guid);
}

