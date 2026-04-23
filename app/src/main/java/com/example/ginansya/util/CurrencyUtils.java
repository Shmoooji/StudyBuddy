package com.example.ginansya.util;

import java.text.NumberFormat;
import java.util.Locale;

public final class CurrencyUtils {

    private static final NumberFormat CURRENCY =
            NumberFormat.getCurrencyInstance(Locale.US);
    private static final NumberFormat COMPACT =
            NumberFormat.getCurrencyInstance(Locale.US);

    static {
        CURRENCY.setMaximumFractionDigits(2);
        CURRENCY.setMinimumFractionDigits(0);
        COMPACT.setMaximumFractionDigits(0);
        COMPACT.setMinimumFractionDigits(0);
    }

    private CurrencyUtils() {}

    public static String format(double amount) {
        if (amount == Math.floor(amount)) {
            return COMPACT.format(amount);
        }
        return CURRENCY.format(amount);
    }

    public static String formatCompact(double amount) {
        if (Math.abs(amount) >= 1_000_000) {
            return "$" + round(amount / 1_000_000.0, 1) + "M";
        }
        if (Math.abs(amount) >= 1_000) {
            return "$" + round(amount / 1_000.0, 1) + "k";
        }
        return COMPACT.format(amount);
    }

    public static String formatSigned(double amount) {
        String base = format(Math.abs(amount));
        return amount >= 0 ? "+" + base : "−" + base;
    }

    private static String round(double v, int decimals) {
        double factor = Math.pow(10, decimals);
        double r = Math.round(v * factor) / factor;
        if (r == Math.floor(r)) return String.valueOf((long) r);
        return String.valueOf(r);
    }
}
