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

#include <sys/dir.h>
#include <stdio.h>
#include <dirent.h>
#include <sys/ioctl.h>
#include <fcntl.h>
#include <linux/input.h>
#include <string.h>
#include <malloc.h>
#include <unistd.h>
#include "Device.h"
#include "EventDevice.h"

int evNumDevices;
int eventInterfaceVersion;
Device **evDeviceList;
int evInited = 0;

int evFileFilter(const struct direct *entry) {
  if (strncmp(entry->d_name, "event", 5) == 0) {
    return 1;
  }
    return 0;
  }

int evGetDeviceFiles(char ***filenames) {
  struct direct **files;
  int num_files, i;
  char dirName[] = {"/dev/input"};

  num_files = scandir(dirName, &files, &evFileFilter, alphasort);

  *filenames = (char **)malloc(num_files * sizeof(char *));

  for(i=0;i<num_files;i++) {
    char *filename = files[i]->d_name;
    char *fullFileName;

    fullFileName = (char *)malloc((strlen(dirName) + 1 + strlen(filename) + 1));
    sprintf(fullFileName, "%s/%s", dirName, filename);
    (*filenames)[i] = fullFileName;
  }

  return num_files;
}

int evInit() {
  int fd=-1;
  int i;
  char **deviceFileNames;
  int numDeviceFiles;

  numDeviceFiles = evGetDeviceFiles(&deviceFileNames);
  if(numDeviceFiles<0) {
    return -1;
  }
  
  if ((fd = open(deviceFileNames[0], O_RDONLY)) <0) {
    return -1;
  }

  if (ioctl(fd, EVIOCGVERSION, &eventInterfaceVersion)) {
    close(fd);
    return -1;
  }

  close(fd);

  Device *tempDeviceList[numDeviceFiles];
 
  evNumDevices = 0;
  for(i=0;i<numDeviceFiles;i++) {
    tempDeviceList[i] = new EventDevice(deviceFileNames[i]);
    if(tempDeviceList[i]->getBusType()!=-1) {
      evNumDevices++;
    }
  }

  // Now we know for certain which devices are open, we can take notes
  evDeviceList = (Device **)malloc(evNumDevices * sizeof(Device *));
  for(i=0;i<evNumDevices;i++) {
    evDeviceList[i] = tempDeviceList[i];
  }

  evInited=1;

  return 0;
}

int evGetEventInterfaceVersionNumber() {
  return eventInterfaceVersion;
}

int evGetNumberDevices() {
  if(evInited) {
    return evNumDevices;
  }
  return -1;
}

void evGetDevices(Device **theirDeviceList) {
  int i;
  for(i=0;i<evNumDevices;i++) {
    theirDeviceList[i] = evDeviceList[i];
  }
}
