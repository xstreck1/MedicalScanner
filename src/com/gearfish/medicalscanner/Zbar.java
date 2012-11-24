package com.gearfish.medicalscanner;

/**
 * This class provides a wrapper for launching Zbar library function that
 * implements qr code recognition.
 * 
 * This class is taken from the original project OBSQR by Anna Kruglik and Serge
 * Zaitsev. See
 * https://play.google.com/store/apps/details?id=trikita.obsqr&hl=en for
 * details.
 * 
 */
class Zbar {
    static {
	System.loadLibrary("zbar");
    }

    public native String process(int width, int height, byte[] imgData);
}
