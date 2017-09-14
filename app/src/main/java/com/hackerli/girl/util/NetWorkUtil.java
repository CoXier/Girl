package com.hackerli.girl.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;

/**
 * Created by Administrator on 2016/4/8.
 */
public class NetWorkUtil {
    private NetworkInfo mNetworkInfo;

    public NetWorkUtil(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = manager.getActiveNetworkInfo();
    }


    public boolean isNetConnected() {
        return mNetworkInfo != null && mNetworkInfo.isConnected();
    }

    public String getNetType() {
        return isNetConnected() ? mNetworkInfo.getTypeName() : "NO_INTERNET";
    }

    public
    @Nullable
    String getSubbNetType() {
        String state = getNetType();
        switch (state) {
            case "WIFI":
                return "WIFI";
            case "MOBILE":
                switch (mNetworkInfo.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
                    case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
                    case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        return "MOBILE_2G";
                    case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        return "MOBILE_3G";
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        return "MOBILE_4G";
                    default:
                        return "MOBILE_UNKNOWN";
                }
            case "NO_INTERNET":
                return "NO_INTERNET";
            default:
                return "UNKNOWN";

        }
    }

}
