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
#include "MixedDevice.h"
#include <stdio.h>
#include <linux/input.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>
#include <malloc.h>
#include <errno.h>

MixedDevice::MixedDevice(JoystickDevice *jsDevice, EventDevice *evDevice) {

  joystickDevice = jsDevice;
  eventDevice = evDevice;
}

int MixedDevice::getNumberRelAxes(){
  eventDevice->getNumberRelAxes();
}

int MixedDevice::getNumberAbsAxes(){
  return eventDevice->getNumberAbsAxes();
}

int MixedDevice::getNumberButtons(){
  return eventDevice->getNumberButtons();
}

const char *MixedDevice::getName(){ 
  return eventDevice->getName();
}

int MixedDevice::getBusType(){
  return eventDevice->getBusType();
}

int MixedDevice::getVendorID(){
  return eventDevice->getVendorID();
}

int MixedDevice::getProductID(){
  return eventDevice->getProductID();
}

int MixedDevice::getVersion(){
  return eventDevice->getVersion();
}

void MixedDevice::getSupportedRelAxes(int supportedAxis[]){
  return eventDevice->getSupportedRelAxes(supportedAxis);
}

void MixedDevice::getSupportedAbsAxes(int supportedAxis[]){
  return eventDevice->getSupportedAbsAxes(supportedAxis);
}

void MixedDevice::getSupportedButtons(int supportedButtons[]){
  return eventDevice->getSupportedButtons(supportedButtons);
}

/**
 * A return value of -1 means error, 0 means ok, but no change
 * a return of >0 means the data for this device has changed
 */
int MixedDevice::poll(){
  eventDevice->poll();
  return joystickDevice->poll();
}

void MixedDevice::getPolledData(int relAxesData[], int absAxesData[], int buttonData[]){
  int i;

  joystickDevice->getPolledData(new int[joystickDevice->getNumberRelAxes()], absAxesData, new int[joystickDevice->getNumberButtons()]);
  eventDevice->getPolledData(relAxesData, new int[eventDevice->getNumberAbsAxes()], buttonData);

}

int MixedDevice::getAbsAxisMinimum(int axisNumber) {
  return joystickDevice->getAbsAxisMinimum(axisNumber);
}

int MixedDevice::getAbsAxisMaximum(int axisNumber) {
  return joystickDevice->getAbsAxisMaximum(axisNumber);
}

int MixedDevice::getAbsAxisFuzz(int axisNumber) {
  return joystickDevice->getAbsAxisFuzz(axisNumber);
}

bool MixedDevice::getFFEnabled() {
	return eventDevice->getFFEnabled();
}

void MixedDevice::rumble(float force) {
	eventDevice->rumble(force);
}

void MixedDevice::cleanup() {
	if(joystickDevice!=0 && eventDevice!=0) {
		joystickDevice->cleanup();
		eventDevice->cleanup();
		free(joystickDevice);
		free(eventDevice);
		joystickDevice=0;
		eventDevice=0;
	}
}
