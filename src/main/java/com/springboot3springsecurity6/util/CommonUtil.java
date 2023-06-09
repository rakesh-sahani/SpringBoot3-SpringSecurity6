package com.springboot3springsecurity6.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {

    public static boolean isSupportedExtension(String extension) {
        return extension != null && (
                extension.equals("txt") || extension.equals("csv"));
    }

    public static boolean isEmail(String email) {
        if (email.contains("@")) {
            return true;
        }
        return false;
    }

    public static boolean isMobile(String mobile) {
        Pattern mobilePattern = Pattern.compile("^\\d{10}$");
        Matcher numberSize = mobilePattern.matcher(mobile);
        if (numberSize.matches()) {
            return true;
        }
        return false;
    }

    public static String dateStringDDMMMYYYY(Date date) {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");
        String today = DATE_FORMAT.format(date).toUpperCase();
        return today;
    }

    public static String timeString(Date date) {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("hh:mm a");
        String today = DATE_FORMAT.format(date).toUpperCase();
        return today;
    }

    public static String dateStringDDMMYYYY(Date date) {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
        String today = DATE_FORMAT.format(date);
        return today;
    }

    public static String dateFormatYYYYMMDD(Date date) throws ParseException {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
        String convertDate = DATE_FORMAT.format(date);
        return convertDate;
    }

    public static Date dateFormatYYYYMMDD(String date) throws ParseException {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        Date convertDate = DATE_FORMAT.parse(date);
        return convertDate;
    }

    public static Date dateFormatDDMMYYYY(String date) throws ParseException {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
        Date convertDate = DATE_FORMAT.parse(date);
        return convertDate;
    }
}