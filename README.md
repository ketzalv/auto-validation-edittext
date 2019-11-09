

# Auto Validation EditText

[ ![Download](https://api.bintray.com/packages/ketzalv/android-utils/validationedittext/images/download.svg?version=1.0.2) ](https://bintray.com/ketzalv/android-utils/validationedittext/1.0.2/link)

Provides a custom component of Edittext, that facility create forms, and its validations, as require a little lines of code for use

### Features

* Validate in realtime the current regular expression
* Automatic configuration of field need, for example the correct keyboard
* Provides methods to validate in code
* Configurated to work with TextInputLayout or alone
* Can modify like Edittext with styles or attributes
* Provide a little personalization layout in specific fields for example in numberCurrency type the User Experience required the pattern $0,000.00 and show it


## How to Use

Gradle dependency:
```Groovy
implementation 'io.github.ketzalv:validationedittext:1.0.2'
```

In XML:

```XML
<com.google.android.material.textfield.TextInputLayout
    style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
    android:layout_marginTop="8dp"
    android:layout_marginEnd="8dp"
    android:layout_marginStart="8dp"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">
    <io.github.ketzalv.validationedittext.ValidationEditText
        android:id="@+id/edit_email"
        android:hint="Email"
        android:padding="12sp"
        android:textSize="14sp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:imeOptions="actionNext"
        app:autoValidate="true"
        app:showErrorMessage="true"
        app:format="email"/>
</com.google.android.material.textfield.TextInputLayout>
```

In Code:

```JAVA
ValidationEditText editIntNum = new ValidationEditText(getContext(), ValidationType.text);
        editIntNum.setAutoValidateEnable(true);
        editIntNum.setShowMessageError(true);
        editIntNum.setEmptyMessage("Empty Field");
        editIntNum.setErrorMessage("InvalidField");
        editIntNum.setCustomLocale(Locale.CANADA);
```

Provides a callback when the Field is valid in realtime

```JAVA
ValidationEditText editIntNum = new ValidationEditText(getContext(), ValidationType.text);
    editIntNum.setAutoValidate(new ValidationEditText.ValidatorListener() {
            @Override
            public void onValidEditText(String string) {
                //do something...
            }

            @Override
            public void onInvalidEditText() {

            }
        });
```
And you should look something like Figure 1:



<img src="images/figure_1.png" height="680">

Figure 1

Check the app Sample include in this repo to see a complex form and all implementations

Some Gifs of the app Sample

![](images/figure_2.gif)

Figure 2

![](images/figure_3.gif)

Figure 3



## Custom Attributes

The next table contains all information about of custom attributes with their description

| name  | type | description |
|---|---|---|
|  app:autoValidate | boolean  | This feature enable and disable the validate in real time |
|  app:showErrorMessage | boolean  | This feature enable and disable auto manage when the field show the error |
|  app:errorEmptyMessage | String  | This feature enable custom error messages in case of the field is empty |
|  app:errorMessage | String  | This feature enable custom error messages in case of the field is not empty |
|  app:regularExpression | String  | This feature match your current regular expression to validate the field |
|  app:minAmount | float  | This feature is only used in NumberCurreny type and NumberCurrencyRounded |
|  app:maxAmount | float  | This feature is only used in NumberCurreny type and NumberCurrencyRounded |
|  app:format | enum  | This feature configure the types that are supported. The types are: **email, password, phone, zipcode, text, number, cellphone, date, personName, numberCurrency, curp, numberCurrencyRounded** |


## Fields Supported

The next table contains all information about of fields supported and their descriptions!type

| type | default configuration | default validate | image example |
|---|---|---|---|
| **email** | InputType <ul><li>TYPE_CLASS_TEXT</li><li>TYPE_TEXT_VARIATION_EMAIL_ADDRESS</li></ul><br> Maxlines: 1 | <ul><li>Regular Expression: android.util.Patterns.**EMAIL_ADDRESS**</li><li>Check empty field</li></ul>|
| **password** | InputType <ul><li>TYPE_CLASS_TEXT</li><li>TYPE_TEXT_VARIATION_PASSWORD</li></ul><br> Maxlines: 1 | <ul><li>Check empty field</li></ul> |
| **phone** | InputType <ul><li>TYPE_CLASS_PHONE</li></ul><br> Maxlines: 1 | <ul><li>Regular Expression: ` Pattern.compile("^[0-9]{8}|[0-9]{10}$") ` </li><li>Check empty field</li></ul> |
| **zipcode** | InputType <ul><li>TYPE_CLASS_NUMBER</li></ul><br> Maxlines: 1 <br> MaxLength: 5 | <ul><li>Check empty field</li></ul> |
| **text** | InputType <ul><li>TYPE_CLASS_TEXT</li></ul><br> Maxlines: 1 | <ul><li>Check empty field</li></ul> |
| **number** | InputType <ul><li>TYPE_CLASS_NUMBER</li></ul><br> Maxlines: 1 | <ul><li>Check empty field</li></ul> |
| **cellphone** | InputType <ul><li>TYPE_CLASS_PHONE</li></ul><br> Maxlines: 1 <br> MaxLength: 10 | <ul><li>Regular Expression: ` Pattern.compile("^[0-9]{8}|[0-9]{10}$") ` </li><li>Check empty field</li></ul> |
| **date** | InputType <ul><li>TYPE_CLASS_DATETIME</li><li>TYPE_DATETIME_VARIATION_DATE</li></ul><br> Maxlines: 1 | <ul><li>Check empty field</li></ul> |
| **personName** | InputType <ul><li>TYPE_TEXT_FLAG_CAP_SENTENCES</li><li>TYPE_CLASS_TEXT</li></ul><br> Maxlines: 1 | <ul>li>Check empty field</li></ul> |
| **numberCurrency** | InputType <ul><li>TYPE_CLASS_NUMBER</li></ul><br> Maxlines: 1 | <ul><li>Check if is Valid mount</li><li>Check empty field</li></ul> |
| **curp** | InputType <ul><li>TYPE_CLASS_TEXT</li></ul><br> Maxlines: 1 <br> Maxlength: 18 <br> TextAllCaps: true| <ul><li>Check if length is 18</li><li>Check empty field</li></ul> |
| **numberCurrencyRounded** | InputType <ul><li>TYPE_CLASS_NUMBER</li></ul><br> Maxlines: 1 | <ul><li>Check if is Valid mount</li><li>Check empty field</li></ul> |