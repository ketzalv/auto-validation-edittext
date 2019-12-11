package io.github.ketzalv.validationedittext;

import java.text.NumberFormat;
import java.util.Locale;

public class ValidationUtils {

    public static String cashFormat(Locale locale, double number) {
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        try {
            return format.format(number);
        } catch (Exception e) {
            return String.valueOf(number);
        }
    }

    public static String cashFormat(Locale locale, String number) {
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        try {
            double cast = Double.valueOf(number);
            return format.format(cast);
        } catch (Exception e) {
            return number;
        }
    }

    public static String cashFormat(Locale locale, int number) {
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        try {
            return format.format(number);
        } catch (Exception e) {
            return String.valueOf(number);
        }
    }

    public static String cashFormat(Locale locale, float number) {
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        try {
            return format.format(number);
        } catch (Exception e) {
            return String.valueOf(number);
        }
    }

    public static double parseCurrencyAmount(String aMount) {
        try {
            String cleanString = aMount.replaceAll("[^\\d.]", "").replaceAll("\\.", "");
            return Double.parseDouble(cleanString);
        } catch (Exception e) {
            return 0;
        }
    }

    public static double parseCurrencyAmountWithoutDecimal(String aMount) {
        try {
            String cleanString = aMount.replaceAll("[^\\d.]", "");
            String[] splitedAmount = cleanString.split("\\.");
            if(splitedAmount.length == 2){
                String mount = splitedAmount[0];
                String decimals = splitedAmount[1];
                if(decimals.length() == 1){
                    mount = splitedAmount[0].replaceFirst(".$","");
                }
                String cleanDecimals = decimals.replaceFirst("0", "").replaceFirst("0", "");

                return Double.parseDouble(mount + cleanDecimals);
            }
            return Double.parseDouble(cleanString);
        } catch (Exception e) {
            return 0;
        }
    }

    public static String parseCurrencyAmountString(String aMount) {
        try {
            double doubleMount = parseCurrencyAmount(aMount) / 100;
            return String.valueOf(doubleMount);
        } catch (Exception e) {
            return aMount;
        }
    }
}

