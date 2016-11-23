package com.easyeat.util;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * Created by chenglongwei on 11/22/16.
 */

public class UniqueDeviceIDFactory {
    public static String getUniqueDeviceID(Context context) {
        //imei
        String imei = getIMEI(context);
        String id = imei;
        //android_id
        String androidID = getAndroidID(context);
        id += androidID;
        String pseudoUniqueID = getPseudoUniqueID();
        id += pseudoUniqueID;
        id = Md5Util.encrypt(id);
        return id;
    }


    /**
     * <p>
     * Pseudo-Unique ID
     * </p>
     * <p>
     * 这个在任何Android手机中都有效。有一些特殊的情况，一些如平板电脑的设置没有通话功能，
     * 或者你不愿加入READ_PHONE_STATE许可。而你仍然想获得唯一序列号之类的东西。这时你可以通过取出ROM版本、
     * 制造商、CPU型号、以及其他硬件信息来实现这一点。这样计算出来的ID不是唯一的（因为如果两个手机应用了同样的硬件
     * 以及Rom 镜像）。但是，出现类似情况的可能性基本可以忽略。
     * </p>
     *
     * @return
     */
    private static String getPseudoUniqueID() {
        //we make this look like a valid IMEI
        String id = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10
                + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10
                + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10
                + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 + Build.TYPE.length() % 10
                + Build.USER.length() % 10; //13 digits
        return id;
    }

    /**
     * <p>
     * The Android ID  , 通常被认为不可信，因为它有时为null。开发文档中说明了：这个ID会改变如果进行了出厂设置。
     * 并且，如果某个Android手机被Root过的话，这个ID也可以被任意改变。
     * </p>
     *
     * @return
     */
    private static String getAndroidID(Context context) {
        try {
            String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (androidID == null)
                androidID = "";
            return androidID;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * <p>
     * IMEI(International Mobile Equipment Identity)是国际移动设备身份码的缩写，国际移动装备辨识码，
     * 是由15位数字组成的"电子串号"，它与每台手机一一对应，而且该码是全世界唯一的。
     * 每一只手机在组装完成后都将被赋予一个全球唯一的一组号码，这个号码从生产到交付使用都将被制造生产的厂商所记录。
     * </p>
     * <p>
     * 需要权限android.permission.READ_PHONE_STATE
     * </p>
     * <p>
     * 某些杂牌水货手机可能有无效的IMEI，如：000000000000000，或者带*号
     * </p>
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            if ("000000000000000".equals(imei))
                return "";
            if (imei != null && imei.indexOf('*') >= 0)
                return "";
            if (imei == null)
                imei = "";
            return imei;
        } catch (Exception e) {
            //ignore
            return "";
        }
    }
}
