package io.github.ketzalv.validationedittext;

public enum ValidationType {

    defaulttype(-11),
    email(0),
    password(1),
    phone(2),
    zipcode(3),
    text(4),
    number(5),
    cellphone(6),
    date(7),
    personName(8),
    numberCurrency(9),
    curp(10),
    numberCurrencyRounded(11);

    public int id;

    ValidationType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ValidationType fromId(int id){
        try{
            for(ValidationType b: ValidationType.values()){
                if(b.getId() == id){
                    return b;
                }
            }
        }catch (Exception e){
            return defaulttype;
        }
        return defaulttype;
    }
}
