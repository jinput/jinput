//
//  JNIWrapper.java
//
//  Created by Gregory Pierce on Wed Jul 23 2003.
//  Copyright (c) 2003 __MyCompanyName__. All rights reserved.
//

import java.util.*;

public class JNIWrapper {

    static {
        // Ensure native JNI library is loaded
        System.loadLibrary("hidinput");
    }

    public JNIWrapper() {
        System.out.println("JNIWrapper instance created");
    }

    native void hidCreate();
    native void hidDispose();
    native void enumDevices();
    
    native int native_method(String arg);

    public static void main (String args[]) 
    {
        System.out.println("Started JNIWrapper");
        JNIWrapper newjni = new JNIWrapper();

        System.out.println("Creating HID engine");
        newjni.hidCreate();
        
        System.out.println("Enumerating devices");
        newjni.enumDevices();
        
        System.out.println("Disposing HID engine");
        newjni.hidDispose();
        
        System.out.println("Done");
    }

}
