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
#include "EventDevice.h"
#include <stdio.h>
#include <linux/input.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>
#include <malloc.h>
#include <errno.h>

#include "logger.h"

EventDevice::EventDevice(char *deviceFileName) {
  char tempName[Device::MAX_NAME_LENGTH-1] = "Unknown";
  int i;

  fd = open(deviceFileName, O_RDWR | O_NONBLOCK);
  if(fd<0) {
  	  
		char errorMessage[512];
		sprintf(errorMessage, "Error opening device %s read/write, Force Feedback disabled for this device\n", deviceFileName);
		perror(errorMessage);
		
	  fd = open(deviceFileName, O_RDONLY | O_NONBLOCK);
	  if(fd<0) {
	    /*char errorMessage[512];
	    sprintf(errorMessage, "Error opening device %s\n", deviceFileName);
	    perror(errorMessage);*/
		inited = 0;
		return;
	  }
  } else {
      if(ioctl(fd, EVIOCGBIT(EV_FF, sizeof(ff_bitmask)), ff_bitmask) < 0) {
	    char errorMessage[512];
	    sprintf(errorMessage, "Error reading device %s\n", deviceFileName);
	    perror(errorMessage);
	  }
	  if(getBit(FF_RUMBLE, ff_bitmask)==1) {
	  	ffSupported = 1;
	  	//LOG_TRACE("Force feedback supported for %s\n", deviceFileName);
		  int n_effects = 0;
		  if (ioctl(fd, EVIOCGEFFECTS, &n_effects) == -1) {
		  	char errorMessage[512];
		  	sprintf(errorMessage, "Failed to get number of effects for device %s\n", deviceFileName);
	        perror(errorMessage);
	      }
	      LOG_TRACE("Device %s supports %d simultanious effects\n", deviceFileName, n_effects);
	      effect_playing = false;
	  } else {
	  	ffSupported = 0;
	  	LOG_TRACE("Force feedback not supported for %s %d\n", deviceFileName, getBit(FF_RUMBLE, ff_bitmask));
	  }
  }

  if(ioctl(fd, EVIOCGNAME(sizeof(tempName)), tempName) < 0) {
    char errorMessage[512];
    sprintf(errorMessage, "Error reading device %s\n", deviceFileName);
    perror(errorMessage);
  }

  int namelength=strlen(tempName);
  name = (char *)malloc(namelength+1);
  strncpy(name,tempName, namelength+1);

  LOG_TRACE("Device name for device file %s is %s\n", deviceFileName, name);

  uint8_t evtype_bitmask[EV_MAX/8 + 1];
  memset(evtype_bitmask, 0, sizeof(evtype_bitmask));

  if(ioctl(fd, EVIOCGBIT(0, EV_MAX), evtype_bitmask) < 0) {
    char errorMessage[512];
    sprintf(errorMessage, "Error reading device %s\n", deviceFileName);
    perror(errorMessage);
  }

  struct input_devinfo deviceInfo;
  if(ioctl(fd, EVIOCGID, &deviceInfo) < 0) {
    char errorMessage[512];
    sprintf(errorMessage, "Error reading device %s\n", deviceFileName);
    perror(errorMessage);
  }
  
  bustype = deviceInfo.bustype;
  vendor = deviceInfo.vendor;
  product = deviceInfo.product;
  version = deviceInfo.version;

  numButtons = -1;
  numAbsAxes = -1;
  numRelAxes = -1;

  if(!(getBit(EV_KEY, evtype_bitmask))) {
    numButtons = 0;
  }
  if(!(getBit(EV_REL, evtype_bitmask))) {
    numRelAxes = 0;
  }

  if(!(getBit(EV_ABS, evtype_bitmask))) {
    numAbsAxes = 0;
  }

  if(!getBit(EV_FF, evtype_bitmask)) {
    ffSupported = 0;
  }

  if(numButtons < 0) {
    // This device supports keys, deal with it.
    if(ioctl(fd, EVIOCGBIT(EV_KEY, sizeof(key_bitmask)), key_bitmask) < 0) {
      char errorMessage[512];
      sprintf(errorMessage, "Error reading device %s\n", deviceFileName);
      perror(errorMessage);
    }
    for(i=0;i<KEY_MAX;i++) {
      buttonLookup[i]=-1;
    }
    short tempSupportedButtons[KEY_MAX];
    numButtons = 0;
    for(i=0;i<KEY_MAX;i++) {
      if(getBit(i,key_bitmask)) {
        tempSupportedButtons[numButtons] = i;
        numButtons++;
      }
    }
    supportedButtons = (short *)malloc(numButtons * sizeof(short));
    buttonData = (uint8_t *)malloc(numButtons * sizeof(uint8_t));
    for(i=0;i<numButtons;i++) {
      buttonData[i] = 0;
      supportedButtons[i] = tempSupportedButtons[i];
      buttonLookup[supportedButtons[i]] = i;
    }
  }
  
  if(numRelAxes < 0) {
    // This device supports axes, deal with it.
    if(ioctl(fd, EVIOCGBIT(EV_REL, sizeof(rel_bitmask)), rel_bitmask) < 0) {
      char errorMessage[512];
      sprintf(errorMessage, "Error reading device %s\n", deviceFileName);
      perror(errorMessage);
    }
    for(i=0;i<REL_MAX;i++) {
      relAxisLookup[i]=-1;
    }
    short tempSupportedAxes[REL_MAX];
    numRelAxes=0;
    for(i=0;i<REL_MAX;i++) {
      if(getBit(i,rel_bitmask)) {
        tempSupportedAxes[numRelAxes] = i;
        numRelAxes++;
      }
    }
    relAxesData = (int *)malloc(numRelAxes * sizeof(int));
    supportedRelAxes = (short *)malloc(numRelAxes * sizeof(short));
    for(i=0;i<numRelAxes;i++) {
      relAxesData[i]=0;
      supportedRelAxes[i] = tempSupportedAxes[i];
      relAxisLookup[supportedRelAxes[i]] = i;
    }
  }


  if(numAbsAxes < 0) {
    if(ioctl(fd, EVIOCGBIT(EV_ABS, sizeof(abs_bitmask)), abs_bitmask) < 0) {
      char errorMessage[512];
      sprintf(errorMessage, "Error reading device %s\n", deviceFileName);
      perror(errorMessage);
    }
    for(i=0;i<ABS_MAX;i++) {
      absAxisLookup[i] = -1;
    }
    short tempSupportedAxes[ABS_MAX];
    numAbsAxes=0;
    for(i=0;i<ABS_MAX;i++) {
      if(getBit(i,abs_bitmask)) {
        tempSupportedAxes[numAbsAxes] = i;
        numAbsAxes++;
      }
    }

    absAxesData = (int *)malloc(numAbsAxes * sizeof(int));
    supportedAbsAxes = (short *)malloc(numAbsAxes * sizeof(short));
    for(i=0;i<numAbsAxes;i++) {
      supportedAbsAxes[i] = tempSupportedAxes[i];
      absAxisLookup[supportedAbsAxes[i]] = i;
    }

    abs_features = (struct input_absinfo *)malloc(numAbsAxes * sizeof(struct input_absinfo));
    for(i=0;i<numAbsAxes;i++) {
      if(ioctl(fd, EVIOCGABS(supportedAbsAxes[i]), &(abs_features[i]))) {
        char errorMessage[512];
        sprintf(errorMessage, "Error reading device %s\n", deviceFileName);
        perror(errorMessage);
      }
      absAxesData[i] = abs_features[i].value;
    }
  }

  inited = 1;
}

int EventDevice::isValidDevice() {
  return inited; 
}

int EventDevice::getNumberRelAxes(){
  if(inited!=1) return -1;
  return numRelAxes;
}

int EventDevice::getNumberAbsAxes(){
  if(inited!=1) return -1;
  return numAbsAxes;
}

int EventDevice::getNumberButtons(){
  if(inited!=1) return -1;
  return numButtons;
}

const char *EventDevice::getName(){ 
  LOG_TRACE("EventDevice::getName()\n");
  return name;
}

int EventDevice::getBusType(){
  if(inited!=1) return -1;
  return bustype;
}

int EventDevice::getVendorID(){
  if(inited!=1) return -1;
  return vendor;
}

int EventDevice::getProductID(){
  if(inited!=1) return -1;
  return product;
}

int EventDevice::getVersion(){
  if(inited!=1) return -1;
  return version;
}

void EventDevice::getSupportedRelAxes(int supportedAxis[]){
  int i;

  if(inited!=1) return;
  for(i=0;i<numRelAxes; i++) {
    (supportedAxis)[i] = supportedRelAxes[i];
  }
}

void EventDevice::getSupportedAbsAxes(int supportedAxis[]){
  int i;

  if(inited!=1) return;
  for(i=0;i<numAbsAxes; i++) {
    (supportedAxis)[i] = supportedAbsAxes[i];
  }
}

void EventDevice::getSupportedButtons(int supportedButtons[]){
  int i;

  if(inited!=1) return;
  for(i=0;i<numButtons; i++) {
    (supportedButtons)[i] = this->supportedButtons[i];
  }
}

/**
 * A return value of -1 means error, 0 means ok, but no change
 * a return of >0 means the data for this device has changed
 */
int EventDevice::poll(){
  size_t read_bytes;
  struct input_event events[64];
  int dataChanged=0;

  if(inited!=1) return -1;

  // first thing to do is reset all relative axis as mice never seem to do it
  int i;
  for(i=0;i<numRelAxes;i++){
    if(relAxesData[i]!=0) {
      dataChanged=1;
      relAxesData[i]=0;
    }
  }

  read_bytes = read(fd, events, sizeof(struct input_event) * 64);

  if(read_bytes == 0) {
    // no sweat, just return;
    return 0;
  }
 
  if(read_bytes == -1) {
    if(errno == EAGAIN) {
      // No worries, we are in non blocking and noting is ready
      return 0;
    } else {
      perror("Error reading events: ");
      return -1;
    }
  }
  
  if (read_bytes < (int) sizeof(struct input_event)) {
    perror("Error reading events: ");
    return -1;
  }

  int numEventsRead = (int) (read_bytes / sizeof(struct input_event));
  for(i=0;i<numEventsRead;i++) {
    switch(events[i].type) {
      case EV_SYN: 
      case EV_MSC:
		// not sure what to do with it, ignore for now -- JPK
		break;
      case EV_KEY: {
        dataChanged = 1;
        int buttonIndex = buttonLookup[events[i].code];
        buttonData[buttonIndex] = events[i].value;
        //printf("button %d translates to button %d on this device\n", events[i].code, buttonIndex);
        break; 
      }
      case EV_REL: {
        dataChanged = 1;
        int axisIndex = relAxisLookup[events[i].code];
        relAxesData[axisIndex] += events[i].value;
        //printf("rel axis %d translates to rel axis %d on this device\n", events[i].code, axisIndex);
        break;
      }
      case EV_ABS: {
        dataChanged = 1;
        int axisIndex = absAxisLookup[events[i].code];
        absAxesData[axisIndex] = events[i].value;
        //printf("abs axis %d translates to abs axis %d on this device\n", events[i].code, axisIndex);
        break;
      }
      case EV_LED:
        // reveiced for things like numlock led change
        break;
      default:
        fprintf(stderr, "Received event of type 0x%02X from %s, which I wasn't expecting, please report it to jinput forum at www.javagaming.org\n", events[i].type, name);
    }
  }
  return dataChanged;
}

void EventDevice::getPolledData(int relAxesData[], int absAxesData[], int buttonData[]){
  int i;

  if(inited!=1) return;
  for(i=0;i<numRelAxes;i++) {
    (relAxesData)[i] = this->relAxesData[i];
  }
  for(i=0;i<numAbsAxes;i++) {
    (absAxesData)[i] = this->absAxesData[i];
  }
  for(i=0;i<numButtons;i++) {
    (buttonData)[i] = this->buttonData[i];
  }
}

int EventDevice::getAbsAxisMinimum(int axisNumber) {
  return abs_features[axisNumber].minimum;
}

int EventDevice::getAbsAxisMaximum(int axisNumber) {
  return abs_features[axisNumber].maximum;
}

int EventDevice::getAbsAxisFuzz(int axisNumber) {
  return abs_features[axisNumber].fuzz;
}

bool EventDevice::getFFEnabled() {
	if(ffSupported==1) {
		//LOG_TRACE("FF is supported for %s\n", getName());
		return true;
	}
	//LOG_TRACE("FF is not supported for %s\n", getName());
	return false;
}

void EventDevice::rumble(float force) {
	if(force>1) force=1;
	if(force<-1) force=-1;
	//LOG_TRACE("Rumbling at %d%%, (shh, pretend)\n", (int)(force*100));
	
	if(effect_playing==true) {
		stop.type=EV_FF;
		stop.code = effect.id;
		stop.value=0;
		
		LOG_TRACE("Removing effect %d\n", effect.id);
		if (ioctl(fd, EVIOCRMFF, &effect) == -1) {
		        perror("Remove effect");
		}
    
	} else {
		effect.id=-1;
	}
	
	effect.type=FF_RUMBLE;
	//effect.id=-1;
	effect.u.rumble.strong_magnitude = (int)(0x8000*force);
    effect.u.rumble.weak_magnitude = (int)(0xc000*force);
    effect.replay.length = 15000;
    effect.replay.delay = 0;

	if(effect_playing==true) {
		LOG_TRACE("Stoping %d\n", stop.code);
		if (write(fd, (const void*) &stop, sizeof(stop)) == -1) {
	        perror("Failed to stop effect");
	    }
	    effect_playing=false;
	}
	LOG_TRACE("Uploading effect %d\n", effect.id);
    if (ioctl(fd, EVIOCSFF, &effect) == -1) {
            perror("Upload effect");
    }
    
    play.type = EV_FF;
    play.code=effect.id;
    play.value=1;
    
    LOG_TRACE("Playing effect %d\n", play.code);
    if (write(fd, (const void*) &play, sizeof(play)) == -1) {
        perror("Failed to play effect");
    } else {
    	effect_playing=true;
    }
    
}
