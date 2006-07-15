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

#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include "util.h"

static jstring sprintfJavaString(JNIEnv *env, const char *format, va_list ap) {
#define BUFFER_SIZE 4000
    char buffer[BUFFER_SIZE];
    jstring str; 
#ifdef _MSC_VER
	vsnprintf_s(buffer, BUFFER_SIZE, _TRUNCATE, format, ap);
#else
    vsnprintf(buffer, BUFFER_SIZE, format, ap);
#endif
    buffer[BUFFER_SIZE - 1] = '\0';
    str = (*env)->NewStringUTF(env, buffer);
    return str;
}

void printfJava(JNIEnv *env, const char *format, ...) {
    jstring str; 
    jclass org_lwjgl_LWJGLUtil_class;
    jmethodID log_method;
    va_list ap;
    va_start(ap, format);
    str = sprintfJavaString(env, format, ap);
    va_end(ap);
    org_lwjgl_LWJGLUtil_class = (*env)->FindClass(env, "net/java/games/input/ControllerEnvironment");
	if (org_lwjgl_LWJGLUtil_class == NULL)
		return;
    log_method = (*env)->GetStaticMethodID(env, org_lwjgl_LWJGLUtil_class, "log", "(Ljava/lang/String;)V");
	if (log_method == NULL)
		return;
    (*env)->CallStaticVoidMethod(env, org_lwjgl_LWJGLUtil_class, log_method, str);
}                                                                                                                                                                                                                

static void throwException(JNIEnv * env, const char *exception_name, const char *format, va_list ap) {
    jstring str;
    jobject exception;

    if ((*env)->ExceptionCheck(env) == JNI_TRUE)
        return; // The JVM crashes if we try to throw two exceptions from one native call
    str = sprintfJavaString(env, format, ap);
	exception = newJObject(env, exception_name, "(Ljava/lang/String;)V", str);
    (*env)->Throw(env, exception);
}

void throwRuntimeException(JNIEnv * env, const char *format, ...) {
    va_list ap;
    va_start(ap, format);
    throwException(env, "java/lang/RuntimeException", format, ap);
    va_end(ap);
}

void throwIOException(JNIEnv * env, const char *format, ...) {
    va_list ap;
    va_start(ap, format);
    throwException(env, "java/io/IOException", format, ap);
    va_end(ap);
}

jobject newJObject(JNIEnv * env, const char *class_name, const char *constructor_signature, ...) {
    va_list ap;
	jclass clazz;
	jmethodID constructor;
	jobject obj;

	clazz = (*env)->FindClass(env, class_name);
	if (clazz == NULL)
		return NULL;
	constructor = (*env)->GetMethodID(env, clazz, "<init>", constructor_signature);
	if (constructor == NULL)
		return NULL;
    va_start(ap, constructor_signature);
	obj = (*env)->NewObjectV(env, clazz, constructor, ap);
    va_end(ap);
	return obj;
}

