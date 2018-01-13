package com.noumi0k.fabulous_number_formatter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by noumi0k on 2017/12/09.
 */

public class FabulousNumberFormatter {

    public static String getRawDecimal(Double number) {
        return getRawDecimal(String.valueOf(number));
    }

    public static String getRawDecimal(String numberString) {
        try {
            String stringWithoutComma = numberString.replaceAll(",", "");
            return new BigDecimal(stringWithoutComma).stripTrailingZeros().toPlainString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String keepSignificantForDecimalInput(int cursorPosition, String numberString) {
        return keepSignificantForDecimalInput(cursorPosition, numberString, 0);
    }

    public static String keepSignificantForDecimalInput(int cursorPosition, String numberString, int maxDecimal) {
        int minDecimal = 0;
        int dotsNumber = getDotsNumber(numberString);
        int lastCharPosition = Math.max(0, numberString.length() - 1);

        // 複数の「.」を打たせない
        if (dotsNumber > 1) {
            return new StringBuilder(numberString).deleteCharAt(cursorPosition - 1).toString();
        }

        String numberStringRaw = getRawDecimal(numberString);

        // 数字でなければ、emptyを返す
        if (numberStringRaw == null || numberStringRaw.isEmpty()) {
            return "";
        }

        // 「0」 で始まる場合、0が消えない & 文字が多くなりすぎないように返す
        if (numberString.indexOf('0') == 0 && dotsNumber == 0) {
            return getNumberWithoutComma(numberString, maxDecimal);
        }

        // 「.」で始まる場合、点が消えない & 文字が多くなりすぎないように返す
        if (numberString.indexOf('.') == 0) {
            return getNumberWithoutComma(numberString, maxDecimal);
        }

        // 「.」で終わる場合、打ったばかりであれば何もしないで返す
        if (numberString.charAt(lastCharPosition) == '.' && cursorPosition == lastCharPosition + 1) {
            return numberString;
        }

        BigDecimal bigDecimal = new BigDecimal(numberStringRaw);

        if (bigDecimal.doubleValue() == 0) {
            return numberStringRaw;
        }

        // ユーザが「.」の後に入れた桁数の数が変更されないように
        if (numberString.contains(".")) {
            minDecimal = numberString.length() - (numberString.indexOf(".") + 1);
        }

        return getDecimalFormat(minDecimal, maxDecimal).format(bigDecimal);
    }

    private static String getNumberWithoutComma(String numberString, int maxDecimal) {
        if (numberString.length() > maxDecimal - 1) {
            return numberString.replace(",", "").substring(0, maxDecimal);
        } else {
            return numberString.replace(",", "");
        }
    }

    private static int getDotsNumber(String amountString) {
        return amountString.length() - amountString.replace(".", "").length();
    }

    private static DecimalFormat getDecimalFormat(int minFractionDigits, int maxFractionDigits) {
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        decimalFormat.setMinimumFractionDigits(minFractionDigits);
        decimalFormat.setMaximumFractionDigits(maxFractionDigits);
        decimalFormat.setDecimalFormatSymbols(symbols);
        decimalFormat.setGroupingUsed(true);
        return decimalFormat;
    }

    private static DecimalFormat getDecimalFormat(int fractionDigits) {
        return getDecimalFormat(fractionDigits, fractionDigits);
    }
}
