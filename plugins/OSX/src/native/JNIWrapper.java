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
import java.util.*;

public class JNIWrapper
{    
    static 
    {
        System.loadLibrary("hidinput");
    }

    public native void hidCreate();
    public native void hidDispose();
    public native void enumDevices( ArrayList list );     
    
    
    private ArrayList            devices = new ArrayList();
    
    
    public JNIWrapper()
    {
        System.out.println("JNIWrapper instance created");
    }    
    
    public ArrayList getDevices()
    {
        return devices;
    }
    
    public void setDevices( ArrayList list )
    {
        devices = list;
    }
    
    private void addDevice( ArrayList list, long lpDevice, int type, String productName )
    {
        System.out.println("Found device [" + productName + "] of type [" + type + "]");
    }
    
    public static void main (String args[]) 
    {
        System.out.println("Started JNIWrapper");
        JNIWrapper newjni = new JNIWrapper();

        System.out.println("Creating HID engine");
        newjni.hidCreate();
        
        System.out.println("Enumerating devices");
        newjni.enumDevices( newjni.getDevices() );
        
        System.out.println("Disposing HID engine");
        newjni.hidDispose();
        
        System.out.println("Done");
    }

}
