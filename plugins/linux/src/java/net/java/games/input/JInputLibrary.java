package net.java.games.input;

public class JInputLibrary {
    static {
        if(isSupported()) {
            System.loadLibrary("jinput-linux");
        }
    }    
    
    private static boolean inited = false;
    private static Object workerThreadMonitor = new Object();
    private static boolean shutdown = false;
    private static Object shutdownThreadMonitor = new Object();
    private static boolean cleanupDone = false;
    private static int rumbler;
    private static float force;

    public static boolean isSupported() {
        System.out.println("OS name is: " + System.getProperty("os.name"));
        if(System.getProperty("os.name").indexOf("Linux")!=-1) {
            System.out.println("Linux plugin is supported");
            return true;
        }
        System.out.println("Linux plugin is not supported");
        return false;
    }
    
    public static void init() {
        if(!inited) {
            System.out.println("Initing JInputLibrary");
            Thread initShutdownThread = new Thread() {
                public void run() {            
                    
                    nativeInit();
                    inited=true;
                    
                    synchronized (workerThreadMonitor) {
                        workerThreadMonitor.notify();
                    }
                    
                    synchronized(workerThreadMonitor) {
                        while(!shutdown) {
                            System.out.println("Waiting on monitor");
                            System.out.flush();
                            try {
                                workerThreadMonitor.wait();
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            if(rumbler>=0) {
                                nativeRumble(rumbler,force);
                                rumbler =-1;
                            }
                        }
                    }
                    System.out.println("Cleaning up from shutdown thread");
                    realCleanup();
                    cleanupDone = true;
                    synchronized (shutdownThreadMonitor) {
                        System.out.println("Notifying on shutdownThreadMonitor after shutdown");
                        System.out.flush();
                        shutdownThreadMonitor.notifyAll();
                        System.out.println("Notified on shutdownThreadMonitor after shutdown");
                        System.out.flush();
                    }
                }
            };
            
            initShutdownThread.setDaemon(true);
            initShutdownThread.start();
            
            System.out.println("Shutdown thread created and run");

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    cleanup();
                }
            });         
                
            synchronized (workerThreadMonitor) {
                while(!inited) {
                    try {
                        workerThreadMonitor.wait();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        }
    }
    
    private static void realCleanup() {
        //Give the rumblers chance to cleanup
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Environment cleanup");
        for(int i=0;i<JInputLibrary.getNumberOfDevices();i++) {
            JInputLibrary.nativeCleanup(i);
        }
    }
    
    public static void rumble(int rumblerNo, float forceValue) {
        rumbler = rumblerNo;
        force = forceValue;
        synchronized (workerThreadMonitor) {
            System.out.println("Notifying clean up thread");
            System.out.flush();
            workerThreadMonitor.notify();
        }
    }
    
    private static void cleanup() {
        shutdown = true;
        System.out.println("Trying to notify for cleanup");
        System.out.flush();
        synchronized (workerThreadMonitor) {
            System.out.println("Notifying clean up thread");
            System.out.flush();
            workerThreadMonitor.notify();
        }
        
        while(!cleanupDone) {
            synchronized (shutdownThreadMonitor) {
                try {
                    System.out.println("cleanup waiting on shutdownThreadMonitor");
                    System.out.flush();
                    shutdownThreadMonitor.wait();
                    System.out.println("cleanup done waiting on shutdownThreadMonitor");
                    System.out.flush();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    
    /** Call to poll the device at the native library
     * @param deviceID The native device ID
     * @param buttonData Array to populate with button values
     * @param relAxesData Array to populate with relative axes values
     * @param absAxesData Array to populate with absolute axes values
     * @return the number of events read
     */    
    public static int safePoll(int deviceID, int buttonData[], int relAxesData[], int absAxesData[]) {
        if(!shutdown) {
            return poll(deviceID, buttonData, relAxesData, absAxesData);
        }
        
        return 0;
    }

    
    /** Get the name of a device from the native library
     * @param deviceID The device id
     * @return The devices name
     */    
    public static native String getDeviceName(int deviceID);
    /** Get the number of absolute axes for the requested device
     * @param deviceID The device ID
     * @return The number of abs axes
     */    
    public static native int getNumAbsAxes(int deviceID);
    /** Get the nmber or relative axes from the native library
     * @param deviceID The native device ID
     * @return The number of raltive axes for the device
     */    
    public static native int getNumRelAxes(int deviceID);
    /** Gets the number of buttons for the requested devce from the native library
     * @param deviceID The device ID
     * @return The number of buttons
     */    
    public static native int getNumButtons(int deviceID);
    /** Initialises the native library
     * @return <0 if something went wrong
     */    
    private static native int nativeInit();
    /** Gets the number of devices the native library found
     * @return Th number of devices
     */    
    public static native int getNumberOfDevices();
    /** Native call to get the supported absolute axes for a device
     * @param deviceID The native device number
     * @param supportedAbsAxes aray to populate
     */    
    public static native void getSupportedAbsAxes(int deviceID, int supportedAbsAxes[]);
    /** Native call to get the supported relative axes for a device
     * @param deviceID The native device number
     * @param supportedRelAxes aray to populate
     */    
    public static native void getSupportedRelAxes(int deviceID, int supportedRelAxes[]);
    /** Native call to get the supported buttons for a device
     * @param deviceID The native device ID
     * @param supportedButtons The array to populate
     */    
    public static native void getSupportedButtons(int deviceID, int supportedButtons[]);
    /** Call to poll the device at the native library
     * @param deviceID The native device ID
     * @param buttonData Array to populate with button values
     * @param relAxesData Array to populate with relative axes values
     * @param absAxesData Array to populate with absolute axes values
     * @return the number of events read
     */    
    public static native int poll(int deviceID, int buttonData[], int relAxesData[], int absAxesData[]);
    /** Returns the fuzz of an axis fro mthe native lib
     * @param deviceID The native device id
     * @param axisID The native axis ID
     * @return The fuzz
     */    
    public static native int getAbsAxisFuzz(int deviceID, int axisID);
    /** Gets the maximum value for an absloute axis fr omthe native library
     * @param deviceID The native device ID
     * @param axisID The native axis ID
     * @return The Max value
     */    
    public static native int getAbsAxisMaximum(int deviceID, int axisID);
    /** Gets the minimum value for an absloute axis from the native library
     * @param deviceID The native device ID
     * @param axisID The native axis number
     * @return The min value
     */    
    public static native int getAbsAxisMinimum(int deviceID, int axisID);
    /** Gets the port type from the native lib
     * @param deviceID The device to get the port type for
     * @return The port type
     */    
    public static native int getNativePortType(int deviceID);
    
    public static native boolean getFFEnabled(int deviceID);
    
    private static native void nativeRumble(int deviceID, float intensity);
    
    private static native void nativeCleanup(int deviceID);
}
