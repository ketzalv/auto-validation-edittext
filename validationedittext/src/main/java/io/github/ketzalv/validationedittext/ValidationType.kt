package io.github.ketzalv.validationedittext

enum class ValidationType(var id: Int) {
    defaulttype(-11), email(0), password(1), phone(2), zipcode(3), text(4), number(5), cellphone(6), date(7), personName(8), numberCurrency(9), curp(10), numberCurrencyRounded(11);

    companion object {
        fun fromId(id: Int): ValidationType {
            try {
                for (b in values()) {
                    if (b.id == id) {
                        return b
                    }
                }
            } catch (e: Exception) {
                return defaulttype
            }
            return defaulttype
        }
    }

}