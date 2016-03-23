package util;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by bgm on 3/16/2016 AD.
 */
public class DNService {

    public static String BaseDNPath = "/sdcard/";
    public static String DNFile = BaseDNPath + "dn.txt";

    public static String process(Activity activity) throws IOException {
        U.d("Start DN process");
        String dnLine = DNService.getDNLine(activity);
        U.d(DNFile + "=" + dnLine);
        DNService.write(dnLine);
        return dnLine;
    }

    public static String getDNLine(Activity activity) throws SocketException {
        String s = "DATA DNApp|DN_INFO|";
        String ip = "IP=" + getIP();
        String msisdn = ",MSISDN=" + getTelNo(activity);
        String imei = ",IMEI=" + getIMEI(activity);
//        String lat = ",LAT="+getLat(activity);
//        String lat = ",LAT=" + "10.00";
//        String lng = ",LONG=" + "1000.433";
        String mcc = ",MCCMNC=" + getMCCMNC(activity);
        String simOperatorName = ",SIMOPERATORNAME=" + getSimOperatorName(activity);
        String networkOperatorName = ",NETWORKOPERATORNAME=" + getNetworkOperatorName(activity);
        String lac = ",LAC=" + getLAC(activity);
        String cellId = ",CELLID=" + getCellID(activity);
        String networkType = ",NETWORKTPYE=" + getNetworkType(activity);
        String signal = ",SIGNALSTRENGTH=" + getSignal(activity);
        String signalAsu = ",SIGNALSTRENGTHASU=" + getSignalAsu(activity);

        return s + ip + msisdn + imei + mcc + simOperatorName + networkOperatorName + lac + cellId + networkType + signal + signalAsu;
    }


    public static String getTelNo(Activity activity) {
        TelephonyManager tMgr = (TelephonyManager) activity.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        //String mPhoneNumber = tMgr.getNetworkOperatorName();
        U.d("mPhoneNumber=" + mPhoneNumber);
        return mPhoneNumber;
    }

    public static String getIP() throws SocketException {
        String ip = "";
        for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
            NetworkInterface intf = (NetworkInterface) en.nextElement();
            for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                if (!inetAddress.isLoopbackAddress()) {
                    U.d("IP=" + inetAddress.getHostAddress());
                    ip = inetAddress.getHostAddress();
                }
            }
        }
        return ip;
    }

    public static String getIMEI(Activity activity) {
        TelephonyManager tm = (TelephonyManager) activity.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = tm.getDeviceId();
        U.d("IMEI=" + IMEI);
        return IMEI;
    }

    public static String getLAC(Activity activity) {
        TelephonyManager tm = (TelephonyManager) activity.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
        int lac = location.getLac();
        U.d("LAC=" + lac);
        return lac + "";
    }

    public static String getCellID(Activity activity) {
        TelephonyManager tm = (TelephonyManager) activity.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
        int cid = location.getCid();
        U.d("CellID=" + cid);
        return cid + "";
    }

    public static String getMCCMNC(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telephonyManager.getSimState();
        // Get the operator code of the active SIM (MCC + MNC)
        String simOperatorCode = telephonyManager.getSimOperator();
        U.d("simOperatorCode=" + simOperatorCode);
        return simOperatorCode;
    }

    public static String getSimOperatorName(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telephonyManager.getSimState();
        // Get the name of the SIM operator
        String simOperatorName = telephonyManager.getSimOperatorName();
        U.d("simOperatorName=" + simOperatorName);
        return simOperatorName;
    }

    public static String getNetworkOperatorName(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String networkOperatorName = telephonyManager.getNetworkOperatorName();
        U.d("NetworkOperatorName=" + networkOperatorName);
        return networkOperatorName;
    }


    public static String getNetworkType(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telephonyManager.getSimState();
        String netTypeName = "";
        // Finally get the radio type.
        int type = telephonyManager.getNetworkType();
        if (type == TelephonyManager.NETWORK_TYPE_UMTS) {
            netTypeName = "UMTS";
        } else if (type == TelephonyManager.NETWORK_TYPE_GPRS || type == TelephonyManager.NETWORK_TYPE_EDGE) {
            netTypeName = "GSM";
        } else if (type == TelephonyManager.NETWORK_TYPE_LTE) {
            netTypeName = "LTE";
        } else if (type == TelephonyManager.NETWORK_TYPE_HSDPA) {
            netTypeName = "HSDPA";
        } else if (type == TelephonyManager.NETWORK_TYPE_HSPA) {
            netTypeName = "HSSPA";
        } else if (type == TelephonyManager.NETWORK_TYPE_HSUPA) {
            netTypeName = "HSUPA";
        } else if (type == TelephonyManager.NETWORK_TYPE_HSPAP) {
            netTypeName = "HSPAP";
        } else {
            netTypeName = "" + type;
        }
        U.d("networkType=" + netTypeName);
        return netTypeName;
    }

    public static String getL(Activity activity) {
        GPSHelper g = new GPSHelper(activity.getApplicationContext());
        return g.getLatitude() + "";
    }


    public static String getLat(Activity activity) {
        LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//         final LocationListener locationListener = new LocationListener() {
//            public void onLocationChanged(Location location) {
//                longitude = location.getLongitude();
//                latitude = location.getLatitude();
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//        };
//
//        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);


        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        U.d("longitude=" + longitude);
        U.d("latitude=" + latitude);
        return latitude + "";
    }

    public static String getSignal(Activity activity) {
        return myPhoneStateListener.signalStrengthDbm + "";
    }

    public static String getSignalAsu(Activity activity) {
        return myPhoneStateListener.signalStrengthAsuLevel + "";
    }

    public static String getCellInfo(Activity activity) {
        // myPhoneStateListener pslistener = new myPhoneStateListener();
        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        //telephonyManager.listen(pslistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        List<CellInfo> infos = telephonyManager.getAllCellInfo();
        U.d("cellInfos size=" + telephonyManager.getAllCellInfo().size());
        for (int i = 0; i < infos.size(); i++) {

            CellInfo info = infos.get(i);
            int dbm = 0;
            int asuLevel = 0;

            //d((i + 1) + ". describeContents=" + info.describeContents());
            if (info instanceof CellInfoLte) {
                CellSignalStrengthLte sig = ((CellInfoLte) info).getCellSignalStrength();
                dbm = sig.getDbm();
                asuLevel = sig.getAsuLevel();
            }
            if (info instanceof CellInfoWcdma) {
                CellSignalStrengthWcdma sig = ((CellInfoWcdma) info).getCellSignalStrength();
                dbm = sig.getDbm();
                asuLevel = sig.getAsuLevel();
            }
            if (info instanceof CellInfoCdma) {
                CellSignalStrengthCdma sig = ((CellInfoCdma) info).getCellSignalStrength();
                dbm = sig.getDbm();
                asuLevel = sig.getAsuLevel();
            }
            if (info instanceof CellInfoGsm) {
                CellSignalStrengthGsm sig = ((CellInfoGsm) info).getCellSignalStrength();
                dbm = sig.getDbm();
                asuLevel = sig.getAsuLevel();
            }
            U.d((i + 1) + ". dbm=" + dbm + ", asuLevel=" + asuLevel);


        }
        // String sig = cellSignalStrengthGsm.getDbm() + "";
        //String sig = pslistener.signalStrengthDdm + "";
        String sig = "";
        //d("sig=" + sig);
        return sig;
    }


    //--------------------------------------------------------------------------------
    public static String getCurDateStr() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ");
        return df.format(new Date());
    }

    public static void write(String str) throws IOException {
        String fileStr = DNFile;
        FileUtils.write(new File(fileStr), getCurDateStr() + str);
    }

    public static String read() throws IOException {
        String fileStr = DNFile;
        return FileUtils.readFileToString(new File(fileStr));
    }

}
