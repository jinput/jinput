/**
 * Copyright (C) 2003 Jeremy Booth (jeremy@newdawnsoftware.com)
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this 
 * list of conditions and the following disclaimer. Redistributions in binary 
 * form must reproduce the above copyright notice, this list of conditions and 
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. 
 * The name of the author may not be used to endorse or promote products derived
 * from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO 
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
 */

#include <stdio.h>
#include <stdlib.h>
#include <malloc.h>
#include <string.h>

#include "net_java_games_input_JInputLibrary.h"

#include "Device.h"
#include "EventDevice.h"
#include "JoystickDevice.h"
#include "MixedDevice.h"
#include "eventInterface.h"
#include "joystickInterface.h"
#include "logger.h"

Device **jinputDeviceList;
int jinputNumDevices;

/*
 * Class:     net_java_games_input_JInputLibrary
 * Method:    nativeInit
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_net_java_games_input_JInputLibrary_nativeInit
  (JNIEnv *, jclass) {

  LOG_TRACE("Initing event interface\n");
  if(evInit()!=0) {
    fprintf(stderr, "Could not find any working event devices\n");
//    return -1;
  }
  LOG_TRACE("Initing joystick interface\n");
  if(jsInit()!=0) {
    fprintf(stderr, "Could not find any working joystick devices\n");
//    return -1;
  }

  LOG_TRACE("Getting the number of event devices\n");
  int numEventDevices = evGetNumberDevices();
  EventDevice *eventDevices[numEventDevices];
  
  LOG_TRACE("Getting %d event devices\n", numEventDevices);
  evGetDevices((Device **)eventDevices);

  LOG_TRACE("Getting the number of joystick devices\n");
  int numJoysticks = jsGetNumberDevices();
  JoystickDevice *jsDevices[numJoysticks];
  LOG_TRACE("Getting %d joystick devices\n", numJoysticks);
  jsGetDevices((Device **)jsDevices);

  
  int i;
  int j;
  int joystickPtr = 0;
  jinputDeviceList = (Device **)malloc((numEventDevices + numJoysticks) * sizeof(Device *));
  for(i=0;i<numEventDevices;i++) {
    EventDevice *eventDevice = eventDevices[i];
    int deviceCountCache = jinputNumDevices;

    for(j=joystickPtr;j<numJoysticks;j++) {
      JoystickDevice *jsDevice = jsDevices[j];
      LOG_TRACE("Getting device information for event device %d and joystick %d\n", i, j);
      if((jsDevice->getNumberButtons() == eventDevice->getNumberButtons()) && (jsDevice->getNumberAbsAxes() == (eventDevice->getNumberAbsAxes() + eventDevice->getNumberRelAxes()))) {
        const char *jsName = jsDevice->getName();
        const char *eventDeviceName = eventDevice->getName();

        if(strcmp(jsName, eventDeviceName) == 0) {
          // The current event device is the curre joystick device too
          LOG_TRACE("Creating a mixed device with id %d, combining event device %d and joystick device %d\n", jinputNumDevices, i, j);
          jinputDeviceList[jinputNumDevices] = new MixedDevice(jsDevice, eventDevice);
          jsDevices[j] = NULL;
          jinputNumDevices++;
          joystickPtr = j;
          j = numJoysticks;
        }
      }
      /*if(jinputNumDevices == deviceCountCache) {
        fprintf(stderr, "event device \"%s\" doesn't match js \"%s\"\n", eventDevice->getName(), jsDevice->getName());
        fprintf(stderr, "event device has %d rel axes, %d abs axis and %d buttons\n", eventDevice->getNumberRelAxes(), eventDevice->getNumberAbsAxes(), eventDevice->getNumberButtons());
        fprintf(stderr, "js device has %d axes and %d buttons\n", jsDevice->getNumberAbsAxes(), jsDevice->getNumberButtons());
      } else {
        fprintf(stderr, "event device %s did match js %s\n", eventDevice->getName(), jsDevice->getName());
      }*/ 
  
    }

    if(jinputNumDevices == deviceCountCache) {
      jinputDeviceList[jinputNumDevices] = eventDevice;
      jinputNumDevices++;
    }
  }

  for(i=0;i<numJoysticks;i++) {
  	if(jsDevices[i]!=NULL) {
  		LOG_TRACE("Copying joystick device %d to jinput device list %d\n", i, jinputNumDevices);
  		jinputDeviceList[jinputNumDevices] = jsDevices[i];
  		jinputNumDevices++;
  	}
  }

  return(0);

}

/*
 * Class:     net_java_games_input_JInputLibrary
 * Method:    getDeviceName
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_net_java_games_input_JInputLibrary_getDeviceName
  (JNIEnv *env, jclass, jint deviceID) {
  
  LOG_TRACE("Gettign device name for jinput device %d.\n", deviceID);
  LOG_TRACE("jinput device %d is %d\n", deviceID, jinputDeviceList[deviceID]);
  LOG_TRACE("Gettign device name for jinput device %d, (%s)\n", deviceID, jinputDeviceList[deviceID]->getName());
  return env->NewStringUTF(jinputDeviceList[deviceID]->getName());
}

/*
 * Class:     net_java_games_input_JInputLibrary
 * Method:    getNumAbsAxes
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_java_games_input_JInputLibrary_getNumAbsAxes
  (JNIEnv *env, jclass, jint deviceID) {

  LOG_TRACE("Gettign number of absolute axes for jinput device %d (%d)\n", deviceID, jinputDeviceList[deviceID]->getNumberAbsAxes());
  return jinputDeviceList[deviceID]->getNumberAbsAxes();
}

/*
 * Class:     net_java_games_input_JInputLibrary
 * Method:    getNumRelAxes
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_java_games_input_JInputLibrary_getNumRelAxes
  (JNIEnv *env, jclass, jint deviceID) {
  
  LOG_TRACE("Gettign number of relative axes for jinput device %d (%d)\n", deviceID, jinputDeviceList[deviceID]->getNumberRelAxes());
  return jinputDeviceList[deviceID]->getNumberRelAxes();
}

/*
 * Class:     net_java_games_input_JInputLibrary
 * Method:    getNumButtons
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_java_games_input_JInputLibrary_getNumButtons
  (JNIEnv *, jclass, jint deviceID) {

  LOG_TRACE("Gettign number of buttons for jinput device %d (%d)\n", deviceID, jinputDeviceList[deviceID]->getNumberButtons());
  return jinputDeviceList[deviceID]->getNumberButtons();
}

/*
 * Class:     net_java_games_input_JInputLibrary
 * Method:    getNumberOfDevices
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_net_java_games_input_JInputLibrary_getNumberOfDevices
  (JNIEnv *, jclass) {

  return jinputNumDevices;
}

/*
 * Class:     net_java_games_input_JInputLibrary
 * Method:    getSupportedAbsAxes
 * Signature: (I[I)V
 */
JNIEXPORT void JNICALL Java_net_java_games_input_JInputLibrary_getSupportedAbsAxes
  (JNIEnv *env, jclass, jint deviceID, jintArray axesData) {

  jint *axisReturns = env->GetIntArrayElements(axesData, 0);
  
  LOG_TRACE("Getting suported absolute axes for jinput device %d\n", deviceID);
  jinputDeviceList[deviceID]->getSupportedAbsAxes(axisReturns);

  env->ReleaseIntArrayElements(axesData, axisReturns, 0);
}

/*
 * Class:     net_java_games_input_JInputLibrary
 * Method:    getSupportedRelAxes
 * Signature: (I[I)V
 */
JNIEXPORT void JNICALL Java_net_java_games_input_JInputLibrary_getSupportedRelAxes
  (JNIEnv *env, jclass, jint deviceID, jintArray axesData) {

  jint *axisReturns = env->GetIntArrayElements(axesData, 0);
  
  LOG_TRACE("Getting suported relative axes for jinput device %d\n", deviceID);
  jinputDeviceList[deviceID]->getSupportedRelAxes(axisReturns);

  env->ReleaseIntArrayElements(axesData, axisReturns, 0);
}

/*
 * Class:     net_java_games_input_JInputLibrary
 * Method:    getSupportedButtons
 * Signature: (I[I)V
 */
JNIEXPORT void JNICALL Java_net_java_games_input_JInputLibrary_getSupportedButtons
  (JNIEnv *env, jclass, jint deviceID, jintArray buttonData) {

  jint *buttonDataElements = env->GetIntArrayElements(buttonData, 0);
  
  LOG_TRACE("Getting supported buttons for jinput device %d\n", deviceID);
  jinputDeviceList[deviceID]->getSupportedButtons(buttonDataElements);

  env->ReleaseIntArrayElements(buttonData, buttonDataElements, 0);
}

/*
 * Class:     net_java_games_input_JInputLibrary
 * Method:    poll
 * Signature: (I[I[I[I)I
 */
JNIEXPORT jint JNICALL Java_net_java_games_input_JInputLibrary_poll
  (JNIEnv *env, jclass, jint deviceID, jintArray buttons, jintArray relAxes, jintArray absAxes) {

  jint *buttonElements = env->GetIntArrayElements(buttons, 0);
  jint *relAxesElements = env->GetIntArrayElements(relAxes, 0);
  jint *absAxesElements = env->GetIntArrayElements(absAxes, 0);

  LOG_POLL_TRACE("Polling jinput device %d\n", deviceID);
  int retval = jinputDeviceList[deviceID]->poll();
  LOG_POLL_TRACE("Getting polled data for device %d\n", deviceID);
  jinputDeviceList[deviceID]->getPolledData(relAxesElements, absAxesElements, buttonElements);

  env->ReleaseIntArrayElements(buttons, buttonElements, 0);
  env->ReleaseIntArrayElements(relAxes, relAxesElements, 0);
  env->ReleaseIntArrayElements(absAxes, absAxesElements, 0);

  return retval;
}

/*
 * Class:     net_java_games_input_JInputLibrary
 * Method:    getAbsAxisFuzz
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_net_java_games_input_JInputLibrary_getAbsAxisFuzz
  (JNIEnv *, jclass, jint deviceID, jint axisID) {

  LOG_TRACE("Getting fuzz data for axis %d on device %d\n", axisID, deviceID);
  return jinputDeviceList[deviceID]->getAbsAxisFuzz(axisID);
}

/*
 * Class:     net_java_games_input_JInputLibrary
 * Method:    getAbsAxisMaximum
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_net_java_games_input_JInputLibrary_getAbsAxisMaximum
  (JNIEnv *, jclass, jint deviceID, jint axisID) {

  LOG_TRACE("Getting absolute axes maximum value data for axis %d on device %d\n", axisID, deviceID);
  return jinputDeviceList[deviceID]->getAbsAxisMaximum(axisID);
}


/*
 * Class:     net_java_games_input_JInputLibrary
 * Method:    getAbsAxisMinimum
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_net_java_games_input_JInputLibrary_getAbsAxisMinimum
  (JNIEnv *, jclass, jint deviceID, jint axisID) {

  LOG_TRACE("Getting absolute axes minimum value data for axis %d on device %d\n", axisID, deviceID);
  return jinputDeviceList[deviceID]->getAbsAxisMinimum(axisID);
}

/*
 * Class:     net_java_games_input_JInputLibrary
 * Method:    getNativePortType
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_java_games_input_JInputLibrary_getNativePortType
  (JNIEnv *, jclass, jint deviceID) {

  LOG_TRACE("Getting bus type for device %d\n", deviceID);
  return jinputDeviceList[deviceID]->getBusType();
}

/*
 * Class:     net_java_games_input_JInputLibrary
 * Method:    getFFEnabled
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_net_java_games_input_JInputLibrary_getFFEnabled
  (JNIEnv *, jclass, jint deviceID) {
  	
  LOG_TRACE("Getting FFEnabled status for device %d\n", deviceID);
  if(jinputDeviceList[deviceID]->getFFEnabled()) {
  	//LOG_TRACE("jinput lib thinks device %d is ff enabled\n", deviceID);
  	return JNI_TRUE;
  }
  	//LOG_TRACE("jinput lib thinks device %d is ff disabled\n", deviceID);
  return JNI_FALSE;
}

/*
 * Class:     net_java_games_input_JInputLibrary
 * Method:    nativeRumble
 * Signature: (IF)V
 */
JNIEXPORT void JNICALL Java_net_java_games_input_JInputLibrary_nativeRumble
  (JNIEnv *, jclass, jint deviceID, jfloat force) {
  	jinputDeviceList[deviceID]->rumble(force);
}

/*
 * Class:     net_java_games_input_JInputLibrary
 * Method:    nativeCleanup
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_java_games_input_JInputLibrary_nativeCleanup
  (JNIEnv *, jclass, jint deviceID) {
  	if(jinputDeviceList[deviceID]!=0) {
	  	jinputDeviceList[deviceID]->cleanup();
	  	free(jinputDeviceList);
	  	jinputDeviceList[deviceID]=0;
  	}
}
