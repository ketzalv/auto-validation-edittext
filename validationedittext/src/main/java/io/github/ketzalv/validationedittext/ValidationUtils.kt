package io.github.ketzalv.validationedittext

import java.text.NumberFormat
import java.util.*

object ValidationUtils {
    fun cashFormat(locale: Locale?, number: Double): String {
        val format = NumberFormat.getCurrencyInstance(locale)
        return try {
            format.format(number)
        } catch (e: Exception) {
            number.toString()
        }
    }

    fun cashFormat(locale: Locale?, number: String): String {
        val format = NumberFormat.getCurrencyInstance(locale)
        return try {
            val cast = java.lang.Double.valueOf(number)
            format.format(cast)
        } catch (e: Exception) {
            number
        }
    }

    fun cashFormat(locale: Locale?, number: Int): String {
        val format = NumberFormat.getCurrencyInstance(locale)
        return try {
            format.format(number.toLong())
        } catch (e: Exception) {
            number.toString()
        }
    }

    fun cashFormat(locale: Locale?, number: Float): String {
        val format = NumberFormat.getCurrencyInstance(locale)
        return try {
            format.format(number.toDouble())
        } catch (e: Exception) {
            number.toString()
        }
    }

    fun parseCurrencyAmount(aMount: String): Double {
        return try {
            val cleanString = aMount.replace("[^\\d.]".toRegex(), "").replace("\\.".toRegex(), "")
            cleanString.toDouble()
        } catch (e: Exception) {
            0.0
        }
    }

    fun parseCurrencyAmountWithoutDecimal(aMount: String): Double {
        try {
            val output: Double
            val cleanString = aMount.replace("[^\\d.]".toRegex(), "")
            val split: List<String> = cleanString.split(".")
            if (split.size == 2) {
                var mount = split[0]
                val decimals = split[1]
                if (decimals.length == 1) {
                    mount = split[0].replaceFirst(".$".toRegex(), "")
                }
                val cleanDecimals = decimals.replaceFirst("0".toRegex(), "").replaceFirst("0".toRegex(), "")
                output = (mount + cleanDecimals).toDouble()
            } else {
                output = cleanString.toDouble()
            }
            return output
        } catch (e: Exception) {
            return 0.0
        }
    }

    fun parseCurrencyAmountString(aMount: String): String {
        return try {
            val doubleMount = parseCurrencyAmount(aMount) / 100
            doubleMount.toString()
        } catch (e: Exception) {
            aMount
        }
    }
}