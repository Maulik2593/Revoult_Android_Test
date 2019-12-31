package com.revolut.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale


/**
 * Create formatted string from float value
 * Locale safe
 */
fun Float.formatToString() : String = String.format(Locale.getDefault(), "%.2f", this)

/**
 * Returns a float from string, Locale safe
 */
fun String.toFloat(): Float = if (isNullOrBlank()) {
    0F
} else {
    DecimalFormat("0.#", DecimalFormatSymbols.getInstance(Locale.getDefault())).parse(this).toFloat()
}
