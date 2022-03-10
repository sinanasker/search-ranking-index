package com.sellics.casestudy.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LoaderUtil {
    public String getKey(String keyword, String asin){
        return  keyword + asin;
    }
}
