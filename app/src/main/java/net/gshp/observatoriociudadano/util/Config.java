package net.gshp.observatoriociudadano.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import net.gshp.observatoriociudadano.contextApp.ContextApp;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by gnu on 22/02/17.
 */

public class Config {

    /**
     * @return la zona horaria de donde este configurado el telefono
     */
    public static String getTimeZone() {
        return TimeZone.getDefault().getID();
    }

    /**
     * @return el imei del telefono
     */
    public static String getIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) ContextApp.context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public static boolean checkCameraHardware() {
        if (ContextApp.context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Comprueba el nivel de bateria
     *
     * @return nivel de bateria en porcentaje
     */

    public static float getBatteryLevel() {
        Intent batteryIntent = ContextApp.context.registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        // Error checking that probably isn't needed but I added just in case.
        if (level == -1 || scale == -1) {
            return 50.0f;
        }
        return ((float) level / (float) scale) * 100.0f;
    }

    /**
     * comprueba que este desabilitado la captura de coordenadas falsas
     *
     * @return true if actived
     */
    public static boolean isMockLocation() {
        return !Settings.Secure.getString(ContextApp.context.getContentResolver(),
                Settings.Secure.ALLOW_MOCK_LOCATION).equals("0");
    }

    public static boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) ContextApp.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Comprueba que esten encendidos los provedores de geolocalizacion
     *
     * @return true si estan disponibles, false si estan apagados
     */
    public static boolean isGPSenabled() {
        LocationManager manager = (LocationManager) ContextApp.context
                .getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                && manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public static Calendar getCalendar() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(Config.getTimeZone()), Locale.US);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTimeInMillis(System.currentTimeMillis());
        return cal;
    }


    public static String formatDateFromCurrentMillis(long currentMillis, String format) {

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date resultdate = new Date(currentMillis);
        return sdf.format(resultdate);
    }

    public static String formatDateFromCurrentMillis(String currentMillis, String format) {
        Long curr = 0l;
        try {
            curr = Long.valueOf(currentMillis);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format, new Locale("es", "MX"));
        Date resultdate = new Date(curr);
        return sdf.format(resultdate);
    }

    public static String formatDate(String oldFormat, String newFormat, String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(oldFormat, new Locale("es", "MX"));
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat(newFormat);
        return timeFormat.format(myDate);
    }

    public static String formatDate() {
        SimpleDateFormat format = new SimpleDateFormat("MMMM dd yyyy", new Locale("es", "MX"));
        Date date = new Date();
        return format.format(date);
    }

    /**
     * comrueba que la hora sea automatica
     */
    public static boolean isDateAutomatic() {
        try {
            int opcion = Settings.System.getInt(
                    ContextApp.context.getContentResolver(),
                    Settings.System.AUTO_TIME);
            return opcion == 1;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isDateAutomatic1() {
        try {

            int opcion1 = Settings.System.getInt(
                    ContextApp.context.getContentResolver(),
                    Settings.System.AUTO_TIME_ZONE);
            // AUTO_TIME

            System.out.println("OPCION1 " + opcion1);
            return opcion1 == 1;

        } catch (Settings.SettingNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }


    public static String getPhoneNumber() {
        TelephonyManager tMgr = (TelephonyManager) ContextApp.context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(ContextApp.context, Manifest.permission.READ_SMS) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ContextApp.context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        return tMgr.getLine1Number();
    }

    public static String getBrandDevice() {
        return Build.BRAND;
    }

    public static String getModel() {
        return Build.MODEL;
    }

    public static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static String getOs() {
        return System.getProperty("os.version");
    }

    public static String getVersionApp() {
        PackageInfo pInfo = null;
        try {
            pInfo = ContextApp.context.getPackageManager().getPackageInfo(ContextApp.context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pInfo.versionName;
    }


    public static String formatPriceBigDecimal(String priceIntCent) {

        if (priceIntCent == null) {
            return "NA";
        }
        BigDecimal priceBigDecimal = new BigDecimal(priceIntCent);
        NumberFormat mxnCurrencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));
        mxnCurrencyFormat.setMinimumFractionDigits(2);
        mxnCurrencyFormat.setMaximumFractionDigits(2);
        BigDecimal result = priceBigDecimal.divide(BigDecimal.valueOf(100d));
        return mxnCurrencyFormat.format(priceBigDecimal);
    }


}
