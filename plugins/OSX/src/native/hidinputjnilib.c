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
#include <unistd.h>
#include <ctype.h>
#include <sys/errno.h>
#include <sysexits.h>
#include <mach/mach.h>
#include <mach/mach_error.h>
#include <IOKit/IOKitLib.h>
#include <IOKit/IOCFPlugIn.h>
#include <IOKit/hid/IOHIDLib.h>
#include <IOKit/hid/IOHIDKeys.h>
#include <CoreFoundation/CoreFoundation.h>
#include <Carbon/Carbon.h>
#include "JNIWrapper.h"

Boolean init( JNIEnv * env );
void createMasterPort();
void disposeMasterPort();

Boolean showDictionaryElement (CFDictionaryRef dictionary, CFStringRef key);
void showProperty(const void * key, const void * value);
void displayCFProperty(CFStringRef object, CFTypeRef value);
void CFObjectShow( CFTypeRef value );

jclass              CLASS_JNIWrapper = NULL;
jmethodID           MID_AddDevice = NULL;
mach_port_t         masterPort = NULL;
io_iterator_t       hidObjectIterator;
int                 gElementIndex;

Boolean init(JNIEnv* env)
{
    CLASS_JNIWrapper = (*env)->FindClass(env,"JNIWrapper");
    if ( CLASS_JNIWrapper == NULL )
    {
        printf("Class JNIWrapper not found... \n");
        return FALSE;
    }
    
    MID_AddDevice = (*env)->GetMethodID(env, CLASS_JNIWrapper, "addDevice", "(Ljava/util/ArrayList;JILjava/lang/String;)V");
    if (MID_AddDevice == NULL)
    {
        printf("Method addDevice not found... \n");
        return FALSE;
    }
    
    return TRUE;
}

void createMasterPort()
{
    IOReturn ioReturnValue = kIOReturnSuccess;
    
    //Get a Mach port to initiate communication with I/O Kit.
    //
    ioReturnValue = IOMasterPort(bootstrap_port, &masterPort);
}

void disposeMasterPort()
{
    //Free master port if we created one.
    //
    if (masterPort)
    {
        mach_port_deallocate(mach_task_self(), masterPort);
    }
}


Boolean showDictionaryElement (CFDictionaryRef dictionary, CFStringRef key)
{
    CFTypeRef object = CFDictionaryGetValue (dictionary, key);
    if (object)
    {
        displayCFProperty (key,object);
    }
    return (object != NULL);
}

static void showCFArray (const void * value, void * parameter)
{
    if (CFGetTypeID (value) != CFDictionaryGetTypeID ())
    {
        return;
    }
    
    CFObjectShow(value);
}

void displayCFProperty(CFStringRef object, CFTypeRef value)
{
    const char * c = CFStringGetCStringPtr (object, CFStringGetSystemEncoding ());
    if (c)
    {
        printf ("%s", c);
    }
    else
    {
        CFIndex bufferSize = CFStringGetLength (object) + 1;
        char * buffer = (char *)malloc (bufferSize);
        if (buffer)
        {
            if (CFStringGetCString (object, buffer, bufferSize, CFStringGetSystemEncoding ()))
                printf ("%s", buffer);
            free(buffer);
        }
    }
    
    printf("=");
    
    CFObjectShow( value );

    printf("\n");
    
}

void CFObjectShow( CFTypeRef value )
{
    CFTypeID type = CFGetTypeID(value);
    if (type == CFArrayGetTypeID())
    {
        CFRange range = {0, CFArrayGetCount (value)};
        CFIndex savedIndex = gElementIndex;
        
        //Show an element array containing one or more element dictionaries
        gElementIndex = 0; //Reset index to zero
        CFArrayApplyFunction (value, range, showCFArray, 0);
        
        gElementIndex = savedIndex;        
    }
    else if (type == CFBooleanGetTypeID())
    {
        printf(CFBooleanGetValue(value) ? "true" : "false");
    }
    else if (type == CFDictionaryGetTypeID())
    {
        showDictionaryElement (value, CFSTR(kIOHIDElementCookieKey));
        showDictionaryElement (value, CFSTR(kIOHIDElementCollectionTypeKey));
        showDictionaryElement (value, CFSTR(kIOHIDElementMinKey));
        showDictionaryElement (value, CFSTR(kIOHIDElementMaxKey));
        showDictionaryElement (value, CFSTR(kIOHIDElementScaledMinKey));
        showDictionaryElement (value, CFSTR(kIOHIDElementScaledMaxKey));
        showDictionaryElement (value, CFSTR(kIOHIDElementSizeKey));
        showDictionaryElement (value, CFSTR(kIOHIDElementIsRelativeKey));
        showDictionaryElement (value, CFSTR(kIOHIDElementIsWrappingKey));
        showDictionaryElement (value, CFSTR(kIOHIDElementIsNonLinearKey));
#ifdef kIOHIDElementHasPreferredStateKey
        showDictionaryElement (value, CFSTR(kIOHIDElementHasPreferredStateKey));
#else
        showDictionaryElement (value, CFSTR(kIOHIDElementHasPreferedStateKey));
#endif
        showDictionaryElement (value, CFSTR(kIOHIDElementHasNullStateKey));
        showDictionaryElement (value, CFSTR(kIOHIDElementVendorSpecificKey));
        showDictionaryElement (value, CFSTR(kIOHIDElementUnitKey));
        showDictionaryElement (value, CFSTR(kIOHIDElementUnitExponentKey));
        showDictionaryElement (value, CFSTR(kIOHIDElementNameKey));
        showDictionaryElement (value, CFSTR(kIOHIDElementKey));    
        
        printf("\n\n\n");
    }
    else if (type == CFNumberGetTypeID())
    {
        long number;
        if (CFNumberGetValue (value, kCFNumberLongType, &number))
        {
            printf("0x%lx (%ld)", number, number);
        }
    }
    else if (type == CFStringGetTypeID())
    {
        const char * c = CFStringGetCStringPtr (value, CFStringGetSystemEncoding ());
        if (c)
        {
            printf ("%s", c);
        }
        else
        {
            CFIndex bufferSize = CFStringGetLength (value) + 1;
            char * buffer = (char *)malloc (bufferSize);
            if (buffer)
            {
                if (CFStringGetCString (value, buffer, bufferSize, CFStringGetSystemEncoding ()))
                {
                    printf ("%s", buffer);
                }
                
                free(buffer);
            }
        }
    }
}


/*
 * Class:     JNIWrapper
 * Method:    hidCreate
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_JNIWrapper_hidCreate
(JNIEnv * env, jobject obj)
{
    if ( init( env ) )
    {
        createMasterPort();
    }
}

/*
 * Class:     JNIWrapper
 * Method:    hidDispose
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_JNIWrapper_hidDispose
(JNIEnv * env, jobject obj)
{
    disposeMasterPort();
}


/*
 * Class:     JNIWrapper
 * Method:    enumDevices
 * Signature: (Ljava/util/ArrayList;)V
 */
JNIEXPORT void JNICALL Java_JNIWrapper_enumDevices
(JNIEnv * env, jobject obj , jobject list)
{
    CFMutableDictionaryRef hidMatchDictionary = NULL;
    IOReturn ioReturnValue = kIOReturnSuccess;
    Boolean noMatchingDevices = false;
    
    // Set up a matching dictionary to search the I/O Registry by class
    // name for all HID class devices
    //
    hidMatchDictionary = IOServiceMatching(kIOHIDDeviceKey);
    
    // Now search I/O Registry for matching devices.
    //
    ioReturnValue = IOServiceGetMatchingServices(masterPort, hidMatchDictionary, &hidObjectIterator);
    
    noMatchingDevices = ((ioReturnValue != kIOReturnSuccess) | (hidObjectIterator == NULL));
    
    // If search is unsuccessful, print message and hang.
    //
    if (noMatchingDevices)
    {
        printf("No matching HID class devices found.");
    }
    
    // IOServiceGetMatchingServices consumes a reference to the
    //   dictionary, so we don't need to release the dictionary ref.
    //
    hidMatchDictionary = NULL;    
    
    io_object_t             hidDevice = NULL;
    CFMutableDictionaryRef  properties = 0;
    char                    path[512];
    kern_return_t           result;
    
    
    while ((hidDevice = IOIteratorNext(hidObjectIterator)))
    {    
        result = IORegistryEntryGetPath(hidDevice, kIOServicePlane, path);
        
        if ( result == KERN_SUCCESS )
        {
            result = IORegistryEntryCreateCFProperties(hidDevice,
                                                       &properties,
                                                       kCFAllocatorDefault,
                                                       kNilOptions);
        }
        
        if ((result == KERN_SUCCESS) && properties)
        {
            //showDictionaryElement(properties, CFSTR(kIOHIDTransportKey));
            //showDictionaryElement(properties, CFSTR(kIOHIDVendorKey));
            //printf("ProductID: "); showDictionaryElement(properties, CFSTR(kIOHIDProductIDKey));
            //printf("VersionNumber: "); showDictionaryElement(properties, CFSTR(kIOHIDVersionNumberKey));
            //printf("Manufacturer: "); showDictionaryElement(properties, CFSTR(kIOHIDManufacturerKey));
            printf("ProductKey: "); showDictionaryElement(properties, CFSTR(kIOHIDProductKey));
            //printf("SerialNumber: "); showDictionaryElement(properties, CFSTR(kIOHIDSerialNumberKey));
            //showDictionaryElement(properties, CFSTR(kIOHIDLocationIDKey));
            //printf("PrimaryUsage: "); showDictionaryElement(properties, CFSTR(kIOHIDPrimaryUsageKey));
            //showDictionaryElement(properties, CFSTR(kIOHIDPrimaryUsagePageKey));
            //showDictionaryElement(properties, CFSTR(kIOHIDElementKey));
            
            //printf("\n\n");
            CFTypeRef object = CFDictionaryGetValue (properties, CFSTR(kIOHIDProductKey));

            
            
            (*env)->CallVoidMethod(env, obj, MID_AddDevice, 
                                   list, 
                                   (jlong)(long)hidDevice, 
                                   kIOHIDPrimaryUsageKey, 
                                   (*env)->NewStringUTF( env, CFStringGetCStringPtr ( object, CFStringGetSystemEncoding ()) ) );
            
            
            //Release the properties dictionary
            CFRelease(properties);
        }        
    }

    IOObjectRelease(hidObjectIterator);    
}




