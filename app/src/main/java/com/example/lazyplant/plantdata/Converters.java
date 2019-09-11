package com.example.lazyplant.plantdata;

import androidx.room.TypeConverter;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Converters {

    @TypeConverter
    public static Calendar fromTimestamp(Long value){
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(value);
        return value == null ? null : calendar;
    }

    @TypeConverter
    public static Long dateToTimestamp(Calendar date){
        return date == null ? null : date.getTimeInMillis();
    }

}
