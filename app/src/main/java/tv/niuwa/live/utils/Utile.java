package tv.niuwa.live.utils;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/12/12.
 * Author: XuDeLong
 */
public class Utile {
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
}
