package net.java.games.input;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: gpierce
 * Date: Aug 2, 2003
 * Time: 2:57:15 PM
 * To change this template use Options | File Templates.
 */
public class InputController
{
    private OSXEnvironmentPlugin            plugin;
    private long                            lpDevice;
    private long                            lpQueue;
    private int                             queueDepth;
    private String                          transportKey;
    private int                             vendorID;
    private int                             productID;
    private int                             version;
    private String                          manufacturer;
    private String                          productName;
    private String                          serialNumber;
    private int                             usbLocationID;
    private int                             usagePage;
    private int                             usage;

    private HashMap                         controllerElements = new HashMap();

    public InputController( OSXEnvironmentPlugin plugin )
    {
        this.plugin = plugin;
    }

    public InputController(OSXEnvironmentPlugin plugin, long lpDevice, String productName, int usage)
    {
        this.plugin = plugin;
        this.lpDevice = lpDevice;
        this.productName = productName;
        this.usage = usage;
    }

    public InputController( OSXEnvironmentPlugin plugin, long lpDevice, String transportKey, int vendorID, int productID, int version, String manufacturer, String productName, String serialNumber, int usbLocationID, int usagePage, int usage)
    {
        this.plugin = plugin;
        this.lpDevice = lpDevice;
        this.transportKey = transportKey;
        this.vendorID = vendorID;
        this.productID = productID;
        this.version = version;
        this.manufacturer = manufacturer;
        this.productName = productName;
        this.serialNumber = serialNumber;
        this.usbLocationID = usbLocationID;
        this.usagePage = usagePage;
        this.usage = usage;
    }

    public long getLpQueue()
    {
        return lpQueue;
    }

    public void setLpQueue(long lpQueue)
    {
        this.lpQueue = lpQueue;
    }

    public int getQueueDepth()
    {
        return queueDepth;
    }

    public void setQueueDepth(int queueDepth)
    {
        this.queueDepth = queueDepth;
    }

    public long getLpDevice()
    {
        return lpDevice;
    }

    public void setLpDevice(long lpDevice)
    {
        this.lpDevice = lpDevice;
    }

    public String getTransportKey()
    {
        return transportKey;
    }

    public void setTransportKey(String transportKey)
    {
        this.transportKey = transportKey;
    }

    public int getVendorID()
    {
        return vendorID;
    }

    public void setVendorID(int vendorID)
    {
        this.vendorID = vendorID;
    }

    public int getProductID()
    {
        return productID;
    }

    public void setProductID(int productID)
    {
        this.productID = productID;
    }

    public int getVersion()
    {
        return version;
    }

    public void setVersion(int version)
    {
        this.version = version;
    }

    public String getManufacturer()
    {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer)
    {
        this.manufacturer = manufacturer;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber)
    {
        this.serialNumber = serialNumber;
    }

    public int getUsbLocationID()
    {
        return usbLocationID;
    }

    public void setUsbLocationID(int usbLocationID)
    {
        this.usbLocationID = usbLocationID;
    }

    public int getUsagePage()
    {
        return usagePage;
    }

    public void setUsagePage(int usagePage)
    {
        this.usagePage = usagePage;
    }

    public int getUsage()
    {
        return usage;
    }

    public void setUsage(int usage)
    {
        this.usage = usage;
    }

    public HashMap getControllerElements()
    {
        return controllerElements;
    }

    public void addControllerElement( InputControllerElement controllerElement )
    {
        controllerElements.put( new Long(controllerElement.getHidCookie()), controllerElement );
    }

    public InputControllerElement getControllerElement( long hidCookie )
    {
        return (InputControllerElement) controllerElements.get( new Long( hidCookie ) );
    }


    public void openDevice()
    {
        this.lpQueue = plugin.openDevice( this.lpDevice, 32 );
    }

    public void closeDevice()
    {
        plugin.closeDevice( this.lpDevice, this.lpQueue );
    }

    public void pollDevice()
    {
        plugin.pollDevice( this.lpQueue );
    }
}
