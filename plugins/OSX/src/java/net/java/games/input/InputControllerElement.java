package net.java.games.input;

/**
 * Created by IntelliJ IDEA.
 * User: gpierce
 * Date: Aug 2, 2003
 * Time: 2:59:26 PM
 * To change this template use Options | File Templates.
 */
public class InputControllerElement
{
    private long                hidCookie;
    private int                 elementType;
    private String              elementName;

    private int                 rawMin;
    private int                 rawMax;
    private int                 scaledMin;
    private int                 scaledMax;

    private int                 dataBitSize;

    private boolean             isRelative;
    private boolean             isWrapping;
    private boolean             isNonLinear;
    private boolean             hasPreferredState;
    private boolean             hasNullState;

    public InputControllerElement()
    {
    }

    public InputControllerElement(long hidCookie, int elementType, String elementName,
                                  int rawMin, int rawMax, int scaledMin, int scaledMax,
                                  int dataBitSize, boolean relative, boolean wrapping,
                                  boolean nonLinear, boolean hasPreferredState, boolean hasNullState )
    {
        this.hidCookie = hidCookie;
        this.elementType = elementType;
        this.elementName = elementName;
        this.rawMin = rawMin;
        this.rawMax = rawMax;
        this.scaledMin = scaledMin;
        this.scaledMax = scaledMax;
        this.dataBitSize = dataBitSize;
        isRelative = relative;
        isWrapping = wrapping;
        isNonLinear = nonLinear;
        this.hasPreferredState = hasPreferredState;
        this.hasNullState = hasNullState;
    }

    public long getHidCookie()
    {
        return hidCookie;
    }

    public void setHidCookie(long hidCookie)
    {
        this.hidCookie = hidCookie;
    }

    public int getElementType()
    {
        return elementType;
    }

    public void setElementType(int elementType)
    {
        this.elementType = elementType;
    }

    public String getElementName()
    {
        return elementName;
    }

    public void setElementName(String elementName)
    {
        this.elementName = elementName;
    }

    public int getRawMin()
    {
        return rawMin;
    }

    public void setRawMin(int rawMin)
    {
        this.rawMin = rawMin;
    }

    public int getRawMax()
    {
        return rawMax;
    }

    public void setRawMax(int rawMax)
    {
        this.rawMax = rawMax;
    }

    public int getScaledMin()
    {
        return scaledMin;
    }

    public void setScaledMin(int scaledMin)
    {
        this.scaledMin = scaledMin;
    }

    public int getScaledMax()
    {
        return scaledMax;
    }

    public void setScaledMax(int scaledMax)
    {
        this.scaledMax = scaledMax;
    }

    public int getDataBitSize()
    {
        return dataBitSize;
    }

    public void setDataBitSize(int dataBitSize)
    {
        this.dataBitSize = dataBitSize;
    }

    public boolean isRelative()
    {
        return isRelative;
    }

    public void setRelative(boolean relative)
    {
        isRelative = relative;
    }

    public boolean isWrapping()
    {
        return isWrapping;
    }

    public void setWrapping(boolean wrapping)
    {
        isWrapping = wrapping;
    }

    public boolean isNonLinear()
    {
        return isNonLinear;
    }

    public void setNonLinear(boolean nonLinear)
    {
        isNonLinear = nonLinear;
    }

    public boolean isHasPreferredState()
    {
        return hasPreferredState;
    }

    public void setHasPreferredState(boolean hasPreferredState)
    {
        this.hasPreferredState = hasPreferredState;
    }

    public boolean isHasNullState()
    {
        return hasNullState;
    }

    public void setHasNullState(boolean hasNullState)
    {
        this.hasNullState = hasNullState;
    }

}
