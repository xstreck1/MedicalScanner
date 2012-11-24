package com.gearfish.medicalscanner;

/**
 * This class is taken from the original project.
 * 
 * This class provides a wrapper for launching zbar library function
 * that implements qr code recognition
 */
class Zbar {
    static {
	System.loadLibrary("zbar");
    }

    public native String process(int width, int height, byte[] imgData);
}
