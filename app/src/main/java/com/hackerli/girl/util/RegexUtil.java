package com.hackerli.girl.util;

import android.support.annotation.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lijianxin on 2018/3/2.
 */

public class RegexUtil {
    public static @Nullable
    String parse(String pattern, String src, int index) {
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(src);
        if (matcher.find()) {
            return matcher.group(index);
        }
        return null;
    }
}
