/*
 *  hidinputjnilib.c
 *  hidinput
 *
 *  Created by Gregory Pierce on Wed Jul 23 2003.
 *  Copyright (c) 2003 __MyCompanyName__. All rights reserved. 
 *
 */

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

void createMasterPort();
void disposeMasterPort();
void enumDevices();

Boolean showDictionaryElement (CFDictionaryRef dictionary, CFStringRef key);
void showProperty(const void * key, const void * value);
void displayCFProperty(CFStringRef object, CFTypeRef value);
void CFObjectShow( CFTypeRef value );

mach_port_t masterPort = NULL;
io_iterator_t hidObjectIterator;
int gElementIndex;

void createMasterPort()
{
    IOReturn ioReturnValue = kIOReturnSuccess;
    
    //Get a Mach port to initiate communication with I/O Kit.
    ioReturnValue = IOMasterPort(bootstrap_port, &masterPort);
}

void disposeMasterPort()
{
    //Free master port if we created one.
    if (masterPort)
    {
        mach_port_deallocate(mach_task_self(), masterPort);
    }
}

/**
 * Enumerate the devices attached to this machine.
 **/
void enumDevices()
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
            showDictionaryElement(properties, CFSTR(kIOHIDTransportKey));
            //MyShowDictionaryElement(properties, CFSTR(kIOHIDVendorKey));
            showDictionaryElement(properties, CFSTR(kIOHIDProductIDKey));
            showDictionaryElement(properties, CFSTR(kIOHIDVersionNumberKey));
            showDictionaryElement(properties, CFSTR(kIOHIDManufacturerKey));
            showDictionaryElement(properties, CFSTR(kIOHIDProductKey));
            showDictionaryElement(properties, CFSTR(kIOHIDSerialNumberKey));
            showDictionaryElement(properties, CFSTR(kIOHIDLocationIDKey));
            showDictionaryElement(properties, CFSTR(kIOHIDPrimaryUsageKey));
            showDictionaryElement(properties, CFSTR(kIOHIDPrimaryUsagePageKey));
            showDictionaryElement(properties, CFSTR(kIOHIDElementKey));
            
            //Release the properties dictionary
            CFRelease(properties);
        }        
    }
    
    IOObjectRelease(hidObjectIterator);    
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
        //showUsageAndPageElement (object);
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
    createMasterPort();
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
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_JNIWrapper_enumDevices
(JNIEnv * env, jobject obj)
{
    enumDevices();
}


