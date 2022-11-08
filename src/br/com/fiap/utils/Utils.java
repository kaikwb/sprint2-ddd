package br.com.fiap.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    private static final String db_date_format = "dd-MM-yyyy HH:mm:ss";

    public static String dateToDbDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(db_date_format);
        return formatter.format(date);
    }

    public static Date dateFromDbFormat(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(db_date_format);
        return date != null ? formatter.parse(date) : null;
    }
}
