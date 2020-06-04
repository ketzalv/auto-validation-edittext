package io.github.ketzalv.validationedittext

import android.util.Patterns
import java.util.regex.Pattern

object ValidateFormsUtils {
    private val emptyString = Pattern.compile("^(?=\\s*\\S).*$")
    private val ptVisa = Pattern.compile("^4[0-9]{6,}$")
    private val ptMasterCard = Pattern.compile("^5[1-5][0-9]{5,}$")
    private val ptAmeExp = Pattern.compile("^3[47][0-9]{5,}$")
    private val ptDinClb = Pattern.compile("^3(?:0[0-5]|[68][0-9])[0-9]{4,}$")
    private val ptDiscover = Pattern.compile("^6(?:011|5[0-9]{2})[0-9]{3,}$")
    private val ptJcb = Pattern.compile("^(?:2131|1800|35[0-9]{3})[0-9]{3,}$")
    private val genericCreditCard = Pattern.compile("^[0-9]{16}$")
    private val cablePtrn = Pattern.compile("^[0-9]{18}$")
    private val phonePattern = Pattern.compile("^[0-9]{8}|[0-9]{10}$")
    private val cellPhonePattern = Pattern.compile("^[0-9]{10}$")
    private val number = Pattern.compile("[0-9]+")

    /**
     * ^           # Assert position at the beginning of the string.
     * [0-9]{5}    # Match a digit, exactly five times.
     * (?:         # Group but don't capture:
     * -         #   Match a literal "-".
     * [0-9]{4}  #   Match a digit, exactly four times.
     * )           # End the non-capturing group.
     * ?         #   Make the group optional.
     * $           # Assert position at the end of the string.
     */
    private val zipCodePattern = Pattern.compile("^[0-9]{5}$")

    /*
   *   (			# Start of group
       (?=.*\d)		#   must contains one digit from 0-9
       (?=.*[a-z])		#   must contains one lowercase characters
       (?=.*[A-Z])		#   must contains one uppercase characters
             .		    #     match anything with previous condition checking
               {6,20}	#        length at least 6 characters and maximum of 20
           )			# End of group
   * */
    private val passwprdPattern = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})")
    private val emailPattern = Patterns.EMAIL_ADDRESS //Pattern.compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");
    @JvmStatic
    fun isValidEmailAddress(email: String?): Boolean {
        return emailPattern.matcher(email).matches()
    }

    fun isValidPassword(pass: String): Boolean {
        return !pass.isEmpty() && pass.length >= 6 //passwprdPattern.matcher(pass).matches();
    }

    @JvmStatic
    fun isValidPhone(phone: String?): Boolean {
        return phonePattern.matcher(phone).matches()
    }

    fun isValidCellPhone(cellPhone: String?): Boolean {
        return cellPhonePattern.matcher(cellPhone).matches()
    }

    fun isValidNumber(compare: String?): Boolean {
        return number.matcher(compare).matches()
    }

    fun isValidZipCode(zipCode: String?): Boolean {
        return zipCodePattern.matcher(zipCode).matches()
    }

    fun isValidCreditCard(card: String?): Boolean {
        return genericCreditCard.matcher(card).matches()
    }

    fun isValidCABLE(cable: String?): Boolean {
        return cablePtrn.matcher(cable).matches()
    }
}