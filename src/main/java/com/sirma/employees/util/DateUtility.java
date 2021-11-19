package com.sirma.employees.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtility {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtility.class);

    public static LocalDate parseDate(String inputDate) {

        LocalDate outputDate = null;
        String[] dateFormats =
                {
                        "yyyy-MM-dd",
                        "yyyyMMdd",
                        "dd/MM/yy",
                        "dd/MM/yyyy",
                        "dd MMM yyyy",
                        "dd MMMM yyyy",
                        "yyyy.MM.dd G 'at' HH:mm:ss z",
                        "EEE, MMM d, ''yy",
                        "yyyyy.MMMMM.dd GGG hh:mm aaa",
                        "EEE, d MMM yyyy HH:mm:ss Z",
                        "yyMMddHHmmssZ",
                        "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                        "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                        "YYYY-'W'ww-u",
                        "EEE, dd MMM yyyy HH:mm:ss z",
                        "EEE, dd MMM yyyy HH:mm zzzz",
                        "yyyy-MM-dd'T'HH:mm:ssZ",
                        "yyyy-MM-dd'T'HH:mm:ss.SSSzzzz",
                        "yyyy-MM-dd'T'HH:mm:sszzzz",
                        "yyyy-MM-dd'T'HH:mm:ss z",
                        "yyyy-MM-dd'T'HH:mm:ssz",
                        "yyyy-MM-dd'T'HH:mm:ss",
                        "yyyy-MM-dd'T'HHmmss.SSSz",
                };
        for (String format : dateFormats) {
            try {
                outputDate = LocalDate.parse(inputDate, DateTimeFormatter.ofPattern(format));
                LOGGER.info(inputDate + " parses to " + outputDate);
                break;
            } catch (RuntimeException ex) {
//                LOGGER.warn(ex.getMessage());
            }
        }
        return outputDate;
    }
}
