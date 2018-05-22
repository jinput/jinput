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

#include <IOKit/IOTypes.h>
#include <IOKit/IOKitLib.h>
#include <IOKit/IOCFPlugIn.h>
#include <IOKit/hid/IOHIDLib.h>
#include <IOKit/hid/IOHIDKeys.h>
#include <CoreFoundation/CoreFoundation.h>
#include "net_java_games_input_OSXHIDQueue.h"
#include "util.h"
#include "macosxutil.h"

JNIEXPORT void JNICALL Java_net_java_games_input_OSXHIDQueue_nOpen(JNIEnv *env, jclass unused, jlong address, jint queue_depth) {
    IOHIDQueueInterface **queue = (IOHIDQueueInterface **)(intptr_t)address;
	IOReturn ioReturnValue = (*queue)->create(queue, 0, queue_depth);
    if (ioReturnValue != kIOReturnSuccess) {
        throwIOException(env, "Queue open failed: %d\n", ioReturnValue);
        return;
    }
}

JNIEXPORT void JNICALL Java_net_java_games_input_OSXHIDQueue_nStart(JNIEnv *env, jclass unused, jlong address) {
    IOHIDQueueInterface **queue = (IOHIDQueueInterface **)(intptr_t)address;
	IOReturn ioReturnValue = (*queue)->start(queue);
    if (ioReturnValue != kIOReturnSuccess) {
        throwIOException(env, "Queue start failed: %d\n", ioReturnValue);
        return;
    }
}

JNIEXPORT void JNICALL Java_net_java_games_input_OSXHIDQueue_nStop(JNIEnv *env, jclass unused, jlong address) {
    IOHIDQueueInterface **queue = (IOHIDQueueInterface **)(intptr_t)address;
    IOReturn ioReturnValue = (*queue)->stop(queue);
    if (ioReturnValue != kIOReturnSuccess) {
        throwIOException(env, "Queue stop failed: %d\n", ioReturnValue);
        return;
    }
}

JNIEXPORT void JNICALL Java_net_java_games_input_OSXHIDQueue_nClose(JNIEnv *env, jclass unused, jlong address) {
    IOHIDQueueInterface **queue = (IOHIDQueueInterface **)(intptr_t)address;
    IOReturn ioReturnValue = (*queue)->dispose(queue);
    if (ioReturnValue != kIOReturnSuccess) {
        throwIOException(env, "Queue dispose failed: %d\n", ioReturnValue);
        return;
    }
}

JNIEXPORT void JNICALL Java_net_java_games_input_OSXHIDQueue_nReleaseQueue(JNIEnv *env, jclass unused, jlong address) {
    IOHIDQueueInterface **queue = (IOHIDQueueInterface **)(intptr_t)address;
    IOReturn ioReturnValue = (*queue)->Release(queue);
    if (ioReturnValue != kIOReturnSuccess) {
        throwIOException(env, "Queue Release failed: %d\n", ioReturnValue);
        return;
    }
}

JNIEXPORT void JNICALL Java_net_java_games_input_OSXHIDQueue_nAddElement(JNIEnv *env, jclass unused, jlong address, jlong cookie_address) {
    IOHIDQueueInterface **queue = (IOHIDQueueInterface **)(intptr_t)address;
    IOHIDElementCookie cookie = (IOHIDElementCookie)(intptr_t)cookie_address;

    IOReturn ioReturnValue = (*queue)->addElement(queue, cookie, 0);
    if (ioReturnValue != kIOReturnSuccess) {
        throwIOException(env, "Queue addElement failed: %d\n", ioReturnValue);
        return;
    }
}

JNIEXPORT void JNICALL Java_net_java_games_input_OSXHIDQueue_nRemoveElement(JNIEnv *env, jclass unused, jlong address, jlong cookie_address) {
    IOHIDQueueInterface **queue = (IOHIDQueueInterface **)(intptr_t)address;
    IOHIDElementCookie cookie = (IOHIDElementCookie)(intptr_t)cookie_address;

    IOReturn ioReturnValue = (*queue)->removeElement(queue, cookie);
    if (ioReturnValue != kIOReturnSuccess) {
        throwIOException(env, "Queue removeElement failed: %d\n", ioReturnValue);
        return;
    }
}


JNIEXPORT jboolean JNICALL Java_net_java_games_input_OSXHIDQueue_nGetNextEvent(JNIEnv *env, jclass unused, jlong address, jobject event_return) {
    IOHIDQueueInterface **queue = (IOHIDQueueInterface **)(intptr_t)address;
    IOHIDEventStruct event;

    AbsoluteTime zeroTime = {0, 0};
    IOReturn ioReturnValue = (*queue)->getNextEvent(queue, &event, zeroTime, 0);
    if (ioReturnValue == kIOReturnUnderrun) {
		return JNI_FALSE;
	} else if (ioReturnValue != kIOReturnSuccess) {
        throwIOException(env, "Queue getNextEvent failed: %d\n", ioReturnValue);
        return JNI_FALSE;
    }
    copyEvent(env, &event, event_return);
    if (event.longValue != NULL) {
        free(event.longValue);
    }
	return JNI_TRUE;
}
