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

#include "eventInterfaceTypes.h"
#include "JoystickDevice.h"
#include <stdio.h>
#include <linux/input.h>
#include <linux/joystick.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>
#include <malloc.h>
#include <errno.h>

#include "logger.h"

JoystickDevice::JoystickDevice(char *deviceFileName) {
  char tempName[Device::MAX_NAME_LENGTH-1] = "Unknown";
  int i;

  LOG_TRACE("Trying to open %s\n", deviceFileName);
  fd = open(deviceFileName, O_RDWR | O_NONBLOCK);
  /*if(fd<0) {
    char errorMessage[512];
    sprintf(errorMessage, "Error opening device %s\n", deviceFileName);
    perror(errorMessage);
  }*/

  if(fd>0){
    LOG_TRACE("Opened %s, trying to get device name\n", deviceFileName);
    if(ioctl(fd, JSIOCGNAME(sizeof(tempName)), tempName) < 0) {
      LOG_TRACE("Failed to get device name for %s\n", deviceFileName);
      char errorMessage[512];
      sprintf(errorMessage, "Error reading device %s\n", deviceFileName);
      perror(errorMessage);
    }

    int namelength=strlen(tempName);
    name = (char *)malloc(namelength+1);
    strncpy(name,tempName, namelength+1);

    char tempNumButtons;
    char tempNumAxes;
    LOG_TRACE("Getting button and axes information for %s\n", deviceFileName);
    ioctl (fd, JSIOCGBUTTONS, &tempNumButtons);
    ioctl (fd, JSIOCGAXES, &tempNumAxes);

    numButtons = tempNumButtons;
    numAbsAxes = tempNumAxes;

    //fprintf(stderr, "Got joystick %s with %d buttons and %d axes\n", tempName, numButtons, numAbsAxes);

    //buttonData = (uint8_t *)malloc(numButtons * sizeof(uint8_t));
    buttonData = new uint8_t[numButtons];
    //absAxesData = (int *)malloc(numAbsAxes * sizeof(int));
    absAxesData = new int[numAbsAxes];

    LOG_TRACE("Initialisation of %s completed\n", deviceFileName);
    inited = 1;
  } else {
    LOG_TRACE("Failed to open device %s\n", deviceFileName);
    inited = 0;
  }
}

int JoystickDevice::isValidDevice() {
  return inited; 
}

int JoystickDevice::getNumberRelAxes(){
  if(inited!=1) return -1;
  return 0;
}

int JoystickDevice::getNumberAbsAxes(){
  if(inited!=1) return -1;
  return numAbsAxes;
}

int JoystickDevice::getNumberButtons(){
  if(inited!=1) return -1;
  return numButtons;
}

const char *JoystickDevice::getName(){ 
  return name;
}

int JoystickDevice::getBusType(){
  if(inited!=1) return -1;
  return 0;
}

int JoystickDevice::getVendorID(){
  if(inited!=1) return -1;
  return 0;
}

int JoystickDevice::getProductID(){
  if(inited!=1) return -1;
  return 0;
}

int JoystickDevice::getVersion(){
  if(inited!=1) return -1;
  return 0;
}

void JoystickDevice::getSupportedRelAxes(int supportedAxis[]){
}

void JoystickDevice::getSupportedAbsAxes(int supportedAxis[]){
}

void JoystickDevice::getSupportedButtons(int supportedButtons[]){
}

/**
 * A return value of -1 means error, 0 means ok, but no change
 * a return of >0 means the data for this device has changed
 */
int JoystickDevice::poll(){
  struct js_event event;
  int numEvents = 0;
  while(read(fd, &event, sizeof event) > 0) {
    numEvents++;
    event.type &= ~JS_EVENT_INIT;
    if(event.type == JS_EVENT_BUTTON) {
      buttonData[event.number] = event.value;
    } else if(event.type == JS_EVENT_AXIS) {
      absAxesData[event.number] = event.value;
    }

  }
  // EAGAIN is returned when the queue is empty
  if(errno != EAGAIN) {
    printf("Something went wrong getting an event\n");
  }

  return numEvents;
}

void JoystickDevice::getPolledData(int relAxesData[], int absAxesData[], int buttonData[]){
  int i;

  if(inited!=1) return;
  for(i=0;i<numAbsAxes;i++) {
    (absAxesData)[i] = this->absAxesData[i];
  }
  for(i=0;i<numButtons;i++) {
    (buttonData)[i] = this->buttonData[i];
  }
}

int JoystickDevice::getAbsAxisMinimum(int axisNumber) {
  return -32767;
}

int JoystickDevice::getAbsAxisMaximum(int axisNumber) {
  return 32767;
}

int JoystickDevice::getAbsAxisFuzz(int axisNumber) {
  return 0;
}
