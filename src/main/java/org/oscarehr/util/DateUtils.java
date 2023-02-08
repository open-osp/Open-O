/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

public final class DateUtils {
    public static final String JS_ISO_DATE_FORMAT = "yy-mm-dd";

    public DateUtils() {
    }

    public static String getIsoDateTimeNoTNoSeconds(Calendar cal) {
        if (cal == null) {
            return "";
        } else {
            String s = getIsoDateTimeNoT(cal);
            return s.substring(0, s.length() - 3);
        }
    }

    public static String getIsoDateTimeNoT(Calendar cal) {
        return cal == null ? "" : DateFormatUtils.ISO_DATETIME_FORMAT.format(cal).replace('T', ' ');
    }

    public static String getIsoDateTime(Calendar cal) {
        return cal == null ? "" : DateFormatUtils.ISO_DATETIME_FORMAT.format(cal);
    }

    public static String getIsoDate(Calendar cal) {
        return cal == null ? "" : DateFormatUtils.ISO_DATE_FORMAT.format(cal);
    }

    public static Date parseIsoDate(String s) throws ParseException {
        s = StringUtils.trimToNull(s);
        if (s == null) {
            return null;
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormatUtils.ISO_DATE_FORMAT.getPattern());
            Date date = dateFormat.parse(s);
            return date;
        }
    }

    public static GregorianCalendar parseIsoDateAsCalendar(String s) throws ParseException {
        Date date = parseIsoDate(s);
        if (date == null) {
            return null;
        } else {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            cal.getTimeInMillis();
            return cal;
        }
    }

    public static Date parseIsoDateTime(String s) throws ParseException {
        s = StringUtils.trimToNull(s);
        if (s == null) {
            return null;
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormatUtils.ISO_DATETIME_FORMAT.getPattern());
            Date date = dateFormat.parse(s);
            return date;
        }
    }

    public static GregorianCalendar parseIsoDateTimeAsCalendar(String s) throws ParseException {
        Date date = parseIsoDateTime(s);
        if (date == null) {
            return null;
        } else {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            cal.getTimeInMillis();
            return cal;
        }
    }

    public static Integer yearDifference(Calendar date1, Calendar date2) {
        if (date1 != null && date2 != null) {
            int yearDiff = date2.get(1) - date1.get(1);
            if (date2.get(6) > date1.get(6)) {
                --yearDiff;
            }

            return yearDiff;
        } else {
            return null;
        }
    }

    public static Integer getAge(Calendar dateOfBirth, Calendar onThisDay) {
        return yearDifference(dateOfBirth, onThisDay);
    }

    public static Calendar setToBeginningOfDay(Calendar cal) {
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        cal.getTimeInMillis();
        return cal;
    }

    public static Date parseJsIsoDateTimeNoTNoSeconds(String s) throws ParseException {
        s = StringUtils.trimToNull(s);
        if (s == null) {
            return null;
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = dateFormat.parse(s);
            return date;
        }
    }
}
