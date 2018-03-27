package com.im.smarto.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Anatoly Chernyshev on 26.03.2018.
 */

public class DateUtils {

    private static final String TAG = DateUtils.class.getSimpleName();

    public static String convertToISOFormat(int year, int month, int day) {
        String y = String.valueOf(year);
        String m = String.valueOf(month+1);
        String d = String.valueOf(day);

        if ((month + 1) < 10) m = "0" + m;
        if (day < 10) d = "0" + d;

        Log.i(TAG, y + "-" + m + "-" + d);
        return y + "-" + m + "-" + d;
    }

    public static String convertToISOFormat(int hours, int minutes) {
        String h = String.valueOf(hours);
        String m = String.valueOf(minutes);

        if (hours < 10) h = "0" + h;
        if (minutes < 10) m = "0" + m;

        Log.i(TAG, h + ":" + m);
        return h + ":" + m;
    }

    public static String checkDate(String ISOFormat) {

        Log.i(TAG, "checkDate() - " + ISOFormat);

        String result = "";

        String year = ISOFormat.split(" ")[0].substring(0, 4);
        String month = ISOFormat.split(" ")[0].substring(5, 7);
        String day = ISOFormat.split(" ")[0].substring(8, 10);

        String hours = ISOFormat.split(" ")[1].substring(0, 2);
        String minutes = ISOFormat.split(" ")[1].substring(3, 5);

        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        String formattedDate = df.format(currentDate);

        Log.i(TAG, "Year: " + year + "\n"
                    + "Month: " + month + "\n"
                    + "Day: " + day + "\n"
                    + "Hours: " + hours + "\n"
                    + "Minutes: " + minutes);

        Log.i(TAG, "Current date: " + formattedDate);

        switch (month){
            case "01": result = day + " Jan " + year;
                break;
            case "02": result = day + " Feb " + year;
                break;
            case "03": result = day + " Mar " + year;
                break;
            case "04": result = day + " Apr " + year;
                break;
            case "05": result = day + " May " + year;
                break;
            case "06": result = day + " June " + year;
                break;
            case "07": result = day + " Jul " + year;
                break;
            case "08": result = day + " Aug " + year;
                break;
            case "09": result = day + " Sept " + year;
                break;
            case "10": result = day + " Oct " + year;
                break;
            case "11": result = day + " Nov " + year;
                break;
            case "12": result = day + " Dec " + year;
                break;
        }

        if (formattedDate.equals(result)) return hours + ":" + minutes;

        return result.split(" ")[0] + " " + result.split(" ")[1] + ".";
    }

}
