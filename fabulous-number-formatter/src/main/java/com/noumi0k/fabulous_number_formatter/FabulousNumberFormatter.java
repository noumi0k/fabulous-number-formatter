package com.noumi0k.fabulous_number_formatter;

import android.text.TextWatcher;
import android.widget.EditText;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by noumi0k on 2017/12/09.
 */

public class FabulousNumberFormatter {

    private final static int MAX_DECIMAL = 8;

    public static String getRawDecimal(String numberString) {
        try {
            String stringWithoutComma = numberString.replaceAll(",", "");
            return new BigDecimal(stringWithoutComma).stripTrailingZeros().toPlainString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String keepSignificantForDecimalInput(int cursorPosition, String numberString) {
        int minDecimal = 0;
        int dotsCount = getDotsCount(numberString);
        int lastCharPosition = Math.max(0, numberString.length() - 1);

        // Make it impossible to press several "."
        if (dotsCount > 1) {
            return new StringBuilder(numberString).deleteCharAt(cursorPosition - 1).toString();
        }

        String numberStringRaw = getRawDecimal(numberString);

        // Return an empty string if it's not a number
        if (numberStringRaw == null || numberStringRaw.isEmpty()) {
            return "";
        }

        // Don't remove the "0" if it's added at the beginning
        if (numberString.indexOf('0') == 0 && dotsCount == 0) {
            return getNumberWithoutComma(numberString);
        }

        // Don't remove the "." if it's added at the beginning
        if (numberString.indexOf('.') == 0) {
            return getNumberWithoutComma(numberString);
        }

        // If a "." has just been pressed at the end of the string, do nothing
        if (numberString.charAt(lastCharPosition) == '.' && cursorPosition == lastCharPosition + 1) {
            return numberString;
        }

        BigDecimal bigDecimal = new BigDecimal(numberStringRaw);

        if (bigDecimal.doubleValue() == 0) {
            return numberStringRaw;
        }

        // Digits (even 0) after a "." should not be removed
        if (numberString.contains(".")) {
            minDecimal = numberString.length() - (numberString.indexOf(".") + 1);
        }

        return getDecimalFormat(minDecimal, MAX_DECIMAL).format(bigDecimal);
    }

    private static String getNumberWithoutComma(String numberString) {
        if (numberString.length() > MAX_DECIMAL - 1) {
            return numberString.replace(",", "").substring(0, MAX_DECIMAL);
        } else {
            return numberString.replace(",", "");
        }
    }

    private static int getDotsCount(String amountString) {
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

    public static void updateCommaSeparators(String amountString, EditText numberEditText, TextWatcher textWatcher) {
        int beforeCursor = numberEditText.getSelectionStart();
        int beforeSize = numberEditText.getText().length();
        numberEditText.removeTextChangedListener(textWatcher);
        numberEditText.setText(FabulousNumberFormatter.keepSignificantForDecimalInput(beforeCursor, amountString));
        numberEditText.addTextChangedListener(textWatcher);
        int afterSize = numberEditText.getText().length();
        int sizeDifference = afterSize - beforeSize;
        numberEditText.setSelection(Math.max(0, beforeCursor + sizeDifference));
        return;
    }
}
