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

#if !defined(eventInterface_Device_h)
#define eventInterface_Device_h

/**
 * Simple abstract device class
 *
 * @author Jeremy Booth (jeremy@newdawnsoftware.com)
 */
class Device {

  private:

  public:
    /** Maximum name length for a device */
    const static int MAX_NAME_LENGTH = 256;
    /** Return the number of relative axes for this device */
    virtual int getNumberRelAxes() = 0;
    /** Return the number ofr absolute axes for this device */
    virtual int getNumberAbsAxes() = 0;
    /** Return the number of buttons for this device */
    virtual int getNumberButtons() = 0;
    /** Get the name of this device */
    virtual const char *getName() = 0;
    /** get teh bus type */
    virtual int getBusType() = 0;
    virtual int getVendorID() = 0;
    virtual int getProductID() = 0;
    virtual int getVersion() = 0;
    /** Get the supported axes/button maps */
    virtual void getSupportedRelAxes(int supportedAxis[]) = 0;
    virtual void getSupportedAbsAxes(int supportedAxis[]) = 0;
    virtual void getSupportedButtons(int supportedButtons[]) = 0;
    /** poll it */
    virtual int poll() = 0;
    /** get the data */
    virtual void getPolledData(int relAxesData[], int absAxesData[], int buttonData[]) = 0;
    /** Get axis details */
    virtual int getAbsAxisMinimum(int axisNumber) = 0;
    virtual int getAbsAxisMaximum(int axisNumber) = 0;
    virtual int getAbsAxisFuzz(int axisNumber) = 0;
    virtual bool getFFEnabled() = 0;
    virtual void rumble(float force) = 0;
    virtual void cleanup() = 0;
};

#endif //eventInterface_Device_h
