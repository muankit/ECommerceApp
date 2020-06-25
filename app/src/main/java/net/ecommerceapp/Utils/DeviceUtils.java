package net.ecommerceapp.Utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class DeviceUtils {

    /**
     *  This method gives you the device's IMEI
     */
    public static String getDeviceIMEI(Context context){
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    /**
     *  This method gives you the device's NAME
     */
    public static String getDeviceName(){
        return android.os.Build.MODEL;
    }

    /**
     *  This method gives you the device's Android version
     */
    public static String getDeviceVersion(){
        return Build.VERSION.RELEASE;
    }

    /**
     *  This method gives you the network name
     */
    /*public static String getNetworkName(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String carrierName = manager.getNetworkOperatorName();

        return carrierName;
    }*/

    /**
     *  Check for rooted device
     */
    public static boolean isDeviceRooted() {
        return checkRoot1() || checkRoot2() || checkRoot3();
    }

    private static boolean checkRoot1() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private static boolean checkRoot2() {
        String[] paths = { "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    private static boolean checkRoot3() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[] { "/system/xbin/which", "su" });
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) return true;
            return false;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }
}
