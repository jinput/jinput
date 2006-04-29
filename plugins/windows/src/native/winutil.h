/*
 * %W% %E%
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

#ifndef _WINUTIL_H
#define _WINUTIL_H

#include <windows.h>
#include <jni.h>

extern jbyteArray wrapGUID(JNIEnv *env, const GUID *guid);
extern void unwrapGUID(JNIEnv *env, const jobjectArray byte_array, GUID *guid);

#endif
