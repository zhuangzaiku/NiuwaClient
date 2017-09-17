package com.smart.loginsharesdk.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: xiaoht
 * Date: 14-10-28
 * Time: 上午10:28
 * To change this template use File | Settings | File Templates.
 */
public class StringUtils {


    public static boolean isMobileNO(String mobiles){
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean isEmail(String email){
        String str="^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();

    }

    public static String getLoginType (String u) {
        if (StringUtils.isMobileNO(u)) {
            return "3";
        }
        if (StringUtils.isEmail(u)) {
            return "2";
        }
        return "1";
    }


}

