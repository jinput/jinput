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
#include "net_java_games_input_OSXEnvironmentPlugin.h"

Boolean init( JNIEnv * env );
void createMasterPort();
void disposeMasterPort();


void createHIDDevice(io_object_t hidDevice, IOHIDDeviceInterface ***hidDeviceInterface);
IOReturn openDevice(IOHIDDeviceInterface ***hidDeviceInterface);
IOReturn closeDevice(IOHIDDeviceInterface ***hidDeviceInterface);


Boolean showDictionaryElement (CFDictionaryRef dictionary, CFStringRef key);
void showProperty(const void * key, const void * value);
void displayCFProperty(CFStringRef object, CFTypeRef value);
void CFObjectShow( CFTypeRef value );
void CFObjectSend( CFTypeRef value );

jclass              CLASS_JNIWrapper = NULL;
jmethodID           MID_AddController = NULL;
jmethodID           MID_AddControllerElement = NULL;
mach_port_t         masterPort = NULL;
io_iterator_t       hidObjectIterator;

long                elementCookie;
long                elementType;
long                usage;
long                usagePage;
long                min;
long                max;
long                scaledMin;
long                scaledMax;
long                size;
jboolean            isRelative;
jboolean            isWrapping;
jboolean            isNonLinear;


JNIEnv *            lpEnv;
jlong               lpDevice;
jobject             lpObj;
jboolean            completeElement = JNI_FALSE;




Boolean showDictionaryElement (CFDictionaryRef dictionary, CFStringRef key)
{
    CFTypeRef value = CFDictionaryGetValue (dictionary, key);
    if (value)
    {
        const char * c = CFStringGetCStringPtr (key, CFStringGetSystemEncoding ());
        if (c)
        {
            printf ("%s", c);
        }
        else
        {
            CFIndex bufferSize = CFStringGetLength (key) + 1;
            char * buffer = (char *)malloc (bufferSize);
            if (buffer)
            {
                if (CFStringGetCString (key, buffer, bufferSize, CFStringGetSystemEncoding ()))
                    printf ("%s", buffer);
                free(buffer);
            }
        }
        
        printf("=");
        
        CFObjectShow( value );
    
        printf("\n");
    }
    return (value != NULL);
}

static void showCFArray (const void * value, void * parameter)
{
    if (CFGetTypeID (value) != CFDictionaryGetTypeID ())
    {
        return;
    }
    
    CFObjectShow(value);
}

void CFObjectShow( CFTypeRef value )
{
    CFTypeID type = CFGetTypeID(value);
    if (type == CFArrayGetTypeID())
    {
        CFRange range = {0, CFArrayGetCount (value)};
        
        //Show an element array containing one or more element dictionaries
        CFArrayApplyFunction (value, range, showCFArray, 0);
    }
    else if (type == CFBooleanGetTypeID())
    {
        printf(CFBooleanGetValue(value) ? "true" : "false");
    }
    else if (type == CFDictionaryGetTypeID())
    {
        printf("Map\n");


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

////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////

Boolean init(JNIEnv* env)
{
    CLASS_JNIWrapper = (*env)->FindClass(env,"net/java/games/input/OSXEnvironmentPlugin");
    if ( CLASS_JNIWrapper == NULL )
    {
        printf("Class OSXEnvironmentPlugin not found... \n");
        return FALSE;
    }
    
    MID_AddController = (*env)->GetMethodID(env, CLASS_JNIWrapper, "addController", "(JLjava/lang/String;I)V");
    if (MID_AddController == NULL)
    {
        printf("Method addController not found... \n");
        return FALSE;
    }

    MID_AddControllerElement = (*env)->GetMethodID(env, CLASS_JNIWrapper, "addControllerElement", "(JJIIIIIIIIZZZZZ)V");
    if (MID_AddControllerElement == NULL)
    {
        printf("Method addControllerElement not found... \n");
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

void createHIDDevice( io_object_t hidDevice, IOHIDDeviceInterface ***hidDeviceInterface )
{
    io_name_t               className;
    IOCFPlugInInterface     **plugInInterface = NULL;
    HRESULT                 plugInResult = S_OK;
    SInt32                  score = 0;
    IOReturn                ioReturnValue = kIOReturnSuccess;
    
    ioReturnValue = IOObjectGetClass(hidDevice, className);
    if ( ioReturnValue != kIOReturnSuccess )
    {
        printf("Failed to get IOObject class name.");
    }
    
    printf("Found device type [%s]\n", className);
    
    ioReturnValue = IOCreatePlugInInterfaceForService(hidDevice,
                                                      kIOHIDDeviceUserClientTypeID,
                                                      kIOCFPlugInInterfaceID,
                                                      &plugInInterface,
                                                      &score);
    
    if (ioReturnValue == kIOReturnSuccess)
    {
        //Call a method of the intermediate plug-in to create the device 
        //interface
        //
        plugInResult = (*plugInInterface)->QueryInterface(plugInInterface,
                                                          CFUUIDGetUUIDBytes(kIOHIDDeviceInterfaceID),
                                                          (LPVOID) hidDeviceInterface);
        if ( plugInResult != S_OK )
        {
            printf("Couldn't create HID class device interface");
        }
                    
        (*plugInInterface)->Release(plugInInterface);
    }
}

IOReturn openDevice(IOHIDDeviceInterface ***hidDeviceInterface)
{
    IOReturn                ioReturnValue = kIOReturnSuccess;

    //todo, change this to be controlled from the java layer at each device
    //
    ioReturnValue = (**hidDeviceInterface)->open(*hidDeviceInterface, 0 );
    if ( ioReturnValue != kIOReturnSuccess )
    {
        printf("Unable to open device - return [%d]\n", ioReturnValue );
    }
    else
    {
        printf("Successfully opened device \n");
    }
    
    return ioReturnValue;
}

IOReturn closeDevice(IOHIDDeviceInterface ***hidDeviceInterface)
{    
    IOReturn                ioReturnValue = kIOReturnSuccess;    
    
    ioReturnValue = (**hidDeviceInterface)->close(*hidDeviceInterface);
    if ( ioReturnValue != kIOReturnSuccess )
    {
        printf("Unable to close device - return [%d]\n", ioReturnValue );
    }
    else
    {
        printf("Successfully closed device \n");
    }
    
    // release the device interface
    //
    (**hidDeviceInterface)->Release(*hidDeviceInterface);
    
    return ioReturnValue;
}

static void sendCFArray(const void * value, void * parameter)
{
    if (CFGetTypeID (value) != CFDictionaryGetTypeID ())
    {
        return;
    }

    CFObjectSend(value);
}

void CFObjectSend( CFTypeRef value )
{
    CFTypeID type = CFGetTypeID(value);
    if (type == CFArrayGetTypeID())
    {
        CFRange range = {0, CFArrayGetCount (value)};
        
        //Show an element array containing one or more element dictionaries
        CFArrayApplyFunction (value, range, sendCFArray, 0);
    }
    else if (type == CFDictionaryGetTypeID())
    {
//        printf("Sending Map\n");


        CFTypeRef val = CFDictionaryGetValue( value, CFSTR(kIOHIDElementCookieKey) );
        if ( val )
        {
            CFNumberGetValue ( val , kCFNumberLongType, &elementCookie);
            printf("ElementCookie - 0x%lx (%ld) \n", elementCookie, elementCookie);
        }

        val = CFDictionaryGetValue( value, CFSTR(kIOHIDElementTypeKey) );
        if ( val )
        {
            CFNumberGetValue ( val, kCFNumberLongType, &elementType);
            printf("element Type - 0x%lx (%ld) \n", elementType, elementType);
        }

        val = CFDictionaryGetValue( value, CFSTR(kIOHIDElementUsageKey) );
        if ( val )
        {
            CFNumberGetValue ( val, kCFNumberLongType, &usage);
            printf("usage - 0x%lx (%ld) \n", usage, usage);
        }

        val = CFDictionaryGetValue( value, CFSTR(kIOHIDElementUsagePageKey) );
        if ( val )
        {
            CFNumberGetValue ( val, kCFNumberLongType, &usagePage);
            printf("usage page- 0x%lx (%ld) \n", usagePage, usagePage);
        }

        val = CFDictionaryGetValue( value, CFSTR(kIOHIDElementMinKey) );
        if ( val )
        {
            CFNumberGetValue ( val, kCFNumberLongType, &min);
            //printf("min - 0x%lx (%ld) \n", min, min);
        }

        val = CFDictionaryGetValue( value, CFSTR(kIOHIDElementMaxKey) );
        if ( val )
        {
            CFNumberGetValue ( val, kCFNumberLongType, &max);
            //printf("max - 0x%lx (%ld) \n", max, max);
        }
        
        val = CFDictionaryGetValue( value, CFSTR(kIOHIDElementScaledMinKey) );
        if ( val ) 
        {
            CFNumberGetValue ( val, kCFNumberLongType, &scaledMin);
            //printf("scaledMin - 0x%lx (%ld) \n", scaledMin, scaledMin);
        }

        val = CFDictionaryGetValue( value, CFSTR(kIOHIDElementScaledMaxKey) );
        if ( val )
        {
            CFNumberGetValue ( val, kCFNumberLongType, &scaledMax);
            //printf("scaledMax - 0x%lx (%ld) \n", scaledMax, scaledMax);
        }

        val = CFDictionaryGetValue( value, CFSTR(kIOHIDElementSizeKey) );
        if ( val )
        {
            CFNumberGetValue ( val, kCFNumberLongType, &size);
            //printf("Size - 0x%lx (%ld) \n", size, size);
        }

        jboolean isRelative = JNI_FALSE;
        val = CFDictionaryGetValue( value, CFSTR(kIOHIDElementIsRelativeKey) );
        if ( val )
        {
            isRelative = (CFBooleanGetValue(val) ? JNI_TRUE : JNI_FALSE);
        }


        jboolean isWrapping = JNI_FALSE;
        val = CFDictionaryGetValue( value, CFSTR(kIOHIDElementIsWrappingKey) );
        if ( val )
        {
            isWrapping = (CFBooleanGetValue(val) ? JNI_TRUE : JNI_FALSE);
        }


        jboolean isNonLinear = JNI_FALSE;
        val = CFDictionaryGetValue( value, CFSTR(kIOHIDElementIsNonLinearKey) );
        if ( val )
        {
            isNonLinear = (CFBooleanGetValue(val) ? JNI_TRUE : JNI_FALSE);
        }


        jboolean hasPreferredState = JNI_FALSE;
#ifdef kIOHIDElementHasPreferredStateKey
        val = CFDictionaryGetValue( value, CFSTR(kIOHIDElementHasPreferredStateKey) );
        if ( val )
        {
            hasPreferredState = (CFBooleanGetValue(val) ? JNI_TRUE : JNI_FALSE);
        }
#else
        val = CFDictionaryGetValue( value, CFSTR(kIOHIDElementHasPreferedStateKey) );
        if ( val )
        {
            hasPreferredState = (CFBooleanGetValue(val) ? JNI_TRUE : JNI_FALSE);
        }
#endif
        
        jboolean hasNullState = JNI_FALSE;
        val = CFDictionaryGetValue( value, CFSTR(kIOHIDElementHasNullStateKey) );
        if ( val )
        {
            hasNullState = (CFBooleanGetValue(val) ? JNI_TRUE : JNI_FALSE);
        }

        (*lpEnv)->CallVoidMethod(lpEnv, lpObj, MID_AddControllerElement,
                                (jlong)(long)lpDevice,
                                (jlong)(long)elementCookie,
                                (jint)(long)elementType,
                                (jint)(long)usage,
                                (jint)(long)usagePage,
                                (jint)(long)min,
                                (jint)(long)max,
                                (jint)(long)scaledMin,
                                (jint)(long)scaledMax,
                                (jint)(long)size,
                                (jboolean)isRelative,
                                (jboolean)isWrapping,
                                (jboolean)isNonLinear,
                                (jboolean)hasPreferredState,
                                (jboolean)hasNullState);

//      printf("End of element definition \n");


        
        CFTypeRef object = CFDictionaryGetValue (value, CFSTR(kIOHIDElementKey));
        if (object)
        {
            CFObjectSend( object );
        }

        printf("\n\n\n");
    }
}

void addControllerElements( CFMutableDictionaryRef dictionary, CFStringRef key )
{
    printf("Adding controller elements\n");

    CFTypeRef value = CFDictionaryGetValue (dictionary, key);
    if (value)
    {
        CFRange range = {0, CFArrayGetCount (value)};
        CFArrayApplyFunction (value, range, sendCFArray, 0);
    }
}

/*
 * Class:     net_java_games_input_OSXEnvironmentPlugin
 * Method:    hidCreate
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_net_java_games_input_OSXEnvironmentPlugin_hidCreate
  (JNIEnv * env, jobject obj)
{
    if ( init( env ) )
    {
        createMasterPort();
    }
}
/*
 * Class:     net_java_games_input_OSXEnvironmentPlugin
 * Method:    hidDispose
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_net_java_games_input_OSXEnvironmentPlugin_hidDispose
  (JNIEnv * env, jobject obj)
{
    disposeMasterPort();
}


/*
 * Class:     net_java_games_input_OSXEnvironmentPlugin
 * Method:    enumDevices
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_net_java_games_input_OSXEnvironmentPlugin_enumDevices
  (JNIEnv * env, jobject obj)
{

    lpEnv = env;
    lpObj = obj;

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
    IOHIDDeviceInterface    **hidDeviceInterface = NULL;
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
            //printf("ProductKey: "); showDictionaryElement(properties, CFSTR(kIOHIDProductKey));
            //printf("SerialNumber: "); showDictionaryElement(properties, CFSTR(kIOHIDSerialNumberKey));
            //showDictionaryElement(properties, CFSTR(kIOHIDLocationIDKey));
            //printf("PrimaryUsage: "); showDictionaryElement(properties, CFSTR(kIOHIDPrimaryUsageKey));
            //showDictionaryElement(properties, CFSTR(kIOHIDPrimaryUsagePageKey));
            //showDictionaryElement(properties, CFSTR(kIOHIDElementKey));



            // get the product name
            //
            CFTypeRef productName = CFDictionaryGetValue (properties, CFSTR(kIOHIDProductKey)); 

            // get the usage for this product
            //
            long usage;
            CFNumberGetValue ( CFDictionaryGetValue( properties, CFSTR(kIOHIDPrimaryUsageKey) ), kCFNumberLongType, &usage);
            

            createHIDDevice( hidDevice, &hidDeviceInterface );
            
            IOObjectRelease( hidDevice );
            
            if ( hidDeviceInterface != NULL )
            {                
                (*env)->CallVoidMethod(env, obj, MID_AddController, 
                                       (jlong)(long)hidDeviceInterface,
                                       (*env)->NewStringUTF( env, CFStringGetCStringPtr( productName, CFStringGetSystemEncoding()) ),
                                       (jint)usage );
                lpEnv = env;
                lpDevice = (jlong)(long)hidDeviceInterface;

                addControllerElements( properties, CFSTR(kIOHIDElementKey) );


            }            
                    
            //Release the properties dictionary
            CFRelease(properties);
        }        

    }

    IOObjectRelease(hidObjectIterator);    
}

/*
 * Class:     net_java_games_input_OSXEnvironmentPlugin
 * Method:    openDevice
 * Signature: (JI)J
 */
JNIEXPORT jlong JNICALL Java_net_java_games_input_OSXEnvironmentPlugin_openDevice
  (JNIEnv * env, jobject obj, jlong lpDevice, jint queueDepth)
{
    IOHIDDeviceInterface    **hidDeviceInterface = NULL;
    hidDeviceInterface = (IOHIDDeviceInterface **) (long)lpDevice;
    openDevice( &hidDeviceInterface  );


    IOHIDQueueInterface  **queue = NULL;
    queue = (*hidDeviceInterface)->allocQueue(hidDeviceInterface);

    if (queue)
    {
        // create a queue and specify how deep they want the input queue to be
        //
        (*queue)->create( queue, 0, (int)queueDepth );
        printf("InputQueue created %lx with depth %d \n", queue, (int)queueDepth );

        // todo - add the buttons/keys we want to receive from the queue


        // start the input queue
        //
        (*queue)->start( queue );
    }
    else
    {
        printf("Unable to create queue for device! \n");
    }

    return (jlong)(long)queue;
    
}

/*
 * Class:     net_java_games_input_OSXEnvironmentPlugin
 * Method:    closeDevice
 * Signature: (JJ)V
 */
JNIEXPORT void JNICALL Java_net_java_games_input_OSXEnvironmentPlugin_closeDevice
  (JNIEnv * env, jobject obj, jlong lpDevice, jlong lpQueue)
{
    IOHIDDeviceInterface    **hidDeviceInterface = NULL;
    hidDeviceInterface = (IOHIDDeviceInterface **) (long)lpDevice;

    IOHIDQueueInterface  **queue = NULL;
    queue = (IOHIDQueueInterface **)(long)lpQueue;
    
    // stop the queue
    //
    (*queue)->stop(queue);

    // dispose of the queue
    //
    (*queue)->dispose(queue);

    // release the queue
    //
    (*queue)->Release(queue);

    // close the input device
    //
    closeDevice( &hidDeviceInterface  );
}

/*
 * Class:     net_java_games_input_OSXEnvironmentPlugin
 * Method:    pollDevice
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_net_java_games_input_OSXEnvironmentPlugin_pollDevice
  (JNIEnv * env, jobject obj, jlong lpQueue)
{
    IOHIDEventStruct event;

    IOHIDQueueInterface  **queue = NULL;
    queue = (IOHIDQueueInterface **)(long)lpQueue;

    AbsoluteTime zeroTime = {0,0};

    HRESULT result = (*queue)->getNextEvent(queue, &event, zeroTime, 0);
    if ( result )
    {
        printf("Queue getNextEvent result: %lx\n", result );
    }
    else
    {
        printf("Queue event[%lx] %ld\n", (unsigned long) event.elementCookie, event.value );
    }
    
    
}



