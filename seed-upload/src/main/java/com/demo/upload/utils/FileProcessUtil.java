package com.demo.upload.utils;

import java.time.LocalDate;
import java.util.UUID;

/**
 * @author wql
 * @desc 文件处理相关工具
 * @date 2020/12/17 10:29
 * @lastUpdateUser
 * @lastUpdateDesc
 * @lastUpdateTime 2020/12/17 10:29
 */
public class FileProcessUtil {

    private final static String IMG_SUFFIX = "jpg,png,gif,tif,bmp,dwg,jpeg";

    public static boolean isImage(String type) {
        return IMG_SUFFIX.contains(type);
    }

    /**
     * 获取年月日[2020, 09, 01]
     *
     * @return String[]
     */
    public static String getDateFolder() {
        String[] retVal = new String[3];

        LocalDate localDate = LocalDate.now();
        retVal[0] = localDate.getYear() + "";

        int month = localDate.getMonthValue();
        retVal[1] = month < 10 ? "0" + month : month + "";

        int day = localDate.getDayOfMonth();
        retVal[2] = day < 10 ? "0" + day : day + "";
        return String.join("/", retVal);
    }

    public static String reName(String suffix) {
        return UUID.randomUUID().toString().replace("-", "") + "." + suffix;
    }

}
