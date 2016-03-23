package util;

import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;

import com.example.bgm.devicenetworkapp.MainActivity;

/**
 * Created by bgm on 3/17/2016 AD.
 */
public class myPhoneStateListener extends PhoneStateListener {
    public static int signalStrengthDbm = 0;
    public static int signalStrengthAsuLevel=0;

    MainActivity activity;
    public myPhoneStateListener(MainActivity activity) {
        super();
        this.activity = activity;
    }

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        signalStrengthDbm = getSignalStrengthByName(signalStrength, "getDbm");
        signalStrengthAsuLevel = getSignalStrengthByName(signalStrength, "getAsuLevel");
        U.d("signalStrengthDbm="+signalStrengthDbm +", signalStrengthAsuLevel="+signalStrengthAsuLevel);
        activity.processDN();
    }

    private int getSignalStrengthByName(SignalStrength signalStrength, String methodName) {
        try {
            Class classFromName = Class.forName(SignalStrength.class.getName());
            java.lang.reflect.Method method = classFromName.getDeclaredMethod(methodName);
            Object object = method.invoke(signalStrength);
            return (int) object;
        } catch (Exception ex) {
            return -9999;
        }
    }


}
