package com.acme.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * For easier dates management
 */
public class DateUtility {

    /**
     * Creates the date object in MM-dd-YYYY format
     * @return
     */
    public Date create(int month, int day, int year){
        GregorianCalendar calendar;
        calendar = new GregorianCalendar(year,month,day );
        Date date = calendar.getTime();
        return date;
    }

    /**
     * Creates the date object with provided parameters
     * @return
     */
    public Date create(int month, int day, int year, int hour, int minute){
        GregorianCalendar calendar;
        calendar = new GregorianCalendar(year,month,day, hour, minute, 0 );
        Date date = calendar.getTime();
        return date;
    }

    /**
     * Create the string representation in format MM-dd-YYYY
     */
    public String formatDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        return sdf.format(date);
    }
}
