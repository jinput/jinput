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
#include "net_java_games_input_OSXHIDDevice.h"
#include "util.h"
#include "macosxutil.h"

JNIEXPORT void JNICALL Java_net_java_games_input_OSXHIDDevice_nReleaseDevice(JNIEnv *env, jclass unused, jlong device_address, jlong interface_address) {
	io_object_t hidDevice = (io_object_t)device_address;
	IOHIDDeviceInterface **device_interface = (IOHIDDeviceInterface **)(intptr_t)interface_address;;
	(*device_interface)->Release(device_interface);
	IOObjectRelease(hidDevice);
}

JNIEXPORT jobject JNICALL Java_net_java_games_input_OSXHIDDevice_nGetDeviceProperties(JNIEnv *env, jclass unused, jlong device_address) {
	io_object_t hidDevice = (io_object_t)device_address;
	CFMutableDictionaryRef properties;

	kern_return_t result = IORegistryEntryCreateCFProperties(hidDevice,
			&properties,
			kCFAllocatorDefault,
			kNilOptions);
	if (result != KERN_SUCCESS) {
		throwIOException(env, "Failed to create properties for device (%ld)", result);
		return NULL;
	}
	jobject map = createMapFromCFDictionary(env, properties);
	CFRelease(properties);
	return map;
}

JNIEXPORT void JNICALL Java_net_java_games_input_OSXHIDDevice_nOpen
  (JNIEnv * env, jclass unused, jlong lpDevice) {
    IOHIDDeviceInterface **hidDeviceInterface = (IOHIDDeviceInterface **)(intptr_t)lpDevice;
    IOReturn ioReturnValue = (*hidDeviceInterface)->open(hidDeviceInterface, 0);
    if (ioReturnValue != kIOReturnSuccess) {
        throwIOException(env, "Device open failed: %d", ioReturnValue);
    }
}

JNIEXPORT void JNICALL Java_net_java_games_input_OSXHIDDevice_nClose
  (JNIEnv * env, jclass unused, jlong lpDevice) {
    IOHIDDeviceInterface **hidDeviceInterface = (IOHIDDeviceInterface **)(intptr_t)lpDevice;
    IOReturn ioReturnValue = (*hidDeviceInterface)->close(hidDeviceInterface);
    if (ioReturnValue != kIOReturnSuccess) {
        throwIOException(env, "Device close failed: %d", ioReturnValue);
    }
}

JNIEXPORT void JNICALL Java_net_java_games_input_OSXHIDDevice_nGetElementValue
  (JNIEnv * env, jclass unused, jlong lpDevice, jlong hidCookie, jobject event_return) {
    IOHIDDeviceInterface **hidDeviceInterface = (IOHIDDeviceInterface **)(intptr_t)lpDevice;
    IOHIDElementCookie cookie = (IOHIDElementCookie)(intptr_t)hidCookie;
    IOHIDEventStruct event;

    IOReturn ioReturnValue = (*hidDeviceInterface)->getElementValue(hidDeviceInterface, cookie, &event);
    if (ioReturnValue != kIOReturnSuccess) {
        throwIOException(env, "Device getElementValue failed: %d", ioReturnValue);
        return;
    }
    copyEvent(env, &event, event_return);
    if (event.longValue != NULL) {
        free(event.longValue);
    }
}

JNIEXPORT jlong JNICALL Java_net_java_games_input_OSXHIDDevice_nCreateQueue(JNIEnv *env, jclass unused, jlong device_address) {
    IOHIDDeviceInterface **hidDeviceInterface = (IOHIDDeviceInterface **)(intptr_t)device_address;
    IOHIDQueueInterface  **queue = (*hidDeviceInterface)->allocQueue(hidDeviceInterface);

    if (queue == NULL) {
        throwIOException(env, "Could not allocate queue");
        return 0;
	}
    return (jlong)(intptr_t)queue;
}
