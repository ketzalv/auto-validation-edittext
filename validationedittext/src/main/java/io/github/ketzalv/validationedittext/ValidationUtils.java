package io.github.ketzalv.validationedittext;

import java.text.NumberFormat;
import java.util.Locale;

public class ValidationUtils {
    public static final Locale mexicoLocale = new Locale("es", "MX");

    public static String cashFormat(double number) {
        NumberFormat format = NumberFormat.getCurrencyInstance(mexicoLocale);
        try {
            return format.format(number);
        } catch (Exception e) {
            return String.valueOf(number);
        }
    }

    public static String cashFormat(String number) {
        NumberFormat format = NumberFormat.getCurrencyInstance(mexicoLocale);
        try {
            double cast = Double.valueOf(number);
            return format.format(cast);
        } catch (Exception e) {
            return number;
        }
    }

    public static String cashFormat(int number) {
        NumberFormat format = NumberFormat.getCurrencyInstance(mexicoLocale);
        try {
            return format.format(number);
        } catch (Exception e) {
            return String.valueOf(number);
        }
    }

    public static String cashFormat(float number) {
        NumberFormat format = NumberFormat.getCurrencyInstance(mexicoLocale);
        try {
            return format.format(number);
        } catch (Exception e) {
            return String.valueOf(number);
        }
    }

    public static double parseCurrencyAmount(String aMount) {
        try {
            String cleanString = aMount.replaceAll("[$,.]", "");
            return Double.parseDouble(cleanString);
        } catch (Exception e) {
            return 0;
        }
    }

    public static double parseCurrencyAmountWithoutDecimal(String aMount) {
        try {
            String cleanString = aMount.replaceAll("[$,]", "");
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

