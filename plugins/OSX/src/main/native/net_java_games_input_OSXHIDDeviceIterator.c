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
#include "net_java_games_input_OSXHIDDeviceIterator.h"
#include "util.h"

JNIEXPORT jlong JNICALL Java_net_java_games_input_OSXHIDDeviceIterator_nCreateIterator(JNIEnv *env, jclass unused) {
    io_iterator_t hidObjectIterator;
    // Set up a matching dictionary to search the I/O Registry by class
    // name for all HID class devices
    //
    CFMutableDictionaryRef hidMatchDictionary = IOServiceMatching(kIOHIDDeviceKey);
    
    // Now search I/O Registry for matching devices.
    // IOServiceGetMatchingServices consumes a reference to the dictionary so we don't have to release it
    IOReturn ioReturnValue = IOServiceGetMatchingServices(kIOMasterPortDefault, hidMatchDictionary, &hidObjectIterator);
    
    if (ioReturnValue != kIOReturnSuccess) {
		throwIOException(env, "Failed to create iterator (%ld)\n", ioReturnValue);
		return 0;
	}
		
	if (hidObjectIterator == IO_OBJECT_NULL) {
		throwIOException(env, "Failed to create iterator\n");
		return 0;
	}
	return (jlong)hidObjectIterator;
}

JNIEXPORT void JNICALL Java_net_java_games_input_OSXHIDDeviceIterator_nReleaseIterator(JNIEnv *env, jclass unused, jlong address) {
	io_iterator_t iterator = (io_iterator_t)address;
    IOObjectRelease(iterator);
}

static IOHIDDeviceInterface **createHIDDevice(JNIEnv *env, io_object_t hidDevice) {
//    io_name_t               className;
	IOHIDDeviceInterface **hidDeviceInterface;
    IOCFPlugInInterface     **plugInInterface;
    SInt32                  score;
    
/*    ioReturnValue = IOObjectGetClass(hidDevice, className);
    if (ioReturnValue != kIOReturnSuccess) {
        printfJava(env, "Failed to get IOObject class name.");
    }
    
    printfJava(env, "Found device type [%s]\n", className);
  */  
    IOReturn ioReturnValue = IOCreatePlugInInterfaceForService(hidDevice,
                                                      kIOHIDDeviceUserClientTypeID,
                                                      kIOCFPlugInInterfaceID,
                                                      &plugInInterface,
                                                      &score);

	if (ioReturnValue != kIOReturnSuccess) {
		throwIOException(env, "Couldn't create plugin for device interface (%ld)\n", ioReturnValue);
		return NULL;
	}
	//Call a method of the intermediate plug-in to create the device 
	//interface
	//
	HRESULT plugInResult = (*plugInInterface)->QueryInterface(plugInInterface,
			CFUUIDGetUUIDBytes(kIOHIDDeviceInterfaceID),
			(LPVOID)&hidDeviceInterface);
	(*plugInInterface)->Release(plugInInterface);
	if (plugInResult != S_OK) {
		throwIOException(env, "Couldn't create HID class device interface (%ld)\n", plugInResult);
		return NULL;
	}
	return hidDeviceInterface;
}

JNIEXPORT jobject JNICALL Java_net_java_games_input_OSXHIDDeviceIterator_nNext(JNIEnv *env, jclass unused, jlong address) {
	io_iterator_t iterator = (io_iterator_t)address;
    io_object_t             hidDevice;
//    io_string_t             path;
//    kern_return_t           result;
    
    hidDevice = IOIteratorNext(iterator);
	if (hidDevice == MACH_PORT_NULL)
		return NULL;
/*	IOResult result = IORegistryEntryGetPath(hidDevice, kIOServicePlane, path);

	if (result != KERN_SUCCESS) {
		IOObjectRelease(hidDevice);
		throwIOException("Failed to get device path (%ld)\n", result);
		return NULL;
	}
*/
	IOHIDDeviceInterface **device_interface = createHIDDevice(env, hidDevice);
	if (device_interface == NULL) {
		IOObjectRelease(hidDevice);
		return NULL;
	}
    jobject device_object = newJObject(env, "net/java/games/input/OSXHIDDevice", "(JJ)V", (jlong)hidDevice, (jlong)(intptr_t)device_interface);
	if (device_object == NULL) {
		(*device_interface)->Release(device_interface);
		IOObjectRelease(hidDevice);
		return NULL;
	}
	return device_object;
}
