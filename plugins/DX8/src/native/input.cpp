/*
 * %W% %E%
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

#ifndef WIN32
    #error This is a Windows-only file
#endif

// hard define as DX7
//#define DIRECTINPUT_VERSION 0x0800
#include <windows.h>
#include <jni.h>
#include <dinput.h>


/*
 ******************************************************************************
 * Global variables
 ******************************************************************************
 */
jclass CLASS_AxisIdentifier = NULL;
jclass CLASS_ButtonIdentifier = NULL;
jclass CLASS_DirectInputEnvironmentPlugin = NULL;
jclass CLASS_DirectInputDevice = NULL;
jclass CLASS_DirectInputKeyboard = NULL;
jclass CLASS_DirectInputMouse = NULL;
jmethodID MID_AddDevice = NULL;
jmethodID MID_AddAxis = NULL;
jmethodID MID_AddRumbler = NULL;
jmethodID MID_RenameKey = NULL;
jmethodID MID_RenameAxis = NULL;
jfieldID FID_X = NULL;
jfieldID FID_Y = NULL;
jfieldID FID_Z = NULL;
jfieldID FID_RX = NULL;
jfieldID FID_RY = NULL;
jfieldID FID_RZ = NULL;
jfieldID FID_Slider = NULL;
jfieldID FID_Button = NULL;
jfieldID FID_POV = NULL;
jfieldID FID_Left = NULL;
jfieldID FID_Right = NULL;
jfieldID FID_Middle = NULL;
jfieldID FID_Extra = NULL;
jfieldID FID_Side = NULL;
jfieldID FID_Forward = NULL;
jfieldID FID_Back = NULL;

const char* FD_AxisIdentifier = "Lnet/java/games/input/Component$Identifier$Axis;";
const char* FD_ButtonIdentifier = "Lnet/java/games/input/Component$Identifier$Button;";
// Dummy input window.  This is needed because DirectX evidently needs a window
// to do anything, such as setting the cooperative level for a device.
const TCHAR* DUMMY_WINDOW_NAME = "InputControllerWindow";
HWND hwndDummy = NULL;
// Buffer size
// Endolf changed the name as it is specific to the mouse
// Endolf increased the size as he kept making it go pop
const DWORD MOUSE_BUFFER_SIZE = 32;

// Class for handing device data to the callback for EnumDevices
class DeviceParamData {
public:
    DeviceParamData(LPDIRECTINPUT8 lpdi, JNIEnv* e, jobject o, jobject l) :
        lpDirectInput(lpdi), env(e), obj(o), list(l)
    {
    }
    LPDIRECTINPUT8 lpDirectInput;
    JNIEnv* env;
    jobject obj;
    jobject list;
};

// Class for handing device data to the callback for EnumObjects
class ObjectParamData {
public:
    ObjectParamData(LPDIRECTINPUTDEVICE8 lpDev, JNIEnv* e, jobject o,
        jobject l) :
        lpDevice(lpDev), env(e), obj(o), list(l)
    {
    }
    LPDIRECTINPUTDEVICE8 lpDevice;
    JNIEnv* env;
    jobject obj;
    jobject list;
};

void PrintOutput(TCHAR* tszMessage) {
    printf("%s\n", tszMessage);
}

void PrintDIError(TCHAR* tszOutput, HRESULT res) {
    TCHAR tszMessage[256];
#define CHECK_RESULT(r) case r: \
sprintf(tszMessage, "%s : %s", tszOutput, #r); \
break;
    switch (res) {
        CHECK_RESULT(DI_OK)
        CHECK_RESULT(DI_NOTATTACHED)
        CHECK_RESULT(DI_POLLEDDEVICE)
        CHECK_RESULT(DI_DOWNLOADSKIPPED)
        CHECK_RESULT(DI_EFFECTRESTARTED)
        CHECK_RESULT(DI_TRUNCATED)
        CHECK_RESULT(DI_TRUNCATEDANDRESTARTED)
        CHECK_RESULT(DIERR_OLDDIRECTINPUTVERSION)
        CHECK_RESULT(DIERR_BETADIRECTINPUTVERSION)
        CHECK_RESULT(DIERR_BADDRIVERVER)
        CHECK_RESULT(DIERR_DEVICENOTREG)
        CHECK_RESULT(DIERR_NOTFOUND)
        //CHECK_RESULT(DIERR_OBJECTNOTFOUND)
        CHECK_RESULT(DIERR_INVALIDPARAM)
        CHECK_RESULT(DIERR_NOINTERFACE)
        CHECK_RESULT(DIERR_GENERIC)
        CHECK_RESULT(DIERR_OUTOFMEMORY)
        CHECK_RESULT(DIERR_UNSUPPORTED)
        CHECK_RESULT(DIERR_NOTINITIALIZED)
        CHECK_RESULT(DIERR_ALREADYINITIALIZED)
        CHECK_RESULT(DIERR_NOAGGREGATION)
        CHECK_RESULT(DIERR_OTHERAPPHASPRIO)
        CHECK_RESULT(DIERR_INPUTLOST)
        CHECK_RESULT(DIERR_ACQUIRED)
        CHECK_RESULT(DIERR_NOTACQUIRED)
        //CHECK_RESULT(DIERR_READONLY)
        //CHECK_RESULT(DIERR_HANDLEEXISTS)
        CHECK_RESULT(DIERR_INSUFFICIENTPRIVS)
        CHECK_RESULT(DIERR_DEVICEFULL)
        CHECK_RESULT(DIERR_MOREDATA)
        CHECK_RESULT(DIERR_NOTDOWNLOADED)
        CHECK_RESULT(DIERR_HASEFFECTS)
        CHECK_RESULT(DIERR_NOTEXCLUSIVEACQUIRED)
        CHECK_RESULT(DIERR_INCOMPLETEEFFECT)
        CHECK_RESULT(DIERR_NOTBUFFERED)
        CHECK_RESULT(DIERR_EFFECTPLAYING)
        CHECK_RESULT(DIERR_UNPLUGGED)
        CHECK_RESULT(DIERR_REPORTFULL)
        default: sprintf(tszMessage, "Unknown"); break;
    }
    PrintOutput(tszMessage);
}

/*
 ******************************************************************************
 * DirectInputEnvironmentPlugin
 ******************************************************************************
 */

/*
 * Initialize all class, method, and field IDs
 */
BOOL InitIDs(JNIEnv* env) {
    CLASS_AxisIdentifier =
        env->FindClass("net/java/games/input/Component$Identifier$Axis");
    if (CLASS_AxisIdentifier == NULL) {
        return FALSE;
    }
    FID_X = env->GetStaticFieldID(CLASS_AxisIdentifier, "X",
        FD_AxisIdentifier);
    if (FID_X == NULL) {
        return FALSE;
    }
    FID_Y = env->GetStaticFieldID(CLASS_AxisIdentifier, "Y",
        FD_AxisIdentifier);
    if (FID_Y == NULL) {
        return FALSE;
    }
    FID_Z = env->GetStaticFieldID(CLASS_AxisIdentifier, "Z",
        FD_AxisIdentifier);
    if (FID_Z == NULL) {
        return FALSE;
    }
    FID_RX = env->GetStaticFieldID(CLASS_AxisIdentifier, "RX",
        FD_AxisIdentifier);
    if (FID_RX == NULL) {
        return FALSE;
    }
    FID_RY = env->GetStaticFieldID(CLASS_AxisIdentifier, "RY",
        FD_AxisIdentifier);
    if (FID_RY == NULL) {
        return FALSE;
    }
    FID_RZ = env->GetStaticFieldID(CLASS_AxisIdentifier, "RZ",
        FD_AxisIdentifier);
    if (FID_RZ == NULL) {
        return FALSE;
    }
    FID_Slider = env->GetStaticFieldID(CLASS_AxisIdentifier, "SLIDER",
        FD_AxisIdentifier);
    if (FID_Slider == NULL) {
        return FALSE;
    }
/*    FID_Button = env->GetStaticFieldID(CLASS_AxisIdentifier, "BUTTON",
        FD_AxisIdentifier);
    if (FID_Button == NULL) {
        return FALSE;
    }*/
    FID_POV = env->GetStaticFieldID(CLASS_AxisIdentifier, "POV",
        FD_AxisIdentifier);
    if (FID_POV == NULL) {
        return FALSE;
    }
    CLASS_ButtonIdentifier =
        env->FindClass("net/java/games/input/Component$Identifier$Button");
    if (CLASS_ButtonIdentifier == NULL) {
        return FALSE;
    }
    FID_Left = env->GetStaticFieldID(CLASS_ButtonIdentifier, "LEFT",
        FD_ButtonIdentifier);
    if (FID_Left == NULL) {
        return FALSE;
    }
    FID_Right = env->GetStaticFieldID(CLASS_ButtonIdentifier, "RIGHT",
        FD_ButtonIdentifier);
    if (FID_Right == NULL) {
        return FALSE;
    }
    FID_Middle = env->GetStaticFieldID(CLASS_ButtonIdentifier, "MIDDLE",
        FD_ButtonIdentifier);
    if (FID_Middle == NULL) {
        return FALSE;
    }
// Endolf
    FID_Side = env->GetStaticFieldID(CLASS_ButtonIdentifier, "SIDE",
        FD_ButtonIdentifier);
    if (FID_Side == NULL) {
        return FALSE;
    }
    FID_Extra = env->GetStaticFieldID(CLASS_ButtonIdentifier, "EXTRA",
        FD_ButtonIdentifier);
    if (FID_Extra == NULL) {
        return FALSE;
    }
    FID_Forward = env->GetStaticFieldID(CLASS_ButtonIdentifier, "FORWARD",
        FD_ButtonIdentifier);
    if (FID_Forward == NULL) {
        return FALSE;
    }
    FID_Back = env->GetStaticFieldID(CLASS_ButtonIdentifier, "BACK",
        FD_ButtonIdentifier);
    if (FID_Back == NULL) {
        return FALSE;
    }

    CLASS_DirectInputEnvironmentPlugin =
        env->FindClass("net/java/games/input/DirectInputEnvironmentPlugin");
    if (CLASS_DirectInputEnvironmentPlugin == NULL) {
        return FALSE;
    }
    MID_AddDevice = env->GetMethodID(CLASS_DirectInputEnvironmentPlugin, "addDevice",
        "(Ljava/util/ArrayList;JILjava/lang/String;Ljava/lang/String;Z)V");
    if (MID_AddDevice == NULL) {
        return FALSE;
    }
    CLASS_DirectInputDevice =
        env->FindClass("net/java/games/input/DirectInputDevice");
    if (CLASS_DirectInputDevice == NULL) {
        return FALSE;
    }
    MID_AddAxis = env->GetMethodID(CLASS_DirectInputDevice, "addAxis",
        "(Ljava/util/ArrayList;Lnet/java/games/input/Component$Identifier;ILjava/lang/String;)V");
    if (MID_AddAxis == NULL) {
        return FALSE;
    }
    MID_AddRumbler = env->GetMethodID(CLASS_DirectInputDevice, "addRumbler",
        "(JLnet/java/games/input/Component$Identifier;Ljava/lang/String;)V");
    if (MID_AddRumbler == NULL) {
        return FALSE;
    }
    CLASS_DirectInputKeyboard =
        env->FindClass("net/java/games/input/DirectInputKeyboard");
    if (CLASS_DirectInputKeyboard == NULL) {
        return FALSE;
    }
    MID_RenameKey = env->GetMethodID(CLASS_DirectInputKeyboard, "renameKey",
        "(ILjava/lang/String;)V");
    if (MID_RenameKey == NULL) {
        return FALSE;
    }
    CLASS_DirectInputMouse =
        env->FindClass("net/java/games/input/DirectInputMouse");
    if (CLASS_DirectInputMouse == NULL) {
        return FALSE;
    }
    MID_RenameAxis = env->GetMethodID(CLASS_DirectInputMouse, "renameAxis",
        "(Lnet/java/games/input/Component$Identifier;Ljava/lang/String;)V");
    if (MID_RenameAxis == NULL) {
        return FALSE;
    }
    return TRUE;
}

/*
 * WndProc for our dummy input window
 */
LRESULT CALLBACK DummyWndProc(
    HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam)
{
    return DefWindowProc(hWnd, message, wParam, lParam);
}

/*
 * Register the dummy input window class
 */
BOOL RegisterDummyWindow(HINSTANCE hInstance)
{
	WNDCLASSEX wcex;
	wcex.cbSize = sizeof(WNDCLASSEX); 
	wcex.style			= CS_HREDRAW | CS_VREDRAW;
	wcex.lpfnWndProc	= (WNDPROC)DummyWndProc;
	wcex.cbClsExtra		= 0;
	wcex.cbWndExtra		= 0;
	wcex.hInstance		= hInstance;
	wcex.hIcon			= NULL;
	wcex.hCursor		= NULL;
	wcex.hbrBackground	= (HBRUSH)(COLOR_WINDOW+1);
	wcex.lpszMenuName	= (LPCSTR)NULL;
	wcex.lpszClassName	= DUMMY_WINDOW_NAME;
	wcex.hIconSm		= NULL;
	return RegisterClassEx(&wcex);
}

/*
 * Class:     org_java_games_input_DirectInputEnvironmentPlugin
 * Method:    directInputCreate
 * Signature: ()J
 */
extern "C" JNIEXPORT jlong JNICALL
Java_net_java_games_input_DirectInputEnvironmentPlugin_directInputCreate
    (JNIEnv* env, jobject obj)
{
    // Get our module handle
    HINSTANCE hInst = GetModuleHandle(NULL);

    // Register the dummy input window
    if (!RegisterDummyWindow(hInst)) {
        return (jlong)0;
    }

    // Create the dummy input window
    hwndDummy = CreateWindow(DUMMY_WINDOW_NAME, NULL,
        WS_POPUP | WS_ICONIC,
        0, 0, 0, 0, NULL, NULL, hInst, NULL);
    if (hwndDummy == NULL)
    {
        return (jlong)0;
    }

    // Create the IDirectInput object
    DWORD dwVersion = DIRECTINPUT_VERSION;
    LPDIRECTINPUT8 lpDirectInput = NULL;
    HRESULT res;
    if (FAILED(res = DirectInput8Create(hInst, DIRECTINPUT_VERSION,
        IID_IDirectInput8,(VOID **)&lpDirectInput, NULL))){
        PrintDIError("DirectInputCreate", res);
        return (jlong)0;
    }

    // Initialize method, class, and field IDs
    if (!InitIDs(env)) {
        lpDirectInput->Release();
        return (jlong)0;
    }

    return (jlong)(long)lpDirectInput;
}

BOOL CALLBACK CountFFAxesCallback( const DIDEVICEOBJECTINSTANCE* pdidoi,
                                VOID* pContext )
{
    DWORD* pdwNumForceFeedbackAxis = (DWORD*) pContext;

    if( (pdidoi->dwFlags & DIDOI_FFACTUATOR) != 0 ) {
		//printf("%s is ff enabled\n", pdidoi->tszName);
        (*pdwNumForceFeedbackAxis)++;
	}

    return DIENUM_CONTINUE;
}

/*
 * Enumeration callback for devices
 *
 * returns DIENUM_CONTINUE or DIENUM_STOP
 */

/** jeff's new enum callback */
BOOL CALLBACK EnumDeviceCallback(LPCDIDEVICEINSTANCE lpddi, LPVOID pvRef)
{
    DeviceParamData* pData = (DeviceParamData*)pvRef;
    LPDIRECTINPUT8 lpDirectInput = pData->lpDirectInput;
    JNIEnv* env = pData->env;
    jobject obj = pData->obj;
    jobject list = pData->list;
    LPDIRECTINPUTDEVICE8 lpDevice = NULL;
    LPUNKNOWN pUnknown = NULL;
    HRESULT res;

     // Create the device object
    if (FAILED(res = lpDirectInput->CreateDevice(lpddi->guidInstance, &lpDevice,
        pUnknown))){
        PrintDIError("CreateDevice", res);
        return DIENUM_STOP;
    }

    /*
    LPDIRECTINPUTDEVICE8 lpDevice2 = NULL;
    // Get the IDirectDrawDevice8 interface from the object
    res = lpDevice->QueryInterface(IID_IDirectInputDevice8,
        (void**)&lpDevice2);
    if (res != DI_OK) {
        PrintDIError("QueryInterface DID2", res);
        lpDevice->Release();
        return DIENUM_STOP;
    }
    */

	//find out if this device is ff enabled
	DWORD numForceFeedbackAxis = 0;
	res = lpDevice->EnumObjects( CountFFAxesCallback, 
                                              (VOID*)&numForceFeedbackAxis, DIDFT_AXIS );
	if(FAILED(res)) {
		PrintDIError("getting ff devices", res);
	}

       // Set the data format
    LPCDIDATAFORMAT lpDataFormat;
    DWORD category = GET_DIDEVICE_TYPE(lpddi->dwDevType)&0xFF;
    switch (category){
        case DI8DEVTYPE_KEYBOARD: 
            //printf("found Keyboard\n");
            lpDataFormat = &c_dfDIKeyboard;
        break;
        case DI8DEVTYPE_MOUSE: 
            //printf("found mouse\n");
            lpDataFormat = &c_dfDIMouse2;
            // set up buffering
            DIPROPDWORD dipropdw;
            dipropdw.diph.dwSize = sizeof(DIPROPDWORD);
            dipropdw.diph.dwHeaderSize = sizeof(DIPROPHEADER);
            dipropdw.diph.dwObj = 0;
            dipropdw.diph.dwHow = DIPH_DEVICE;
            dipropdw.dwData = MOUSE_BUFFER_SIZE;
            if (FAILED(
                res = lpDevice->SetProperty(DIPROP_BUFFERSIZE, 
                        &dipropdw.diph))) {
				PrintDIError("SetProperty", res);
				lpDevice->Release();
				return DIENUM_STOP;
			}
        break;
        case DI8DEVTYPE_JOYSTICK:
        default:
           //printf("found stick\n");
           lpDataFormat = &c_dfDIJoystick;
        break; 
    }

    if (FAILED(res = lpDevice->SetDataFormat(lpDataFormat))){
        PrintDIError("SetDataFormat", res);
        lpDevice->Release();
        return DIENUM_STOP;
    }

	if(numForceFeedbackAxis>0) {
		// Set the cooperative level
		if(FAILED(res = lpDevice->SetCooperativeLevel(hwndDummy,
			DISCL_EXCLUSIVE | DISCL_BACKGROUND))){
			PrintDIError("SetCooperativeLevel", res);
			lpDevice->Release();
			return DIENUM_STOP;
		}
	} else {
		// Set the cooperative level
		if(FAILED(res = lpDevice->SetCooperativeLevel(hwndDummy,
			DISCL_NONEXCLUSIVE | DISCL_BACKGROUND))){
			PrintDIError("SetCooperativeLevel", res);
			lpDevice->Release();
			return DIENUM_STOP;
		}
	}

    // get polling
    DIDEVCAPS didc;
    // Allocate space for all the device's objects (axes, buttons, POVS)
    ZeroMemory( &didc, sizeof(DIDEVCAPS) );
    didc.dwSize = sizeof(DIDEVCAPS);
    if (FAILED(res=lpDevice->GetCapabilities(&didc))){
        PrintDIError("Get Device Capabilities", res);
        lpDevice->Release();
        return DIENUM_STOP;
    }
    jboolean polled = JNI_FALSE;
    if ((didc.dwFlags)&DIDC_POLLEDDATAFORMAT) {
        polled = JNI_TRUE;
    }
    
    // Acquire the device
    if(FAILED(res = lpDevice->Acquire())){
        PrintDIError("Acquire", res);
        lpDevice->Release();
        return DIENUM_STOP;
    }

    // Set the variables for the Java callback
    jint type = (jint)lpddi->dwDevType&0xffff;
    //printf("type == %x\n",type);
    jstring productName = env->NewStringUTF(lpddi->tszProductName);
    if (productName == NULL) {
        lpDevice->Release();
        return DIENUM_STOP;
    }
    jstring instanceName = env->NewStringUTF(lpddi->tszInstanceName);
    if (instanceName == NULL) {
        lpDevice->Release();
        return DIENUM_STOP;
    }

    // Add the device into the list
    env->CallVoidMethod(obj, MID_AddDevice, list, (jlong)(long)lpDevice, type,
        productName, instanceName,(jboolean)polled);
    return DIENUM_CONTINUE;
}

/*
 * Class:     org_java_games_input_DirectInputEnvironmentPlugin
 * Method:    enumDevices
 * Signature: (JLjava/util/ArrayList;)Z
 */
extern "C" JNIEXPORT jboolean JNICALL
Java_net_java_games_input_DirectInputEnvironmentPlugin_enumDevices
    (JNIEnv* env, jobject obj, jlong lDirectInput, jobject list)
{
    LPDIRECTINPUT8 lpDirectInput = (LPDIRECTINPUT8)(long)lDirectInput;
    DWORD dwDevType = DI8DEVCLASS_ALL;
    DeviceParamData data(lpDirectInput, env, obj, list);
    LPVOID pvRef = (LPVOID)&data;
    DWORD dwFlags = DIEDFL_ATTACHEDONLY;
    HRESULT res;
    if(FAILED(res=lpDirectInput->EnumDevices(dwDevType,
        EnumDeviceCallback, pvRef, dwFlags))){
        PrintDIError("EnumDevices", res);
        return JNI_FALSE;
    }
    return JNI_TRUE;
}

/*
 ******************************************************************************
 * DirectInputDevice
 ******************************************************************************
 */

/*
 * Class:     org_java_games_input_DirectInputDevice
 * Method:    pollNative
 * Signature: (J[B)Z
 */
extern "C" JNIEXPORT jboolean JNICALL
Java_net_java_games_input_DirectInputDevice_pollNative
    (JNIEnv* env, jobject obj, jlong lDevice, jintArray baData,
        jboolean pollme)
{
    LPDIRECTINPUTDEVICE8 lpDevice = (LPDIRECTINPUTDEVICE8)(long)lDevice;
    // Reacquire the device
    HRESULT res = lpDevice->Acquire();
    if (res != DI_OK && res != S_FALSE) {
        PrintDIError("Acquire", res);
        return JNI_FALSE;
    }
    // Poll the device
    if (pollme == JNI_TRUE) {
        res = lpDevice->Poll();
		// Changed this to FAILED(res) instead of res != DI_OK as it was
		// causeing problems and the dx samples check for FAILED too.
        if ( FAILED(res) ) {
            PrintDIError("Poll", res);
            return JNI_FALSE;
        }
    }
    // Get the device state (data)
    DIJOYSTATE data;
    res = lpDevice->GetDeviceState(sizeof(data), &data);
    if (res != DI_OK) {
        PrintDIError("GetDeviceState", res);
        return JNI_FALSE;
    }
    // Copy the data into the byte array
    env->SetIntArrayRegion(baData, 0, (jsize)(sizeof(data)/4), (jint*)&data);
    return JNI_TRUE;
}

/*
 * Enumeration callback for device objects
 *
 * returns DIENUM_CONTINUE or DIENUM_STOP
 */
BOOL CALLBACK EnumObjectsCallback(LPCDIDEVICEOBJECTINSTANCE lpddoi,
    LPVOID pvRef)
{
    ObjectParamData* pData = (ObjectParamData*)pvRef;
    LPDIRECTINPUTDEVICE8 lpDevice = pData->lpDevice;
    JNIEnv* env = pData->env;
    jobject obj = pData->obj;
    jobject list = pData->list;
    jobject identifier = NULL;
	char ffEnabled = 0;

    HRESULT res;
    if (lpddoi->guidType == GUID_XAxis) {
        identifier = env->GetStaticObjectField(CLASS_AxisIdentifier, FID_X);
    } else if (lpddoi->guidType == GUID_YAxis) {
        identifier = env->GetStaticObjectField(CLASS_AxisIdentifier, FID_Y);
    } else if (lpddoi->guidType == GUID_ZAxis) {
        identifier = env->GetStaticObjectField(CLASS_AxisIdentifier, FID_Z);
    } else if (lpddoi->guidType == GUID_RxAxis) {
        identifier = env->GetStaticObjectField(CLASS_AxisIdentifier, FID_RX);
    } else if (lpddoi->guidType == GUID_RyAxis) {
        identifier = env->GetStaticObjectField(CLASS_AxisIdentifier, FID_RY);
    } else if (lpddoi->guidType == GUID_RzAxis) {
        identifier = env->GetStaticObjectField(CLASS_AxisIdentifier, FID_RZ);
    } else if (lpddoi->guidType == GUID_Slider) {
        identifier = env->GetStaticObjectField(CLASS_AxisIdentifier, FID_Slider);
    } else if (lpddoi->guidType == GUID_Button) {
        identifier = env->GetStaticObjectField(CLASS_AxisIdentifier, FID_Button);
    } else if (lpddoi->guidType == GUID_POV) {
        identifier = env->GetStaticObjectField(CLASS_AxisIdentifier, FID_POV);
    } else {
        // Do not add this axis into the list, since we don't know what it is
        return DIENUM_CONTINUE;
    }
    if (identifier == NULL) {
        return DIENUM_STOP;
    }
    if (DIDFT_GETTYPE(lpddoi->dwType)&DIDFT_AXIS){
        // set axis range
        DIPROPRANGE  joy_axis_range;
        joy_axis_range.lMin = -32768;
        joy_axis_range.lMax = 32768;
        joy_axis_range.diph.dwSize=sizeof(DIPROPRANGE);
        joy_axis_range.diph.dwHeaderSize=sizeof(DIPROPHEADER);
        joy_axis_range.diph.dwHow = DIPH_BYID;
        joy_axis_range.diph.dwObj=lpddoi->dwType;
        if (FAILED(
              res=lpDevice->SetProperty(DIPROP_RANGE,&joy_axis_range.diph))){
            PrintDIError("SetProperty", res);
        }

		//check if this axis is ff enabled
		if( (lpddoi->dwFlags & DIDOI_FFACTUATOR) != 0 ) {
			//printf("%s is ff enabled\n", lpddoi->tszName);
			ffEnabled = 1;
		}
    }

	jint didft = (jint)lpddoi->dwType;
    jstring name = env->NewStringUTF(lpddoi->tszName);
    // Add the axis into our list
    env->CallVoidMethod(obj, MID_AddAxis, list, identifier, didft,
        name);

	if(ffEnabled) {
		// This application needs only one effect: Applying raw forces.
		DWORD           rgdwAxes;
		LONG            rglDirection = 0;
		if(lpddoi->guidType == GUID_XAxis) {
			//printf("effect is in the x axis\n");
			rgdwAxes = DIJOFS_X;
		} else if(lpddoi->guidType == GUID_YAxis) {
			//printf("effect is in the y axis\n");
			rgdwAxes = DIJOFS_Y;
		} else if(lpddoi->guidType == GUID_ZAxis) {
			//printf("effect is in the z axis\n");
			rgdwAxes = DIJOFS_Z;
		}

		DICONSTANTFORCE cf              = { DI_FFNOMINALMAX };

		DIEFFECT eff;
		ZeroMemory( &eff, sizeof(eff) );
		eff.dwSize                  = sizeof(DIEFFECT);
		eff.dwFlags                 = DIEFF_CARTESIAN | DIEFF_OBJECTOFFSETS;
		eff.dwDuration              = INFINITE;
		eff.dwSamplePeriod          = 0;
		eff.dwGain                  = DI_FFNOMINALMAX;
		eff.dwTriggerButton         = DIEB_NOTRIGGER;
		eff.dwTriggerRepeatInterval = 0;
		eff.cAxes                   = 1;
		eff.rgdwAxes                = &rgdwAxes;
		eff.rglDirection            = &rglDirection;
		eff.lpEnvelope              = 0;
		eff.cbTypeSpecificParams    = sizeof(DICONSTANTFORCE);
		eff.lpvTypeSpecificParams   = &cf;
		eff.dwStartDelay            = 0;
		cf.lMagnitude = (long) (0);

		LPDIRECTINPUTEFFECT g_pEffect;
		// Create the prepared effect
		if( FAILED( res = lpDevice->CreateEffect( GUID_ConstantForce, 
												  &eff, &g_pEffect, NULL ) ) )
		{
			PrintDIError("Create effect", res);
			return res;
		}

		env->CallVoidMethod(obj, MID_AddRumbler, (jlong)(long)g_pEffect, identifier, name);
	}
    return DIENUM_CONTINUE;
}

/*
 * Class:    org_java_games_input_DirectInputRumbler
 * Method    setRumble
 * Signature (JF)Z
 */
extern "C" JNIEXPORT jboolean JNICALL
Java_net_java_games_input_DirectInputRumbler_setRumble
    (JNIEnv *env, jobject obj, jlong effect, jfloat value)
{
	LPDIRECTINPUTEFFECT g_pEffect = (LPDIRECTINPUTEFFECT)(long)effect;
	float force = (float)value;
	HRESULT hr;

	DICONSTANTFORCE cf              = { DI_FFNOMINALMAX };

	DIEFFECT eff;
	ZeroMemory( &eff, sizeof(eff) );
	eff.dwSize                  = sizeof(DIEFFECT);
	eff.cbTypeSpecificParams    = sizeof(DICONSTANTFORCE);
	eff.lpvTypeSpecificParams   = &cf;
	cf.lMagnitude = (long) (((float)DI_FFNOMINALMAX)*force);

	//printf("force: %f, mag: %d\n", force, cf.lMagnitude);

	hr = g_pEffect->SetParameters( &eff, DIEP_TYPESPECIFICPARAMS );
	if (FAILED(hr)) {
		PrintDIError("set parameters", hr);
		return hr;
	}

	if(force!=0) {
		hr = g_pEffect->Start(1,0);
		if (FAILED(hr)) {
			PrintDIError("start", hr);
		    return hr;
		}
	} else {
		hr = g_pEffect->Stop();
		if (FAILED(hr)) {
			PrintDIError("stop", hr);
		    return hr;
		}
	}
}

/*
 * Class:     org_java_games_input_DirectInputDevice
 * Method:    enumObjects
 * Signature: (JLjava/util/ArrayList;)Z
 */
extern "C" JNIEXPORT jboolean JNICALL
Java_net_java_games_input_DirectInputDevice_enumObjects
    (JNIEnv* env, jobject obj, jlong lDevice, jobject list)
{
    LPDIRECTINPUTDEVICE8 lpDevice = (LPDIRECTINPUTDEVICE8)(long)lDevice;
    ObjectParamData data(lpDevice, env, obj, list);
    LPVOID pvRef = (LPVOID)&data;
    DWORD dwFlags = DIDFT_ALL;
    // Enum objects
    HRESULT res = lpDevice->EnumObjects(EnumObjectsCallback, pvRef, dwFlags);
    if (res != DI_OK) {
        PrintDIError("EnumObjects", res);
        return JNI_FALSE;
    }
    return JNI_TRUE;
}

/*
 ******************************************************************************
 * DirectInputKeyboard
 ******************************************************************************
 */

/*
 * Class:     org_java_games_input_DirectInputKeyboard
 * Method:    pollNative
 * Signature: (J[B)Z
 */
extern "C" JNIEXPORT jboolean JNICALL
Java_net_java_games_input_DirectInputKeyboard_pollNative
    (JNIEnv* env, jobject obj, jlong lDevice, jbyteArray baData)
{
    LPDIRECTINPUTDEVICE8 lpDevice = (LPDIRECTINPUTDEVICE8)(long)lDevice;
    // Reacquire the device
    HRESULT res = lpDevice->Acquire();
    if (res != DI_OK && res != S_FALSE) {
        PrintDIError("Acquire", res);
        return JNI_FALSE;
    }
    // Get the device state (data)
    char data[256];
    res = lpDevice->GetDeviceState(sizeof(data), data);
    if (res != DI_OK) {
        PrintDIError("GetDeviceState", res);
        return JNI_FALSE;
    }
    env->SetByteArrayRegion(baData, 0, (jsize)sizeof(data), (jbyte*)&data);
    return JNI_TRUE;
}

/*
 * Enumeration callback to rename keyboard keys
 *
 * returns DIENUM_CONTINUE or DIENUM_STOP
 */
BOOL CALLBACK RenameKeysCallback(LPCDIDEVICEOBJECTINSTANCE lpddoi,
    LPVOID pvRef)
{
    ObjectParamData* pData = (ObjectParamData*)pvRef;
    //LPDIRECTINPUTDEVICE8 lpDevice = pData->lpDevice;
    JNIEnv* env = pData->env;
    jobject obj = pData->obj;
    jint index = (jint)lpddoi->dwOfs;
    jstring name = env->NewStringUTF(lpddoi->tszName);
    env->CallVoidMethod(obj, MID_RenameKey, index, name);
    return DIENUM_CONTINUE;
}

/*
 * Class:     org_java_games_input_DirectInputKeyboard
 * Method:    renameKeys
 * Signature: (J)Z
 */
extern "C" JNIEXPORT jboolean JNICALL
Java_net_java_games_input_DirectInputKeyboard_renameKeys
    (JNIEnv* env, jobject obj, jlong lDevice)
{
    LPDIRECTINPUTDEVICE8 lpDevice = (LPDIRECTINPUTDEVICE8)(long)lDevice;
    ObjectParamData data(lpDevice, env, obj, NULL);
    LPVOID pvRef = (LPVOID)&data;
    DWORD dwFlags = DIDFT_ALL;
    // Enum objects
    HRESULT res = lpDevice->EnumObjects(RenameKeysCallback, pvRef, dwFlags);
    if (res != DI_OK) {
        PrintDIError("EnumObjects", res);
        return JNI_FALSE;
    }
    return JNI_TRUE;
}

/*
 ******************************************************************************
 * DirectInputMouse
 ******************************************************************************
 */

/*
 * Class: org_java_games_input_DirectInputMouse
 * Method: getNumAxes
 * Signature: (J)I
 */
extern "C" JNIEXPORT jint JNICALL
Java_net_java_games_input_DirectInputMouse_getNumAxes
    (JNIEnv* env, jobject obj, jlong lDevice)
{
    LPDIRECTINPUTDEVICE8 lpDevice = (LPDIRECTINPUTDEVICE8)(long)lDevice;
    // Reacquire the device
    HRESULT res = lpDevice->Acquire();
    if (res != DI_OK && res != S_FALSE) {
        PrintDIError("Acquire", res);
        return 0;
    }
    DIDEVCAPS deviceCaps;
    // Allocate space for all the device's objects (axes, buttons, POVS)
    ZeroMemory( &deviceCaps, sizeof(DIDEVCAPS) );
    deviceCaps.dwSize = sizeof(DIDEVCAPS);
    res = lpDevice->GetCapabilities(&deviceCaps);
    if(res != DI_OK) {
        PrintDIError("GetCapabilities", res);
        return JNI_FALSE;
    }
    return deviceCaps.dwAxes;
}

/*
 * Class: org_java_games_input_DirectInputMouse
 * Method: getNumButtons
 * Signature: (J)I
 */
extern "C" JNIEXPORT jint JNICALL
Java_net_java_games_input_DirectInputMouse_getNumButtons
    (JNIEnv* env, jobject obj, jlong lDevice)
{
    LPDIRECTINPUTDEVICE8 lpDevice = (LPDIRECTINPUTDEVICE8)(long)lDevice;
    // Reacquire the device
    HRESULT res = lpDevice->Acquire();
    if (res != DI_OK && res != S_FALSE) {
        PrintDIError("Acquire", res);
        return 0;
    }
    DIDEVCAPS deviceCaps;
    // Allocate space for all the device's objects (axes, buttons, POVS)
    ZeroMemory( &deviceCaps, sizeof(DIDEVCAPS) );
    deviceCaps.dwSize = sizeof(DIDEVCAPS);
    res = lpDevice->GetCapabilities(&deviceCaps);
    if(res != DI_OK) {
        PrintDIError("GetCapabilities", res);
        return JNI_FALSE;
    }
    return deviceCaps.dwButtons;
}

/*
 * Class:     org_java_games_input_DirectInputMouse
 * Method:    pollNative
 * Signature: (J[B)Z
 */
extern "C" JNIEXPORT jboolean JNICALL
Java_net_java_games_input_DirectInputMouse_pollNative
    (JNIEnv* env, jobject obj, jlong lDevice, jbyteArray baData)
{
    LPDIRECTINPUTDEVICE8 lpDevice = (LPDIRECTINPUTDEVICE8)(long)lDevice;
    // Reacquire the device
    HRESULT res = lpDevice->Acquire();
    if (res != DI_OK && res != S_FALSE) {
        PrintDIError("Acquire", res);
        return JNI_FALSE;
    }
    // Get the data
    DIMOUSESTATE2 data;
    res = lpDevice->GetDeviceState(sizeof(data), &data);
    if (res != DI_OK) {
        PrintDIError("GetDeviceState", res);
        return JNI_FALSE;
    }
    
    // Endolf woz here
    // Set the axis data to 0, we only want the buttons for this second
    data.lX = 0;
    data.lY = 0;
    data.lZ = 0;

    DIDEVICEOBJECTDATA dataBuffer[MOUSE_BUFFER_SIZE];
    DWORD numEvents = MOUSE_BUFFER_SIZE;
    HRESULT res2 = lpDevice->GetDeviceData(sizeof(DIDEVICEOBJECTDATA), dataBuffer, &numEvents, 0);
    switch(res2) {
        case DIERR_INPUTLOST:
            printf("DIERR_INPUTLOST\n");
            break;
        case DIERR_INVALIDPARAM:
            printf("DIERR_INVALIDPARAM\n");
            break;
        case DIERR_NOTACQUIRED:
            printf("DIERR_NOTACQUIRED\n");
            break;
        case DIERR_NOTBUFFERED:
            printf("DIERR_NOTBUFFERED\n");
            break;
        case DIERR_NOTINITIALIZED:
            printf("DIERR_NOTINITIALIZED\n");
            break;
        case DI_BUFFEROVERFLOW:
            printf("DI_BUFFEROVERFLOW\n");
            break;
    }
    int i=0;
    for(i=0;i<numEvents;i++) {
      switch(dataBuffer[i].dwOfs) {
          case DIMOFS_BUTTON0:
              if(dataBuffer[i].dwData == 0x80) {
                  data.rgbButtons[0] = 0x80;
              }
              break;
          case DIMOFS_BUTTON1:
              if(dataBuffer[i].dwData == 0x80) {
                  data.rgbButtons[1] = 0x80;
              }
              break;
          case DIMOFS_BUTTON2:
              if(dataBuffer[i].dwData == 0x80) {
                  data.rgbButtons[2] = 0x80;
              }
              break;
          case DIMOFS_BUTTON3:
              if(dataBuffer[i].dwData == 0x80) {
                  data.rgbButtons[3] = 0x80;
              }
              break;
          case DIMOFS_BUTTON4:
              if(dataBuffer[i].dwData == 0x80) {
                  data.rgbButtons[4] = 0x80;
              }
              break;
          case DIMOFS_BUTTON5:
              if(dataBuffer[i].dwData == 0x80) {
                  data.rgbButtons[5] = 0x80;
              }
              break;
          case DIMOFS_BUTTON6:
              if(dataBuffer[i].dwData == 0x80) {
                  data.rgbButtons[6] = 0x80;
              }
              break;
          case DIMOFS_BUTTON7:
              if(dataBuffer[i].dwData == 0x80) {
                  data.rgbButtons[7] = 0x80;
              }
              break;
          case DIMOFS_X:
              data.lX += dataBuffer[i].dwData;
              break;
          case DIMOFS_Y:
              data.lY += dataBuffer[i].dwData;
              break;
          case DIMOFS_Z:
              data.lZ += dataBuffer[i].dwData;
              break;
          default:
              printf("Uknown data offset (%d)\n", dataBuffer[i].dwOfs);
      }
    }

	//printf("axis data in native at poll end is x: %d, y: %d, z: %d\n", data.lX, data.lY, data.lZ);

    // Set the data in our array
    env->SetByteArrayRegion(baData, 0, (jsize)sizeof(data), (jbyte*)&data);
    return JNI_TRUE;
}

/*
 * Enumeration callback to rename mouse axes
 *
 * returns DIENUM_CONTINUE or DIENUM_STOP
 */
BOOL CALLBACK RenameAxesCallback(LPCDIDEVICEOBJECTINSTANCE lpddoi,
    LPVOID pvRef)
{
    ObjectParamData* pData = (ObjectParamData*)pvRef;
    //LPDIRECTINPUTDEVICE8 lpDevice = pData->lpDevice;
    JNIEnv* env = pData->env;
    jobject obj = pData->obj;
    jobject identifier;
    switch (lpddoi->dwOfs) {
        case DIMOFS_X:
            identifier = env->GetStaticObjectField(CLASS_AxisIdentifier,
                FID_X);
            break;
        case DIMOFS_Y:
            identifier = env->GetStaticObjectField(CLASS_AxisIdentifier,
                FID_Y);
            break;
        case DIMOFS_Z:
            identifier = env->GetStaticObjectField(CLASS_AxisIdentifier,
                FID_Slider);
            break;
        case DIMOFS_BUTTON0:
            identifier = env->GetStaticObjectField(CLASS_ButtonIdentifier,
                FID_Left);
            break;
        case DIMOFS_BUTTON1:
            identifier = env->GetStaticObjectField(CLASS_ButtonIdentifier,
                FID_Right);
            break;
        case DIMOFS_BUTTON2:
            identifier = env->GetStaticObjectField(CLASS_ButtonIdentifier,
                FID_Middle);
            break;
        case DIMOFS_BUTTON3:
            identifier = env->GetStaticObjectField(CLASS_ButtonIdentifier,
                FID_Side);
            break;
        case DIMOFS_BUTTON4:
            identifier = env->GetStaticObjectField(CLASS_ButtonIdentifier,
                FID_Extra);
            break;
        case DIMOFS_BUTTON5:
            identifier = env->GetStaticObjectField(CLASS_ButtonIdentifier,
                FID_Forward);
            break;
        case DIMOFS_BUTTON6:
            identifier = env->GetStaticObjectField(CLASS_ButtonIdentifier,
                FID_Back);
            break;
        default:
            return DIENUM_CONTINUE; // Not an axis we know
    }
    jstring name = env->NewStringUTF(lpddoi->tszName);
    env->CallVoidMethod(obj, MID_RenameAxis, identifier, name);
    return DIENUM_CONTINUE;
}

/*
 * Class:     org_java_games_input_DirectInputMouse
 * Method:    renameAxes
 * Signature: (J)Z
 */
extern "C" JNIEXPORT jboolean JNICALL
Java_net_java_games_input_DirectInputMouse_renameAxes
    (JNIEnv* env, jobject obj, jlong lDevice)
{
    LPDIRECTINPUTDEVICE8 lpDevice = (LPDIRECTINPUTDEVICE8)(long)lDevice;
    ObjectParamData data(lpDevice, env, obj, NULL);
    LPVOID pvRef = (LPVOID)&data;
    DWORD dwFlags = DIDFT_ALL;
    // Enum objects
    HRESULT res = lpDevice->EnumObjects(RenameAxesCallback, pvRef, dwFlags);
    if (res != DI_OK) {
        PrintDIError("EnumObjects", res);
        return JNI_FALSE;
    }
    return JNI_TRUE;
}


