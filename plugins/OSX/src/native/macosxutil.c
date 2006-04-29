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

#include <CoreServices/CoreServices.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <jni.h>
#include "util.h"
#include "macosxutil.h"

typedef struct {
	JNIEnv *env;
	jobject map;
} dict_context_t;

typedef struct {
	JNIEnv *env;
	jobjectArray array;
	jsize index;
} array_context_t;

static jobject createObjectFromCFObject(JNIEnv *env, CFTypeRef cfobject);

static jstring createStringFromCFString(JNIEnv *env, CFStringRef cfstring) {
	CFIndex unicode_length = CFStringGetLength(cfstring);
	CFIndex utf8_length = CFStringGetMaximumSizeForEncoding(unicode_length, kCFStringEncodingUTF8);
	// Allocate buffer large enough, plus \0 terminator
	char *buffer = (char *)malloc(utf8_length + 1);
	if (buffer == NULL)
		return NULL;
	Boolean result = CFStringGetCString(cfstring, buffer, utf8_length + 1, kCFStringEncodingUTF8);
	if (!result) {
		free(buffer);
		return NULL;
	}
	jstring str = (*env)->NewStringUTF(env, buffer);
	free(buffer);
	return str;
}

static jobject createDoubleObjectFromCFNumber(JNIEnv *env, CFNumberRef cfnumber) {
	double value;
	Boolean result = CFNumberGetValue(cfnumber, kCFNumberDoubleType, &value);
	if (!result)
		return NULL;
	return newJObject(env, "java/lang/Double", "(D)V", (jdouble)value); 
}

static jobject createLongObjectFromCFNumber(JNIEnv *env, CFNumberRef cfnumber) {
	SInt64 value;
	Boolean result = CFNumberGetValue(cfnumber, kCFNumberSInt64Type, &value);
	if (!result)
		return NULL;
	return newJObject(env, "java/lang/Long", "(J)V", (jlong)value); 
}

static jobject createNumberFromCFNumber(JNIEnv *env, CFNumberRef cfnumber) {
	CFNumberType number_type = CFNumberGetType(cfnumber);
	switch (number_type) {
		case kCFNumberSInt8Type:
		case kCFNumberSInt16Type:
		case kCFNumberSInt32Type:
		case kCFNumberSInt64Type:
		case kCFNumberCharType:
		case kCFNumberShortType:
		case kCFNumberIntType:
		case kCFNumberLongType:
		case kCFNumberLongLongType:
		case kCFNumberCFIndexType:
			return createLongObjectFromCFNumber(env, cfnumber);
		case kCFNumberFloat32Type:
		case kCFNumberFloat64Type:
		case kCFNumberFloatType:
		case kCFNumberDoubleType:
			return createDoubleObjectFromCFNumber(env, cfnumber);
		default:
			return NULL;
	}
}

static void createArrayEntries(const void *value, void *context) {
	array_context_t *array_context = (array_context_t *)context;
	jobject jval = createObjectFromCFObject(array_context->env, value);
	(*array_context->env)->SetObjectArrayElement(array_context->env, array_context->array, array_context->index++, jval);
	(*array_context->env)->DeleteLocalRef(array_context->env, jval);
}

static jobject createArrayFromCFArray(JNIEnv *env, CFArrayRef cfarray) {
	jclass Object_class = (*env)->FindClass(env, "java/lang/Object");
	if (Object_class == NULL)
		return NULL;
	CFIndex size = CFArrayGetCount(cfarray);
	CFRange range = {0, size};
	jobjectArray array = (*env)->NewObjectArray(env, size, Object_class, NULL);
	array_context_t array_context;
	array_context.env = env;
	array_context.array = array;
	array_context.index = 0;
	CFArrayApplyFunction(cfarray, range, createArrayEntries, &array_context);
	return array;
}

static jobject createObjectFromCFObject(JNIEnv *env, CFTypeRef cfobject) {
	CFTypeID type_id = CFGetTypeID(cfobject);
	if (type_id == CFDictionaryGetTypeID()) {
		return createMapFromCFDictionary(env, cfobject);
	} else if (type_id == CFArrayGetTypeID()) {
		return createArrayFromCFArray(env, cfobject);
	} else if (type_id == CFStringGetTypeID()) {
		return createStringFromCFString(env, cfobject);
	} else if (type_id == CFNumberGetTypeID()) {
		return createNumberFromCFNumber(env, cfobject);
	} else {
		return NULL;
	}
}

static void createMapKeys(const void *key, const void *value, void *context) {
	dict_context_t *dict_context = (dict_context_t *)context;

	jclass Map_class = (*dict_context->env)->GetObjectClass(dict_context->env, dict_context->map);
	if (Map_class == NULL)
		return;
	jmethodID map_put = (*dict_context->env)->GetMethodID(dict_context->env, Map_class, "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
	if (map_put == NULL)
		return;
	jobject jkey = createObjectFromCFObject(dict_context->env, key);
	jobject jvalue = createObjectFromCFObject(dict_context->env, value);
	if (jkey == NULL || jvalue == NULL)
		return;
	(*dict_context->env)->CallObjectMethod(dict_context->env, dict_context->map, map_put, jkey, jvalue);
	(*dict_context->env)->DeleteLocalRef(dict_context->env, jkey);
	(*dict_context->env)->DeleteLocalRef(dict_context->env, jvalue);
}

jobject createMapFromCFDictionary(JNIEnv *env, CFDictionaryRef dict) {
	jobject map = newJObject(env, "java/util/HashMap", "()V");
	if (map == NULL)
		return NULL;
	dict_context_t dict_context;
	dict_context.env = env;
	dict_context.map = map;
	CFDictionaryApplyFunction(dict, createMapKeys, &dict_context);
	return map;
}

void copyEvent(JNIEnv *env, IOHIDEventStruct *event, jobject event_return) {
    jclass OSXEvent_class = (*env)->GetObjectClass(env, event_return);
    if (OSXEvent_class == NULL) {
        return;
    }
    
    jmethodID OSXEvent_set = (*env)->GetMethodID(env, OSXEvent_class, "set", "(JJIJ)V");
    if (OSXEvent_set == NULL) {
        return;
    }
	Nanoseconds nanos = AbsoluteToNanoseconds(event->timestamp);
    uint64_t nanos64= *((uint64_t *)&nanos);
    (*env)->CallVoidMethod(env, event_return, OSXEvent_set, (jlong)event->type, (jlong)(intptr_t)event->elementCookie, (jint)event->value, (jlong)nanos64);
}
